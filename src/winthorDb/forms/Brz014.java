/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.Cursor;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz014 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Brz014() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        initGuid();
    }

    private void initGuid() {
        tblPedido.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblPedido.setColumnSelectionAllowed(false);
        tblPedido.setRowSelectionAllowed(true);

        tblItemPedido.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblItemPedido.setColumnSelectionAllowed(false);
        tblItemPedido.setRowSelectionAllowed(true);

        tblProduto.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblProduto.setColumnSelectionAllowed(false);
        tblProduto.setRowSelectionAllowed(true);
    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     *
     */
    private void buscaPedidosWinthor() throws Exception {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    tblPedido.clearTableData();
                    String strSelect = "SELECT p.obs, p.obs1, p.obs2,  p.numcar, ('' || p.numped) as numped, "
                            + " p.ORIGEMPED, p.data, p.vltotal, p.vlatend, p.codcli, c.cliente, p.codfilial, "
                            + " p.codfilialnf, c.codfilialnf cli_FilialFatura ,"
                            + " p.posicao, u.codusur, u.nome as NomeRCA, p.dtfat, p.numcupom  "
                            + " FROM pcpedc p, pcusuari u, pcclient c "
                            + " WHERE c.codcli = p.codcli "
                            + " AND u.codusur = p.codusur ";
                    if (!edtCodFilial.getText().isEmpty()) {
                        strSelect += " AND P.CODFILIAL IN (" + edtCodFilial.getText() + ") ";
                    }
                    if (!edtNumPedido.getText().isEmpty()) {
                        strSelect += " AND p.numped IN (" + edtNumPedido.getText() + ") ";
                    }

                    wint.openConectOracle();
                    tblPedido.clearTableData();
                    tblPedido.setTableData(wint.selectResultSet(strSelect));

                    if (tblPedido.getRowCount() <= 0) {
                        txtLog.append("Registros não localizados para o filtro informado!\n");
                    } else {
                        lblQtde.setText("Qtde Pedidos: " + tblPedido.getRowCount());
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

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     *
     */
    private void buscaItemPedidoWinthor(final String numPedido) throws Exception {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    tblItemPedido.clearTableData();
                    String strSelect = "select c.codfilial, i.numped, i.data, i.codprod, "
                            + " p.descricao, p.embalagem, i.codauxiliar \n"
                            + " from pcpedc c, pcpedi i, pcprodut p\n"
                            + " where  c.numped = i.numped \n"
                            + " and i.codprod = p.codprod  \n"
                            + " and i.numped = " + numPedido;
                    wint.openConectOracle();
                    tblItemPedido.clearTableData();
                    tblItemPedido.setTableData(wint.selectResultSet(strSelect));

                    if (tblItemPedido.getRowCount() <= 0) {
                        txtLog.append("Registros não localizados para o filtro informado!\n");
                    } else {
                        lblQtde.setText("Qtde Itens do Pedidos: " + tblItemPedido.getRowCount());
                    }
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaItemPedidoWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaItemPedidoWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    /**
     * Faz a busca dos dados do item do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param codProd codigo do produto digitado no sistema winthor
     * @param codFilial codigo da filial das embalagens do produto digitado no
     * sistema winthor
     *
     */
    private void buscaEmbalagemProdutoWinthor(final String codProd, final String codFilial) throws Exception {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    tblProduto.clearTableData();
                    String strSelect = "select e.codprod, p.descricao, e.embalagem, e.codauxiliar \n"
                            + " from pcembalagem e, pcprodut p\n"
                            + " where e.codprod = p.codprod\n"
                            + " and e.codfilial = '" + codFilial + "' "
                            + " and e.codprod = " + codProd;

                    wint.openConectOracle();
                    tblProduto.clearTableData();
                    tblProduto.setTableData(wint.selectResultSet(strSelect));

                    if (tblProduto.getRowCount() <= 0) {
                        txtLog.append("Registros não localizados para o filtro informado!\n");
                    } else {
                        lblQtde.setText("Qtde Embalagens para o produto: " + tblProduto.getRowCount());
                    }
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaEmbalagemProdutoWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaEmbalagemProdutoWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void atualizaEanItem(final String codFilial, final String numPedido, final String codProduto, final String codAuxiliar) throws Exception {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
                try {
                    int result=0;
                    wint.openConectOracle();
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (codFilial.isEmpty() || numPedido.isEmpty() || codProduto.isEmpty() ) {
                        txtLog.append("Registros não localizados!\n");
                        MessageDialog.error("Faltam dados para continuar a atualização!");
                    } else {
                        String update = "UPDATE PCPEDI SET CODAUXILIAR = '" + codAuxiliar + "' "
                                + " WHERE NUMPED = " + numPedido 
                                + " AND CODPROD = " + codProduto;
                        result = wint.updateDados(update);
                        
                        if (result ==0){
                            MessageDialog.error("Erro ao atualizar o codigo auxiliar!");
                        }
                    }
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "atualizaEanItem");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    MessageDialog.info("Atualização concluida com sucesso!");                    
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
        btnAtualizar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPedido = new winthorDb.util.CustomTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        btnSalvarLog = new javax.swing.JButton();
        lblQtde = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        edtNumPedido = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProduto = new winthorDb.util.CustomTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblItemPedido = new winthorDb.util.CustomTable();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz011");

        jLabel1.setText("Atualiza o Codigo de Barras (GTIN) que esta vinculado ao pedido");

        jLabel3.setText("* *  Faz a Atualização dos codigos de barras para o pedido informado.");

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

        btnAtualizar.setText("Atualizar EAN");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        tblPedido.setToolTipText("");
        tblPedido.setCellSelectionEnabled(true);
        tblPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPedidoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblPedido);

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        jLabel4.setText("Filial:");

        edtCodFilial.setText("1");

        btnSalvarLog.setText("Salvar Log");
        btnSalvarLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarLogActionPerformed(evt);
            }
        });

        lblQtde.setText("...");

        jLabel8.setText("Pedido:");

        tblProduto.setToolTipText("");
        tblProduto.setCellSelectionEnabled(true);
        jScrollPane4.setViewportView(tblProduto);

        tblItemPedido.setToolTipText("");
        tblItemPedido.setCellSelectionEnabled(true);
        tblItemPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemPedidoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblItemPedido);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesquisar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblQtde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAtualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalvarLog)))
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(edtNumPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblQtde, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAtualizar)
                        .addComponent(btnSalvarLog)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        try {
            atualizaEanItem(tblItemPedido.getConteudoRowSelected("codfilial").toString(), 
                    tblItemPedido.getConteudoRowSelected("numped").toString(), 
                    tblItemPedido.getConteudoRowSelected("codprod").toString(),
                    tblProduto.getConteudoRowSelected("codauxiliar").toString());
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnValidarActionPerformed");
        }

    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        try {
            txtLog.setText("");
            buscaPedidosWinthor();
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
                tblPedido.writeToDisk(tblPedido.getTableData(), file.getAbsolutePath());
                MessageDialog.saveSucess();
            } catch (Exception ex) {
                trataErro.trataException(ex);
            }
        }

    }//GEN-LAST:event_btnSalvarLogActionPerformed

    private void tblItemPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemPedidoMouseClicked
        // TODO add your handling code here:
        try {
            if (tblItemPedido.getSelectedRowCount() > 0) {
                buscaEmbalagemProdutoWinthor(tblItemPedido.getConteudoRowSelected("codprod").toString(), tblItemPedido.getConteudoRowSelected("codfilial").toString());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_tblItemPedidoMouseClicked

    private void tblPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPedidoMouseClicked
        // TODO add your handling code here:
        try {
            if (tblPedido.getSelectedRowCount() > 0) {
                buscaItemPedidoWinthor(tblPedido.getConteudoRowSelected("numped").toString());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }

    }//GEN-LAST:event_tblPedidoMouseClicked

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
                new Brz014().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvarLog;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtNumPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblQtde;
    private winthorDb.util.CustomTable tblItemPedido;
    private winthorDb.util.CustomTable tblPedido;
    private winthorDb.util.CustomTable tblProduto;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
