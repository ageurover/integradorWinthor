/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.util.ArrayList;
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
public class Brz003 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    public Brz003() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        edtDetalhePedido.setText("");
        if (!Main.codFilial.isEmpty()){
            edtCodFilial.setText(Main.codFilial);
        }

    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private void buscaPedidoWinthor(String numPedido, String codCliente, String codFilial) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String strSelect = "select concat('NumPed: ',p.numped) as numped, concat('|DataPed: ',p.data) as data, "
                    + " concat('|Vlr. Total: ',p.vltotal) as vltotal, concat('|Posicao: ',p.posicao) as posicao,"
                    + " concat('|RCA: ', concat(u.codusur,u.nome)) as RCA, concat('|Cliente: ', concat(c.codcli, c.cliente)) as cliente "
                    + " from pcpedc p, pcusuari u, pcclient c "
                    + " where p.posicao in ('L','B','P','M','F') "
                    + " and p.condvenda in (1) "
                    + " and u.codusur = p.codusur "
                    + " and c.codcli = p.codcli "
                    + " and p.codCob in ('DH','D','CH','CHV','CHP') "
                    + " and p.vlatend < 9999.00"
                    + " and p.codfilial = " + codFilial
                    + " and p.numped = " + numPedido;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtDetalhePedido.append("Lista de Pedidos Encontrados no Winthor\n");

            for (Object object : lst.toArray()) {
                edtDetalhePedido.append(lst.size() + "\n");
                edtDetalhePedido.append(object.toString() + "\n");
            }
            if (lst.isEmpty()) {
                edtDetalhePedido.append("O Pedido não foi localizado\n");
            }
            lst = null;

        } catch (Exception ex) {
            trataErro.trataException(ex,"buscaPedidoWinthor");
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
    private void convertePedidoCupom(String numPedido, String codCliente, String codFilial, String numCargaAtual) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        try {
            wint.openConectOracle();
//            // passo 1 - Criar o carregamento para o cupom fiscal
//            String sqlString = "select FERRAMENTAS.F_PROX_NUMCAR as numcar from dual";
//            List lst = wint.selectDados(sqlString);
//            // String numcar = lst.get(0).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\.", "");
            String numcar = "1";

            // Apaga o carregamento 1 caso ele exita para somente este pedido possa ser faturado pela 2075
            strUpdate = "delete from pccarreg where  NUMCAR = " + numcar;
            result = wint.updateDados(strUpdate);
            edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
            edtDetalhePedido.append("Limpando o carregamento 1 na base de dados do winthor\n");

            // Criar o carragemento na tabela de carregamentos
            String insString = "INSERT INTO pccarreg (numcar, dtsaida, totpeso, totvolume, vltotal, destino, datamon, codveiculo, codfuncmon) "
                    + " VALUES (" + numcar + " , TRUNC(SYSDATE), 0, 0, 0, 'CONVERSAO CUPOM', TRUNC(SYSDATE), 0, 1)";
            result = wint.updateDados(insString);
            edtDetalhePedido.append("[" + result + "] Carregamento Criado: " + numcar + "\n");
            edtDetalhePedido.append("O carregamento para o pedido acima foi atualizado na base de dados do winthor\n");

            // altera o pedido caso o carregamento seja criado corretamente
            if (result > 0) {
                strUpdate = "update pcpedc set ORIGEMPED = 'R', CODCLI= " + Main.codConsumidor + " , NUMCAR = " + numcar
                        + " , OBS1 = 'CodCliOrigem: " + codCliente + " Carregamento: " + numCargaAtual + "' "
                        + " WHERE NUMPED =  " + numPedido
                        + " and codcli =  " + codCliente
                        + " and codfilial = " + codFilial;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O Cabeçalho do pedido acima foi atualizado na base de dados do winthor\n");

                // Atualiza os itens do pedido se o cabeçalho foi atualizado
                if (result > 0) {
                    strUpdate = "Update pcpedi set CODCLI=" + Main.codConsumidor + ",  NUMCAR = " + numcar + " WHERE NUMPED =  " + numPedido + " and codcli =  " + codCliente;
                    result = wint.updateDados(strUpdate);
                    edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                    edtDetalhePedido.append("Os Itens do pedido acima foram atualizado na base de dados do winthor\n");
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex,"convertePedidoCupom");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }

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
    private void vinculaTitulos(String numPedido, String codCliente, String codFilial, String numCarga, String numCupom) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        int result = 0;
        try {
            wint.openConectOracle();
            // passo 1 - Alterar o codigo do cliente na tabela de titulos do winthor (pcprest)
            String updString = "UPDATE PCPREST SET CODCLI = " + codCliente + " , numcar = " + numCarga
                    + " WHERE duplic = " + numCupom
                    + " and NUMPED =  " + numPedido
                    + " and codfilial = " + codFilial;
            result = wint.updateDados(updString);

            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Titulos do Contas a Receber vinculados ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o contas a receber do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 2 - Altera a caga das notas faturados como cupom para pode sair corretamente no romaneio
            updString = "UPDATE PCNFSAID SET NUMCAR = " + numCarga + " , CODCLI = " + codCliente
                    + " WHERE NUMNOTA = " + numCupom
                    + " and NUMPED =  " + numPedido
                    + " and codfilial = " + codFilial;
            result = wint.updateDados(updString);
            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Cabecalho da Nota Fiscal vinculado ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o cabecalho de nota fiscal do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 2.1 - Alterar os dados dos item da nota fiscal
            updString = "UPDATE PCMOV SET NUMCAR = " + numCarga + " ,CODCLI = " + codCliente
                    + " WHERE NUMNOTA = " + numCupom
                    + " and NUMPED =  " + numPedido
                    + " and codfilial = " + codFilial;
            result += wint.updateDados(updString);

            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Itens da Nota Fiscal vinculados ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o Item da nota fiscal do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 3 - Altera a caga dos pedidos faturados como cupom para pode sair corretamente no romaneio
            updString = "UPDATE PCPEDC SET NUMCAR = " + numCarga + " ,CODCLI = " + codCliente
                    + " WHERE NUMPED =  " + numPedido
                    + " and codfilial = " + codFilial;
            result += wint.updateDados(updString);
            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Cabecalho do Pedido vinculado ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o cabecalho do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // passo 3.1 - Atera os itens do pedido
            updString = "UPDATE PCPEDI SET NUMCAR = " + numCarga + " ,CODCLI = " + codCliente
                    + " WHERE NUMPED =  " + numPedido;
            result += wint.updateDados(updString);
            if (result > 0) {
                edtDetalhePedido.append("[" + result + "] Itens do Pedido vinculados ao cliente: " + codCliente + "\n");
            } else {
                MessageDialog.error("Falha ao vincular o itens do pedido: " + numPedido + " Cupom Fiscal: " + numCupom);
            }

            // Verifica se existe neste carregamento somente pedidos cupom
            String strSelect = "select "
                    + "  nvl((select count(p1.numped) as qtde from pcpedc p1 where p1.origemped = 'R' and  p1.numcar = p.numcar),0) as qtdeCupom, "
                    + "  nvl((select count(p1.numped) as qtde from pcpedc p1 where p1.origemped <> 'R' and  p1.numcar = p.numcar),0) as qtdeOutro "
                    + "from pcpedc p where ROWNUM<2 and numcar = " + numCarga;
            result = Formato.strToInt(wint.selectDados(strSelect).get(1).toString());
            if (result == 0) {
                updString = "update pccarreg set dtsaida = trunc(sysdate), dtfat = trunc(sysdate), codfuncfat=1 where numcar = " + numCarga;
                result = wint.updateDados(updString);
                if (result > 0) {
                    edtDetalhePedido.append("[" + result + "] Carregamento [" + numCarga + "] maracdo como faturado pois só existe cupom fiscal\n");
                } else {
                    MessageDialog.error("Falha ao marcar carregemento como faturado [" + numCarga + "]");
                }
            }
            edtDetalhePedido.append("Processo de Vinculação ao cliente e a carga concluidos pode continuar o faturamento da carga\n");
        } catch (Exception ex) {
            trataErro.trataException(ex,"vinculaTitulos");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * busca os dados do contas a receber gerados pelo sistema winthor apos o
     * processo de faturamento da rotina 2075
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codFilial Codigo da filial onde o pedido foi digitado
     * @param numCarga numero da carga a qual o pedido esta vinculado no
     * processo da 2075 (Padrão: 9999999 )
     */
    private boolean buscaCReceberWinthor(String duplic, String numCarga, String codFilial) throws Exception {
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
                    + " and p.codfilial = " + codFilial
                    + " and p.duplic = " + duplic;

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
            trataErro.trataException(ex,"buscaCReceberWinthor");
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
    private boolean validaPedidoCupom(String numPedido, String codCli, String codFilial) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        boolean ret = true;
        List lstDados = new ArrayList<>();

        try {
            String strSelect = "select it.numped, it.codcli, it.codprod, nvl(it.codauxiliar,0) as codauxiliar, "
                    + "it.pvenda, it.codecf, it.codfilialretira, "
                    + "    (select count(emb.codprod) as qtdEmb from pcembalagem emb  "
                    + "         where emb.codauxiliar = it.codauxiliar "
                    + "               and emb.codprod = it.codprod "
                    + "               and emb.codfilial = " + codFilial + " ) "
                    + "    as qtdEmbalagem "
                    + "  , it.bonific "
                    + " from pcpedi it "
                    + " where it.numped = " + numPedido
                    + " and it.codcli = " + codCli;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtDetalhePedido.append("Validação do Pedido para emissão do cupom fiscal\n");

            if (lst.isEmpty()) {
                edtDetalhePedido.append("O Pedido não foi localizado\n");
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
                                edtDetalhePedido.append("\nFalta o cadastro de embalagem na rotina 2014 para o produto: " + lstDados.get(2).toString() + " - " + lstDados.get(3).toString());
                            }
                            if (lstDados.get(7).toString().equalsIgnoreCase("0")) {
                                ret = false;
                                edtDetalhePedido.append("\nFalta o cadastro de embalagem na rotina 2014 para o produto: " + lstDados.get(2).toString() + " - " + lstDados.get(3).toString());
                            }
                        }
                        if ((i == 8)) {
                            if (lstDados.get(8).toString().equalsIgnoreCase("S")) {
                                ret = false;
                                edtDetalhePedido.append("\nProduto Bonificado no pedido: " + lstDados.get(2).toString() + " - " + lstDados.get(8).toString());
                            }
                        }
                    }

                    if (ret) {
                        edtDetalhePedido.append("\nO Produto " + lstDados.get(2).toString() + " esta Ok");
                    }
                }
            }

            lst = null;

            if (ret) {
                edtDetalhePedido.append("\nO Pedido esta OK para emissão do cupom fiscal");
            } else {
                edtDetalhePedido.append("\nO Pedido NAO esta preparado para emissão do cupom fiscal\n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"validaPedidoCupom");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtpSteps = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        edtNumeroPedido = new javax.swing.JTextField();
        btnPesquisaPedido = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtDetalhePedido = new javax.swing.JTextArea();
        btnConvertePedido = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        edtCodFilial = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edtCodCliente = new javax.swing.JTextField();
        btnLimpar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        edtNumCarga = new javax.swing.JTextField();
        edtNumCupom = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnVinculaTitulo = new javax.swing.JButton();
        btnValidarPedido = new javax.swing.JButton();
        btnImprimeLog = new javax.swing.JButton();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz003");

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

        jLabel4.setText("Núm.Pedido:");

        btnPesquisaPedido.setText("Pesquisar");
        btnPesquisaPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaPedidoActionPerformed(evt);
            }
        });

        edtDetalhePedido.setColumns(20);
        edtDetalhePedido.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        edtDetalhePedido.setLineWrap(true);
        edtDetalhePedido.setRows(5);
        edtDetalhePedido.setWrapStyleWord(true);
        jScrollPane1.setViewportView(edtDetalhePedido);

        btnConvertePedido.setText("Converter Pedido >>");
        btnConvertePedido.setEnabled(false);
        btnConvertePedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConvertePedidoActionPerformed(evt);
            }
        });

        jLabel5.setText("Cod. Filial:");

        edtCodFilial.setText("1");
        edtCodFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodFilialActionPerformed(evt);
            }
        });

        jLabel6.setText("Cod. Cliente:");

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel12.setText("Num. Carga:");

        edtNumCupom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtNumCupomFocusLost(evt);
            }
        });
        edtNumCupom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtNumCupomActionPerformed(evt);
            }
        });
        edtNumCupom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtNumCupomKeyPressed(evt);
            }
        });

        jLabel11.setText("Núm.Cupom:");

        btnVinculaTitulo.setText("Vincular Titulos");
        btnVinculaTitulo.setEnabled(false);
        btnVinculaTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVinculaTituloActionPerformed(evt);
            }
        });

        btnValidarPedido.setText("Validar Pedido >>");
        btnValidarPedido.setEnabled(false);
        btnValidarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarPedidoActionPerformed(evt);
            }
        });

        btnImprimeLog.setText("Imprimir Log");
        btnImprimeLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnImprimeLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPesquisaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnValidarPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConvertePedido)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCupom, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVinculaTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(edtCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(edtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimpar)
                        .addComponent(btnPesquisaPedido))
                    .addComponent(btnImprimeLog))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnVinculaTitulo)
                        .addComponent(jLabel11)
                        .addComponent(edtNumCupom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnConvertePedido)
                        .addComponent(btnValidarPedido)))
                .addContainerGap())
        );

        jtpSteps.addTab("1 - Converte Pedido/Cupom", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jtpSteps)
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
            if ((!edtNumeroPedido.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                buscaPedidoWinthor(edtNumeroPedido.getText(), edtCodCliente.getText(), edtCodFilial.getText());
                btnPesquisaPedido.setEnabled(false);
//                edtNumeroPedido.setEditable(false);
//                edtCodCliente.setEditable(false);
//                edtCodFilial.setEditable(false);
//                edtNumCarga.setEditable(false);
                btnValidarPedido.setEnabled(true);
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o codigo do cliente, o codigo da filial e o numero do carregamento !!!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"btnPesquisaPedidoActionPerformed");
        }
    }//GEN-LAST:event_btnPesquisaPedidoActionPerformed

    private void edtCodFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialActionPerformed

    }//GEN-LAST:event_edtCodFilialActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        btnPesquisaPedido.setEnabled(true);
        edtNumeroPedido.setEditable(true);
        edtCodCliente.setEditable(true);
        edtCodFilial.setEditable(true);
        edtNumCarga.setEditable(true);
        btnConvertePedido.setEnabled(false);
        btnValidarPedido.setEnabled(false);
        edtNumCupom.setEditable(false);
        btnVinculaTitulo.setEnabled(false);
        edtCodFilial.setText("1");
        edtNumeroPedido.setText("");
        edtCodCliente.setText("");
        edtNumCarga.setText("");
        edtNumCupom.setText("");
        edtDetalhePedido.setText("");

    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnConvertePedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConvertePedidoActionPerformed
        try {
            if ((!edtNumeroPedido.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                convertePedidoCupom(edtNumeroPedido.getText(), edtCodCliente.getText(), edtCodFilial.getText(), edtNumCarga.getText());
                MessageDialog.info("Conversão de Pedido em Cupom Realizada! \n * Realize o Faturamento da carga 1 pela rotina 2075 do winthor! \n ** Apos o faturamento continue com a proxima etapa!");
                //btnVinculaTitulo.setEnabled(true);
                edtNumCupom.setEditable(true);
                buscaCReceberWinthor(edtNumeroPedido.getText(), edtNumCarga.getText(), edtCodFilial.getText());
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o codigo do cliente e o codigo da filial!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"btnConvertePedidoActionPerformed");
        }
    }//GEN-LAST:event_btnConvertePedidoActionPerformed

    private void btnVinculaTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVinculaTituloActionPerformed
        try {

            if ((!edtNumeroPedido.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty()) && (!edtNumCupom.getText().isEmpty())) {
                if (buscaCReceberWinthor(edtNumCupom.getText(), "1", edtCodFilial.getText())) {
                    vinculaTitulos(edtNumeroPedido.getText(), edtCodCliente.getText(), edtCodFilial.getText(), edtNumCarga.getText(), edtNumCupom.getText());
                    MessageDialog.info("Vinculação dos titulos Realizada! ");

                } else {
                    MessageDialog.info("O Pedido ainda não esta faturado no winthor \n Tente novamente em alguns minutos !!!"
                            + "\nNumeroPedido: " + edtNumeroPedido.getText()
                            + "\nCod.Cliente: " + edtCodCliente.getText()
                            + "\nCod.Filia: " + edtCodFilial.getText()
                            + "\nNum. Carga: " + edtNumCarga.getText()
                            + "\nNum. Cupom: " + edtNumCupom.getText());
                }
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o numero da carga, o codigo do cliente e o codigo da filial!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"btnVinculaTituloActionPerformed");
        }
    }//GEN-LAST:event_btnVinculaTituloActionPerformed

    private void btnValidarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarPedidoActionPerformed
        try {

            if ((!edtNumeroPedido.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty())) {
                if (validaPedidoCupom(edtNumeroPedido.getText(), edtCodCliente.getText(), edtCodFilial.getText())) {
                    MessageDialog.info("Pedido OK para gerar o Cupom fiscal !!! \n");
                    btnConvertePedido.setEnabled(true);
                } else {
                    MessageDialog.info("Existem produtos com problemas de cadastro que impedem a geração do cupom fiscal !!!");
                }
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o numero da carga, o codigo do cliente e o codigo da filial!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex,"btnValidarPedidoActionPerformed");
        }
    }//GEN-LAST:event_btnValidarPedidoActionPerformed

    private void btnImprimeLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeLogActionPerformed
        try {
            edtDetalhePedido.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 8));
            edtDetalhePedido.print();

        } catch (PrinterException ex) {
            trataErro.trataException(ex,"btnImprimeLogActionPerformed");
        } finally {
            edtDetalhePedido.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 10));
        }
    }//GEN-LAST:event_btnImprimeLogActionPerformed

    private void edtNumCupomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtNumCupomKeyPressed
        if ((evt.getKeyCode() >= KeyEvent.VK_0) && (evt.getKeyCode() <= KeyEvent.VK_9)) {
            btnVinculaTitulo.setEnabled(true);
        }
    }//GEN-LAST:event_edtNumCupomKeyPressed

    private void edtNumCupomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtNumCupomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtNumCupomActionPerformed

    private void edtNumCupomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtNumCupomFocusLost
        // TODO add your handling code here
        if (!edtNumCupom.getText().isEmpty()) {
            btnVinculaTitulo.setEnabled(true);
        }
    }//GEN-LAST:event_edtNumCupomFocusLost

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
            java.util.logging.Logger.getLogger(Brz003.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz003().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConvertePedido;
    private javax.swing.JButton btnImprimeLog;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisaPedido;
    private javax.swing.JButton btnValidarPedido;
    private javax.swing.JButton btnVinculaTitulo;
    private javax.swing.JTextField edtCodCliente;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextArea edtDetalhePedido;
    private javax.swing.JTextField edtNumCarga;
    private javax.swing.JTextField edtNumCupom;
    private javax.swing.JTextField edtNumeroPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jtpSteps;
    // End of variables declaration//GEN-END:variables
}
