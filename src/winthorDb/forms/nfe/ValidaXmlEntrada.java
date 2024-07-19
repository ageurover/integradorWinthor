/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms.nfe;

import br.inf.portalfiscal.nfe.TNFe;
import br.inf.portalfiscal.nfe.TNFe.InfNFe.Det;
import br.inf.portalfiscal.nfe.TNfeProc;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.bean.BeanItemNotaEntrada;
import winthorDb.jpa.connection.SingleConnection;
import winthorDb.jpa.dao.DaoItemNotaEntrada;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class ValidaXmlEntrada extends javax.swing.JFrame {

    private final DefaultListModel lista = new DefaultListModel();
    private final ArrayList ListFile = new ArrayList();
    private final IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
    boolean cancel = false;

    /**
     * Creates new form Brz001
     */
    public ValidaXmlEntrada() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        limpaTela();
    }

    /**
     * List all the files under a directory
     *
     * @param directoryName to be listed
     */
    public void listFiles(String directoryName) {

        File directory = new File(directoryName);
        String nameFile = "";
        //lstArquivos.setModel(null);
        lista.clear();
        ListFile.clear();
        //get all the files from a directory

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                nameFile = file.getName();
                if ((!nameFile.contains("(")) || (!nameFile.contains(")"))) {
                    if (nameFile.toLowerCase().endsWith(".xml")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                } else {
                    MessageDialog.error("Não Localizados os XML's na pasta!");
                    txtLog.append("\n >> Nao Processar: " + nameFile);
                }
            }
        }
        lstArquivos.setModel(lista);
        txtLog.append("\n ");
        btnNovo.setEnabled(true);
    }

    private void processaXmlWinthor() {

        String fileXml = "";
        TNFe nfe = null;
        TNfeProc nfeProc = null;
        Object obj = null;
        List<BeanItemNotaEntrada> nota_itens = null;
        List itens_xml = null;
        int i = 0;
        int resetCon = 0;
        int total = 0;
        int gravou = 0;
        int naogravou = 0;
        NFeXMLImporter importer = new NFeXMLImporter();
        DaoItemNotaEntrada nota = new DaoItemNotaEntrada();
        try {
            total = ListFile.size();
            txtLog.append("Arquivos a serem processados: " + total + "\n");
            lblProcessados.setText("Gravou: " + gravou + " / Não Gravou: " + naogravou);
            jProgressBar1.setMaximum(0);
            jProgressBar1.setMaximum(total);
            for (Object File1 : ListFile) {

                if (cancel) {
                    MessageDialog.info("Operação Cancelada pelo usuario!");
                    break;
                }
                jProgressBar1.setValue(i);

                fileXml = edtArquivoOrigem.getText() + "\\" + File1.toString();
                txtLog.append("\n" + Formato.replicate("-", 50));
                txtLog.append("\nXML-> " + i++ + " >> " + fileXml);
                lstArquivos.setSelectedIndex(i);
                lstArquivos.ensureIndexIsVisible(lstArquivos.getSelectedIndex());

                nfe = null;
                nfeProc = null;
                obj = null;
                resetCon++;
                if (!fileXml.isEmpty()) {
                    obj = importer.importarNfeProcXML(fileXml);
                    if (obj == null) {
                        txtLog.append("\n<<< Objeto XML invalido ou nullo >>> \n\n");
                    } else {
                        nfeProc = (TNfeProc) obj;
                        if (nfeProc == null) {
                            txtLog.append("\n<<< Objeto XML nota processada invalido ou nullo >>> \n\n");
                        } else {
                            nfe = nfeProc.getNFe();
                            if (nfe != null) {
                                txtLog.append("\n >>> Nota Fiscal Nr : " + nfe.getInfNFe().getIde().getNNF() + " Serie: " + nfe.getInfNFe().getIde().getSerie() + " Data: " + nfe.getInfNFe().getIde().getDhSaiEnt());
                                itens_xml = nfe.getInfNFe().getDet();

                                // 1 - localizar dados do xml no winthor                                
                                nota_itens = nota.listarItemNota(edtCodFilial.getText(), nfe.getInfNFe().getIde().getNNF(), nfe.getInfNFe().getEmit().getCNPJ());

                                // 2 - fazer a alteração da sequencia dos itens
                                gerarSequencia(nota_itens, itens_xml, mesmaRaiz(nfe.getInfNFe().getEmit().getCNPJ(), nfe.getInfNFe().getDest().getCNPJ()),nfe);
                                // 3 - gravar sequencia no winthor
                                if (nota.atualizar(nota_itens)) {
                                    gravou++;
                                    txtLog.append("\n >>> Nota Fiscal - " + nfe.getInfNFe().getIde().getNNF() + " >>> Atualizada com sucesso no winthor! <<<\n\n");
                                } else {
                                    naogravou++;
                                    txtLog.append("\n >>> Nota Fiscal - " + nfe.getInfNFe().getIde().getNNF() + " >>> Atualizada com sucesso no winthor! <<<\n\n");
                                }

                            } else {
                                naogravou++;
                                if (nfeProc != null) {
                                    txtLog.append("\n<<< XML da nota não processado >>> " + nfeProc.getProtNFe().getInfProt().getChNFe() + "\n\n");
                                } else {
                                    txtLog.append("\n<<< XML da nota não processado ou nulo >>> \n\n");
                                }

                            }
                        }
                    }
                } else {
                    txtLog.append("\n<<< Arquivo XML invalido! >>> ");
                }
                lblProcessados.setText("Gravou: " + gravou + " / Não Gravou: " + naogravou);
                lblContador.setText("" + i + " / " + total);
                txtLog.setCaretPosition(txtLog.getText().length());
                if (resetCon >= 10) {
                    resetCon = 1;
                    nota = null;
                    SingleConnection.reConnection();
                    nota = new DaoItemNotaEntrada();
                }
            }
            MessageDialog.info("Processamento concluido!");

        } catch (Exception e) {
            trataErro.trataException(e, "processaXmlWinthor");
        } finally {
            // salva o log
            SaveTextArea();

            // limpa os objetos da memoria
            nfe = null;
            nfeProc = null;
            obj = null;
            nota_itens = null;
            itens_xml = null;
            importer = null;
            nota = null;
            btnNovo.setEnabled(true);
        }

    }

    private boolean mesmaRaiz(String cnpjEmit, String cnpjDest) {
        boolean ret = cnpjEmit.substring(0, 8).equalsIgnoreCase(cnpjDest.substring(0, 8));
        txtLog.append("\nCnpj Emitente: " + cnpjEmit + " \t Cnp Destinatario:" + cnpjDest + " << Matriz/Filial= " + ret);
        return ret;
    }

    public void gerarSequencia(List itens_nota, List itens_xml, boolean transferencia, TNFe nfe) {
        boolean achou = false;
        boolean imprimeItensNota = false;
        Det det = null;
        BeanItemNotaEntrada item = null;
        String cProd = "";
        String cEAN = "";
        String nItem = "";
        String codprod = "";
        String codauxiliar = "";
        String codauxiliar2 = "";
        String seqEnt = "";
        String codfabrica = "";
        String codfab = "";
        String codfab253 = "";
        try {
            if ((itens_xml != null) && (itens_nota != null)) {
                imprimeItensNota = false;
                // passa por cada item do xml para deixar a sequencia;
                txtLog.append("\n >>> Processando os itens do xml <<<");
                txtLog.append("\n >>> Produtos no XML: " + itens_xml.size());
                txtLog.append("\n >>> Produtos na Nota: " + itens_nota.size());
                txtLog.append("\n XML_Prod....... \t XML_EAN........ \t CodProd. \t codauxiliar.... \t CobFab_P..... \t CodFab_M...... \t CodFab_253.... \t Seq_Ent \t Seq_Xml. \tAchou");
                for (int i = 0; i < itens_xml.size(); i++) {
                    det = (Det) itens_xml.get(i);
                    cProd = det.getProd().getCProd().trim();
                    cEAN = det.getProd().getCEAN().trim();
                    nItem = det.getNItem().trim();
                    achou = false;

                    // localiza o produto na lista da nota pelo codfabrica
                    for (int j = 0; j < itens_nota.size(); j++) {

                        if (!achou) {
                            item = (BeanItemNotaEntrada) itens_nota.get(j);
                            codprod = item.getCodprod().toString();
                            codauxiliar = item.getCodauxiliar().trim();
                            codauxiliar2 = item.getCodauxiliar2().trim();
                            codfab = item.getCodfab().trim();
                            codfabrica = item.getCodfabrica().trim();
                            codfab253 = item.getCodfab253().trim();
                            seqEnt = item.getNumseqent().toString();
                            
                            if (transferencia) {
                                if (cProd.equalsIgnoreCase(codprod)) {
                                    item.setNovonumseq(Long.valueOf(nItem));
                                    achou = true;
                                }
                            } else {
                                if ((!achou)
                                        && (codfabrica.equalsIgnoreCase(cProd))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }
                                if ((!achou)
                                        && (codfab.equalsIgnoreCase(cProd))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }
                                if ((!achou)
                                        && (codfab253.equalsIgnoreCase(cProd))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }

                                // transforma tudo em numero para buscar numeto semelhante
                                if ((!achou)
                                        && (Formato.somenteNumeros(codfab253).equalsIgnoreCase(Formato.somenteNumeros(cProd)))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }

                                // transforma tudo em numero inteiro quando a cadeia numerica for até 9 digitos para buscar numeto semelhante
                                if ((!achou)
                                        && (Formato.somenteNumeros(codfab253).length() > 0) && (Formato.somenteNumeros(codfab253).length() <= 9)
                                        && (Formato.somenteNumeros(cProd).length() > 0) && (Formato.somenteNumeros(cProd).length() <= 9)) {

                                    if (Formato.strToLong(Formato.somenteNumeros(codfab253)).longValue() == Formato.strToLong(Formato.somenteNumeros(cProd)).longValue()) {
                                        achou = true;
                                        item.setNovonumseq(Long.valueOf(nItem));
                                    }
                                }

                                if ((!achou)
                                        && (codauxiliar.equalsIgnoreCase(cEAN))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }
                                if ((!achou)
                                        && (codauxiliar2.equalsIgnoreCase(cEAN))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }
                                if ((!achou)
                                        && (cProd.equalsIgnoreCase(codprod))) {
                                    achou = true;
                                    item.setNovonumseq(Long.valueOf(nItem));
                                }
                            }
                        } else {
                            txtLog.append("\n " + Formato.strTamanhoExato(cProd, 15)
                                    + " \t" + Formato.strTamanhoExato(cEAN, 15)
                                    + " \t" + Formato.strTamanhoExato(codprod, 10)
                                    + " \t" + Formato.strTamanhoExato(codauxiliar, 15)
                                    + " \t" + Formato.strTamanhoExato(codfab, 15)
                                    + " \t" + Formato.strTamanhoExato(codfabrica, 15)
                                    + " \t" + Formato.strTamanhoExato(codfab253, 15)
                                    + " \t" + Formato.strTamanhoExato(seqEnt, 7)
                                    + " \t" + Formato.strTamanhoExato(nItem, 7)
                                    + " \t" + achou);
                            break;
                        }
                    } // fim for item nota winthor
                    if (!achou) {
                        imprimeItensNota = true;
                        txtLog.append("\n " + Formato.strTamanhoExato(cProd, 15)
                                + " \t" + Formato.strTamanhoExato(cEAN, 15)
                                + "\t não localizdo na Nota fiscal do sistema!");
                        
                        txtLogErro.append("\n" + nfe.getInfNFe().getIde().getNNF()
                                + ";" + nfe.getInfNFe().getIde().getSerie()
                                + ";" + nfe.getInfNFe().getIde().getMod()
                                + ";" + nfe.getInfNFe().getEmit().getCNPJ()
                                + ";" + nfe.getInfNFe().getId()
                                + ";" + cProd
                                + ";" + cEAN
                                + ";" + nItem
                                );
                    }
                } // fim for item xml

                if (imprimeItensNota) {
                    txtLog.append("\n <<< ITENS DA NOTA NO WINTHOR >>> \n");
                    for (int j = 0; j < itens_nota.size(); j++) {
                        item = (BeanItemNotaEntrada) itens_nota.get(j);
                        codprod = item.getCodprod().toString();
                        codauxiliar = item.getCodauxiliar();
                        codfab = item.getCodfab();
                        codfabrica = item.getCodfabrica();
                        codfab253 = item.getCodfab253();
                        txtLog.append("\n " + Formato.strTamanhoExato("...", 10)
                                + " \t" + Formato.strTamanhoExato("...", 15)
                                + " \t" + Formato.strTamanhoExato(codprod, 10)
                                + " \t" + Formato.strTamanhoExato(codauxiliar, 15)
                                + " \t" + Formato.strTamanhoExato(codfab, 15)
                                + " \t" + Formato.strTamanhoExato(codfabrica, 15)
                                + " \t" + Formato.strTamanhoExato(codfab253, 15)
                                + " \t" + Formato.strTamanhoExato(seqEnt, 7)
                                + " \t" + Formato.strTamanhoExato("...", 7)
                                + " \t...");

                    }
                }
            } else {
                txtLog.append("\n >>> Erro ao Processar os itens do xml ou nota (invalidos ou não existem)<<<");
            }
        } catch (NumberFormatException e) {
            trataErro.trataException(e, "gerarSequencia");
        } finally {
            det = null;
            item = null;
        }
    }

    public void SaveTextArea() {
        try {

            BufferedWriter outFile = new BufferedWriter(new FileWriter(edtArquivoOrigem.getText() + "\\log_Geral_importacao_" + Formato.dateTimeNowToStr("MMMyyyy_HHmmss") + ".txt"));
            outFile.write(txtLog.getText()); //put in textfile
            outFile.flush();
            outFile.close();
            
            BufferedWriter outFileEr = new BufferedWriter(new FileWriter(edtArquivoOrigem.getText() + "\\log_Erro_importacao_" + Formato.dateTimeNowToStr("MMMyyyy_HHmmss") + ".csv"));
            outFileEr.write(txtLogErro.getText()); //put in textfile
            outFileEr.flush();
            outFileEr.close();
        } catch (IOException ex) {
            trataErro.trataException(ex, "Salvar Log");
        }

    }

    private void limpaTela() {
        btnProcessar1.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnAbrirOrigem.setEnabled(true);
        btnNovo.setEnabled(true);
        edtArquivoOrigem.setText("");
        txtLog.setText("");
        txtLogErro.setText("");
        lstArquivos.removeAll();
        lstArquivos.updateUI();
        lblContador.setText("...");
        lblProcessados.setText("...");
        jProgressBar1.setValue(0);
        jProgressBar1.updateUI();
        txtLogErro.append("\nNumNota;Serie;Modelo;CnpjEmit;ChaveXml;Produto,Ean,Seq_xml\n");
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
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        btnAbrirOrigem = new javax.swing.JButton();
        edtArquivoOrigem = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstArquivos = new javax.swing.JList();
        lblContador = new javax.swing.JLabel();
        lblProcessados = new javax.swing.JLabel();
        btnProcessar1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLogErro = new javax.swing.JTextArea();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N

        jLabel1.setText("XML Entrada de Mercadorias");

        jLabel3.setText("-> Organização da sequencia do XML no winthor");

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("XML's"));

        jLabel5.setText("Filial:");

        edtCodFilial.setText("1");

        btnAbrirOrigem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/folder.png"))); // NOI18N
        btnAbrirOrigem.setToolTipText("Arquivos XML de origem");
        btnAbrirOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirOrigemActionPerformed(evt);
            }
        });

        edtArquivoOrigem.setText("...");

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/close.png"))); // NOI18N
        btnCancelar.setToolTipText("Cancelar a operação");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/file.png"))); // NOI18N
        btnNovo.setToolTipText("Grava sequencia do XML no banco de dados");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        txtLog.setColumns(20);
        txtLog.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtLog.setRows(5);
        txtLog.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtLog);

        lstArquivos.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquivos"));
        lstArquivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstArquivosMouseClicked(evt);
            }
        });
        lstArquivos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstArquivosValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstArquivos);

        lblContador.setText("..");

        lblProcessados.setText("...");

        btnProcessar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/diskette.png"))); // NOI18N
        btnProcessar1.setToolTipText("Grava sequencia do XML no banco de dados");
        btnProcessar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessar1ActionPerformed(evt);
            }
        });

        txtLogErro.setColumns(20);
        txtLogErro.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtLogErro.setRows(5);
        txtLogErro.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtLogErro);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblContador, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblProcessados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnNovo)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAbrirOrigem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtArquivoOrigem)
                        .addGap(18, 18, 18)
                        .addComponent(btnProcessar1))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addComponent(edtArquivoOrigem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbrirOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcessar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContador, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProcessados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnAbrirOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirOrigemActionPerformed
        limpaTela();
        JFileChooser fileChooser = new JFileChooser();
        try {

            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                edtArquivoOrigem.setText(selectedFile.getCanonicalPath());
                listFiles(selectedFile.getCanonicalPath());
                if (!ListFile.isEmpty()) {
                    lstArquivos.setSelectedIndex(0);
                    btnProcessar1.setEnabled(true);
                    btnCancelar.setEnabled(true);
                }
            }
        } catch (HeadlessException | IOException e) {
            trataErro.trataException(e, "btnAbrirOrigemActionPerformed");
        } finally {
            lstArquivos.updateUI();
        }

    }//GEN-LAST:event_btnAbrirOrigemActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.cancel = true;
        btnNovo.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                try {
                    limpaTela();
                } catch (Exception ex) {
                    trataErro.trataException(ex, "btnNovoActionPerformed");
                }
            }//- Fim do Run
        }.start();//Fim Thread        

    }//GEN-LAST:event_btnNovoActionPerformed

    private void lstArquivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstArquivosMouseClicked

    }//GEN-LAST:event_lstArquivosMouseClicked

    private void lstArquivosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstArquivosValueChanged

    }//GEN-LAST:event_lstArquivosValueChanged

    private void btnProcessar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessar1ActionPerformed
        // TODO add your handling code here:
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                try {
                    btnNovo.setEnabled(false);
                    btnAbrirOrigem.setEnabled(false);
                    processaXmlWinthor();
                } catch (Exception ex) {
                    trataErro.trataException(ex, "btnNovoActionPerformed");
                }
            }//- Fim do Run
        }.start();//Fim Thread   
    }//GEN-LAST:event_btnProcessar1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> {
            new ValidaXmlEntrada().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirOrigem;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnProcessar1;
    private javax.swing.JTextField edtArquivoOrigem;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblContador;
    private javax.swing.JLabel lblProcessados;
    private javax.swing.JList lstArquivos;
    private javax.swing.JTextArea txtLog;
    private javax.swing.JTextArea txtLogErro;
    // End of variables declaration//GEN-END:variables
}
