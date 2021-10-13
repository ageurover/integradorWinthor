/*
 * Dao.java
 *
 * Created on 20 de Fevereiro de 2006, 19:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package winthorDb.jpa.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;
import oracle.toplink.essentials.config.HintValues;
import oracle.toplink.essentials.config.TopLinkQueryHints;
import oracle.toplink.essentials.ejb.cmp3.EntityManager;
import oracle.toplink.essentials.expressions.Expression;
import oracle.toplink.essentials.expressions.ExpressionBuilder;
import oracle.toplink.essentials.queryframework.ReadAllQuery;
import oracle.toplink.essentials.queryframework.ReadObjectQuery;
import oracle.toplink.essentials.tools.sessionmanagement.SessionManager;
import winthorDb.Main;
import winthorDb.error.trataErro;
import winthorDb.util.Formato;

/**
 *
 * @author Ageu Rover
 */
public class Dao {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;
    private static final SessionManager sm = null;

    /**
     * Creates a new instance of Dao
     */
    public Dao() {
    }
    // TESTADO

    public static EntityManagerFactory getEmf() throws Exception {

        // busca os dados da coneção para o usuario senha e base dados informados
        Map<String, Object> configOverrides = new HashMap<>();
        configOverrides.put("toplink.jdbc.user", Main.nomeUsuario);
        configOverrides.put("toplink.jdbc.password", Main.senhaUsuario);
        configOverrides.put("toplink.jdbc.schema", Main.sidServidor);
        // configOverrides.put("toplink.jdbc.url", "jdbc:mysql://" + Main.ipServidor + ":" + Main.portaServidor + "/" + Main.nomeSchema + "?zeroDateTimeBehavior=convertToNull");
        configOverrides.put("toplink.jdbc.url", Main.getConnectionDb);

        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("winthorPU", configOverrides);
        }

