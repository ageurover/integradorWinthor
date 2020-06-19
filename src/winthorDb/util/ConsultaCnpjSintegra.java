/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.util.List;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.Cliente;

/**
 *
 * @author ageurover
 */
public class ConsultaCnpjSintegra {

    /**
     *
     * @param cnpj
     * @return objeto cliente preenchido
     * @throws Exception
     */
    public Cliente sintegraCNPJ_RO(String cnpj) throws Exception {
        Cliente ret = new Cliente();
        String pgTxt = "";
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            CookieManager cookieManager = webClient.getCookieManager();
            cookieManager.setCookiesEnabled(true);
            webClient.setCookieManager(cookieManager);
            
            // Get the first page
            final HtmlPage page1 = webClient.getPage("https://portalcontribuinte.sefin.ro.gov.br/Publico/parametropublica.jsp");

            // Get the form that we are dealing with and within that form, 
            // find the submit button and the field that we want to change.
            final List<HtmlForm> form = page1.getForms();
            for (HtmlForm form1 : form) {
                System.out.println(form1.getChildNodes().toString());
                System.out.println(form1.getChildElements().toString());
            }
            final HtmlSubmitInput button = form.get(0).getInputByName("B1");
            final HtmlTextInput NuDevedor = form.get(0).getInputByName("NuDevedor");
            final HtmlSelect tipoDevedor = form.get(0).getSelectByName("TipoDevedor");

            // Change the value of the text field
            tipoDevedor.setDefaultValue("3");
            NuDevedor.setValueAttribute(cnpj);

            // Now submit the form by clicking the button and get back the second page.
            final HtmlPage page2 = button.click();
            pgTxt = page2.asText();
            if (!pgTxt.isEmpty()) {
                if (!Formato.buscaTexto(pgTxt, "Situação do Contribuinte:", "Data Inicio Atividade:").equalsIgnoreCase("ATIVO")) {
                    MessageDialog.error("CNPJ inativo na sefin Rondonia!");
                    trataErro.addListaErros(pgTxt);
                    trataErro.mostraListaErros();
                    ret = null;
                } else {
                    ret.setInscricaoEstadual(Formato.buscaTexto(pgTxt, "Inscrição Estadual:", "Nire:"));
                    ret.setNomeCliente(Formato.buscaTexto(pgTxt, "Razão Social:", "Nome Fantasia:"));
                    ret.setNomeFantasia(Formato.buscaTexto(pgTxt, "Nome Fantasia:", "Utilização do Estabelecimento:"));
                    ret.setEmail(Formato.buscaTexto(pgTxt, "E-mail:", "INFORMAÇÕES COMPLEMENTARES"));

                    ret.setEndereco(Formato.buscaTexto(pgTxt, "Endereço:", "Complemento:"));
                    ret.setComplemento(Formato.buscaTexto(pgTxt, "Complemento:", "Bairro:"));
                    ret.setBairro(Formato.buscaTexto(pgTxt, "Bairro:", "Número:"));
                    ret.setNumero(Formato.buscaTexto(pgTxt, "Número:", "Município:"));
                    ret.setCep(Formato.buscaTexto(pgTxt, "CEP:", "UF:"));
                    ret.setEstado(Formato.buscaTexto(pgTxt, "UF:", "ENDEREÇO DE CORRESPONDÊNCIA"));

                }
            }
        }
        return ret;
    }

    /**
     *
     * @param cnpj
     * @return objeto cliente preenchido
     * @throws Exception
     */
    public Cliente sintegraCNPJ_AC(String cnpj) throws Exception {
        Cliente ret = new Cliente();
        String pgTxt = "";
        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45)) {

            // Get the first page
            final HtmlPage page1 = webClient.getPage("http://sefaznet.ac.gov.br/sefazonline/servlet/hpfsincon");

            final List<DomElement> elem = page1.getElementsByIdAndOrName("captchaImage");

            for (DomElement elem1 : elem.get(0).getChildElements()) {
                System.out.println(elem1.getChildNodes().get(0).asText());
            }
            MessageDialog.getCaptcha(Formato.buscaTexto(pgTxt, "img src=", " alt="));

            // Get the form that we are dealing with and within that form, 
            // find the submit button and the field that we want to change.
            final List<HtmlForm> form = page1.getForms();
            for (HtmlForm form1 : form) {
                System.out.println(form1.getChildNodes().toString());
                System.out.println(form1.getChildElements().toString());
            }
            final HtmlSubmitInput button = form.get(0).getInputByName("B1");
            final HtmlTextInput NuDevedor = form.get(0).getInputByName("NuDevedor");
            final HtmlSelect tipoDevedor = form.get(0).getSelectByName("TipoDevedor");

            // Change the value of the text field
            tipoDevedor.setDefaultValue("3");
            NuDevedor.setValueAttribute(cnpj);

            // Now submit the form by clicking the button and get back the second page.
            final HtmlPage page2 = button.click();
            pgTxt = page2.asText();
            if (!pgTxt.isEmpty()) {
                if (!Formato.buscaTexto(pgTxt, "Situação do Contribuinte:", "Data Inicio Atividade:").equalsIgnoreCase("ATIVO")) {
                    MessageDialog.error("CNPJ inativo na sefin Rondonia!");
                    trataErro.addListaErros(pgTxt);
                    trataErro.mostraListaErros();
                    ret = null;
                } else {
                    ret.setInscricaoEstadual(Formato.buscaTexto(pgTxt, "Inscrição Estadual:", "Nire:"));
                    ret.setNomeCliente(Formato.buscaTexto(pgTxt, "Razão Social:", "Nome Fantasia:"));
                    ret.setNomeFantasia(Formato.buscaTexto(pgTxt, "Nome Fantasia:", "Utilização do Estabelecimento:"));
                    ret.setEmail(Formato.buscaTexto(pgTxt, "E-mail:", "INFORMAÇÕES COMPLEMENTARES"));

                    ret.setEndereco(Formato.buscaTexto(pgTxt, "Endereço:", "Complemento:"));
                    ret.setComplemento(Formato.buscaTexto(pgTxt, "Complemento:", "Bairro:"));
                    ret.setBairro(Formato.buscaTexto(pgTxt, "Bairro:", "Número:"));
                    ret.setNumero(Formato.buscaTexto(pgTxt, "Número:", "Município:"));
                    ret.setCep(Formato.buscaTexto(pgTxt, "CEP:", "UF:"));
                    ret.setEstado(Formato.buscaTexto(pgTxt, "UF:", "ENDEREÇO DE CORRESPONDÊNCIA"));

                }
            }
        }
        return ret;
    }
}
