/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms.etl.cafe3Coracoes;

import java.math.BigDecimal;
import java.util.Date;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.dao.DaoDirect;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.*;

/**
 *
 * @author ageurover
 */
public class LancarNotaServico extends javax.swing.JDialog {

    boolean insert = true;

    /**
     * Creates new form
     *
     * @param parent
     * @param modal
     */
    public LancarNotaServico(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);
        setTitle("Nota de Serviço");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
    }

    public static void open() {
        new LancarNotaServico(null, true).setVisible(true);
    }

    /**
     * Faz a busca dos dados da nota de serviço
     *
     */
    private void buscaNotaServico() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String filtro = " ";

            if (!edtNrNfs.getText().isEmpty()) {
                filtro += " AND N.NUMERO = '" + edtNrNfs.getText() + "' ";
            }

            String strSelect = "SELECT * FROM KT_NOTASERVICO N "
                    + "   WHERE 1=1 \n"
                    + filtro
                    + "   ORDER BY N.NUMERO";

            wint.openConectOracle();
            tblNfServico.clearTableData();
            tblNfServico.setTableData(wint.selectResultSet(strSelect));
            buscaNotaVinculada();

            if (tblNfServico.getRowCount() >= 1) {
                insert = false;
            } else {
                insert = true;
            }
            lblStatus.setText((insert?"Novo":"Atualiza"));
        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaNotaServico");
        } finally {
            wint.closeConectOracle();
        }

    }

    /**
     * Faz a busca dos dados do pedido no sistema winthor para que seja
     * confirmado pelo usuario.
     *
     */
    private void buscaNotaVinculada() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            String filtro = " ";
            if (!edtNrNfs.getText().isEmpty()) {
                filtro += " AND nv.numero_nfs = '" + edtNrNfs.getText() + "' ";

                String strSelect = "SELECT * FROM kt_notaservicovinculada nv \n"
                        + "   WHERE 1=1 \n"
                        + filtro
                        + "   ORDER BY nv.numero_nfs, nv.numero_nota";

                wint.openConectOracle();
                tblNfServicoVinculada.clearTableData();
                tblNfServicoVinculada.setTableData(wint.selectResultSet(strSelect));
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "buscaNotaVinculada");
        } finally {
            wint.closeConectOracle();
        }

    }

    private boolean gravarNotaServico() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strInsert = "";
        int result = 0;
        boolean ret = false;
        try {
            if (!edtNrNfs.getText().isEmpty()) {
                wint.openConectOracle();

                if (insert) {
                    // altera a data do pedido
                    strInsert = "INSERT INTO KT_NOTASERVICO (numero,serie,dataemissao,codcli,peso,valorunit,valortotal,CHAVE_NFS) "
                            + " VALUES (" + Formato.somenteNumeros(edtNrNfs.getText())
                            + ", '" + edtSerie.getText() + "' "
                            + ", to_date('" + Formato.dateToStr(edtDataEmissao.getDate()) + "','dd/mm/yyyy') "
                            + ", " + edtCodCli.getText()
                            + ", " + Formato.decimalToStrDb(edtPesoTransportado.getCurr(), 6)
                            + ", " + Formato.decimalToStrDb(edtValorUnitFrete.getCurr(), 6)
                            + ", " + Formato.decimalToStrDb(edtValorTotalFrete.getCurr(), 6)
                            + ", '" + edtChaveNfs.getText()
                            + "' )";
                    result = wint.insertDados(strInsert);
                    ret = (result != 0);
                    buscaNotaServico();
                } else {
                    strInsert = "UPDATE KT_NOTASERVICO SET CHAVE_NFS = '" + edtChaveNfs.getText() + "' WHERE NUMERO = " + Formato.somenteNumeros(edtNrNfs.getText());
                    result = wint.updateDados(strInsert);
                    ret = (result != 0);
                    buscaNotaServico();
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "gravarNotaServico");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private boolean removeNotaServico(String numeroNota, Date dataEmissao) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strDelete = "";
        int result = 0;
        boolean ret = false;
        try {
            if (!numeroNota.isEmpty()) {
                wint.openConectOracle();

                // altera a data do pedido
                strDelete = "DELETE FROM KT_NOTASERVICO WHERE "
                        + " numero = " + numeroNota
                        + " and dataemissao = to_date('" + Formato.dateToStr(dataEmissao) + "','dd/mm/yyyy') ";
                result += wint.deleteDados(strDelete);

                strDelete = "DELETE FROM kt_notaservicovinculada WHERE "
                        + " numero_nfs = " + numeroNota;
                result += wint.deleteDados(strDelete);

                ret = (result != 0);
                buscaNotaServico();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "removeNotaServico");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private boolean validaNotaServico() {
        boolean ret = true;
        try {
            if (edtNrNfs.getText().isEmpty()) {
                ret = false;
                trataErro.addListaErros("Favor informar o numero da nota de serviço!");
            }

            if (edtSerie.getText().isEmpty()) {
                ret = false;
                trataErro.addListaErros("Favor informar a serie da nota de serviço!");
            }

            if (edtDataEmissao.getDate() == null) {
                ret = false;
                trataErro.addListaErros("Favor informar a data de emissão da nota de serviço!");
            }

            if (edtPesoTransportado.getCurr().doubleValue() == 0) {
                ret = false;
                trataErro.addListaErros("Favor informar o peseo total da nota de serviço!");
            }

            if (edtValorUnitFrete.getCurr().doubleValue() == 0) {
                ret = false;
                trataErro.addListaErros("Favor informar o valor unitário do frete da nota de serviço!");
            }
            if (edtValorTotalFrete.getCurr().doubleValue() == 0) {
                ret = false;
                trataErro.addListaErros("Favor informar o valor total do frete da nota de serviço!");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "validaNotaServico");
            throw ex;
        }
        return ret;
    }

    private boolean gravarVinculaNotaServico() throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strInsert = "";
        int result = 0;
        boolean ret = false;
        try {
            if ((!edtNrNfs.getText().isEmpty()) && (!edtNrNotaVinculada.getText().isEmpty())) {
                wint.openConectOracle();

                // altera a data do pedido
                strInsert = "INSERT INTO kt_notaservicovinculada (numero_nfs,numero_nota,serie_nota,chave_nota ) "
                        + " VALUES (" + Formato.somenteNumeros(edtNrNfs.getText())
                        + ", " + Formato.somenteNumeros(edtNrNotaVinculada.getText())
                        + ", '" + edtSerieNotaVinculada.getText() + "' "
                        + ", '" + edtChaveNfe.getText() + "' "
                        + " )";
                result = wint.insertDados(strInsert);
                ret = (result != 0);
                buscaNotaServico();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "gravarVinculaNotaServico");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private boolean removeVinculadaNotaServico(String numeroNfs, String numeroNota) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        String strDelete = "";
        int result = 0;
        boolean ret = false;
        try {
            if ((!numeroNfs.isEmpty()) && (!numeroNota.isEmpty())) {
                wint.openConectOracle();

                strDelete = "DELETE FROM kt_notaservicovinculada WHERE 1=1"
                        + " and numero_nfs = " + numeroNfs
                        + " and numero_nota = " + numeroNota;
                result += wint.deleteDados(strDelete);

                ret = (result != 0);
                buscaNotaServico();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "removeVinculadaNotaServico");
            throw ex;
        } finally {
            wint.closeConectOracle();
        }
        return ret;
    }

    private void calculaValorFreteUnit() {
        double peso = 0.00;
        double vlfrete = 0.00;
        if (edtValorUnitFrete.getCurr().doubleValue() == 0) {
            peso = edtPesoTransportado.getCurr().doubleValue();
            vlfrete = edtValorTotalFrete.getCurr().doubleValue();
            if ((peso > 0) && (vlfrete > 0)) {
                edtValorUnitFrete.setCurr(new BigDecimal(vlfrete / peso));
            }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        btnGravaNotaServico = new javax.swing.JButton();
        btnRemoveNotaServico = new javax.swing.JButton();
        edtDataEmissao = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblNfServico = new winthorDb.util.CustomTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        edtNrNfs = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edtSerie = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edtCodCli = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        edtNomeCliente = new javax.swing.JTextField();
        edtPesoTransportado = new winthorDb.util.CurrencyField();
        edtValorUnitFrete = new winthorDb.util.CurrencyField();
        edtValorTotalFrete = new winthorDb.util.CurrencyField();
        btnNovo = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        edtChaveNfs = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblNfServicoVinculada = new winthorDb.util.CustomTable();
        jLabel4 = new javax.swing.JLabel();
        edtChaveNfe = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtNrNotaVinculada = new javax.swing.JTextField();
        btnRemoveNotaVinculada = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        edtSerieNotaVinculada = new javax.swing.JTextField();
        btnGravaNotaVinculada = new javax.swing.JButton();

        setTitle("Conversão de Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz010");

        jLabel1.setText("Lançamento de Nota de Serviços");

        jLabel3.setText("* * Faz o lançamento da nota fiscal de serviços de entrega e vincula as notas de cada cliente");

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe os dados"));

        btnGravaNotaServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/diskette.png"))); // NOI18N
        btnGravaNotaServico.setToolTipText("Salvar");
        btnGravaNotaServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravaNotaServicoActionPerformed(evt);
            }
        });

        btnRemoveNotaServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/less.png"))); // NOI18N
        btnRemoveNotaServico.setToolTipText("Remover Nota");
        btnRemoveNotaServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveNotaServicoActionPerformed(evt);
            }
        });

        edtDataEmissao.setDateFormatString("dd/MM/yyyy");

        jLabel6.setText("Data Emissão:");

        tblNfServico.setToolTipText("");
        tblNfServico.setCellSelectionEnabled(true);
        tblNfServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNfServicoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblNfServico);

        txtLog.setColumns(20);
        txtLog.setRows(5);
        txtLog.setText("Log de execução:\n");
        jScrollPane1.setViewportView(txtLog);

        jLabel5.setText("Nr. NF-s:");

        edtNrNfs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtNrNfsFocusLost(evt);
            }
        });

        jLabel7.setText("Serie:");

        edtSerie.setText("1");

        jLabel8.setText("Cliente");

        edtCodCli.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtCodCliFocusLost(evt);
            }
        });

        jLabel14.setText("Peso Transportado:");

        jLabel15.setText("Valor Unitario Frete:");

        jLabel16.setText("Valor:");

        edtNomeCliente.setEnabled(false);

        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/file.png"))); // NOI18N
        btnNovo.setToolTipText("Novo");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        jLabel11.setText("Chave Nfs:");

        lblStatus.setText("...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtPesoTransportado, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtValorUnitFrete, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(edtChaveNfs))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(edtNrNfs, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(edtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(edtDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(edtValorTotalFrete, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGravaNotaServico, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnNovo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemoveNotaServico))
                            .addComponent(lblStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(edtNrNfs, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(edtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGravaNotaServico)
                        .addComponent(jLabel16)
                        .addComponent(edtValorTotalFrete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(edtDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel15)
                        .addComponent(edtValorUnitFrete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(edtPesoTransportado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRemoveNotaServico)
                    .addComponent(btnNovo)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(edtChaveNfs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edtCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nota de Serviço", jPanel2);

        tblNfServicoVinculada.setToolTipText("");
        tblNfServicoVinculada.setCellSelectionEnabled(true);
        tblNfServicoVinculada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNfServicoVinculadaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblNfServicoVinculada);

        jLabel4.setText("Chave NF-e:");

        jLabel9.setText("Nr. NF-e:");

        btnRemoveNotaVinculada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/less.png"))); // NOI18N
        btnRemoveNotaVinculada.setToolTipText("Remover");
        btnRemoveNotaVinculada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveNotaVinculadaActionPerformed(evt);
            }
        });

        jLabel10.setText("Serie:");

        btnGravaNotaVinculada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/plus.png"))); // NOI18N
        btnGravaNotaVinculada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravaNotaVinculadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edtNrNotaVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edtSerieNotaVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGravaNotaVinculada)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemoveNotaVinculada))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtChaveNfe, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edtChaveNfe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(edtNrNotaVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(edtSerieNotaVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnGravaNotaVinculada))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveNotaVinculada)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Notas Vinculadas", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Remessa Gerada");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGravaNotaServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravaNotaServicoActionPerformed
        try {
            if (validaNotaServico()) {
                gravarNotaServico();
            } else {
                trataErro.mostraListaErros();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnGravaNotaServicoActionPerformed");
        }

    }//GEN-LAST:event_btnGravaNotaServicoActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnRemoveNotaServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveNotaServicoActionPerformed

        try {
            if (MessageDialog.ask("Deseja Realmente excliuir a nota de serviço e todas as notas vinculadas?")
                    == MessageDialog.YES_OPTION) {
                if ((!edtNrNfs.getText().isEmpty()) && (edtDataEmissao.getDate() != null)) {
                    removeNotaServico(edtNrNfs.getText(), edtDataEmissao.getDate());
                } else {
                    MessageDialog.error("O Numero da Nota e a data de emissão deve ser informada! \n");
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnRemoveNotaServicoActionPerformed");
        }
    }//GEN-LAST:event_btnRemoveNotaServicoActionPerformed

    private void tblNfServicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNfServicoMouseClicked
        // TODO add your handling code here:
        try {
            if (tblNfServico.getRowCount() > 0) {
                edtNrNfs.setText(tblNfServico.getConteudoRowSelected("numero").toString());
                edtDataEmissao.setDate(Formato.strToDateNfe(tblNfServico.getConteudoRowSelected("dataemissao").toString().substring(0, 10)));
                edtSerie.setText(tblNfServico.getConteudoRowSelected("serie").toString());
                edtValorUnitFrete.setCurr(Formato.currStrToDecimal(tblNfServico.getConteudoRowSelected("valorunit").toString().replace(".", ",")));
                edtPesoTransportado.setCurr(Formato.currStrToDecimal(tblNfServico.getConteudoRowSelected("peso").toString().replace(".", ",")));
                edtValorTotalFrete.setCurr(Formato.currStrToDecimal(tblNfServico.getConteudoRowSelected("valortotal").toString().replace(".", ",")));
                edtCodCli.setText(tblNfServico.getConteudoRowSelected("codcli").toString());
                edtChaveNfs.setText(tblNfServico.getConteudoRowSelected("chave_nfs").toString());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "tblNfServicoMouseClicked");
        }
    }//GEN-LAST:event_tblNfServicoMouseClicked

    private void tblNfServicoVinculadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNfServicoVinculadaMouseClicked
        try {
            if (tblNfServicoVinculada.getRowCount() > 0) {
                edtSerieNotaVinculada.setText(tblNfServicoVinculada.getConteudoRowSelected("serie_nota").toString());
                edtNrNotaVinculada.setText(tblNfServicoVinculada.getConteudoRowSelected("numero_nota").toString());
                edtChaveNfe.setText(tblNfServicoVinculada.getConteudoRowSelected("chave_nota").toString());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "tblNfServicoVinculadaMouseClicked");
        }
    }//GEN-LAST:event_tblNfServicoVinculadaMouseClicked

    private void btnRemoveNotaVinculadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveNotaVinculadaActionPerformed
        try {
            if (MessageDialog.ask("Deseja Realmente excluir a Nota Vinculada?") == MessageDialog.YES_OPTION) {
                if (removeVinculadaNotaServico(edtNrNfs.getText(), edtNrNotaVinculada.getText())) {
                    buscaNotaVinculada();
                    edtSerieNotaVinculada.setText("");
                    edtNrNotaVinculada.setText("");
                    edtChaveNfe.setText("");
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnBuscarRemessaCartorioActionPerformed");
        }
    }//GEN-LAST:event_btnRemoveNotaVinculadaActionPerformed

    private void btnGravaNotaVinculadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravaNotaVinculadaActionPerformed
        try {
            if (gravarVinculaNotaServico()) {
                buscaNotaVinculada();
                edtSerieNotaVinculada.setText("");
                edtNrNotaVinculada.setText("");
                edtChaveNfe.setText("");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "btnGravaRemessaCartorioActionPerformed");
        }
    }//GEN-LAST:event_btnGravaNotaVinculadaActionPerformed

    private void edtValorTotalFreteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtValorTotalFreteFocusLost
        calculaValorFreteUnit();
    }//GEN-LAST:event_edtValorTotalFreteFocusLost

    private void edtNrNfsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtNrNfsFocusLost
        try {
            if (!edtNrNfs.getText().isEmpty()) {
                buscaNotaServico();
                tblNfServicoMouseClicked(null);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtNrNfsFocusLost");
        }
    }//GEN-LAST:event_edtNrNfsFocusLost

    private void edtPesoTransportadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtPesoTransportadoFocusLost
        calculaValorFreteUnit();
    }//GEN-LAST:event_edtPesoTransportadoFocusLost

    private void edtCodCliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCodCliFocusLost
        try {
            edtNomeCliente.setText(DaoDirect.getSingleField("Select cliente from pcclient where codcli= " + edtCodCli.getText()));

        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCodCliFocusLost");
        }
    }//GEN-LAST:event_edtCodCliFocusLost

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        // TODO add your handling code here:
        tblNfServico.clearTableData();
        tblNfServicoVinculada.clearTableData();
        edtNrNfs.setText("");
        edtDataEmissao.setDate(null);
        edtSerie.setText("");
        edtValorUnitFrete.setCurr(BigDecimal.ZERO);
        edtPesoTransportado.setCurr(BigDecimal.ZERO);
        edtValorTotalFrete.setCurr(BigDecimal.ZERO);
        edtCodCli.setText("");
        edtChaveNfs.setText("");

        edtChaveNfe.setText("");
        edtNrNotaVinculada.setText("");
        edtSerieNotaVinculada.setText("");
        
        insert = true;
        lblStatus.setText((insert?"Novo":"Atualiza"));

    }//GEN-LAST:event_btnNovoActionPerformed

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
                new LancarNotaServico(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGravaNotaServico;
    private javax.swing.JButton btnGravaNotaVinculada;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnRemoveNotaServico;
    private javax.swing.JButton btnRemoveNotaVinculada;
    private javax.swing.JTextField edtChaveNfe;
    private javax.swing.JTextField edtChaveNfs;
    private javax.swing.JTextField edtCodCli;
    private com.toedter.calendar.JDateChooser edtDataEmissao;
    private javax.swing.JTextField edtNomeCliente;
    private javax.swing.JTextField edtNrNfs;
    private javax.swing.JTextField edtNrNotaVinculada;
    private winthorDb.util.CurrencyField edtPesoTransportado;
    private javax.swing.JTextField edtSerie;
    private javax.swing.JTextField edtSerieNotaVinculada;
    private winthorDb.util.CurrencyField edtValorTotalFrete;
    private winthorDb.util.CurrencyField edtValorUnitFrete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private winthorDb.util.CustomTable tblNfServico;
    private winthorDb.util.CustomTable tblNfServicoVinculada;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
