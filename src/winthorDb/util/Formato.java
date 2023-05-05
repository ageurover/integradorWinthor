/*
 * Formato.java
 *
 * Created on 23 de Janeiro de 2006, 09:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package winthorDb.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import winthorDb.error.trataErro;

/**
 *
 * @author Administrador
 */
@SuppressWarnings({"null", "CallToPrintStackTrace"})
public class Formato {

    /*
     * Uso geral
     */
    public static final DateFormat dataFormatBr = new SimpleDateFormat("EEEE, d' de 'MMMM' de 'yyyy");
    public static final DateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat dataFormatYm = new SimpleDateFormat("yyyyMM");
    public static final DateFormat dataFormatBd = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat dataFormatSg = new SimpleDateFormat("dd/MM/yy");
    public static final DateFormat dataFormatCp = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat dataFormatEcf = new SimpleDateFormat("ddMMyy");
    public static final DateFormat TimeFormatEcf = new SimpleDateFormat("hhmm");
    public static final DateFormat TimeFormatNfe = new SimpleDateFormat("hh:mm:ss");
    public static final NumberFormat currencyFormat = NumberFormat.getInstance();
    public static final NumberFormat currencyFormatDB = NumberFormat.getNumberInstance();
    public static final NumberFormat quatroFormat = NumberFormat.getInstance();
    public static final NumberFormat seisFormat = NumberFormat.getInstance();
    /*
     * Formatters para uso no JFormattedTextField
     */
    public static final DateFormatter dataFormatter = new DateFormatter(dataFormat);
    public static final DateFormatter dataFormatterBd = new DateFormatter(dataFormatBd);
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
    public static final String[] listaUf = {"AC", "AL", "AM", "AP", "BA", "CE",
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

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna a data e hora atual no formato dd/MM/yyyy HH:mm:ss
     *
     * @return retorna a data no formato dd/MM/yyyy HH:mm:ss
     */
    public static String dateTimeNowToStr() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        calendar.setTime(date);
        return (out.format(calendar.getTime()));
    }

    /**
     * @param formatoData Retorna a data atual conforme o formato solicitado
     * Formatos aceitos: dd/MM/yyyy HH:mm:ss -> yyyy.MM.dd G 'at' HH:mm:ss z ==
     * 2001.07.04 AD at 12:08:56 PDT -> EEE, MMM d, ''yy == Wed, Jul 4, '01 ->
     * h:mm a == 12:08 PM -> hh 'o''clock' a, zzzz == 12 o'clock PM, Pacific
     * Daylight Time -> K:mm a, z == 0:08 PM, PDT -> yyyyy.MMMMM.dd GGG hh:mm
     * aaa == 02001.July.04 AD 12:08 PM -> EEE, d MMM yyyy HH:mm:ss Z == Wed, 4
     * Jul 2001 12:08:56 -0700 -> yyMMddHHmmssZ == 010704120856-0700 ->
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ == 2001-07-04T12:08:56.235-0700 -> yyyyMMdd ==
     * 20010228 -> yyyy == 2001 -> yyyyMM == 200102 Padrao Esperado para a
     * composição do formato da Data Letter Date or Time Component Presentation
     * Examples G Era designator Text AD y Year Year 1996; 96 M Month in year
     * Month July; Jul; 07 w Week in year Number 27 W Week in month Number 2 D
     * Day in year Number 189 d Day in month Number 10 F Day of week in month
     * Number 2 E Day in week Text Tuesday; Tue a Am/pm marker Text PM H Hour in
     * day (0-23) Number 0 k Hour in day (1-24) Number 24 K Hour in am/pm (0-11)
     * Number 0 h Hour in am/pm (1-12) Number 12 m Minute in hour Number 30 s
     * Second in minute Number 55 S Millisecond Number 978 z Time zone General
     * time zone Pacific Standard Time; PST; GMT-08:00 Z Time zone RFC 822 time
     * zone -0800
     *
     * @return - Retorna a data no formato informado
     */
    public static String dateTimeNowToStr(String formatoData) {
        if (!formatoData.isEmpty()) {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat out = new SimpleDateFormat(formatoData);
            Date date = new Date();
            calendar.setTime(date);
            return (out.format(calendar.getTime()));
        } else {
            return "";
        }
    }

