/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb;

import java.awt.Color;
import java.io.File;
import javax.swing.JFrame;
import winthorDb.error.trataErro;
import winthorDb.forms.Login;
import winthorDb.oracleDb.CarregaStringConect;

/**
 *
 * @author ageurover
 */
public class Main {

    // String de conexao ao oracle
    //jdbc:oracle:thin:winthor2/zlqwkru5@(DESCRIPTION =(ADDRESS =(PROTOCOL = TCP)(HOST =192.168.1.201)(PORT = 1521))(CONNECT_DATA = (SID =WINT)))
    public static String getConnectionDb = null;
    public static String dirAplication = null;
    public static int colunaColor = 0;
    public static JFrame dialog = null;
    public static JFrame dialogMenu = null;
    public static JFrame dialogLogin = null;
    public static String xmlConectDb = "";
    public static String Usuario = "";
    public static String codConsumidor = "";
    public static String codFilial = "";
    public static String codFilialFatura = "";
    public static String licenca = "";
    public static String cnpjMatriz = "";
    public static String AjustaFrenteLoja = "";
    public static String PastaImagens = "";
    public static Color corColuna = Color.BLACK;
    public static int typeColunaColor = 0; // 1 =, 2 >, 3 <; 4!=; 0 default contem;
    public static int idColunaColor = -1;
    public static String nameColunaColor = null;
    public static String valorColunaColor = null;
    public static int[] linhaColor;

    public static String nomeUsuario = "";
    public static String senhaUsuario = "";
    public static String sidServidor = "";
    public static String ipServidor = "";
    public static String portaServidor = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Argumentos de linha de comando
        // BRZ001 BRASILDIS DR4WT9PC 192.168.5.2 WINT
        initModules();

//        if (args.length > 0) {
//            if (args.length == 5) {
//                
//               // getConnectionDb = "jdbc:oracle:thin:" + args[1] + "/" + args[2] + "@(DESCRIPTION =(ADDRESS =(PROTOCOL = TCP)(HOST = " + args[3] + ")(PORT = 1521))(CONNECT_DATA = (SID = " + args[4] + " )))";
//               getConnectionDb = CarregaStringConect.getStringConectDbOracle();
//
//                System.out.println(getConnectionDb);
//
//                switch (args[0]) {
//                    case "BRZ001":
//                        dialog = new Brz001();
//                        dialog.setVisible(true);
//                        break;
//                    case "BRZ002":
//                        dialog = new Brz002();
//                        dialog.setVisible(true);
//                        break;
//                    case "BRZ003":
//                        break;
//                    case "BRZ004":
//                        break;
//                    case "BRZ005":
//                        break;
//                    default:
//                        trataErro.lstErros.add("Por Favor informe o nome da Rotina para podermos inicar!");
//                        trataErro.mostraListaErros();
//                        System.exit(0);
//                }
//
//            }
//        } else {
        xmlConectDb = System.getProperties().get("user.dir").toString() + System.getProperties().get("file.separator").toString() + "conectDb.xml";
        System.out.println(xmlConectDb);
        getConnectionDb = CarregaStringConect.getStringConectDbOracle();
        Main.nomeUsuario = CarregaStringConect.getNomeUsuario();
        Main.senhaUsuario = CarregaStringConect.getSenhaUsuario();
        Main.ipServidor = CarregaStringConect.getIpServidor();
        Main.portaServidor = "1521";
        Main.sidServidor = CarregaStringConect.getSidServidor();
        
        
        System.out.println(getConnectionDb);
        Main.codConsumidor = CarregaStringConect.getCodConsumidor();
        Main.codFilial = CarregaStringConect.getCodFilial();
        Main.codFilialFatura = CarregaStringConect.getCodFilialFatura();
        Main.cnpjMatriz = CarregaStringConect.getCnpjMatriz();
        Main.licenca = CarregaStringConect.getLicenca();
        Main.AjustaFrenteLoja = CarregaStringConect.getAjustaFrenteLoja();
        Main.PastaImagens = CarregaStringConect.getPastaImagens();
        

        // abre a tela de login
        dialogLogin = new Login();
        dialogLogin.setVisible(true);
//        }
    }

    public static void initModules() {

        try {
            dirAplication = System.getProperties().get("user.dir").toString() + System.getProperties().get("file.separator").toString();
            // falta colocar a leito do xml contendo a configuraÃ§ao do banco de dados

            // Validar a Estrutura de pastas da aplicação
            File file = new File(dirAplication + "/log");
            if (!file.exists()) {
                file.mkdir();
            }
            file = null;
            System.out.println("Logs: " + dirAplication + "/log");
        } catch (Exception e) {
            trataErro.trataException(e);
            System.exit(0);
        }
    }

}
