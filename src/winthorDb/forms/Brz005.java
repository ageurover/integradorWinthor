/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import winthorDb.Main;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz005 extends javax.swing.JFrame {

    int qtdCheque = 0;

    /**
     * Creates new form Brz001
     */
    public Brz005() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    /**
     * Faz a busca dos dados do produto no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private void buscaProdutoMaster(String codProd, String codFilial) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        IntegracaoWinthorDb wint2 = new IntegracaoWinthorDb();

        if (codProd.isEmpty()) {
            return;
        }
        try {

            String strSelect = " select pd.codprod, pd.descricao, pd.embalagem, "
                    + " nvl(pd.pesobruto,0) pesobruto, "
                    + " nvl(pd.pesoliq,0) pesoliq, "
                    + " nvl(pd.qtunit,0) qtunit, "
                    + " nvl(pd.qtunitcx,0) qtunitcx , "
                    + " nvl(es.qtestger,0) qtestger,  "
                    + " round(nvl(es.valorultent,0),6) valorultent, "
                    + " round(nvl(es.vlultpcompra,0),6) vlultpcompra, "
                    + " round(nvl(es.custoultent,0),6) custoultent,  "  
                    + " round(nvl(es.custoreal,0),6) custoreal, "
                    + " round(nvl(es.custorealliq,0),6) custorealliq,  "
                    + " round(nvl(es.custocont,0),6) custocont,  "
                    + " round(nvl(es.custofin,0),6) custofin,  "
                    + " round(nvl(es.custorep,0),6) custorep,  "
                    + " round(nvl(es.custorealsemst,0),6) custorealsemst , "
                    + " round(nvl(es.custofinsemst,0),6) custofinsemst,  "
                    + " round(nvl(es.custoultentfin,0),6) custoultentfin ,  "
                    + " round(nvl(es.custoultentliq,0),6) custoultentliq, "
                    + " round(nvl(es.vlultentcontsemst,0),6) vlultentcontsemst,  "
                    + " round(nvl(es.custoultentcont,0),6) custoultentcont, "
                    + " round(nvl(es.custoultentfinsemst,0),6) custoultentfinsemst, "
                    + " round(nvl(es.custodolar,0),6) custodolar, "
                    + " round(nvl(es.custoultpedcomprasemst,0),6) custoultpedcomprasemst, "
                    + " round(nvl(es.stbcr,0),6) stbcr,  "
                    + " round(nvl(es.basebcr,0),6) basebcr,  "
                    + " round(nvl(es.baseicmsultent,0),6) baseicmsultent,  "
                    + " '' as endfield  "
                    + " from pcprodut pd, pcest es  "
                    + " where pd.codprod = es.codprod  "
                    + " and es.codfilial = " + codFilial
                    + " and pd.codprod = " + codProd;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            if (lst.isEmpty()) {
                MessageDialog.error("Produto Master não localizado!");
            }
            String[] field;

            for (Object lst1 : lst) {
                field = lst1.toString().split(",", -1);

                edtMstDesricao.setText(field[1]);
                edtMstEmbalagem.setText(field[2]);
                edtMstQtdUndMaster.setText(field[3]);
                edtMstQtdUndVenda.setText(field[4]);
                edtMstPesoBruto.setText(field[5]);
                edtMstPesoLiquido.setText(field[6]);
                edtMstQtdEst.setText(field[7]);

                edtMstVlrUltEnt.setText(field[8]);
                edtMstCustoUltEnt.setText(field[10]);
                edtCnvFator.setText("1");

            }

            lst = null;

            wint2.openConectOracle();
            tblCustoMaster.clearTableData();
            tblCustoMaster.setTableData(wint2.selectResultSet(strSelect));
            if (tblCustoMaster.getRowCount() > 0) {
                tblCustoMaster.highlightLastRow(0);
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaProdutoMaster");
        } finally {
            wint.closeConectOracle();
            //wint2.closeConectOracle();
        }

    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private void buscaProdutoDestino(String codProd, String codFilial) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        if (codProd.isEmpty()) {
            return;
        }
        try {
            String strSelect = "select pd.codprod, pd.descricao, pd.embalagem, nvl(pd.qtunitcx,1) as qtunitcx, Nvl(pd.qtunit,1) as qtunit, "
                    + "nvl(pd.pesobruto,0) pesobruto, nvl(pd.pesoliq,0) pesoliq , nvl(es.valorultent,0) valorultent, "
                    + "nvl(es.custoultent,0) custoultent, nvl(es.custoreal,0) custoreal, nvl(es.qtestger,0) qtestger "
                    + "from pcprodut pd, pcest es "
                    + "where pd.codprod = es.codprod "
                    + "and es.codfilial =  " + codFilial
                    + "and pd.codprod = " + codProd;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            if (lst.isEmpty()) {
                MessageDialog.error("Produto Master não localizado!");
            }
            String[] field;

            for (Object lst1 : lst) {
                field = lst1.toString().split(",", -1);

                edtDstDescricao.setText(field[1]);
                edtDstEmbalagem.setText(field[2]);
                edtDstQtdUndMaster.setText(field[3]);
                edtDstUndVenda.setText(field[4]);
                edtDstPesoBruto.setText(field[5]);
                edtDstVlrUltEnt.setText(field[7]);
                edtDstCustoUltEnt.setText(field[8]);
                edtDstCustoReal.setText(field[9]);
                edtDstQtdEst.setText(Formato.somenteNumeros(field[10]));

            }

            lst = null;

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaProdutoDestino");
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * Faz a transferencia dos custos do produto master para o produto destino
     * proporcionalmente ao fator de conversao.
     *
     */
    private void transfereProdutoMasterDestino() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String StrUpd = "";
        String strFilial = "";
        try {
            // valida a lista de filial para atualizar os custos
            strFilial += strFilial.isEmpty() ? (chkFilial_1.isSelected() ? "'1'" : "") : (chkFilial_1.isSelected() ? ",'1'" : "");
            strFilial += strFilial.isEmpty() ? (chkFilial_2.isSelected() ? "'2'" : "") : (chkFilial_2.isSelected() ? ",'2'" : "");
            strFilial += strFilial.isEmpty() ? (chkFilial_3.isSelected() ? "'3'" : "") : (chkFilial_3.isSelected() ? ",'3'" : "");
            strFilial += strFilial.isEmpty() ? (chkFilial_4.isSelected() ? "'4'" : "") : (chkFilial_3.isSelected() ? ",'4'" : "");
            strFilial += strFilial.isEmpty() ? (chkFilial_5.isSelected() ? "'5'" : "") : (chkFilial_5.isSelected() ? ",'5'" : "");
            strFilial += strFilial.isEmpty() ? (chkFilial_6.isSelected() ? "'6'" : "") : (chkFilial_6.isSelected() ? ",'6'" : "");

            // prepara o comando para fazer a atualizacao no banco de dados
//            StrUpd = "UPDATE PCPRODUTO SET PESOLIQ = " + edtCnvPesoLiquido.getText();
//            StrUpd += ", PESOBRUTO = " + edtCnvPesoBruto.getText();
//            StrUpd += " WHERE CODPROD = " + edtDstCodProduto.getText() + "; ";
            StrUpd += "UPDATE PCEST SET "
                    + "  valorultent = " + edtCnvVlrUltEnt.getText().replace(',', '.')
                    + " ,custoultent = " + edtCnvCustoUltEnt.getText().replace(',', '.')
                    + " ,custoreal = " + edtCnvCustoReal.getText().replace(',', '.')
                    + " ,custocont = " + edtCnvCustoCont.getText().replace(',', '.')
                    + " ,custofin = " + edtCnvCustoFin.getText().replace(',', '.')
                    + " ,custorep = " + edtCnvCustoRep.getText().replace(',', '.')
                    + " ,custorealsemst = " + edtCnvCustoRealSemSt.getText().replace(',', '.')
                    + " ,custofinsemst = " + edtCnvCustoFinSemSt.getText().replace(',', '.')
                    + " ,custoultentfin = " + edtCnvCustoUltEntFin.getText().replace(',', '.')
                    + " ,vlultentcontsemst = " + edtCnvCustoUltEntContSemSt.getText().replace(',', '.')
                    + " ,vlultpcompra = " + edtCnvVlrUltCompra.getText().replace(',', '.')
                    + " ,custorealliq = " + edtCnvCustoRealLiq.getText().replace(',', '.')
                    + " ,custoultentliq = " + edtCnvCustoUltEntLiq.getText().replace(',', '.')
                    + " ,custoultentcont = " + edtCnvCustoUltEntCont.getText().replace(',', '.')
                    + " ,custoultentfinsemst = " + edtCnvCustoUltEntFinSemSt.getText().replace(',', '.')
                    + " ,custodolar = " + edtCnvCustoDolar.getText().replace(',', '.')
                    + " ,custoultpedcomprasemst = " + edtCnvCustoUltPedCompraSemSt.getText().replace(',', '.')
                    + " ,stbcr =" + edtCnvCustoStBcr.getText().replace(',', '.')
                    + " ,basebcr = " + edtCnvCustoBaseBcr.getText().replace(',', '.')
                    + " ,baseicmsultent = " + edtCnvCustoBaseIcmsUltEnt.getText().replace(',', '.')
                    + " WHERE CODFILIAL in ( " + strFilial + ") "
                    + " AND CODPROD = " + edtDstCodProduto.getText();

            wint.openConectOracle();
            wint.updateDados(StrUpd);

            geraLog();
            MessageDialog.info("Dados do produto destino alterados com sucesso!");

            limpaConversao();
            limpaDestino();
            limpaOrigem();

        } catch (Exception ex) {
            trataErro.trataException(ex, "transfereProdutoMasterDestino");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }

    }
    /*
     * Gera o logo dos dados gravados em arquivo padrao XML
     */

    private void geraLog() {
        FileWriter arqLog = null;
        String strLog = "";
        try {
            // verifica se ja existe arquivo de log para o dia desejado
            // Validar a Estrutura de pastas da aplicação
            File file = new File(Main.dirAplication + "/log/log_" + Formato.dateBdToStr(new Date()) + ".log");
            if (file.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader lerArq = new BufferedReader(fr);
                while (lerArq.ready()) {
                    strLog += lerArq.readLine() + "\n";
                }
                lerArq = null;
                fr = null;
            } else {
                strLog = "Data/hora;Usuario;Origem;MstEmbalagem;MstQtdUndMaster;MstQtdUndVenda;MstPesoBruto;MstPesoLiquido;MstQtdEst;MstVlrUltEnt;"
                        + "MstCustoUltEnt;MstCustoReal;MstCustoCont;MstCustoFin;MstCustoRep;MstCustoRealSemSt;MstCustoFinSemSt;MstCustoUltEntFin;MstCustoUltEntContSemSt;"
                        + "MstCustoStBcr;MstCustoBaseBcr;MstCustoBaseIcmsUltEnt;Produto Destino;DstEmbalagem;DstQtdUndMaster;DstUndVenda;DstPesoBruto;"
                        + "DstVlrUltEnt;DstCustoUltEnt;DstCustoReal;DstQtdEst;Fator Conversao;CnvPesoLiquido;CnvPesoBruto;CnvVlrUltEnt;CnvCustoUltEnt;CnvCustoReal;"
                        + "CnvCustoCont;CnvCustoFin;CnvCustoRep;CnvCustoRealSemSt;CnvCustoFinSemSt;CnvCustoUltEntFin;CnvCustoUltEntContSemSt;CnvCustoStBcr;CnvCustoBaseBcr;"
                        + "CnvCustoBaseBcr;CnvCustoBaseIcmsUltEnt\n";
            }
            file = null;

            // DADOS ADICIONAIS
            strLog += new Date() + ";"
                    + Main.Usuario + ";";

            // PRODUTO ORIGEM
            strLog += edtMstDesricao.getText() + ";"
                    + edtMstEmbalagem.getText() + ";"
                    + edtMstQtdUndMaster.getText() + ";"
                    + edtMstQtdUndVenda.getText() + ";"
                    + edtMstPesoBruto.getText() + ";"
                    + edtMstPesoLiquido.getText() + ";"
                    + edtMstQtdEst.getText() + ";"
                    + edtMstVlrUltEnt.getText() + ";"
                    + edtMstCustoUltEnt.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custoreal") + ";" //edtMstCustoReal.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custocont") + ";" //edtMstCustoCont.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custofin") + ";" //edtMstCustoFin.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custorep") + ";" //edtMstCustoRep.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custorealsemst") + ";" //edtMstCustoRealSemSt.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custoultentfinsemst") + ";" //edtMstCustoFinSemSt.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("custoultentfin") + ";" //edtMstCustoUltEntFin.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("vlultentcontsemst") + ";" //edtMstCustoUltEntContSemSt.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("stbcr") + ";" //edtMstCustoStBcr.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("basebcr") + ";" //edtMstCustoBaseBcr.getText() + ";"
                    + tblCustoMaster.getConteudoRowSelected("baseicmsultent") + ";"; //edtMstCustoBaseIcmsUltEnt.getText() + ";";
            // PRODUTO DESTINO
            strLog += edtDstDescricao.getText() + ";"
                    + edtDstEmbalagem.getText() + ";"
                    + edtDstQtdUndMaster.getText() + ";"
                    + edtDstUndVenda.getText() + ";"
                    + edtDstPesoBruto.getText() + ";"
                    + edtDstVlrUltEnt.getText() + ";"
                    + edtDstCustoUltEnt.getText() + ";"
                    + edtDstCustoReal.getText() + ";"
                    + edtDstQtdEst.getText() + ";";
            // CONVERSAO
            strLog += edtCnvFator.getText() + ";"
                    + edtCnvPesoLiquido.getText() + ";"
                    + edtCnvPesoBruto.getText() + ";"
                    + edtCnvVlrUltEnt.getText() + ";"
                    + edtCnvCustoUltEnt.getText() + ";"
                    + edtCnvCustoReal.getText() + ";"
                    + edtCnvCustoCont.getText() + ";"
                    + edtCnvCustoFin.getText() + ";"
                    + edtCnvCustoRep.getText() + ";"
                    + edtCnvCustoRealSemSt.getText() + ";"
                    + edtCnvCustoFinSemSt.getText() + ";"
                    + edtCnvCustoUltEntFin.getText() + ";"
                    + edtCnvCustoUltEntContSemSt.getText() + ";"
                    + edtCnvCustoStBcr.getText() + ";"
                    + edtCnvCustoBaseBcr.getText() + ";"
                    + edtCnvCustoBaseBcr.getText() + ";"
                    + edtCnvCustoBaseIcmsUltEnt.getText() + "\n";

            arqLog = new FileWriter(new File(Main.dirAplication + "/log/log_" + Formato.dateBdToStr(new Date()) + ".log"));
            arqLog.append(strLog);
            arqLog.flush();
            arqLog = null;
        } catch (Exception ex) {
            trataErro.trataException(ex, "geraLog");
        }
    }
    /*
     * Limpa os campos de conversao para forcar o usuario a calcular os dados de conversao
     */

    private void limpaConversao() {
        try {
            edtCnvPesoLiquido.setText("0.00");
            edtCnvPesoBruto.setText("0.00");
            edtCnvVlrUltEnt.setText("0.00");
            edtCnvCustoUltEnt.setText("0.00");
            edtCnvCustoReal.setText("0.00");
            edtCnvCustoCont.setText("0.00");
            edtCnvCustoFin.setText("0.00");
            edtCnvCustoRep.setText("0.00");
            edtCnvCustoRealSemSt.setText("0.00");
            edtCnvCustoFinSemSt.setText("0.00");
            edtCnvCustoUltEntFin.setText("0.00");
            edtCnvCustoUltEntContSemSt.setText("0.00");
            edtCnvCustoStBcr.setText("0.00");
            edtCnvCustoBaseBcr.setText("0.00");
            edtCnvCustoBaseBcr.setText("0.00");
            edtCnvCustoBaseIcmsUltEnt.setText("0.00");
            edtCnvEstoque.setText("0.00");

        } catch (Exception ex) {
            trataErro.trataException(ex, "limpaConversao");
        }
    }

    /*
     * Limpa os campos da aba produto destino
     */
    private void limpaDestino() {
        try {
            edtDstCodProduto.setText("");
            edtDstDescricao.setText("");
            edtDstEmbalagem.setText("");
            edtDstCustoReal.setText("0.00");
            edtDstCustoUltEnt.setText("0.00");
            edtDstPesoBruto.setText("0.00");
            edtDstQtdEst.setText("0.00");
            edtDstQtdUndMaster.setText("0.00");
            edtDstUndVenda.setText("0.00");
            edtDstVlrUltEnt.setText("0.00");

        } catch (Exception ex) {
            trataErro.trataException(ex, "limpaDestino");
        }
    }

    /*
     * Limpa os campos da aba de produtos origem
     */
    private void limpaOrigem() {
        try {
            edtMstCodProd.setText("");
            edtMstDesricao.setText("");
            edtMstEmbalagem.setText("");
            edtMstCustoUltEnt.setText("0.00");
            edtMstPesoBruto.setText("0.00");
            edtMstPesoLiquido.setText("0.00");
            edtMstQtdEst.setText("0.00");
            edtMstQtdUndMaster.setText("0.00");
            edtMstQtdUndVenda.setText("0.00");
            edtMstVlrUltEnt.setText("0.00");
            tblCustoMaster.clearTableData();

        } catch (Exception ex) {
            trataErro.trataException(ex, "limpaOrigem");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel25 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        edtDstCodProduto = new javax.swing.JTextField();
        edtDstDescricao = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        edtDstEmbalagem = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        edtDstQtdUndMaster = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        edtDstUndVenda = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        edtDstQtdEst = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        edtDstVlrUltEnt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        edtDstCustoUltEnt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        edtDstCustoReal = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        edtDstPesoBruto = new javax.swing.JTextField();
        btnDstBuscar = new javax.swing.JButton();
        edtGravar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        edtMstCodProd = new javax.swing.JTextField();
        edtMstDesricao = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edtMstEmbalagem = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edtMstQtdUndMaster = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edtMstQtdUndVenda = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtMstQtdEst = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        edtMstPesoBruto = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        edtMstPesoLiquido = new javax.swing.JTextField();
        btnMstBuscar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        edtMstVlrUltEnt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        edtMstCustoUltEnt = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCustoMaster = new winthorDb.util.CustomTable();
        chkFilial_1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        chkFilial_2 = new javax.swing.JCheckBox();
        chkFilial_3 = new javax.swing.JCheckBox();
        chkFilial_5 = new javax.swing.JCheckBox();
        chkFilial_6 = new javax.swing.JCheckBox();
        jLabel34 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        chkFilial_4 = new javax.swing.JCheckBox();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        edtCnvFator = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        edtCnvEstoque = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        edtCnvPesoLiquido = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        edtCnvPesoBruto = new javax.swing.JTextField();
        edtCnvCalcular = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        edtCnvVlrUltEnt = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        edtCnvCustoUltEnt = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        edtCnvCustoReal = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        edtCnvCustoFin = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        edtCnvCustoCont = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        edtCnvCustoRep = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        edtCnvCustoRealSemSt = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        edtCnvCustoFinSemSt = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        edtCnvCustoUltEntFin = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        edtCnvCustoUltEntContSemSt = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        edtCnvCustoStBcr = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        edtCnvCustoBaseBcr = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        edtCnvCustoBaseIcmsUltEnt = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        edtCnvVlrUltCompra = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        edtCnvCustoRealLiq = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        edtCnvCustoUltEntLiq = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        edtCnvVlrUltEntContSemSt = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        edtCnvCustoUltEntCont = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        edtCnvCustoUltPedCompraSemSt = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        edtCnvCustoDolar = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        edtCnvCustoUltEntFinSemSt = new javax.swing.JTextField();

        jLabel25.setText("jLabel25");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz005");

        jLabel1.setText("Converção de produtos Master/Unidade");
        jLabel1.setToolTipText("");

        jLabel3.setText("* * Este processo só deve ser realizado com ordem da gerencia de logistica");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados dos Produtos"));

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Produto Destino (Unidade)"));

        jLabel15.setText("Cod. Produto:");

        edtDstCodProduto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtDstCodProdutoFocusLost(evt);
            }
        });

        edtDstDescricao.setEditable(false);
        edtDstDescricao.setFocusable(false);

        jLabel17.setText("Embalagem");

        edtDstEmbalagem.setEditable(false);
        edtDstEmbalagem.setFocusable(false);

        jLabel18.setText("Qtd und. Master");

        edtDstQtdUndMaster.setEditable(false);
        edtDstQtdUndMaster.setFocusable(false);

        jLabel19.setText("Und. Venda");

        edtDstUndVenda.setEditable(false);
        edtDstUndVenda.setFocusable(false);

        jLabel20.setText("Estoque Atual:");

        edtDstQtdEst.setEditable(false);
        edtDstQtdEst.setFocusable(false);

        jLabel21.setText("Vrl Ult. Ent:");

        edtDstVlrUltEnt.setEditable(false);
        edtDstVlrUltEnt.setFocusable(false);

        jLabel22.setText("Custo Ult. Ent");

        edtDstCustoUltEnt.setEditable(false);
        edtDstCustoUltEnt.setFocusable(false);

        jLabel23.setText("Custo Real:");

        edtDstCustoReal.setEditable(false);
        edtDstCustoReal.setFocusable(false);

        jLabel24.setText("Peso Bruto");

        edtDstPesoBruto.setEditable(false);
        edtDstPesoBruto.setFocusable(false);

        btnDstBuscar.setText("...");
        btnDstBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDstBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edtDstCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDstBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtDstDescricao))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstEmbalagem, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstQtdUndMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstUndVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstQtdEst, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstVlrUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstCustoUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstCustoReal, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtDstPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(edtDstCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDstBuscar)
                    .addComponent(edtDstDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(edtDstEmbalagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(edtDstQtdUndMaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(edtDstUndVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(edtDstQtdEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(edtDstVlrUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(edtDstCustoUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(edtDstCustoReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(edtDstPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        edtGravar.setText("Gravar");
        edtGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtGravarActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Produto Master (Origem)"));

        jLabel4.setText("Cod. Produto:");

        edtMstCodProd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtMstCodProdFocusLost(evt);
            }
        });

        edtMstDesricao.setEditable(false);
        edtMstDesricao.setFocusable(false);

        jLabel6.setText("Embalagem:");

        edtMstEmbalagem.setEditable(false);
        edtMstEmbalagem.setFocusable(false);

        jLabel7.setText("Qtd und. Master:");

        edtMstQtdUndMaster.setEditable(false);
        edtMstQtdUndMaster.setFocusable(false);

        jLabel8.setText("Und. Venda:");

        edtMstQtdUndVenda.setEditable(false);
        edtMstQtdUndVenda.setFocusable(false);

        jLabel9.setText("Estoque:");

        edtMstQtdEst.setEditable(false);
        edtMstQtdEst.setFocusable(false);

        jLabel13.setText("Peso Bruto:");

        edtMstPesoBruto.setEditable(false);
        edtMstPesoBruto.setFocusable(false);

        jLabel14.setText("Peso Liquido:");

        edtMstPesoLiquido.setEditable(false);
        edtMstPesoLiquido.setFocusable(false);

        btnMstBuscar.setText("...");
        btnMstBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMstBuscarActionPerformed(evt);
            }
        });

        jLabel10.setText("Vlr Ult Ent");

        edtMstVlrUltEnt.setEditable(false);
        edtMstVlrUltEnt.setText(" ");
        edtMstVlrUltEnt.setFocusable(false);

        jLabel11.setText("Custo Ult Ent");

        edtMstCustoUltEnt.setEditable(false);
        edtMstCustoUltEnt.setFocusable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtMstCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMstBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtMstDesricao))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstVlrUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstCustoUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstEmbalagem, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstQtdUndMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstQtdUndVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtMstQtdEst, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtMstCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMstBuscar)
                    .addComponent(edtMstDesricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(edtMstEmbalagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(edtMstQtdUndMaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(edtMstQtdUndVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(edtMstQtdEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(edtMstPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(edtMstPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(edtMstVlrUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(jLabel11)
                        .addComponent(edtMstCustoUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Produto Master", jPanel3);

        tblCustoMaster.setToolTipText("");
        tblCustoMaster.setCellSelectionEnabled(true);
        jScrollPane3.setViewportView(tblCustoMaster);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Custos", jPanel6);

        chkFilial_1.setSelected(true);
        chkFilial_1.setText("Filial 1");
        chkFilial_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFilial_1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Filiais:");

        chkFilial_2.setSelected(true);
        chkFilial_2.setText("Filial 2");

        chkFilial_3.setSelected(true);
        chkFilial_3.setText("Filial 3");

        chkFilial_5.setSelected(true);
        chkFilial_5.setText("Filial 5");

        chkFilial_6.setSelected(true);
        chkFilial_6.setText("Filial 6");

        jLabel34.setText("Filial Base:");

        edtCodFilial.setText("1");

        chkFilial_4.setSelected(true);
        chkFilial_4.setText("Filial 4");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Conversão de Custos"));

        jLabel27.setText("Fator de Conversao:");

        edtCnvFator.setText("1");

        jLabel31.setText("Novo Estoque:");

        edtCnvEstoque.setEditable(false);
        edtCnvEstoque.setFocusable(false);

        jLabel32.setText("Peso Liquido:");

        edtCnvPesoLiquido.setEditable(false);
        edtCnvPesoLiquido.setFocusable(false);

        jLabel33.setText("Peso Bruto:");

        edtCnvPesoBruto.setEditable(false);
        edtCnvPesoBruto.setFocusable(false);

        edtCnvCalcular.setBackground(new java.awt.Color(255, 0, 102));
        edtCnvCalcular.setText("Calcular");
        edtCnvCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCnvCalcularActionPerformed(evt);
            }
        });

        jLabel45.setText("Vlr Ult Ent");

        edtCnvVlrUltEnt.setEditable(false);
        edtCnvVlrUltEnt.setText(" ");
        edtCnvVlrUltEnt.setFocusable(false);

        jLabel46.setText("Custo Ult Ent");

        edtCnvCustoUltEnt.setEditable(false);
        edtCnvCustoUltEnt.setFocusable(false);

        jLabel47.setText("Custo Real");

        edtCnvCustoReal.setEditable(false);
        edtCnvCustoReal.setFocusable(false);

        jLabel48.setText("Custo Financ.");

        edtCnvCustoFin.setEditable(false);
        edtCnvCustoFin.setText(" ");
        edtCnvCustoFin.setFocusable(false);

        jLabel49.setText("Custo Contabil");

        edtCnvCustoCont.setEditable(false);
        edtCnvCustoCont.setFocusable(false);

        jLabel50.setText("Custo Rep.");

        edtCnvCustoRep.setEditable(false);
        edtCnvCustoRep.setFocusable(false);

        jLabel51.setText("Custo Real sem ST");

        edtCnvCustoRealSemSt.setEditable(false);
        edtCnvCustoRealSemSt.setFocusable(false);

        jLabel52.setText("Custo Fin -ST");

        edtCnvCustoFinSemSt.setEditable(false);
        edtCnvCustoFinSemSt.setText(" ");
        edtCnvCustoFinSemSt.setFocusable(false);

        jLabel53.setText("Cst Ult Ent Fin");

        edtCnvCustoUltEntFin.setEditable(false);
        edtCnvCustoUltEntFin.setFocusable(false);

        jLabel54.setText("Cst Ult Ent Cont -ST");

        edtCnvCustoUltEntContSemSt.setEditable(false);
        edtCnvCustoUltEntContSemSt.setFocusable(false);

        jLabel55.setText("ST Bcr");

        edtCnvCustoStBcr.setEditable(false);
        edtCnvCustoStBcr.setText(" ");
        edtCnvCustoStBcr.setFocusable(false);

        jLabel56.setText("Base Bcr");

        edtCnvCustoBaseBcr.setEditable(false);
        edtCnvCustoBaseBcr.setFocusable(false);

        jLabel57.setText("Base Icms Ult Ent");

        edtCnvCustoBaseIcmsUltEnt.setEditable(false);
        edtCnvCustoBaseIcmsUltEnt.setFocusable(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvFator, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvCalcular)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvVlrUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addComponent(edtCnvCustoReal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoFin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoCont, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50)
                            .addComponent(edtCnvCustoRep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvCustoRealSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoFinSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoUltEntFin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvCustoUltEntContSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoStBcr, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoBaseBcr, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addComponent(edtCnvCustoBaseIcmsUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(edtCnvFator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtCnvCalcular)
                    .addComponent(jLabel31)
                    .addComponent(edtCnvEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(edtCnvPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(edtCnvPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvCustoRealSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(jLabel49)
                            .addComponent(jLabel50))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edtCnvCustoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edtCnvVlrUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(jLabel56)
                            .addComponent(jLabel57))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edtCnvCustoStBcr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoBaseBcr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoBaseIcmsUltEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel52)
                            .addComponent(jLabel53)
                            .addComponent(jLabel54))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edtCnvCustoFinSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoUltEntFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCnvCustoUltEntContSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Custos base (prod. destino)", jPanel5);

        jLabel58.setText("Vlr.Ult.Compra");

        edtCnvVlrUltCompra.setEditable(false);
        edtCnvVlrUltCompra.setText(" ");
        edtCnvVlrUltCompra.setFocusable(false);

        jLabel59.setText("Custo Real Liq");

        edtCnvCustoRealLiq.setEditable(false);
        edtCnvCustoRealLiq.setText(" ");
        edtCnvCustoRealLiq.setFocusable(false);

        jLabel61.setText("Custo Ult Ent Liq");

        edtCnvCustoUltEntLiq.setEditable(false);
        edtCnvCustoUltEntLiq.setText(" ");
        edtCnvCustoUltEntLiq.setFocusable(false);

        jLabel62.setText("VlrUltEntContSemSt");

        edtCnvVlrUltEntContSemSt.setEditable(false);
        edtCnvVlrUltEntContSemSt.setText(" ");
        edtCnvVlrUltEntContSemSt.setFocusable(false);

        jLabel63.setText("CustoUltEntCont");

        edtCnvCustoUltEntCont.setEditable(false);
        edtCnvCustoUltEntCont.setText(" ");
        edtCnvCustoUltEntCont.setFocusable(false);

        jLabel64.setText("CustoUltPedCompraSemSt");

        edtCnvCustoUltPedCompraSemSt.setEditable(false);
        edtCnvCustoUltPedCompraSemSt.setText(" ");
        edtCnvCustoUltPedCompraSemSt.setFocusable(false);

        jLabel65.setText("Custo Dolar");

        edtCnvCustoDolar.setEditable(false);
        edtCnvCustoDolar.setText(" ");
        edtCnvCustoDolar.setFocusable(false);

        jLabel66.setText("custoUltEntFinSemSt");

        edtCnvCustoUltEntFinSemSt.setEditable(false);
        edtCnvCustoUltEntFinSemSt.setText(" ");
        edtCnvCustoUltEntFinSemSt.setFocusable(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvVlrUltCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel58))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoRealLiq, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel59))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvCustoUltEntLiq))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvVlrUltEntContSemSt))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvCustoUltEntCont))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtCnvCustoDolar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvCustoUltPedCompraSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edtCnvCustoUltEntFinSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvCustoUltEntCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel62)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtCnvVlrUltEntContSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel61)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtCnvCustoUltEntLiq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel59)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtCnvCustoRealLiq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel58)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtCnvVlrUltCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvCustoDolar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvCustoUltPedCompraSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel66)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCnvCustoUltEntFinSemSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Custo Complementa (prod. destino)", jPanel8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFilial_1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFilial_2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFilial_3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFilial_4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFilial_5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFilial_6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(edtGravar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar))
                    .addComponent(jTabbedPane2))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtGravar)
                    .addComponent(btnFechar)
                    .addComponent(chkFilial_1)
                    .addComponent(jLabel5)
                    .addComponent(chkFilial_2)
                    .addComponent(chkFilial_3)
                    .addComponent(chkFilial_5)
                    .addComponent(chkFilial_6)
                    .addComponent(jLabel34)
                    .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkFilial_4))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnMstBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMstBuscarActionPerformed
        try {
            buscaProdutoMaster(edtMstCodProd.getText(), edtCodFilial.getText());
            limpaConversao();
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnMstBuscarActionPerformed");
        }
    }//GEN-LAST:event_btnMstBuscarActionPerformed

    private void btnDstBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDstBuscarActionPerformed
        try {
            buscaProdutoDestino(edtDstCodProduto.getText(), edtCodFilial.getText());
            limpaConversao();
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnDstBuscarActionPerformed");
        }
    }//GEN-LAST:event_btnDstBuscarActionPerformed

    private void edtCnvCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCnvCalcularActionPerformed
        try {
            limpaConversao();
            if (edtMstCodProd.getText().isEmpty()) {
                MessageDialog.error("Procedimento abortado!\nProduto Master não informado!");
                return;
            }
            if (edtDstCodProduto.getText().isEmpty()) {
                MessageDialog.error("Procedimento abortado!\nProduto Destino não informado!");
                return;
            }
            if (edtCnvFator.getText().equalsIgnoreCase("1")) {
                MessageDialog.error("Procedimento abortado!\nO fator de conversao deve ser maior que 1!");
                return;
            }
            double fatorConv = Double.parseDouble(edtCnvFator.getText());
            double vlrUltEnt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("valorultent").toString());
            double custoUltEnt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoultent").toString());
            double custoReal = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoreal").toString());
            double custoRealSemSt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custorealsemst").toString());
            double custoCont = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custocont").toString());
            double custoFin = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custofin").toString());
            double custoRep = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custorep").toString());
            double custoFinSt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custorealsemst").toString());
            double custoUltEntFinSemSt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoultentfinsemst").toString());
            double custoUltEntFin = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoultentfin").toString());
            double custoUltEntContSemSt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("vlultentcontsemst").toString());
            double custoStBcr = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("stbcr").toString());
            double custoBaseBcr = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("basebcr").toString());
            double custoBaseIcmsUltEnt = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("baseicmsultent").toString());
            //
            double vlultpcompra = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("vlultpcompra").toString());
            double custorealliq = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custorealliq").toString());
            double custoultentliq = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoultentliq").toString());
            double vlultentcontsemst = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("vlultentcontsemst").toString());
            double custoultentcont = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoultentcont").toString());
            double custodolar = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custodolar").toString());
            double custoultpedcomprasemst = Double.parseDouble(tblCustoMaster.getConteudoRowSelected("custoultpedcomprasemst").toString());
            //
            double pesoBruto = Double.parseDouble(edtMstPesoBruto.getText());
            double pesoLiq = Double.parseDouble(edtMstPesoLiquido.getText());
            //
            double qtdEst = Double.parseDouble(edtMstQtdEst.getText());

            // Multipica a quantidade de estoque pelo fator de conversao
            edtCnvEstoque.setText(Formato.doubleToCurrStr((qtdEst * fatorConv), 6));

            //Divide o peso liquido e bruto
            edtCnvPesoBruto.setText(Formato.doubleToCurrStr((pesoBruto / fatorConv), 3));
            edtCnvPesoLiquido.setText(Formato.doubleToCurrStr((pesoLiq / fatorConv), 3));

            //Divido os custos pelo fator de conversao
            edtCnvVlrUltCompra.setText(Formato.doubleToCurrStr((vlultpcompra / fatorConv)));
            edtCnvCustoRealLiq.setText(Formato.doubleToCurrStr((custorealliq / fatorConv)));
            edtCnvCustoUltEntLiq.setText(Formato.doubleToCurrStr((custoultentliq / fatorConv)));
            edtCnvVlrUltEntContSemSt.setText(Formato.doubleToCurrStr((vlultentcontsemst / fatorConv)));
            edtCnvCustoUltEntCont.setText(Formato.doubleToCurrStr((custoultentcont / fatorConv)));
            edtCnvCustoDolar.setText(Formato.doubleToCurrStr((custodolar / fatorConv)));
            edtCnvCustoUltPedCompraSemSt.setText(Formato.doubleToCurrStr((custoultpedcomprasemst / fatorConv)));

            //
            edtCnvVlrUltEnt.setText(Formato.doubleToCurrStr((vlrUltEnt / fatorConv)));
            edtCnvCustoUltEnt.setText(Formato.doubleToCurrStr((custoUltEnt / fatorConv)));
            edtCnvCustoReal.setText(Formato.doubleToCurrStr((custoReal / fatorConv)));
            edtCnvCustoRealSemSt.setText(Formato.doubleToCurrStr((custoRealSemSt / fatorConv)));
            edtCnvCustoFin.setText(Formato.doubleToCurrStr((custoFin / fatorConv)));
            edtCnvCustoCont.setText(Formato.doubleToCurrStr((custoCont / fatorConv)));
            edtCnvCustoRep.setText(Formato.doubleToCurrStr((custoRep / fatorConv)));
            edtCnvCustoFinSemSt.setText(Formato.doubleToCurrStr((custoFinSt / fatorConv)));
            edtCnvCustoUltEntFinSemSt.setText(Formato.doubleToCurrStr((custoUltEntFinSemSt / fatorConv)));
            edtCnvCustoUltEntFin.setText(Formato.doubleToCurrStr((custoUltEntFin / fatorConv)));
            edtCnvCustoUltEntContSemSt.setText(Formato.doubleToCurrStr((custoUltEntContSemSt / fatorConv)));
            edtCnvCustoStBcr.setText(Formato.doubleToCurrStr((custoStBcr / fatorConv)));
            edtCnvCustoBaseBcr.setText(Formato.doubleToCurrStr((custoBaseBcr / fatorConv)));
            edtCnvCustoBaseIcmsUltEnt.setText(Formato.doubleToCurrStr((custoBaseIcmsUltEnt / fatorConv)));

        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCnvCalcularActionPerformed");
        }
    }//GEN-LAST:event_edtCnvCalcularActionPerformed

    private void edtGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtGravarActionPerformed
        // TODO add your handling code here:
        if (edtCnvFator.getText().equalsIgnoreCase("1")) {
            MessageDialog.error("Procedimento abortado!\nO Fator de conversao deve ser maior que 1!");
            return;
        }
        if (edtCnvCustoReal.getText().equalsIgnoreCase("0.00")) {
            MessageDialog.error("Procedimento abortado!\nO custo real de conversao deve ser maior que 0!");
            return;
        }
        if (edtCnvCustoFin.getText().equalsIgnoreCase("0.00")) {
            MessageDialog.error("Procedimento abortado!\nO custo financeiro de conversao deve ser maior que 0!");
            return;
        }
        if (edtCnvCustoUltEnt.getText().equalsIgnoreCase("0.00")) {
            MessageDialog.error("Procedimento abortado!\nO custo ult ent de conversao deve ser maior que 0!");
            return;
        }
        if (edtCnvVlrUltEnt.getText().equalsIgnoreCase("0.00")) {
            MessageDialog.error("Procedimento abortado!\nO valor ult ent de conversao deve ser maior que 0!");
            return;
        }
        try {

            transfereProdutoMasterDestino();
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtGravarActionPerformed");
        }
    }//GEN-LAST:event_edtGravarActionPerformed

    private void edtMstCodProdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtMstCodProdFocusLost
        // TODO add your handling code here:
        try {
            buscaProdutoMaster(edtMstCodProd.getText(), edtCodFilial.getText());
            limpaConversao();
            edtDstCodProduto.setFocusable(true);
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtMstCodProdFocusLost");
        }

    }//GEN-LAST:event_edtMstCodProdFocusLost

    private void edtDstCodProdutoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtDstCodProdutoFocusLost
        try {
            buscaProdutoDestino(edtDstCodProduto.getText(), edtCodFilial.getText());
            limpaConversao();
            edtCnvFator.setFocusable(true);
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtDstCodProdutoFocusLost");
        }
    }//GEN-LAST:event_edtDstCodProdutoFocusLost

    private void chkFilial_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFilial_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkFilial_1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            trataErro.trataException(ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz005().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDstBuscar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnMstBuscar;
    private javax.swing.JCheckBox chkFilial_1;
    private javax.swing.JCheckBox chkFilial_2;
    private javax.swing.JCheckBox chkFilial_3;
    private javax.swing.JCheckBox chkFilial_4;
    private javax.swing.JCheckBox chkFilial_5;
    private javax.swing.JCheckBox chkFilial_6;
    private javax.swing.JButton edtCnvCalcular;
    private javax.swing.JTextField edtCnvCustoBaseBcr;
    private javax.swing.JTextField edtCnvCustoBaseIcmsUltEnt;
    private javax.swing.JTextField edtCnvCustoCont;
    private javax.swing.JTextField edtCnvCustoDolar;
    private javax.swing.JTextField edtCnvCustoFin;
    private javax.swing.JTextField edtCnvCustoFinSemSt;
    private javax.swing.JTextField edtCnvCustoReal;
    private javax.swing.JTextField edtCnvCustoRealLiq;
    private javax.swing.JTextField edtCnvCustoRealSemSt;
    private javax.swing.JTextField edtCnvCustoRep;
    private javax.swing.JTextField edtCnvCustoStBcr;
    private javax.swing.JTextField edtCnvCustoUltEnt;
    private javax.swing.JTextField edtCnvCustoUltEntCont;
    private javax.swing.JTextField edtCnvCustoUltEntContSemSt;
    private javax.swing.JTextField edtCnvCustoUltEntFin;
    private javax.swing.JTextField edtCnvCustoUltEntFinSemSt;
    private javax.swing.JTextField edtCnvCustoUltEntLiq;
    private javax.swing.JTextField edtCnvCustoUltPedCompraSemSt;
    private javax.swing.JTextField edtCnvEstoque;
    private javax.swing.JTextField edtCnvFator;
    private javax.swing.JTextField edtCnvPesoBruto;
    private javax.swing.JTextField edtCnvPesoLiquido;
    private javax.swing.JTextField edtCnvVlrUltCompra;
    private javax.swing.JTextField edtCnvVlrUltEnt;
    private javax.swing.JTextField edtCnvVlrUltEntContSemSt;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextField edtDstCodProduto;
    private javax.swing.JTextField edtDstCustoReal;
    private javax.swing.JTextField edtDstCustoUltEnt;
    private javax.swing.JTextField edtDstDescricao;
    private javax.swing.JTextField edtDstEmbalagem;
    private javax.swing.JTextField edtDstPesoBruto;
    private javax.swing.JTextField edtDstQtdEst;
    private javax.swing.JTextField edtDstQtdUndMaster;
    private javax.swing.JTextField edtDstUndVenda;
    private javax.swing.JTextField edtDstVlrUltEnt;
    private javax.swing.JButton edtGravar;
    private javax.swing.JTextField edtMstCodProd;
    private javax.swing.JTextField edtMstCustoUltEnt;
    private javax.swing.JTextField edtMstDesricao;
    private javax.swing.JTextField edtMstEmbalagem;
    private javax.swing.JTextField edtMstPesoBruto;
    private javax.swing.JTextField edtMstPesoLiquido;
    private javax.swing.JTextField edtMstQtdEst;
    private javax.swing.JTextField edtMstQtdUndMaster;
    private javax.swing.JTextField edtMstQtdUndVenda;
    private javax.swing.JTextField edtMstVlrUltEnt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private winthorDb.util.CustomTable tblCustoMaster;
    // End of variables declaration//GEN-END:variables
}