    /**
     * @param date data a ser formatada
     * @param formatoData Retorna a data atual conforme o formato solicitado
     * Formatos aceitos: dd/MM/yyyy HH:mm:ss -> yyyy.MM.dd G 'at' HH:mm:ss z ==
     * 2001.07.04 AD at 12:08:56 PDT -> EEE, MMM d, ''yy == Wed, Jul 4, '01 ->
     * h:mm a == 12:08 PM -> hh 'o''clock' a, zzzz == 12 o'clock PM, Pacific
     * Daylight Time -> K:mm a, z == 0:08 PM, PDT -> yyyyy.MMMMM.dd GGG hh:mm
     * aaa == 02001.July.04 AD 12:08 PM -> EEE, d MMM yyyy HH:mm:ss Z == Wed, 4
     * Jul 2001 12:08:56 -0700 -> yyMMddHHmmssZ == 010704120856-0700 ->
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ == 2001-07-04T12:08:56.235-0700 -> yyyyMMdd ==
     * 20010228 -> yyyy == 2001 -> yyyyMM == 200102 Padrao Esperado para a
     * composição do formato da Data Letter Date or Time Component Presentation
     * Examples G Era designator Text AD y Year Year 1996; 96 M Month in year
     * Month July; Jul; 07 w Week in year Number 27 W Week in month Number 2 D
     * Day in year Number 189 d Day in month Number 10 F Day of week in month
     * Number 2 E Day in week Text Tuesday; Tue a Am/pm marker Text PM H Hour in
     * day (0-23) Number 0 k Hour in day (1-24) Number 24 K Hour in am/pm (0-11)
     * Number 0 h Hour in am/pm (1-12) Number 12 m Minute in hour Number 30 s
     * Second in minute Number 55 S Millisecond Number 978 z Time zone General
     * time zone Pacific Standard Time; PST; GMT-08:00 Z Time zone RFC 822 time
     * zone -0800
     *
     * @return - Retorna a data no formato informado
     */
    public static String dateToStr(Date date, String formatoData) {
        if (!formatoData.isEmpty()) {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat out = new SimpleDateFormat(formatoData);
            calendar.setTime(date);
            return (out.format(calendar.getTime()));
        } else {
            return "";
        }
    }

