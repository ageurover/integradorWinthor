package winthorDb.jpa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import winthorDb.jpa.bean.BeanEnderecoLoja;
import winthorDb.jpa.bean.BeanEnderecoWms;

import winthorDb.jpa.connection.SingleConnection;

/*
 * Classe DaoProduto
 * Classe Que Provê os Métodos e Validações Para Manipular Dados, e Acesso e Manipulação do BD
 */
public class DaoBrEnderecoLoja {

    private Connection connection;

    /*
		 * Construtor DaoProduto()
		 * Recebe um Objeto connection da Classe SingleConnection
     */
    public DaoBrEnderecoLoja() {
        connection = SingleConnection.getConnection();
    }

    /*
		 * Método listar()
		 * Responsável Por Listar Todos os Produtos do Sistema
     */
    public List<BeanEnderecoLoja> listarEnderecoLoja(String idFilial, String idEndereco) throws Exception {
        List<BeanEnderecoLoja> ListaEndLoja = new ArrayList<>();
        String sql = "select el.codprod, el.codendereco, el.pontoreposicao, el.capacidade, el.codfilial, "
                + " p.descricao, p.embalagem "
                + " from pcestendloja el, pcprodut p "
                + " where p.codprod = el.codprod "
                + " and el.codfilial = '" + idFilial + "' "
                + " and el.codendereco = " + idEndereco
                + " order by el.codprod ";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            BeanEnderecoLoja endLoja = new BeanEnderecoLoja();
            endLoja.setCodprod(resultSet.getLong("codprod"));
            endLoja.setDescricao(resultSet.getString("descricao"));
            endLoja.setCodendereco(resultSet.getLong("codendereco"));
            endLoja.setCodfilial(resultSet.getString("codfilial"));
            endLoja.setPontoreposicao(resultSet.getDouble("pontoreposicao"));
            endLoja.setCapacidade(resultSet.getDouble("capacidade"));
            endLoja.setEmbalagem(resultSet.getString("embalagem"));

            ListaEndLoja.add(endLoja);
        }
        return ListaEndLoja;
    }

    public ResultSet listar(String idFilial, String idEndereco) throws Exception {

        String sql = "select el.codprod, el.codendereco, el.pontoreposicao, el.capacidade, el.codfilial, "
                + " p.descricao, p.embalagem "
                + " from pcestendloja el, pcprodut p "
                + " where p.codprod = el.codprod "
                + " and el.codfilial = '" + idFilial + "' "
                + " and el.codendereco = " + idEndereco
                + " order by el.codprod ";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    /*
		 * Método consultar()
		 * Responsável Por Fazer Consultas (SELECT) no BD
		 * @param String id = Atributo ID do Produto
     */
    public BeanEnderecoWms consultar(String idFilial, String idEndereco) throws Exception {
        String sql = "SELECT * "
                + " FROM pcendereco "
                + " WHERE tipopal = 9 "
                + " AND CODFILIAL = '" + idFilial + "' "
                + " AND CODENDERECO = " + idEndereco;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            BeanEnderecoWms endWms = new BeanEnderecoWms();
            endWms.setDeposito(resultSet.getInt("deposito"));
            endWms.setRua(resultSet.getInt("rua"));
            endWms.setPredio(resultSet.getInt("predio"));
            endWms.setNivel(resultSet.getInt("nivel"));
            endWms.setApto(resultSet.getInt("apto"));
            endWms.setTipoender(resultSet.getString("tipoender"));
            endWms.setTipopal(resultSet.getInt("tipopal"));
            endWms.setBloqueio(resultSet.getString("bloqueio"));
            endWms.setSituacao(resultSet.getString("situacao"));
            endWms.setCodprod(resultSet.getLong("codprod"));
            endWms.setQt(resultSet.getDouble("qt"));
            endWms.setDtval(resultSet.getDate("dtval"));
            endWms.setCodarmazenagem(resultSet.getLong("codarmazenagem"));
            endWms.setCodestrutura(resultSet.getLong("codestrutura"));
            endWms.setQtbloqueada(resultSet.getDouble("qtbloqueada"));
            endWms.setDtultalter(resultSet.getDate("dtultalter"));
            endWms.setCodfuncultalter(resultSet.getLong("codfuncultalter"));
            endWms.setPckrotativo(resultSet.getString("pckrotativo"));
            endWms.setStatus(resultSet.getString("status"));
            endWms.setCodendereco(resultSet.getLong("codendereco"));
            endWms.setCodfuncsep(resultSet.getLong("codfuncsep"));
            endWms.setNuminvent(resultSet.getLong("numinvent"));
            endWms.setNumbonus(resultSet.getLong("numbonus"));
            endWms.setEstacao(resultSet.getLong("estacao"));
            endWms.setNumlote(resultSet.getString("numlote"));
            endWms.setQtreserv(resultSet.getDouble("qtreserv"));
            endWms.setCodfornec(resultSet.getLong("codfornec"));
            endWms.setPossuielevadorcarga(resultSet.getString("possuielevadorcarga"));
            endWms.setDigitoverificador(resultSet.getLong("digitoverificador"));
            endWms.setCodfilial(resultSet.getString("codfilial"));
            endWms.setQtpaleteender(resultSet.getLong("qtpaleteender"));
            endWms.setRuaent(resultSet.getLong("ruaent"));
            endWms.setPredioent(resultSet.getLong("predioent"));
            endWms.setCodmotivoavaria(resultSet.getLong("codmotivoavaria"));
            endWms.setAtivo(resultSet.getString("ativo"));
            endWms.setFilialgestaowms(resultSet.getString("filialgestaowms"));

            return endWms;
        }
        return null;
    }

//		/*
//		 * Método atualizar()
//		 * Método Responsável Por Atualizar os Dados (UPDATE) no BD
//		 * @param BeanProduto produto = Objeto Produto da Classe BeanProduto
//		 */
//		public void atualizar(BeanProduto produto) {
//			try {
//				String sql = "UPDATE produto SET nome = ?, quantidade = ?, valor = ? WHERE id = "+ produto.getId();
//				PreparedStatement preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, produto.getNome());
//				preparedStatement.setDouble(2, produto.getQuantidade());
//				preparedStatement.setDouble(3, produto.getValor());
//				preparedStatement.executeUpdate();
//				connection.commit();
//			} catch(Exception e) {
//				e.printStackTrace();
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
}
