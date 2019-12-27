/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import winthorDb.Main;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz006 extends javax.swing.JFrame {

    int qtdCheque = 0;
    private final DefaultListModel lista = new DefaultListModel();
    private final ArrayList ListFile = new ArrayList();
    private final IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

    /**
     * Creates new form Brz001
     *
     * @throws java.lang.Exception
     */
    public Brz006() throws Exception {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        btnProcessar.setEnabled(false);
        wint.openConectOracle();
        edtPastaImagens.setText(Main.PastaImagens);
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
                    if (nameFile.toLowerCase().endsWith(".jpg")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                    if (nameFile.toLowerCase().endsWith(".png")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                    if (nameFile.toLowerCase().endsWith(".bmp")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                    if (nameFile.toLowerCase().endsWith(".gif")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                    if (nameFile.toLowerCase().endsWith(".jpeg")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                    if (nameFile.toLowerCase().endsWith(".ani")) {
                        lista.addElement(file.getName());
                        ListFile.add(file.getName());
                    }
                } else {
                    txtLog.append("\n >> Nao Processar: " + nameFile);
                }
            }
        }
        lstArquivos.setModel(lista);
        txtLog.append("\n ");
        btnProcessar.setEnabled(true);
    }

    private int updateImagemToProdut(String idProduto, String imagem) throws Exception {

        String strUpdate = "";
        int result = 0;
        try {
            if (chkCodigoProduto.isSelected()) {
                if (chkSustituirExistente.isSelected()) {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE CODPROD = " + idProduto;
                } else {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE dirfotoprod is null and CODPROD = " + idProduto;
                }
            }
            if (chkCodigoFabrica.isSelected()) {
                if (chkSustituirExistente.isSelected()) {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE CODFAB = '" + idProduto + "' ";
                } else {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE dirfotoprod is null and CODFAB = '" + idProduto + "' ";
                }
            }
            if (chkCodigoBarras.isSelected()) {
                if (chkSustituirExistente.isSelected()) {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE CODAUXILIAR = '" + idProduto + "' ";
                } else {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE dirfotoprod is null and CODAUXILIAR = '" + idProduto + "' ";
                }
            }
            if (chkCodigoBarras2014.isSelected()) {
                if (chkSustituirExistente.isSelected()) {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE CODPROD in (SELECT CODPROD FROM PCEMBALAGEM WHERE CODAUXILIAR =  '" + idProduto + "' ) ";
                } else {
                    strUpdate = "UPDATE PCPRODUT SET dirfotoprod = '" + imagem + "' WHERE dirfotoprod is null and CODPROD in (SELECT CODPROD FROM PCEMBALAGEM WHERE CODAUXILIAR =  '" + idProduto + "' ) ";
                }
            }

            txtLog.append("\nComando: " + strUpdate);
            result = wint.updateDados(strUpdate);

        } catch (Exception ex) {
            trataErro.trataException(ex);
            throw ex;
        }
        return result;
    }

    private void processaWinthor() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                String codprod = "";
                String imagem = "";
                int result = 0;
                int i = 0;
                int total = 0;
                int gravou = 0;
                int naogravou = 0;
                try {
                    total = ListFile.size();
                    txtLog.append("Arquivos a serem processados: " + total);
                    lblProcessados.setText("Gravou: " + gravou + " / Não Gravou: " + naogravou);
                    jProgressBar1.setMaximum(0);
                    jProgressBar1.setMaximum(total);
                    for (Object File1 : ListFile) {
                        jProgressBar1.setValue(i);
                        codprod = Formato.somenteNumeros(File1.toString());
                        imagem = edtPastaImagens.getText() + "\\" + File1.toString();
                        txtLog.append("\n" + Formato.replicate("-", 50));
                        txtLog.append("\nItem: " + i++ + " Produto: " + codprod + " >> imagem: " + imagem);
                        if (!codprod.isEmpty()) {
                            result = updateImagemToProdut(codprod, imagem);
                            if (result == 0) {
                                naogravou++;
                                txtLog.append("\n****** Erro ao gravar o produto");
                            } else {
                                gravou++;
                                txtLog.append("\n" + result + " >>> Gravado com sucesso!");
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
                    trataErro.trataException(e, "processaWinthor");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        edtPathImagens = new javax.swing.JTextField();
        btnBuscaPath = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstArquivos = new javax.swing.JList();
        btnProcessar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        lblImagem = new javax.swing.JLabel();
        lblContador = new javax.swing.JLabel();
        lblProcessados = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        edtPastaImagens = new javax.swing.JTextField();
        chkSustituirExistente = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        chkCodigoProduto = new javax.swing.JCheckBox();
        chkCodigoBarras = new javax.swing.JCheckBox();
        chkCodigoFabrica = new javax.swing.JCheckBox();
        jProgressBar1 = new javax.swing.JProgressBar();
        chkCodigoBarras2014 = new javax.swing.JCheckBox();

        setTitle("Vincular imagem ao produto");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz006");

        jLabel1.setText("Vincular Imagens aos Produtos no winthor");

        jLabel3.setText("* * Informar a pasta onde estão as imagens e processar! ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados para processamento"));

        jLabel4.setText("Pasta de Origem:");

        btnBuscaPath.setText("...");
        btnBuscaPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaPathActionPerformed(evt);
            }
        });

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

        btnProcessar.setText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessarActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Imagem"));

        lblImagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagem.setToolTipText("");
        lblImagem.setAutoscrolls(true);
        jScrollPane3.setViewportView(lblImagem);

        lblContador.setText("..");

        lblProcessados.setText("...");

        jLabel5.setText("Pasta Destino Winthor:");

        chkSustituirExistente.setText("Substituir o existente ?");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(btnProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(chkSustituirExistente)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(edtPastaImagens)
                                    .addComponent(lblProcessados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblContador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtPathImagens)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscaPath, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtPathImagens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscaPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(edtPastaImagens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(chkSustituirExistente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addComponent(lblProcessados)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblContador, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnProcessar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        buttonGroup1.add(chkCodigoProduto);
        chkCodigoProduto.setSelected(true);
        chkCodigoProduto.setText("Codigo Produto");
        chkCodigoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCodigoProdutoActionPerformed(evt);
            }
        });

        buttonGroup1.add(chkCodigoBarras);
        chkCodigoBarras.setText("Codigo Barras EAN");
        chkCodigoBarras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCodigoBarrasActionPerformed(evt);
            }
        });

        buttonGroup1.add(chkCodigoFabrica);
        chkCodigoFabrica.setText("Codigo Fabrica");
        chkCodigoFabrica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCodigoFabricaActionPerformed(evt);
            }
        });

        jProgressBar1.setStringPainted(true);

        buttonGroup1.add(chkCodigoBarras2014);
        chkCodigoBarras2014.setText("Codigo Barras EAN (2014/292)");
        chkCodigoBarras2014.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCodigoBarras2014ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkCodigoBarras2014)
                                .addGap(18, 18, 18)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkCodigoProduto)
                                .addGap(18, 18, 18)
                                .addComponent(chkCodigoFabrica)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkCodigoBarras)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkCodigoProduto)
                            .addComponent(chkCodigoFabrica)
                            .addComponent(chkCodigoBarras))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkCodigoBarras2014))))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            wint.closeConectOracle();
        } catch (Exception e) {
            trataErro.trataException(e, "formWindowClosed");
        }
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnBuscaPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaPathActionPerformed
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
                        edtPathImagens.setText(selectedFile.getCanonicalPath());
                        listFiles(selectedFile.getCanonicalPath());
                        lstArquivos.updateUI();
                    }
                } catch (HeadlessException | IOException e) {
                    trataErro.trataException(e, "btnBuscaPathActionPerformed");
                }
            }//- Fim do Run
        }.start();//Fim Thread

    }//GEN-LAST:event_btnBuscaPathActionPerformed

    private void lstArquivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstArquivosMouseClicked

    }//GEN-LAST:event_lstArquivosMouseClicked

    private void lstArquivosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstArquivosValueChanged
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                try {
                    ImageIcon img = Formato.setImagemDimensao(edtPathImagens.getText() + "/" + lstArquivos.getSelectedValue().toString(), lblImagem.getWidth(), lblImagem.getHeight());
                    lblImagem.setIcon(img);
                } catch (Exception e) {
                    trataErro.trataException(e, "lstArquivosMouseClicked");
                }
            }//- Fim do Run
        }.start();//Fim Thread

    }//GEN-LAST:event_lstArquivosValueChanged

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessarActionPerformed

        try {
            processaWinthor();

        } catch (Exception e) {
            trataErro.trataException(e, "btnProcessarActionPerformed");
        }

    }//GEN-LAST:event_btnProcessarActionPerformed

    private void chkCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCodigoProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCodigoProdutoActionPerformed

    private void chkCodigoBarrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCodigoBarrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCodigoBarrasActionPerformed

    private void chkCodigoFabricaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCodigoFabricaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCodigoFabricaActionPerformed

    private void chkCodigoBarras2014ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCodigoBarras2014ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCodigoBarras2014ActionPerformed

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
                try {
                    new Brz006().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Brz006.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscaPath;
    private javax.swing.JButton btnProcessar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkCodigoBarras;
    private javax.swing.JCheckBox chkCodigoBarras2014;
    private javax.swing.JCheckBox chkCodigoFabrica;
    private javax.swing.JCheckBox chkCodigoProduto;
    private javax.swing.JCheckBox chkSustituirExistente;
    private javax.swing.JTextField edtPastaImagens;
    private javax.swing.JTextField edtPathImagens;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblContador;
    private javax.swing.JLabel lblImagem;
    private javax.swing.JLabel lblProcessados;
    private javax.swing.JList lstArquivos;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
