/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * xx
 */
package winthorDb.jpa;

import java.io.Serializable;

/**
 *
 * @author Ageu Rover
 */
public class ContaPagar implements Serializable {

    private String codFilial;
    private String indice = "A";
    private String recNum;
    private String numTransent;
    private String codConta;
    private String tipoParceiro = "F";
    private String codFornec;
    private String dtEmissao;
    private String dtLanc;
    private String dtVenc;
    private String dtPagto;
    private String numNota;
    private String duplic;
    private String valor;
    private String historico;
    private String moeda = "R";
    private String tipoLanc = "C";
    private String tipoPagto = "DIN";
    private String formaPagto = "41";
    private String tipoServico = "99";
    private String nfServico = "N";
    private String fornecedor;
    private String dtCompetencia;
    private String codComprador = "4";
    private String utilizouRateioconta = "N";
    private String prcRateioUtilizado = "100";
    private String numTrans;

    public ContaPagar() {
    }

    /**
     * @return the codFilial
     */
    public String getCodFilial() {
        return codFilial;
    }

    /**
     * @param codFilial the codFilial to set
     */
    public void setCodFilial(String codFilial) {
        this.codFilial = codFilial;
    }

    /**
     * @return the indice
     */
    public String getIndice() {
        return indice;
    }

    /**
     * @param indice the indice to set
     */
    public void setIndice(String indice) {
        this.indice = indice;
    }

    /**
     * @return the recNum
     */
    public String getRecNum() {
        return recNum;
    }

    /**
     * @param recNum the recNum to set
     */
    public void setRecNum(String recNum) {
        this.recNum = recNum;
    }

    /**
     * @return the numTransent
     */
    public String getNumTransent() {
        return numTransent;
    }

    /**
     * @param numTransent the numTransent to set
     */
    public void setNumTransent(String numTransent) {
        this.numTransent = numTransent;
    }

    /**
     * @return the codConta
     */
    public String getCodConta() {
        return codConta;
    }

    /**
     * @param codConta the codConta to set
     */
    public void setCodConta(String codConta) {
        this.codConta = codConta;
    }

    /**
     * @return the tipoParceiro
     */
    public String getTipoParceiro() {
        return tipoParceiro;
    }

    /**
     * @param tipoParceiro the tipoParceiro to set
     */
    public void setTipoParceiro(String tipoParceiro) {
        this.tipoParceiro = tipoParceiro;
    }

    /**
     * @return the codFornec
     */
    public String getCodFornec() {
        return codFornec;
    }

    /**
     * @param codFornec the codFornec to set
     */
    public void setCodFornec(String codFornec) {
        this.codFornec = codFornec;
    }

    /**
     * @return the dtEmissao
     */
    public String getDtEmissao() {
        return dtEmissao;
    }

