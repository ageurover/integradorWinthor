package winthorDb.jpa.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import winthorDb.Main;

/*
 * Classe SingleConnection
 * Responsável Por Fazer a Conexão com o BD
 * @author Filipe Pereira Lara
 */
public class SingleConnection {

    private static Connection connection = null;

    /*
	 * Chamada Estática do Método conectar()
     */
    static {
        conectar();
    }

    /*
	 * Construtor da Classe SingleConnection()
	 * Chama o Método conectar()
     */
    public SingleConnection() {
        conectar();
    }

    /*
	 * Método conectar()
	 * Provê os Meios de Conexão ao BD
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
	 * Método getConnection()
	 * Responsável Por Fazer Uso da Conexão na Aplicação
     */
    public static Connection getConnection() {
        return connection;
    }
}
