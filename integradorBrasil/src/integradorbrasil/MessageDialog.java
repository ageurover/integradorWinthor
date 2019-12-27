/*
 * MessageDialog.java
 *
 * Created on 23 de Fevereiro de 2006, 20:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package integradorbrasil;

import javax.swing.JOptionPane;

/**
 *
 * @author Eduardo
 */
public class MessageDialog {
    public static int YES_OPTION =  JOptionPane.OK_OPTION;
    public static int NO_OPTION =  JOptionPane.NO_OPTION;
    public static int CA_OPTION =  JOptionPane.CANCEL_OPTION;
    
    /** Creates a new instance of MessageDialog */
    public MessageDialog() {
    }
    
    public static Object inputBox(String mensagem) {
        return JOptionPane.showInputDialog(null, mensagem, "Atenção", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static Object inputBox(String mensagem, Object valorInicial) {
        return JOptionPane.showInputDialog(null, mensagem, valorInicial);
    }
    
    public static void info(String info) {
        JOptionPane.showMessageDialog(null, info, "Atenção", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void error(String info) {
        JOptionPane.showMessageDialog(null, info, "Atenção", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void saveSucess() {
        JOptionPane.showMessageDialog(null, "Dados gravados com sucesso.", "Inserir", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void updateSucess() {
        JOptionPane.showMessageDialog(null, "Alteraçães realizadas com sucesso.", "Alterar", JOptionPane.INFORMATION_MESSAGE);
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
    
}
