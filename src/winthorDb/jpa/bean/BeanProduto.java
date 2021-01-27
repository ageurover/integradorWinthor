/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.jpa.bean;

/**
 *
 * @author ageurover
 */
public class BeanProduto {

    private Long codprod;
    private String descricao;
    private String embalagem;
    private String embalagemmaster;
    private String codauxiliar;
    private String codfilial;
    private Long codenderecoloja;
    private double qtfrentegondula;
    private double qtfundogondula;
    private double qtalturagondula;
    private double pontoreposicao;
    private double capacidade;
    private double percpontoreposicao;
    private double qtUnidadeMaster;
    private String pcestendloja;
    private String brendecoloja; 
    private double girodia;
    
    /**
     * @return the codprod
     */
    public Long getCodprod() {
        return codprod;
    }

    /**
     * @param codprod the codprod to set
     */
    public void setCodprod(Long codprod) {
        this.codprod = codprod;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the embalagem
     */
    public String getEmbalagem() {
        return embalagem;
    }

    /**
     * @param embalagem the embalagem to set
     */
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    /**
     * @return the codauxiliar
     */
    public String getCodauxiliar() {
        return codauxiliar;
    }

    /**
     * @param codauxiliar the codauxiliar to set
     */
    public void setCodauxiliar(String codauxiliar) {
        this.codauxiliar = codauxiliar;
    }

    /**
     * @return the codfilial
     */
    public String getCodfilial() {
        return codfilial;
    }

    /**
     * @param codfilial the codfilial to set
     */
    public void setCodfilial(String codfilial) {
        this.codfilial = codfilial;
    }

    /**
     * @return the qtfrentegondula
     */
    public double getQtfrentegondula() {
        return qtfrentegondula;
    }

    /**
     * @param qtfrentegondula the qtfrentegondula to set
     */
    public void setQtfrentegondula(double qtfrentegondula) {
        this.qtfrentegondula = qtfrentegondula;
    }

    /**
     * @return the qtfundogondula
     */
    public double getQtfundogondula() {
        return qtfundogondula;
    }

    /**
     * @param qtfundogondula the qtfundogondula to set
     */
    public void setQtfundogondula(double qtfundogondula) {
        this.qtfundogondula = qtfundogondula;
    }

    /**
     * @return the qtalturagondula
     */
    public double getQtalturagondula() {
        return qtalturagondula;
    }

    /**
     * @param qtalturagondula the qtalturagondula to set
     */
    public void setQtalturagondula(double qtalturagondula) {
        this.qtalturagondula = qtalturagondula;
    }

    /**
     * @return the pontoreposicao
     */
    public double getPontoreposicao() {
        return pontoreposicao;
    }

    /**
     * @param pontoreposicao the pontoreposicao to set
     */
    public void setPontoreposicao(double pontoreposicao) {
        this.pontoreposicao = pontoreposicao;
    }

    /**
     * @return the capacidade
     */
    public double getCapacidade() {
        return capacidade;
    }

    /**
     * @param capacidade the capacidade to set
     */
    public void setCapacidade(double capacidade) {
        this.capacidade = capacidade;
    }

    /**
     * @return the embalagemmaster
     */
    public String getEmbalagemmaster() {
        return embalagemmaster;
    }

    /**
     * @param embalagemmaster the embalagemmaster to set
     */
    public void setEmbalagemmaster(String embalagemmaster) {
        this.embalagemmaster = embalagemmaster;
    }

    /**
     * @return the codenderecoloja
     */
    public Long getCodenderecoloja() {
        return codenderecoloja;
    }

    /**
     * @param codenderecoloja the codenderecoloja to set
     */
    public void setCodenderecoloja(Long codenderecoloja) {
        this.codenderecoloja = codenderecoloja;
    }

    /**
     * @return the pcestendloja
     */
    public String getPcestendloja() {
        return pcestendloja;
    }

    /**
     * @param pcestendloja the pcestendloja to set
     */
    public void setPcestendloja(String pcestendloja) {
        this.pcestendloja = pcestendloja;
    }

    /**
     * @return the brendecoloja
     */
    public String getBrendecoloja() {
        return brendecoloja;
    }

    /**
     * @param brendecoloja the brendecoloja to set
     */
    public void setBrendecoloja(String brendecoloja) {
        this.brendecoloja = brendecoloja;
    }

    /**
     * @return the percpontoreposicao
     */
    public double getPercpontoreposicao() {
        return percpontoreposicao;
    }

    /**
     * @param percpontoreposicao the percpontoreposicao to set
     */
    public void setPercpontoreposicao(double percpontoreposicao) {
        this.percpontoreposicao = percpontoreposicao;
    }

    /**
     * @return the qtUnidadeMaster
     */
    public double getQtUnidadeMaster() {
        return qtUnidadeMaster;
    }

    /**
     * @param qtUnidadeMaster the qtUnidadeMaster to set
     */
    public void setQtUnidadeMaster(double qtUnidadeMaster) {
        this.qtUnidadeMaster = qtUnidadeMaster;
    }

    /**
     * @return the girodia
     */
    public double getGirodia() {
        return girodia;
    }

    /**
     * @param girodia the girodia to set
     */
    public void setGirodia(double girodia) {
        this.girodia = girodia;
    }

}
