
/*
 * IntegracaoWinthorDb.java
 *
 * Created on 16 de Outubro de 2007, 15:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package winthorDb.oracleDb;

// Import the JDBC classes
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.pool.OracleDataSource;
import winthorDb.Main;
import winthorDb.error.trataErro;
import winthorDb.util.Formato;

// Import the java classes used in applets
/**
 *
 * @author Ageu Elias Rover
 */
public class IntegracaoWinthorDb {

    private Connection connOrigem = null;

    /**
     * Creates a new instance of IntegracaoWinthorDb
     */
    public IntegracaoWinthorDb() {
    }

    public void openConectOracle() throws SQLException, Exception {

        try {

            // conecta-se a base de origem dos dados
            if (connOrigem == null) {
                // Create a OracleDataSource instance and set URL
                OracleDataSource ods = new OracleDataSource();
                ods.setURL(Main.getConnectionDb);
                connOrigem = ods.getConnection();
            }

        } catch (SQLException e) {
            trataErro.trataException(e, "openConectOracle");
            throw e;
        }
    }

    public void closeConectOracle() throws SQLException, Exception {
        try {

            // See if we need to open the connection to the database
            if (connOrigem != null) {
                connOrigem.close();
                connOrigem = null;
            }

        } catch (SQLException e) {
            trataErro.trataException(e, "closeConectOracle");
            throw e;
        }
    }

     /**
     *
     * @param sqlInsert
     * @return retorna o numero de linhas atualizadas
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("FinallyDiscardsException")
    public int insertDados(String sqlInsert) throws SQLException, Exception {
        int ret = 0;
        if (connOrigem == null) {
            throw new NullPointerException("Falha na conexao com o banco de dados");
        }
        // Create a statement
        PreparedStatement ps = null;
        ResultSet rset = null;

        try {

            // Execute the query
            ps = connOrigem.prepareStatement(sqlInsert);
            ret = ps.executeUpdate(sqlInsert);

        } catch (SQLException e) {
            trataErro.trataException(e, "insertDados -> " + sqlInsert);
            throw e;
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }
            return ret;
        }
    }
    
    /**
     *
     * @param sqlUpdate
     * @return retorna o numero de linhas atualizadas
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("FinallyDiscardsException")
    public int updateDados(String sqlUpdate) throws SQLException, Exception {
        int ret = 0;
        if (connOrigem == null) {
            throw new NullPointerException("Falha na conexao com o banco de dados");
        }
        // Create a statement
        PreparedStatement ps = null;
        ResultSet rset = null;

        try {

            // Execute the query
            ps = connOrigem.prepareStatement(sqlUpdate);
            ret = ps.executeUpdate(sqlUpdate);

        } catch (SQLException e) {
            trataErro.trataException(e, "updateDados -> " + sqlUpdate);
            throw e;
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }
            return ret;
        }
    }

    /**
     *
     * @param sqlSelect
     * @return
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("FinallyDiscardsException")
    public ResultSet selectResultSet(String sqlSelect) throws SQLException, Exception {

        if (connOrigem == null) {
            return null;
        }
        // Create a statement
        Statement ps = null;
        ResultSet rset = null;

        try {

            // Execute the query
            ps = connOrigem.prepareStatement(sqlSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rset = ps.executeQuery(sqlSelect);

            return rset;

        } catch (SQLException e) {
            trataErro.trataException(e, "selectResultSet -> " + sqlSelect);
            throw e;
        }
    }

    /**
     *
     * @param sqlSelect
     * @return
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("FinallyDiscardsException")
    public List selectDados(String sqlSelect) throws SQLException, Exception {
        List dados = new ArrayList<>();
        if (connOrigem == null) {
            return null;
        }
        // Create a statement
        PreparedStatement ps = null;
        ResultSet rset = null;

        try {

            // Execute the query
            ps = connOrigem.prepareStatement(sqlSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rset = ps.executeQuery(sqlSelect);

            dados = getDados(rset);

        } catch (SQLException e) {
            trataErro.trataException(e, "selectDados -> " + sqlSelect);
            throw e;
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }
            return dados;
        }
    }

    /*
    

     // Se o parâmetro OUT p_dsc_erro retornar diferente de null, exibe mensagem de erro
     if (ctsmt.getString(3) != null) {
     throw new Exception(ctsmt.getString(3));
     }

     */
    /**
     *
     * @param sqlProcedure - Nome da procedure a ser executada
     * @param parametros - Array com os parametros da procedure
     * @param tipoParametros - Array com o tipo de dados dos parametros conforme
     * segue: 1 - Inteiro, 2 - String, 3 - Numerico, 4 - Data
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("FinallyDiscardsException")
    public void executeProcedure(String sqlProcedure, String[] parametros, int[] tipoParametros) throws SQLException, Exception {
        List dados = new ArrayList<>();
        if (connOrigem == null) {
            return;
        }
        // Create a statement
        CallableStatement ctsmt = null;
        String sqlCommand = "";

        try {
            // Montando o sqlCommand para a execução conforme o nome da procedure e os parametros
            sqlCommand = "{call " + sqlProcedure + "(";
            for (int i = 0; i < parametros.length; i++) {
                if (i == 0) {
                    sqlCommand += "?";
                } else {
                    sqlCommand += ",?";
                }

            }
            sqlCommand += ")}";

            ctsmt = connOrigem.prepareCall(sqlCommand, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // carrega os dados dos parametros conforme o tipo
            for (int i = 0; i < tipoParametros.length; i++) {
                switch (tipoParametros[i]) {
                    case 1:
                        ctsmt.setInt(i, Formato.strToInt(parametros[i]));
                    case 2:
                        ctsmt.setString(i, parametros[i]);
                    case 3:
                        ctsmt.setDouble(i, Formato.doubleToCurrStr(parametros[i]));
                    case 4:
                        ctsmt.setDate(i, (Date) Formato.strToDate(parametros[i]));
                    default:
                        ctsmt.setString(i, parametros[i]);
                }

            }

            ctsmt.execute();

        } catch (SQLException e) {
            trataErro.trataException(e, "executeProcedure -> " + sqlCommand);
            throw e;
        } finally {
            if (ctsmt != null) {
                try {
                    ctsmt.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                    throw sqe;
                }
            }
        }
    }

    /*
     * Gera uma lista com os dados da linha retornada pelo result set
     */
    private List getDados(ResultSet data) throws SQLException {
        List dataRows = new ArrayList<>();

        try {
            // carega as colunas do resultset
            ResultSetMetaData rsmd = data.getMetaData();
            // carega os dados do resultset
            while (data.next()) {
                try {
                    dataRows.add(getNextRow(data, rsmd));
                } catch (SQLException e) {
                    trataErro.trataException(e, "dataRows");
                    throw e;
                }
            }

        } catch (SQLException e) {
            trataErro.trataException(e, "getDados");
            throw e;
        }

        return dataRows;
    }

