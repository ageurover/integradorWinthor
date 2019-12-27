/*
 * trataErro.java
 *
 * Created on 4 de Outubro de 2006, 18:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package integradorbrasil;

import java.util.ArrayList;


/**
 *
 * @author Ageu Elias Rover
 */
public class trataErro {
    public static ArrayList lstErros = new ArrayList();
    /** Creates a new instance of trataErro */
    public trataErro() {
    }
    
    public static void trataException(Exception exp){
        exp.printStackTrace();
        ErroDialog.abrir(exp);
        
        //JOptionPane.showMessageDialog(null, exp.getMessage()+"\n"+exp.getLocalizedMessage()+"\n"+exp.toString(), "Erro ao executar", JOptionPane.ERROR_MESSAGE);
    }

    public static void trataException(Exception exp,String complemento){
        exp.printStackTrace();
        ErroDialog.abrir(exp,complemento);
        
        //JOptionPane.showMessageDialog(null, exp.getMessage()+"\n"+exp.getLocalizedMessage()+"\n"+exp.toString(), "Erro ao executar", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostraListaErros(){
        ErroDialog.abrir(lstErros);
        lstErros.clear();
    }

    public static void addListaErros(String strErro){
        lstErros.add(strErro);
    }

}
