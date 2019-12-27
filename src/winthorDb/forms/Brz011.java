/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.Cursor;
import java.io.File;
import javax.swing.JFileChooser;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz011 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Brz011() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numCarga Numero do pedido digitado no sistema winthor
     *
     */
    private void buscaProdutoWinthor203() throws Exception {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    tblProduto.clearTableData();
                    String strSelect = "SELECT P.CODPROD, P.DESCRICAO, P.EMBALAGEM, P.CODFAB, E.QTESTGER, "
                            + "NVL(P.GTINCODAUXILIARTRIB,0) AS GTINCODAUXILIARTRIB, NVL(P.CODAUXILIARTRIB,0) AS CODAUXILIARTRIB, '' AS VALIDO_TRIB, "
                            + "NVL(P.GTINCODAUXILIAR,0) AS GTINCODAUXILIAR, NVL(P.CODAUXILIAR,0) AS  CODAUXILIAR, '' AS VALIDO_EAN, "
                            + "NVL(P.GTINCODAUXILIAR2,0) AS GTINCODAUXILIAR2, NVL(P.CODAUXILIAR2,0) AS CODAUXILIAR2, '' AS VALIDO_EAN2 "
                            + "FROM PCPRODUT P, PCEST E WHERE P.CODPROD = E.CODPROD AND P.DTEXCLUSAO IS NULL ";
                    if (!edtCodFilial.getText().isEmpty()) {
                        strSelect += " AND E.CODFILIAL IN (" + edtCodFilial.getText() + ") ";
                    }
                    if (!edtCodFornecedor.getText().isEmpty()) {
                        strSelect += " AND P.CODFORNEC IN (" + edtCodFornecedor.getText() + ") ";
                    }
                    if (!edtCodDepto.getText().isEmpty()) {
                        strSelect += " AND P.CODEPTO IN (" + edtCodDepto.getText() + ") ";
                    }
                    if (!edtCodSecao.getText().isEmpty()) {
                        strSelect += " AND P.CODSEC IN (" + edtCodSecao.getText() + ") ";
                    }
                    if (!edtCodProd.getText().isEmpty()) {
                        strSelect += " AND P.CODPROD IN (" + edtCodProd.getText() + ") ";
                    }
                    wint.openConectOracle();
                    tblProduto.clearTableData();
                    tblProduto.setTableData(wint.selectResultSet(strSelect));

                    if (tblProduto.getRowCount() <= 0) {
                        txtLog.append("Registros não localizados para o intervalo de datas informado!\n");
                    } else {
                        lblQtde.setText("Qtde Produtos: " + tblProduto.getRowCount());
                    }
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaPedidosWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaPedidosWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void validarEan203() throws Exception {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                int qtdeTrue = 0;
                int qtdeFalse = 0;
                int qtdeForaPadrao = 0;
                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (tblProduto.getRowCount() > 0) {
                        //txtLog.append("Quantidade de produtos: " + tblProduto.getRowCount() + " \n");

                        boolean valido = false;
                        String codBar = "";
                        String lineCheck = "";
                        txtLog.append("CODPROD;DESCRICAO;EMBALAGEM;CODFAB;CODAUXILIARTRIB;GTINCODAUXILIARTRIB;ValidaTrib; CODAUXILIAR;GTINCODAUXILIAR;ValidaEAN;CODAUXILIAR2;GTINCODAUXILIAR2;ValidaEAN2\n");
                        for (int i = 0; i < tblProduto.getRowCount(); i++) {
                            // Valida o codigo EAN Tributavel que vai na NF-E
                            codBar = Formato.somenteNumeros(tblProduto.getConteudoRow("CODAUXILIARTRIB", i).toString());
                            if ((!codBar.isEmpty()) && (codBar.length() >= 8)) {
                                valido = Formato.isValidBarCodeEAN(codBar);
                                if (valido) {
                                    ++qtdeTrue;
                                } else {
                                    ++qtdeFalse;
                                }
                                tblProduto.setValueAt(valido, i, (int) tblProduto.getConteudoRow("VALIDO_TRIB", i));
                                lineCheck = tblProduto.getConteudoRow("CODPROD", i).toString() + " ; " + tblProduto.getConteudoRow("DESCRICAO", i).toString() + " ; " + tblProduto.getConteudoRow("EMBALAGEM", i).toString() + " ; " + tblProduto.getConteudoRow("CODFAB", i).toString() + " ; " + codBar + " ; " + tblProduto.getConteudoRow("GTINCODAUXILIARTRIB", i).toString() + " ; " + valido + " ";
                            } else {
                                ++qtdeForaPadrao;
                                tblProduto.setValueAt("Fora do Padrao", i, (int) tblProduto.getConteudoRow("VALIDO_TRIB", i));
                                lineCheck = tblProduto.getConteudoRow("CODPROD", i).toString() + " ; " + tblProduto.getConteudoRow("DESCRICAO", i).toString() + " ; " + tblProduto.getConteudoRow("EMBALAGEM", i).toString() + " ; " + tblProduto.getConteudoRow("CODFAB", i).toString() + " ; " + codBar + " ; " + tblProduto.getConteudoRow("GTINCODAUXILIARTRIB", i).toString() + " ; Fora do Padrao ";
                            }

                            // Valida o codigo EAN da unidade do produto
                            codBar = Formato.somenteNumeros(tblProduto.getConteudoRow("CODAUXILIAR", i).toString());
                            if ((!codBar.isEmpty()) && (codBar.length() >= 8)) {
                                valido = Formato.isValidBarCodeEAN(codBar);
                                if (valido) {
                                    ++qtdeTrue;
                                } else {
                                    ++qtdeFalse;
                                }
                                tblProduto.setValueAt(valido, i, (int) tblProduto.getConteudoRow("VALIDO_EAN", i));
                                lineCheck += " ; " + codBar + " ; " + tblProduto.getConteudoRow("GTINCODAUXILIAR", i).toString() + " ; " + valido + "  ";
                            } else {
                                ++qtdeForaPadrao;
                                tblProduto.setValueAt("Fora do Padrao", i, (int) tblProduto.getConteudoRow("VALIDO_EAN", i));
                                lineCheck += " ; " + codBar + " ; " + tblProduto.getConteudoRow("GTINCODAUXILIAR", i).toString() + " ; Fora do Padrao ";
                            }

                            // Valida o codigo EAN da unidade master do produto
                            codBar = Formato.somenteNumeros(tblProduto.getConteudoRow("CODAUXILIAR2", i).toString());
                            if ((!codBar.isEmpty()) && (codBar.length() >= 8)) {
                                valido = Formato.isValidBarCodeEAN(codBar);
                                if (valido) {
                                    ++qtdeTrue;
                                } else {
                                    ++qtdeFalse;
                                }
                                tblProduto.setValueAt(valido, i, (int) tblProduto.getConteudoRow("VALIDO_EAN2", i));
                                lineCheck += " ; " + codBar + " ; " + tblProduto.getConteudoRow("GTINCODAUXILIAR2", i).toString() + " ; " + valido + "  ";
                            } else {
                                ++qtdeForaPadrao;
                                tblProduto.setValueAt("Fora do Padrao", i, (int) tblProduto.getConteudoRow("VALIDO_EAN2", i));
                                lineCheck += " ; " + codBar + " ; " + tblProduto.getConteudoRow("GTINCODAUXILIAR2", i).toString() + " ; Fora do Padrao ";
                            }
                            txtLog.append(lineCheck + "\n");
                            lineCheck = "";
                        }

                    } else {
                        txtLog.append("Registros não localizados!\n");
                    }
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaPedidosWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    MessageDialog.info("Validação concluida com sucesso!");
                    lblQtde.setText(lblQtde.getText() + " Qtde Valido: " + qtdeTrue + " Qtde Invalido: " + qtdeFalse + " Qtde Fora Padrao: " + qtdeForaPadrao);
                }
            }//- Fim do Run
        }.start();//Fim Thread
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
        btnPesquisar = new javax.swing.JButton();
        btnValidar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProduto = new winthorDb.util.CustomTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        edtCodDepto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edtCodSecao = new javax.swing.JTextField();
        btnSalvarLog = new javax.swing.JButton();
        lblQtde = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        edtCodProd = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edtCodFornecedor = new javax.swing.JTextField();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz011");

        jLabel1.setText("Validação de Codigo de Barras (GTIN)");

        jLabel3.setText("* *  Faz a validação dos codigos de barras para os produto ativos e revenda do winthor");

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

        btnValidar.setText("Validar EAN");
        btnValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarActionPerformed(evt);
            }
        });

        tblProduto.setToolTipText("");
        tblProduto.setCellSelectionEnabled(true);
        jScrollPane3.setViewportView(tblProduto);

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        jLabel4.setText("Filial:");

        edtCodFilial.setText("1");

        jLabel5.setText("Departamento:");

        jLabel6.setText("Seção:");

        btnSalvarLog.setText("Salvar Log");
        btnSalvarLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarLogActionPerformed(evt);
            }
        });

        lblQtde.setText("...");

        jLabel7.setText("Produto:");

        jLabel8.setText("Fornecedor:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblQtde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnValidar)
                                .addGap(18, 18, 18)
                                .addComponent(btnSalvarLog))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodDepto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodSecao, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPesquisar)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(edtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edtCodDepto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(edtCodSecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(edtCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnValidar)
                        .addComponent(btnSalvarLog))
                    .addComponent(lblQtde))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarActionPerformed
        try {
            validarEan203();
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnValidarActionPerformed");
        }

    }//GEN-LAST:event_btnValidarActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        try {
            txtLog.setText("");
            buscaProdutoWinthor203();
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnPesquisarActionPerformed");
        }
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnSalvarLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarLogActionPerformed
        // TODO add your handling code here:
        JFileChooser saver = new JFileChooser("./");
        //saver.setFileFilter(new Text_Filter());
        int returnVal = saver.showSaveDialog(this);
        File file = saver.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                tblProduto.writeToDisk(tblProduto.getTableData(), file.getAbsolutePath());
                MessageDialog.saveSucess();
            } catch (Exception ex) {
                trataErro.trataException(ex);
            }
        }

    }//GEN-LAST:event_btnSalvarLogActionPerformed

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
                new Brz011().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvarLog;
    private javax.swing.JButton btnValidar;
    private javax.swing.JTextField edtCodDepto;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtCodFornecedor;
    private javax.swing.JTextField edtCodProd;
    private javax.swing.JTextField edtCodSecao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblQtde;
    private winthorDb.util.CustomTable tblProduto;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
