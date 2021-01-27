/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.util.logging.Level;
import java.util.logging.Logger;
import winthorDb.Main;
import winthorDb.forms.coletor.consultaOS;
import winthorDb.forms.coletor.vinculaEnderecoLoja;
import winthorDb.forms.export.ExportDocDialog;

/**
 *
 * @author ageurover
 */
public class Brz000 extends javax.swing.JFrame {

    /**
     * Creates new form Brz000
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Brz000() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuRotinas = new javax.swing.JMenu();
        mnuProcessoPedidoCupom = new javax.swing.JMenuItem();
        mnuProcessoPedidoCarga = new javax.swing.JMenuItem();
        mnuFilialFaturaCarga = new javax.swing.JMenuItem();
        mnuProcessoDevolucaoCupom = new javax.swing.JMenuItem();
        mnuConversaoEmbalagem = new javax.swing.JMenuItem();
        mnuProcessarImagens = new javax.swing.JMenuItem();
        mnuPedidoViradaMes = new javax.swing.JMenuItem();
        mnuAjusteEstoqueFilial90 = new javax.swing.JMenuItem();
        mnuBaixaLancProducao = new javax.swing.JMenuItem();
        mnuValidaEan = new javax.swing.JMenuItem();
        mnuExportaProtesto = new javax.swing.JMenuItem();
        mnuImportaFolhaCnab240 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        mnuColetor = new javax.swing.JMenu();
        mnuConsultaOs = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        mnuConfig = new javax.swing.JMenu();
        mnuConfigDb = new javax.swing.JMenuItem();

        jMenu3.setText("jMenu3");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Intergrado (Rover Tecnologia de informa��o) -> Vers�o: 1.0.7");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel1.setText("www.rovertecnologia.com.br");

        jMenuRotinas.setText("Winthor");

        mnuProcessoPedidoCupom.setText("Processo Pedido/Cupom (Pedido)");
        mnuProcessoPedidoCupom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessoPedidoCupomActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuProcessoPedidoCupom);

        mnuProcessoPedidoCarga.setText("Processo Pedido/Cupom (Carga)");
        mnuProcessoPedidoCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessoPedidoCargaActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuProcessoPedidoCarga);

        mnuFilialFaturaCarga.setText("Processo Filial Fatura por Carregamento");
        mnuFilialFaturaCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFilialFaturaCargaActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuFilialFaturaCarga);

        mnuProcessoDevolucaoCupom.setText("Processo Devolu��o Cupom Fiscal");
        mnuProcessoDevolucaoCupom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessoDevolucaoCupomActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuProcessoDevolucaoCupom);

        mnuConversaoEmbalagem.setText("Convers�o de Embagem (Master/Unidade)");
        mnuConversaoEmbalagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConversaoEmbalagemActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuConversaoEmbalagem);

        mnuProcessarImagens.setText("Processar Imagens de Produtos");
        mnuProcessarImagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessarImagensActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuProcessarImagens);

        mnuPedidoViradaMes.setText("Ajusta Data Pedido Virada M�s");
        mnuPedidoViradaMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPedidoViradaMesActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuPedidoViradaMes);

        mnuAjusteEstoqueFilial90.setText("Ajuste Estoque Filial 90 para Filial Retira");
        mnuAjusteEstoqueFilial90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAjusteEstoqueFilial90ActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuAjusteEstoqueFilial90);

        mnuBaixaLancProducao.setText("Baixa Lan�amento Entrada Producao");
        mnuBaixaLancProducao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBaixaLancProducaoActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuBaixaLancProducao);

        mnuValidaEan.setText("Valida��o EAN 8/13");
        mnuValidaEan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuValidaEanActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuValidaEan);

        mnuExportaProtesto.setText("Exporta��o Dados Protesto de Titulos");
        mnuExportaProtesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExportaProtestoActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuExportaProtesto);

        mnuImportaFolhaCnab240.setText("Importa��o Folha Pagamento (CNAB-240)");
        mnuImportaFolhaCnab240.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuImportaFolhaCnab240ActionPerformed(evt);
            }
        });
        jMenuRotinas.add(mnuImportaFolhaCnab240);

        jMenuItem3.setText("Cadastro de Cliente Sintegra");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuRotinas.add(jMenuItem3);

        jMenuBar1.add(jMenuRotinas);

        mnuColetor.setText("Coletor");

        mnuConsultaOs.setText("Consulta OS");
        mnuConsultaOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConsultaOsActionPerformed(evt);
            }
        });
        mnuColetor.add(mnuConsultaOs);

        jMenuItem4.setText("Endere�o Loja");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuColetor.add(jMenuItem4);

        jMenuBar1.add(mnuColetor);

        mnuConfig.setText("Configura��o");

        mnuConfigDb.setText("Conex�o Banco de Dados");
        mnuConfigDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigDbActionPerformed(evt);
            }
        });
        mnuConfig.add(mnuConfigDb);

        jMenuBar1.add(mnuConfig);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuConfigDbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConfigDbActionPerformed
        Main.dialog = new BrzConfigDb();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuConfigDbActionPerformed

    private void mnuProcessoPedidoCupomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessoPedidoCupomActionPerformed
        Main.dialog = new Brz003();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuProcessoPedidoCupomActionPerformed

    private void mnuConversaoEmbalagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConversaoEmbalagemActionPerformed
        Main.dialog = new Brz005();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuConversaoEmbalagemActionPerformed

    private void mnuProcessarImagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessarImagensActionPerformed
        try {
            Main.dialog = new Brz006();
        } catch (Exception ex) {
            Logger.getLogger(Brz000.class.getName()).log(Level.SEVERE, null, ex);
        }
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuProcessarImagensActionPerformed

    private void mnuProcessoDevolucaoCupomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessoDevolucaoCupomActionPerformed
        Main.dialog = new Brz007();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuProcessoDevolucaoCupomActionPerformed

    private void mnuProcessoPedidoCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessoPedidoCargaActionPerformed
        Main.dialog = new Brz009();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuProcessoPedidoCargaActionPerformed

    private void mnuPedidoViradaMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPedidoViradaMesActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz010();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuPedidoViradaMesActionPerformed

    private void mnuAjusteEstoqueFilial90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAjusteEstoqueFilial90ActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz004();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuAjusteEstoqueFilial90ActionPerformed

    private void mnuConsultaOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultaOsActionPerformed
        Main.dialog = new consultaOS();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuConsultaOsActionPerformed

    private void mnuFilialFaturaCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFilialFaturaCargaActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz0012();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuFilialFaturaCargaActionPerformed

    private void mnuBaixaLancProducaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBaixaLancProducaoActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz002();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuBaixaLancProducaoActionPerformed

    private void mnuValidaEanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuValidaEanActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz011();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuValidaEanActionPerformed

    private void mnuExportaProtestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportaProtestoActionPerformed
        // TODO add your handling code here:
        ExportDocDialog.open(9999, "CART", "0");
    }//GEN-LAST:event_mnuExportaProtestoActionPerformed

    private void mnuImportaFolhaCnab240ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuImportaFolhaCnab240ActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz013();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_mnuImportaFolhaCnab240ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        Main.dialog = new Brz008();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Main.dialog = new vinculaEnderecoLoja();
        Main.dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

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
            java.util.logging.Logger.getLogger(Brz000.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Brz000.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Brz000.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Brz000.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz000().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenu jMenuRotinas;
    private javax.swing.JMenuItem mnuAjusteEstoqueFilial90;
    private javax.swing.JMenuItem mnuBaixaLancProducao;
    private javax.swing.JMenu mnuColetor;
    private javax.swing.JMenu mnuConfig;
    private javax.swing.JMenuItem mnuConfigDb;
    private javax.swing.JMenuItem mnuConsultaOs;
    private javax.swing.JMenuItem mnuConversaoEmbalagem;
    private javax.swing.JMenuItem mnuExportaProtesto;
    private javax.swing.JMenuItem mnuFilialFaturaCarga;
    private javax.swing.JMenuItem mnuImportaFolhaCnab240;
    private javax.swing.JMenuItem mnuPedidoViradaMes;
    private javax.swing.JMenuItem mnuProcessarImagens;
    private javax.swing.JMenuItem mnuProcessoDevolucaoCupom;
    private javax.swing.JMenuItem mnuProcessoPedidoCarga;
    private javax.swing.JMenuItem mnuProcessoPedidoCupom;
    private javax.swing.JMenuItem mnuValidaEan;
    // End of variables declaration//GEN-END:variables
}
