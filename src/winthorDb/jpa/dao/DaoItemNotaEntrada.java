package winthorDb.jpa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import winthorDb.jpa.bean.BeanItemNotaEntrada;

import winthorDb.jpa.connection.SingleConnection;

/*
 * Classe DaoProduto
 * Classe Que Provê os Métodos e Validações Para Manipular Dados, e Acesso e Manipulação do BD
 */
public class DaoItemNotaEntrada {

    private Connection connection;

    /*
		 * Construtor DaoProduto()
		 * Recebe um Objeto connection da Classe SingleConnection
     */
    public DaoItemNotaEntrada() {
        connection = SingleConnection.getConnection();
    }

    /*
		 * Método listar()
		 * Responsável Por Listar Todos os Produtos do Sistema
     */
    public List<BeanItemNotaEntrada> listarItemNota(String idFilial, String numnota) throws Exception {
        List<BeanItemNotaEntrada> ListaItem = new ArrayList<>();
        String sql = "select e.codfilial, e.numtransent, e.numnota, replace(replace(replace(e.cgc,'.',''),'/',''),'-','') as cgc, e.rowid rowidnf, \n"
                + "       m.codprod, m.numseq, m.rowid rowidmov,\n"
                + "       mc.numseqent, mc.codfabrica, mc.rowid rowidcomplemento,\n"
                + "       p.codfab, p.codauxiliar, p.codauxiliar2\n"
                + " from pcnfent e, pcmov m, pcmovcomple mc, pcprodut p\n"
                + " where e.numtransent = m.numtransent\n"
                + " and mc.numtransitem = m.numtransitem\n"
                + " and p.codprod = m.codprod\n"
                + " and e.numnota = " + numnota
                + " and e.codfilial = " + idFilial;
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            BeanItemNotaEntrada item = new BeanItemNotaEntrada();
            item.setCodprod(resultSet.getLong("codprod"));
            item.setCodfilial(resultSet.getString("codfilial"));
            item.setNumtransent(resultSet.getLong("numtransent"));
            item.setCgc(resultSet.getString("cgc"));
            item.setNumnota(resultSet.getLong("numnota"));
            item.setNumseq(resultSet.getLong("numseq"));
            item.setNumseqent(resultSet.getLong("numseqent"));
            item.setCodfabrica(resultSet.getString("codfabrica"));
            item.setCodfab(resultSet.getString("codfab"));
            item.setCodauxiliar(resultSet.getString("codauxiliar"));
            item.setCodauxiliar2(resultSet.getString("codauxiliar2"));
            item.setRowidnf(resultSet.getString("rowidnf"));
            item.setRowidmov(resultSet.getString("rowidmov"));
            item.setRowidcomplemento(resultSet.getString("rowidcomplemento"));

            ListaItem.add(item);
        }
        return ListaItem;
    }

    /*
		 * Método atualizar()
		 * Método Responsável Por Atualizar os Dados (UPDATE) no BD
		 * @param BeanItemNotaEntrada itens nota de entrada
     */
    public void atualizar(List<BeanItemNotaEntrada> itens) {
        try {
            for (int i = 0; i < itens.size(); i++) {
                BeanItemNotaEntrada item = itens.get(i);
                if (item.getNovonumseq() != null) {
                    String sql = "UPDATE pcmov SET numseq = ? WHERE rowid = " + item.getRowidmov();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setLong(1, item.getNovonumseq());
                    preparedStatement.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
            }
        }
    }
}
