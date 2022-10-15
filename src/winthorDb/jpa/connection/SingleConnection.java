package winthorDb.jpa.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import winthorDb.Main;

/*
 * Classe SingleConnection
 * Respons�vel Por Fazer a Conex�o com o BD
 * @author Filipe Pereira Lara
 */
public class SingleConnection {

    private static Connection connection = null;

    /*
	 * Chamada Est�tica do M�todo conectar()
     */
    static {
        conectar();
    }

    /*
	 * Construtor da Classe SingleConnection()
	 * Chama o M�todo conectar()
     */
    public SingleConnection() {
        conectar();
    }

    /*
	 * M�todo conectar()
	 * Prov� os Meios de Conex�o ao BD
     */
    private static void conectar() {
        try {
            if (connection == null) {
                String url = Main.getConnectionDb;
                Class.forName("oracle.jdbc.OracleDriver");
                connection = DriverManager.getConnection(url, Main.nomeUsuario, Main.senhaUsuario);
                connection.setAutoCommit(false);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao Conectar com o Banco de Dados");
        }
    }

    /*
	 * M�todo getConnection()
	 * Respons�vel Por Fazer Uso da Conex�o na Aplica��o
     */
    public static Connection getConnection() {
        return connection;
    }
}
