 /*
 * ExportDocDialog.java
 *
 * Created on 22 de Outubro de 2018, 22:10
 */
package winthorDb.forms.etl.cafe3Coracoes;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.forms.export.LayoutRemessaDialog;
import winthorDb.util.Formato;

/**
 *
 * @author Ageu Elias Rover
 */
public class ExportDocDialog extends javax.swing.JDialog {

    int vContaRegistroD = 0;
    int vContaRegistroD1 = 0;
    int vContaRegistroD2 = 0;
    int vContaRegistroD3 = 0;
    int vContaRegistroD4 = 0;
    int vContaRegistroD5 = 0;

    int vContaRegistroH = 0;
    int vContaRegistroT = 0;
    int vContaRegistroG = 0;

    public static void open(int idDoc, String tipoDoc, String idFaturamento) {
        new ExportDocDialog(null, true, idDoc, tipoDoc, idFaturamento).setVisible(true);
    }

    /**
     * Creates new form
     *
     * @param parent
     * @param modal
     * @param idDoc
     * @param tipoDoc
     * @param idFaturamento
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ExportDocDialog(java.awt.Frame parent, boolean modal, int idDoc, String tipoDoc, String idFaturamento) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);
        setTitle("Comando SQL");

        edtCodDoc.setText("" + idDoc);
        edtTipoDoc.setText(tipoDoc);
        edtRemessa.setText(Formato.dateTimeNowToStr("ddMMyyHHmms"));

        exibeSqlComando();
    }

    private void exibeSqlComando() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                String sqlHeader = "";
                String sqlDetalhe = "";
                String sqlDetalheN1 = "";
                String sqlDetalheN2 = "";
                String sqlDetalheN3 = "";
                String sqlDetalheN4 = "";
                String sqlDetalheN5 = "";
                String sqlTreller = "";
                String sqlComando = "";
                try {
                    tblHeader.clearTableData();
                    tblDetalhe.clearTableData();
                    tblDetalheN1.clearTableData();
                    tblDetalheN2.clearTableData();
                    tblDetalheN3.clearTableData();
                    tblDetalheN4.clearTableData();
                    tblDetalheN5.clearTableData();
                    tblTreller.clearTableData();
                    tblSqlConsulta.clearTableData();

                    if ((!edtCodDoc.getText().isEmpty()) && (!edtTipoDoc.getText().isEmpty())) {
                        sqlHeader = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'H'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlDetalhe = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'D'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlDetalheN1 = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'D1'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlDetalheN2 = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'D2'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlDetalheN3 = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'D3'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlDetalheN4 = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'D4'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlDetalheN5 = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'D5'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlTreller = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                                + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                                + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posInicial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                                + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                                + " ('' ||comentario) as comentario "
                                + " FROM layoutDoc "
                                + " WHERE tipoDoc = '" + edtTipoDoc.getText() + "' "
                                + " AND idDoc = " + edtCodDoc.getText()
                                + " AND tipoRegistro = 'T'"
                                + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                        sqlComando = "SELECT "
                                + " ('' ||id) as id, ('' ||idDoc) as idDoc, ('' ||tipoDoc) as tipoDoc, "
                                + " ('' ||sql_Header) as sql_Header, "
                                + " ('' ||sql_Detalhe_n0) as sql_Detalhe_n0, "
                                + " ('' ||sql_Detalhe_n1) as sql_Detalhe_n1, "
                                + " ('' ||sql_Detalhe_n2) as sql_Detalhe_n2, "
                                + " ('' ||sql_Detalhe_n3) as sql_Detalhe_n3, "
                                + " ('' ||sql_Detalhe_n4) as sql_Detalhe_n4, "
                                + " ('' ||sql_Detalhe_n5) as sql_Detalhe_n5, "
                                + " ('' ||sql_Treller) as sql_Treller "
                                + " FROM LayoutDocSql"
                                + " WHERE idDoc = " + edtCodDoc.getText()
                                + " AND tipoDoc = '" + edtTipoDoc.getText() + "' ";

                        tblHeader.setTableData(sqlHeader);
                        tblDetalhe.setTableData(sqlDetalhe);
                        tblDetalheN1.setTableData(sqlDetalheN1);
                        tblDetalheN2.setTableData(sqlDetalheN2);
                        tblDetalheN3.setTableData(sqlDetalheN3);
                        tblDetalheN4.setTableData(sqlDetalheN4);
                        tblDetalheN5.setTableData(sqlDetalheN5);
                        tblTreller.setTableData(sqlTreller);
                        tblSqlConsulta.setTableData(sqlComando);

                    }

                } catch (Exception ex) {
                    trataErro.trataException(ex, "exibeSqlComando");
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void exportaDados() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                try {
                    txtExportDados.setText("");
                    vContaRegistroH = 0;
                    vContaRegistroD = 0;
                    vContaRegistroD1 = 0;
                    vContaRegistroD2 = 0;
                    vContaRegistroD3 = 0;
                    vContaRegistroD4 = 0;
                    vContaRegistroD5 = 0;
                    vContaRegistroG = 0;
                    vContaRegistroT = 0;

                    // Campos variaveis de tele para o Select   #NOVAS_TAGS#
                    String fields = "";

                    // Exportacao do Header
                    String filtro_header = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                    if (!edtCodCli.getText().isEmpty()) {
                        filtro_header += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                    }
                    if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                        if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                            filtro_header += "AND PCNFSAID.DTSAIDA BETWEEN "
                                    + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                    + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                        } else {
                            MessageDialog.error("Intervalo de datas de saida invalido!");
                        }
                    }
                    // executar a consulta SQL para buscar os dados
                    String sqlHeader = tblSqlConsulta.getConteudoRowSelected("sql_header").toString().replaceAll("#FILTRO_DADOS_HEADER#", filtro_header);
                    sqlHeader = sqlHeader.replaceAll("#NOVAS_TAGS#", fields);

                    if (!sqlHeader.isEmpty()) {
                        tblDataHeader.clearTableData();
                        tblDataHeader.setTableData(sqlHeader);
                        if (tblDataHeader.getRowCount() >= 0) {
                            // inicia a montagem dos dados da linha de exportacao
                            processaExport("H");
                        }
                    }
                    // Exportacao do Detalhe

                    // Campos variaveis de tele para o Select   #NOVAS_TAGS#
                    fields = " ";

                    String filtro_detalhe = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                    if (!edtCodCli.getText().isEmpty()) {
                        filtro_detalhe += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                    }

                    if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                        if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                            filtro_detalhe += "AND PCNFSAID.DTSAIDA BETWEEN "
                                    + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                    + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                        } else {
                            MessageDialog.error("Intervalo de datas de saida invalido!");
                        }
                    }

                    // executar a consulta SQL para buscar os dados
                    String sqlDetalhe = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe_n0").toString().replaceAll("#FILTRO_DADOS_DETALHE_N0#", filtro_detalhe);
                    sqlDetalhe = sqlDetalhe.replaceAll("#NOVAS_TAGS#", fields);

                    if (!sqlDetalhe.isEmpty()) {
                        tblDataDetalhe.clearTableData();
                        tblDataDetalhe.setTableData(sqlDetalhe);
                        if (tblDataDetalhe.getRowCount() >= 0) {
                            // inicia a montagem dos dados da linha de exportacao nivel 0
                            processaExport("D");
                        }
                    }
                    // Exportacao do Treller
                    // Campos variaveis de tele para o Select   #NOVAS_TAGS#

                    fields = " ";

                    String filtro_treller = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                    if (!edtCodCli.getText().isEmpty()) {
                        filtro_treller += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                    }
                    if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                        if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                            filtro_treller += "AND PCNFSAID.DTSAIDA BETWEEN "
                                    + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                    + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                        } else {
                            MessageDialog.error("Intervalo de datas de saida invalido!");
                        }
                    }
                    // executar a consulta SQL para buscar os dados
                    String sqlTreller = tblSqlConsulta.getConteudoRowSelected("sql_Treller").toString().replaceAll("#FILTRO_DADOS_TRELLER#", filtro_treller);
                    sqlTreller = sqlTreller.replaceAll("#NOVAS_TAGS#", fields);

                    if (!sqlTreller.isEmpty()) {
                        tblDataTreller.clearTableData();
                        tblDataTreller.setTableData(sqlTreller);
                        if (tblDataTreller.getRowCount() >= 0) {
                            // inicia a montagem dos dados da linha de exportacao
                            processaExport("T");
                        }
                    }
                } catch (Exception ex) {
                    trataErro.trataException(ex, "exportaDados \n " + this.getName());
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void processaExport(final String tipoReg) {
        String linha = "";
        String campo = "";
        String tipoDado = "";
        String mascara = "";
        String vlrDefault = "";
        String sqlCampo = "";
        int tamanho = 0;

        try {
            switch (tipoReg.toUpperCase()) {
                case "H":
                    for (int d = 0; d < tblDataHeader.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroH++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblHeader.getRowCount(); i++) {
                            tipoDado = tblHeader.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblHeader.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblHeader.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblHeader.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblHeader.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataHeader.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblHeader.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);
                        linha = "";
                    }
                    break;

                case "D":
                    for (int d = 0; d < tblDataDetalhe.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroD++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblDetalhe.getRowCount(); i++) {
                            tipoDado = tblDetalhe.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblDetalhe.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblDetalhe.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblDetalhe.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblDetalhe.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataDetalhe.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblDataDetalhe.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);

                        // Exportacao do Detalhe N1 para cada linha do N0
                        String filtro_detalhe = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                        if (!edtCodCli.getText().isEmpty()) {
                            filtro_detalhe += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                        }

                        if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                            if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                                filtro_detalhe += "AND PCNFSAID.DTSAIDA BETWEEN "
                                        + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                        + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                            } else {
                                MessageDialog.error("Intervalo de datas de saida invalido!");
                            }
                        }

                        // executar a consulta SQL para buscar os dados
                        String sqlDetalheBaseN1 = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe_n1").toString();
                        String sqlDetalheN1 = sqlDetalheBaseN1.replaceAll("#FILTRO_DADOS_DETALHE_N1#", filtro_detalhe);

                        if (!sqlDetalheBaseN1.isEmpty()) {
                            tblDataDetalheN1.clearTableData();
                            tblDataDetalheN1.setTableData(sqlDetalheN1);
                            if (tblDataDetalheN1.getRowCount() >= 0) {
                                // inicia a montagem dos dados da linha de exportacao nivel 0
                                processaExport("D1");
                            }
                        }
                    }

                    break;
                case "D1":
                    for (int d = 0; d < tblDataDetalheN1.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroD1++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblDetalheN1.getRowCount(); i++) {
                            tipoDado = tblDetalheN1.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblDetalheN1.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblDetalheN1.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblDetalheN1.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblDetalheN1.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataDetalheN1.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblDataDetalheN1.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }

                        txtExportDados.append(linha);

                        // Exportacao do Detalhe N2 para cada linha do N1
                        String filtro_detalhe = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                        if (!edtCodCli.getText().isEmpty()) {
                            filtro_detalhe += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                        }

                        if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                            if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                                filtro_detalhe += "AND PCNFSAID.DTSAIDA BETWEEN "
                                        + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                        + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                            } else {
                                MessageDialog.error("Intervalo de datas de saida invalido!");
                            }
                        }

                        // executar a consulta SQL para buscar os dados
                        String sqlDetalheBaseN2 = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe_n2").toString();
                        String sqlDetalheN2 = sqlDetalheBaseN2.replaceAll("#FILTRO_DADOS_DETALHE_N2#", filtro_detalhe);

                        if (!sqlDetalheBaseN2.isEmpty()) {
                            tblDataDetalheN2.clearTableData();
                            tblDataDetalheN2.setTableData(sqlDetalheN2);
                            if (tblDataDetalheN2.getRowCount() >= 0) {
                                // inicia a montagem dos dados da linha de exportacao nivel 0
                                processaExport("D2");
                            }
                        }
                    }

                    break;

                case "D2":
                    for (int d = 0; d < tblDataDetalheN2.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroD2++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblDetalheN2.getRowCount(); i++) {
                            tipoDado = tblDetalheN2.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblDetalheN2.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblDetalheN2.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblDetalheN2.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblDetalheN2.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataDetalheN2.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblDataDetalheN2.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);

                        // Exportacao do Detalhe N3 para cada linha do N2
                        String filtro_detalhe = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                        if (!edtCodCli.getText().isEmpty()) {
                            filtro_detalhe += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                        }

                        if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                            if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                                filtro_detalhe += "AND PCNFSAID.DTSAIDA BETWEEN "
                                        + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                        + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                            } else {
                                MessageDialog.error("Intervalo de datas de saida invalido!");
                            }
                        }

                        
                        filtro_detalhe += " AND PCNFSAID.NUMNOTA IN (" + tblDataDetalheN2.getConteudoRowSelected("NUMERO_NOTA").toString() + ") ";
                        
                        // executar a consulta SQL para buscar os dados
                        String sqlDetalheBaseN3 = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe_n3").toString();
                        String sqlDetalheN3 = sqlDetalheBaseN3.replaceAll("#FILTRO_DADOS_DETALHE_N3#", filtro_detalhe);

                        if (!sqlDetalheBaseN3.isEmpty()) {
                            tblDataDetalheN3.clearTableData();
                            tblDataDetalheN3.setTableData(sqlDetalheN3);
                            if (tblDataDetalheN3.getRowCount() >= 0) {
                                // inicia a montagem dos dados da linha de exportacao nivel 0
                                processaExport("D3");
                            }
                        }
                    }

                    break;

                case "D3":
                    for (int d = 0; d < tblDataDetalheN3.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroD3++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblDetalheN3.getRowCount(); i++) {
                            tipoDado = tblDetalheN3.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblDetalheN3.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblDetalheN3.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblDetalheN3.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblDetalheN3.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataDetalheN3.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblDataDetalheN3.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);

                        // Exportacao do Detalhe N4 para cada linha do N3
                        String filtro_detalhe = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                        if (!edtCodCli.getText().isEmpty()) {
                            filtro_detalhe += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                        }

                        if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                            if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                                filtro_detalhe += "AND PCNFSAID.DTSAIDA BETWEEN "
                                        + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                        + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                            } else {
                                MessageDialog.error("Intervalo de datas de saida invalido!");
                            }
                        }


                        // executar a consulta SQL para buscar os dados
                        String sqlDetalheBaseN4 = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe_n4").toString();
                        String sqlDetalheN4 = sqlDetalheBaseN4.replaceAll("#FILTRO_DADOS_DETALHE_N4#", filtro_detalhe);

                        if (!sqlDetalheBaseN4.isEmpty()) {
                            tblDataDetalheN4.clearTableData();
                            tblDataDetalheN4.setTableData(sqlDetalheN4);
                            if (tblDataDetalheN4.getRowCount() >= 0) {
                                // inicia a montagem dos dados da linha de exportacao nivel 0
                                processaExport("D4");
                            }
                        }
                    }

                    break;

                case "D4":
                    for (int d = 0; d < tblDataDetalheN4.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroD4++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblDetalheN4.getRowCount(); i++) {
                            tipoDado = tblDetalheN4.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblDetalheN4.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblDetalheN4.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblDetalheN4.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblDetalheN4.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataDetalheN4.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblDataDetalheN4.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);

                        // Exportacao do Detalhe N5 para cada linha do N4
                        String filtro_detalhe = " AND NVL(PCNFSAID.CODFILIALNF,PCNFSAID.CODFILIAL) = '" + edtCodFilial.getText() + "' ";

                        if (!edtCodCli.getText().isEmpty()) {
                            filtro_detalhe += " AND CLIENTE.CODCLI = " + edtCodCli.getText();
                        }

                        if ((txtDataInicial.getDate() != null) && (txtDataFinal.getDate() != null)) {
                            if (txtDataInicial.getDate().before(txtDataFinal.getDate())) {
                                filtro_detalhe += "AND PCNFSAID.DTSAIDA BETWEEN "
                                        + "TO_DATE('" + Formato.dateToStr(txtDataInicial.getDate()) + "','DD/MM/YYYY') "
                                        + " AND TO_DATE('" + Formato.dateToStr(txtDataFinal.getDate()) + "','DD/MM/YYYY') ";
                            } else {
                                MessageDialog.error("Intervalo de datas de saida invalido!");
                            }
                        }


                        // executar a consulta SQL para buscar os dados
                        String sqlDetalheBaseN5 = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe_n5").toString();
                        String sqlDetalheN5 = sqlDetalheBaseN5.replaceAll("#FILTRO_DADOS_DETALHE_N5#", filtro_detalhe);

                        if (!sqlDetalheBaseN5.isEmpty()) {
                            tblDataDetalheN5.clearTableData();
                            tblDataDetalheN5.setTableData(sqlDetalheN5);
                            if (tblDataDetalheN5.getRowCount() >= 0) {
                                // inicia a montagem dos dados da linha de exportacao nivel 0
                                processaExport("D5");
                            }
                        }
                    }

                    break;

                case "D5":
                    for (int d = 0; d < tblDataDetalheN5.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroD5++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblDetalheN5.getRowCount(); i++) {
                            tipoDado = tblDetalheN5.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblDetalheN5.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblDetalheN5.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblDetalheN5.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblDetalheN5.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataDetalheN5.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblDataDetalheN5.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);
                    }

                    break;
                case "T":
                    for (int d = 0; d < tblDataTreller.getRowCount(); d++) {
                        linha = "";
                        vContaRegistroT++;
                        vContaRegistroG++;
                        for (int i = 0; i < tblTreller.getRowCount(); i++) {
                            tipoDado = tblTreller.getConteudoRow("tipoDado", i).toString().trim();
                            tamanho = Formato.strToInt(tblTreller.getConteudoRow("tamanho", i).toString().trim());
                            vlrDefault = tblTreller.getConteudoRow("valor_default", i).toString().trim();
                            mascara = tblTreller.getConteudoRow("mascara", i).toString().trim();
                            sqlCampo = tblTreller.getConteudoRow("sqlCampo", i).toString().trim();
                            campo = "";

                            // processa o tipo de dado solicitado
                            switch (tipoDado.toUpperCase()) {
                                case "ALF":
                                    campo = Formato.strTamanhoExato(vlrDefault, tamanho);
                                    break;
                                case "NMR":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(vlrDefault, tamanho), tamanho);
                                    break;
                                case "DTA":
                                    campo = Formato.strTamanhoExato(Formato.zerosEsquerda(Formato.dateTimeNowToStr(mascara), tamanho), tamanho);
                                    break;
                                case "VZA":
                                    campo = Formato.strTamanhoExato(Formato.replicate(" ", tamanho), tamanho);
                                    break;
                                case "ZRO":
                                    campo = Formato.strTamanhoExato(Formato.replicate("0", tamanho), tamanho);
                                    break;
                                case "NLN":
                                    campo = "\r\n";
                                    break;
                                case "SQL":
                                    campo = Formato.strTamanhoExato(tblDataTreller.getConteudoRow(sqlCampo, d).toString(), tamanho);
                                    break;
                                case "VAR":
                                    switch (vlrDefault.toUpperCase()) {
                                        case "VCONTAREGISTROD":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD1":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD1, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD2":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD2, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD3":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD3, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD4":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD4, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROD5":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroD5, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VREMESSA":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtRemessa.getText(), tamanho), tamanho);
                                            break;
                                    }
                            }
                            if ((!tipoDado.equalsIgnoreCase("NLN")) && (campo.length() != tamanho)) {
                                MessageDialog.error("Tamanho do campo não confere com o layout "
                                        + "\n sequencia: " + tblTreller.getConteudoRow("sequencia", i).toString().trim()
                                        + "\n tipo dado: " + tipoDado
                                        + "\n Campo: " + vlrDefault + " | " + sqlCampo
                                        + "\n Dado: " + campo
                                        + "\n Tamanho: " + tamanho
                                        + "\n calculado: " + campo.length()
                                );
                            }
                            linha += campo;
                        }
                        txtExportDados.append(linha);
                        linha = "";
                    }
                    break;
                default:
                    throw new Exception("Tipo de Registro invalido!");

            } // fim for tblDados
        } catch (Exception ex) {
            trataErro.trataException(ex, "exportaDados");
        }

    }

    /*
     Gera um arquivo CSV com os campos de cabeçalho e os dados separados por 
     ponto e virgula, para poder usar o execel para verificar se as posiçoes 
     estao corretas conforme o layout
     */
    private void processaValidarExport(final String tipoReg, final String dados) {
        String separador = ";";
        String linhaCampos = "";
        String linhaDados = "";
        String sqlCampo = "";
        int posInicial = 0;
        int posFinal = 0;

        try {
            switch (tipoReg.toUpperCase()) {
                case "H":

                    linhaCampos = "";
                    linhaDados = "";
                    for (int i = 0; i < tblHeader.getRowCount(); i++) {
                        posInicial = Formato.strToInt(tblHeader.getConteudoRow("posinicial", i).toString().trim());
                        posFinal = Formato.strToInt(tblHeader.getConteudoRow("posfinal", i).toString().trim());
                        sqlCampo = tblHeader.getConteudoRow("sqlCampo", i).toString().trim();

                        if (tblDataHeader.getRowCount() <= 1) {
                            // captura os campos e separa os mesmos
                            linhaCampos += sqlCampo + separador;
                        }

                        // captura os dados dos campos e separa os mesmo
                        linhaDados += dados.substring(posInicial, posFinal) + separador;
                    }
                    txtExportDados.append(linhaCampos + "\n");
                    txtExportDados.append(linhaDados + "\n");

                    break;
                case "D":

                    linhaCampos = "";
                    linhaDados = "";

                    for (int i = 0; i < tblDetalhe.getRowCount(); i++) {
                        posInicial = Formato.strToInt(tblDetalhe.getConteudoRow("posinicial", i).toString().trim());
                        posFinal = Formato.strToInt(tblDetalhe.getConteudoRow("posfinal", i).toString().trim());
                        sqlCampo = tblDetalhe.getConteudoRow("sqlCampo", i).toString().trim();

                        if (tblDataDetalhe.getRowCount() <= 1) {
                            // captura os campos e separa os mesmos
                            linhaCampos += sqlCampo + separador;
                        }

                        // captura os dados dos campos e separa os mesmo
                        linhaDados += dados.substring(posInicial, posFinal) + separador;
                    }

                    txtExportDados.append(linhaCampos + "\n");
                    txtExportDados.append(linhaDados + "\n");

                    break;
                case "T":

                    linhaCampos = "";
                    linhaDados = "";
                    for (int i = 0; i < tblTreller.getRowCount(); i++) {
                        posInicial = Formato.strToInt(tblTreller.getConteudoRow("posinicial", i).toString().trim());
                        posFinal = Formato.strToInt(tblTreller.getConteudoRow("posfinal", i).toString().trim());
                        sqlCampo = tblTreller.getConteudoRow("sqlCampo", i).toString().trim();

                        if (tblDataTreller.getRowCount() <= 1) {
                            // captura os campos e separa os mesmos
                            linhaCampos += sqlCampo + separador;
                        }

                        // captura os dados dos campos e separa os mesmo
                        linhaDados += dados.substring(posInicial, posFinal) + separador;
                    }
                    txtExportDados.append(linhaCampos + "\n");
                    txtExportDados.append(linhaDados + "\n");

                    break;
                default:
                    throw new Exception("Tipo de Registro invalido!");

            } // fim for tblDados
        } catch (Exception ex) {
            trataErro.trataException(ex, "ValidarExportaDados");
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pMenuRemessa = new javax.swing.JPopupMenu();
        mnuRemessaNova = new javax.swing.JMenuItem();
        mnuRemessaBuscar = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        edtCodDoc = new javax.swing.JTextField();
        edtTipoDoc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        btnProcessar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        edtCodCli = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edtRemessa = new javax.swing.JTextField();
        txtDataInicial = new com.toedter.calendar.JDateChooser();
        txtDataFinal = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnLayout = new javax.swing.JButton();
        edtValidar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtExportDados = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblDataHeader = new winthorDb.util.CustomTable();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblDataDetalhe = new winthorDb.util.CustomTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblDataDetalheN1 = new winthorDb.util.CustomTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblDataDetalheN2 = new winthorDb.util.CustomTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblDataDetalheN3 = new winthorDb.util.CustomTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        tblDataDetalheN4 = new winthorDb.util.CustomTable();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblDataDetalheN5 = new winthorDb.util.CustomTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblDataTreller = new winthorDb.util.CustomTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDetalhe = new winthorDb.util.CustomTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblHeader = new winthorDb.util.CustomTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSqlConsulta = new winthorDb.util.CustomTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblTreller = new winthorDb.util.CustomTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblDetalheN1 = new winthorDb.util.CustomTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblDetalheN2 = new winthorDb.util.CustomTable();
        jScrollPane18 = new javax.swing.JScrollPane();
        tblDetalheN3 = new winthorDb.util.CustomTable();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblDetalheN4 = new winthorDb.util.CustomTable();
        jScrollPane20 = new javax.swing.JScrollPane();
        tblDetalheN5 = new winthorDb.util.CustomTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        mnuRemessaNova.setText("Listar Remessa");
        mnuRemessaNova.setComponentPopupMenu(pMenuRemessa);
        pMenuRemessa.add(mnuRemessaNova);

        mnuRemessaBuscar.setText("Buscar Remessa");
        mnuRemessaBuscar.setComponentPopupMenu(pMenuRemessa);
        mnuRemessaBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRemessaBuscarActionPerformed(evt);
            }
        });
        pMenuRemessa.add(mnuRemessaBuscar);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Código");

        edtCodDoc.setEditable(false);
        edtCodDoc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtCodDocFocusLost(evt);
            }
        });

        edtTipoDoc.setEditable(false);
        edtTipoDoc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtTipoDocFocusLost(evt);
            }
        });

        jLabel2.setText("Tipo Documento");

        jLabel5.setText("Cod. Filial");

        edtCodFilial.setText("1");

        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/config.png"))); // NOI18N
        btnProcessar.setToolTipText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessarActionPerformed(evt);
            }
        });

        jLabel6.setText("Cod. Cliente");

        jLabel7.setText("Remessa");

        jLabel3.setText("até");

        jLabel4.setText("Periodo de Saida");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(edtCodDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtTipoDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtRemessa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtCodFilial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(edtCodCli)
                    .add(jLabel6))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(txtDataInicial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtDataFinal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnProcessar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                                .add(jLabel1)
                                .add(jLabel2))
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel7)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel6)
                                    .add(jLabel4))
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                            .add(txtDataInicial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3)
                            .add(txtDataFinal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(btnProcessar))
                    .add(org.jdesktop.layout.GroupLayout.CENTER, jPanel1Layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.CENTER, edtCodCli, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.CENTER, edtCodFilial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.CENTER, edtRemessa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.CENTER, edtTipoDoc)
                            .add(org.jdesktop.layout.GroupLayout.CENTER, edtCodDoc))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {edtCodCli, edtCodDoc, edtCodFilial, edtTipoDoc}, org.jdesktop.layout.GroupLayout.VERTICAL);

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnLayout.setText("Layuot");
        btnLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayoutActionPerformed(evt);
            }
        });

        edtValidar.setText("Validar");
        edtValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtValidarActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(btnLayout)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(edtValidar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnSalvar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancelar))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(btnCancelar)
                .add(btnSalvar)
                .add(btnLayout)
                .add(edtValidar))
        );

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(800, 400));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(800, 400));

        txtExportDados.setColumns(20);
        txtExportDados.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtExportDados.setRows(5);
        txtExportDados.setToolTipText("Consulta SQL para o HEADER");
        jScrollPane1.setViewportView(txtExportDados);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Export Dados", jPanel3);

        tblDataHeader.setToolTipText("");
        tblDataHeader.setCellSelectionEnabled(true);
        tblDataHeader.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane7.setViewportView(tblDataHeader);

        jTabbedPane2.addTab("Header", jScrollPane7);

        tblDataDetalhe.setToolTipText("");
        tblDataDetalhe.setCellSelectionEnabled(true);
        tblDataDetalhe.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane8.setViewportView(tblDataDetalhe);

        jTabbedPane3.addTab("Detalhe", jScrollPane8);

        tblDataDetalheN1.setToolTipText("");
        tblDataDetalheN1.setCellSelectionEnabled(true);
        tblDataDetalheN1.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane10.setViewportView(tblDataDetalheN1);

        jTabbedPane3.addTab("Detalhe Nivel 1", jScrollPane10);

        tblDataDetalheN2.setToolTipText("");
        tblDataDetalheN2.setCellSelectionEnabled(true);
        tblDataDetalheN2.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane11.setViewportView(tblDataDetalheN2);

        jTabbedPane3.addTab("Detalhe Nivel 2", jScrollPane11);

        tblDataDetalheN3.setToolTipText("");
        tblDataDetalheN3.setCellSelectionEnabled(true);
        tblDataDetalheN3.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane15.setViewportView(tblDataDetalheN3);

        jTabbedPane3.addTab("Detalhe Nivel 3", jScrollPane15);

        tblDataDetalheN4.setToolTipText("");
        tblDataDetalheN4.setCellSelectionEnabled(true);
        tblDataDetalheN4.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane16.setViewportView(tblDataDetalheN4);

        jTabbedPane3.addTab("Detalhe Nivel 4", jScrollPane16);

        tblDataDetalheN5.setToolTipText("");
        tblDataDetalheN5.setCellSelectionEnabled(true);
        tblDataDetalheN5.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane17.setViewportView(tblDataDetalheN5);

        jTabbedPane3.addTab("Detalhe Nivel 5", jScrollPane17);

        jTabbedPane2.addTab("Detalhe", jTabbedPane3);

        tblDataTreller.setToolTipText("");
        tblDataTreller.setCellSelectionEnabled(true);
        tblDataTreller.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane9.setViewportView(tblDataTreller);

        jTabbedPane2.addTab("Trealler", jScrollPane9);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dados", jPanel4);

        tblDetalhe.setToolTipText("");
        tblDetalhe.setCellSelectionEnabled(true);
        tblDetalhe.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane3.setViewportView(tblDetalhe);

        tblHeader.setToolTipText("");
        tblHeader.setCellSelectionEnabled(true);
        tblHeader.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane4.setViewportView(tblHeader);

        tblSqlConsulta.setToolTipText("");
        tblSqlConsulta.setCellSelectionEnabled(true);
        tblSqlConsulta.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane5.setViewportView(tblSqlConsulta);

        tblTreller.setToolTipText("");
        tblTreller.setCellSelectionEnabled(true);
        tblTreller.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane6.setViewportView(tblTreller);

        tblDetalheN1.setToolTipText("");
        tblDetalheN1.setCellSelectionEnabled(true);
        tblDetalheN1.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane12.setViewportView(tblDetalheN1);

        tblDetalheN2.setToolTipText("");
        tblDetalheN2.setCellSelectionEnabled(true);
        tblDetalheN2.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane13.setViewportView(tblDetalheN2);

        tblDetalheN3.setToolTipText("");
        tblDetalheN3.setCellSelectionEnabled(true);
        tblDetalheN3.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane18.setViewportView(tblDetalheN3);

        tblDetalheN4.setToolTipText("");
        tblDetalheN4.setCellSelectionEnabled(true);
        tblDetalheN4.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane19.setViewportView(tblDetalheN4);

        tblDetalheN5.setToolTipText("");
        tblDetalheN5.setCellSelectionEnabled(true);
        tblDetalheN5.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane20.setViewportView(tblDetalheN5);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane4)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jScrollPane12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                            .add(jScrollPane19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .add(jScrollPane20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Layouts", jPanel6);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Variaveis Filtros de dados para os comandos SQL:\n\n#FILTRO_DADOS_HEADER# - usar no select para filtar os dados\n#FILTRO_DADOS_DETALHE_N0# - usar no select para filtrar os dados detalhes principal\n#FILTRO_DADOS_DETALHE_N1# - Usar no select n1 para cada principal vai filtrar o N1\n#FILTRO_DADOS_DETALHE_N2# - Usar no select n2 para cada N1 vai filtrar o N2\n#FILTRO_DADOS_DETALHE_N3# - Usar no select n3 para cada N2 vai filtrar o N3\n#FILTRO_DADOS_DETALHE_N4# - Usar no select n4 para cada N3 vai filtrar o N4\n#FILTRO_DADOS_DETALHE_N5# - Usar no select n5 para cada N4 vai filtrar o N5\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\nTotalizadores:\n");
        jScrollPane14.setViewportView(jTextArea1);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ajuda", jPanel5);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        dispose();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

//        FileWriter file = null;
        try {
            String nomeFile = FileSystemView.getFileSystemView().getHomeDirectory() + "/" + edtTipoDoc.getText() + edtRemessa.getText() + ".txt";
            JFileChooser jfc = new JFileChooser();
            jfc.setSelectedFile(new File(nomeFile));
            //abre janela para pastas

            int ret = jfc.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                String val = jfc.getSelectedFile().getAbsolutePath();
                try (OutputStreamWriter bufferOut = new OutputStreamWriter(
                        new FileOutputStream(val), "UTF-8")) {
                    String fdata = txtExportDados.getText();

                    bufferOut.write(fdata);
                }
                MessageDialog.saveSucess();
                jfc = null;
            }

        } catch (HeadlessException | IOException ex) {
            trataErro.trataException(ex, "btnSalvarActionPerformed");
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnSalvarActionPerformed");
        }

