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

/**
 *
 * @author ageurover
 */
public class Brz007 extends javax.swing.JFrame {

    /**
     * Creates new form Brz001
     */
    public Brz007() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        edtLogProcesso.setText("");
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
    private void buscaNotaFiscalWinthor(String numTransVenda, String codFilial) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String strSelect = "select concat('NumNota: ',nf.numnota) as numNota, concat('|DataFat: ',nf.dtsaida) as dataFatura, "
                    + "concat('|Vlr. Total: ',nf.vltotger) as vltotal, concat('|Posicao: ',nf.numcar) as carga, "
                    + "concat('|RCA: ', concat(u.codusur,u.nome)) as RCA, concat('|Cliente: ', concat(c.codcli, c.cliente)) as cliente "
                    + "from pcnfsaid nf, pcusuari u, pcclient c  "
                    + "where nf.numtransvenda = " + numTransVenda
                    + "and nf.codfilial = " + codFilial
                    + "and u.codusur = p.codusur "
                    + "and c.codcli = p.codcli";

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtLogProcesso.append("Lista de Notas Fiscais Encontrados no Winthor\n");

            for (Object object : lst.toArray()) {
                edtLogProcesso.append(lst.size() + "\n");
                edtLogProcesso.append(object.toString() + "\n");
            }
            if (lst.isEmpty()) {
                edtLogProcesso.append("A Nota Fiscal não foi localizada\n");
            }
            lst = null;

        } catch (Exception ex) {
            trataErro.trataException(ex);
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
    private void converteNotaFiscalConsumidor(String numTransVenda, String codCliente, String codFilial, String numNota) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        try {
            wint.openConectOracle();

            // passo 1 - Alterar o codigo do cliente na tabela de titulos do winthor (pcprest)
            String updString = "UPDATE PCPREST SET CODCLI = " + Main.codConsumidor + ", obs = 'Cliente Original " + codCliente + "' "
                    + " WHERE numtransvenda = " + numTransVenda
                    + " and duplic =  " + numNota
                    + " and codfilial = " + codFilial;
            result = wint.updateDados(updString);

            if (result > 0) {
                edtLogProcesso.append("[" + result + "] Titulos do Contas a Receber convertido com sucesso \n");
            }

            // passo 2 - Altera a caga das notas faturados como cupom para pode sair corretamente no romaneio
            updString = "UPDATE PCNFSAID SET CODCLI = " + Main.codConsumidor
                    + " WHERE numtransvenda = " + numTransVenda
                    + " and codfilial = " + codFilial;
            result = wint.updateDados(updString);
            if (result > 0) {
                edtLogProcesso.append("[" + result + "] Cabecalho da Nota Fiscal convertida com sucesso \n");
            }

            // passo 2.1 - Alterar os dados dos item da nota fiscal
            updString = "UPDATE PCMOV SET CODCLI = " + Main.codConsumidor
                    + " WHERE numtransvenda = " + numTransVenda
                    + " and codfilial = " + codFilial;
            result += wint.updateDados(updString);

            if (result > 0) {
                edtLogProcesso.append("[" + result + "] Itens da Nota Fiscal convertido com sucesso\n");
            }

            edtLogProcesso.append("Processo de conversao para o cliente consumidorconcluidos pode continuar o processo de devolição de cupom fiscal\n");

        } catch (Exception ex) {
            trataErro.trataException(ex);
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
    private void vinculaTitulos(String numTransVenda, String codCliente, String codFilial, String numCarga, String numCupom) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        int result = 0;
        try {
            wint.openConectOracle();
            // passo 1 - Alterar o codigo do cliente na tabela de titulos do winthor (pcprest)
            String updString = "UPDATE PCPREST SET CODCLI = " + codCliente + " ,obs = ' '"
                    + " WHERE duplic = " + numCupom
                    + " and CODCLI =  " + Main.codConsumidor
                    + " and codfilial = " + codFilial;
            result = wint.updateDados(updString);

            if (result > 0) {
                edtLogProcesso.append("[" + result + "] Titulos do Contas a Receber vinculados ao cliente: " + codCliente + "\n");
            }

            // passo 2 - Altera a caga das notas faturados como cupom para pode sair corretamente no romaneio
            updString = "UPDATE PCNFSAID SET CODCLI = " + codCliente
                    + " WHERE numtransvenda = " + numTransVenda
                    + " and codfilial = " + codFilial;
            result = wint.updateDados(updString);
            if (result > 0) {
                edtLogProcesso.append("[" + result + "] Cabecalho da Nota Fiscal vinculado ao cliente: " + codCliente + "\n");
            }

            // passo 2.1 - Alterar os dados dos item da nota fiscal
            updString = "UPDATE PCMOV SET CODCLI = " + codCliente
                    + " WHERE numtransvenda = " + numTransVenda
                    + " and codfilial = " + codFilial;
            result += wint.updateDados(updString);

            if (result > 0) {
                edtLogProcesso.append("[" + result + "] Itens da Nota Fiscal vinculados ao cliente: " + codCliente + "\n");
            }

            edtLogProcesso.append("Processo de Vinculação ao cliente concluidos\n");
        } catch (Exception ex) {
            trataErro.trataException(ex);
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
    private boolean buscaCReceberWinthor(String numNotaFiscal, String codFilial) throws Exception {
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
                    + " and p.codcob in ('DEVP','DEVT')"
                    + " and p.duplic = " + numNotaFiscal
                    + " and p.codcli = " + Main.codConsumidor
                    + " and p.codfilial = " + codFilial;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtLogProcesso.append("Contas a Receber encontrado no Winthor\n");

            for (Object object : lst.toArray()) {
                edtLogProcesso.append(lst.size() + "\n");
                edtLogProcesso.append(object.toString() + "\n");

            }
            if (lst.isEmpty()) {
                edtLogProcesso.append("O Contas a Receber da Devolucao não foi localizado\n");
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
                    + " from pcpedi it "
                    + " where it.numped = " + numPedido
                    + " and it.codcli = " + codCli;

            wint.openConectOracle();
            List lst = wint.selectDados(strSelect);

            edtLogProcesso.append("Validação do Pedido para emissão do cupom fiscal\n");

            if (lst.isEmpty()) {
                edtLogProcesso.append("O Pedido não foi localizado\n");
                ret = false;
            } else {

                for (Object lst1 : lst) {
                    lstDados = (List) lst1;
                    for (int i = 0; i < lstDados.size(); i++) {

                        if (i == 3) {
                            if (lstDados.get(3) == null) {
                                ret = false;
                                edtLogProcesso.append("\nFalta o codigo de Barras no cadastrado do produto: " + lstDados.get(2).toString() + "\n\tpara corrigir edite o cadastro do produto na 203 e remova e inclua o item pela 336");
                            }
                            if (lstDados.get(3).toString().equalsIgnoreCase("0")) {
                                ret = false;
                                edtLogProcesso.append("\nFalta o codigo de Barras cadastrado no pedido para o produto: " + lstDados.get(2).toString() + "\n\tpara corrigir edite o cadastro do produto na 203 e remova e inclua o item pela 336");
                            }
                        }
//                        if ((i == 5) && (lstDados.get(i).toString().isEmpty())) {
//                            ret = false;
//                            edtDetalhePedido.append("\nFalta a tributacao da ECF para o produto: " + lstDados.get(2).toString());
//                        }
                        if ((i == 7)) {
                            if (lstDados.get(7).toString().isEmpty()) {
                                ret = false;
                                edtLogProcesso.append("\nFalta o cadastro de embalagem na rotina 2014 para o produto: " + lstDados.get(2).toString() + " - " + lstDados.get(3).toString());
                            }
                            if (lstDados.get(7).toString().equalsIgnoreCase("0")) {
                                ret = false;
                                edtLogProcesso.append("\nFalta o cadastro de embalagem na rotina 2014 para o produto: " + lstDados.get(2).toString() + " - " + lstDados.get(3).toString());
                            }
                        }
                    }

                    if (ret) {
                        edtLogProcesso.append("\nO Produto " + lstDados.get(2).toString() + " esta Ok");
                    }
                }
            }

            lst = null;

            if (ret) {
                edtLogProcesso.append("\nO Pedido esta OK para emissão do cupom fiscal");
            } else {
                edtLogProcesso.append("\nO Pedido NAO esta preparado para emissão do cupom fiscal\n");
            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
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
        edtNumTransVenda = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtLogProcesso = new javax.swing.JTextArea();
        btnConverteNotaFiscal = new javax.swing.JButton();
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
        btnImprimeLog = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        edtNumNotaDev = new javax.swing.JTextField();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz007");

        jLabel1.setText("Processar faturamento de cupom para fazer devolução como consumidor final");

        jLabel3.setText("* * Os pedidos devem estar faturados antes de realizar este procedimento");

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados do faturamento"));

        jLabel4.setText("Núm. Trans. Venda:");

        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        edtLogProcesso.setColumns(20);
        edtLogProcesso.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        edtLogProcesso.setLineWrap(true);
        edtLogProcesso.setRows(5);
        edtLogProcesso.setWrapStyleWord(true);
        jScrollPane1.setViewportView(edtLogProcesso);

        btnConverteNotaFiscal.setText("Converter Nota Fiscal >>");
        btnConverteNotaFiscal.setEnabled(false);
        btnConverteNotaFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConverteNotaFiscalActionPerformed(evt);
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

        btnImprimeLog.setText("Imprimir Log");
        btnImprimeLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeLogActionPerformed(evt);
            }
        });

        jLabel13.setText("Núm.Nota Devlução:");

        edtNumNotaDev.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtNumNotaDevFocusLost(evt);
            }
        });
        edtNumNotaDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtNumNotaDevActionPerformed(evt);
            }
        });
        edtNumNotaDev.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtNumNotaDevKeyPressed(evt);
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
                        .addComponent(edtNumTransVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnImprimeLog)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumCupom, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnConverteNotaFiscal)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNumNotaDev, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(edtNumTransVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(edtCodFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimpar)
                        .addComponent(btnPesquisar))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(edtNumCupom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnImprimeLog))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(edtNumNotaDev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnVinculaTitulo)
                        .addComponent(btnConverteNotaFiscal)))
                .addContainerGap())
        );

        jtpSteps.addTab("1 - Converte Faturamento para Devolução de consumidor final", jPanel2);

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

        jtpSteps.getAccessibleContext().setAccessibleName("1 - Converte Faturamento para Devolução");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        try {
            if ((!edtNumTransVenda.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                buscaNotaFiscalWinthor(edtNumTransVenda.getText(), edtCodFilial.getText());
                btnPesquisar.setEnabled(false);
                btnConverteNotaFiscal.setEnabled(true);
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero da Transação de venda, o codigo do cliente, o codigo da filial e o numero do carregamento !!!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void edtCodFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialActionPerformed

    }//GEN-LAST:event_edtCodFilialActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        btnPesquisar.setEnabled(true);
        edtNumTransVenda.setEditable(true);
        edtCodCliente.setEditable(true);
        edtCodFilial.setEditable(true);
        edtNumCarga.setEditable(true);
        btnConverteNotaFiscal.setEnabled(false);
        edtNumCupom.setEditable(false);
        btnVinculaTitulo.setEnabled(false);
        edtLogProcesso.setText("");

        if (MessageDialog.ask("Deseja Limpar os dados dos filtros?") == MessageDialog.YES_OPTION) {
            edtCodFilial.setText("1");
            edtNumTransVenda.setText("");
            edtCodCliente.setText("");
            edtNumCarga.setText("");
            edtNumCupom.setText("");
        }


    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnConverteNotaFiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConverteNotaFiscalActionPerformed
        try {
            if ((!edtNumTransVenda.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty()) && (!edtNumCupom.getText().isEmpty())) {
                converteNotaFiscalConsumidor(edtNumTransVenda.getText(), edtCodCliente.getText(), edtCodFilial.getText(), edtNumCupom.getText());
                MessageDialog.info("Conversão para Cliente Consumidor Realizada! \n * Realize o processo de devolucao de cupom fiscal do winthor! \n ** Apos concluir continue com a proxima etapa!");
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o codigo do cliente e o codigo da filial!");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnConverteNotaFiscalActionPerformed

    private void btnVinculaTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVinculaTituloActionPerformed
        try {

            if ((!edtNumTransVenda.getText().isEmpty()) && (!edtCodCliente.getText().isEmpty()) && (!edtCodFilial.getText().isEmpty()) && (!edtNumNotaDev.getText().isEmpty())) {
                if (buscaCReceberWinthor(edtNumCupom.getText(), edtCodFilial.getText())) {
                    vinculaTitulos(edtNumTransVenda.getText(), edtCodCliente.getText(), edtCodFilial.getText(), edtNumCarga.getText(), edtNumCupom.getText());
                    MessageDialog.info("Vinculação dos titulos Realizada! ");

                } else {
                    MessageDialog.info("Devolução nao localizada! \n Tente novamente em alguns minutos !!!"
                            + "\nNumTransVenda: " + edtNumTransVenda.getText()
                            + "\nCod.Cliente: " + edtCodCliente.getText()
                            + "\nCod.Filial: " + edtCodFilial.getText()
                            + "\nNum. Carga: " + edtNumCarga.getText()
                            + "\nNum. Cupom: " + edtNumCupom.getText());
                }
            } else {
                trataErro.lstErros.clear();
                trataErro.addListaErros("ATENÇÃO: Deve ser informado todos os campos do filtro para proceguir !");
                trataErro.mostraListaErros();
                trataErro.lstErros.clear();

            }
        } catch (Exception ex) {
            trataErro.trataException(ex);
        }
    }//GEN-LAST:event_btnVinculaTituloActionPerformed

    private void btnImprimeLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeLogActionPerformed
        try {
            edtLogProcesso.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 8));
            edtLogProcesso.print();

        } catch (PrinterException ex) {
            trataErro.trataException(ex);
        } finally {
            edtLogProcesso.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 10));
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

    }//GEN-LAST:event_edtNumCupomFocusLost

    private void edtNumNotaDevFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtNumNotaDevFocusLost
        // TODO add your handling code here:
        if (!edtNumNotaDev.getText().isEmpty()) {
            btnVinculaTitulo.setEnabled(true);
        }
    }//GEN-LAST:event_edtNumNotaDevFocusLost

    private void edtNumNotaDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtNumNotaDevActionPerformed
        // TODO add your handling code here:
        if (!edtNumNotaDev.getText().isEmpty()) {
            btnVinculaTitulo.setEnabled(true);
        }
    }//GEN-LAST:event_edtNumNotaDevActionPerformed

    private void edtNumNotaDevKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtNumNotaDevKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtNumNotaDevKeyPressed

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
            java.util.logging.Logger.getLogger(Brz007.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz007().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConverteNotaFiscal;
    private javax.swing.JButton btnImprimeLog;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnVinculaTitulo;
    private javax.swing.JTextField edtCodCliente;
    private javax.swing.JTextField edtCodFilial;
    private javax.swing.JTextArea edtLogProcesso;
    private javax.swing.JTextField edtNumCarga;
    private javax.swing.JTextField edtNumCupom;
    private javax.swing.JTextField edtNumNotaDev;
    private javax.swing.JTextField edtNumTransVenda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
