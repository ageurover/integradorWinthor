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
public class Cliente implements Serializable {

    private Integer id;
    private String cnpjCpf;
    private String inscricaoEstadual;
    private String nomeFantasia;
    private String nomeCliente;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String codIbge;
    private String fone1;
    private String fone2;
    private String fone3;
    private String fax;
    private String celular;
    private String nomeContato;
    private String email;
    private String observacao;
    private String numeroRG;
    private String sexo;

    public Cliente() {
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the cnpjCpf
     */
    public String getCnpjCpf() {
        return cnpjCpf;
    }

    /**
     * @param cnpjCpf the cnpjCpf to set
     */
    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    /**
     * @return the inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @param inscricaoEstadual the inscricaoEstadual to set
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * @return the nomeFantasia
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     * @param nomeFantasia the nomeFantasia to set
     */
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    /**
     * @return the nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * @param nomeCliente the nomeCliente to set
     */
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the complemento
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @param complemento the complemento to set
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return the bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the codIbge
     */
    public String getCodIbge() {
        return codIbge;
    }

    /**
     * @param codIbge the codIbge to set
     */
    public void setCodIbge(String codIbge) {
        this.codIbge = codIbge;
    }

    /**
     * @return the fone1
     */
    public String getFone1() {
        return fone1;
    }

    /**
     * @param fone1 the fone1 to set
     */
    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    /**
     * @return the fone2
     */
    public String getFone2() {
        return fone2;
    }

    /**
     * @param fone2 the fone2 to set
     */
    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    /**
     * @return the fone3
     */
    public String getFone3() {
        return fone3;
    }

    /**
     * @param fone3 the fone3 to set
     */
    public void setFone3(String fone3) {
        this.fone3 = fone3;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * @return the nomeContato
     */
    public String getNomeContato() {
        return nomeContato;
    }

    /**
     * @param nomeContato the nomeContato to set
     */
    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the numeroRG
     */
    public String getNumeroRG() {
        return numeroRG;
    }

    /**
     * @param numeroRG the numeroRG to set
     */
    public void setNumeroRG(String numeroRG) {
        this.numeroRG = numeroRG;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


}
