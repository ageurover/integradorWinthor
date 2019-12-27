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
    @Column(name = "sql_Detalhe", nullable = true, length=8092)
    private String sqlDetalhe;
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
     * @return the sql_Detalhe
     */
    public String getSql_Detalhe() {
        return sqlDetalhe;
    }

    /**
     * @param sql_Detalhe the sql_Detalhe to set
     */
    public void setSql_Detalhe(String sql_Detalhe) {
        this.sqlDetalhe = sql_Detalhe;
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

}
