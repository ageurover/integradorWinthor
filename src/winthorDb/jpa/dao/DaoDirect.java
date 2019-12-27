package winthorDb.jpa.dao;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import winthorDb.Main;

/**
 * Faz a execução de comandos SQL na base de dados de forma direta com o banco
 * de dados usando a API do banco de dados para conexão.
 *
 * @author AGEUROVER
 *
 */
public class DaoDirect {

    /**
     * Faz a execução de comandos SQL na base de dados de forma direta com o
     * banco de dados usando a API do banco de dados para conexão.
     *
     */
    public DaoDirect() {
    }

    /**
     * Faz a conexão com banco de dados que fica aberta durante todo o tempo em
     * o sistema estiver aberto.
     *
     * @return Retorna uma conexão com o banco de dados
     * @throws java.lang.ClassNotFoundException valida se a class
     * com.mysql.jdbc.Driver existe
     * @throws java.sql.SQLException erro nas expressões SQL
     *
     */
    public static Connection getConnection() throws
            ClassNotFoundException, SQLException {
        // Carregando o JDBC Driver
        Class.forName("oracle.jdbc.OracleDriver");

        // Criando a conexao com o Banco de Dados
        String url = Main.getConnectionDb;

        java.sql.Connection con = DriverManager.getConnection(url, Main.nomeUsuario, Main.senhaUsuario);
        con.setAutoCommit(true);
        //con.setSchema("gestao");
        return con;

    }