    /**
     * @param addMinutos minutos para adicionar a hora atual
     * @param formatoData Retorna a data atual conforme o formato solicitado
     * Formatos aceitos: dd/MM/yyyy HH:mm:ss -> yyyy.MM.dd G 'at' HH:mm:ss z ==
     * 2001.07.04 AD at 12:08:56 PDT -> EEE, MMM d, ''yy == Wed, Jul 4, '01 ->
     * h:mm a == 12:08 PM -> hh 'o''clock' a, zzzz == 12 o'clock PM, Pacific
     * Daylight Time -> K:mm a, z == 0:08 PM, PDT -> yyyyy.MMMMM.dd GGG hh:mm
     * aaa == 02001.July.04 AD 12:08 PM -> EEE, d MMM yyyy HH:mm:ss Z == Wed, 4
     * Jul 2001 12:08:56 -0700 -> yyMMddHHmmssZ == 010704120856-0700 ->
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ == 2001-07-04T12:08:56.235-0700 -> yyyyMMdd ==
     * 20010228 -> yyyy == 2001 -> yyyyMM == 200102 Padrao Esperado para a
     * composição do formato da Data Letter Date or Time Component Presentation
     * Examples G Era designator Text AD y Year Year 1996; 96 M Month in year
     * Month July; Jul; 07 w Week in year Number 27 W Week in month Number 2 D
     * Day in year Number 189 d Day in month Number 10 F Day of week in month
     * Number 2 E Day in week Text Tuesday; Tue a Am/pm marker Text PM H Hour in
     * day (0-23) Number 0 k Hour in day (1-24) Number 24 K Hour in am/pm (0-11)
     * Number 0 h Hour in am/pm (1-12) Number 12 m Minute in hour Number 30 s
     * Second in minute Number 55 S Millisecond Number 978 z Time zone General
     * time zone Pacific Standard Time; PST; GMT-08:00 Z Time zone RFC 822 time
     * zone -0800
     *
     * @return - Retorna a data no formato informado
     */
    public static String dateTimeNowToStr(int addMinutos, String formatoData) {
        if (!formatoData.isEmpty()) {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat out = new SimpleDateFormat(formatoData);
            Date date = new Date();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, addMinutos);
            return (out.format(calendar.getTime()));
        } else {
            return "";
        }
    }

    /**
     * Formata a data passada no parametro (segunda-feira, 01 de Maio de 2012)
     *
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

    /**
     *
     * @param date
     * @return YYYYMM
     */
    public static String dateToStrAnoMes(Date date) {
        try {
            if (date != null) {
                return dataFormatYm.format(date);
            } else {
                return "";
            }
        } catch (Exception ex) {
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
        try {
            if (date != null) {
                return dataFormat.format(date);
            } else {
                return "";
            }
        } catch (Exception ex) {
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

    /**
     *
     * @param date DD/MM/YYYY
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

        } catch (ParseException e) {
            return null;
        }
        return d;

    }

    /**
     *
     * @param date YYYY-MM-DD
     * @return Date
     */
    public static Date strToDateNfe(String date) {
        Date d;
        try {
            if ((date.equals("")) || (date == null)) {
                d = null;
            } else {
                d = dataFormatBd.parse(date);
            }

        } catch (ParseException e) {
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
    public static String doubleToCurrStrDb(double ndouble) {
        String ret = "0";
        try {
            //ret = currencyFormat.format(ndouble).replace('.', ' ').replace(',', '.').trim();
            //return ret.replace(" ", "");
            return Double.toString(ndouble);
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

    public static double doubleToRound(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     *
     * @param ndouble
     * @param casas
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
                    if (casas > 6) {
                        retorno = seisFormat.format(ndouble);
                    } else {
                        retorno = currencyFormat.format(ndouble);
                    }
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
                    if (casas > 6) {
                        retorno = seisFormat.format(bdecimal.doubleValue());
                    } else {
                        retorno = currencyFormat.format(bdecimal.doubleValue());
                    }
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
            sRetorno = currencyFormat.format(bdecimal.doubleValue());
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
            sRetorno = currencyFormat.format(bdecimal.doubleValue());
            sRetorno = sRetorno.replace(".", "");
            sRetorno = sRetorno.replace(",", ".");
            return sRetorno;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     *
     * @param bdecimal valor a ser convertido
     * @param casas precisao decimal (2 casas, 4 casas , 6 casas)
     * @return
     */
    public static String decimalToStrDb(BigDecimal bdecimal, int casas) {
        String sRetorno = "0";

        try {
            switch (casas) {
                case 2:
                    sRetorno = currencyFormat.format(bdecimal.doubleValue());
                    break;
                case 4:
                    sRetorno = quatroFormat.format(bdecimal.doubleValue());
                    break;
                case 6:
                    sRetorno = seisFormat.format(bdecimal.doubleValue());
                    break;
                default:
                    sRetorno = currencyFormat.format(bdecimal.doubleValue());
                    break;
            }
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
            sRetorno = sRetorno.replace("ª", " ");
            sRetorno = sRetorno.replace("°", " ");
            sRetorno = sRetorno.replace("º", " ");
            sRetorno = sRetorno.replace("§", " ");
            sRetorno = sRetorno.replace("_", " ");
            sRetorno = sRetorno.replace("£", " ");
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
            sRetorno = sRetorno.replace("ª", " ");
            sRetorno = sRetorno.replace("°", " ");
            sRetorno = sRetorno.replace("º", " ");
            sRetorno = sRetorno.replace("§", " ");
            sRetorno = sRetorno.replace("_", " ");
            sRetorno = sRetorno.replace("£", " ");
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
            sRetorno = strDado.replace("Ç", "C");
            sRetorno = sRetorno.replace("ç", "c");
            sRetorno = sRetorno.replace("Ã", "A");
            sRetorno = sRetorno.replace("ã", "a");
            sRetorno = sRetorno.replace("Õ", "O");
            sRetorno = sRetorno.replace("õ", "o");
            sRetorno = sRetorno.replace("É", "E");
            sRetorno = sRetorno.replace("é", "e");
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
            sRetorno = currencyFormat.format(bdecimal.doubleValue());
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
                curr = curr.replace(",", ".");
                d = new BigDecimal(curr);
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
            if (intStr != null) {
                String newstr = intStr;
                newstr = intStr.replaceAll("[,][0-9]{2}", "");
                newstr = intStr.replaceAll("[.][0-9]{2}", "");

                intStr = newstr.trim();

                if (intStr.equals("") || (intStr.equals("0") || (intStr == null) || (intStr.isEmpty()))) {
                    r = 0;
                } else {
                    r = Integer.parseInt(intStr);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }

        return r;
    }

    /**
     * Converte o numero do tipo inteiro em texto
     *
     * @param integer
     * @return Retorna o numero inteiro como texto
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
     * Converte o Texto em numero do tipo Long
     *
     * @param longStr
     * @return Retorna o texto informado como numero
     */
    public static Long strToLong(String longStr) {
        Long l = 0L;
        try {
            l = Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * Converte Long para String
     *
     * @param l (Numero do tipo Long)
     * @return Retorna o numero informado como texto
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
     * Coloca zeros a esquerda do texto informado.
     *
     * @param strDados
     * @param tamanho
     * @return Retorna Zeros a esquerda do texto informado
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
     * Coloca zeros a direita do texto informado
     *
     * @param strDados
     * @param tamanho
     * @return Retorna o Texto informado com espa?os a Direita
     */
    public static String zerosDireita(String strDados, int tamanho) {
        String newDados = ""; //strDados;
        int tam = strDados.length();
        if (strDados != null) {
            newDados = strDados;
        }

        for (int i = 0; i < (tamanho - tam); i++) {
            newDados += "0";
        }
        return newDados;
    }

    /**
     * Coloca espaços a esquerda do texto informado
     *
     * @param strDados
     * @param tamanho
     * @return Retorna o Texto informado com espaços a esquerda
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
     * Coloca espaços a direita do texto informado
     *
     * @param strDados
     * @param tamanho
     * @return Retorna o Texto informado com espaços a Direita
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
     * Repete o texto informado a quantidade de vezes que foi informado
     *
     * @param strNova
     * @param qtd
     * @return Retorna um texto contendo o texto informado
     */
    public static String replicate(String strNova, int qtd) {
        String newDados = "";

        for (int i = 0; i <= qtd; i++) {
            newDados += strNova;
        }

        return newDados;
    }

    /**
     * Trunca o texto informado no tamanho informado
     *
     * @param strDados
     * @param tamanho
     * @return Retorna texto informado dentro do tamanho informado.
     */
    public static String strTruncate(String strDados, int tamanho) {
        String newDados = "";
        if (strDados != null) {
            if (strDados.length() >= tamanho) {
                newDados = strDados.substring(0, tamanho);
            } else {
                newDados = strDados.substring(0, strDados.length());
            }
        }
        return newDados;
    }

    /**
     * Alinha o texto informado a direita dentro do tamanho informado.
     *
     * @param strDados
     * @param tamanho
     * @return Retorna texto informado com espaços a direita dentro do tamanho
     * informado.
     */
    public static String strTamanhoExato(String strDados, int tamanho) {
        String newDados = "";
        if (strDados == null) {
            newDados = espacosDireita(" ", tamanho);
        }
        if (strDados.isEmpty()) {
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
     * Alinha o texto informado a esquerda dentro do tamanho informado.
     *
     * @param strDados
     * @param tamanho
     * @return Retorna o texto informado com espaços a esquerda dentro do
     * tamanho informado.
     */
    public static String strTamanhoExatoEs(String strDados, int tamanho) {
        String newDados = "";
        if (strDados == null) {
            newDados = espacosEsquerda(" ", tamanho);
        }
        if (strDados.isEmpty()) {
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
     * Centraliza o texto dentro do tamanha informado
     *
     * @param strDados
     * @param tamanho
     * @return Retorna o texto informado centralizado dentro do tamanha
     * informado
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
     * Calcula a data com base no prazo informado.
     *
     * @param prazo
     * @param data
     * @return Retorna uma data somando o prazo informado (Calendar)
     */
    public static Calendar calculaVencimento(int prazo, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.DAY_OF_MONTH, prazo);
        return hoje;
    }

    /**
     * Calcula e Retorna a data para o mes informado
     *
     * @param mes
     * @param data
     * @return Retorna uma data para o mes informado (Calendar)
     */
    public static Calendar calculaVencimentoFixo(int mes, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.MONTH, mes);
        return hoje;
    }

    /**
     * Calcula e Retorna a data para o mes informado
     *
     * @param mes mes para o vencimento
     * @param data inicial para contagem
     * @return Retorna a da para o mes informado (Date)
     */
    public static Date calculaDataFixa(int mes, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.MONTH, mes);
        return hoje.getTime();
    }

    /**
     * Adiciona o numero de dias a uma data informada.
     *
     * @param prazo prazo para o vencimento
     * @param data inicial para contagem
     * @return Retorna a data somando o numero de dias informado.
     */
    public static Date calculaData(int prazo, Date data) {
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        hoje.add(Calendar.DAY_OF_MONTH, prazo);
        return hoje.getTime();
    }

    /**
     * Calcula e retorna o ultimo dia do mes na data informada.
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
     * Calcula e retorna o primeiro dia do mes na data informada.
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
     * Calcula a diferença de duas datas em dias <br> <b>Importante:</b> Quando
     * realiza a diferença em dias entre duas datas, este método não considera
     * as horas restantes.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de meses existentes entre a dataInicial e dataFinal.
     * Se a data for parte do mes retorna 1
     */
    public static int diferencaEmMeses(Date dataInicial, Date dataFinal) {
        int result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado é diferença entre as datas em dias
        result = (int) (diferencaEmDias / 30);

        if (result == 0) {
            result = 1;
        }

        return result;

    }

    /**
     * Calcula a diferença de duas datas em dias <br> <b>Importante:</b> Quando
     * realiza a diferença em dias entre duas datas, este método considera as
     * horas restantes e as converte em fração de dias.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de dias existentes entre a dataInicial e dataFinal.
     */
    public static double diferencaEmDias(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado é diferença entre as datas em dias
        long horasRestantes = (diferenca / 1000) / 60 / 60 % 24; //calcula as horas restantes
        result = diferencaEmDias + (horasRestantes / 24d); //transforma as horas restantes em fração de dias

        return result;
    }

    /**
     * Calcula a diferença de duas datas em dias <br> <b>Importante:</b> Quando
     * realiza a diferença em dias entre duas datas, este método nao considera
     * as horas restantes e as converte em fração de dias.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de dias existentes entre a dataInicial e dataFinal.
     */
    public static int diferencaEmDiasInteiros(Date dataInicial, Date dataFinal) {
        int result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado é diferença entre as datas em dias
        result = (int) diferencaEmDias;

        return result;
    }

    /**
     * Calcula a diferença de duas datas em horas <br> <b>Importante:</b> Quando
     * realiza a diferença em horas entre duas datas, este método considera os
     * minutos restantes e os converte em fração de horas.
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
     * Calcula a diferença de duas datas em minutos <br> <b>Importante:</b>
     * Quando realiza a diferença em minutos entre duas datas, este método
     * considera os segundos restantes e os converte em fração de minutos.
     *
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de minutos existentes entre a dataInicial e dataFinal.
     */
    public static double diferencaEmMinutos(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmMinutos = (diferenca / 1000) / 60; //resultado é diferença entre as datas em minutos
        long segundosRestantes = (diferenca / 1000) % 60; //calcula os segundos restantes
        result = diferencaEmMinutos + (segundosRestantes / 60d); //transforma os segundos restantes em minutos

        return result;
    }

    /**
     *
     * @param toIndex texto a ser procurado em args
     * @param args a cadeixa de
     * @return pesquisa
     */
    public static int returnIndex(String toIndex, String... args) {
        for (int i = 0; i < args.length; i++) {
            if (toIndex.equals(args[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retorna somente os numeros que fazem parte de uma string
     *
     * @param strNumber - string contendo numeros a serem processados
     * @return retorna somente os numeros dentro da string
     */
    public static String somenteNumeros(String strNumber) {
        if ((strNumber != null) && (!strNumber.isEmpty())) {
            return strNumber.replaceAll("[^0123456789]", "");
        } else {
            return "";
        }
    }

    /**
     * Retorna True se existir somente numeros na string
     *
     * @param str - string contendo numeros a serem processados
     * @return retorna verdadeiro se existir somente numeros na string
     */
    public static boolean isNumeric(String str) {
        return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
    }

    /**
     * Retorna BigDecimal aredendando o valor para quantidade de casas informada
     *
     * @param valor valor a ser aredondado
     * @param scala valor da escala numerica apos a virgula
     * @return retorna o valor arrendondado
     */
    public static BigDecimal bigDecimalRound(BigDecimal valor, int scala) {
        return valor.setScale(scala, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Retorna Texto contido no intervalor de tags informado
     *
     * @param strBase texto a ser pesquisado
     * @param strInicial parte do texto iniciar da pesquisa
     * @param strFinal parte do texto final da pesquisa
     * @return retorna o texto entre o texto inicia e o texto final
     */
    public static String buscaTexto(String strBase, String strInicial, String strFinal) {
        String ret = "";

        // busca aposicao inicial no texto
        int pinicial = strBase.indexOf(strInicial);
        int pfinal = strBase.indexOf(strFinal);
        if ((pinicial > 0) && (pfinal > pinicial)) {
            pinicial += strInicial.length();
            ret = Formato.strTiraIncompativeisBd(Formato.strTiraIncompativeis(Formato.strTiraAcentos(strBase.substring(pinicial, pfinal)))).trim();
        }
        return ret;
    }

    /**
     * Retorna Texto contido no intervalor de tags informado
     *
     * @param barCode codigo de barras a ser validado
     * @return retorna o texto entre o texto inicia e o texto final
     */
    public static boolean isValidBarCodeEAN(String barCode) {
        int digit;
        int calculated;
        String ean;
        String checkSum = "";
        int sum = 0;
        //checkSum = "313131313131";
        checkSum = "131313131313";
        if (barCode != null) {
            if (!barCode.isEmpty()) {
                barCode = Formato.somenteNumeros(barCode);

                if (barCode.length() == 8 || barCode.length() == 13) {
                    digit = Integer.parseInt("" + barCode.charAt(barCode.length() - 1));
                    ean = barCode.substring(0, barCode.length() - 1);
                    for (int i = 0; i <= ean.length() - 1; i++) {
                        sum += (Integer.parseInt("" + ean.charAt(i))) * (Integer.parseInt("" + checkSum.charAt(i)));
                    }
                    calculated = 10 - (sum % 10);
                    return (digit == calculated);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Converte decimal para hexadecimal. A regra é ficar dividindo o valor por
     * 16, pegar o resto de cada divisão e inserir o valor da direita para a
     * esquerda na String * de retorno. Se o resultado da divisão for maior que
     * 15, chamo o método através de recursão. O algoritmo é executado até que o
     * valor que foi sucessivamente dividido se torne 0. Obs.: assume que o
     * valor passado é inteiro positivo. Exemplo: 1279 1279/16 = 79 -> resto 15
     * -> Resultado: F 79/16 = 4 -> resto 15 -> Resultado: F 4/16 = 0 -> resto 4
     * -> Resultado: 4
     *
     * Resultado: 4FF
     *
     * @param valor número decimal a ser convertido
     * @return String contendo o valor em hexadecimal
     */
    public static String converteDecimalParaHexadecimal(int valor) {
        char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int resto = -1;
        StringBuilder sb = new StringBuilder();
        try {
            if (valor == 0) {
                return "0";
            }

            // enquanto o resultado da divisão por 16 for maior que 0 adiciona o resto ao início da String de retorno
            while (valor > 0) {
                resto = valor % 16;
                valor = valor / 16;
                sb.insert(0, hexa[resto]);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "converteDecimalParaHexadecimal");
        }
        return sb.toString();
    }

    /*
     * Gera a chave de licença do sistema por CNPJ da empresa
     * @param cnpj numero do CNPJ para gercao da licenca
     */
    public static String geraChaveLicenca(String cnpj) {
        String chave = "";
        char[] dig;
        char[] key = {'A', 'g', 'e', 'u', 'E', 'l', 'i', 'a', 's', 'R', 'o', 'v', 'e', 'R'};
        int valor;
        try {
            if ((!cnpj.isEmpty()) && (cnpj.length() == 14)) {
                for (int i = 0; i < cnpj.length(); i++) {
                    dig = cnpj.toCharArray();
                    valor = dig[i] + key[i];
                    chave += converteDecimalParaHexadecimal(valor);
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "geraChaveLicenca");
        }
        return chave;
    }

    /*
     * Pesquisa se a string informada esta contida na string permitida
     * @param strPermitida string com os dados permitdos
     * @param strBusca string a ser pesquisada
     * @return verdadeiro se o strBusca estiver contido na strPermitida
     */
    public static boolean strPermitida(String strPermitida, String strBusca) {
        return (!strPermitida.contains(strBusca));
    }

    /**
     * Copia arquivos de um local para o outro
     *
     * @param origem - Arquivo de origem
     * @param destino - Arquivo de destino
     * @param overwrite - Confirmação para sobrescrever os arquivos
     * @throws IOException
     */
    public static void copyFile(File origem, File destino, boolean overwrite) throws IOException {
        if (destino.exists() && !overwrite) {
            trataErro.addListaErros(destino.getName() + " já existe, ignorando...");
            trataErro.mostraListaErros();
        } else {
            FileOutputStream fisDestino;
            try (FileInputStream fisOrigem = new FileInputStream(origem)) {
                fisDestino = new FileOutputStream(destino);
                FileChannel fcOrigem = fisOrigem.getChannel();
                FileChannel fcDestino = fisDestino.getChannel();
                fcOrigem.transferTo(0, fcOrigem.size(), fcDestino);
            }
            fisDestino.close();
        }
    }

    /**
     * Faz redimensionamento da imagem conforme os parâmetros imgLargura e
     * imgAltura mantendo a proporcionalidade. Caso a imagem seja menor do que
     * os parâmetros de redimensionamento, a imagem não será redimensionada.
     *
     * @param caminhoImg caminho e nome da imagem a ser redimensionada.
     * @param imgLargura nova largura da imagem após ter sido redimensionada.
     * @param imgAltura nova altura da imagem após ter sido redimensionada.
     *
     * @return Retorna a imagem redimencionada
     * @throws Exception Erro ao redimensionar imagem
     * **********************************************************************************************************
     */
    public static ImageIcon setImagemDimensao(String caminhoImg, Integer imgLargura, Integer imgAltura) throws Exception {
        Double novaImgLargura = null;
        Double novaImgAltura = null;
        Double imgProporcao = null;
        Graphics2D g2d = null;
        BufferedImage imagem = null, novaImagem = null;
        try {
            //--- Obtém a imagem a ser redimensionada ---
            imagem = ImageIO.read(new File(caminhoImg));
            //--- Obtém a largura da imagem ---
            novaImgLargura = (double) imagem.getWidth();
            //--- Obtám a altura da imagem ---
            novaImgAltura = (double) imagem.getHeight();
            //--- Verifica se a altura ou largura da imagem recebida é maior do que os ---
            //--- parâmetros de altura e largura recebidos para o redimensionamento   ---
            if (novaImgLargura >= imgLargura) {
                imgProporcao = (novaImgAltura / novaImgLargura);//calcula a proporção
                novaImgLargura = (double) imgLargura;
                //--- altura deve <= ao parâmetro imgAltura e proporcional a largura ---
                novaImgAltura = (novaImgLargura * imgProporcao);
                //--- se altura for maior do que o parâmetro imgAltura, diminui-se a largura de ---
                //--- forma que a altura seja igual ao parâmetro imgAltura e proporcional a largura ---
                while (novaImgAltura > imgAltura) {
                    novaImgLargura = (double) (--imgLargura);
                    novaImgAltura = (novaImgLargura * imgProporcao);
                }
            } else if (novaImgAltura >= imgAltura) {
                imgProporcao = (novaImgLargura / novaImgAltura);//calcula a proporção
                novaImgAltura = (double) imgAltura;
                //--- se largura for maior do que o parâmetro imgLargura, diminui-se a altura de ---
                //--- forma que a largura seja igual ao parâmetro imglargura e proporcional a altura ---
                while (novaImgLargura > imgLargura) {
                    novaImgAltura = (double) (--imgAltura);
                    novaImgLargura = (novaImgAltura * imgProporcao);
                }
            }
            novaImagem = new BufferedImage(novaImgLargura.intValue(), novaImgAltura.intValue(), BufferedImage.TYPE_INT_RGB);
            g2d = novaImagem.createGraphics();
            g2d.drawImage(imagem.getScaledInstance(novaImgLargura.intValue(), novaImgAltura.intValue(), 10000), 0, 0, null);
            g2d.dispose();
            //ImageIO.write(novaImagem, "JPG", new File(caminhoImg));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return new ImageIcon(novaImagem);
    }//setImagemDimensao

    public static void comboBoxSelectedValue(JComboBox comboBox, String value) {
        String item;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            comboBox.setSelectedIndex(i);
            item = comboBox.getSelectedItem().toString();
            if (item.substring(0, value.length()).equalsIgnoreCase(value)) {
                comboBox.setSelectedIndex(i);
                break;
            }

        }
    }

}
