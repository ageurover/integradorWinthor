/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms.export;

import java.util.Date;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class ConsultaRemessaGerada extends javax.swing.JDialog {

    private boolean updateRemessa = true;
//    /**
//     * Creates new form ConsultaRemessaGerada
//     */
//    @SuppressWarnings("OverridableMethodCallInConstructor")
//    public ConsultaRemessaGerada() {
//        initComponents();
//        setLocationRelativeTo(null);
//        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
//    }

    /**
     * Creates new form CfopDialog
     *
     * @param parent
     * @param modal
     */
    public ConsultaRemessaGerada(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);
        setTitle("Remessa Gerada");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    public static void open() {
        new ConsultaRemessaGerada(null, true).setVisible(true);
    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     */
    private void buscaRemessaCartorio() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String filtro = " ";

            if (!edtIbgeCartorio.getText().isEmpty()) {
                filtro += " AND C.CODIBGE = '" + edtIbgeCartorio.getText() + "' ";
            }
            if (!edtConvenioCartorio.getText().isEmpty()) {
                filtro += " AND C.CODCONVENIO = '" + edtConvenioCartorio.getText() + "' ";
            }

//            if ((edtDataInicial.getDate() != null)) {
//                filtro += " AND C.DTENVIO BETWEEN TO_DATE('" + Formato.dateToStr(edtDataInicial.getDate()) + " 00:01','DD/MM/YYYY HH24:MI') "
//                        + " AND TO_DATE('" + Formato.dateToStr(edtDataInicial.getDate()) + " 23:59','DD/MM/YYYY HH24:MI') \n";
//            } else {
//                MessageDialog.error("A Data final deve ser maior ou igual a data inicial!");
//            }
            String strSelect = "SELECT * FROM BRZ_CARTORIO_REMESSA C "
                    + "   WHERE C.CODIBGE IS NOT NULL \n"
                    + filtro
                    + "   ORDER BY C.CODCONVENIO, C.CODIBGE";

            wint.openConectOracle();
            tblBrzCartorioRemessa.clearTableData();
            tblBrzCartorioRemessa.setTableData(wint.selectResultSet(strSelect));

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaRemessaCartorio");
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     */
    private void buscaWinthor() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String filtro = " ";
            filtro += " AND C.codcob = '" + edtCodCob.getText() + "' ";

            if (!edtNumRemessa.getText().isEmpty()) {
                filtro += " AND C.numremessa = '" + edtNumRemessa.getText() + "' ";
            }

            if (!edtCodCli.getText().isEmpty()) {
                filtro += " AND C.CODCLI = " + edtCodCli.getText();
            }

            if ((edtDataInicial.getDate() != null)) {
                filtro += " AND C.DTENVIO BETWEEN TO_DATE('" + Formato.dateToStr(edtDataInicial.getDate()) + " 00:01','DD/MM/YYYY HH24:MI') "
                        + " AND TO_DATE('" + Formato.dateToStr(edtDataInicial.getDate()) + " 23:59','DD/MM/YYYY HH24:MI') \n";
            } else {
                MessageDialog.error("A Data final deve ser maior ou igual a data inicial!");
            }

            String strSelect = "SELECT * FROM BRZ_CARTORIO C \n"
                    + "   WHERE C.NUMREMESSA IS NOT NULL \n"
                    + filtro
                    + "   ORDER BY C.DTENVIO, C.CODCLI";

            wint.openConectOracle();
            tblBrzCartorio.clearTableData();
            tblBrzCartorio.setTableData(wint.selectResultSet(strSelect));
            if (tblBrzCartorio.getRowCount() > 0) {
                txtLog.append("Quantidade: " + tblBrzCartorio.getRowCount() + " \n");
            } else {
                txtLog.append("Registros não localizados para os filtros informados informado!\n" + strSelect);
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaWinthor");
        } finally {
            wint.closeConectOracle();
        }

    }

    private boolean exluirRemessa(Date dataEnvio, String numRemessa, String CodCliente, String codCobranca) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        boolean ret = false;
        try {
            if (dataEnvio != null) {
                if ((!numRemessa.isEmpty()) && (!CodCliente.isEmpty()) && (!codCobranca.isEmpty())) {
                    wint.openConectOracle();

                    // altera a data do pedido
                    strUpdate = "DELETE FROM BRZ_CARTORIO "
                            + "  WHERE DTENVIO BETWEEN to_date('" + Formato.dateToStr(dataEnvio) + " 00:01','DD/MM/YYYY HH24:MI') AND to_date('" + Formato.dateToStr(dataEnvio) + " 23:59', 'dd/mm/yyyy HH24:MI') \n"
                            + "  AND CODCLI = " + CodCliente + "\n"
                            + "  AND CODCOB = '" + codCobranca + "' \n"
                            + "  AND NUMREMESSA = " + numRemessa;

                    result = wint.updateDados(strUpdate);
                    ret = (result != 0);
                    txtLog.append("Registros Removidos: " + result + "\n");

                } else {
                    txtLog.append("Os Filtros necessarios não foram informados! \n"
                            + " Dever ser informado: Remessa, Cliente, Cobranca, Data de Geracao!\n");
                }
            } else {
                txtLog.append("Os Filtros necessarios não foram informados! \n"
                        + " Dever ser informado: Remessa, Cliente, Cobranca, Data de Geracao!\n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "exluirRemessa");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
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
        jPanel2 = new javax.swing.JPanel();
        btnPesquisar = new javax.swing.JButton();
        btnGravar = new javax.swing.JButton();
        edtDataInicial = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBrzCartorio = new winthorDb.util.CustomTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        edtCodCob = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edtNumRemessa = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edtCodCli = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBrzCartorioRemessa = new winthorDb.util.CustomTable();
        jLabel4 = new javax.swing.JLabel();
        edtIbgeCartorio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtConvenioCartorio = new javax.swing.JTextField();
        btnBuscarRemessaCartorio = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        edtConvenioRemessaX = new javax.swing.JTextField();
        edtCartorioIbgeX = new javax.swing.JTextField();
        edtRemessaX = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnGravaRemessaCartorio = new javax.swing.JButton();
        edtDataUltimaGeradaX = new com.toedter.calendar.JDateChooser();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz010");

        jLabel1.setText("Consulta Remessa Gerada");

        jLabel3.setText("* * Faz a consulta das remessas geradas para o cartorio e permite exclusão para nova geração");

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados"));

        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        btnGravar.setText("Remover Remessa");
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt);
            }
        });

        jLabel6.setText("Periodo Geração");

        tblBrzCartorio.setToolTipText("");
        tblBrzCartorio.setCellSelectionEnabled(true);
        tblBrzCartorio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBrzCartorioMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBrzCartorio);

        txtLog.setColumns(20);
        txtLog.setRows(5);
        txtLog.setText("Log de execução:\n");
        jScrollPane1.setViewportView(txtLog);

        jLabel5.setText("Cod. Cobrança");

        edtCodCob.setText("CRA");

        jLabel7.setText("Remessa:");

        edtNumRemessa.setText("1");

        jLabel8.setText("Cod. Cliente");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(edtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPesquisar))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(edtCodCob, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtNumRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnGravar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPesquisar)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel6)
                        .addComponent(edtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edtCodCob, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(edtNumRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(edtCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGravar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Remessa Gerada", jPanel2);

        tblBrzCartorioRemessa.setToolTipText("");
        tblBrzCartorioRemessa.setCellSelectionEnabled(true);
        tblBrzCartorioRemessa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBrzCartorioRemessaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblBrzCartorioRemessa);

        jLabel4.setText("IBGE Cartorio:");

        jLabel9.setText("Convenio:");

        btnBuscarRemessaCartorio.setText("Buscar");
        btnBuscarRemessaCartorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarRemessaCartorioActionPerformed(evt);
            }
        });

        jLabel10.setText("Convenio:");

        jLabel11.setText("IBGE Cartorio:");

        jLabel12.setText("Remessa:");

        jLabel13.setText("Dt. Ultima gerada:");

        btnGravaRemessaCartorio.setText("Gravar");
        btnGravaRemessaCartorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravaRemessaCartorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(edtConvenioRemessaX)
                                    .addComponent(edtCartorioIbgeX)
                                    .addComponent(edtRemessaX)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDataUltimaGeradaX, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(btnGravaRemessaCartorio))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtIbgeCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtConvenioCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarRemessaCartorio)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtIbgeCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(edtConvenioCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarRemessaCartorio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(edtConvenioRemessaX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(edtCartorioIbgeX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(edtRemessaX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(edtDataUltimaGeradaX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGravaRemessaCartorio))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Controle Remessa", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Remessa Gerada");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        try {
            if ((edtDataInicial.getDate() != null)) {
                buscaWinthor();
            } else {
                txtLog.append("A data de envio devem ser informadas!\n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnPesquisarActionPerformed");
        }

    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed

        try {
            if ((edtDataInicial.getDate() != null)) {
                exluirRemessa(edtDataInicial.getDate(), edtNumRemessa.getText(), edtCodCli.getText(), edtCodCob.getText());
            } else {
                txtLog.append("A Nova data deve ser informada! \n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnGravarActionPerformed

    private void tblBrzCartorioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBrzCartorioMouseClicked
        // TODO add your handling code here:
        if (tblBrzCartorio.getRowCount() > 0) {

        }

    }//GEN-LAST:event_tblBrzCartorioMouseClicked

    private void tblBrzCartorioRemessaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBrzCartorioRemessaMouseClicked
        try {
            if (tblBrzCartorioRemessa.getRowCount() > 0) {
                updateRemessa = true;
                btnGravaRemessaCartorio.setText("Gravar");
                edtConvenioRemessaX.setText(tblBrzCartorioRemessa.getConteudoRowSelected("CODCONVENIO").toString());
                edtCartorioIbgeX.setText(tblBrzCartorioRemessa.getConteudoRowSelected("CODIBGE").toString());
                edtRemessaX.setText(tblBrzCartorioRemessa.getConteudoRowSelected("NUMREMESSA").toString());
                edtDataUltimaGeradaX.setDate(Formato.strToDate(tblBrzCartorioRemessa.getConteudoRowSelected("DTULTREMESSA").toString()));
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_tblBrzCartorioRemessaMouseClicked

    private void btnBuscarRemessaCartorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarRemessaCartorioActionPerformed
        try {
            // TODO add your handling code here:
            buscaRemessaCartorio();
        } catch (Exception ex) {
            trataErro.trataException(ex);
            //Logger.getLogger(ConsultaRemessaGerada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarRemessaCartorioActionPerformed

    private void btnGravaRemessaCartorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravaRemessaCartorioActionPerformed
        // TODO add your handling code here:
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        try {
            wint.openConectOracle();
            if (updateRemessa) {
                if ((!edtCartorioIbgeX.getText().isEmpty()) && (!edtConvenioRemessaX.getText().isEmpty()) && (!edtRemessaX.getText().isEmpty())) {
                    wint.updateDados("UPDATE BRZ_CARTORIO_REMESSA SET CODIBGE = " + edtCartorioIbgeX.getText()
                            + " , CODCONVENIO = " + edtConvenioRemessaX.getText()
                            + " ,NUMREMESSA = " + edtRemessaX.getText()
                            + " ,DTULTREMESSA = trunc(sysdate) "
                            + " WHERE CODIBGE = " + edtCartorioIbgeX.getText()
                            + " AND CODCONVENIO = " + edtConvenioRemessaX.getText());
                }
            } else {
                if ((!edtCartorioIbgeX.getText().isEmpty()) && (!edtConvenioRemessaX.getText().isEmpty()) && (!edtRemessaX.getText().isEmpty())) {
                    wint.insertDados("INSERT INTO BRZ_CARTORIO_REMESSA ( CODIBGE,CODCONVENIO,NUMREMESSA,DTULTREMESSA) "
                            + " VALUES ( " + edtCartorioIbgeX.getText() + " , " + edtConvenioRemessaX.getText() + " , " + edtRemessaX.getText() + "  , trunc(sysdate) ) ");
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnGravaRemessaCartorioActionPerformed");
        } finally {
            btnGravaRemessaCartorio.setText("Novo");
            updateRemessa = false;
            edtCartorioIbgeX.setText("");
            edtConvenioRemessaX.setText("");
            edtRemessaX.setText("");
            edtDataUltimaGeradaX.setDate(null);
            try {
                wint.closeConectOracle();
            } catch (Exception ex) {
                trataErro.trataException(ex);
            }
        }
    }//GEN-LAST:event_btnGravaRemessaCartorioActionPerformed

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
                new ConsultaRemessaGerada(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarRemessaCartorio;
    private javax.swing.JButton btnGravaRemessaCartorio;
    private javax.swing.JButton btnGravar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JTextField edtCartorioIbgeX;
    private javax.swing.JTextField edtCodCli;
    private javax.swing.JTextField edtCodCob;
    private javax.swing.JTextField edtConvenioCartorio;
    private javax.swing.JTextField edtConvenioRemessaX;
    private com.toedter.calendar.JDateChooser edtDataInicial;
    private com.toedter.calendar.JDateChooser edtDataUltimaGeradaX;
    private javax.swing.JTextField edtIbgeCartorio;
    private javax.swing.JTextField edtNumRemessa;
    private javax.swing.JTextField edtRemessaX;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private winthorDb.util.CustomTable tblBrzCartorio;
    private winthorDb.util.CustomTable tblBrzCartorioRemessa;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
