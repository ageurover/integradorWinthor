/*
 * Formato.java
 *
 * Created on 23 de Janeiro de 2006, 09:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package integradorbrasil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Administrador
 */
public class Formato {

    /*
     * Uso geral
     */
    public static DateFormat dataFormatBr = new SimpleDateFormat("EEEE, d' de 'MMMM' de 'yyyy"); 
    public static DateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static DateFormat dataFormatBd = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat dataFormatSg = new SimpleDateFormat("dd/MM/yy");
    public static DateFormat dataFormatCp = new SimpleDateFormat("yyyyMMdd");
    public static DateFormat dataFormatEcf = new SimpleDateFormat("ddMMyy");
    public static DateFormat TimeFormatEcf = new SimpleDateFormat("hhmm");
    public static NumberFormat currencyFormat = NumberFormat.getInstance();
    public static NumberFormat currencyFormatDB = NumberFormat.getNumberInstance();
    public static NumberFormat quatroFormat = NumberFormat.getInstance();
    public static NumberFormat seisFormat = NumberFormat.getInstance();
    /*
     * Formatters para uso no JFormattedTextField
     */
    public static DateFormatter dataFormatter = new DateFormatter(dataFormat);
    public static DateFormatter dataFormatterBd = new DateFormatter(dataFormatBd);
    public static MaskFormatter dataMaskFormatter;
    public static MaskFormatter cpfFormatter;
    public static MaskFormatter cnpjFormatter;
    public static NumberFormatter currencyFormatter;
    public static NumberFormatter quatroFormatter;
    public static NumberFormatter seisFormatter;
    public static NumberFormatter currencyFormatterDB;
    /*
     * Listagem de UF
     */
    public static String[] listaUf = {"AC", "AL", "AM", "AP", "BA", "CE",
        "DF", "ES", "GO", "MA", "MG", "MS",
        "MT", "PA", "PB", "PE", "PI", "PR",
        "RJ", "RN", "RO", "RS", "SC", "SE", "SP", "TO"
    };
    /*
     * Model de comboBox de listagem de UF
     */
    public static DefaultComboBoxModel cmbUfModel = new javax.swing.DefaultComboBoxModel(listaUf);

