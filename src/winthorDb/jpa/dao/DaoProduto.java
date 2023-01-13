package winthorDb.jpa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import winthorDb.error.MessageDialog;

import winthorDb.jpa.bean.BeanProduto;
import winthorDb.jpa.connection.SingleConnection;

/*
 * Classe DaoProduto
 * Classe Que Provê os Métodos e Validações Para Manipular Dados, e Acesso e Manipulação do BD
 */
public class DaoProduto {

    private Connection connection;

    /*
		 * Construtor DaoProduto()
		 * Recebe um Objeto connection da Classe SingleConnection
     */
    public DaoProduto() {
        connection = SingleConnection.getConnection();
    }

//    /*
//		 * Método listar()
//		 * Responsável Por Listar Todos os Produtos do Sistema
//     */
//    public List<BeanProduto> listar() throws Exception {
//        List<BeanProduto> listar = new ArrayList<>();
//        String sql = "SELECT codprod, descricao, embalagem FROM produto";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        ResultSet resultSet = statement.executeQuery();
//        while (resultSet.next()) {
//            BeanProduto produto = new BeanProduto();
//            produto.setCodprod(resultSet.getLong("codprod"));
//            produto.setDescricao(resultSet.getString("descricao"));
//            produto.setEmbalagem(resultSet.getString("embalagem"));
//            listar.add(produto);
//        }
//        return listar;
//    }
    public BeanProduto consultar(String idFilial, String idProduto, String idEndereco, boolean consultaCodProd) throws Exception {
        String sql = "SELECT p.codprod, p.descricao, p.embalagem, p.UNIDADEMASTER || ' - ' || p.QTUNITCX as embalagemmaster,\n"
                + " nvl(p.QTUNITCX,1) as qtUnidadeMaster, e.codauxiliar , e.codfilial, \n"
                + " nvl(bl.qtfrentegondula,1) as qtfrentegondula, nvl(bl.qtfundogondula,1) as qtfundogondula, \n"
                + " nvl(bl.qtalturagondula,1) as qtalturagondula ,  nvl(bl.percpontoreposicao,1) as percpontoreposicao , \n"
                + " nvl(es.codendereco,0) as codenderecoloja, nvl(es.pontoreposicao,0) as pontoreposicao, nvl(es.capacidade,1) as capacidade,\n"
                + " decode(es.codendereco,null, 'N', 'S') as estendloja, \n"
                + " decode(bl.codendereco,null, 'N', 'S') as endecoloja, \n"
                + " decode(ee.codendereco,null, 'N', 'S') as estendereco, \n"
                + " round(((nvl(et.qtvendmes,0) + nvl(qtvendmes1,0) + nvl(qtvendmes2,0) + nvl(qtvendmes3,0))/4)/30,0) as girodia \n"
                + " FROM pcprodut p, pcembalagem e, pcestendloja es , brendecoloja bl, pcendereco ed, pcest et , pcestendereco ee \n"
                + " WHERE p.codprod = e.codprod\n"
                + " and bl.codprod (+)= p.codprod\n"
                + " and bl.codfilial (+)= e.codfilial\n"
                + " and bl.codendereco (+) = ed.codendereco\n"
                + " and es.codprod (+) = e.codprod \n"
                + " and es.codfilial (+) = e.codfilial \n"
                + " and es.codendereco (+) = ed.codendereco \n"
                + " and ee.codendereco (+) = ed.codendereco \n"
                + " and ee.codprod (+)= e.codprod \n"
                + " and et.codprod = p.codprod \n"
                + " and et.codfilial = e.codfilial \n"
                + (consultaCodProd ? " and e.codprod = " + idProduto : " and e.codauxiliar = '" + idProduto + "'")
                + " and e.codfilial = '" + idFilial + "'"
                + " and ed.codendereco = " + idEndereco;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            BeanProduto produto = new BeanProduto();
            produto.setCodprod(resultSet.getLong("codprod"));
            produto.setDescricao(resultSet.getString("descricao"));
            produto.setEmbalagem(resultSet.getString("embalagem"));
            produto.setEmbalagemmaster(resultSet.getString("embalagemmaster"));
            produto.setCodauxiliar(resultSet.getString("codauxiliar"));
            produto.setCodfilial(resultSet.getString("codfilial"));
            produto.setCodenderecoloja(resultSet.getLong("codenderecoloja"));
            produto.setQtfrentegondula(resultSet.getDouble("qtfrentegondula"));
            produto.setQtfundogondula(resultSet.getDouble("qtfundogondula"));
            produto.setQtalturagondula(resultSet.getDouble("qtalturagondula"));
            produto.setPercpontoreposicao(resultSet.getDouble("percpontoreposicao"));
            produto.setPontoreposicao(resultSet.getDouble("pontoreposicao"));
            produto.setCapacidade(resultSet.getDouble("capacidade"));
            produto.setQtUnidadeMaster(resultSet.getDouble("qtUnidadeMaster"));
            produto.setPcestendloja(resultSet.getString("estendloja"));
            produto.setBrendecoloja(resultSet.getString("endecoloja"));
            produto.setPcestendereco(resultSet.getString("estendereco"));
            produto.setGirodia(resultSet.getDouble("girodia"));

            return produto;
        }
        return null;
    }

    /*
		 * Método atualizar()
		 * Método Responsável Por Atualizar os Dados (UPDATE) no BD
		 * @param BeanProduto produto = Objeto Produto da Classe BeanProduto
     */
    public void atualizar(BeanProduto produto) {
        try {
            int ret = 0;
            String sqlEstEndereco = "";
            String sqlEnderecoLoja = "";
            String sqlBrEnderecoLoja = "";

            if (produto.getPcestendloja().equalsIgnoreCase("S")) {
                sqlEnderecoLoja = "UPDATE pcestendloja SET capacidade = ?, pontoreposicao = ? "
                        + " WHERE codendereco = " + produto.getCodenderecoloja()
                        + " AND codprod = " + produto.getCodprod();
                PreparedStatement updEndLoja = connection.prepareStatement(sqlEnderecoLoja);
                updEndLoja.setDouble(1, produto.getCapacidade());
                updEndLoja.setDouble(2, produto.getPontoreposicao());
                ret = updEndLoja.executeUpdate();
                if (ret > 0) {
                    connection.commit();
                }
            } else {
                sqlEnderecoLoja = "INSERT INTO pcestendloja "
                        + " (codprod, codendereco, pontoreposicao, capacidade, codfilial)  "
                        + " VALUES ( ? , ? , ? , ? , ? )";
                PreparedStatement insEndLoja = connection.prepareStatement(sqlEnderecoLoja);
                insEndLoja.setLong(1, produto.getCodprod());
                insEndLoja.setLong(2, produto.getCodenderecoloja());
                insEndLoja.setDouble(3, produto.getPontoreposicao());
                insEndLoja.setDouble(4, produto.getCapacidade());
                insEndLoja.setString(5, produto.getCodfilial());
                ret = insEndLoja.executeUpdate();

                if (ret > 0) {
                    connection.commit();
                }
            }

            if (produto.getBrendecoloja().equalsIgnoreCase("S")) {
                sqlBrEnderecoLoja = "UPDATE brendecoloja "
                        + " SET qtfrentegondula =? , qtfundogondula =?, qtalturagondula =? , "
                        + " percpontoreposicao =?, pontoreposicao =?, capacidade =?"
                        + " WHERE codendereco = " + produto.getCodenderecoloja()
                        + " AND codprod = " + produto.getCodprod()
                        + " AND codfilial = '" + produto.getCodfilial() + "' ";
                PreparedStatement updBrEndLoja = connection.prepareStatement(sqlBrEnderecoLoja);
                updBrEndLoja.setDouble(1, produto.getQtfrentegondula());
                updBrEndLoja.setDouble(2, produto.getQtfundogondula());
                updBrEndLoja.setDouble(3, produto.getQtalturagondula());
                updBrEndLoja.setDouble(4, produto.getPercpontoreposicao());
                updBrEndLoja.setDouble(5, produto.getPontoreposicao());
                updBrEndLoja.setDouble(6, produto.getCapacidade());

                ret = updBrEndLoja.executeUpdate();
                if (ret > 0) {
                    connection.commit();
                }
            } else {
                sqlBrEnderecoLoja = "INSERT INTO brendecoloja "
                        + " (codfilial, codprod, codendereco, qtfrentegondula, qtfundogondula, "
                        + " qtalturagondula, percpontoreposicao, pontoreposicao, capacidade)  "
                        + " VALUES ( ? , ? , ? , ? , ? , ?, ? ,? ,?)";
                PreparedStatement insBrEndLoja = connection.prepareStatement(sqlBrEnderecoLoja);
                insBrEndLoja.setString(1, produto.getCodfilial());
                insBrEndLoja.setLong(2, produto.getCodprod());
                insBrEndLoja.setLong(3, produto.getCodenderecoloja());
                insBrEndLoja.setDouble(4, produto.getQtfrentegondula());
                insBrEndLoja.setDouble(5, produto.getQtfundogondula());
                insBrEndLoja.setDouble(6, produto.getQtalturagondula());
                insBrEndLoja.setDouble(7, produto.getPercpontoreposicao());
                insBrEndLoja.setDouble(8, produto.getPontoreposicao());
                insBrEndLoja.setDouble(9, produto.getCapacidade());

                ret = insBrEndLoja.executeUpdate();

                if (ret > 0) {
                    connection.commit();
                }
            }
            if (produto.getPcestendereco().equalsIgnoreCase("N")) {
                sqlEstEndereco = "INSERT INTO PCESTENDERECO(CODPROD,CODENDERECO,QT)  "
                        + " VALUES ( ? , ? , ? )";
                PreparedStatement insEstEnd = connection.prepareStatement(sqlEstEndereco);
                insEstEnd.setLong(1, produto.getCodprod());
                insEstEnd.setLong(2, produto.getCodenderecoloja());
                insEstEnd.setDouble(3, 0.0);
                ret = insEstEnd.executeUpdate();

                if (ret > 0) {
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                MessageDialog.error("Transação abortada!");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /*
		 * Método REMOVER()
		 * Método Responsável Por remover os Dados no BD
		 * @param BeanProduto produto = Objeto Produto da Classe BeanProduto
     */
    public void remover(BeanProduto produto) {
        try {
            int ret = 0;
            String sqlEstEndereco = "";
            String sqlEnderecoLoja = "";
            String sqlBrEnderecoLoja = "";

            if (produto.getPcestendloja().equalsIgnoreCase("S")) {
                sqlEnderecoLoja = "DELETE FROM pcestendloja "
                        + " WHERE codendereco = " + produto.getCodenderecoloja()
                        + " AND codprod = " + produto.getCodprod();
                PreparedStatement updEndLoja = connection.prepareStatement(sqlEnderecoLoja);
                ret = updEndLoja.executeUpdate();
                if (ret > 0) {
                    connection.commit();
                }
            }

            if (produto.getBrendecoloja().equalsIgnoreCase("S")) {
                sqlBrEnderecoLoja = "DELETE FROM brendecoloja "
                        + " WHERE codendereco = " + produto.getCodenderecoloja()
                        + " AND codprod = " + produto.getCodprod()
                        + " AND codfilial = '" + produto.getCodfilial() + "' ";
                PreparedStatement updBrEndLoja = connection.prepareStatement(sqlBrEnderecoLoja);
                ret = updBrEndLoja.executeUpdate();

                if (ret > 0) {
                    connection.commit();
                }
            }
            if (produto.getPcestendereco().equalsIgnoreCase("S")) {
                sqlEstEndereco = "DELETE FROM PCESTENDERECO "
                        + " WHERE codendereco = " + produto.getCodenderecoloja()
                        + " AND codprod = " + produto.getCodprod();
                PreparedStatement updEstEndereco = connection.prepareStatement(sqlEstEndereco);
                ret = updEstEndereco.executeUpdate();

                if (ret > 0) {
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                MessageDialog.error("Transação abortada -> Atualizar!");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
