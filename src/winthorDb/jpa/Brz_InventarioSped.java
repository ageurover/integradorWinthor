/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.jpa;

/**
 *
 * @author ageurover
 */
public class Brz_InventarioSped {

    @Override
    public String toString() {
        return "Brz_InventarioSped{" + "anoMes=" + anoMes + ", codfilial=" + codfilial + ", codprod=" + codprod + ", unidade=" + unidade + ", quantidade=" + quantidade + ", valorUnitario=" + valorUnitario + ", valorTotal=" + valorTotal + '}';
    }
     private long anoMes;
     private String codfilial;
     private long codprod;
     private String unidade;
     private double quantidade;
     private double valorUnitario;
     private double valorTotal;

    /**
     * @return the anoMes
     */
    public long getAnoMes() {
        return anoMes;
    }

    /**
     * @param anoMes the ano to set
     */
    public void setAnoMes(long anoMes) {
        this.anoMes = anoMes;
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
     * @return the codprod
     */
    public long getCodprod() {
        return codprod;
    }

    /**
     * @param codprod the codprod to set
     */
    public void setCodprod(long codprod) {
        this.codprod = codprod;
    }

    /**
     * @return the unidade
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * @param unidade the unidade to set
     */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /**
     * @return the quantidade
     */
    public double getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the valorUnitario
     */
    public double getValorUnitario() {
        return valorUnitario;
    }

    /**
     * @param valorUnitario the valorUnitario to set
     */
    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    /**
     * @return the valorTotal
     */
    public double getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
