/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.util.List;
import winthorDb.Main;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;

/**
 *
 * @author ageurover
 */
public class Brz016 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    public Brz016() {
        initComponents();
        setLocationRelativeTo(null);
        tblDados.clearTableData();
        edtLog.setText("");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    private void buscaLancamentoWinthor(String numTrans) {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            tblDados.clearTableData();
            String strSelect = "select s.codfilial as filial_origem, s.numtransvenda, e.codfilial as filial_destino, e.numtransent,\n"
                    + "e.numnota,e.serie,e.especie,e.dtemissao,e.dtent,e.vltotal \n"
                    + "from pcnfent e, pcnfsaid s \n"
                    + "where s.numtransvenda = e.numtransvendaorig\n"
                    + "and e.numtransent = " + numTrans;

            tblDados.setTableData(strSelect);

            edtLog.setText("");
            edtLog.append("Lan�amentos Encontrados no Winthor -> " + tblDados.getRowCount() + "\n");

        } catch (Exception ex) {
            trataErro.trataException(ex);
        }

    }

    private void atualizaCusto(String filialOrigem, String filialDestino, String numTransEnt) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            wint.openConectOracle();
            String sqlString = "Select kt_atualiza_custo_transf(pcodfilialorigem=>'" + filialOrigem 
                    + "' ,pcodfilialdestino=>'" + filialDestino 
                    + "' ,pnumtransent=>" 
                    + numTransEnt + ") as log from dual";
            List lst = wint.selectDados(sqlString);
            
            
            for (Object object : lst.toArray()) {
                edtLog.append(lst.size() + "\n");
                edtLog.append(object.toString() + "\n");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex);
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
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        edtCodFilialDestino = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtLog = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilialOrigem = new javax.swing.JTextField();
        btnLimpar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        edtNumTransEnt = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDados = new winthorDb.util.CustomTable();
        btnAtualizaCusto = new javax.swing.JButton();

        setTitle("Atualiza��o de Custo Filial Destino");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz002");

        jLabel1.setText("Ajuste de Custos na Transferencia entre Filiais!");

        jLabel3.setText("* * Rotina Distribuidora");

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

        jLabel4.setText("Filial Destino:");

        edtCodFilialDestino.setText("1");

        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        edtLog.setColumns(20);
        edtLog.setRows(5);
        edtLog.setWrapStyleWord(true);
        jScrollPane1.setViewportView(edtLog);

        jLabel5.setText("Filial Origem:");

        edtCodFilialOrigem.setText("2");
        edtCodFilialOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodFilialOrigemActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel7.setText("Num. Transa��o Entrada:");

        tblDados.setToolTipText("");
        tblDados.setCellSelectionEnabled(true);
        tblDados.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane3.setViewportView(tblDados);

        btnAtualizaCusto.setText("Atualizar Custo Filial Destino");
        btnAtualizaCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizaCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilialOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilialDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumTransEnt, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesquisar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpar))
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAtualizaCusto)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtNumTransEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(edtCodFilialOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(edtCodFilialDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(btnPesquisar)
                    .addComponent(btnLimpar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAtualizaCusto))
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

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        try {
            if ((!edtCodFilialDestino.getText().isEmpty()) && (!edtNumTransEnt.getText().isEmpty()) && (!edtCodFilialOrigem.getText().isEmpty())) {
                buscaLancamentoWinthor(edtNumTransEnt.getText());
                btnPesquisar.setEnabled(false);
                edtCodFilialDestino.setEditable(false);
                edtCodFilialOrigem.setEditable(false);
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATEN��O: \nDeve ser informado a filial de origem e destino e numero da transa��o de entrada!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();
                btnLimparActionPerformed(evt);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void edtCodFilialOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialOrigemActionPerformed

    }//GEN-LAST:event_edtCodFilialOrigemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        btnPesquisar.setEnabled(true);
        edtCodFilialDestino.setEditable(true);
        edtCodFilialOrigem.setEditable(true);
        edtCodFilialOrigem.setText("1");
        edtCodFilialDestino.setText("");
        edtLog.setText("");
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnAtualizaCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizaCustoActionPerformed
        // TODO add your handling code here:
        try {
            if (tblDados.getSelectedRowCount() > 0) {
                atualizaCusto(tblDados.getConteudoRowSelected("filial_origem").toString(),
                        tblDados.getConteudoRowSelected("filial_destino").toString(),
                        tblDados.getConteudoRowSelected("numtransent").toString());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnAtualizaCustoActionPerformed

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
            java.util.logging.Logger.getLogger(Brz016.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Brz016().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizaCusto;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JTextField edtCodFilialDestino;
    private javax.swing.JTextField edtCodFilialOrigem;
    private javax.swing.JTextArea edtLog;
    private javax.swing.JTextField edtNumTransEnt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private winthorDb.util.CustomTable tblDados;
    // End of variables declaration//GEN-END:variables
}
