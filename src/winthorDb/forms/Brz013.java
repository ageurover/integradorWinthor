/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.Cursor;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.ContaPagar;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz013 extends javax.swing.JFrame {

    List contasPagar = new ArrayList();
    String nomeFornecedor = "";

    /**
     * Creates new form Brz001
     */
    public Brz013() {
        initComponents();
        initGuid();
    }

    private void initGuid() {
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        edtDataLanc.setDate(new Date());
        edtDataVenc.setDate(new Date());
        btnGravarContasPagar.setEnabled(false);
    }

    private void abreArquivo() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        try {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(true);

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                edtFileName.setText(selectedFile.getCanonicalPath());

                if (!edtFileName.getText().isEmpty()) {
                    try (FileReader reader = new FileReader(edtFileName.getText())) {
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line, seg, cnpjFilial, filial[] = new String[6];
                        getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        while ((line = bufferedReader.readLine()) != null) {
                            //System.out.println(line);
                            seg = line.substring(13, 14);
                            
                            if (seg.equalsIgnoreCase("0")) {
                                cnpjFilial = line.substring(18, 32);
                                filial = buscaFilial(cnpjFilial);
                                if (!filial[0].equalsIgnoreCase(edtCodFilial.getText())) {
                                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                    MessageDialog.error("Filial informada é diferente do arquivo a ser processado!");
                                    reader.close();
                                    return;
                                }
                                
                            }
                            if (seg.equalsIgnoreCase("A")) {
                                nomeFornecedor = line.substring(43, 73);
                            }
                            if (seg.equalsIgnoreCase("B")) {
                                processaLinha(line);
                                nomeFornecedor = "";
                            }
                        }
                    }
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (!contasPagar.isEmpty()) {
                        btnGravarContasPagar.setEnabled(true);
                        for (Object cp : contasPagar) {
                            edtLog.append(cp.toString() + "\n");
                        }
                        if (!trataErro.lstErros.isEmpty()) {
                            trataErro.mostraListaErros();
                            btnGravarContasPagar.setEnabled(false);
                        }
                    }
                } else {
                    MessageDialog.error("Arquivo de Remessa não informado !");
                }
            }
        } catch (HeadlessException | IOException e) {
            trataErro.trataException(e, "processaArquivo");
        }

    }

    private void processaLinha(String linha) {
        try {
            String seg = "", tipoF = "0", cpf = "", cnpj = "";
            String fornecedor[] = new String[2];
            String number[] = new String[3];

            ContaPagar cp = null;
            if (linha.length() == 240) {
                // verifica se a linha é do Seguimento B do Cnab 240
                seg = linha.substring(13, 14);
                if (seg.equalsIgnoreCase("B")) {
                    cp = new ContaPagar();
                    cp.setCodFilial(edtCodFilial.getText());
                    cp.setCodConta(edtContaContabil.getText());
                    cp.setDtEmissao(Formato.dateToStr(edtDataLanc.getDate()));
                    cp.setDtLanc(Formato.dateToStr(edtDataLanc.getDate()));
                    cp.setDtVenc(Formato.dateToStr(edtDataVenc.getDate()));
                    cp.setDtCompetencia(Formato.dateToStr(edtDataLanc.getDate()));
                    cp.setHistorico(edtHistorico.getText());
                    cp.setTipoParceiro("F");
                    tipoF = linha.substring(17, 18);
                    cpf = linha.substring(21, 32);
                    cnpj = linha.substring(18, 32);
                    if (tipoF.equalsIgnoreCase("1")) {
                        fornecedor = buscaFornecedor("F", cpf);
                    } else {
                        fornecedor = buscaFornecedor("F", cnpj);
                    }
                    if (fornecedor[0] != null) {
                        cp.setCodFornec(fornecedor[0]);
                        cp.setFornecedor(fornecedor[1]);
                    } else {
                        cp.setTipoParceiro("O");
                        cp.setCodFornec("");
                        cp.setFornecedor(nomeFornecedor);
                        trataErro.addListaErros("Nao Localizado -> " + linha.substring(18, 32) + " - " + nomeFornecedor);
                    }
                    cp.setNumNota("0");
                    cp.setDuplic("0");
                    cp.setValor(linha.substring(135, 148) + "." + linha.substring(148, 150));
                    number = buscaNumerador();
                    cp.setRecNum(number[0]);
                    cp.setNumTrans("null");
                    cp.setNumTransent("null");

                    contasPagar.add(cp);
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "processaLinha");
        }
    }

    private String[] buscaFornecedor(String tipoFornec, String cpfCnpj) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        List dados = new ArrayList();
        List fornec = new ArrayList();
        String ret[] = new String[2];
        try {
            String strSelect = "";

            switch (tipoFornec) {
                case "F":
                    strSelect = "Select CodFornec, Fornecedor from pcfornec where replace(replace(replace(CGC,'-',''),'.',''),'/','') = replace(replace(replace('" + cpfCnpj + "' ,'-',''),'.',''),'/','')";
                    break;
                case "O":
                    strSelect = " ";
                    break;
                default:
                    strSelect = " ";
            }

            wint.openConectOracle();
            dados = wint.selectDados(strSelect);
            if (!dados.isEmpty()) {
                fornec = (List) dados.get(0);
                ret[0] = fornec.get(0).toString().replace("[", "").replace("]", "").trim();
                ret[1] = fornec.get(1).toString().replace("[", "").replace("]", "").trim();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaFornecedor");
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private String[] buscaNumerador() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        List number = new ArrayList();
        List dados = new ArrayList();
        String ret[] = new String[3];
        String strSelect = "";
        try {
            strSelect = "Select PROXNUMLANC+1 from PCCONSUM ";
            wint.openConectOracle();
            dados = wint.selectDados(strSelect);
            if (!dados.isEmpty()) {
                number = (List) dados.get(0);
                ret[0] = number.get(0).toString().replace("[", "").replace("]", "").trim();
                // ret[1] = number.get(1).toString().replace("[", "").replace("]", "").trim();
                // ret[2] = number.get(2).toString().replace("[", "").replace("]", "").trim();

                wint.updateDados("UPDATE PCCONSUM SET PROXNUMLANC = " + ret[0] );
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaNumerador");
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private String[] buscaFilial(String cnpj) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        List dados = new ArrayList();
        List filial = new ArrayList();
        String ret[] = new String[6];
        try {
            String strSelect = "";

            strSelect = "Select Codigo,CGC, RazaoSocial, cidade, uf from pcfilial "
                    + " where replace(replace(replace(CGC,'-',''),'.',''),'/','') = replace(replace(replace('" + cnpj + "' ,'-',''),'.',''),'/','')";

            wint.openConectOracle();
            dados = wint.selectDados(strSelect);
            if (!dados.isEmpty()) {
                filial = (List) dados.get(0);
                ret[0] = filial.get(0).toString().replace("[", "").replace("]", "").trim(); // codigo
                ret[1] = filial.get(1).toString().replace("[", "").replace("]", "").trim(); // cgc
                ret[2] = filial.get(2).toString().replace("[", "").replace("]", "").trim(); // razaosocial
                ret[3] = filial.get(3).toString().replace("[", "").replace("]", "").trim(); // cidade
                ret[4] = filial.get(4).toString().replace("[", "").replace("]", "").trim(); // uf
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaFilial");
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private void gravarContasPagar() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        int exec = 0;
        String strInsert = "";
        try {
            for (Iterator it = contasPagar.iterator(); it.hasNext();) {
                ContaPagar cp = (ContaPagar) it.next();
                strInsert = "INSERT INTO PCLANC( CODFILIAL, INDICE, RECNUM, NUMTRANSENT, NUMTRANS,"
                        + " CODCONTA, TIPOPARCEIRO, CODFORNEC, fornecedor, DTEMISSAO, DTLANC, DTVENC, dtCompetencia, \n"
                        + " NUMNOTA, DUPLIC, VALOR, HISTORICO, moeda, tipoLanc, tipoPagto, FORMAPGTO, \n"
                        + " tipoServico, nfServico, codComprador, utilizouRateioconta, prcRateioUtilizado ) VALUES ("
                        + "   '" + cp.getCodFilial() + "'"
                        + " , '" + cp.getIndice() + "'"
                        + " ,  " + cp.getRecNum()
                        + " ,  null " // + cp.getNumTransent()
                        + " ,  null " // + cp.getNumTrans() + "\n"
                        + " ,  " + cp.getCodConta()
                        + " , '" + cp.getTipoParceiro() + "' "
                        + " ,  " + cp.getCodFornec()
                        + " , '" + Formato.strTiraIncompativeisBd(cp.getFornecedor()) + "'"
                        + " , '" + cp.getDtEmissao() + "'"
                        + " , '" + cp.getDtLanc() + "'"
                        + " , '" + cp.getDtVenc() + "'"
                        + " , '" + cp.getDtCompetencia() + "'\n"
                        + " ,  0 " // + cp.getNumNota()
                        + " , '" + cp.getDuplic() + "'"
                        + " ,  " + cp.getValor()
                        + " , '" + Formato.strTiraIncompativeisBd(cp.getHistorico()) + "'"
                        + " , '" + cp.getMoeda() + "'"
                        + " , '" + cp.getTipoLanc() + "'"
                        + " , '" + cp.getTipoPagto() + "' \n"
                        + " ,  " + cp.getFormaPagto()
                        + " , '" + cp.getTipoServico() + "'"
                        + " , '" + cp.getNfServico() + "'"
                        + " , '" + cp.getCodComprador() + "'"
                        + " , '" + cp.getUtilizouRateioconta() + "'"
                        + " , '" + cp.getPrcRateioUtilizado() + "'"
                        + " )";

                wint.openConectOracle();
                exec = wint.insertDados(strInsert);
                if (exec == 0) {
                    MessageDialog.error("Erro ao gravar registro no contas a pagar!");
                    trataErro.addListaErros(strInsert);
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "gravarContasPagar");
        } finally {
            wint.closeConectOracle();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        edtContaContabil = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edtDataLanc = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        edtDataVenc = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        edtHistorico = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        edtFileName = new javax.swing.JTextField();
        btnBuscaArquivo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtLog = new javax.swing.JTextArea();
        btnGravarContasPagar = new javax.swing.JButton();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N

        jLabel1.setText("Importar Lançamentos Folha de Pagamento");

        jLabel3.setText("Faz a importação para o wiinthor dos Lançamentos da Folha de Pagamento com modelo CNAB-240");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3))
        );

        jLabel4.setText("Conta Contabil:");

        edtContaContabil.setText("600015");

        jLabel5.setText("Codigo Filial:");

        edtCodFilial.setText("1");
        edtCodFilial.setToolTipText("");

        jLabel6.setText("Data Lançamento:");

        jLabel7.setText("Data Vencimento:");

        jLabel8.setText("Historico:");

        edtHistorico.setText("SALARIO REF - ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edtHistorico))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(edtContaContabil, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                    .addComponent(edtCodFilial)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edtDataLanc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edtDataVenc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 509, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtContaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(edtDataLanc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(edtDataVenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edtHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(216, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Parametros", jPanel4);

        jLabel9.setText("Arquivo:");

        edtFileName.setEditable(false);

        btnBuscaArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/folder.png"))); // NOI18N
        btnBuscaArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaArquivoActionPerformed(evt);
            }
        });

        edtLog.setColumns(20);
        edtLog.setRows(5);
        jScrollPane1.setViewportView(edtLog);

        btnGravarContasPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/diskette.png"))); // NOI18N
        btnGravarContasPagar.setToolTipText("Gravar contas a pagar");
        btnGravarContasPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarContasPagarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtFileName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscaArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGravarContasPagar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBuscaArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(edtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGravarContasPagar))
        );

        jTabbedPane1.addTab("Importação", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnBuscaArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaArquivoActionPerformed
        try {
            abreArquivo();
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnBuscaArquivoActionPerformed

    private void btnGravarContasPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarContasPagarActionPerformed
        // TODO add your handling code here:
        try {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            gravarContasPagar();
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (!trataErro.lstErros.isEmpty()) {
                trataErro.mostraListaErros();
            } else {
                MessageDialog.saveSucess();
                contasPagar=null;
                edtLog.setText("");
                btnGravarContasPagar.setEnabled(false);
                        
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnGravarContasPagarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            trataErro.trataException(ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz013().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscaArquivo;
    private javax.swing.JButton btnGravarContasPagar;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtContaContabil;
    private com.toedter.calendar.JDateChooser edtDataLanc;
    private com.toedter.calendar.JDateChooser edtDataVenc;
    private javax.swing.JTextField edtFileName;
    private javax.swing.JTextField edtHistorico;
    private javax.swing.JTextArea edtLog;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
