
/*
 * IntegracaoWinthorDb.java
 *
 * Created on 16 de Outubro de 2007, 15:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package integradorbrasil.util.winthor;

// Import the JDBC classes
import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import integradorbrasil.Formato;
import integradorbrasil.Main;
import integradorbrasil.MessageDialog;
import integradorbrasil.trataErro;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import oracle.jdbc.pool.OracleDataSource;

// Import the java classes used in applets
/**
 *
 * @author Ageu Elias Rover
 */
public class IntegracaoWinthorDb {

    private Connection connOrigem = null;
    private Connection connDestin = null;

    /**
     * Creates a new instance of IntegracaoWinthorDb
     */
    public IntegracaoWinthorDb() {
    }

    public void openConectOracleOrigem() throws SQLException, Exception {

        try {

            // conecta-se a base de origem dos dados
            if (connOrigem == null) {
                // Create a OracleDataSource instance and set URL
                OracleDataSource ods = new OracleDataSource();
                ods.setURL(Main.getConnectionDbOrigem);
                connOrigem = ods.getConnection();
            }

            // conecta-se a base de destino dos dados
            if (connDestin == null) {
                // Create a OracleDataSource instance and set URL
                OracleDataSource ods = new OracleDataSource();
                ods.setURL(Main.getConnectionDbDestin);
                connDestin = ods.getConnection();
            }

        } catch (Exception e) {
            trataErro.trataException(e);
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
            // See if we need to open the connection to the database
            if (connDestin != null) {
                connDestin.close();
                connDestin = null;
            }
        } catch (Exception e) {
            trataErro.trataException(e);
            throw e;
        }
    }

    //
    public Vector selectDadosOrigem(String sqlSelect) throws SQLException, Exception {
        Vector dados = new Vector();
        if (connOrigem == null) {
            return null;
        }
        // Create a statement
        PreparedStatement ps = null;
        ResultSet rset = null;

        try {

            // Execute the query

            ps = connOrigem.prepareStatement(sqlSelect);
            rset = ps.executeQuery(sqlSelect);

            dados = getDados(rset);

        } catch (Exception e) {
            trataErro.trataException(e);
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                }
            }
            return dados;
        }
    }

    private Vector getDados(ResultSet data) {
        Vector dataRows = new Vector();

        try {
            // carega as colunas do resultset
            ResultSetMetaData rsmd = data.getMetaData();
            // carega os dados do resultset
            while (data.next()) {
                try {
                    dataRows.addElement(getNextRow(data, rsmd));
                } catch (Exception e) {
                    trataErro.trataException(e);
                }
            }

        } catch (Exception e) {
            trataErro.trataException(e);
        }

        return dataRows;
    }

    private Vector getNextRow(ResultSet rs, ResultSetMetaData rsmd) {
        Vector currentRow = new Vector();
        int x = 0;
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                switch (rsmd.getColumnType(i)) {
                    case Types.VARCHAR:
                        if (rs.getString(i) != null) {
                            currentRow.addElement(rs.getString(i));
                        } else {
                            currentRow.addElement("");
                        }
                        break;
                    case Types.LONGVARCHAR:
                        if (rs.getString(i) != null) {
                            currentRow.addElement(rs.getString(i));
                        } else {
                            currentRow.addElement("");
                        }

                        break;
                    case Types.LONGNVARCHAR:
                        if (rs.getString(i) != null) {
                            currentRow.addElement(rs.getString(i));
                        } else {
                            currentRow.addElement("");
                        }

                        break;
                    case Types.INTEGER:
                        try {
                            currentRow.addElement(Formato.intToStr(rs.getInt(i)));
                        } catch (Exception ex) {
                            currentRow.addElement(Formato.intToStr(0));
                            //ex.printStackTrace();
                        }
                        break;
                    case Types.BIGINT:
                        try {
                            currentRow.addElement(rs.getLong(i));
                        } catch (Exception ex) {
                            currentRow.addElement(Formato.intToStr(0));
                        }

                        break;
                    case Types.DATE:
                        try {
                            currentRow.addElement(Formato.dateToStr(rs.getDate(i)));
                        } catch (Exception ex) {
                            currentRow.addElement(Formato.intToStr(0));
                        }

                        break;
                    case Types.DECIMAL:
                        try {
                            currentRow.addElement(Formato.decimalToCurrStr(rs.getBigDecimal(i)));
                        } catch (Exception ex) {
                            currentRow.addElement(Formato.intToStr(0));
                        }

                        break;
                    case Types.DOUBLE:
                        try {
                            currentRow.addElement(Formato.doubleToCurrStr(rs.getDouble(i)));
                        } catch (Exception ex) {
                            currentRow.addElement(Formato.intToStr(0));
                        }

                        break;
                    case Type.FLOAT:
                        try {
                            currentRow.addElement(Formato.doubleToCurrStr(rs.getFloat(i)));
                        } catch (Exception ex) {
                            currentRow.addElement(Formato.intToStr(0));
                        }

                        break;
                    default:
                        try {
                            currentRow.addElement(rs.getString(i));
                        } catch (Exception ex) {
                            currentRow.addElement("<Erro> " + ex.getMessage());
                        }

                }
                x++;
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return currentRow;
    }


    /*
     * @Param filtroOrigem -> Colocar a expreção sql para o filto na tabela de 
     * pcclient da base de origem dos dados
     */
    public void integraCliente(String filtroOrigem) throws SQLException, Exception {
        if (connOrigem == null) {
            trataErro.addListaErros("Base de dados de oriem não esta conectada oa sistema!");
            trataErro.mostraListaErros();
            return;
        }
        if (connDestin == null) {
            trataErro.addListaErros("Base de dados de destino não esta conectada oa sistema!");
            trataErro.mostraListaErros();
            return;
        }
        // Create a statement
        PreparedStatement ps = null;
        ResultSet rset = null;

        int retIns = 0;
        String erroUpdate = "";
        ArrayList<String> insertSql = new ArrayList<>();
        String newCodCliente = "";

        /* inicia o processo de integração
         * passo 1 -> Buscar os dados na base de dados de origem 
         * passo 2 -> Fazer o parcer gerando os insert no padrao SQL ansi 92
         * passo 2.1 -> Pegar os novos id do cliente na base de destino para evitar problemas de chave estrangeira
         * passo 2.2 -> Validar os cadastros de praça (1), rota (2), região(3), vendedor (4) para ficar conforme a nova base de dados
         *              usar as tabelas de parcer que devem esta na base de dados de destino tabela meta_parcer
         * passo 3 -> Processar os insert's na base de dados de destino
         */
        try {
            // passo 1
            String sqlOrigem = "Select * from pcclient";
            Vector clienteOrigem = selectDadosOrigem(sqlOrigem);
            
            for (int i = 0; i < clienteOrigem.size(); i++) {
                Object object = clienteOrigem.elementAt(i);
                
                
            }
 
            
            // passo 3
            erroUpdate = "Erro ao inserir pcclient \n";
            
            for (int i = 0; i < insertSql.size(); i++) {
                ps = connDestin.prepareStatement(insertSql.get(i));
                retIns = ps.executeUpdate(insertSql.get(i));
                if (retIns == 0) {
                    erroUpdate = "[ " + insertSql.get(i) + " ]\n";
                }
            }

            MessageDialog.info("Final da Integração\n" + erroUpdate);

        } catch (Exception e) {
            trataErro.trataException(e);
            // throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException | RuntimeException sqe) {
                    // logar excecao
                    trataErro.trataException(sqe);
                }
            }
        }
    }
} //fim
