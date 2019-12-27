/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms;

import java.awt.print.PrinterException;
import javax.swing.ListSelectionModel;
import winthorDb.Main;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;

/**
 *
 * @author ageurover
 */
public class Brz0012 extends javax.swing.JFrame {

    int[] selecionados = null;

    /**
     * Creates new form Brz001
     */
    public Brz0012() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        edtDetalhePedido.setText("");

        // permite selecionar mais de uma linha de dados.
        tblPedidoCarga.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblPedidoCarga.setColumnSelectionAllowed(false);
        tblPedidoCarga.setRowSelectionAllowed(true);

        if (!Main.codFilialFatura.isEmpty()) {
            edtCodFilialPedido.setText(Main.codFilial);
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
            String strSelect = "SELECT p.numcar, ('' || p.numped) as numped, p.ORIGEMPED, p.data, p.vltotal, p.vlatend, p.codcli, c.cliente, p.codfilial, p.codfilialnf, c.codfilialnf FilialFat_Cli ,"
                    + " p.posicao, u.codusur, u.nome as NomeRCA, p.dtfat, p.numcupom, p.obs, p.obs1, p.obs2  "
                    + " FROM pcpedc p, pcusuari u, pcclient c "
                    + " WHERE p.posicao in ('L','B','P','M') "
                    // + " AND p.condvenda in (1,5) "
                    + " AND u.codusur = p.codusur "
                    + " AND c.codcli = p.codcli "
                    + " AND p.numcar = " + numCarga
                    + " ORDER BY p.numped ";

            /*
                    + " AND p.codCob in ('DH','D','CH','CHV','CHP') "
                    + " AND p.vlatend < 9999.00 "
                    + " AND p.numped not in (select it.numped from pcpedi it where it.numcar = p.numcar and it.bonific in ('S','F') and it.pbonific > 0 ) "
             */
            wint.openConectOracle();
            //tblPedidoCarga.setTableData(wint.selectResultSet(strSelect));
            
            tblPedidoCarga.setTableData(strSelect);

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
     * Faz a conversão do Pedido para cupom fiscal para que possa ser faturado
     * pela rotina 2075 seguindo os padroes do winthor
     *
     * @param numPedido Numero do pedido digitado no sistema winthor
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     * @param codFilial Codigo da filial onde o pedido foi digitado
     */
    private boolean addPedidoFilialFatura(String numPedido, String codCliente, String numCargaAtual, String codFilialPedido, String codFilialFatura) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        boolean ret = false;
        try {
            wint.openConectOracle();

            // altera a filial fatura do cliente
            if (result >= 0) {
                strUpdate = "update pcclient  set codfilialnf = " + codFilialFatura
                        + " WHERE codcli =  " + codCliente;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Comando: " + strUpdate + "\n");
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O cliente do pedido acima foi atualizado na base de dados do winthor\n");
                ret = (result != 0);
            }

            // altera o pedido caso o carregamento seja criado corretamente
            if (result >= 0) {
                String obs = " 'Carga: " + numCargaAtual + "' ";

                strUpdate = "update pcpedc set codfilialnf = " + codFilialFatura
                        + " , OBS2 = " + (obs.length() < 50 ? obs : obs.subSequence(0, 49))
                        + " WHERE NUMPED =  " + numPedido
                        + " and codcli =  " + codCliente
                        + " and codfilial = " + codFilialPedido;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Comando: " + strUpdate + "\n");
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O Cabeçalho do pedido " + numPedido + " foi atualizado na base de dados do winthor\n");
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
     * @param codCliente Codigo do cliente no qual o pedido foi digitado
     */
    private boolean limpaClienteFilialFatura(String codCliente) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strUpdate = "";
        int result = 0;
        boolean ret = false;
        try {
            wint.openConectOracle();

            // altera a filial fatura do cliente
            if (result >= 0) {
                strUpdate = "update pcclient  set codfilialnf = null WHERE codcli =  " + codCliente;

                result = wint.updateDados(strUpdate);
                edtDetalhePedido.append("Comando: " + strUpdate + "\n");
                edtDetalhePedido.append("Registros Atualizados: " + result + "\n");
                edtDetalhePedido.append("O cliente do pedido acima foi atualizado na base de dados do winthor\n");
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
        btnPesquisaPedido = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        edtNumCarga = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPedidoCarga = new winthorDb.util.CustomTable();
        jLabel8 = new javax.swing.JLabel();
        edtCodFilialPedido = new javax.swing.JTextField();
        btnConverteFilialFatura = new javax.swing.JButton();
        edtCodFilialFatura = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnLimpaFilialFaturaCliente = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        edtDetalhePedido = new javax.swing.JTextArea();
        btnImprimeLog = new javax.swing.JButton();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz012");

        jLabel1.setText("Converter pedidos do força de vendas para a filial virtual");

        jLabel3.setText("* * Os pedidos devem estar montados e não faturados");

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

        jLabel12.setText("Num. Carga:");

        tblPedidoCarga.setToolTipText("");
        tblPedidoCarga.setCellSelectionEnabled(true);
        jScrollPane3.setViewportView(tblPedidoCarga);

        jLabel8.setText("Cod. Filial Pedido:");

        edtCodFilialPedido.setText("1");
        edtCodFilialPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodFilialPedidoActionPerformed(evt);
            }
        });

        btnConverteFilialFatura.setText("1 - Converte Pedido/Cliente Filial Fatura");
        btnConverteFilialFatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConverteFilialFaturaActionPerformed(evt);
            }
        });

        edtCodFilialFatura.setText("2");
        edtCodFilialFatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodFilialFaturaActionPerformed(evt);
            }
        });

        jLabel9.setText("Cod. Filial Fatura:");

        btnLimpaFilialFaturaCliente.setText("2 - Limpa Filial Fatura Cliente");
        btnLimpaFilialFaturaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpaFilialFaturaClienteActionPerformed(evt);
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodFilialPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodFilialFatura, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPesquisaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnConverteFilialFatura)
                                .addGap(18, 18, 18)
                                .addComponent(btnLimpaFilialFaturaCliente)))
                        .addGap(0, 237, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(edtCodFilialFatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(edtCodFilialPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(edtNumCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLimpar)
                        .addComponent(btnPesquisaPedido)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConverteFilialFatura)
                    .addComponent(btnLimpaFilialFaturaCliente))
                .addContainerGap())
        );

        jtpSteps.addTab("1 - Selecionar Pedidos", jPanel2);

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
                        .addGap(0, 981, Short.MAX_VALUE)))
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
            if ((!edtCodFilialPedido.getText().isEmpty()) && (!edtCodFilialPedido.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                buscaPedidoWinthor(edtNumCarga.getText());
                if (tblPedidoCarga.getRowCount() > 0) {
                    btnPesquisaPedido.setEnabled(true);
                    btnConverteFilialFatura.setEnabled(true);
                    btnLimpaFilialFaturaCliente.setEnabled(true);
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
        btnConverteFilialFatura.setEnabled(false);
        btnLimpaFilialFaturaCliente.setEnabled(false);

        edtCodFilialPedido.setText(Main.codFilial);
        edtCodFilialFatura.setText(Main.codFilialFatura);
        edtNumCarga.setEditable(true);
        edtNumCarga.setText("");
        edtDetalhePedido.setText("");

        tblPedidoCarga.clearTableData();
        selecionados = null;

    }//GEN-LAST:event_btnLimparActionPerformed

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

    private void edtCodFilialPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodFilialPedidoActionPerformed

    private void btnConverteFilialFaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConverteFilialFaturaActionPerformed
        try {
            String numeroPedido = "";
            String codCliente = "";
            String codFilial = "";
            boolean valida = true;
            boolean falha = false;

            if (selecionados == null) {
                selecionados = tblPedidoCarga.getSelectedRows();
            }

            if (selecionados == null) {
                MessageDialog.info("Não Existem pedidos selecionados para adicionar !!!\n");
            } else {

                // processa os pedidos selecionados
                for (int i : selecionados) {
                    numeroPedido = tblPedidoCarga.getConteudoRow("numped", i).toString();
                    codCliente = tblPedidoCarga.getConteudoRow("codcli", i).toString();
                    codFilial = tblPedidoCarga.getConteudoRow("codfilial", i).toString();

                    if ((!numeroPedido.isEmpty()) && (!codCliente.isEmpty()) && (!edtCodFilialFatura.getText().isEmpty()) && (!edtCodFilialPedido.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                        valida = addPedidoFilialFatura(numeroPedido, codCliente, edtNumCarga.getText(), codFilial, edtCodFilialFatura.getText());
                        if (!valida) {
                            falha = true;
                        }
                    } else {
                        trataErro.lstErros.clear();
                        trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero do pedido, o numero da carga,  o codigo do cliente , codigo da filial do pedido, codigo da filial Fatura!");
                        trataErro.mostraListaErros();
                        trataErro.lstErros.clear();
                    }
                }
                if (falha) {
                    MessageDialog.info("Algum pedido não foi adicionado a carga nfce!!!\n VERIFIQUE O LOG");
                } else {
                    MessageDialog.info("Carga Selecionada, vinculada com sucesso na filial fatura!!\n Para continuar, efetuar a transferencia de mercadoria pela rotina 1419\n Apos a transferencia fatura a carga pela rotina 1402");
                    buscaPedidoWinthor(edtNumCarga.getText());
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnAddPedidoCargaNfceActionPerformed");
        }

    }//GEN-LAST:event_btnConverteFilialFaturaActionPerformed

    private void edtCodFilialFaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodFilialFaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodFilialFaturaActionPerformed

    private void btnLimpaFilialFaturaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpaFilialFaturaClienteActionPerformed
        try {
            String codCliente = "";
            boolean valida = true;
            boolean falha = false;

            if (selecionados == null) {
                selecionados = tblPedidoCarga.getSelectedRows();
            }

            if (selecionados == null) {
                MessageDialog.info("Não Existem pedidos selecionados para processamento !!!\n");
            } else {

                // processa os pedidos selecionados
                for (int i : selecionados) {
                    codCliente = tblPedidoCarga.getConteudoRow("codcli", i).toString();

                    if ((!codCliente.isEmpty()) && (!edtCodFilialFatura.getText().isEmpty()) && (!edtCodFilialPedido.getText().isEmpty()) && (!edtNumCarga.getText().isEmpty())) {
                        valida = limpaClienteFilialFatura(codCliente);
                        if (!valida) {
                            falha = true;
                        }
                    } else {
                        trataErro.lstErros.clear();
                        trataErro.addListaErros("ATENÇÃO: Deve ser informado o numero da carga,  o codigo do cliente , codigo da filial do pedido, codigo da filial Fatura!");
                        trataErro.mostraListaErros();
                        trataErro.lstErros.clear();
                    }
                }
                if (falha) {
                    MessageDialog.info("Erro no processamento!!!\n VERIFIQUE O LOG");
                } else {
                    MessageDialog.info("Filial Fatura removida do cadastros dos clientes!");
                    buscaPedidoWinthor(edtNumCarga.getText());
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnLimpaFilialFaturaClienteActionPerformed");
        }
    }//GEN-LAST:event_btnLimpaFilialFaturaClienteActionPerformed

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
            java.util.logging.Logger.getLogger(Brz0012.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Brz0012().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConverteFilialFatura;
    private javax.swing.JButton btnImprimeLog;
    private javax.swing.JButton btnLimpaFilialFaturaCliente;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisaPedido;
    private javax.swing.JTextField edtCodFilialFatura;
    private javax.swing.JTextField edtCodFilialPedido;
    private javax.swing.JTextArea edtDetalhePedido;
    private javax.swing.JTextField edtNumCarga;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jtpSteps;
    private winthorDb.util.CustomTable tblPedidoCarga;
    // End of variables declaration//GEN-END:variables
}
