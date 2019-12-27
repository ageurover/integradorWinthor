/*
 * ConexaoDb.java
 *
 * Created on 18 de Fevereiro de 2006, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package integradorbrasil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Ageu Elias Rover
 */
public class ConexaoDb {
    
    /** Creates a new instance of ConexaoDb */
    public ConexaoDb() {
        
    }
    
    public Connection getConnectionMySql( ) throws
            ClassNotFoundException, SQLException {
        // Carregando o JDBC Driver
       Class.forName("org.gjt.mm.mysql.Driver");
       // Class.forName("com.mysql.jdbc.Driver");
        
        // Criando a conexao com o Banco de Dados
        String serverName = "ServerMySql:3306";
        String mydatabase = "gestao";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase + "?autoReconnect=true"; // a JDBC url
        String username = "gestaoUsr";
        String password = "1234";
        java.sql.Connection con = DriverManager.getConnection(url, username, password);
        
        return con;
        
    }
}
