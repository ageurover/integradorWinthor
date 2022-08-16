/*
 * LayoutDocSqlDialog.java
 *
 * Created on 22 de Outubro de 2018, 22:10
 */
package winthorDb.forms.export;

import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.LayoutDocSql;
import winthorDb.jpa.dao.Dao;
import winthorDb.util.Formato;

/**
 *
 * @author Ageu Elias Rover
 */
public class LayoutDocSqlDialog extends javax.swing.JDialog {

    private LayoutDocSql sqlEdit = null;

    public static void open(String idDoc, String tipoDoc) {
        new LayoutDocSqlDialog(null, true, idDoc, tipoDoc).setVisible(true);
    }

    /**
     * Creates new form CfopDialog
     *
     * @param parent
     * @param modal
     * @param idDoc
     * @param tipoDoc
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LayoutDocSqlDialog(java.awt.Frame parent, boolean modal, String idDoc, String tipoDoc) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);
        setTitle("Comando SQL");

        edtCodDoc.setText(idDoc);
        edtTipoDoc.setText(tipoDoc);

        exibeSqlComando();
    }

    private void exibeSqlComando() {
        String sqlString = "";
        try {
            tblConsulta.clearTableData();
            if ((!edtCodDoc.getText().isEmpty()) && (!edtTipoDoc.getText().isEmpty())) {
                sqlString = "SELECT "
                        + " ('' ||id) as id, ('' ||idDoc) as idDoc, ('' ||tipoDoc) as tipoDoc, "
                        + " ('' ||sql_Header) as sql_Header, "
                        + " ('' ||sql_Detalhe_n0) as sql_Detalhe_n0, "
                        + " ('' ||sql_Detalhe_n1) as sql_Detalhe_n1, "
                        + " ('' ||sql_Detalhe_n2) as sql_Detalhe_n2, "
                        + " ('' ||sql_Detalhe_n3) as sql_Detalhe_n3, "
                        + " ('' ||sql_Detalhe_n4) as sql_Detalhe_n4, "
                        + " ('' ||sql_Detalhe_n5) as sql_Detalhe_n5, "
                        + " ('' ||sql_Treller) as sql_Treller, "
                        + " ('' ||sql_extra_1) as sql_Extra_1, "
                        + " ('' ||sql_extra_2) as sql_Extra_2 "
                        + " FROM LayoutDocSql"
                        + " WHERE idDoc = " + edtCodDoc.getText()
                        + " AND tipoDoc = '" + edtTipoDoc.getText() + "' ";

                tblConsulta.setTableData(sqlString);

                if (tblConsulta.getRowCount() >= 1) {
                    tblConsulta.clearSelection();
                    tblConsulta.changeSelection(0, 0, false, false);
                    tblConsulta.grabFocus();
                    tblConsulta.requestFocus();
                    mostraDados();
                } else {
                    sqlEdit = new LayoutDocSql();
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "exibeSqlComando");
        }
    }

    private void mostraDados() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                String idSql = "";
                try {
                    idSql = tblConsulta.getConteudoRowSelected("id").toString();

                    if (idSql != null) {
                        if ((!idSql.isEmpty()) || (!idSql.equals("0"))) {

                            sqlEdit = Dao.get(LayoutDocSql.class, Formato.strToInt(idSql));
                            if (sqlEdit != null) {
                                edtId.setText(idSql);
                                txtHeader.setText(sqlEdit.getSql_Header());
                                txtDetalheN0.setText(sqlEdit.getSqlDetalhe_n0());
                                txtDetalheN1.setText(sqlEdit.getSqlDetalhe_n1());
                                txtDetalheN2.setText(sqlEdit.getSqlDetalhe_n2());
                                txtDetalheN3.setText(sqlEdit.getSqlDetalhe_n3());
                                txtDetalheN4.setText(sqlEdit.getSqlDetalhe_n4());
                                txtDetalheN5.setText(sqlEdit.getSqlDetalhe_n5());
                                txtTreller.setText(sqlEdit.getSql_Treller());
                                txtExtra.setText(sqlEdit.getSqlExtra1());
                                txtExtra2.setText(sqlEdit.getSqlExtra2());
                            }
                        }
                    }
                } catch (Exception ex) {
                    trataErro.trataException(ex, "mostraDados");
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void formToSqlEdit() throws Exception {
        if (sqlEdit != null) {
            sqlEdit.setSql_Header(txtHeader.getText());
            sqlEdit.setSqlDetalhe_n0(txtDetalheN0.getText());
            sqlEdit.setSqlDetalhe_n1(txtDetalheN1.getText());
            sqlEdit.setSqlDetalhe_n2(txtDetalheN2.getText());
            sqlEdit.setSqlDetalhe_n3(txtDetalheN3.getText());
            sqlEdit.setSqlDetalhe_n4(txtDetalheN4.getText());
            sqlEdit.setSqlDetalhe_n5(txtDetalheN5.getText());
            sqlEdit.setSql_Treller(txtTreller.getText());
            sqlEdit.setSqlExtra1(txtExtra.getText());
            sqlEdit.setSqlExtra2(txtExtra2.getText());
        } else {
            throw new Exception("Objeto não declado!");

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        edtCodDoc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        edtId = new javax.swing.JTextField();
        edtTipoDoc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtHeader = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetalheN0 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDetalheN1 = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDetalheN2 = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtDetalheN3 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtDetalheN4 = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtDetalheN5 = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtExtra = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtExtra2 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtTreller = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblConsulta = new winthorDb.util.CustomTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Código Documento");

        edtCodDoc.setEditable(false);
        edtCodDoc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtCodDocFocusLost(evt);
            }
        });

        jLabel4.setText("ID:");

        edtId.setEditable(false);

        edtTipoDoc.setEditable(false);
        edtTipoDoc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtTipoDocFocusLost(evt);
            }
        });

        jLabel2.setText("Tipo Documento");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(edtId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(edtCodDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(edtTipoDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(edtTipoDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(jLabel4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(edtCodDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(edtId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnSalvar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancelar))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(btnCancelar)
                .add(btnSalvar))
        );

        txtHeader.setColumns(20);
        txtHeader.setLineWrap(true);
        txtHeader.setRows(5);
        txtHeader.setToolTipText("Consulta SQL para o HEADER");
        txtHeader.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtHeader);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SQL Header", jPanel3);

        txtDetalheN0.setColumns(20);
        txtDetalheN0.setLineWrap(true);
        txtDetalheN0.setRows(5);
        txtDetalheN0.setToolTipText("SQL comando para o DETALHE");
        txtDetalheN0.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtDetalheN0);

        jTabbedPane2.addTab("Detalhe nivel 0", jScrollPane2);

        jScrollPane5.setViewportView(txtDetalheN1);

        jTabbedPane2.addTab("Detalhe nivel 1", jScrollPane5);

        jScrollPane6.setViewportView(txtDetalheN2);

        jTabbedPane2.addTab("Detalhe nivel 2", jScrollPane6);

        txtDetalheN3.setColumns(20);
        txtDetalheN3.setLineWrap(true);
        txtDetalheN3.setRows(5);
        txtDetalheN3.setToolTipText("SQL comando para o DETALHE");
        txtDetalheN3.setWrapStyleWord(true);
        jScrollPane7.setViewportView(txtDetalheN3);

        jTabbedPane2.addTab("Detalhe nivel 3", jScrollPane7);

        txtDetalheN4.setColumns(20);
        txtDetalheN4.setLineWrap(true);
        txtDetalheN4.setRows(5);
        txtDetalheN4.setToolTipText("SQL comando para o DETALHE");
        txtDetalheN4.setWrapStyleWord(true);
        jScrollPane8.setViewportView(txtDetalheN4);

        jTabbedPane2.addTab("Detalhe nivel 4", jScrollPane8);

        txtDetalheN5.setColumns(20);
        txtDetalheN5.setLineWrap(true);
        txtDetalheN5.setRows(5);
        txtDetalheN5.setToolTipText("SQL comando para o DETALHE");
        txtDetalheN5.setWrapStyleWord(true);
        jScrollPane9.setViewportView(txtDetalheN5);

        jTabbedPane2.addTab("Detalhe nivel 5", jScrollPane9);

        txtExtra.setColumns(20);
        txtExtra.setLineWrap(true);
        txtExtra.setRows(5);
        txtExtra.setToolTipText("SQL comando para o Extra");
        txtExtra.setWrapStyleWord(true);
        jScrollPane10.setViewportView(txtExtra);

        jTabbedPane2.addTab("Extra", jScrollPane10);

        txtExtra2.setColumns(20);
        txtExtra2.setLineWrap(true);
        txtExtra2.setRows(5);
        txtExtra2.setToolTipText("SQL comando para o Extra");
        txtExtra2.setWrapStyleWord(true);
        jScrollPane11.setViewportView(txtExtra2);

        jTabbedPane2.addTab("Extra 2", jScrollPane11);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("SQL Detalhe", jPanel4);

        txtTreller.setColumns(20);
        txtTreller.setRows(5);
        jScrollPane3.setViewportView(txtTreller);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SQL Treller", jPanel5);

        tblConsulta.setToolTipText("");
        tblConsulta.setCellSelectionEnabled(true);
        tblConsulta.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane4.setViewportView(tblConsulta);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dados", jPanel6);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        sqlEdit = null;
        dispose();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            if (edtId.getText().isEmpty()) {
                sqlEdit = new LayoutDocSql();
                sqlEdit.setIdDoc(Formato.strToInt(edtCodDoc.getText()));
                sqlEdit.setTipoDoc(edtTipoDoc.getText());
            }

            formToSqlEdit();
            Dao.saveOrUpdate(sqlEdit);

            MessageDialog.saveSucess();

        } catch (Exception e) {
            trataErro.trataException(e, "btnSalvarActionPerformed");
        }

        sqlEdit = null;
        dispose();

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void edtCodDocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCodDocFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodDocFocusLost

    private void edtTipoDocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtTipoDocFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_edtTipoDocFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LayoutDocSqlDialog(new javax.swing.JFrame(), true, null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JTextField edtCodDoc;
    private javax.swing.JTextField edtId;
    private javax.swing.JTextField edtTipoDoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private winthorDb.util.CustomTable tblConsulta;
    private javax.swing.JTextArea txtDetalheN0;
    private javax.swing.JTextPane txtDetalheN1;
    private javax.swing.JTextPane txtDetalheN2;
    private javax.swing.JTextArea txtDetalheN3;
    private javax.swing.JTextArea txtDetalheN4;
    private javax.swing.JTextArea txtDetalheN5;
    private javax.swing.JTextArea txtExtra;
    private javax.swing.JTextArea txtExtra2;
    private javax.swing.JTextArea txtHeader;
    private javax.swing.JTextArea txtTreller;
    // End of variables declaration//GEN-END:variables

}
