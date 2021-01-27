/*
 * MessageDialog.java
 *
 * Created on 23 de Fevereiro de 2006, 20:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package winthorDb.error;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Eduardo
 */
public class MessageDialog {

    public static int YES_OPTION = JOptionPane.OK_OPTION;
    public static int NO_OPTION = JOptionPane.NO_OPTION;
    public static int CA_OPTION = JOptionPane.CANCEL_OPTION;

    /**
     * Creates a new instance of MessageDialog
     */
    public MessageDialog() {
    }

    public static Object inputBox(String mensagem) {
        return JOptionPane.showInputDialog(null, mensagem, "Atenção", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Object inputBox(String mensagem, Object valorInicial) {
        return JOptionPane.showInputDialog(null, mensagem, valorInicial);
    }

    public static void info(String info) {
//        if (info.length() > 50) {
//            int ct = info.length() / 50;
//            String newInfo = "";
//            int ultpos = 0;
//            for (int i = 0; i < ct; i++) {
//                newInfo += info.substring(ultpos, (info.length() > ultpos + 50 ? ultpos + 50 : info.length())) + "\n";
//                ultpos+=50;
//            }
//            info = newInfo;
//        }
        JOptionPane.showMessageDialog(null, info, "Atenção", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(String info) {
        JOptionPane.showMessageDialog(null, info, "Atenção", JOptionPane.ERROR_MESSAGE);
    }

    public static void saveSucess() {
        JOptionPane.showMessageDialog(null, "Dados gravados com sucesso.", "Inserir", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void updateSucess() {
        JOptionPane.showMessageDialog(null, "Alterações realizadas com sucesso.", "Alterar", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void deleteSucess() {
        JOptionPane.showMessageDialog(null, "Remoção realizada com sucesso.", "Remover", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int askCancel() {
        return JOptionPane.showConfirmDialog(null,
                "Ao sair sem gravar, as alterações serão perdidas. Deseja sair?", "Deseja fechar?", JOptionPane.YES_NO_OPTION);
    }

    public static int ask(String question) {
        return JOptionPane.showConfirmDialog(null,
                question, "Confirmação", JOptionPane.YES_NO_OPTION);
    }

    public static int askDeleteItem() {
        return JOptionPane.showConfirmDialog(null,
                "Deseja remover o item selecionado?", "Deseja remover?", JOptionPane.YES_NO_OPTION);
    }

    public static String getCaptcha(String urlImg) {
        try {
            if (!urlImg.isEmpty()) {
                String ret = "";
                final JFrame frame = new JFrame();
                JLabel label = new JLabel(new ImageIcon(urlImg));
                JLabel label1 = new JLabel("Digite o captcha");
                final JTextField txtCaptcha = new JTextField();
                JButton btnConfirma = new JButton();
                btnConfirma.setText("Confirma");
                btnConfirma.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {

                    }

                });
                JButton btnCancela = new JButton();
                btnCancela.setText("Cancelar");
                btnCancela.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        frame.dispose();
                    }
                });
                frame.getContentPane().add(label, BorderLayout.CENTER);
                frame.getContentPane().add(label1, BorderLayout.CENTER);
                frame.getContentPane().add(txtCaptcha, BorderLayout.CENTER);
                frame.getContentPane().add(btnConfirma, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
                // entrada do texto do captcha
                return txtCaptcha.getText();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "getCaptcha");
        }
        return null;
    }
}
