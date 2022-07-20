/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ListSelectionModel;
import winthorDb.Main;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class Brz009 extends javax.swing.JFrame {

    int[] selecionados = null;

    /**
     * Creates new form Brz001
     */
    public Brz009() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        edtDetalhePedido.setText("");

        // permite selecionar mais de uma linha de dados.
        tblPedidoCarga.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblPedidoCarga.setColumnSelectionAllowed(false);
        tblPedidoCarga.setRowSelectionAllowed(true);

        if (!Main.codFilialFatura.isEmpty()) {
            edtCodFilialFatura.setText(Main.codFilialFatura);
        }
    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numCarga Numero do pedido digitado no sistema winthor
     *
     */
    private void buscaPedidoWinthor(String numCarga) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String strSelect = "SELECT p.obs, p.obs1, p.obs2,  p.numcar, ('' || p.numped) as numped, "
                    + " p.ORIGEMPED, p.data, p.vltotal, p.vlatend, p.codcli, c.cliente, p.codfilial, "
                    + " p.codfilialnf, c.codfilialnf cli_FilialFatura ,"
                    + " p.posicao, u.codusur, u.nome as NomeRCA, p.dtfat, p.numcupom  "
                    + " FROM pcpedc p, pcusuari u, pcclient c "
                    + " WHERE p.posicao in ('L','B','P','M') "
                    + " AND p.condvenda in (1) "
                    + " AND u.codusur = p.codusur "
                    + " AND c.codcli = p.codcli "
                    + " AND p.codCob in ('DH','D','CH','CHV','CHP') "
                    + " AND p.vlatend < 9999.00 "
                    + " AND p.numped not in (select it.numped from pcpedi it where it.numcar = p.numcar and it.bonific in ('S','F') and it.pbonific > 0 ) "
                    + " AND p.numcar = " + numCarga
                    + " ORDER BY p.numped";

            wint.openConectOracle();
            tblPedidoCarga.setTableData(wint.selectResultSet(strSelect));

            if (tblPedidoCarga.getRowCount() > 0) {
                edtDetalhePedido.append("Lista de Pedidos Encontrados no Winthor\n");
            } else {
                edtDetalhePedido.append("Carregamento não localizado no Winthor\n");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaPedidoWinthor");
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numCargaNfce Numero do pedido digitado no sistema winthor
     *
     */
    private void buscaCargaNfce(String numCargaNfce) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String strSelect = "SELECT p.numcupom, p.dtfat, p.codfilial, p.codfilialnf, c.codfilialnf cli_FilialFatura , "
                    + " p.numcar, ('' || p.numped) as numped, p.ORIGEMPED, p.data, p.vltotal, p.vlatend, p.codcli, c.cliente, "
                    + " p.posicao, u.codusur, u.nome as NomeRCA, p.obs, p.obs1, p.obs2, "
                    + " RV.NUMCARGAORIGINAL, ('' || RV.NUMPEDIDO) AS NUMPEDIDO , RV.CODCLIENTE, RV.CODFILIALORIGINAL, "
                    + " RV.CODFILIALFATURA, RV.NUMCARGANFCE, RV.LOG, RV.CONVERTIDO, RV.VINCULADO "
                    + " FROM pcpedc p, pcusuari u, pcclient c, rv_carregamento rv"
                    + " WHERE u.codusur = p.codusur "
                    + " AND c.codcli = p.codcli "
                    + " AND p.numped = rv.numpedido "
                    + " AND rv.numcarganfce = " + numCargaNfce
                    + " ORDER BY p.numped";

            wint.openConectOracle();
            tblCargaNfce.setTableData(wint.selectResultSet(strSelect));

            if (tblCargaNfce.getRowCount() > 0) {
                edtDetalhePedido.append("Lista de Pedidos NFC-e Encontrados no Winthor\n");
            } else {
                edtDetalhePedido.append("Carregamento NFC-e não localizado no Winthor\n");
                MessageDialog.error("Carregamento NFC-e não localizado !!");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaCargaNfce");
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * Faz a conversão do Pedido para cupom fiscal para que possa ser faturado
     * pela rotina 2075 seguindo os padroes do winthor
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private boolean addPedidoCargaNFCe(String numPedido, String codCliente, String codFilial, String numCargaAtual, String codFilialFatura, String numCargaNfce) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        String strSelect = "";
        String insString = "";
        int result = 0;
        boolean ret = false;
        try {
            wint.openConectOracle();

            // Verifica se ja existe este pedido na tabela de carregamentos nfce
            strSelect = "Select * from rv_carregamento where numcarganfce = " + numCargaNfce + " AND numpedido=" + numPedido;
            result = wint.selectDados(strSelect).size();
            if (result == 0) {
                // Adciona pedido ao controle de conversao de pedido -> cupom
                insString = "INSERT INTO rv_carregamento (numcargaoriginal, numpedido, codcliente,"
                        + " codfilialoriginal, codfilialfatura, numcarganfce, log ) "
                        + " VALUES(" + numCargaAtual
                        + " , " + numPedido
                        + " , " + codCliente
                        + " ,'" + codFilial + "'"
                        + " ,'" + codFilialFatura + "'"
                        + " , " + numCargaNfce
                        + " ,'dtCriacao:" + Formato.dateTimeCalendarToStr(Calendar.getInstance()) + "')";
                result = wint.updateDados(insString);
                ret = (result != 0);
                edtDetalhePedido.append("[" + result + "] Pedido adicionado a rv_carregamento Pedido:[" + numPedido + "] CarregamentoNFC-e [" + numCargaNfce + "]\n");
            }
            // Verificar se o carregamento NFC-e existe, caso nao exita criar na pccarreg
            if (result >= 0) {
                strSelect = "Select * from pccarreg where  NUMCAR = " + numCargaNfce;
                result = wint.selectDados(strSelect).size();
                if (result == 0) {
                    // Criar o carragemento na tabela de carregamentos
                    insString = "INSERT INTO pccarreg (numcar, dtsaida, totpeso, totvolume, vltotal, destino, datamon, codveiculo, codfuncmon) "
                            + " VALUES (" + numCargaNfce + " , TRUNC(SYSDATE), 0, 0, 0, 'CONVERSAO CUPOM', TRUNC(SYSDATE), 0, 1)";
                    result = wint.updateDados(insString);
                    edtDetalhePedido.append("[" + result + "] Carregamento Criado: " + numCargaNfce + "\n");
                    edtDetalhePedido.append("O carregamento para o pedido acima foi atualizado na base de dados do winthor\n");
                    ret = (result != 0);
                }
            }

            // altera a filial fatura do cliente
            if (result >= 0) {
                strUpdate = "update pcclient  set tipoDocumento = 'C', codfilialnf = " + codFilialFatura
                        + " WHERE codcli =  " + codCliente;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O cliente do pedido acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);
            }

            // altera o pedido caso o carregamento seja criado corretamente
            if (result > 0) {
                String obs = " 'Carga: " + numCargaAtual + "' ";

                strUpdate = "update pcpedc set tipodocumento = 'C', NUMCAR = " + numCargaNfce + " , codfilialnf = " + codFilialFatura
                        + " , OBS2 = " + (obs.length() < 50 ? obs : obs.subSequence(0, 49))
                        + " WHERE NUMPED =  " + numPedido
                        + " and codcli =  " + codCliente
                        + " and codfilial = " + codFilial;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O Cabeçalho do pedido acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);

                // Atualiza os itens do pedido se o cabeçalho foi atualizado
                if (result > 0) {
                    strUpdate = "Update pcpedi set NUMCAR = " + numCargaNfce + " WHERE NUMPED =  " + numPedido + " and codcli =  " + codCliente;
                    result = wint.updateDados(strUpdate);
                    edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                    edtDetalhePedido.append("Os Itens do pedido acima foram atualizado na base de dados do winthor\n");
                    ret = (result != 0);
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex);
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    /**
     * Faz a mudança do tipo de documento da tabela Pedido nota fiscal evitando
     * erro no processamento da 2075
     *
     * @param numCargaAtual Numero do carregamento digitado no sistema winthor
     */
    private boolean mudaTipoDocumentoPedido(String numCargaAtual) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        boolean ret = false;
        try {
            wint.openConectOracle();

            // altera a filial fatura do cliente
            if (result >= 0) {
                strUpdate = "update pcclient  set tipoDocumento = 'N' "
                        + " WHERE codcli in (Select codCli from pcpedc where numcar =" + numCargaAtual + ")";

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("Os clientes do Carregamento acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);
            }

            // altera o pedido caso o carregamento seja criado corretamente
            if (result >= 0) {
                strUpdate = "update pcpedc set tipodocumento = 'N' WHERE NumCar =  " + numCargaAtual;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O Cabeçalho dos pedidos acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    /**
     * Faz a conversão do Pedido para cupom fiscal para que possa ser faturado
     * pela rotina 2075 seguindo os padroes do winthor
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private boolean convertePedidoNFCe(String numPedido, String codCliente, String codFilial, String numCargaAtual, String codFilialFatura, String numCargaNfce) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";

        int result = 0;
        boolean ret = false;
        try {
            wint.openConectOracle();

            // altera o pedido caso o carregamento seja criado corretamente
            if (result >= 0) {
                String obs = " 'Cli: " + codCliente + " Carga: " + numCargaAtual + " Filial: " + codFilial + "' ";

                strUpdate = "update pcpedc set ORIGEMPED = 'R', CODCLI= " + Main.codConsumidor
                        + " , codfilial = " + codFilialFatura + " , codfilialnf = " + codFilialFatura
                        + " , OBS2 = " + (obs.length() < 50 ? obs : obs.subSequence(0, 49))
                        + " WHERE NUMPED =  " + numPedido
                        + " and codcli =  " + codCliente
                        + " and codfilial = " + codFilial;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O Cabeçalho do pedido acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);

                // Atualiza os itens do pedido se o cabeçalho foi atualizado
                if (result > 0) {
                    strUpdate = "Update pcpedi set CODCLI=" + Main.codConsumidor + " WHERE NUMPED =  " + numPedido + " and codcli =  " + codCliente;
                    result = wint.updateDados(strUpdate);
                    edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                    edtDetalhePedido.append("Os Itens do pedido acima foram atualizado na base de dados do winthor\n");
                    ret = (result != 0);
                }
            }

            // marca como convertido na tabela rv_carregamento
            if (result >= 0) {
                strUpdate = "update rv_carregamento  set convertido ='S' "
                        + " where numcarganfce = " + numCargaNfce + " AND numpedido=" + numPedido;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O rv_carregemento convertido na base de dados do winthor\n");
                ret = (result != 0);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    /**
     * Vincula o cupom fiscal gerado pela rotina 2075 ao cliente original do
     * pedido, este processo deve ser usado apos a geração do cupom fiscal pela
     * 2075 para que o romaneio da carga seja gerado de forma correta.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     * @param numCarga numero da carga a qual o pedido esta vinculado no
     * processo de separação de mercadorias
     * @param numCupom numero do cupom fiscal gerado pela rotina 2075 no
     * processo de faturamento da venda
     */
    private boolean vinculaTitulos(String numPedido, String codCliente, String codFilial, String codFilialFatura, String numCargaOrigem, String numCupom, String numCargaNfce) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        boolean ret = false;
        int result = 0;
        try {
            wint.openConectOracle();
            // passo 1 - Alterar o codigo do cliente na tabela de titulos do winthor (pcprest)
            String updString = "UPDATE PCPREST SET CODCLI = " + codCliente + " , numcar = " + numCargaOrigem
                    + " WHERE duplic = " + numCupom
                    + " and NUMPED =  " + numPedido
                    + " and codfilial = " + codFilialFatura;
            result = wint.updateDados(updString);
            ret = (result != 0);
            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Titulos do Contas a Receber vinculados ao cliente: " + codCliente + "\n");

            } else {
                MessageDialog.error("Falha ao vincular o contas a receber do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 2 - Altera a caga das notas faturados como cupom para pode sair corretamente no romaneio
            updString = "UPDATE PCNFSAID SET NUMCAR = " + numCargaOrigem + " , CODCLI = " + codCliente
                    + " WHERE NUMNOTA = " + numCupom
                    + " and NUMPED =  " + numPedido
                    + " and codfilial = " + codFilialFatura;
            result = wint.updateDados(updString);
            ret = (result != 0);
            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Cabecalho da Nota Fiscal vinculado ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o cabecalho de nota fiscal do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 2.1 - Alterar os dados dos item da nota fiscal
            updString = "UPDATE PCMOV SET NUMCAR = " + numCargaOrigem + " ,CODCLI = " + codCliente
                    + " WHERE CODOPER = 'S' AND NUMNOTA = " + numCupom
                    + " and NUMPED =  " + numPedido
                    + " and codfilial = " + codFilialFatura;
            result = wint.updateDados(updString);
            ret = (result != 0);

            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Itens da Nota Fiscal vinculados ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o Item da nota fiscal do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 3 - Altera a caga dos pedidos faturados como cupom para pode sair corretamente no romaneio
            updString = "UPDATE PCPEDC SET NUMCAR = " + numCargaOrigem + " , CODCLI = " + codCliente
                    + " , codfilial = " + codFilial + " , codfilialnf = " + codFilial
                    + " WHERE NUMPED =  " + numPedido
                    + " and codfilial = " + codFilialFatura;
            result = wint.updateDados(updString);
            ret = (result != 0);

            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Cabecalho do Pedido vinculado ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o cabecalho do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 3.1 - Atera os itens do pedido
            updString = "UPDATE PCPEDI SET NUMCAR = " + numCargaOrigem + " ,CODCLI = " + codCliente
                    + " WHERE NUMPED =  " + numPedido;
            result = wint.updateDados(updString);
            ret = (result != 0);

            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Itens do Pedido vinculados ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o itens do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // altera a filial fatura do cliente
            // 28/01 - Gravacao da dtultcomp no cadastro do cliente, o winthor estava 
            //         marcando esse cliente como inativo quando so compra no cupom
            if (result >= 0) {
                updString = "update pcclient set tipoDocumento = 'A', codfilialnf = " + codFilial
                        + " , DTULTCOMP = TO_DATE('" + Formato.dateToStr(new Date()) + "', 'DD/MM/YYYY') "
                        + " WHERE codcli =  " + codCliente;

                result = wint.updateDados(updString);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O cliente do pedido acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);
            }

            // ajustar estoque de frente de loja para os produtos do carregamento
            if (Main.AjustaFrenteLoja.equalsIgnoreCase("S")) {
                if (result >= 0) {
                    updString = " UPDATE PCEST SET "
                            + " qtfrenteloja = qtfrenteloja + NVL((SELECT SUM(pcmov.QT) FROM pcmov "
                            + "                      WHERE pcmov.numcar = " + numCargaOrigem
                            + "                        AND pcmov.NUMPED IN (SELECT NUMPEDIDO FROM rv_carregamento WHERE NUMCARGAORIGINAL = " + numCargaOrigem + " )"
                            + "                        AND pcmov.CODPROD = PCEST.CODPROD ),0)\n"
                            + " WHERE PCEST.CODFILIAL = " + codFilialFatura
                            + " AND PCEST.CODPROD IN (SELECT CODPROD FROM pcmov "
                            + "                       WHERE pcmov.numcar = " + numCargaOrigem + " \n"
                            + "                         AND pcmov.NUMPED IN (SELECT NUMPEDIDO FROM rv_carregamento WHERE NUMCARGAORIGINAL = " + numCargaOrigem + " ))";

                    result = wint.updateDados(updString);
                    edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                    edtDetalhePedido.append("Estoque de Frente de Loja Ajustado com Suscesso na base de dados do winthor\n");
                    ret = (result != 0);
                }
            }
            // Verifica se existe neste carregamento somente pedidos cupom
            List retx = new ArrayList();
            String strSelect = "select "
                    + "  nvl((select count(p1.numped) as qtde from pcpedc p1 where p1.origemped = 'R' and  p1.numcar = p.numcar),0) as qtdeCupom, "
                    + "  nvl((select count(p1.numped) as qtde from pcpedc p1 where p1.origemped <> 'R' and  p1.numcar = p.numcar),0) as qtdeOutro "
                    + " from pcpedc p where ROWNUM<2 and numcar = " + numCargaOrigem;
            retx = (List) wint.selectDados(strSelect).get(0);
            if (retx.size() == 2) {
                result = Formato.strToInt(retx.get(1).toString());
                if (result == 0) {
                    updString = "update pccarreg set dtsaida = trunc(sysdate), dtfat = trunc(sysdate),  codfuncfat=1 where numcar = " + numCargaOrigem; //dtfecha  = trunc(sysdate),
                    result = wint.updateDados(updString);
                    if (result > 0) {
                        edtDetalhePedido.append("[" + result + "] Carregamento [" + numCargaOrigem + "] maracdo como faturado pois só existe cupom fiscal\n");
                    } else {
                        MessageDialog.error("Falha ao marcar carregemento como faturado [" + numCargaOrigem + "]");
                    }
                }
            }

            // marca como vinculado na tabela rv_carregamento
            if (result >= 0) {
                updString = "update rv_carregamento  set vinculado ='S' "
                        + " where numcarganfce = " + numCargaNfce + " AND numpedido=" + numPedido;

                result = wint.updateDados(updString);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O rv_carregemento vinculado na base de dados do winthor\n");
                ret = (result != 0);
            }

            edtDetalhePedido.append("Processo de Vinculação ao cliente e a carga concluidos pode continuar o faturamento da carga\n");
        } catch (Exception ex) {
            trataErro.trataException(ex, "vinculaTitulos");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private void pesquisaCargaNfce() {
        try {
            if (!edtNumCargaNfce1.getText().isEmpty()) {
                buscaCargaNfce(edtNumCargaNfce1.getText());
                if (tblCargaNfce.getRowCount() >= 0) {
                    btnConvertePedidoNfce.setEnabled(true);
                    btnVinculaTitulo.setEnabled(true);
                } else {
                    btnConvertePedidoNfce.setEnabled(false);
                    btnVinculaTitulo.setEnabled(false);
                }
            } else {
                MessageDialog.error("Favor informar o numero do carregamento NFC-e!!");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnPesquisaConversaoActionPerformed");
        }
    }

    /**
     * DesVincula o cupom fiscal gerado pela rotina 2075 ao cliente original do
     * pedido, este processo deve ser usado apos a geração do cupom fiscal pela
     * 2075 para que o romaneio da carga seja gerado de forma correta.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     * @param numCarga numero da carga a qual o pedido esta vinculado no
     * processo de separação de mercadorias
     * @param numCupom numero do cupom fiscal gerado pela rotina 2075 no
     * processo de faturamento da venda
     */
    private boolean desvinculaTitulos(String numPedido, String numCargaNfce) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        boolean ret = false;
        int result = 0;
        String updString = "";
        try {
            wint.openConectOracle();
            // marca como desvinculado na tabela rv_carregamento
            if (result >= 0) {
                updString = "update rv_carregamento  set vinculado ='N' "
                        + " where numcarganfce = " + numCargaNfce + " AND numpedido=" + numPedido;

                result = wint.updateDados(updString);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O rv_carregemento desvinculado na base de dados do winthor\n");
                ret = (result != 0);
            }

            edtDetalhePedido.append("Processo de desVinculação ao cliente e a carga concluidos pode continuar o faturamento da carga\n");
        } catch (Exception ex) {
            trataErro.trataException(ex, "desvinculaTitulos");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

/**
     * DesVincula o cupom fiscal gerado pela rotina 2075 ao cliente original do
     * pedido, este processo deve ser usado apos a geração do cupom fiscal pela
     * 2075 para que o romaneio da carga seja gerado de forma correta.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     * @param numCarga numero da carga a qual o pedido esta vinculado no
     * processo de separação de mercadorias
     * @param numCupom numero do cupom fiscal gerado pela rotina 2075 no
     * processo de faturamento da venda
     */
    private boolean forcarConverterPedido(String numPedido, String numCargaNfce) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        boolean ret = false;
        int result = 0;
        String updString = "";
        try {
            wint.openConectOracle();
            // marca como desvinculado na tabela rv_carregamento
            if (result >= 0) {
                updString = "update rv_carregamento  set convertido ='N' "
                        + " where numcarganfce = " + numCargaNfce + " AND numpedido=" + numPedido;

                result = wint.updateDados(updString);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O rv_carregemento Forçado a conversão na base de dados do winthor\n");
                ret = (result != 0);
            }

            edtDetalhePedido.append("Processo de Forçar a conversão concluido, pode continuar o faturamento da carga\n");
        } catch (Exception ex) {
            trataErro.trataException(ex, "forcarConverterPedido");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }
    
    /**
     * busca os dados do contas a receber gerados pelo sistema winthor apos o
     * processo de faturamento da rotina 2075
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codFilialFatura Codigo da filial onde o pedido foi faturado
     * @param numCarga numero da carga a qual o pedido esta vinculado no
     * processo da 2075 (Padrão: 9999999 )
     */
    private boolean buscaCReceberWinthor(String numPedido, String numCarga, String codFilialFatura) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        boolean ret = false;
        try {
            String strSelect = "Select concat('Cliente:',c.codcli || ' - ' || c.cliente) as cliente, "
                    + " concat('Titulo:', p.duplic || ' - ' || p.prest) as titulo, "
                    + " concat('Valor:', p.valor) as valor, "
                    + " concat('Cobranca:',p.codcob) as codcob, "
                    + " concat('DtEmissao:',p.dtemissao) as dtemissao, "
                    + " concat('DtVencimento:',p.dtvenc) as dtvenc, "
                    + " concat('Carga:', p.numcar) as numcar, "
                    + " concat('Pedido:', p.numped) as numped, "
                    + " concat('Num.Transvenda:',p.numtransvenda) as transvenda "
                    + " from pcprest p, pcclient c "
                    + " where c.codcli = p.codcli "
                    + " and p.numcar =  " + numCarga
                    + " and p.codfilial = " + codFilialFatura
                    + " and p. numped = " + numPedido;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtDetalhePedido.append("Contas a Receber encontrado no Winthor\n");

            for (Object object : lst.toArray()) {
                edtDetalhePedido.append(lst.size() + "\n");
                edtDetalhePedido.append(object.toString() + "\n");

            }
            if (lst.isEmpty()) {
                edtDetalhePedido.append("O Pedido não foi localizado\n");
                ret = false;
            } else {
                ret = true;
            }

            lst = null;

        } catch (Exception ex) {
            trataErro.trataException(ex);
        } finally {
            wint.closeConectOracle();
        }
        return ret;

    }

    /**
     * faz a validação do pedido para que o mesmo possa ser processado pela 2075
     * Esta validação verifica se os itens possuem embalagem cadastrada na 2014
     * Se tem o codigo de barras cadastrado na PCPEDI (codauxiliar) Se tem a
     * tributação da ECF cadastrado na PCPDI (codecf) Se a filial de faturamento
     * é a mesma do pedido (codfilialretira) Se
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codFilial Codigo da filial onde o pedido foi digitado
     * @param numCarga numero da carga a qual o pedido esta vinculado no
     * processo da 2075 (Padrão: 9999999 )
     */
    private boolean validaPedidoCupom(String numPedido, String codCli, String codFilial, String codFilialFatura) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        boolean ret = true;
        List lstDados = new ArrayList<>();

        try {
            String strSelect = "select it.numped, it.codcli, it.codprod, nvl(it.codauxiliar,0) as codauxiliar, "
                    + "it.pvenda, it.codecf, it.codfilialretira, "
                    + "    (select count(emb.codprod) as qtdEmb from pcembalagem emb  "
                    + "         where emb.codauxiliar = it.codauxiliar "
                    + "               and emb.codprod = it.codprod "
                    + "               and emb.codfilial = " + codFilialFatura + " ) "
                    + "    as qtdEmbalagem "
                    + "  , nvl(it.bonific,'N') bonific "
                    + "  , nvl(it.pbonific,'0') pbonific "
                    + " from pcpedi it "
                    + " where it.numped = " + numPedido
                    + " and it.codcli = " + codCli;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtDetalhePedido.append("\n" + Formato.replicate("-*", 25) + "\n");
            edtDetalhePedido.append("Validação do Pedido para emissão do cupom fiscal\n");
            edtDetalhePedido.append("Pedido [" + numPedido + "] Filial Fatura[" + codFilialFatura + " ] Cod. Cliente [" + codCli + "]\n");

            if (lst.isEmpty()) {
                edtDetalhePedido.append("O Pedido não foi localizado\n");
                edtDetalhePedido.append("\n" + Formato.replicate("+", 50) + "\n");
                edtDetalhePedido.append(strSelect);
                edtDetalhePedido.append("\n" + Formato.replicate("-", 50) + "\n");
                ret = false;
            } else {

                for (Object lst1 : lst) {
                    lstDados = (List) lst1;
                    for (int i = 0; i < lstDados.size(); i++) {

                        if (i == 3) {
                            if (lstDados.get(3) == null) {
                                ret = false;
                                edtDetalhePedido.append("\nFalta o codigo de Barras no cadastrado do produto: " + lstDados.get(2).toString() + "\n\tpara corrigir edite o cadastro do produto na 203 e remova e inclua o item pela 336");
                            }
                            if (lstDados.get(3).toString().equalsIgnoreCase("0")) {
                                ret = false;
                                edtDetalhePedido.append("\nFalta o codigo de Barras cadastrado no pedido para o produto: " + lstDados.get(2).toString() + "\n\tpara corrigir edite o cadastro do produto na 203 e remova e inclua o item pela 336");
                            }
                        }

                        if ((i == 7)) {
                            if (lstDados.get(7).toString().isEmpty()) {
                                ret = false;
                                edtDetalhePedido.append("\nFalta o cadastro de embalagem na rotina 2014 para o produto: " + lstDados.get(2).toString() + " - " + lstDados.get(3).toString() + " - Filial [ " + codFilialFatura + " ]");
                            }
                            if (lstDados.get(7).toString().equalsIgnoreCase("0")) {
                                ret = false;
                                edtDetalhePedido.append("\nFalta o cadastro de embalagem na rotina 2014 para o produto: " + lstDados.get(2).toString() + " - " + lstDados.get(3).toString() + " - Filial [ " + codFilialFatura + " ]");
                            }
                        }
                        if ((i == 8)) {
                            if (lstDados.get(8).toString().equalsIgnoreCase("S")) {
                                ret = false;
                                edtDetalhePedido.append("\nProduto Bonificado no pedido: " + lstDados.get(2).toString() + " - " + lstDados.get(8).toString());
                            }
                            if (lstDados.get(8).toString().equalsIgnoreCase("F")) {
                                ret = false;
                                edtDetalhePedido.append("\nProduto Bonificado no pedido: " + lstDados.get(2).toString() + " - " + lstDados.get(8).toString());
                            }
                        }
                        if ((i == 9)) {
                            if (!lstDados.get(9).toString().equalsIgnoreCase("0")) {
                                ret = false;
                                edtDetalhePedido.append("\nProduto Quantidade Bonificado > 0 no pedido: " + lstDados.get(2).toString() + " - " + lstDados.get(9).toString());
                            }
                        }
                    }

                    if (ret) {
                        edtDetalhePedido.append("\nO Produto " + lstDados.get(2).toString() + " esta Ok\n");
                    }
                }
            }

            lst = null;

            if (ret) {
                edtDetalhePedido.append("\nO Pedido esta OK para emissão do cupom fiscal\n");
            } else {
                edtDetalhePedido.append("\nO Pedido NAO esta preparado para emissão do cupom fiscal\n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "validaPedidoCupom");
        } finally {
            wint.closeConectOracle();
        }
        return ret;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuTblConversao = new javax.swing.JPopupMenu();
        mniDesVincular = new javax.swing.JMenuItem();
        mnuDesConverter = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtpSteps = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        btnPesquisaPedido = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        edtNumCarga = new javax.swing.JTextField();
        btnValidarPedido = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPedidoCarga = new winthorDb.util.CustomTable();
        jLabel8 = new javax.swing.JLabel();
        edtCodFilialFatura = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        edtNumCargaNfce = new javax.swing.JTextField();
        btnAddPedidoCargaNfce = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCargaNfce = new winthorDb.util.CustomTable();
        btnVinculaTitulo = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        edtNumCargaNfce1 = new javax.swing.JTextField();
        btnPesquisaConversao = new javax.swing.JButton();
        btnConvertePedidoNfce = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtDetalhePedido = new javax.swing.JTextArea();
        btnImprimeLog = new javax.swing.JButton();

        mniDesVincular.setText("Forçar Vincular");
        mniDesVincular.setActionCommand("Forçar Vincular");
        mniDesVincular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniDesVincularActionPerformed(evt);
            }
        });
        mnuTblConversao.add(mniDesVincular);

        mnuDesConverter.setText("Forçar Converter");
        mnuDesConverter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDesConverterActionPerformed(evt);
            }
        });
        mnuTblConversao.add(mnuDesConverter);

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz009");

        jLabel1.setText("Converter pedidos do força de vendas para cupom fiscal");

        jLabel3.setText("* * Os pedidos devem estar montados e não faturadospela 1402");

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados do pedido"));

        btnPesquisaPedido.setText("Pesquisar");
        btnPesquisaPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaPedidoActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel12.setText("Num. Carga Original:");

        btnValidarPedido.setText("Validar Pedidos Selecionados");
        btnValidarPedido.setEnabled(false);
        btnValidarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarPedidoActionPerformed(evt);
            }
        });

        tblPedidoCarga.setToolTipText("");
        tblPedidoCarga.setCellSelectionEnabled(true);
        jScrollPane3.setViewportView(tblPedidoCarga);

        jLabel8.setText("Cod. Filial Fatura:");

        edtCodFilialFatura.setText("2");
        edtCodFilialFatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodFilialFaturaActionPerformed(evt);
            }
        });

        jLabel13.setText("Num. Carga NFC-e:");

        edtNumCargaNfce.setEditable(false);

        btnAddPedidoCargaNfce.setText("Add Pedidos Carga NFC-e");
        btnAddPedidoCargaNfce.setEnabled(false);
        btnAddPedidoCargaNfce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPedidoCargaNfceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnValidarPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddPedidoCargaNfce)
                        .addGap(360, 360, 360)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCargaNfce, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilialFatura, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edtCodFilialFatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(btnLimpar)
                    .addComponent(btnPesquisaPedido))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(edtNumCargaNfce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnValidarPedido)
                        .addComponent(btnAddPedidoCargaNfce)))
                .addContainerGap())
        );

        jtpSteps.addTab("1 - Selecionar Pedidos", jPanel2);

        tblCargaNfce.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCargaNfceMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCargaNfce);

        btnVinculaTitulo.setText("Vincular Titulos");
        btnVinculaTitulo.setEnabled(false);
        btnVinculaTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVinculaTituloActionPerformed(evt);
            }
        });

        jLabel14.setText("Num. Carga NFC-e:");

        btnPesquisaConversao.setText("Pesquisar");
        btnPesquisaConversao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaConversaoActionPerformed(evt);
            }
        });

        btnConvertePedidoNfce.setText("Converter Pedido->NFC-e");
        btnConvertePedidoNfce.setEnabled(false);
        btnConvertePedidoNfce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConvertePedidoNfceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCargaNfce1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisaConversao)
                        .addGap(128, 128, 128)
                        .addComponent(btnConvertePedidoNfce)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVinculaTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtNumCargaNfce1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(btnPesquisaConversao)
                    .addComponent(btnConvertePedidoNfce)
                    .addComponent(btnVinculaTitulo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtpSteps.addTab("2 - Finalizar Conversão", jPanel4);

        edtDetalhePedido.setColumns(20);
        edtDetalhePedido.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        edtDetalhePedido.setLineWrap(true);
        edtDetalhePedido.setRows(5);
        edtDetalhePedido.setWrapStyleWord(true);
        jScrollPane1.setViewportView(edtDetalhePedido);

        btnImprimeLog.setText("Imprimir Log");
        btnImprimeLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnImprimeLog)
                        .addGap(0, 877, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimeLog)
                .addContainerGap())
        );

        jtpSteps.addTab("Log de Processamento", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jtpSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpSteps))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisaPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaPedidoActionPerformed
        try {
            if ((!edtCodFilialFatura.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                buscaPedidoWinthor(edtNumCarga.getText());
                if (tblPedidoCarga.getRowCount() > 0) {
                    btnPesquisaPedido.setEnabled(false);
                    btnValidarPedido.setEnabled(true);
                }
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o codigo da filial fatura e o numero do carregamento !!!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnPesquisaPedidoActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        btnPesquisaPedido.setEnabled(true);
        btnValidarPedido.setEnabled(false);
        btnVinculaTitulo.setEnabled(false);
        btnAddPedidoCargaNfce.setEnabled(false);

        edtCodFilialFatura.setText(Main.codFilialFatura);
        edtNumCarga.setEditable(true);
        edtNumCarga.setText("");
        edtNumCargaNfce.setText("");
        edtDetalhePedido.setText("");

        tblCargaNfce.clearTableData();
        tblPedidoCarga.clearTableData();
        selecionados = null;

    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnVinculaTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVinculaTituloActionPerformed
        try {
            String numeroPedido = "";
            String codCliente = "";
            String codFilial = "";
            String codFilialFatura = "";
            String numCarga = "";
            String numCargaNfce = "";
            String numCupom = "";
            String vinculado = "";
            String dtfatura = "";

            boolean valida = true;
            boolean falha = false;

            if (tblCargaNfce.getRowCount() < 0) {
                MessageDialog.info("Não Existem pedidos para vincular !!!\n");
            } else {

                for (int i = 0; i < tblCargaNfce.getRowCount(); i++) {
                    numeroPedido = tblCargaNfce.getConteudoRow("numpedido", i).toString(); // tabela rv_carregamento
                    codCliente = tblCargaNfce.getConteudoRow("codcliente", i).toString();// tabela rv_carregamento
                    codFilial = tblCargaNfce.getConteudoRow("codfilialoriginal", i).toString();// tabela rv_carregamento
                    codFilialFatura = tblCargaNfce.getConteudoRow("codfilialfatura", i).toString();// tabela rv_carregamento
                    numCarga = tblCargaNfce.getConteudoRow("numcargaoriginal", i).toString();// tabela rv_carregamento
                    numCargaNfce = tblCargaNfce.getConteudoRow("numcarganfce", i).toString();// tabela rv_carregamento
                    vinculado = tblCargaNfce.getConteudoRow("vinculado", i).toString(); // tabela rv_carregamento
                    numCupom = tblCargaNfce.getConteudoRow("numcupom", i).toString(); // tabela pcpedc
                    dtfatura = tblCargaNfce.getConteudoRow("dtfat", i).toString(); // tabela pcpedc

                    if ((!numeroPedido.trim().isEmpty()) && (!codCliente.trim().isEmpty()) 
                            && (!codFilial.trim().isEmpty()) && (!codFilialFatura.trim().isEmpty()) 
                            && (!dtfatura.trim().isEmpty()) && (!numCupom.trim().isEmpty()) 
                            && (!numCarga.trim().isEmpty()) && (!numCargaNfce.trim().isEmpty())) {
                        if (vinculado.equalsIgnoreCase("N")) {
                            valida = vinculaTitulos(numeroPedido, codCliente, codFilial, codFilialFatura, numCarga, numCupom, numCargaNfce);
                            if (!valida) {
                                falha = true;
                            }
                        }
                    } else {
                        trataErro.lstErros.clear();
                        trataErro.addListaErros("ATENÇÃO: Deve ser informado, numero do cupom , data de faturamento, o numero do pedido, o numero da carga, o numero da carganfce , o codigo do cliente , codigo da filial, codigo da filial Fatura!");
                        trataErro.mostraListaErros();
                        trataErro.lstErros.clear();
                    }
                }
                if (falha) {
                    MessageDialog.error("Algum pedido não foi vinculado ao contas a receber/nota fiscal!!!\n VERIFIQUE O LOG");
                } else {
                    MessageDialog.info("Pedidos Selecionados, vinculados os clientes originais!");
                }
            }
            pesquisaCargaNfce();
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnVinculaTituloActionPerformed");
        }
    }//GEN-LAST:event_btnVinculaTituloActionPerformed

    private void btnValidarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarPedidoActionPerformed
        try {
            String numeroPedido = "";
            String codCliente = "";
            String codFilial = "";
            boolean valida = true;
            boolean falha = false;
            int cargaNfce = 0;

            if (selecionados == null) {
                selecionados = tblPedidoCarga.getSelectedRows();
            }

            if ((selecionados == null) || (selecionados.length < 0)) {
                MessageDialog.info("Não existem Pedidos Selecionados!!");
                return;
            }
            for (int i : selecionados) {
                numeroPedido = tblPedidoCarga.getConteudoRow("numped", i).toString();
                codCliente = tblPedidoCarga.getConteudoRow("codcli", i).toString();
                codFilial = tblPedidoCarga.getConteudoRow("codfilial", i).toString();

                if ((!numeroPedido.isEmpty()) && (!codCliente.isEmpty()) && (!codFilial.isEmpty()) && (!edtCodFilialFatura.getText().isEmpty())) {
                    valida = validaPedidoCupom(numeroPedido, codCliente, codFilial, edtCodFilialFatura.getText());
                    if (!valida) {
                        falha = true;
                    }
                } else {
                    trataErro.lstErros.clear();
                    trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o numero da carga, o codigo do cliente , codigo da filial, codigo da filial Fatura!");
                    trataErro.mostraListaErros();
                    trataErro.lstErros.clear();
                }
            }
            if (falha) {
                MessageDialog.info("Existem produtos com problemas de cadastro que impedem a geração do cupom fiscal !!!\n VERIFIQUE O LOG");
                btnAddPedidoCargaNfce.setEnabled(false);
            } else {
                MessageDialog.info("Pedidos Selecionados validados para geração de cupom fiscal!!");
                btnAddPedidoCargaNfce.setEnabled(true);
                // cria a numeracao da nova carga
                cargaNfce = 90000000 + Formato.strToInt(edtNumCarga.getText());
                edtNumCargaNfce.setText(Formato.intToStr(cargaNfce));
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "btnValidarPedidoActionPerformed");
        }
    }//GEN-LAST:event_btnValidarPedidoActionPerformed

    private void btnImprimeLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeLogActionPerformed
        try {
            edtDetalhePedido.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 8));
            edtDetalhePedido.print();

        } catch (PrinterException ex) {
            trataErro.trataException(ex);
        } finally {
            edtDetalhePedido.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 10));
        }
    }//GEN-LAST:event_btnImprimeLogActionPerformed

    private void edtCodFilialFaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialFaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodFilialFaturaActionPerformed

    private void btnAddPedidoCargaNfceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPedidoCargaNfceActionPerformed
        try {
            String numeroPedido = "";
            String codCliente = "";
            String codFilial = "";
            boolean valida = true;
            boolean falha = false;
            if (selecionados == null) {
                MessageDialog.info("Não Existem pedidos selecionados para adicionar !!!\n");
            } else {
                // mudar os pedido do carregamento para Nota Fiscal para evitar erros na 2075
                if (!edtNumCarga.getText().isEmpty()) {
                    mudaTipoDocumentoPedido(edtNumCarga.getText());
                }

                // processa os pedidos selecionados
                for (int i : selecionados) {
                    numeroPedido = tblPedidoCarga.getConteudoRow("numped", i).toString();
                    codCliente = tblPedidoCarga.getConteudoRow("codcli", i).toString();
                    codFilial = tblPedidoCarga.getConteudoRow("codfilial", i).toString();

                    if ((!numeroPedido.isEmpty()) && (!codCliente.isEmpty()) && (!codFilial.isEmpty()) && (!edtCodFilialFatura.getText().isEmpty()) && (!edtNumCargaNfce.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                        valida = addPedidoCargaNFCe(numeroPedido, codCliente, codFilial, edtNumCarga.getText(), edtCodFilialFatura.getText(), edtNumCargaNfce.getText());
                        if (!valida) {
                            falha = true;
                        }
                    } else {
                        trataErro.lstErros.clear();
                        trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o numero da carga, o numero da carganfce , o codigo do cliente , codigo da filial, codigo da filial Fatura!");
                        trataErro.mostraListaErros();
                        trataErro.lstErros.clear();
                    }
                }
                if (falha) {
                    MessageDialog.info("Algum pedido não foi adicionado a carga nfce!!!\n VERIFIQUE O LOG");
                } else {
                    MessageDialog.info("Pedidos Selecionados, prontos para conversão em NFC-e!!\n Para continuar efetuar a transferencia de mercadoria pela rotina 1419\n Apos transferir fazer a conversao de pedido para NFC-e");
                    edtNumCargaNfce1.setText(edtNumCargaNfce.getText());
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnAddPedidoCargaNfceActionPerformed");
        }

    }//GEN-LAST:event_btnAddPedidoCargaNfceActionPerformed

    private void btnConvertePedidoNfceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConvertePedidoNfceActionPerformed
        try {
            String numeroPedido = "";
            String codCliente = "";
            String codFilial = "";
            String codFilialFatura = "";
            String numCarga = "";
            String convertido = "";
            boolean valida = true;
            boolean falha = false;

            if (tblCargaNfce.getRowCount() <= 0) {
                MessageDialog.info("Não Existem pedidos para converter !!!\n");
            } else {

                for (int i = 0; i < tblCargaNfce.getRowCount(); i++) {
                    numeroPedido = tblCargaNfce.getConteudoRow("numped", i).toString();
                    codCliente = tblCargaNfce.getConteudoRow("codcli", i).toString();
                    codFilial = tblCargaNfce.getConteudoRow("codfilial", i).toString();
                    codFilialFatura = tblCargaNfce.getConteudoRow("codfilialnf", i).toString();
                    numCarga = tblCargaNfce.getConteudoRow("numcar", i).toString();
                    convertido = tblCargaNfce.getConteudoRow("convertido", i).toString();

                    if ((!numeroPedido.isEmpty()) && (!codCliente.isEmpty()) && (!codFilial.isEmpty()) && (!codFilialFatura.isEmpty()) && (!edtNumCargaNfce1.getText().isEmpty()) && (!numCarga.isEmpty())) {
                        if (convertido.equalsIgnoreCase("N")) {
                            valida = convertePedidoNFCe(numeroPedido, codCliente, codFilial, numCarga, codFilialFatura, edtNumCargaNfce1.getText());
                            if (!valida) {
                                falha = true;
                            }
                        } else {
                            falha = true;
                            edtDetalhePedido.append("\nPedido Ja convertido anteriormente " + numeroPedido + "\n");
                        }
                    } else {
                        trataErro.lstErros.clear();
                        trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o numero da carga, o numero da carganfce , o codigo do cliente , codigo da filial, codigo da filial Fatura!");
                        trataErro.mostraListaErros();
                        trataErro.lstErros.clear();
                    }
                }
                if (falha) {
                    MessageDialog.info("Algum pedido não convertido para nfce!!!\n VERIFIQUE O LOG");
                } else {
                    MessageDialog.info("Pedidos Selecionados, prontos para impressão pedo PDV em NFC-e!!\n Para continuar faturar os cupom na rotina 2075");
                    edtNumCargaNfce1.setText(edtNumCargaNfce.getText());
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnConvertePedidoNfceActionPerformed");
        }


    }//GEN-LAST:event_btnConvertePedidoNfceActionPerformed

    private void btnPesquisaConversaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaConversaoActionPerformed
        pesquisaCargaNfce();
    }//GEN-LAST:event_btnPesquisaConversaoActionPerformed

    private void mniDesVincularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniDesVincularActionPerformed
        // TODO add your handling code here:
        try {
            String numeroPedido = tblCargaNfce.getConteudoRowSelected("numpedido").toString(); // tabela rv_carregamento
            String numCargaNfce = tblCargaNfce.getConteudoRowSelected("numcarganfce").toString();// tabela rv_carregamento
            if ((!numeroPedido.isEmpty()) && (!numCargaNfce.isEmpty())) {
                desvinculaTitulos(numeroPedido, numCargaNfce);
                pesquisaCargaNfce();
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado, numero do pedido , o numero da carganfce !");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "mniDesVincularActionPerformed");
        }
    }//GEN-LAST:event_mniDesVincularActionPerformed

    private void tblCargaNfceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCargaNfceMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == MouseEvent.BUTTON3) {
            // Exibe o popup menu na posição do mouse.
            mnuTblConversao.show(tblCargaNfce, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tblCargaNfceMouseClicked

    private void mnuDesConverterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDesConverterActionPerformed
               try {
            String numeroPedido = tblCargaNfce.getConteudoRowSelected("numpedido").toString(); // tabela rv_carregamento
            String numCargaNfce = tblCargaNfce.getConteudoRowSelected("numcarganfce").toString();// tabela rv_carregamento
            if ((!numeroPedido.isEmpty()) && (!numCargaNfce.isEmpty())) {
                forcarConverterPedido(numeroPedido, numCargaNfce);
                pesquisaCargaNfce();
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado, numero do pedido , o numero da carganfce !");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "mnuDesConverterActionPerformed");
        }
    }//GEN-LAST:event_mnuDesConverterActionPerformed

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
            java.util.logging.Logger.getLogger(Brz009.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz009().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddPedidoCargaNfce;
    private javax.swing.JButton btnConvertePedidoNfce;
    private javax.swing.JButton btnImprimeLog;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisaConversao;
    private javax.swing.JButton btnPesquisaPedido;
    private javax.swing.JButton btnValidarPedido;
    private javax.swing.JButton btnVinculaTitulo;
    private javax.swing.JTextField edtCodFilialFatura;
    private javax.swing.JTextArea edtDetalhePedido;
    private javax.swing.JTextField edtNumCarga;
    private javax.swing.JTextField edtNumCargaNfce;
    private javax.swing.JTextField edtNumCargaNfce1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jtpSteps;
    private javax.swing.JMenuItem mniDesVincular;
    private javax.swing.JMenuItem mnuDesConverter;
    private javax.swing.JPopupMenu mnuTblConversao;
    private winthorDb.util.CustomTable tblCargaNfce;
    private winthorDb.util.CustomTable tblPedidoCarga;
    // End of variables declaration//GEN-END:variables
}
