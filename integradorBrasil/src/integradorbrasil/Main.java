/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integradorbrasil;

import java.sql.SQLException;

/**
 *
 * @author ageurover
 */
public class Main {

    public static MainWindow mainWindow = null;
    public static String getConnectionDbOrigem = null;
    public static String getConnectionDbDestin = null;
    public static String dirAplication = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        initModules();
        openMainWindow();
    }

    public static void initModules() {
        new Formato();

        try {
            dirAplication = System.getProperties().get("user.dir").toString() + System.getProperties().get("file.separator").toString();
            // falta colocar a leito do xml contendo a configuraçao do banco de dados
            
        } catch (Exception e) {
            trataErro.trataException(e);
            System.exit(0);
        }
    }

    public static void openMainWindow() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
