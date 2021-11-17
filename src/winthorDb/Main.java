/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb;

import java.awt.Color;
import java.io.File;
import java.util.Date;
import javax.swing.JFrame;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.forms.BrzConfigDb;
import winthorDb.forms.Login;
import winthorDb.jpa.dao.DaoDirect;
import winthorDb.oracleDb.CarregaStringConect;
import winthorDb.util.AES;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Main {

    // String de conexao ao oracle
    //jdbc:oracle:thin:winthor2/zlqwkru5@(DESCRIPTION =(ADDRESS =(PROTOCOL = TCP)(HOST =192.168.1.201)(PORT = 1521))(CONNECT_DATA = (SID =WINT)))
    public static String versao = "1.0 r18";
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
    public static final String SECRETKEY = "#KimTec!546832%$";
    public static String nomeUsuario = "";
    public static String senhaUsuario = "";
    public static String sidServidor = "";
    public static String ipServidor = "";
    public static String portaServidor = "";
    public static String dataExpiracao = "";
    public static String qtdeMaxUsuarios = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Argumentos de linha de comando
        // BRZ001 BRASILDIS DR4WT9PC 192.168.5.2 WINT
        initModules();

        xmlConectDb = System.getProperties().get("user.dir").toString() + System.getProperties().get("file.separator").toString() + "conectDb.xml";
        System.out.println(xmlConectDb);
        File fileCfgDb = new File(Main.xmlConectDb);

        if (fileCfgDb.exists()) {
            getConnectionDb = CarregaStringConect.getStringConectDbOracle();
            Main.nomeUsuario = CarregaStringConect.getNomeUsuario();
            Main.senhaUsuario = CarregaStringConect.getSenhaUsuario();
            Main.ipServidor = CarregaStringConect.getIpServidor();
            Main.portaServidor = CarregaStringConect.getPortaServidor();
            Main.sidServidor = CarregaStringConect.getSidServidor();

            System.out.println(getConnectionDb);
            Main.codConsumidor = CarregaStringConect.getCodConsumidor();
            Main.codFilial = CarregaStringConect.getCodFilial();
            Main.codFilialFatura = CarregaStringConect.getCodFilialFatura();
            Main.cnpjMatriz = CarregaStringConect.getCnpjMatriz();
            Main.licenca = CarregaStringConect.getLicenca();
            Main.dataExpiracao = CarregaStringConect.getDataExpiracao();
            Main.qtdeMaxUsuarios = CarregaStringConect.getQtdeMaxUsuarios();
            Main.AjustaFrenteLoja = CarregaStringConect.getAjustaFrenteLoja();
            Main.PastaImagens = CarregaStringConect.getPastaImagens();

            validKeySystem();
        } else {
            if (MessageDialog.ask("Arquivo de configuração não encontrado, deseja configurar agora ?") == MessageDialog.YES_OPTION) {
                // abre a tela de configuracao do banco de dados
                Main.dialog = new BrzConfigDb();
                Main.dialog.setVisible(true);

//                // reinicia a aplicação
//                MessageDialog.info("Por favor abra o sistema novamente para que as configurações tenham efeito!");
//                System.exit(0);
            } else {
                System.exit(0);
            }
        }

        // abre a tela de login
        dialogLogin = new Login();
        dialogLogin.setVisible(true);

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

    /**
     * Faz a validacao do sistema conforme a chave de acesso do sistema
     * verificando o CNPJ comercializado; verificando a data limite de expiracao
     * da licenca setando a quantidade maxima de usuarios para acessar o sistema
     */
    public static void validKeySystem() {
        try {
            if (!CarregaStringConect.getLicenca().isEmpty()) {

                String decryptedString = AES.decrypt(CarregaStringConect.getLicenca(), Main.SECRETKEY);
                if (decryptedString != null) {
                    String decodeKey[] = decryptedString.split(";");
                    if (decodeKey.length == 3) {
                        String decodeKeyCnpj = decodeKey[0];
                        String decodeKeyDataExp = decodeKey[1];
                        String decodeKeyQtdeUser = decodeKey[2];

                        // verficia se o cbnpj da chave é o mesmo do cnpj matriz
                        if (!CarregaStringConect.getCnpjMatriz().equalsIgnoreCase(decodeKeyCnpj)) {
                            MessageDialog.error("Erro ao validar licença de uso!\nEntrar em contato com o fornecedor de software.");
                            System.exit(0);
                        }

                        // verifica se a chave informada pertence a algum cnjp cadastrado no sistema
                        int qtdeFilial = DaoDirect.getMaxLength("PcFilial", " replace(replace(replace(cgc, '.',''), '/','') , '-','') = " + decodeKeyCnpj);

                        if (qtdeFilial == 0) {
                            MessageDialog.error("Nao existe cnpj cadastrado para esta licença!\nEntrar em contato com o fornecedor de software.");
                            System.exit(0);
                        }

                        Date dtExpira = Formato.strToDateNfe(decodeKeyDataExp);
                        int diasExpira = Formato.diferencaEmDiasInteiros(new Date(), dtExpira);
                        if (diasExpira < 0) {
                            MessageDialog.error("Data de validade expirou licença!\nEntrar em contato com o fornecedor de software.");
                            System.exit(0);
                        }
                        if (diasExpira < 30) {
                            MessageDialog.info("Data de validade licença expira em " + diasExpira + " dias! \nEntrar em contato com o fornecedor de software.");
                        }
                    }

                } else {
                    MessageDialog.error("Erro ao processar licença de uso!\nEntrar em contato com o fornecedor de software.");
                    if (MessageDialog.ask("Deseja informar a licença correta?") == MessageDialog.YES_OPTION) {
                        Main.dialog = new BrzConfigDb();
                        Main.dialog.setVisible(true);
                    } else {
                        System.exit(0);
                    }
                }
            } else {
                MessageDialog.error("licença de uso invalida!\nEntrar em contato com o fornecedor de software.");
                if (MessageDialog.ask("Deseja informar a licença correta?") == MessageDialog.YES_OPTION) {
                    Main.dialog = new BrzConfigDb();
                    Main.dialog.setVisible(true);
                } else {
                    System.exit(0);
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "validKeySystem");
        }
    }

}
