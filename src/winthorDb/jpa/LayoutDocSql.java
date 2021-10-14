/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * xx
 */

package winthorDb.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name="LayoutDocSql")
public class LayoutDocSql implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    //
    @Column(name = "idDoc", nullable = false, length=11)
    private Integer idDoc;
    //-
    @Column(name = "tipoDoc", nullable = true, length=5)
    private String tipoDoc;
    //-
    @Column(name = "sql_Header", nullable = true, length=8092)
    private String sqlHeader;
        //-
    @Column(name = "sql_Detalhe_n0", nullable = true, length=8092)
    private String sqlDetalhe_n0;
            //-
    @Column(name = "sql_Detalhe_n1", nullable = true, length=8092)
    private String sqlDetalhe_n1;
            //-
    @Column(name = "sql_Detalhe_n2", nullable = true, length=8092)
    private String sqlDetalhe_n2;
                //-
    @Column(name = "sql_Detalhe_n3", nullable = true, length=8092)
    private String sqlDetalhe_n3;
                    //-
    @Column(name = "sql_Detalhe_n4", nullable = true, length=8092)
    private String sqlDetalhe_n4;
                    //-
    @Column(name = "sql_Detalhe_n5", nullable = true, length=8092)
    private String sqlDetalhe_n5;
        //-
    @Column(name = "sql_Treller", nullable = true, length=8092)
    private String sqlTreller;
    
    public LayoutDocSql() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutDocSql)) {
            return false;
        }
        LayoutDocSql other = (LayoutDocSql) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + getId() + " - " + getIdDoc() + " - " + getTipoDoc();
    }

    /**
     * @return the idDoc
     */
    public Integer getIdDoc() {
        return idDoc;
    }

    /**
     * @param idDoc the idDoc to set
     */
    public void setIdDoc(Integer idDoc) {
        this.idDoc = idDoc;
    }

    /**
     * @return the tipoDoc
     */
    public String getTipoDoc() {
        return tipoDoc;
    }

    /**
     * @param tipoDoc the tipoDoc to set
     */
    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    /**
     * @return the sql_Header
     */
    public String getSql_Header() {
        return sqlHeader;
    }

    /**
     * @param sql_Header the sql_Header to set
     */
    public void setSql_Header(String sql_Header) {
        this.sqlHeader = sql_Header;
    }

    /**
     * @return the sql_Treller
     */
    public String getSql_Treller() {
        return sqlTreller;
    }

    /**
     * @param sql_Treller the sql_Treller to set
     */
    public void setSql_Treller(String sql_Treller) {
        this.sqlTreller = sql_Treller;
    }

    /**
     * @return the sqlDetalhe_n0
     */
    public String getSqlDetalhe_n0() {
        return sqlDetalhe_n0;
    }

    /**
     * @param sqlDetalhe_n0 the sqlDetalhe_n0 to set
     */
    public void setSqlDetalhe_n0(String sqlDetalhe_n0) {
        this.sqlDetalhe_n0 = sqlDetalhe_n0;
    }

    /**
     * @return the sqlDetalhe_n1
     */
    public String getSqlDetalhe_n1() {
        return sqlDetalhe_n1;
    }

    /**
     * @param sqlDetalhe_n1 the sqlDetalhe_n1 to set
     */
    public void setSqlDetalhe_n1(String sqlDetalhe_n1) {
        this.sqlDetalhe_n1 = sqlDetalhe_n1;
    }

    /**
     * @return the sqlDetalhe_n2
     */
    public String getSqlDetalhe_n2() {
        return sqlDetalhe_n2;
    }

    /**
     * @param sqlDetalhe_n2 the sqlDetalhe_n2 to set
     */
    public void setSqlDetalhe_n2(String sqlDetalhe_n2) {
        this.sqlDetalhe_n2 = sqlDetalhe_n2;
    }

    /**
     * @return the sqlDetalhe_n3
     */
    public String getSqlDetalhe_n3() {
        return sqlDetalhe_n3;
    }

    /**
     * @param sqlDetalhe_n3 the sqlDetalhe_n3 to set
     */
    public void setSqlDetalhe_n3(String sqlDetalhe_n3) {
        this.sqlDetalhe_n3 = sqlDetalhe_n3;
    }

    /**
     * @return the sqlDetalhe_n4
     */
    public String getSqlDetalhe_n4() {
        return sqlDetalhe_n4;
    }

    /**
     * @param sqlDetalhe_n4 the sqlDetalhe_n4 to set
     */
    public void setSqlDetalhe_n4(String sqlDetalhe_n4) {
        this.sqlDetalhe_n4 = sqlDetalhe_n4;
    }

    /**
     * @return the sqlDetalhe_n5
     */
    public String getSqlDetalhe_n5() {
        return sqlDetalhe_n5;
    }

    /**
     * @param sqlDetalhe_n5 the sqlDetalhe_n5 to set
     */
    public void setSqlDetalhe_n5(String sqlDetalhe_n5) {
        this.sqlDetalhe_n5 = sqlDetalhe_n5;
    }
}
