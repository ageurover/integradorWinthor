/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;

/**
 *
 * @author ageurover
 */
public class Brz004 extends javax.swing.JFrame {

    int qtdCheque = 0;

    /**
     * Creates new form Brz001
     */
    public Brz004() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private void buscaProdutoPedidoWinthor(String numPedido, String codFilialPedido) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String strSelect = "select p.numped as numped, "
                    + " i.codprod, pr.descricao, pr.embalagem, i.qt, i.pvenda, (i.pvenda * i.qt) as TotalItem, "
                    + " concat(c.codcli, c.cliente) as cliente, p.data, p.posicao, "
                    + " concat(u.codusur,u.nome) as RCA "
                    + " from pcpedc p, pcusuari u, pcclient c, pcpedi i, pcprodut pr "
                    + " where i.numped = p.numped "
                    + " and i.codprod = pr.codprod"
                    + " and p.posicao in ('B','P', 'L', 'M') "
                    + " and u.codusur = p.codusur "
                    + " and c.codcli = p.codcli "
                    + " and p.codfilial = " + codFilialPedido
                    + " and p.numped = " + numPedido;

            wint.openConectOracle();
            tblOs.clearTableData();
            tblOs.setTableData(wint.selectResultSet(strSelect));

            

        } catch (Exception ex) {
            trataErro.trataException(ex,"buscaProdutoPedidoWinthor");
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * Faz a transferencia do estoque dos produtos pertencentes ao Pedido Da
     * Filial de Estoque para a Filial do Pedido
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param FilialPedido Codigo da filial onde o pedido foi digitado
     * @param FilialEstoque Codigo da filial de origem do estoque
     */
    private void transfereEstoquePedido(String numPedido, String filialPedido, String filialEstoque) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        try {
            wint.openConectOracle();
            wint.executeProcedure("ajusta_estoque_pedido", new String[]{numPedido, filialPedido, filialEstoque}, new int[]{1, 2, 2});
        } catch (Exception ex) {
            trataErro.trataException(ex,"transfereEstoquePedido");
            throw ex;
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
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        edtFilialPedido = new javax.swing.JTextField();
        edtNumeroPedido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edtFilialEstoque = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        edtLogAjuste = new javax.swing.JTextArea();
        btnPesquisaPedido = new javax.swing.JButton();
        btnProcessaEstoque = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblOs = new winthorDb.util.CustomTable();

        setTitle("Convers�o de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz004");

        jLabel1.setText("Transferencia de estoque entre filiais por Pedido ");
        jLabel1.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados"));

        jLabel4.setText("Pedido:");

        jLabel5.setText("Filial Fatura:");

        edtFilialPedido.setText("90");

        jLabel6.setText("Filial Estoque:");

        edtFilialEstoque.setText("01");

        jLabel7.setText("Produtos da Carrega:");

        jLabel8.setText("Log's:");

        edtLogAjuste.setColumns(20);
        edtLogAjuste.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        edtLogAjuste.setRows(5);
        jScrollPane2.setViewportView(edtLogAjuste);

        btnPesquisaPedido.setText("Pesquisar");
        btnPesquisaPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaPedidoActionPerformed(evt);
            }
        });

        btnProcessaEstoque.setText("Transferir Estoque");
        btnProcessaEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessaEstoqueActionPerformed(evt);
            }
        });

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        tblOs.setToolTipText("");
        tblOs.setCellSelectionEnabled(true);
        tblOs.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane3.setViewportView(tblOs);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(105, 105, 105)
                                .addComponent(btnProcessaEstoque)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtFilialPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtFilialEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edtFilialPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(edtFilialEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(edtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisaPedido)
                    .addComponent(btnFechar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnProcessaEstoque)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnPesquisaPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaPedidoActionPerformed
        try {
            if ((!edtNumeroPedido.getText().isEmpty()) && (!edtFilialEstoque.getText().isEmpty()) && (!edtFilialPedido.getText().isEmpty())) {
                buscaProdutoPedidoWinthor(edtNumeroPedido.getText(), edtFilialPedido.getText());
                btnPesquisaPedido.setEnabled(false);
                btnProcessaEstoque.setEnabled(true);
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATEN��O: Deve ser informado o numero do pedido, a filial do pedido, o e codigo da filial de origem do estoque !!!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"btnPesquisaPedidoActionPerformed");
        }
    }//GEN-LAST:event_btnPesquisaPedidoActionPerformed

    private void btnProcessaEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessaEstoqueActionPerformed
        try {
            if ((!edtNumeroPedido.getText().isEmpty()) && (!edtFilialEstoque.getText().isEmpty()) && (!edtFilialPedido.getText().isEmpty())) {
                transfereEstoquePedido(edtNumeroPedido.getText(), edtFilialPedido.getText(), edtFilialEstoque.getText());
                btnPesquisaPedido.setEnabled(true);
                btnProcessaEstoque.setEnabled(false);
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATEN��O: Deve ser informado o numero do pedido, a filial do pedido, o e codigo da filial de origem do estoque !!!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"btnProcessaEstoqueActionPerformed");
        }
    }//GEN-LAST:event_btnProcessaEstoqueActionPerformed

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
                new Brz004().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnPesquisaPedido;
    private javax.swing.JButton btnProcessaEstoque;
    private javax.swing.JTextField edtFilialEstoque;
    private javax.swing.JTextField edtFilialPedido;
    private javax.swing.JTextArea edtLogAjuste;
    private javax.swing.JTextField edtNumeroPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private winthorDb.util.CustomTable tblOs;
    // End of variables declaration//GEN-END:variables
}
