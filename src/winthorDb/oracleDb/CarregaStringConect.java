/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.oracleDb;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import winthorDb.Main;
import winthorDb.error.trataErro;

/**
 *
 * @author ageurover
 */
public class CarregaStringConect {

    public static StringConectDb conectDb;

    /**
     * Cria o arquivo com o XML de forma correta para o class path do controle
     * de versao
     */
    public static void setStringConectDb() {
        if (conectDb == null) {
            conectDb = new StringConectDb();
        }

        XStream xstream = new XStream();
        String conectDbToXml = "";
        FileWriter write = null;

        try {
            conectDbToXml = xstream.toXML(conectDb);
            write = new FileWriter(new File(Main.xmlConectDb));
            write.append(conectDbToXml);
            write.flush();
            write = null;
            xstream = null;

        } catch (IOException ex) {
            trataErro.trataException(ex, "Erro ao gravar XML da conectDB");
        }
    }

    /**
     * Carrega o XML para a classe StringConectDb.
     *
     */
    public static void getConectDbXml() {
        if (conectDb == null) {
            conectDb = new StringConectDb();
        }

        XStream xstream = new XStream();
        FileReader reader = null;
        File xmlFile = null;
        try {
            xmlFile = new File(Main.xmlConectDb);
            if (xmlFile.exists()) {
                reader = new FileReader(xmlFile);
                conectDb = (StringConectDb) xstream.fromXML(reader);
            } else {
                setStringConectDb();
            }
        } catch (FileNotFoundException ex) {
            trataErro.trataException(ex, "Erro ao carregar o XML para conectDB");
        }

    }

    public static String getNomeUsuario() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getUsuarioDb();

    }

    public static String getSenhaUsuario() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getSenhaDb();

    }

    public static String getSidServidor() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getSidServidorDb();

    }

    public static String getIpServidor() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getIpServidorDb();

    }

    public static String getPortaServidor() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getPortaSerividor();

    }

    public static String getStringConectDbOracle() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getStringConectDbOracle();

    }

    public static String getCodConsumidor() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getCodConsumidorDb();

    }

    public static String getCodFilial() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getCodFilial();

    }

    public static String getCodFilialFatura() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getCodFilialFatura();

    }

    public static String getLicenca() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getLicenca();

    }

    public static String getCnpjMatriz() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getCnpjMatriz();

    }

    public static String getQtdeMaxUsuarios() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getQtdeMaxUsuario();

    }

    public static String getDataExpiracao() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getDataExpiracao();

    }

    public static String getAjustaFrenteLoja() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getAjustaFrenteLoja();

    }

    public static String getPastaImagens() {
        if (conectDb == null) {
            getConectDbXml();
        }
        return conectDb.getPastaImagens();

    }
}