    /**
     * Retorna a quantidade de registros inseridos ou alterados no banco de
     * dados conforme a expressão SQL passada como parametro, faz um
     * insert/update forçado desabilitando as chaves estrangeiras do banco de
     * dados antes de fazer a operação
     *
     * @param insertString expressão SQL de insert para ser processado
     * @return Retorna um array com a quantidade de registros inser conforme a
     * expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int saveNotKeys(String insertString) throws Exception {
        Statement stmt = null;
        int rs = -1;
        if (insertString == null) {
            return -1;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        //stmt.execute("SET FOREIGN_KEY_CHECKS=0");
        rs = stmt.executeUpdate(insertString);
        //stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        stmt = null;
        return rs;
    }

    /**
     * Retorna a quantidade de registros inseridos no banco de dados conforme a
     * expressão SQL passada como parametro
     *
     * @param insertString expressão SQL de insert para ser processado
     * @return Retorna um array com a quantidade de registros inser conforme a
     * expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int save(String insertString) throws Exception {
        Statement stmt = null;
        int rs = -1;
        if (insertString == null) {
            return -1;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeUpdate(insertString);
        stmt = null;
        return rs;
    }

    /**
     * Retorna a quantidade de registros inseridos no banco de dados conforme o
     * array de expressão SQL passada como parametro, processando o Arry todo de
     * uma só vez no banco de dados
     *
     * @param insertString array de expressão SQL de insert para ser processado
     * @return Retorna um array com a quantidade de registros inser conforme a
     * expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static String saveBatch(String[] insertString) throws Exception {
        Statement stmt = null;
        String retorno = "";
        int[] rs = new int[insertString.length];
        if (insertString.length == 0) {
            return "Falta Dados para o processamento!";
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        for (String insertString1 : insertString) {
            stmt.addBatch(insertString1);
        }

        rs = stmt.executeBatch();
        for (int x = 0; x < rs.length; x++) {
            if (rs[x] < 0) {
                retorno += "[" + rs[x] + " -> " + insertString[x] + "]\n";
            }
        }
        stmt = null;
        return retorno;
    }

    /**
     * Retorna a quantidade de registros inseridos no banco de dados conforme o
     * array de expressão SQL passada como parametro, Processando linha por
     * linha do array
     *
     * @param insertString array de expressão SQL de insert para ser processado
     * @return Retorna um array com a quantidade de registros inser conforme a
     * expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static String scriptDataDef(String[] insertString) throws Exception {
        Statement query = null;
        String retorno = "";

        int[] rs = new int[insertString.length];
        if (insertString.length == 0) {
            return "Falta Dados para o processamento!";
        }

        //query = getConnection().createStatement();
        query = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        for (int i = 0; i < insertString.length; i++) {
            try {
                rs[i] = query.executeUpdate(insertString[i] + ";");
            } catch (SQLException ex) {
                retorno += ex.getErrorCode() + " - " + ex.getMessage() + "\n";
            }
        }

        rs = null;
        query = null;

        return retorno;
    }

    /**
     * Retorna a quantidade de registros alterados conforme expressão SQL
     * passada como parametro
     *
     * @param updateString expressão SQL
     * @return Retorna a quantidade de registros alterados conforme a expressão
     * SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int update(String updateString) throws Exception {
        Statement stmt = null;
        int rs = -1;
        if (updateString == null) {
            return -1;
        }
        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeUpdate(updateString);
        stmt = null;
        return rs;
    }

    /**
     * Retorna a quantidade de registros deletados conforme expressão SQL
     * passada como parametro
     *
     * @param deleteString expressão SQL
     * @return Retorna a quantidade de registros deletados conforme a expressão
     * SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int delete(String deleteString) throws Exception {
        Statement stmt = null;
        int rs = -1;
        if (deleteString == null) {
            return -1;
        }
        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeUpdate(deleteString);
        stmt = null;
        return rs;
    }

    /**
     * Retorna um ResultSet com os dados da expressão SQL passada como parametro
     *
     * @param sqlString expressão SQL
     * @return Retorna um resultset com os dados da expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static ResultSet select(String sqlString) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        if (sqlString == null) {
            return null;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);

        stmt = null;

        return rs;
    }

    /**
     * Retorna um ResultSet com os dados da expressão SQL passada como parametro
     *
     * @param sqlString expressão SQL
     * @return Retorna um resultset com os dados da expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static CachedRowSet selectRS(String sqlString) throws Exception {
        Statement stmt = null;
        CachedRowSet rs = null;
        CachedRowSet crs = null;
        if (sqlString == null) {
            return null;
        }
        
        crs = new CachedRowSetImpl();
        crs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        crs.setConcurrency(ResultSet.CONCUR_UPDATABLE);
        crs.setUsername(Main.nomeUsuario);
        crs.setPassword(Main.senhaUsuario);
        crs.setUrl(Main.getConnectionDb);

        crs.setCommand(sqlString);
        crs.execute();
        stmt = null;

        return crs;
    }

    /**
     * Retorna um ResultSet com os dados da expressão SQL passada como parametro
     * dentro do limite de itens a ser retornado
     *
     * @param sqlString expressão SQL
     * @param Posicao posição inicial do registro de dados
     * @param limite quantidade de itens para ser retornado
     * @return Retorna um resultset com os dados da expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static ResultSet selectLimited(String sqlString, int Posicao, int limite) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        if (sqlString == null) {
            return null;
        }
        sqlString += " LIMIT " + Posicao + "," + limite;
        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);

        stmt = null;

        return rs;
    }

    /**
     * Busca o proximo numero da sequencia de transações do sistema
     *
     * @return Retorna o numero da sequencia
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int getSequence() throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        String sqlString = "Select Seq_Count+1 as Maximo from sequence  Where Seq_Name='SEQ_GEN'";

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        rs.first();
        stmt = null;
        sqlString = null;
        setSequence(rs.getInt(1));
        return (rs.getInt(1) > 0 ? rs.getInt(1) : 0);
    }

    /**
     * Atualiza os proximo numero da sequencia de transações do sistema
     *
     * @param newSequence valor a ser atualizado
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static void setSequence(int newSequence) throws Exception {
        Statement stmt = null;
        String sqlString = "Update sequence set Seq_Count=" + newSequence + " Where Seq_Name='SEQ_GEN'";

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stmt.executeUpdate(sqlString);
        stmt = null;
        sqlString = null;
    }

    /**
     * Busca o proximo numero para os dados de nota fiscal e outro campos
     * conforme parametros Grava no banco de dados na tabela filial o proximo
     * numenro gerado para que o sistema fique sempre atualizado
     *
     * @param codfilial Numero da Filial
     * @param campo Campo a ser gerado o proximo numero numero
     * @return Retorna o proximo numero
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int getSequenceFilial(int codfilial, String campo) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        int numeroNfe = 0;
        String sqlString = "Select " + campo + " as Proximo from filial  Where id = " + codfilial;

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        rs.first();
        numeroNfe = (rs.getInt(1) > 0 ? rs.getInt(1) + 1 : 1);
        stmt = null;
        sqlString = null;
        setSequenceFilial(codfilial, campo, numeroNfe);
        return numeroNfe;
    }

    /**
     * Atualiza os proximo numero na filial informada conforme o campo a ser
     * atualizado
     *
     * @param codFilial codigo da filial a ser atualizado
     * @param campo campo a ser atualizado o proximo numero
     * @param proximo novo numero da sequencia
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static void setSequenceFilial(int codFilial, String campo, int proximo) throws Exception {
        Statement stmt = null;
        String sqlString = "Update filial set " + campo + " = " + proximo + " Where id=" + codFilial;

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stmt.executeUpdate(sqlString);
        stmt = null;
        sqlString = null;
    }

    /**
     * Retorna a quantidade de itens da tabela passada como parametro
     *
     * @param table tabela para filtrar os dados
     * @return quantidade de itens filtrados
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int getMaxLength(String table) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        String sqlString = "Select count(*) as Maximo from " + table;
        if (table == null || table.isEmpty()) {
            return 0;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        rs.first();
        stmt = null;
        sqlString = null;
        return (rs.getInt(1) > 0 ? rs.getInt(1) : 0);
    }

    /**
     * Retorna o valor inteiro da expressão SQL passada como parametro
     *
     * @param sqlString expresão sql para filtrar os dados ex: Select Max(id)
     * from produtos
     * @return quantidade de itens filtrados
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int getMaxCount(String sqlString) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        if (sqlString == null || sqlString.isEmpty()) {
            return 0;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        rs.first();
        stmt = null;
        sqlString = null;
        return (rs.getInt(1) > 0 ? rs.getInt(1) : 0);
    }

    /**
     * Retorna a quantidade de itens da tabela passada como parametro conforme
     * os filtros
     *
     * @param table nome da tabela para filtrar os dados
     * @param strWhere expressão where para filtrar os dados conforme o padrao
     * SQL
     * @return quantidade de itens filtrados
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static int getMaxLength(String table, String strWhere) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        String sqlString = "Select count(*) as Maximo from " + table + " Where " + strWhere;
        if (table == null || table.isEmpty()) {
            return 0;
        }

        if (strWhere == null || strWhere.isEmpty()) {
            return 0;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        rs.first();
        stmt = null;
        sqlString = null;
        return (rs.getInt(1) > 0 ? rs.getInt(1) : 0);
    }

    /**
     * Retorna os dados do primeiro campo da expressão SQL passada como
     * parametro
     *
     * @param sqlString expressão SQL para filtrar os dados
     * @return dados do primeiro campo da expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static String getSingleField(String sqlString) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        String retorno = "";
        if (sqlString == null || sqlString.isEmpty()) {
            return "";
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        rs.first();
        if (rs.isFirst()) {
            retorno = (rs.getRow() != -1 ? rs.getString(1) : "");
        } else {
            retorno = "";
        }
        stmt = null;
        sqlString = null;
        return retorno;
    }

    /**
     * Retorna os dados do primeiro campo da expressão SQL passada como
     * parametro
     *
     * @param sqlString expressão SQL para filtrar os dados
     * @return dados do primeiro campo da expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static ArrayList<String> getSingleListField(String sqlString) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String> retorno = new ArrayList<>();

        if (sqlString == null || sqlString.isEmpty()) {
            return null;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        stmt = null;
        sqlString = null;

        rs.first();
        if (rs.isFirst()) {
            for (int i = 0; i <= rs.getMetaData().getColumnCount(); i++) {
                retorno.add(rs.getString(i));
            }
        }
        stmt = null;
        sqlString = null;
        return retorno;
    }

    /**
     * Retorna um array de dados da expressão SQL passada como parametro
     *
     * @param sqlString expressão SQL para filtrar os dados
     * @return array de dados do primeiro campo da expressão SQL
     * @throws java.lang.Exception - Excecao em caso de erro na consulta ao
     * banco de dados
     *
     */
    public static String[] selectCombo(String sqlString) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        if (sqlString == null || sqlString.isEmpty()) {
            return null;
        }

