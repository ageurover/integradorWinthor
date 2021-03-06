/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.io.File;
import winthorDb.Main;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.CarregaStringConect;
import winthorDb.util.AES;

/**
 *
 * @author ageurover
 */
public class BrzConfigDb extends javax.swing.JFrame {

    /**
     * Creates new form BrzConfigDb
     */
    public BrzConfigDb() {
        initComponents();
        initGuid();
    }

    private void initGuid() {
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());

        File fileCfgDb = new File(Main.xmlConectDb);
        if (fileCfgDb.exists()) {
            conectDbToForm();
        } else {
            limpaForm();
        }
    }

    private void limpaForm() {
        edtIpServidor.setText("");
        edtPortaDb.setText("");
        edtSenhaDb.setText("");
        edtSidDb.setText("");
        edtTipoConect.setText("");
        edtUsuarioDb.setText("");
        edtCodConsumidor.setText("");
        edtCodFilial.setText("");
        edtCodFilialFatura.setText("");
        edtLicenca.setText("");
        edtQtdeUsuarios.setText("");
        edtDataExpiracao.setText("");
        edtCnpjMatriz.setText("");
        edtQtdeUsuarios.setText("");
        edtDataExpiracao.setText("");
        edtValorMaxCupom.setText("");
        edtAjustaFrenteLoja.setText("");
        edtPastaImagens.setText("");
    }

    private void conectDbToForm() {
        try {
            limpaForm();
            CarregaStringConect.getConectDbXml();
            edtIpServidor.setText(CarregaStringConect.conectDb.getIpServidorDb());
            edtPortaDb.setText(CarregaStringConect.conectDb.getPortaSerividor());

            edtUsuarioDb.setText(CarregaStringConect.conectDb.getUsuarioDb());
            edtSenhaDb.setText(CarregaStringConect.conectDb.getSenhaDb());
            edtSidDb.setText(CarregaStringConect.conectDb.getSidServidorDb());
            edtTipoConect.setText(CarregaStringConect.getTipoServidor());
            edtCodConsumidor.setText(CarregaStringConect.conectDb.getCodConsumidorDb());
            edtCodFilial.setText(CarregaStringConect.conectDb.getCodFilial());
            edtCodFilialFatura.setText(CarregaStringConect.conectDb.getCodFilialFatura());
            edtCnpjMatriz.setText(CarregaStringConect.conectDb.getCnpjMatriz());
            edtLicenca.setText(CarregaStringConect.conectDb.getLicenca());
            edtQtdeUsuarios.setText(CarregaStringConect.conectDb.getQtdeMaxUsuario());
            edtDataExpiracao.setText(CarregaStringConect.conectDb.getDataExpiracao());
            edtValorMaxCupom.setText(CarregaStringConect.conectDb.getValorMaxCupom().toString());
            edtAjustaFrenteLoja.setText(CarregaStringConect.conectDb.getAjustaFrenteLoja());
            edtPastaImagens.setText(CarregaStringConect.conectDb.getPastaImagens());

        } catch (Exception ex) {
            trataErro.trataException(ex, "conectDbToForm");
        }
    }

    private void formToConectDb() {
        try {
            CarregaStringConect.conectDb.setIpServidorDb(edtIpServidor.getText());
            CarregaStringConect.conectDb.setPortaSerividor(edtPortaDb.getText());
            CarregaStringConect.conectDb.setSenhaDb(edtSenhaDb.getText());
            CarregaStringConect.conectDb.setSidServidorDb(edtSidDb.getText());
            CarregaStringConect.conectDb.setTipoConectServidorDb(edtTipoConect.getText());
            CarregaStringConect.conectDb.setUsuarioDb(edtUsuarioDb.getText());
            CarregaStringConect.conectDb.setCodConsumidorDb(edtCodConsumidor.getText());
            CarregaStringConect.conectDb.setCodFilial(edtCodFilial.getText());
            CarregaStringConect.conectDb.setCodFilialFatura(edtCodFilialFatura.getText());
            CarregaStringConect.conectDb.setLicenca(edtLicenca.getText());
            CarregaStringConect.conectDb.setCnpjMatriz(edtCnpjMatriz.getText());
            CarregaStringConect.conectDb.setQtdeMaxUsuario(edtQtdeUsuarios.getText());
            CarregaStringConect.conectDb.setDataExpiracao(edtDataExpiracao.getText());
            CarregaStringConect.conectDb.setValorMaxCupom(Double.parseDouble(edtValorMaxCupom.getText()));
            CarregaStringConect.conectDb.setAjustaFrenteLoja(edtAjustaFrenteLoja.getText());
            CarregaStringConect.conectDb.setPastaImagens(edtPastaImagens.getText());
            CarregaStringConect.setStringConectDb();
        } catch (NumberFormatException ex) {
            trataErro.trataException(ex, "formToConectDb");
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        edtIpServidor = new javax.swing.JTextField();
        edtUsuarioDb = new javax.swing.JTextField();
        edtSenhaDb = new javax.swing.JTextField();
        edtSidDb = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        btnCarregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        edtCodConsumidor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        edtCodFilialFatura = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        edtLicenca = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        edtCnpjMatriz = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        edtAjustaFrenteLoja = new javax.swing.JTextField();
        edtValorMaxCupom = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        edtPastaImagens = new javax.swing.JTextField();
        btnLerLicenca = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        edtDataExpiracao = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        edtQtdeUsuarios = new javax.swing.JTextField();
        btnFechar = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        edtPortaDb = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        edtTipoConect = new javax.swing.JTextField();

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N

        jLabel1.setText("Conex�o com o Banco de Dados Oracle");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setText("IP do Servidor:");

        jLabel4.setText("Usuario Db:");

        jLabel5.setText("Senha Db:");

        jLabel6.setText("SID Db:");

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnCarregar.setText("Carregar");
        btnCarregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarregarActionPerformed(evt);
            }
        });

        jLabel7.setText("ConectDb");

        jLabel8.setText("Codigo Consumidor:");

        jLabel9.setText("Filial:");

        jLabel10.setText("Filial Fatura:");

        jLabel11.setText("Licen�a uso:");

        jLabel12.setText("CNPJ Matriz");

        edtCnpjMatriz.setEditable(false);

        jLabel13.setText("Valor maximo cupom Fiscal:");

        jLabel14.setText("Ajustar Estoque Frente Loja (S/N):");

        edtAjustaFrenteLoja.setText("N");

        edtValorMaxCupom.setText("0.00");

        jLabel15.setText("Caminho Imagens:");

        btnLerLicenca.setText("->");
        btnLerLicenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLerLicencaActionPerformed(evt);
            }
        });

        jLabel16.setText("Data Expira�ao:");

        edtDataExpiracao.setEditable(false);

        jLabel17.setText("Limite Usuarios:");

        edtQtdeUsuarios.setEditable(false);

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jLabel18.setText("Porta:");

        edtPortaDb.setText("1521");

        jLabel19.setText("Tipo");

        edtTipoConect.setText("SID");
        edtTipoConect.setToolTipText("SID / SERVICE_NAME");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(edtLicenca)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLerLicenca, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(edtCnpjMatriz, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtQtdeUsuarios))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtPastaImagens))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCarregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalvar)
                        .addGap(29, 29, 29)
                        .addComponent(btnFechar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtAjustaFrenteLoja, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodConsumidor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodFilialFatura, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtValorMaxCupom, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(edtIpServidor, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                                    .addComponent(edtUsuarioDb))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtSidDb, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel19))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtSenhaDb, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(edtTipoConect, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtPortaDb)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(edtIpServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(edtSidDb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(edtTipoConect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtUsuarioDb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(edtSenhaDb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(edtPortaDb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(edtAjustaFrenteLoja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(edtCodConsumidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(edtCodFilialFatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(edtValorMaxCupom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(edtPastaImagens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(edtLicenca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLerLicenca))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(edtCnpjMatriz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(edtDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(edtQtdeUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCarregar)
                    .addComponent(btnSalvar)
                    .addComponent(btnFechar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCarregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarregarActionPerformed
        conectDbToForm();
    }//GEN-LAST:event_btnCarregarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        formToConectDb();
        MessageDialog.saveSucess();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnLerLicencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLerLicencaActionPerformed
        // TODO add your handling code here:
        String keySecret = Main.SECRETKEY;
        String decryptedString = AES.decrypt(edtLicenca.getText(), keySecret);
        if (decryptedString != null) {
            String decodeKey[] = decryptedString.split(";");
            if (decodeKey.length == 3) {
                String decodeKeyCnpj = decodeKey[0];
                String decodeKeyDataExp = decodeKey[1];
                String decodeKeyQtdeUser = decodeKey[2];
                edtCnpjMatriz.setText(decodeKeyCnpj);
                edtDataExpiracao.setText(decodeKeyDataExp);
                edtQtdeUsuarios.setText(decodeKeyQtdeUser);
            } else {
                MessageDialog.error("Erro ao ler a chave de acesso!");
            }
        } else {
            MessageDialog.error("chave de acesso invlida!");
        }

    }//GEN-LAST:event_btnLerLicencaActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BrzConfigDb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BrzConfigDb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BrzConfigDb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BrzConfigDb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BrzConfigDb().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCarregar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnLerLicenca;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JTextField edtAjustaFrenteLoja;
    private javax.swing.JTextField edtCnpjMatriz;
    private javax.swing.JTextField edtCodConsumidor;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtCodFilialFatura;
    private javax.swing.JTextField edtDataExpiracao;
    private javax.swing.JTextField edtIpServidor;
    private javax.swing.JTextField edtLicenca;
    private javax.swing.JTextField edtPastaImagens;
    private javax.swing.JTextField edtPortaDb;
    private javax.swing.JTextField edtQtdeUsuarios;
    private javax.swing.JTextField edtSenhaDb;
    private javax.swing.JTextField edtSidDb;
    private javax.swing.JTextField edtTipoConect;
    private javax.swing.JTextField edtUsuarioDb;
    private javax.swing.JTextField edtValorMaxCupom;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
