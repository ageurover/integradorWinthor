/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.oracleDb;

/**
 *
 * @author ageurover
 */
public final class StringConectDb {

    private String UsuarioDb;
    private String SenhaDb;
    private String IpServidorDb;
    private String SidServidorDb;
    private String CodConsumidorDb;
    private String CodFilial;
    private String CodFilialFatura;
    private String Licenca;
    private String CnpjMatriz;
    private Double valorMaxCupom;
    private String AjustaFrenteLoja;
    private String pastaImagens;

    /**
     * Construtor da classe
     */
    public StringConectDb() {
    }

    /**
     * Construtor da classe
     *
     * @param UsuarioDb
     * @param SenhaDb
     * @param IpServidorDb
     * @param SidServidorDb
     * @param CodConsumidor
     * @param CodFilial
     * @param CodFilialFatura
     * @param CnpjMatriz
     * @param Licenca
     * @param valorMaxCup
     * @param AjustaFrenteLoja
     * @param pastaImagens
     * 
     */
    public StringConectDb(String UsuarioDb, String SenhaDb, String IpServidorDb, String SidServidorDb, String CodConsumidor, String CodFilial,String CodFilialFatura, String CnpjMatriz, String Licenca, Double valorMaxCup, String AjustaFrenteLoja, String pastaImagens) {
        if ((!UsuarioDb.isEmpty()) && (!SenhaDb.isEmpty()) && (!IpServidorDb.isEmpty()) && (!SidServidorDb.isEmpty())) {
            setUsuarioDb(UsuarioDb);
            setSenhaDb(SenhaDb);
            setSidServidorDb(SidServidorDb);
            setIpServidorDb(IpServidorDb);
            setCodConsumidorDb(CodConsumidor);
            setCodFilial(CodFilial);
            setCodFilialFatura(CodFilialFatura);
            setCnpjMatriz(CnpjMatriz);
            setLicenca(Licenca);
            setValorMaxCupom(valorMaxCup);
            setAjustaFrenteLoja(AjustaFrenteLoja);
            setPastaImagens(pastaImagens);
        }
    }

    /**
     * Retorna a String de conexao com o banco de dados oracle 10G, 11G
     *
     * @return
     */
    public String getStringConectDbOracle() {
        String conn = "";

        if ((((!getSenhaDb().isEmpty()) || (!getIpServidorDb().isEmpty())) || (!getSidServidorDb().isEmpty())) || (!getUsuarioDb().isEmpty())) {
            conn = "jdbc:oracle:thin:" + getUsuarioDb() + "/" + getSenhaDb() + "@(DESCRIPTION =(ADDRESS =(PROTOCOL = TCP)(HOST = " + getIpServidorDb() + ")(PORT = 1521))(CONNECT_DATA = (SID = " + getSidServidorDb() + " )))";
        }
        return conn;
    }

    /**
     * @return the UsuarioDb
     */
    public String getUsuarioDb() {
        return (UsuarioDb == null ? "" : UsuarioDb);
    }

    /**
     * @param UsuarioDb the UsuarioDb to set
     */
    public void setUsuarioDb(String UsuarioDb) {
        this.UsuarioDb = UsuarioDb;
    }

    /**
     * @return the SenhaDb
     */
    public String getSenhaDb() {
        return (SenhaDb == null ? "" : SenhaDb);
    }

    /**
     * @param SenhaDb the SenhaDb to set
     */
    public void setSenhaDb(String SenhaDb) {
        this.SenhaDb = SenhaDb;
    }

    /**
     * @return the IpServidorDb
     */
    public String getIpServidorDb() {
        return (IpServidorDb == null ? "" : IpServidorDb);
    }

    /**
     * @param IpServidorDb the IpServidorDb to set
     */
    public void setIpServidorDb(String IpServidorDb) {
        this.IpServidorDb = IpServidorDb;
    }

    /**
     * @return the SidServidorDb
     */
    public String getSidServidorDb() {
        return (SidServidorDb == null ? "" : SidServidorDb);
    }

    /**
     * @param SidServidorDb the SidServidorDb to set
     */
    public void setSidServidorDb(String SidServidorDb) {
        this.SidServidorDb = SidServidorDb;
    }

    /**
     * @return the CodConsumidorDb
     */
    public String getCodConsumidorDb() {
        return CodConsumidorDb;
    }

    /**
     * @param CodConsumidorDb the CodConsumidorDb to set
     */
    public void setCodConsumidorDb(String CodConsumidorDb) {
        this.CodConsumidorDb = CodConsumidorDb;
    }

    /**
     * @return the CodFilial
     */
    public String getCodFilial() {
        return CodFilial;
    }

    /**
     * @param CodFilial the CodFilial to set
     */
    public void setCodFilial(String CodFilial) {
        this.CodFilial = CodFilial;
    }

    /**
     * @return the CodFilialFatura
     */
    public String getCodFilialFatura() {
        return CodFilialFatura;
    }

    /**
     * @param CodFilialFatura the CodFilialFatura to set
     */
    public void setCodFilialFatura(String CodFilialFatura) {
        this.CodFilialFatura = CodFilialFatura;
    }

    /**
     * @return the Licenca
     */
    public String getLicenca() {
        return Licenca;
    }

    /**
     * @param Licenca the Licenca to set
     */
    public void setLicenca(String Licenca) {
        this.Licenca = Licenca;
    }

    /**
     * @return the CnpjMatriz
     */
    public String getCnpjMatriz() {
        return CnpjMatriz;
    }

    /**
     * @param CnpjMatriz the CnpjMatriz to set
     */
    public void setCnpjMatriz(String CnpjMatriz) {
        this.CnpjMatriz = CnpjMatriz;
    }

    /**
     * @return the valorMaxCupom
     */
    public Double getValorMaxCupom() {
        return valorMaxCupom;
    }

    /**
     * @param valorMaxCupom the valorMaxCupom to set
     */
    public void setValorMaxCupom(Double valorMaxCupom) {
        this.valorMaxCupom = valorMaxCupom;
    }

    /**
     * @return the AjustaFrenteLoja
     */
    public String getAjustaFrenteLoja() {
        return AjustaFrenteLoja;
    }

    /**
     * @param AjustaFrenteLoja the AjustaFrenteLoja to set
     */
    public void setAjustaFrenteLoja(String AjustaFrenteLoja) {
        this.AjustaFrenteLoja = AjustaFrenteLoja;
    }

    /**
     * @return the pastaImagens
     */
    public String getPastaImagens() {
        return pastaImagens;
    }

    /**
     * @param pastaImagens the pastaImagens to set
     */
    public void setPastaImagens(String pastaImagens) {
        this.pastaImagens = pastaImagens;
    }

}