//        dispose();

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void edtCodDocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCodDocFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodDocFocusLost

    private void edtTipoDocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtTipoDocFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_edtTipoDocFocusLost

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessarActionPerformed
        if ((tblHeader.getRowCount() > 0) && (tblDetalhe.getRowCount() > 0) && (tblTreller.getRowCount() > 0)) {
            exportaDados();
        }
    }//GEN-LAST:event_btnProcessarActionPerformed

    private void btnLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayoutActionPerformed
        LayoutRemessaDialog.open(9001, edtTipoDoc.getText());
        exibeSqlComando();
    }//GEN-LAST:event_btnLayoutActionPerformed

    private void edtValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtValidarActionPerformed
        if ((tblDetalhe.getRowCount() > 0) && (tblDetalhe.getRowCount() > 0) && (tblTreller.getRowCount() > 0)) {
            try {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                        while (br.ready()) {
                            String linha = br.readLine();
                            MessageDialog.info(linha.substring(0, 3));
                            if (linha.substring(0, 1).equalsIgnoreCase("000")) {
                                processaValidarExport("H", linha);
                            }
                            if (linha.substring(0, 3).equalsIgnoreCase("1")) {
                                processaValidarExport("D", linha);
                            }
                            if (linha.substring(0, 3).equalsIgnoreCase("1")) {
                                processaValidarExport("D1", linha);
                            }
                            if (linha.substring(0, 3).equalsIgnoreCase("1")) {
                                processaValidarExport("D2", linha);
                            }
                            if (linha.substring(0, 3).equalsIgnoreCase("1")) {
                                processaValidarExport("D3", linha);
                            }
                            if (linha.substring(0, 3).equalsIgnoreCase("1")) {
                                processaValidarExport("D4", linha);
                            }
                            if (linha.substring(0, 3).equalsIgnoreCase("1")) {
                                processaValidarExport("D5", linha);
                            }
                            if (linha.substring(0, 1).equalsIgnoreCase("9")) {
                                processaValidarExport("T", linha);
                            }
                        }
                    }
                }

            } catch (HeadlessException | IOException ex) {
                trataErro.trataException(ex, "edtValidarActionPerformed");
            }
        }
    }//GEN-LAST:event_edtValidarActionPerformed

    private void mnuRemessaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemessaBuscarActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_mnuRemessaBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExportDocDialog(new javax.swing.JFrame(), true, 0, null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnLayout;
    private javax.swing.JButton btnProcessar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JTextField edtCodCli;
    private javax.swing.JTextField edtCodDoc;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtRemessa;
    private javax.swing.JTextField edtTipoDoc;
    private javax.swing.JButton edtValidar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JMenuItem mnuRemessaBuscar;
    private javax.swing.JMenuItem mnuRemessaNova;
    private javax.swing.JPopupMenu pMenuRemessa;
    private winthorDb.util.CustomTable tblDataDetalhe;
    private winthorDb.util.CustomTable tblDataDetalheN1;
    private winthorDb.util.CustomTable tblDataDetalheN2;
    private winthorDb.util.CustomTable tblDataDetalheN3;
    private winthorDb.util.CustomTable tblDataDetalheN4;
    private winthorDb.util.CustomTable tblDataDetalheN5;
    private winthorDb.util.CustomTable tblDataHeader;
    private winthorDb.util.CustomTable tblDataTreller;
    private winthorDb.util.CustomTable tblDetalhe;
    private winthorDb.util.CustomTable tblDetalheN1;
    private winthorDb.util.CustomTable tblDetalheN2;
    private winthorDb.util.CustomTable tblDetalheN3;
    private winthorDb.util.CustomTable tblDetalheN4;
    private winthorDb.util.CustomTable tblDetalheN5;
    private winthorDb.util.CustomTable tblHeader;
    private winthorDb.util.CustomTable tblSqlConsulta;
    private winthorDb.util.CustomTable tblTreller;
    private com.toedter.calendar.JDateChooser txtDataFinal;
    private com.toedter.calendar.JDateChooser txtDataInicial;
    private javax.swing.JTextArea txtExportDados;
    // End of variables declaration//GEN-END:variables

}