    private List getNextRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
        List currentRow = new ArrayList<>();
        int x = 0;
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                switch (rsmd.getColumnType(i)) {
                    case Types.VARCHAR:
                        if (rs.getString(i) != null) {
                            currentRow.add(rs.getString(i));
                        } else {
                            currentRow.add("");
                        }
                        break;
                    case Types.LONGVARCHAR:
                        if (rs.getString(i) != null) {
                            currentRow.add(rs.getString(i));
                        } else {
                            currentRow.add("");
                        }

                        break;
                    case Types.LONGNVARCHAR:
                        if (rs.getString(i) != null) {
                            currentRow.add(rs.getString(i));
                        } else {
                            currentRow.add("");
                        }

                        break;
                    case Types.INTEGER:
                        try {
                            //currentRow.add(Formato.intToStr(rs.getInt(i)));
                            currentRow.add(rs.getString(i));
                        } catch (SQLException ex) {
                            currentRow.add(Formato.intToStr(0));
                            //ex.printStackTrace();
                        }
                        break;
                    case Types.BIGINT:
                        try {
                            currentRow.add(rs.getLong(i));
                        } catch (SQLException ex) {
                            currentRow.add(Formato.intToStr(0));
                        }

                        break;
                    case Types.DATE:
                        try {
                            currentRow.add(Formato.dateToStr(rs.getDate(i)));
                        } catch (SQLException ex) {
                            currentRow.add(Formato.intToStr(0));
                        }

                        break;
                    case Types.DECIMAL:
                        try {
                            //currentRow.add(Formato.decimalToCurrStr(rs.getBigDecimal(i)));
                            currentRow.add(rs.getString(i));
                        } catch (SQLException ex) {
                            currentRow.add(Formato.intToStr(0));
                        }

                        break;
                    case Types.DOUBLE:
                        try {
                            //currentRow.add(Formato.doubleToCurrStr(rs.getDouble(i)));
                            currentRow.add(rs.getString(i));
                        } catch (SQLException ex) {
                            currentRow.add(Formato.intToStr(0));
                        }

                        break;
                    case Types.FLOAT:
                        try {
                            //currentRow.add(Formato.doubleToCurrStr(rs.getFloat(i)));
                            currentRow.add(rs.getString(i));
                        } catch (SQLException ex) {
                            currentRow.add(Formato.intToStr(0));
                        }

                        break;
                    case Types.NUMERIC:
                        try {
                            //currentRow.add(Formato.doubleToCurrStr(rs.getFloat(i)));
                            //currentRow.add(Formato.doubleToCurrStr(rs.getDouble(i)));
                            currentRow.add(rs.getString(i));
                        } catch (SQLException ex) {
                            currentRow.add(rs.getString(i));
                        }

                        break;
                    default:
                        try {
                            currentRow.add(rs.getString(i));
                        } catch (SQLException ex) {
                            currentRow.add("<Erro> " + ex.getMessage());
                        }

                }
                x++;
            }

        } catch (SQLException e) {
            trataErro.trataException(e, "getNextRow");
            throw e;
        }

        return currentRow;
    }
} //fim