    /**
     * @param dtEmissao the dtEmissao to set
     */
    public void setDtEmissao(String dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    /**
     * @return the dtLanc
     */
    public String getDtLanc() {
        return dtLanc;
    }

    /**
     * @param dtLanc the dtLanc to set
     */
    public void setDtLanc(String dtLanc) {
        this.dtLanc = dtLanc;
    }

    /**
     * @return the dtVenc
     */
    public String getDtVenc() {
        return dtVenc;
    }

    /**
     * @param dtVenc the dtVenc to set
     */
    public void setDtVenc(String dtVenc) {
        this.dtVenc = dtVenc;
    }

    /**
     * @return the dtPagto
     */
    public String getDtPagto() {
        return dtPagto;
    }

    /**
     * @param dtPagto the dtPagto to set
     */
    public void setDtPagto(String dtPagto) {
        this.dtPagto = dtPagto;
    }

    /**
     * @return the numNota
     */
    public String getNumNota() {
        return numNota;
    }

    /**
     * @param numNota the numNota to set
     */
    public void setNumNota(String numNota) {
        this.numNota = numNota;
    }

    /**
     * @return the duplic
     */
    public String getDuplic() {
        return duplic;
    }

    /**
     * @param duplic the duplic to set
     */
    public void setDuplic(String duplic) {
        this.duplic = duplic;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the historico
     */
    public String getHistorico() {
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(String historico) {
        this.historico = historico;
    }

    /**
     * @return the moeda
     */
    public String getMoeda() {
        return moeda;
    }

    /**
     * @param moeda the moeda to set
     */
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    /**
     * @return the tipoLanc
     */
    public String getTipoLanc() {
        return tipoLanc;
    }

    /**
     * @param tipoLanc the tipoLanc to set
     */
    public void setTipoLanc(String tipoLanc) {
        this.tipoLanc = tipoLanc;
    }

    /**
     * @return the tipoPagto
     */
    public String getTipoPagto() {
        return tipoPagto;
    }

    /**
     * @param tipoPagto the tipoPagto to set
     */
    public void setTipoPagto(String tipoPagto) {
        this.tipoPagto = tipoPagto;
    }

    /**
     * @return the formaPagto
     */
    public String getFormaPagto() {
        return formaPagto;
    }

    /**
     * @param formaPagto the formaPagto to set
     */
    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }

    /**
     * @return the tipoServico
     */
    public String getTipoServico() {
        return tipoServico;
    }

    /**
     * @param tipoServico the tipoServico to set
     */
    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    /**
     * @return the nfServico
     */
    public String getNfServico() {
        return nfServico;
    }

    /**
     * @param nfServico the nfServico to set
     */
    public void setNfServico(String nfServico) {
        this.nfServico = nfServico;
    }

    /**
     * @return the fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return the dtCompetencia
     */
    public String getDtCompetencia() {
        return dtCompetencia;
    }

    /**
     * @param dtCompetencia the dtCompetencia to set
     */
    public void setDtCompetencia(String dtCompetencia) {
        this.dtCompetencia = dtCompetencia;
    }

    /**
     * @return the codComprador
     */
    public String getCodComprador() {
        return codComprador;
    }

    /**
     * @param codComprador the codComprador to set
     */
    public void setCodComprador(String codComprador) {
        this.codComprador = codComprador;
    }

    /**
     * @return the utilizouRateioconta
     */
    public String getUtilizouRateioconta() {
        return utilizouRateioconta;
    }

    /**
     * @param utilizouRateioconta the utilizouRateioconta to set
     */
    public void setUtilizouRateioconta(String utilizouRateioconta) {
        this.utilizouRateioconta = utilizouRateioconta;
    }

    /**
     * @return the prcRateioUtilizado
     */
    public String getPrcRateioUtilizado() {
        return prcRateioUtilizado;
    }

    /**
     * @param prcRateioUtilizado the prcRateioUtilizado to set
     */
    public void setPrcRateioUtilizado(String prcRateioUtilizado) {
        this.prcRateioUtilizado = prcRateioUtilizado;
    }

    /**
     * @return the numTrans
     */
    public String getNumTrans() {
        return numTrans;
    }

    /**
     * @param numTrans the numTrans to set
     */
    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

    @Override
    public String toString() {
        return codFilial
                + " ; " + indice
                + " ; " + recNum
                + " ; " + numTransent
                + " ; " + numTrans                
                + " ; " + codConta
                + " ; " + tipoParceiro
                + " ; " + codFornec
                + " ; " + fornecedor
                + " ; " + dtCompetencia
                + " ; " + dtEmissao
                + " ; " + dtLanc
                + " ; " + dtVenc
                + " ; " + dtPagto
                + " ; " + numNota
                + " ; " + duplic
                + " ; " + valor
                + " ; " + historico
                + " ; " + moeda
                + " ; " + tipoLanc
                + " ; " + tipoPagto
                + " ; " + formaPagto
                + " ; " + tipoServico
                + " ; " + nfServico
                + " ; " + codComprador
                + " ; " + utilizouRateioconta
                + " ; " + prcRateioUtilizado;
    }
}