        if (!emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("winthorPU", configOverrides);
        }
        return emf;
    }

    // TESTADO
    public static EntityManager getEm() throws Exception {
        if (em == null) {
            em = (EntityManager) getEmf().createEntityManager();
        }

        if (!em.isOpen()) {
            em = (EntityManager) getEmf().createEntityManager();
        }
        return em;
    }

    public static void reconect() throws Exception {
        em.close();
        emf.close();
        getEm();
    }

    @SuppressWarnings("null")
    public static void save(Object o) throws Exception {
        if (o == null) {
            return;
        }
        EntityTransaction trx = null;
        try {
            trx = getEm().getTransaction();
            trx.begin();
            getEm().persist(o);
            getEm().flush();
            trx.commit();
        } catch (Exception ex) {
            trx.rollback();
            em.close();
            trataErro.trataException(ex, "Dao.save");
            System.err.print(ex.toString());
        } finally {
            trx = null;
        }
    }

    @SuppressWarnings("null")
    public static void delete(Object o) throws Exception {
        if (o == null) {
            return;
        }
        EntityTransaction trx = null;
        try {
            trx = em.getTransaction();
            trx.begin();
            getEm().remove(o);
            getEm().flush();
            trx.commit();
        } catch (Exception ex) {
            trx.rollback();
            em.close();
            trataErro.trataException(ex, "Dao.delete");
            System.err.print(ex.toString());
        } finally {
            trx = null;
        }
//        try {
//            getEm().getTransaction().begin();
//            getEm().remove(o);
//            getEm().flush();
//            getEm().getTransaction().commit();
//        } catch (Exception ex) {
//            getEm().getTransaction().rollback();
//            getEm().getActiveSession().release();
//            getEm().getSession().release();
//            em.close();
//            trataErro.trataException(ex, "Dao.delete");
//        }
        //refresh(o);
    }

    public static void refresh(Object o) throws Exception {
        if (o == null) {
            return;
        }
        getEm().refresh(o);
    }

    @SuppressWarnings("null")
    public static void saveOrUpdate(Object o) throws Exception {
        if (o == null) {
            return;
        }
        EntityTransaction trx = null;
        try {
            trx = getEm().getTransaction();
            trx.begin();
            getEm().merge(o);
            getEm().flush();
            trx.commit();
        } catch (Exception ex) {
            trx.rollback();
            em.close();
            trataErro.trataException(ex, "Dao.saveOrUpdate");
            System.err.print(ex.toString());
        } finally {
            trx = null;
        }
//        try {
//            getEm().getTransaction().begin();
//            getEm().merge(o);
//            getEm().flush();
//            getEm().getTransaction().commit();
//        } catch (Exception ex) {
//            getEm().getTransaction().rollback();
//            getEm().getActiveSession().release();
//            getEm().getSession().release();
//            em.close();
//            trataErro.trataException(ex, "Dao.saveOrUpdate");
//        }
    }

    @SuppressWarnings("null")
    public static void update(Object o) throws Exception {
        if (o == null) {
            return;
        }

        EntityTransaction trx = null;
        try {
            trx = getEm().getTransaction();
            trx.begin();
            getEm().persist(o);
            getEm().flush();
            trx.commit();
        } catch (Exception ex) {
            trx.rollback();
            em.close();
            trataErro.trataException(ex, "Dao.update");
            System.err.print(ex.toString());
        } finally {
            trx = null;
        }

//        try {
//            getEm().getTransaction().begin();
//            getEm().merge(o);
//            getEm().flush();
//            getEm().getTransaction().commit();
//        } catch (Exception ex) {
//            getEm().getTransaction().rollback();
////            getEm().getActiveSession().release();
////            getEm().getSession().release();
//            reconect();
//            trataErro.trataException(ex, "Dao.update");
//        }
    }

    public static Object getUnique(Class aClass, String field, Object value) throws Exception {

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        Expression e = builder.get(field).equal(value);

        ReadObjectQuery query = new ReadObjectQuery();

        query.setSelectionCriteria(e);

        return getEm().getActiveSession().readObject(aClass, e);

    }

    public static <T> T get(Class<T> aClass, String id) throws Exception {
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            System.err.print(ex.toString());
            return null;
        }

        return getEm().find(aClass, Integer.parseInt(id));

    }

    public static <T> T get(Class<T> aClass, Object id) throws Exception {
        return getEm().find(aClass, id);
    }

    public static Object getUnique(Class aClass, String[] field, Object[] value) throws Exception {
        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTiposPrimitivos(aClass, field, value);
        for (int i = 0; i < field.length; i++) {
            exs.add(builder.get(field[i]).equal(value[i]));
        }

        query = new ReadAllQuery();
        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);
        return getEm().getActiveSession().readObject(aClass, e);

    }

    /**
     * Retorna o primeiro objeto da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param field lista de nomes dos campos para filtrar os dados
     * @param value lista de dados a serem filtrados
     * @param tipo lista de tipos de filtro a ser aplicado no criterio
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que nao seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * <br>6 - Diferente
     * <br>7 - Maior ou igual
     * <br>8 - Menor ou igual
     * <br> Exemplo : Estoque = Dao.getUnique(Estoque.class,true,new
     * String[]{"idFilial","idProduto"},new String[]{idFilial,idProduto},new
     * int[]{3,3});
     * @return
     * @throws java.lang.Exception
     * @thow Excecao em caso de erro na consulta ao banco de dados
     */
    public static Object getUnique(Class aClass, String[] field, Object[] value, int[] tipo) throws Exception {
        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTiposPrimitivos(aClass, field, value);
        for (int i = 0; i < field.length; i++) {
            switch (tipo[i]) {
                case 0:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase((String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 1:
                    exs.add(builder.get(field[i]).equal(value[i]));
                    break;
                case 2:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i]));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 3:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 4:
                    exs.add(builder.get(field[i]).notNull());
                    break;
                case 5:
                    exs.add(builder.get(field[i]).isNull());
                    break;
                case 6:
                    exs.add(builder.get(field[i]).notEqual(value[i]));
                    break;
                case 7:
                    exs.add(builder.get(field[i]).greaterThanEqual(value[i]));
                    break;
                case 8:
                    exs.add(builder.get(field[i]).lessThanEqual(value[i]));
                    break;
            }
            // exs.add(builder.get(field[i]).equal(value[i]));
        }

        query = new ReadAllQuery();
        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);
        return getEm().getActiveSession().readObject(aClass, e);

    }

    // TESTADO FUNCIONANDO 100% DEUS É PAI!!!!!!!
    public static List listOrder(Class aClass, String[] fieldSortAsc, String[] fieldSortDsc) throws Exception {
        ExpressionBuilder builder = new ExpressionBuilder(aClass);

        ArrayList<Expression> exp = new ArrayList<>();

        if (fieldSortAsc != null) {
            for (String fieldSortAsc1 : fieldSortAsc) {
                exp.add(builder.get(fieldSortAsc1).ascending());
            }
        }

        if (fieldSortDsc != null) {
            for (String fieldSortDsc1 : fieldSortDsc) {
                exp.add(builder.get(fieldSortDsc1).descending());
            }
        }

        ReadAllQuery query = new ReadAllQuery();
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);

        for (Expression item : exp) {
            query.addOrdering(item); // PULO DO GATO!
        }

        return getEm().getActiveSession().readAllObjects(aClass, builder.getBaseExpression());
    }

    public static List list(String sqlString) throws Exception {
        Query q = getEm().createNativeQuery(sqlString);
        q.setHint(TopLinkQueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    public static int executeUpdateQuery(String sqlString) throws Exception {
        getEm().getTransaction().begin();

        Query queryRenameCity = getEm().createNativeQuery(sqlString);
        queryRenameCity.setHint(TopLinkQueryHints.REFRESH, HintValues.TRUE);
        int rowCount = queryRenameCity.executeUpdate();
        getEm().getTransaction().commit();

        return rowCount;
    }

    // TESTADO
    @SuppressWarnings("JPQLValidation")
    public static List list(Class aClass) throws Exception {
        return getEm().createQuery("select p from " + getClassName(aClass) + " p").getResultList();
    }

    // TESTADO
    public static List list(Class aClass, String order) throws Exception {
        return getEm().createQuery("select p from " + getClassName(aClass) + " p order by p." + order + " ASC").getResultList();
    }

    @SuppressWarnings("JPQLValidation")
    public static List list(Class aClass, int startIndex, int endIndex) throws Exception {
        Query q = getEm().createQuery("select p from " + getClassName(aClass) + " p");
        q.setHint(TopLinkQueryHints.REFRESH, HintValues.TRUE);
        q.setFirstResult(startIndex);
        q.setMaxResults(endIndex - startIndex + 1);

        return q.getResultList();
    }

    public static List list(Class aClass, boolean filtered, String field, Object value, int tipo) throws Exception {
        if (!filtered) {
            return list(aClass);
        }

        String[] fields = {field};
        Object[] values = {value};
        int[] tipos = {tipo};

        return list(aClass, filtered, fields, values, tipos);
    }

    public static List list(Class aClass, boolean filtered, String field, Object value, int tipo, String order) throws Exception {
        if (!filtered) {
            return list(aClass);
        }

        String[] fields = {field};
        Object[] values = {value};
        String[] orders = {order};
        int[] tipos = {tipo};

        return list(aClass, filtered, fields, values, tipos, orders);
    }

    public static Object normalizaTipoPrimitivo(Class aClass, String field, Object value) {
        try {
            Field f = aClass.getDeclaredField(field);
            Class f_class = f.getType();
            if (f_class.isAssignableFrom(Integer.class)) {
                if (value instanceof Integer) {
                    return value;
                }
                if (value != null) {
                    if (!value.equals("")) {
                        return Integer.parseInt(((String) value));
                    }
                }
            }

            if (f_class.isAssignableFrom(BigDecimal.class)) {
                if (value instanceof BigDecimal) {
                    return value;
                }
                return Formato.currStrToDecimal(((String) value));
            }
            if (f_class.isAssignableFrom(Date.class)) {
                if (value instanceof Date) {
                    return value;
                }
                return Formato.strToDate(((String) value));
            }
            // classes do jpa
            if (f_class.getAnnotation(Table.class) != null) {
                if (value instanceof String) {
                    return instanciaEntity(f_class, (String) value);
                } else {
                    return value;
                }
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            trataErro.trataException(ex);
            System.err.print(ex.toString());
        }
        return value;
    }

    public static Object instanciaEntity(Class c, String id) {
        try {
            Object o = c.newInstance();
            Class[] params = {String.class};
            Object[] valores = {id};
            Method m = c.getDeclaredMethod("setId", params);
            m.invoke(o, valores);
            return o;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            System.err.print(ex.toString());
            return null;
        }
    }

    public static Object instanciaEntity(Class c, Object id) {
        try {
            Object o = c.newInstance();
            Class[] params = {String.class};
            Object[] valores = {id};
            Method m = c.getDeclaredMethod("setId", params);
            m.invoke(o, valores);
            return o;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            System.err.print(ex.toString());
            return null;
        }
    }

    public static Object[] normalizaTiposPrimitivos(Class aClass, String[] field, Object[] value) {
        Object[] ret = new Object[value.length];
        for (int i = 0; i < field.length; i++) {
            ret[i] = normalizaTipoPrimitivo(aClass, field[i], value[i]);
        }
        return ret;
    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param filtered se vai filtrar ou n?o os dados
     * @param field lista de nomes dos campos para filtrar os dados
     * @param value lista de dados a serem filtrados
     * @param tipo lista de tipos de filtro a ser aplicado no criterio
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que n?o seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * <br>6 - Diferente
     * <br>7 - Maior ou igual
     * <br>8 - Menor ou igual
     * <br> Observa??o: Todos as lista de paramentros devem ter obrigat?riamente
     * a mesma quantidade de dados.
     * <br> Exemplo : lstEstoque = Dao.list(Estoque.class,true,new
     * String[]{"idFilial","idProduto"},new String[]{idFilial,idProduto},new
     * int[]{3,3});
     * @return
     * @throws java.lang.Exception
     * @thow Exce??o em caso de erro na consulta ao banco de dados
     *
     */
    public static List list(Class aClass, boolean filtered, String[] field, Object[] value, int[] tipo) throws Exception {
        if (!filtered) {
            return list(aClass);
        }

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTiposPrimitivos(aClass, field, value);
        for (int i = 0; i < tipo.length; i++) {
            switch (tipo[i]) {
                case 0:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase((String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 1:
                    exs.add(builder.get(field[i]).equal(value[i]));
                    break;
                case 2:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i]));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 3:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 4:
                    exs.add(builder.get(field[i]).notNull());
                    break;
                case 5:
                    exs.add(builder.get(field[i]).isNull());
                    break;
                case 6:
                    exs.add(builder.get(field[i]).notEqual(value[i]));
                    break;
                case 7:
                    exs.add(builder.get(field[i]).greaterThanEqual(value[i]));
                    break;
                case 8:
                    exs.add(builder.get(field[i]).lessThanEqual(value[i]));
                    break;
            }
        }

        query = new ReadAllQuery();
        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);
        return getEm().getActiveSession().readAllObjects(aClass, e);
    }

    public static Expression concatenaExpressions(List<Expression> es) {
        if (es == null || es.isEmpty()) {
            return null;
        }
        Expression primeira = es.get(0);
        es.remove(0);
        for (Expression e : es) {
            primeira = primeira.and(e);
        }
        return primeira;
    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param filtered se vai filtrar ou n?o os dados
     * @param field lista de nomes dos campos para filtrar os dados
     * @param value lista de dados a serem filtrados
     * @param valueBetween lista de dados a serem filtrados com a condi??o de
     * Between
     * @param tipo lista de tipos de filtro a ser aplicado no criterio
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que n?o seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * <br>6 - Diferente
     * <br>7 - Maior ou igual
     * <br>8 - Menor ou igual
     * <br>9 - Between (Entre dois valores)
     * <br>10 - NotBetween (Não esta Entre dois valores)
     * <br> Observa??o: Todos as lista de paramentros devem ter obrigat?riamente
     * a mesma quantidade de dados.
     * <br> Exemplo : lstEstoque = Dao.list(Estoque.class,true,new
     * String[]{"idFilial","idProduto"},new String[]{idFilial,idProduto},new
     * int[]{3,3});
     * @param order
     * @return
     * @throws java.lang.Exception
     * @thow Exce??o em caso de erro na consulta ao banco de dados
     *
     */
    public static List list(Class aClass, boolean filtered, String[] field,
            Object[] value, Object[] valueBetween, int[] tipo,
            String[] order) throws Exception {
        if (!filtered) {
            return list(aClass);
        }
        value = normalizaTiposPrimitivos(aClass, field, value);
        valueBetween = normalizaTiposPrimitivos(aClass, field, valueBetween);

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();
        ReadAllQuery query = null;

        for (int i = 0; i < tipo.length; i++) {
            switch (tipo[i]) {
                case 0:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase((String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 1:
                    exs.add(builder.get(field[i]).equal(value[i]));
                    break;
                case 2:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i]));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 3:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 4:
                    exs.add(builder.get(field[i]).notNull());
                    break;
                case 5:
                    exs.add(builder.get(field[i]).isNull());
                    break;
                case 6:
                    exs.add(builder.get(field[i]).notEqual(value[i]));
                    break;
                case 7:
                    exs.add(builder.get(field[i]).greaterThanEqual(value[i]));
                    break;
                case 8:
                    exs.add(builder.get(field[i]).lessThanEqual(value[i]));
                    break;
                case 9:
                    exs.add(builder.get(field[i]).between(value[i], valueBetween[i]));
                    break;
                case 10:
                    exs.add(builder.get(field[i]).notBetween(value[i], valueBetween[i]));
                    break;
            }
        }

        query = new ReadAllQuery();

        for (String s : order) {
            query.addOrdering(builder.get(s).ascending());
        }

        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);

        return getEm().getActiveSession().readAllObjects(aClass, e);

    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param filtered se vai filtrar ou n?o os dados
     * @param field lista de nomes dos campos para filtrar os dados
     * @param value lista de dados a serem filtrados
     * @param tipo lista de tipos de filtro a ser aplicado no criterio
     * @param order lista de campos que ficaram em ordem
     * @return retorna um lista com os dados
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que n?o seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * <br>6 - Diferente
     * <br>7 - Between (Entre dois valores)
     * <br> Observa??o: Todos as lista de paramentros devem ter obrigat?riamente
     * a mesma quantidade de dados.
     * <br> Exemplo : lstEstoque = Dao.list(Estoque.class,true,new
     * String[]{"idFilial","idProduto"},new String[]{idFilial,idProduto},new
     * int[]{3,3});
     *
     * @throws java.lang.Exception
     * @thow Exce??o em caso de erro na consulta ao banco de dados
     *
     */
    public static List list(Class aClass, boolean filtered, String[] field, Object[] value, int[] tipo, String[] order) throws Exception {
        if (!filtered) {
            return list(aClass);
        }

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTiposPrimitivos(aClass, field, value);
        for (int i = 0; i < tipo.length; i++) {
            switch (tipo[i]) {
                case 0:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase((String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 1:
                    exs.add(builder.get(field[i]).equal(value[i]));
                    break;
                case 2:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i]));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 3:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 4:
                    exs.add(builder.get(field[i]).notNull());
                    break;
                case 5:
                    exs.add(builder.get(field[i]).isNull());
                    break;
                case 6:
                    exs.add(builder.get(field[i]).notEqual(value[i]));
                    break;
                case 7:
                    exs.add(builder.get(field[i]).greaterThanEqual(value[i]));
                    break;
                case 8:
                    exs.add(builder.get(field[i]).lessThanEqual(value[i]));
                    break;
            }
        }

        query = new ReadAllQuery();

        for (String s : order) {
            query.addOrdering(builder.get(s).ascending());
        }

        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);

        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);

        return getEm().getActiveSession().readAllObjects(aClass, e);
    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param startIndex Registro inicial
     * @param endIndex Registro Final
     * @param filtered se vai filtrar ou n?o os dados
     * @param field lista de nomes dos campos para filtrar os dados
     * @param value lista de dados a serem filtrados
     * @param tipo lista de tipos de filtro a ser aplicado no criterio
     * @return retorna um lista com os dados
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que n?o seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * <br>6 - Diferente
     * <br>7 - Between (Entre dois valores)
     * <br> Observa??o: Todos as lista de paramentros devem ter obrigat?riamente
     * a mesma quantidade de dados.
     * <br> Exemplo : lstEstoque = Dao.list(Estoque.class,true,new
     * String[]{"idFilial","idProduto"},new String[]{idFilial,idProduto},new
     * int[]{3,3});
     *
     * @throws java.lang.Exception
     * @thow Exce??o em caso de erro na consulta ao banco de dados
     *
     */
    public static List list(Class aClass, int startIndex, int endIndex, boolean filtered, String field, Object value, int tipo) throws Exception {
        if (!filtered) {
            return list(aClass, startIndex, endIndex);
        }

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTipoPrimitivo(aClass, field, value);

        switch (tipo) {
            case 0:
                if (value instanceof String) {
                    exs.add(builder.get(field).likeIgnoreCase((String) value + "%"));
                } else {
                    exs.add(builder.get(field).equal(value));
                }
                break;
            case 1:
                exs.add(builder.get(field).equal(value));
                break;
            case 2:
                if (value instanceof String) {
                    exs.add(builder.get(field).likeIgnoreCase("%" + (String) value));
                } else {
                    exs.add(builder.get(field).equal(value));
                }
                break;
            case 3:
                if (value instanceof String) {
                    exs.add(builder.get(field).likeIgnoreCase("%" + (String) value + "%"));
                } else {
                    exs.add(builder.get(field).equal(value));
                }
                break;
            case 4:
                exs.add(builder.get(field).notNull());
                break;
            case 5:
                exs.add(builder.get(field).isNull());
                break;
            case 6:
                exs.add(builder.get(field).notEqual(value));
                break;
            case 7:
                exs.add(builder.get(field).greaterThanEqual(value));
                break;
            case 8:
                exs.add(builder.get(field).lessThanEqual(value));
                break;
        }

        query = new ReadAllQuery();
        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);

        query.setFirstResult(startIndex);
        query.setMaxRows(endIndex - startIndex + 1);

        return getEm().getActiveSession().readAllObjects(aClass, e);
    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param startIndex o valor inicial da pagina??o
     * @param endIndex o valor final da pagina??o
     * @param filtered se vai filtrar ou n?o os dados
     * @param field o nome do campo para filtrar os dados
     * @param value o dado a serem filtrados
     * @param tipo o tipo de filtro a ser aplicado no criterio
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que n?o seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * @return
     * @throws java.lang.Exception
     * @thow Exce??o em caso de erro na consulta ao banco de dados
     *
     */
    public static List list(Class aClass, int startIndex, int endIndex, boolean filtered, String[] field, Object[] value, int[] tipo) throws Exception {
        if (!filtered) {
            return list(aClass, startIndex, endIndex);
        }

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTiposPrimitivos(aClass, field, value);
        for (int i = 0; i < tipo.length; i++) {
            switch (tipo[i]) {
                case 0:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase((String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 1:
                    exs.add(builder.get(field[i]).equal(value[i]));
                    break;
                case 2:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i]));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 3:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 4:
                    exs.add(builder.get(field[i]).notNull());
                    break;
                case 5:
                    exs.add(builder.get(field[i]).isNull());
                    break;
                case 6:
                    exs.add(builder.get(field[i]).notEqual(value[i]));
                    break;
                case 7:
                    exs.add(builder.get(field[i]).greaterThanEqual(value[i]));
                    break;
                case 8:
                    exs.add(builder.get(field[i]).lessThanEqual(value[i]));
                    break;
            }
        }

        query = new ReadAllQuery();
        Expression e = concatenaExpressions(exs);
        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);

        query.setFirstResult(startIndex);
        query.setMaxRows(endIndex - startIndex + 1);

        return getEm().getActiveSession().readAllObjects(aClass, e);
    }

    /**
     * Retorna uma lista de objetos da classe passada como parametro
     *
     * @param aClass a classe ser retornada na lista
     * @param startIndex o valor inicial da pagina??o
     * @param endIndex o valor final da pagina??o
     * @param filtered se vai filtrar ou n?o os dados
     * @param field o nome do campo para filtrar os dados
     * @param value o dado a serem filtrados
     * @param tipo o tipo de filtro a ser aplicado no criterio
     * <br>0 - Que come?am com o valor
     * <br>1 - Que seja exatamente igual ao valor
     * <br>2 - Que terminem com o valor
     * <br>3 - Que contenha em qualquer parte o valor
     * <br>4 - Que n?o seja igual a nulo
     * <br>5 - Que seja igual a nulo
     * @param order
     * @return
     * @throws java.lang.Exception
     * @thow Exce??o em caso de erro na consulta ao banco de dados
     *
     */
    public static List list(Class aClass, int startIndex, int endIndex, boolean filtered, String[] field, Object[] value, int[] tipo, String[] order) throws Exception {
        if (!filtered) {
            return list(aClass, startIndex, endIndex);
        }

        ExpressionBuilder builder = new ExpressionBuilder(aClass);
        ArrayList<Expression> exs = new ArrayList<>();

        ReadAllQuery query = null;

        value = normalizaTiposPrimitivos(aClass, field, value);
        for (int i = 0; i < tipo.length; i++) {
            switch (tipo[i]) {
                case 0:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase((String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 1:
                    exs.add(builder.get(field[i]).equal(value[i]));
                    break;
                case 2:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i]));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 3:
                    if (value[i] instanceof String) {
                        exs.add(builder.get(field[i]).likeIgnoreCase("%" + (String) value[i] + "%"));
                    } else {
                        exs.add(builder.get(field[i]).equal(value[i]));
                    }
                    break;
                case 4:
                    exs.add(builder.get(field[i]).notNull());
                    break;
                case 5:
                    exs.add(builder.get(field[i]).isNull());
                    break;
                case 6:
                    exs.add(builder.get(field[i]).notEqual(value[i]));
                    break;
                case 7:
                    exs.add(builder.get(field[i]).greaterThanEqual(value[i]));
                    break;
                case 8:
                    exs.add(builder.get(field[i]).lessThanEqual(value[i]));
                    break;
            }
        }

        ArrayList<Expression> exp = new ArrayList<>();
        query = new ReadAllQuery();

        if (order != null) {
            for (String order1 : order) {
                exp.add(builder.get(order1).ascending());
            }
        }

        for (Expression item : exp) {
            for (String order1 : order) {
                //exp.add(builder.get(order[i]).ascending());
                query.addAscendingOrdering(order1); // PULO DO GATO!
            }
        }

        Expression e = concatenaExpressions(exs);

        query.setSelectionCriteria(e);
        query.setCacheUsage(ReadAllQuery.DoNotCheckCache);

        query.setFirstResult(startIndex);
        query.setMaxRows(endIndex - startIndex + 1);

        return getEm().getActiveSession().readAllObjects(aClass, e);
    }

    @SuppressWarnings("JPQLValidation")
    public static int getMaxResult(Class aClass) throws Exception {
        return ((Long) getEm().createQuery("select count(p) from " + getClassName(aClass) + " p").getSingleResult()).intValue();

    }

    public static String getTableName(Object o) {
        Class c = o.getClass();
        Table t = (Table) c.getAnnotation(Table.class);
        return t.name();
    }

    public static String getTableName(Class c) {
        Table t = (Table) c.getAnnotation(Table.class);
        return t.name();
    }

    public static String getClassName(Class c) {
        String FQClassName = c.getName();
        int firstChar;
        firstChar = FQClassName.lastIndexOf('.') + 1;
        if (firstChar > 0) {
            FQClassName = FQClassName.substring(firstChar);
        }
        return FQClassName;
    }

    public static String getClassName(Object o) {
        String FQClassName = o.getClass().getName();
        int firstChar;
        firstChar = FQClassName.lastIndexOf('.') + 1;
        if (firstChar > 0) {
            FQClassName = FQClassName.substring(firstChar);
        }
        return FQClassName;
    }

    public static void main(String[] args) {
//        try {
//
////            Dao dao = new Dao();
////            Dao.getUnique(Unidade.class, "sigla", "RO");
//        } catch (Exception ex) {
//        }

    }
}
