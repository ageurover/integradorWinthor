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
public class BeanEnderecoLoja {
    private Long codprod;
    private String descricao;
    private Long codendereco;
    private double pontoreposicao;
    private double capacidade;
    private String codfilial;
    private String embalagem;

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
     * @return the codendereco
     */
    public Long getCodendereco() {
        return codendereco;
    }

    /**
     * @param codendereco the codendereco to set
     */
    public void setCodendereco(Long codendereco) {
        this.codendereco = codendereco;
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
    
}
