/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.util.Date;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz010 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Brz010() {
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
    private void buscaPedidoWinthor(Date dtInicial, Date dtFinal, String filialPedido) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String strSelect = "SELECT PCPEDC.NUMPED, "
                    + " PCPEDC.CODCLI AS CLIENTE, "
                    + " PCPEDC.CODUSUR AS RCA, "
                    + " PCPEDC.CODSUPERVISOR AS EQUIPE, "
                    + " PCPEDC.POSICAO, "
                    + " TO_CHAR(PCPEDC.DATA, 'DD/MM/YYYY') AS DATAPED, "
                    + " TO_CHAR(PCPEDC.DATA, 'DD/MM/YYYY') AS DATAORIGINAL "
                    + " FROM PCPEDC, PCCLIENT, PCUSUARI "
                    + " WHERE PCPEDC.POSICAO IN ('L','B','P','M')  "
                    + "  AND PCPEDC.DATA BETWEEN TO_DATE('" + Formato.dateToStr(dtInicial) + "', 'DD/MM/YYYY') "
                    + "      AND TO_DATE('" + Formato.dateToStr(dtFinal) + "', 'DD/MM/YYYY') \n"
                    + "  AND PCPEDC.CODFILIAL IN ('" + filialPedido + "') \n"
                    + "  AND PCCLIENT.CODCLI = PCPEDC.CODCLI \n"
                    + "  AND PCUSUARI.CODUSUR = PCPEDC.CODUSUR \n"
                    + "  AND PCPEDC.DATAORIGINAL IS NULL"
                    + " ORDER BY PCPEDC.DATA,PCPEDC.POSICAO ";

            wint.openConectOracle();
            tblPedidos.clearTableData();
            tblPedidos.setTableData(wint.selectResultSet(strSelect));
            if (tblPedidos.getRowCount() > 0) {
                txtLog.append("Quantidade de pedidos: " + tblPedidos.getRowCount() + " \n");
            } else {
                txtLog.append("Registros não localizados para o intervalo de datas informado!\n");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaPedidosWinthor");
        } finally {
            wint.closeConectOracle();
        }

    }

    private boolean mudarDataPedido(String numPedido, Date novaData) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        boolean ret = false;
        try {
            if (numPedido != null) {
                if ((!numPedido.isEmpty()) && (novaData != null)) {
                    wint.openConectOracle();

                    // altera a data do pedido
                    strUpdate = "UPDATE PCPEDC SET PCPEDC.DATAORIGINAL = PCPEDC.DATA "
                            + " , PCPEDC.DATA = TO_DATE('" + Formato.dateToStr(novaData) + "', 'DD/MM/YYYY') "
                            + " , PCPEDC.DTENTREGA = TO_DATE('" + Formato.dateToStr(novaData) + "', 'DD/MM/YYYY') "
                            + " WHERE PCPEDC.NUMPED = " + numPedido;

                    result = wint.updateDados(strUpdate);
                    ret = (result != 0);
                    txtLog.append("Registros Atualizados: " + result + "\n");
                    txtLog.append("O Cabeçalho do pedido: " + numPedido + " nova data: " + Formato.dateToStr(novaData) + " foi atualizado na base de dados do winthor\n");

// altera os itens do pedido
                    if (result >= 0) {
                        strUpdate = "UPDATE PCPEDI SET PCPEDI.DATA = TO_DATE('" + Formato.dateToStr(novaData) + "', 'DD/MM/YYYY') "
                                + " WHERE PCPEDI.NUMPED = " + numPedido;

                        result = wint.updateDados(strUpdate);
                        txtLog.append("Registros Atualizados: " + result + "\n");
                        txtLog.append("Os Itens do pedido: " + numPedido + " nova data: " + Formato.dateToStr(novaData) + " foi atualizado na base de dados do winthor\n");
                        ret = (result != 0);
                    }
                } else {
                    txtLog.append("Numero do Pedido ou Nova Data não informados \n");
                }
            } else {
                txtLog.append("Numero do Pedido não informado!!! \n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
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
        jPanel2 = new javax.swing.JPanel();
        btnPesquisar = new javax.swing.JButton();
        btnGravar = new javax.swing.JButton();
        edtDataInicial = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        edtDataFinal = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        edtNovaData = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPedidos = new winthorDb.util.CustomTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        edtFilial = new javax.swing.JTextField();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz010");

        jLabel1.setText("Pedidos de Vendas");

        jLabel3.setText("* * Altera da data dos pedidos nas posições (B/L/M/P) para a nova data informada");

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

        btnGravar.setText("Gravar");
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt);
            }
        });

        jLabel6.setText("Intervalor para Consulta de Pedidos:");

        jLabel4.setText("Até");

        jLabel5.setText("Nova Data para os Pedidos:");

        tblPedidos.setToolTipText("");
        tblPedidos.setCellSelectionEnabled(true);
        jScrollPane3.setViewportView(tblPedidos);

        txtLog.setColumns(20);
        txtLog.setRows(5);
        txtLog.setText("Log de execução:\n");
        jScrollPane1.setViewportView(txtLog);

        jLabel7.setText("Filial:");

        edtFilial.setText("1");
        edtFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtFilialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPesquisar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNovaData, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGravar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(edtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(edtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar)
                    .addComponent(jLabel7)
                    .addComponent(edtFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnGravar)
                    .addComponent(jLabel5)
                    .addComponent(edtNovaData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        try {
            if ((edtDataInicial.getDate() != null) && (edtDataFinal.getDate().after(edtDataInicial.getDate()))) {
                buscaPedidoWinthor(edtDataInicial.getDate(), edtDataFinal.getDate(), edtFilial.getText());
            } else {
                txtLog.append("A data inicial e final devem ser informadas! \n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnPesquisarActionPerformed");
        }

    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed
//  
        String numPed = "";
        try {
            if (edtNovaData.getDate() != null) {
                if (tblPedidos.getRowCount() > 0) {
                    for (int i = 0; i < tblPedidos.getRowCount(); i++) {
                        numPed = tblPedidos.getConteudoRow("NUMPED", i).toString();
                        //processa a alteracao
                        mudarDataPedido(numPed, edtNovaData.getDate());
                    }
                    tblPedidos.clearTableData();
                    btnGravar.setEnabled(false);
                }
            } else {
                txtLog.append("A Nova data deve ser informada! \n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnGravarActionPerformed

    private void edtFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtFilialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtFilialActionPerformed

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
                new Brz010().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGravar;
    private javax.swing.JButton btnPesquisar;
    private com.toedter.calendar.JDateChooser edtDataFinal;
    private com.toedter.calendar.JDateChooser edtDataInicial;
    private javax.swing.JTextField edtFilial;
    private com.toedter.calendar.JDateChooser edtNovaData;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private winthorDb.util.CustomTable tblPedidos;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
