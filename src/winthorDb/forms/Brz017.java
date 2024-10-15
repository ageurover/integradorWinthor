/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.util.Date;
import java.util.List;
import javax.swing.ListSelectionModel;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz017 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    public Brz017() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());

        // permite selecionar mais de uma linha de dados.
        tblDados.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblDados.setColumnSelectionAllowed(false);
        tblDados.setRowSelectionAllowed(true);
        tblDados.clearTableData();

        edtLog.setText("");
        edtDataInicial.setDate(Formato.calculaData(-7, new Date()));
        edtDataFinal.setDate(new Date());
    }

    private void buscaLancamentoWinthor() {
        try {
            tblDados.clearTableData();
            String whereSql = "";
            if (!edtNumTransEnt.getText().isEmpty()) {
                whereSql += " and e.numtransent = " + edtNumTransEnt.getText() + "\n";
            }
            if ((edtDataInicial.getDate() != null) && (edtDataFinal.getDate() != null)) {
                whereSql += " and e.dtent between to_date('" + Formato.dateToStr(edtDataInicial.getDate()) + "','dd/mm/yyyy')"
                        + " and to_date('" + Formato.dateToStr(edtDataFinal.getDate()) + "','dd/mm/yyyy')\n";
                whereSql += " AND e.numtransent NOT IN (SELECT NUMTRANSENT FROM BRZ_LOG_ATUALIZA_PRECO_SPA)\n";
            }
            if (!whereSql.isEmpty()) {

                String strSelect = "select e.codfilial,  e.codfornec, f.fornecedor, e.numtransent,\n"
                        + " e.numnota, e.serie, e.especie, trunc(e.dtemissao) as dtemissao, \n"
                        + " trunc(e.dtent) as dtent, e.vltotal, count(m.codprod) as qtde_itens \n"
                        + " from pcnfent e, pcmov m, pcfornec f \n"
                        + " where e.numtransent = m.numtransent \n"
                        + " and e.especie = 'NF' \n"
                        + " and e.dtcancel is null \n"
                        + " and f.codfornec = e.codfornec \n"
                        + " and m.codoper = 'E' \n"
                        + " and e.codfilial = '" + edtCodFilial.getText() + "' \n"
                        + whereSql
                        + " group by e.codfilial, e.codfornec, f.fornecedor, e.numtransent,\n"
                        + " e.numnota, e.serie, e.especie, e.dtemissao, \n"
                        + " e.dtent, e.vltotal"
                        + " order by e.dtent, e.numtransent";

                tblDados.setTableData(strSelect);

                edtLog.setText("");
                edtLog.append("Lançamentos Encontrados no Winthor -> " + tblDados.getRowCount() + "\n");
            } else {
                MessageDialog.error("Devem ser informados Transação de entrada ou Periodo de Entrada!");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }

    private void atualizaPreco(String filial, String numTransEnt) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            wint.openConectOracle();

            // atualiza tabela de preço do auto serviço
            edtLog.append("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\nAtualizando Regiao 13 - Super Atacado\n");
            String sqlString = "Select KT_ATUALIZA_PVENDA_SPA(PCODFILIAL=>'" + filial
                    + "' ,pnumtransent=>" + numTransEnt + ") as log "
                    + " from dual";
            List lst = wint.selectDados(sqlString);

            for (Object object : lst.toArray()) {
                edtLog.append(object.toString() + "\n");
            }

            // atualiza tabela de preços da transferencia para matriz
            edtLog.append("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\nAtualizando Regiao 14 - Transferencia Matriz\n");
            String sqlTrf = "Select KT_ATUALIZA_PVENDA_TRF(PCODFILIAL=>'" + filial
                    + "' ,pnumtransent=>" + numTransEnt + ") as log "
                    + " from dual";
            List lstTrf = wint.selectDados(sqlTrf);

            for (Object object : lstTrf.toArray()) {
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
        btnPesquisar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtLog = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        btnLimpar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        edtNumTransEnt = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDados = new winthorDb.util.CustomTable();
        btnPrecificar = new javax.swing.JButton();
        edtDataInicial = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        edtDataFinal = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();

        setTitle("Atualização de Custo Filial Destino");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz002");

        jLabel1.setText("Precificação Região 13-14 (Vendas filial 2 para filial 51 e Transferencia Filial 2 para Matriz)");

        jLabel3.setText("* * Rotina Brasil Distribuidora");

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

        edtLog.setColumns(20);
        edtLog.setRows(5);
        edtLog.setWrapStyleWord(true);
        jScrollPane1.setViewportView(edtLog);

        jLabel5.setText("Filial :");

        edtCodFilial.setText("2");
        edtCodFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodFilialActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel7.setText("Transação Entrada:");

        tblDados.setToolTipText("");
        tblDados.setCellSelectionEnabled(true);
        tblDados.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane3.setViewportView(tblDados);

        btnPrecificar.setText("Precificar Região");
        btnPrecificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrecificarActionPerformed(evt);
            }
        });

        jLabel4.setText("Periodo de Entrada:");

        jLabel6.setText("Até");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnPrecificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLimpar)
                                .addGap(15, 15, 15))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnPesquisar)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtNumTransEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(edtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(edtNumTransEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPesquisar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrecificar)
                    .addComponent(btnLimpar)))
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
            if ((!edtCodFilial.getText().isEmpty())) {
                buscaLancamentoWinthor();
                btnPesquisar.setEnabled(false);
                edtCodFilial.setEditable(false);
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: \nDeve ser informado a filial e Periodo de entrada!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();
                btnLimparActionPerformed(evt);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void edtCodFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialActionPerformed

    }//GEN-LAST:event_edtCodFilialActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        btnPesquisar.setEnabled(true);
        edtCodFilial.setEditable(true);
        edtCodFilial.setText("2");
        tblDados.clearTableData();
        edtLog.setText("");
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnPrecificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrecificarActionPerformed
        // TODO add your handling code here:
        try {
            int lstSelect[] = tblDados.getSelectedRows();
            for (int i = 0; i < lstSelect.length; i++) {
                atualizaPreco(tblDados.getConteudoRow("codfilial", lstSelect[i]).toString(),
                        tblDados.getConteudoRow("numtransent", lstSelect[i]).toString());
            }
            MessageDialog.info("Precificação Relaizada com sucesso !!!\n");
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnPrecificarActionPerformed

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
            java.util.logging.Logger.getLogger(Brz017.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Brz017().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnPrecificar;
    private javax.swing.JTextField edtCodFilial;
    private com.toedter.calendar.JDateChooser edtDataFinal;
    private com.toedter.calendar.JDateChooser edtDataInicial;
    private javax.swing.JTextArea edtLog;
    private javax.swing.JTextField edtNumTransEnt;
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
    private winthorDb.util.CustomTable tblDados;
    // End of variables declaration//GEN-END:variables
}
