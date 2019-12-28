/*
 * ExportDocDialog.java
 *
 * Created on 22 de Outubro de 2018, 22:10
 */
package winthorDb.forms.export;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author Ageu Elias Rover
 */
public class ExportDocDialog extends javax.swing.JDialog {

    int vContaRegistroD = 0;
    int vContaRegistroH = 0;
    int vContaRegistroT = 0;
    int vContaRegistroG = 0;

    public static void open(int idDoc, String tipoDoc, String idFaturamento) {
        new ExportDocDialog(null, true, idDoc, tipoDoc, idFaturamento).setVisible(true);
    }

    /**
     * Creates new form CfopDialog
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

        exibeSqlComando();
    }

    private void exibeSqlComando() {
        String sqlHeader = "";
        String sqlDetalhe = "";
        String sqlTreller = "";
        String sqlComando = "";
        try {
            tblDetalhe.clearTableData();
            tblDetalhe.clearTableData();
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
                        + " ('' ||sql_Detalhe) as sql_Detalhe, "
                        + " ('' ||sql_Treller) as sql_Treller "
                        + " FROM LayoutDocSql"
                        + " WHERE idDoc = " + edtCodDoc.getText()
                        + " AND tipoDoc = '" + edtTipoDoc.getText() + "' ";

                tblHeader.setTableData(sqlHeader);
                tblDetalhe.setTableData(sqlDetalhe);
                tblTreller.setTableData(sqlTreller);
                tblSqlConsulta.setTableData(sqlComando);

            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "exibeSqlComando");
        }
    }

    private void exportaDados() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                try {
                    txtExportDados.setText("");
                    vContaRegistroH = 0;
                    vContaRegistroD = 0;
                    vContaRegistroG = 0;
                    vContaRegistroT = 0;

                    // Campos variaveis de tele para o Select   #NOVAS_TAGS#
                    String fields = "";
                    fields = " , " + edtCodIbgeCartorio + " as IBGE_Cartorio ";

                    // Exportacao do Header
                    String filtro_header = " AND F.codigo = '" + edtCodFilial.getText() + "' ";
                    filtro_header += " AND P.codcob = '" + edtCodCob.getText() + "' ";

                    if (!edtCodCli.getText().isEmpty()) {
                        filtro_header += " AND P.CODCLI = " + edtCodCli.getText();
                    }
                    if (!edtCodPraca.getText().isEmpty()) {
                        filtro_header += " AND C.CODPRACA = " + edtCodPraca.getText();
                    }
                    if (!edtCodSupervisor.getText().isEmpty()) {
                        filtro_header += " AND P.CODSUPERVISOR = " + edtCodSupervisor.getText();
                    }
                    if (!edtCodRca.getText().isEmpty()) {
                        filtro_header += " AND P.CODUSUR = " + edtCodRca.getText();
                    }
                    if (!edtDuplic.getText().isEmpty()) {
                        filtro_header += " AND P.DUPLIC IN ( " + edtDuplic.getText() + " )";
                    }
                    if (!edtPrest.getText().isEmpty()) {
                        filtro_header += " AND P.PREST IN ( " + edtPrest.getText() + " )";
                    }

                    if (cbIbgeCartorio.isSelected()) {
                        if (!edtCodIbgeCartorio.getText().isEmpty()) {
                            filtro_header += " AND C.CODCIDADE IN ( SELECT CODCIDADE FROM PCCIDADE WHERE REPLACE(CODIBGE, '.','') = '" + edtCodIbgeCartorio.getText() + "' )";
                        }
                    }
                    if (!edtDataDesd.getText().isEmpty()) {
                        filtro_header += " AND P.DTULTALTER =  to_date( '" + edtDataDesd.getText() + "', 'dd/mm/yyyy')";
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

                    String filtro_detalhe = " AND F.codigo = '" + edtCodFilial.getText() + "' ";
                    filtro_detalhe += " AND P.codcob = '" + edtCodCob.getText() + "' ";
                    if (!edtCodCli.getText().isEmpty()) {
                        filtro_detalhe += " AND P.CODCLI = " + edtCodCli.getText();
                    }

                    if (!edtCodPraca.getText().isEmpty()) {
                        filtro_detalhe += " AND C.CODPRACA = " + edtCodPraca.getText();
                    }
                    if (!edtCodSupervisor.getText().isEmpty()) {
                        filtro_detalhe += " AND P.CODSUPERVISOR = " + edtCodSupervisor.getText();
                    }
                    if (!edtCodRca.getText().isEmpty()) {
                        filtro_detalhe += " AND P.CODUSUR = " + edtCodRca.getText();
                    }
                    if (!edtDuplic.getText().isEmpty()) {
                        filtro_detalhe += " AND P.DUPLIC IN ( " + edtDuplic.getText() + " )";
                    }
                    if (!edtPrest.getText().isEmpty()) {
                        filtro_detalhe += " AND P.PREST IN ( " + edtPrest.getText() + " )";
                    }

                    if (cbIbgeCartorio.isSelected()) {
                        if (!edtCodIbgeCartorio.getText().isEmpty()) {
                            filtro_detalhe += " AND C.CODCIDADE IN ( SELECT CODCIDADE FROM PCCIDADE WHERE REPLACE(CODIBGE, '.','') = '" + edtCodIbgeCartorio.getText() + "' )";
                        }
                    }
                    if (!edtDataDesd.getText().isEmpty()) {
                        filtro_detalhe += " AND P.DTULTALTER =  to_date( '" + edtDataDesd.getText() + "', 'dd/mm/yyyy')";
                    }
                    // executar a consulta SQL para buscar os dados
                    String sqlDetalhe = tblSqlConsulta.getConteudoRowSelected("sql_Detalhe").toString().replaceAll("#FILTRO_DADOS_DETALHE#", filtro_detalhe);
                    sqlDetalhe = sqlDetalhe.replaceAll("#NOVAS_TAGS#", fields);

                    if (!sqlDetalhe.isEmpty()) {
                        tblDataDetalhe.clearTableData();
                        tblDataDetalhe.setTableData(sqlDetalhe);
                        if (tblDataDetalhe.getRowCount() >= 0) {
                            // inicia a montagem dos dados da linha de exportacao
                            processaExport("D");
                        }
                    }
                    // Exportacao do Treller
                    // Campos variaveis de tele para o Select   #NOVAS_TAGS#

                    fields = " ";

                    String filtro_treller = " AND F.codigo = '" + edtCodFilial.getText() + "' ";
                    filtro_treller += " AND P.codcob = '" + edtCodCob.getText() + "' ";
                    if (!edtCodCli.getText().isEmpty()) {
                        filtro_treller += " AND P.CODCLI = " + edtCodCli.getText();
                    }
                    if (!edtCodPraca.getText().isEmpty()) {
                        filtro_treller += " AND C.CODPRACA = " + edtCodPraca.getText();
                    }
                    if (!edtCodSupervisor.getText().isEmpty()) {
                        filtro_treller += " AND P.CODSUPERVISOR = " + edtCodSupervisor.getText();
                    }
                    if (!edtCodRca.getText().isEmpty()) {
                        filtro_treller += " AND P.CODUSUR = " + edtCodRca.getText();
                    }
                    if (!edtDuplic.getText().isEmpty()) {
                        filtro_treller += " AND P.DUPLIC IN ( " + edtDuplic.getText() + " )";
                    }
                    if (!edtPrest.getText().isEmpty()) {
                        filtro_treller += " AND P.PREST IN ( " + edtPrest.getText() + " )";
                    }
                    if (cbIbgeCartorio.isSelected()) {
                        if (!edtCodIbgeCartorio.getText().isEmpty()) {
                            filtro_treller += " AND C.CODCIDADE IN ( SELECT CODCIDADE FROM PCCIDADE WHERE REPLACE(CODIBGE, '.','') = '" + edtCodIbgeCartorio.getText() + "' )";
                        }
                    }
                    if (!edtDataDesd.getText().isEmpty()) {
                        filtro_treller += " AND P.DTULTALTER =  to_date( '" + edtDataDesd.getText() + "', 'dd/mm/yyyy')";
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

    private void gravaLogExport() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String sqlInsert = "";
        String sqlUpdate = "";
        try {
            wint.openConectOracle();
            for (int d = 0; d < tblDataDetalhe.getRowCount(); d++) {
                sqlInsert = " INSERT INTO BRZ_CARTORIO ( NUMREMESSA, CODCLI, DUPLIC, PREST, DTENVIO, CODCOB, NOSSONUMBCO ) VALUES ( "
                        + edtRemessa.getText() + " , \n"
                        + tblDataDetalhe.getConteudoRow("codcli", d).toString() + " ,\n"
                        + tblDataDetalhe.getConteudoRow("duplic", d).toString() + " ,\n"
                        + "'" + tblDataDetalhe.getConteudoRow("prest", d).toString() + "' ,\n"
                        + " SYSDATE ,\n"
                        + "'" + edtCodCob.getText() + "' ,\n"
                        + "'" + tblDataDetalhe.getConteudoRow("nossonumbco", d).toString() + "' )";

                wint.insertDados(sqlInsert);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "gravaLogExport");
        } finally {
            wint.closeConectOracle();
        }
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
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VIBGECARTORIO":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtCodIbgeCartorio.getText(), tamanho), tamanho);
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
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VIBGECARTORIO":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtCodIbgeCartorio.getText(), tamanho), tamanho);
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
                                        case "VCONTAREGISTROT":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroT, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROH":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroH, tamanho), tamanho);
                                            break;
                                        case "VCONTAREGISTROG":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + vContaRegistroG, tamanho), tamanho);
                                            break;
                                        case "VIBGECARTORIO":
                                            campo = Formato.strTamanhoExato(Formato.zerosEsquerda("" + edtCodIbgeCartorio.getText(), tamanho), tamanho);
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

    private String proximaRemssa(String cidadeIbge, String codConvenio) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String ret = "1";
        try {
            String strSelect = "SELECT MAX(NUMREMESSA) +1 REMESSA "
                    + "  FROM BRZ_CARTORIO_REMESSA \n"
                    + "   WHERE CODIBGE = " + cidadeIbge
                    + "   AND CODCONVENIO = " + codConvenio;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            // se nao existir o codibge para o convenio informado deve ser criado o registro na tabela
            if (lst != null) {
                if (!lst.isEmpty()) {
                    if (lst.get(0).toString().equalsIgnoreCase("[null]") ) {
                        wint.insertDados("INSERT INTO BRZ_CARTORIO_REMESSA ( CODIBGE,CODCONVENIO,NUMREMESSA,DTULTREMESSA) "
                                + " VALUES ( " + cidadeIbge + " , " + codConvenio + " , 1 , trunc(sysdate) ) ");
                    } else {
                        ret = lst.get(0).toString();
                    }
                }
            } else {
                wint.insertDados("INSERT INTO BRZ_CARTORIO_REMESSA ( CODIBGE,CODCONVENIO,NUMREMESSA,DTULTREMESSA) "
                        + " VALUES ( " + cidadeIbge + " , " + codConvenio + " , 1 , trunc(sysdate) ) ");
            }
            return ret;
        } catch (Exception ex) {
            trataErro.trataException(ex, "proximaRemssa");
            throw ex;
        } finally {
            wint.closeConectOracle();
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
        jLabel3 = new javax.swing.JLabel();
        edtCodCob = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        btnProcessar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        edtCodCli = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edtRemessa = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edtConvenio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        edtCodPraca = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtCodSupervisor = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        edtCodRca = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        edtDuplic = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        edtPrest = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        edtCodIbgeCartorio = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        edtDataDesd = new javax.swing.JTextField();
        cbIbgeCartorio = new javax.swing.JCheckBox();
        btnBuscarRemessa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnLayout = new javax.swing.JButton();
        edtValidar = new javax.swing.JButton();
        btnRemessaGerada = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtExportDados = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblDataDetalhe = new winthorDb.util.CustomTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblDataHeader = new winthorDb.util.CustomTable();
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

        jLabel1.setText("Código Doc.");

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

        jLabel2.setText("Tipo Doc.");

        jLabel3.setText("Cobrança");

        edtCodCob.setText("CRA");
        edtCodCob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodCobActionPerformed(evt);
            }
        });

        jLabel5.setText("Cod. Filial");

        edtCodFilial.setText("1");

        btnProcessar.setText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessarActionPerformed(evt);
            }
        });

        jLabel6.setText("Cod. Cliente");

        jLabel7.setText("Remessa");

        jLabel8.setText("Convenio");

        edtConvenio.setText("827");

        jLabel4.setText("Cod. Praca");

        jLabel9.setText("Cod. Superv.");

        jLabel10.setText("Cod. RCA");

        jLabel11.setText("Duplicata");

        jLabel12.setText("Prestacao");

        jLabel13.setText("IBGE Cartorio");

        edtCodIbgeCartorio.setText("1100205");

        jLabel14.setText("Data Desd.");

        edtDataDesd.setToolTipText("DD/MM/AAAA");

        btnBuscarRemessa.setText("buscar");
        btnBuscarRemessa.setComponentPopupMenu(pMenuRemessa);
        btnBuscarRemessa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarRemessaActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(edtCodDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(edtTipoDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(edtCodCob, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(edtCodFilial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(edtCodCli)
                            .add(jLabel6))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(edtCodPraca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(edtCodSupervisor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel10)
                            .add(edtCodRca, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel11)
                            .add(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(edtDuplic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(edtPrest, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel8)
                            .add(edtConvenio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel13)
                            .add(edtCodIbgeCartorio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnBuscarRemessa)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel7)
                            .add(edtRemessa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbIbgeCartorio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(edtDataDesd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel14))
                        .add(142, 142, 142))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(btnProcessar)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodDoc))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtTipoDoc))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodCob, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodFilial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodCli, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodPraca))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel9)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodSupervisor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel10)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtCodRca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel14)
                        .add(13, 13, 13)
                        .add(edtDataDesd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel11)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel12)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(edtPrest, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(edtDuplic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(edtConvenio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(edtCodIbgeCartorio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(edtRemessa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cbIbgeCartorio)))
                    .add(btnBuscarRemessa, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(btnProcessar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(1, 1, 1)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {edtCodCli, edtCodCob, edtCodDoc, edtCodFilial, edtCodPraca, edtCodRca, edtCodSupervisor, edtTipoDoc}, org.jdesktop.layout.GroupLayout.VERTICAL);

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

        btnRemessaGerada.setText("Geradas");
        btnRemessaGerada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemessaGeradaActionPerformed(evt);
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
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnRemessaGerada)
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
                .add(edtValidar)
                .add(btnRemessaGerada))
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
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Export Dados", jPanel3);

        tblDataDetalhe.setToolTipText("");
        tblDataDetalhe.setCellSelectionEnabled(true);
        tblDataDetalhe.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane8.setViewportView(tblDataDetalhe);

        jTabbedPane2.addTab("Detalhe", jScrollPane8);

        tblDataHeader.setToolTipText("");
        tblDataHeader.setCellSelectionEnabled(true);
        tblDataHeader.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane7.setViewportView(tblDataHeader);

        jTabbedPane2.addTab("Header", jScrollPane7);

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
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
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

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                    .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Layouts", jPanel6);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 408, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        dispose();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

//        FileWriter file = null;
        try {
            String nomeFile = FileSystemView.getFileSystemView().getHomeDirectory() + "/B" + edtConvenio.getText() + Formato.dateTimeNowToStr("ddMM.yy") + edtRemessa.getText();
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
                // grava o log no banco de dados
                gravaLogExport();
                MessageDialog.info("Log Salvo em :\n" + val);
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
        LayoutRemessaDialog.open(9999, "CART");
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
                            MessageDialog.info(linha.substring(0, 1));
                            if (linha.substring(0, 1).equalsIgnoreCase("0")) {
                                processaValidarExport("H", linha);
                            }
                            if (linha.substring(0, 1).equalsIgnoreCase("1")) {
                                processaValidarExport("D", linha);
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

    private void btnRemessaGeradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemessaGeradaActionPerformed
        // TODO add your handling code here:
        ConsultaRemessaGerada.open();
    }//GEN-LAST:event_btnRemessaGeradaActionPerformed

    private void edtCodCobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodCobActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodCobActionPerformed

    private void btnBuscarRemessaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarRemessaActionPerformed
        // TODO add your handling code here:
        try {
            if (!edtCodIbgeCartorio.getText().isEmpty()) {
                edtRemessa.setText(Formato.somenteNumeros(proximaRemssa(edtCodIbgeCartorio.getText(), edtConvenio.getText())));
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "busca remessa");
        }

    }//GEN-LAST:event_btnBuscarRemessaActionPerformed

    private void mnuRemessaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemessaBuscarActionPerformed
        // TODO add your handling code here:
        try {
            if (!edtCodIbgeCartorio.getText().isEmpty()) {
                  edtRemessa.setText(Formato.somenteNumeros(proximaRemssa(edtCodIbgeCartorio.getText(), edtConvenio.getText())));
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "busca remessa");
        }
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
    private javax.swing.JButton btnBuscarRemessa;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnLayout;
    private javax.swing.JButton btnProcessar;
    private javax.swing.JButton btnRemessaGerada;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JCheckBox cbIbgeCartorio;
    private javax.swing.JTextField edtCodCli;
    private javax.swing.JTextField edtCodCob;
    private javax.swing.JTextField edtCodDoc;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtCodIbgeCartorio;
    private javax.swing.JTextField edtCodPraca;
    private javax.swing.JTextField edtCodRca;
    private javax.swing.JTextField edtCodSupervisor;
    private javax.swing.JTextField edtConvenio;
    private javax.swing.JTextField edtDataDesd;
    private javax.swing.JTextField edtDuplic;
    private javax.swing.JTextField edtPrest;
    private javax.swing.JTextField edtRemessa;
    private javax.swing.JTextField edtTipoDoc;
    private javax.swing.JButton edtValidar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JMenuItem mnuRemessaBuscar;
    private javax.swing.JMenuItem mnuRemessaNova;
    private javax.swing.JPopupMenu pMenuRemessa;
    private winthorDb.util.CustomTable tblDataDetalhe;
    private winthorDb.util.CustomTable tblDataHeader;
    private winthorDb.util.CustomTable tblDataTreller;
    private winthorDb.util.CustomTable tblDetalhe;
    private winthorDb.util.CustomTable tblHeader;
    private winthorDb.util.CustomTable tblSqlConsulta;
    private winthorDb.util.CustomTable tblTreller;
    private javax.swing.JTextArea txtExportDados;
    // End of variables declaration//GEN-END:variables

}
