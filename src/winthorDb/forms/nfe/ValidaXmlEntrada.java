/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms.nfe;

import br.com.sefaz.nfe.schema.v4.TNFe.InfNFe.Det;
import br.com.sefaz.nfe.schema.v4.TNfeProc;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.bean.BeanItemNotaEntrada;
import winthorDb.jpa.dao.DaoItemNotaEntrada;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class ValidaXmlEntrada extends javax.swing.JFrame {

    private final DefaultListModel lista = new DefaultListModel();
    private final ArrayList ListFile = new ArrayList();
    private final IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
    boolean cancel = false;

    /**
     * Creates new form Brz001
     */
    public ValidaXmlEntrada() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    /**
     * List all the files under a directory
     *
     * @param directoryName to be listed
     */
    public void listFiles(String directoryName) {

        File directory = new File(directoryName);
        String nameFile = "";
        //lstArquivos.setModel(null);
        lista.clear();
        ListFile.clear();
        //get all the files from a directory
        
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                nameFile = file.getName();
                if ((!nameFile.contains("(")) || (!nameFile.contains(")"))) {
                    if (nameFile.toLowerCase().endsWith(".xml")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                } else {
                    MessageDialog.error("Não Localizados os XML's na pasta!");
                    txtLog.append("\n >> Nao Processar: " + nameFile);
                }
            }
        }
        lstArquivos.setModel(lista);
        txtLog.append("\n ");
        btnProcessar.setEnabled(true);
    }

        private void processaXmlWinthor() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea

                String fileXml = "";
                TNfeProc nfe = null;
                List <BeanItemNotaEntrada> nota_itens = null;
                List itens_xml = null;
                int i = 0;
                int total = 0;
                int gravou = 0;
                int naogravou = 0;
                NFeXMLImporter importer = new NFeXMLImporter();
                try {
                    total = ListFile.size();
                    txtLog.append("Arquivos a serem processados: " + total);
                    lblProcessados.setText("Gravou: " + gravou + " / Não Gravou: " + naogravou);
                    jProgressBar1.setMaximum(0);
                    jProgressBar1.setMaximum(total);
                    for (Object File1 : ListFile) {
                        jProgressBar1.setValue(i);
                        
                        fileXml = edtArquivoOrigem.getText() + "\\" + File1.toString();
                        txtLog.append("\n" + Formato.replicate("-", 50));
                        txtLog.append("\nXML-> " + i++ + " >> " + fileXml);
                        nfe = null;
                        if (!fileXml.isEmpty()) {
                            nfe = importer.importarXML(fileXml);
                            if (nfe != null) {
                                itens_xml = nfe.getNFe().getInfNFe().getDet();
                                // 1 - localizar dados do xml no winthor
                                DaoItemNotaEntrada nota = new DaoItemNotaEntrada();
                                nota_itens = nota.listarItemNota(edtCodFilial.getText(), nfe.getNFe().getInfNFe().getIde().getNNF());
                                gerarSequencia(nota_itens, itens_xml);
                                // 2 - fazer a alteração da sequencia dos itens
                                nota.atualizar(nota_itens);
                                // 3 - gravar sequencia no winthor
                                
                                gravou++;
                                txtLog.append("\n" + nfe.getNFe().getInfNFe().getIde().getNNF() + " >>> Gravado com sucesso!");
                                
                            } else {
                                naogravou++;
                                txtLog.append("\n****** Erro ao gravar o produto");
                                
                            }
                        } else {
                            txtLog.append("\n****** Erro ao processar imagem");
                        }
                        lblProcessados.setText("Gravou: " + gravou + " / Não Gravou: " + naogravou);
                        lblContador.setText("" + i + " / " + total);
                        txtLog.setCaretPosition(txtLog.getText().length());
                    }
                    MessageDialog.info("Processamento concluido!");

                } catch (Exception e) {
                    trataErro.trataException(e, "processaXmlWinthor");
                }
            }//- Fim do Run
        }.start();//Fim Thread

    }
        
    public void gerarSequencia(List itens_nota, List itens_xml){
        try {
            // passa por cada item do xml para deixar a sequencia;
            for (int i = 0; i < itens_xml.size(); i++) {
                 Det det = (Det) itens_xml.get(i);
                String cProd = det.getProd().getCProd();
                String nItem = det.getNItem();
                // localiza o produto na lista da nota pelo codfabrica
                for (int j = 0; j < itens_nota.size(); j++) {
                    BeanItemNotaEntrada item = (BeanItemNotaEntrada) itens_nota.get(j);
                    if (item.getCodfabrica().equalsIgnoreCase(cProd)){
                        item.setNovonumseq(Long.valueOf(nItem));
                    }
                }
            }
        } catch (NumberFormatException e) {
                    trataErro.trataException(e, "gerarSequencia");
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
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        btnAbrirOrigem = new javax.swing.JButton();
        edtArquivoOrigem = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnProcessar = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstArquivos = new javax.swing.JList();
        lblContador = new javax.swing.JLabel();
        lblProcessados = new javax.swing.JLabel();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N

        jLabel1.setText("XML Entrada de Mercadorias");

        jLabel3.setText("-> Organização da sequencia do XML no winthor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("XML's"));

        jLabel5.setText("Filial:");

        edtCodFilial.setText("1");

        btnAbrirOrigem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/folder.png"))); // NOI18N
        btnAbrirOrigem.setToolTipText("Arquivos XML de origem");
        btnAbrirOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirOrigemActionPerformed(evt);
            }
        });

        edtArquivoOrigem.setText("...");

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/close.png"))); // NOI18N
        btnCancelar.setToolTipText("Cancelar a operação");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/diskette.png"))); // NOI18N
        btnProcessar.setToolTipText("Grava sequencia do XML no banco de dados");
        btnProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessarActionPerformed(evt);
            }
        });

        txtLog.setColumns(20);
        txtLog.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtLog.setRows(5);
        txtLog.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtLog);

        lstArquivos.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquivos"));
        lstArquivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstArquivosMouseClicked(evt);
            }
        });
        lstArquivos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstArquivosValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstArquivos);

        lblContador.setText("..");

        lblProcessados.setText("...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAbrirOrigem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtArquivoOrigem))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnCancelar)
                        .addGap(18, 18, 18)
                        .addComponent(btnProcessar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblContador, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblProcessados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtArquivoOrigem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbrirOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnProcessar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContador, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProcessados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnAbrirOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirOrigemActionPerformed
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                JFileChooser fileChooser = new JFileChooser();
                try {
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setAcceptAllFileFilterUsed(false);

                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        edtArquivoOrigem.setText(selectedFile.getCanonicalPath());
                        listFiles(selectedFile.getCanonicalPath());
                        lstArquivos.updateUI();
                    }
                } catch (HeadlessException | IOException e) {
                    trataErro.trataException(e, "btnAbrirOrigemActionPerformed");
                }
            }//- Fim do Run
        }.start();//Fim Thread      
    }//GEN-LAST:event_btnAbrirOrigemActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.cancel = true;
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessarActionPerformed
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                try {
                    processaXmlWinthor();
                } catch (Exception ex) {
                    trataErro.trataException(ex, "btnProcessarActionPerformed");
                }
            }//- Fim do Run
        }.start();//Fim Thread        

    }//GEN-LAST:event_btnProcessarActionPerformed

    private void lstArquivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstArquivosMouseClicked

    }//GEN-LAST:event_lstArquivosMouseClicked

    private void lstArquivosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstArquivosValueChanged

    }//GEN-LAST:event_lstArquivosValueChanged

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
        java.awt.EventQueue.invokeLater(() -> {
            new ValidaXmlEntrada().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirOrigem;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnProcessar;
    private javax.swing.JTextField edtArquivoOrigem;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblContador;
    private javax.swing.JLabel lblProcessados;
    private javax.swing.JList lstArquivos;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