    /**
     * Creates a new instance of Formato
     */
    public Formato() {
        try {
            cpfFormatter = new MaskFormatter("###.###.###-##");
            cpfFormatter.setValueContainsLiteralCharacters(false);
            cpfFormatter.setPlaceholderCharacter('_');

            cnpjFormatter = new MaskFormatter("##.###.###/####-##");
            cnpjFormatter.setValueContainsLiteralCharacters(false);
            cnpjFormatter.setPlaceholderCharacter('_');

            dataMaskFormatter = new MaskFormatter("##/##/####");
            dataMaskFormatter.setValueContainsLiteralCharacters(false);
            dataMaskFormatter.setPlaceholderCharacter('_');

            currencyFormat.setMaximumFractionDigits(2);
            currencyFormat.setMinimumFractionDigits(2);
            currencyFormatter = new NumberFormatter(currencyFormat);

            currencyFormatDB.setMaximumFractionDigits(2);
            currencyFormatDB.setMinimumFractionDigits(2);
            currencyFormatterDB = new NumberFormatter(currencyFormatDB);

            quatroFormat.setMaximumFractionDigits(4);
            quatroFormat.setMinimumFractionDigits(4);
            quatroFormatter = new NumberFormatter(quatroFormat);

            seisFormat.setMaximumFractionDigits(6);
            seisFormat.setMinimumFractionDigits(6);
            seisFormatter = new NumberFormatter(seisFormat);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * Formata a data passada no parametro (segunda-feira, 01 de Maio de 2012) 
     * @param date data a ser convertida
     * @return EEEE, d' de 'MMMM' de 'yyyy
     */
    public static String dateBrToStr(Date date) {
        if (date != null) {
            return dataFormatBr.format(date);
        } else {
            return "";
        }
    }

    /*
     * SAIDA: YYYY-MM-DD para o banco de dados MySql
     */
    /**
     *
     * @param date
     * @return YYYY-MM-DD
     */
    public static String dateBdToStr(Date date) {
        if (date != null) {
            return dataFormatBd.format(date);
        } else {
            return "";
        }
    }

    /*
     * SAIDA: DD/MM/YYYY
     */
    /**
     *
     * @param date
     * @return DD/MM/YYYY
     */
    public static String dateToStr(Date date) {
        if (date != null) {
            return dataFormat.format(date);
        } else {
            return "";
        }
    }

    /**
     *
     * @param date
     * @return DD/MM/YY
     */
    public static String dateToStrSg(Date date) {
        if (date != null) {
            return dataFormatSg.format(date);
        } else {
            return "";
        }
    }

    /**
     *
     * @param date
     * @return DDMMYY
     */
    public static String dateToStrEcf(Date date) {
        if (date != null) {
            return dataFormatEcf.format(date);
        } else {
            return "";
        }
    }

    /*
     * SAIDA: YYYY/MM/DD
     */
    /**
     *
     * @param date
     * @return YYYY/MM/DD
     */
    public static String dateToStrCp(Date date) {
        if (date != null) {
            return dataFormatCp.format(date);
        } else {
            return "";
        }
    }


    /*
     * SAIDA: DD/MM/YYYY
     */
    /**
     *
     * @param data
     * @return DD/MM/YYYY
     */
    public static String dateCalendarToStr(Calendar data) {
        DateFormat formato_zona = DateFormat.getDateInstance();
        if (data != null) {
            return formato_zona.format(data.getTime());
        } else {
            return "";
        }
    }

    /*
     * SAIDA: DD/MM/YYYY HH:MM:SS
     */
    /**
     *
     * @param data
     * @return DD/MM/YYYY HH:MM:SS
     */
    public static String dateTimeCalendarToStr(Calendar data) {
        DateFormat formato_zona = DateFormat.getDateInstance(DateFormat.LONG);
        if (data != null) {
            return formato_zona.format(data.getTime());
        } else {
            return "";
        }
    }

    /*
     * SAIDA: DD/MM/YYYY HH:MM:SS
     */
    /**
     *
     * @param data
     * @return DD/MM/YYYY HH:MM:SS
     */
    public static String TimeCalendarToStr(Calendar data) {
        DateFormat formato_zona = DateFormat.getTimeInstance(DateFormat.SHORT);
        if (data != null) {
            return formato_zona.format(data.getTime());
        } else {
            return "";
        }
    }

    /*
     * ENTRADA: DD/MM/YYYY
     */
    /**
     *
     * @param date
     * @return DD/MM/YYYY
     */
    public static Date strToDate(String date) {
        Date d;
        try {
            if ((date.equals("")) || (date == null) || (date.equals("__/__/____"))) {
                d = null;
            } else {
                d = dataFormat.parse(date);
            }

        } catch (Exception e) {
            return null;
        }
        return d;

    }

    /**
     *
     * @param ndouble
     * @return
     */
    public static String doubleToCurrStr(double ndouble) {

        try {
            return currencyFormat.format(ndouble);
            //return bdecimal.toString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param ndouble
     * @return
     */
    public static double doubleToCurrStr(String ndouble) {

        try {
            return currStrToDecimal(ndouble).doubleValue();

        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     *
     * @param ndouble
     * @return
     */
    public static String doubleToCurrStr(double ndouble, int casas) {
        String retorno = "";
        try {
            switch (casas) {
                case 2:
                    retorno = currencyFormat.format(ndouble);
                    break;
                case 4:
                    retorno = quatroFormat.format(ndouble);
                    break;
                case 6:
                    retorno = seisFormat.format(ndouble);
                    break;

                default:
                    retorno = currencyFormat.format(ndouble);
                    break;
            }
            return retorno;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param bdecimal
     * @return
     */
    public static String decimalToCurrStr(BigDecimal bdecimal) {

        try {
            return currencyFormat.format(bdecimal.doubleValue());
            //return bdecimal.toString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param bdecimal
     * @param casas
     * @return
     */
    public static String decimalToCurrStr(BigDecimal bdecimal, int casas) {
        String retorno = "";
        try {
            switch (casas) {
                case 2:
                    retorno = currencyFormat.format(bdecimal.doubleValue());
                    break;
                case 4:
                    retorno = quatroFormat.format(bdecimal.doubleValue());
                    break;
                case 6:
                    retorno = seisFormat.format(bdecimal.doubleValue());
                    break;

                default:
                    retorno = currencyFormat.format(bdecimal.doubleValue());
                    break;
            }

            return retorno;

        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param bdecimal
     * @return
     */
    public static String decimalToStrBematech(BigDecimal bdecimal) {
        String sRetorno = "0";
        try {
            sRetorno = currencyFormat.format(bdecimal.doubleValue()).toString();
            sRetorno = sRetorno.replace(".", "");
            return sRetorno;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param bdecimal
     * @return
     */
    public static String decimalToStrDb(BigDecimal bdecimal) {
        String sRetorno = "0";
        try {
            sRetorno = currencyFormat.format(bdecimal.doubleValue()).toString();
            sRetorno = sRetorno.replace(".", "");
            sRetorno = sRetorno.replace(",", ".");
            return sRetorno;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param number
     * @return
     */
    public static String strNumberToDb(String number) {
        String sRetorno = "0";
        try {
            sRetorno = number.replace(".", "");
            sRetorno = sRetorno.replace(",", ".");
            return sRetorno;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param strDado
     * @return
     */
    public static String strTiraIncompativeis(String strDado) {
        String sRetorno = "";
        try {
            sRetorno = strDado.replace("'", " ");
            sRetorno = sRetorno.replace(",", " ");
            sRetorno = sRetorno.replace(";", " ");
            sRetorno = sRetorno.replace("%", " ");
            sRetorno = sRetorno.replace("*", " ");
            sRetorno = sRetorno.replace("/", " ");
            sRetorno = sRetorno.replace("\"", " ");
            sRetorno = sRetorno.replace("&", " ");
            sRetorno = sRetorno.replace("(", " ");
            sRetorno = sRetorno.replace(")", " ");
            sRetorno = sRetorno.replace("-", " ");
            sRetorno = sRetorno.replace("+", " ");
            sRetorno = sRetorno.replace("?", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("_", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("|", " ");
            sRetorno = sRetorno.replace(":", " ");
            sRetorno = sRetorno.replace(",", " ");
            sRetorno = sRetorno.replace(".", " ");
            sRetorno = sRetorno.replace(">", " ");
            sRetorno = sRetorno.replace("<", " ");
            sRetorno = sRetorno.replace("[", " ");
            sRetorno = sRetorno.replace("]", " ");
            sRetorno = sRetorno.replace("{", " ");
            sRetorno = sRetorno.replace("}", " ");
            sRetorno = sRetorno.replace("^", " ");
            sRetorno = sRetorno.replace("~", " ");
            return sRetorno;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     *
     * @param strDado
     * @return
     */
    public static String strTiraIncompativeisBd(String strDado) {
        String sRetorno = "";
        try {
            sRetorno = strDado.replace("'", " ");
            sRetorno = sRetorno.replace(",", " ");
            //sRetorno = sRetorno.replace(";", " ");
            sRetorno = sRetorno.replace("%", " ");
            //sRetorno = sRetorno.replace("*", " ");
            //sRetorno = sRetorno.replace("/", " ");
            //sRetorno = sRetorno.replace("\"", " ");
            sRetorno = sRetorno.replace("&", " ");
            sRetorno = sRetorno.replace("(", " ");
            sRetorno = sRetorno.replace(")", " ");
            //sRetorno = sRetorno.replace("-", " ");
            //sRetorno = sRetorno.replace("+", " ");
            //sRetorno = sRetorno.replace("?", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("�", " ");
            sRetorno = sRetorno.replace("_", " ");
            sRetorno = sRetorno.replace("�", " ");
            //sRetorno = sRetorno.replace("|", " ");
            //sRetorno = sRetorno.replace(":", " ");
            //sRetorno = sRetorno.replace(",", " ");
            //sRetorno = sRetorno.replace(".", " ");
            //sRetorno = sRetorno.replace(">", " ");
            //sRetorno = sRetorno.replace("<", " ");
            //sRetorno = sRetorno.replace("[", " ");
            //sRetorno = sRetorno.replace("]", " ");
            //sRetorno = sRetorno.replace("{", " ");
            //sRetorno = sRetorno.replace("}", " ");
            //sRetorno = sRetorno.replace("^", " ");
            //sRetorno = sRetorno.replace("~", " ");
            return sRetorno;
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * @param strDado @return
     */
    public static String strTiraAcentos(String strDado) {
        String sRetorno = "";
        try {
            sRetorno = strDado.replace("�", "C");
            sRetorno = sRetorno.replace("�", "c");
            sRetorno = sRetorno.replace("�", "A");
            sRetorno = sRetorno.replace("�", "a");
            sRetorno = sRetorno.replace("�", "O");
            sRetorno = sRetorno.replace("�", "o");
            sRetorno = sRetorno.replace("�", "E");
            sRetorno = sRetorno.replace("�", "e");
            return sRetorno;
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * @param strDado @return
     */
    public static String removerAcentos(String acentuada) {
        CharSequence cs = new StringBuilder(acentuada);
        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     *
     * @param bdecimal
     * @return
     */
    public static String decimalToStrBoleto(BigDecimal bdecimal) {
        String sRetorno = "0";
        try {
            sRetorno = currencyFormat.format(bdecimal.doubleValue()).toString();
            sRetorno = sRetorno.replace(".", "");
            sRetorno = sRetorno.replace(",", ".");
            return sRetorno;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param curr
     * @return
     */
    public static BigDecimal currStrToDecimal(String curr) {
        BigDecimal d = null;

        try {
            if ((curr.equals("0")) || (curr.equals("0,00")) || (curr == null) || (curr.equals("") || (curr.isEmpty()))) {
                d = new BigDecimal("0.00");
            } else {
                curr = curr.replace(".", "");
                d = new BigDecimal(curr.replace(",", "."));
                //d = new BigDecimal(curr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return d;
    }

    /**
     *
     * @param curr
     * @return
     */
    public static BigInteger currStrToInt(String curr) {
        BigDecimal d = null;

        try {
            if ((curr.equals("0")) || (curr.equals("0,00")) || (curr == null) || (curr.equals("") || (curr.isEmpty()))) {
                d = new BigDecimal("0.00");
            } else {
                curr = curr.replace(".", "");
                d = new BigDecimal(curr.replace(",", "."));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return d.toBigInteger();
    }

    /**
     *
     * @param intStr
     * @return
     */
    public static int strToInt(String intStr) {
        int r = 0;
        try {
            String newstr = intStr;
            newstr = intStr.replaceAll("[,][0-9]{2}", "");
            newstr = intStr.replaceAll("[.][0-9]{2}", "");
            intStr = newstr;
            if (intStr.equals("") || (intStr.equals("0") || (intStr == null) || (intStr.isEmpty()))) {
                r = 0;
            } else {
                r = Integer.parseInt(intStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     *
     * @param integer
     * @return
     */
    public static String intToStr(int integer) {
        String r = "0";
        try {
            r = String.valueOf(integer);
        } catch (Exception e) {
            return "0";
        }
        return r;
    }

    /**
     *
     * @param longStr
     * @return
     */
    public static Long strToLong(String longStr) {
        Long l = 0L;
        try {
            l = Long.parseLong(longStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     *
     * @param l
     * @return
     */
    public static String longToStr(Long l) {
        return String.valueOf(l);
    }

    /**
     *
     * @param edit
     * @return
     */
    public static BigDecimal getBigDecimal(JFormattedTextField edit) {
        if (edit.getValue() instanceof Float) {
            return BigDecimal.valueOf((Float) edit.getValue());
        }

        if (edit.getValue() instanceof Double) {
            return BigDecimal.valueOf((Double) edit.getValue());
        }

        if (edit.getValue() instanceof BigDecimal) {
            return (BigDecimal) edit.getValue();
        }

        if (edit.getValue() instanceof Long) {

            return BigDecimal.valueOf((Long) edit.getValue());
        }


        if (edit.getValue() instanceof Byte) {
            return new BigDecimal((Byte) edit.getValue());
        }

        if (edit.getValue() instanceof Short) {
            return new BigDecimal((Short) edit.getValue());
        }

        if (edit.getValue() instanceof Integer) {
            return new BigDecimal((Integer) edit.getValue());
        }

        return new BigDecimal(0.0);
    }

    /**
     *
     * @param strDados
     * @param tamanho
     * @return
     */
    public static String zerosEsquerda(String strDados, int tamanho) {
        String newDados = "";
        if (tamanho < strDados.length()) {
            return strDados.substring(0, tamanho);
        }
        for (int i = 0; i < (tamanho - strDados.length()); i++) {
            newDados += "0";
        }
        newDados += strDados;
        return newDados;
    }

    /**
     *
     * @param strDados
     * @param tamanho
     * @return
     */
    public static String espacosEsquerda(String strDados, int tamanho) {
        String newDados = "";
        for (int i = 0; i < (tamanho - strDados.length()); i++) {
            newDados += " ";
        }
        newDados += strDados;
        return newDados;
    }

    /**
     *
     * @param strDados
     * @param tamanho
     * @return
     */
    public static String espacosDireita(String strDados, int tamanho) {
        String newDados = ""; //strDados;
        int tam = strDados.length();
        if (strDados != null) {
            newDados = strDados;
        }

        for (int i = 0; i < (tamanho - tam); i++) {
            newDados += " ";
        }
        //newDados+=strDados;
        return newDados;
    }

    /**
     *
     * @param strNova
     * @param qtd
     * @return
     */
    public static String replicate(String strNova, int qtd) {
        String newDados = "";

        for (int i = 0; i <= qtd; i++) {
            newDados += strNova;
        }

        return newDados;
    }

    /**
     *
     * @param strDados
     * @param tamanho
     * @return
     */
    public static String strTamanhoExato(String strDados, int tamanho) {
        String newDados = "";
        if (strDados == null) {
            newDados = espacosDireita(" ", tamanho);
        }
        if (strDados.length() >= tamanho) {
            newDados = strDados.substring(0, tamanho);
        } else {
            newDados = espacosDireita(strDados, tamanho);
        }
        return newDados;
    }

    /**
     *
     * @param strDados
     * @param tamanho
     * @return
     */
    public static String strTamanhoExatoEs(String strDados, int tamanho) {
        String newDados = "";
        if (strDados == null) {
            newDados = espacosEsquerda(" ", tamanho);
        }

        if (strDados.length() >= tamanho) {
            newDados = strDados.substring(0, tamanho);
        } else {
            newDados = espacosEsquerda(strDados, tamanho);
        }
        return newDados;
    }

    /**
     *
     * @param strDados
     * @param tamanho
     * @return
     */
    public static String strCentralizado(String strDados, int tamanho) {
        String newDados = "";
        if (strDados == null) {
            newDados = espacosDireita(" ", tamanho);
        }

        if (strDados.length() >= tamanho) {
            newDados = strDados.substring(0, tamanho);
        } else {
            int newTamanho = (int) ((tamanho - strDados.length()) / 2);
            newDados = replicate(" ", newTamanho) + strDados;
        }
        return newDados;
    }

    /**
     *
     * @param prazo
     * @param data
     * @return
     */
    public static Calendar calculaVencimento(int prazo, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.DAY_OF_MONTH, prazo);
        return hoje;
    }

    /**
     *
     * @param mes
     * @param data
     * @return
     */
    public static Calendar calculaVencimentoFixo(int mes, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.MONTH, mes);
        return hoje;
    }

    /**
     *
     * @param mes mes para o vencimento
     * @param data inicial para contagem
     * @return
     */
    public static Date calculaDataFixa(int mes, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.MONTH, mes);
        return hoje.getTime();
    }

    /**
     *
     * @param prazo prazo para o vencimento
     * @param data inicial para contagem
     * @return
     */
    public static Date calculaData(int prazo, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.DAY_OF_MONTH, prazo);
        return hoje.getTime();
    }

    /**
     *
     * @param data referencia para buscar o ultimo dia do mes
     * @return
     */
    public static int ultimoDiaMes(Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.getActualMaximum(Calendar.DAY_OF_MONTH);
        return hoje.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * @param data referencia para buscar o primeiro dia do mes
     * @return
     */
    public static int primeiroDiaMes(Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.getActualMinimum(Calendar.DAY_OF_MONTH);
        return hoje.getActualMinimum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calcula a diferen�a de duas datas em dias <br> <b>Importante:</b> Quando
     * realiza a diferen�a em dias entre duas datas, este m�todo n�o considera as
     * horas restantes.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de meses existentes entre a dataInicial e dataFinal. Se a data for parte do mes retorna 1
     */
    public static int diferencaEmMeses(Date dataInicial, Date dataFinal) {
        int result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado � diferen�a entre as datas em dias
        result = (int) (diferencaEmDias/30);

        if (result ==0){
            result = 1;
        }
        
        return result;

    }

    /**
     * Calcula a diferen�a de duas datas em dias <br> <b>Importante:</b> Quando
     * realiza a diferen�a em dias entre duas datas, este m�todo considera as
     * horas restantes e as converte em fra��o de dias.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de dias existentes entre a dataInicial e dataFinal.
     */
    public static double diferencaEmDias(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado � diferen�a entre as datas em dias
        long horasRestantes = (diferenca / 1000) / 60 / 60 % 24; //calcula as horas restantes
        result = diferencaEmDias + (horasRestantes / 24d); //transforma as horas restantes em fra��o de dias

        return result;
    }

    /**
     * Calcula a diferen�a de duas datas em dias <br> <b>Importante:</b> Quando
     * realiza a diferen�a em dias entre duas datas, este m�todo nao considera as
     * horas restantes e as converte em fra��o de dias.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de dias existentes entre a dataInicial e dataFinal.
     */
    public static int diferencaEmDiasInteiros(Date dataInicial, Date dataFinal) {
        int result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado � diferen�a entre as datas em dias
        result = (int) diferencaEmDias;

        return result;
    }

    /**
     * Calcula a diferen�a de duas datas em horas <br> <b>Importante:</b> Quando
     * realiza a diferen�a em horas entre duas datas, este m�todo considera os
     * minutos restantes e os converte em fra��o de horas.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de horas existentes entre a dataInicial e dataFinal.
     */
    public static double diferencaEmHoras(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        long diferencaEmHoras = (diferenca / 1000) / 60 / 60;
        long minutosRestantes = (diferenca / 1000) / 60 % 60;
        double horasRestantes = minutosRestantes / 60d;
        result = diferencaEmHoras + (horasRestantes);

        return result;
    }

    /**
     * Calcula a diferen�a de duas datas em minutos <br> <b>Importante:</b>
     * Quando realiza a diferen�a em minutos entre duas datas, este m�todo
     * considera os segundos restantes e os converte em fra��o de minutos.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de minutos existentes entre a dataInicial e dataFinal.
     */
    public static double diferencaEmMinutos(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmMinutos = (diferenca / 1000) / 60; //resultado � diferen�a entre as datas em minutos
        long segundosRestantes = (diferenca / 1000) % 60; //calcula os segundos restantes
        result = diferencaEmMinutos + (segundosRestantes / 60d); //transforma os segundos restantes em minutos

        return result;
    }

    /**
     * 
     * @param toIndex texto a ser procurado em args
     * @param args a cadeixa de
     * @return 
     * pesquisa @return retorna aposi��o do toindex dentro de args
     */
    public static int returnIndex(String toIndex, String... args) {
        for (int i = 0; i < args.length; i++) {
            if (toIndex.equals(args[i])) {
                return i;
            }
        }
        return -1;
    }
    
   public static String numeroPorExtenso(double valor){
       Extenso nrext = new Extenso();
       nrext.setNumber(valor);
       return nrext.toString();       
   }
}