        //stmt = getConnection().createStatement();
        stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sqlString);
        stmt = null;
        sqlString = null;

        ArrayList dados = new ArrayList();
        while (rs.next()) {
            dados.add(rs.getString(1));
        }

        String[] itens = new String[dados.size()];
        for (int i = 0; i < dados.size(); i++) {
            itens[i] = dados.get(i).toString();
        }
        rs = null;
        return itens;
    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param fields lista de nomes dos campos para filtrar os dados
     * @param values lista de dados a serem filtrados
     * @param tipos lista de tipos de filtro a ser aplicado no criterio <br>0 -
     * Que comecam com o valor <br>1 - Que seja exatamente igual ao valor <br>2
     * - Que terminem com o valor <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que nao seja igual a nulo <br>5 - Que seja igual a nulo <br>6 -
     * Diferente <br>7 - Maior ou igual <br>8 - Menor ou igual <br>9 - Between
     * (Entre dois valores) <br>10 - Contendo na lista <br>11 - Nao Contendo na
     * <br>12 - Maior que <br>13 - Menor que
     * <br>99 - Operacao livre não esquecer do operador AND ou OR no inicio obs:
     * So usa o values <br>
     * Observacao: Todos as lista de paramentros devem ter obrigatoriamente a
     * mesma quantidade de dados.
     * @return string com a expressao de filtro where no padrao sql
     * @throws java.lang.Exception
     * @thow Excecao em caso de erro na consulta ao banco de dados
     *
     */
    public static String montaWhere(List<String> fields, List<Object> values, List<Integer> tipos) throws Exception {
        String sqlWhere = "";
        if (values == null || fields == null || tipos == null) {
            return "";
        }

        for (int i = 0; i <= fields.size() - 1; i++) {

            switch (tipos.get(i)) {
                case 0: // 0 - Que come?am com o valor
                    sqlWhere += " " + fields.get(i) + " LIKE '" + values.get(i) + "%'";
                    break;
                case 1: // 1 - Que seja exatamente igual ao valor
                    sqlWhere += " " + fields.get(i) + " = " + values.get(i);
                    break;
                case 2: // 2 - Que terminem com o valor
                    sqlWhere += " " + fields.get(i) + " LIKE '%" + values.get(i) + "'";
                    break;
                case 3: // 3 - Que contenha em qualquer parte o valor
                    sqlWhere += " " + fields.get(i) + " LIKE '%" + values.get(i) + "%'";
                    break;
                case 4: // 4 - Que nao seja igual a nulo
                    sqlWhere += " " + fields.get(i) + " IS NOT NULL ";
                    break;
                case 5: // 5 - Que seja igual a nulo
                    sqlWhere += " " + fields.get(i) + " IS NULL ";
                    break;
                case 6: // 6 - Diferente
                    sqlWhere += " " + fields.get(i) + " <> " + values.get(i);
                    break;
                case 7: // 7 - Maior ou igual
                    sqlWhere += " " + fields.get(i) + " >= " + values.get(i);
                    break;
                case 8: // 8 - Menor ou igual
                    sqlWhere += " " + fields.get(i) + " <= " + values.get(i);
                    break;
                case 9: // 9 - Between (Entre dois valores)
                    sqlWhere += " " + fields.get(i) + " BETWEEN " + values.get(i);
                    break;
                case 10: // 10 - Contendo na lista
                    sqlWhere += " " + fields.get(i) + " IN (" + values.get(i) + ")";
                    break;
                case 11: // 11 - Nao Contendo na lista
                    sqlWhere += " " + fields.get(i) + " NOT IN (" + values.get(i) + ")";
                    break;
                case 12: // 12 - Maior que
                    sqlWhere += " " + fields.get(i) + " > " + values.get(i);
                    break;
                case 13: // 13 - Menor que
                    sqlWhere += " " + fields.get(i) + " < " + values.get(i);
                    break;
                case 99: // 12 - Operacao livre não esquecer do operador AND ou OR no inicio
                    sqlWhere += " " + values.get(i) + " ";
                    break;

            }
            if (!sqlWhere.equals("") && i < fields.size() - 1) {
                sqlWhere += " AND ";
            }

        }

        return sqlWhere;
    }
}
