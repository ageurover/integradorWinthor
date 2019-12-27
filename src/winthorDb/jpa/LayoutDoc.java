/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * nnn
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
 * @author Ageu Rover
 */
@Entity
@Table(name = "layoutDoc")
public class LayoutDoc implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    // BOL - Boleto\nAPAC - Apac Sus
    @Column(name = "tipoDoc", length = 5)
    private String tipoDoc;
    // Codigo do documento originador exemplo boleto banco brasil como o id do boleto
    @Column(name = "idDoc", length = 11)
    private Integer idDoc;
    //-'H - Header\nD - Detalhe\nDo - Detalhe opcional \nT - Treller'
    @Column(name = "tipoRegistro", length = 5)
    private String tipoRegistro;
    // Sequencia de execucao da geracao do layout
    @Column(name = "sequencia", length = 11)
    private Integer sequencia;
    // 'ALF - alfa\nNMR - numerico\nDTA - data\nVZA - VAZIO / BRANCOS\nZRO - ZEROS'
    @Column(name = "tipoDado", length = 5)
    private String tipoDado;
    // Tamanho do Campo Diferenca entre a posicao inicial e final do campo
    @Column(name = "tamanho", length = 11)
    private Integer tamanho;
    // Posicao incial do campo dentro do registro
    @Column(name = "posIncial", length = 11)
    private Integer posIncial;
    // Posicao final do campo dentro do registro
    @Column(name = "posFinal", length = 11)
    private Integer posFinal;
    // Campo da tabela ou comando SQL a ser executado
    @Column(name = "sqlCampo", length = 45)
    private String sqlCampo;
    // Comentario a respeito do campo a ser gerado
    @Column(name = "comentario", length = 255)
    private String comentario;
    // Masca de exibicao para numeros e datas
    @Column(name = "mascara", length = 45)
    private String mascara;
    // Valor default quando o retorno do campo for vazio ou null
    @Column(name = "valor_default", length = 45)
    private String valor_default;
    //
    public LayoutDoc() {
    }

    public LayoutDoc(Integer id) {
        this.id = id;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutDoc)) {
            return false;
        }
        LayoutDoc other = (LayoutDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.rovertecnologia.gestao.jpa.BoletosLayout[id=" + id + "]";
    }

    /**
     * @return the tipoRegistro
     */
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * @param tipoRegistro the tipoRegistro to set
     */
    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    /**
     * @return the tipoDado
     */
    public String getTipoDado() {
        return tipoDado;
    }

    /**
     * @param tipoDado the tipoDado to set
     */
    public void setTipoDado(String tipoDado) {
        this.tipoDado = tipoDado;
    }

    /**
     * @return the sequencia
     */
    public Integer getSequencia() {
        return sequencia;
    }

    /**
     * @param sequencia the sequencia to set
     */
    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    /**
     * @return the tamanho
     */
    public Integer getTamanho() {
        return tamanho;
    }

    /**
     * @param tamanho the tamanho to set
     */
    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * @return the posIncial
     */
    public Integer getPosIncial() {
        return posIncial;
    }

    /**
     * @param posIncial the posIncial to set
     */
    public void setPosIncial(Integer posIncial) {
        this.posIncial = posIncial;
    }

    /**
     * @return the posFinal
     */
    public Integer getPosFinal() {
        return posFinal;
    }

    /**
     * @param posFinal the posFinal to set
     */
    public void setPosFinal(Integer posFinal) {
        this.posFinal = posFinal;
    }

    /**
     * @return the sqlCampo
     */
    public String getSqlCampo() {
        return sqlCampo;
    }

    /**
     * @param sqlCampo the sqlCampo to set
     */
    public void setSqlCampo(String sqlCampo) {
        this.sqlCampo = sqlCampo;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

   

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the mascara
     */
    public String getMascara() {
        return mascara;
    }

    /**
     * @param mascara the mascara to set
     */
    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    /**
     * @return the valor_default
     */
    public String getValor_default() {
        return valor_default;
    }

    /**
     * @param valor_default the valor_default to set
     */
    public void setValor_default(String valor_default) {
        this.valor_default = valor_default;
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

}
