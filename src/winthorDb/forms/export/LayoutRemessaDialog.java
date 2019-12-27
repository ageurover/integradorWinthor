/*
 * LayoutRemessaDialog.java
 *
 * Created on 22 de Janeiro de 2018, 11:47
 */
package winthorDb.forms.export;

import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.LayoutDoc;
import winthorDb.jpa.dao.Dao;
import winthorDb.oracleDb.IntegracaoWinthorDb;
import winthorDb.util.Formato;

/**
 *
 * @author AgeuRover
 */
public class LayoutRemessaDialog extends javax.swing.JDialog {

    /**
     * Creates new form AccountDialog
     *
     * @param parent
     * @param modal
     * @param idDoc
     * @param tipoDoc
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LayoutRemessaDialog(java.awt.Frame parent, boolean modal, int idDoc, String tipoDoc) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        if (tipoDoc.isEmpty()) {
            MessageDialog.error("tipo de documento não informado para processar o layout!");
            dispose();
        } else {
            edtIdDocumento.setText("" + idDoc);
            edtTipoDoc.setText(tipoDoc);
            exibeLayout();
        }
    }

    public static void open(int idDoc, String tipoDoc) {
        new LayoutRemessaDialog(null, true, idDoc, tipoDoc).setVisible(true);
    }

    private void exibeLayout() {
        String sqlString = "";
        try {
            tableLayout.clearTableData();
            if ((!edtIdDocumento.getText().isEmpty()) && (!edtTipoDoc.getText().isEmpty())) {
                sqlString = "SELECT ('' || id) as id , ('' || tipoDoc) as tipoDoc, ('' || idDoc) as idDoc, "
                        + " ('' ||tipoRegistro) as tipoRegistro, ('' ||sequencia) as sequencia, ('' ||tipoDado) as tipoDado,  "
                        + " ('' ||tamanho) as tamanho, ('' ||posIncial) as posIncial, ('' ||posFinal) as posFinal, ('' || sqlCampo) as sqlCampo,  "
                        + " ('' ||valor_default) as valor_default, ('' ||mascara) as mascara,  "
                        + " ('' ||comentario) as comentario "
                        + " FROM layoutDoc "
                        + " WHERE idDoc = " + edtIdDocumento.getText()
                        + " AND tipoDoc = '" + edtTipoDoc.getText() + "' "
                        + " AND tipoRegistro = '" + cmbTipoRegistro.getSelectedItem().toString().substring(0, 1) + "' "
                        + " ORDER BY tipoDoc, idDoc, tipoRegistro, CAST (sequencia AS INT)";

                tableLayout.setTableData(sqlString);

                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
                wint.openConectOracle();
                String sqlSoma = "Select sum(Tamanho) from layoutDoc WHERE idDoc = " + edtIdDocumento.getText()
                        + " AND tipoDoc = '" + edtTipoDoc.getText() + "' "
                        + " AND tipoRegistro = '" + cmbTipoRegistro.getSelectedItem().toString().substring(0, 1) + "' ";

                lblTamanhoTotal.setText( wint.selectDados(sqlSoma).get(0).toString());

            }

        } catch (Exception ex) {            
            trataErro.trataException(ex, "exibeLayout");
        }
    }

    private void removeLinha() {
        try {
            if (tableLayout.getSelectedRowCount() > 0) {
                LayoutDoc linha = Dao.get(LayoutDoc.class, tableLayout.getConteudoRowSelected("id").toString());
                if (linha != null) {
                    Dao.delete(linha);

                    exibeLayout();
                    limpaTela();
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "removeLinha");
        }
    }

    private void limpaTela() {
        int proxSeq = 0;
        try {
            edtId.setText("");
            cmbTipoDado.setSelectedIndex(0);
            edtPosInicial.setText("0");
            edtPosFinal.setText("0");
            edtTamanho.setText("0");
            edtSequencia.setText("0");
            edtSqlComando.setText("");
            edtValorDefault.setText("");
            edtMascara.setText("");
            edtComentario.setText("");
            tableLayout.changeSelection(tableLayout.getRowCount() - 1, 0, false, false);

            if (tableLayout.getRowCount() > 1) {
                proxSeq = Formato.strToInt(tableLayout.getConteudoRow("sequencia", tableLayout.getRowCount() - 1).toString()) + 1;
                edtSequencia.setText("" + proxSeq);
                edtPosInicial.setText(tableLayout.getConteudoRow("posFinal", tableLayout.getRowCount() - 1).toString());
                edtPosFinal.requestFocus();
            } else {
                edtSequencia.setText("1");
                edtPosInicial.setText("1");
                edtSequencia.requestFocus();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "limpaTela");
        }
    }

    private void adicionaLinha() {
        try {
            LayoutDoc linha = new LayoutDoc();
            linha.setTipoDoc(edtTipoDoc.getText());
            linha.setIdDoc(Formato.strToInt(edtIdDocumento.getText()));
            linha.setTipoRegistro(cmbTipoRegistro.getSelectedItem().toString().substring(0, 1));
            linha.setPosIncial(Formato.strToInt(edtPosInicial.getText()));
            linha.setPosFinal(Formato.strToInt(edtPosFinal.getText()));
            linha.setTamanho(Formato.strToInt(edtTamanho.getText()));
            linha.setTipoDado(cmbTipoDado.getSelectedItem().toString().substring(0, 3));
            linha.setSequencia(Formato.strToInt(edtSequencia.getText()));
            linha.setSqlCampo(edtSqlComando.getText());
            linha.setValor_default(edtValorDefault.getText());
            linha.setMascara(edtMascara.getText());
            linha.setComentario(edtComentario.getText());

            Dao.save(linha);
            Dao.refresh(linha);

            exibeLayout();
            limpaTela();
        } catch (Exception ex) {
            trataErro.trataException(ex, "adicionaLinha");
        }
    }

    private void alterarLinha(int id) {
        try {
            LayoutDoc linha = Dao.get(LayoutDoc.class, id);
            if (linha != null) {
                linha.setIdDoc(Formato.strToInt(edtIdDocumento.getText()));
                linha.setTipoDoc(edtTipoDoc.getText());
                linha.setTipoRegistro(cmbTipoRegistro.getSelectedItem().toString().substring(0, 1));
                linha.setPosIncial(Formato.strToInt(edtPosInicial.getText()));
                linha.setPosFinal(Formato.strToInt(edtPosFinal.getText()));
                linha.setTamanho(Formato.strToInt(edtTamanho.getText()));
                linha.setTipoDado(cmbTipoDado.getSelectedItem().toString().substring(0, 3));
                linha.setSequencia(Formato.strToInt(edtSequencia.getText()));
                linha.setSqlCampo(edtSqlComando.getText());
                linha.setValor_default(edtValorDefault.getText());
                linha.setMascara(edtMascara.getText());
                linha.setComentario(edtComentario.getText());

                Dao.save(linha);
                Dao.refresh(linha);
                exibeLayout();
                limpaTela();
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "alterarLinha");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        edtId = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbTipoRegistro = new javax.swing.JComboBox<>();
        edtPosInicial = new javax.swing.JTextField();
        edtSequencia = new javax.swing.JTextField();
        edtPosFinal = new javax.swing.JTextField();
        edtTamanho = new javax.swing.JTextField();
        cmbTipoDado = new javax.swing.JComboBox<>();
        edtSqlComando = new javax.swing.JTextField();
        edtValorDefault = new javax.swing.JTextField();
        edtMascara = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        edtComentario = new javax.swing.JTextArea();
        btnNovo = new javax.swing.JButton();
        btnGravar = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        edtTipoDoc = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        edtIdDocumento = new javax.swing.JTextField();
        btnSqlEdit = new javax.swing.JButton();
        lblTamanhoTotal = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableLayout = new winthorDb.util.CustomTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Layout Arquivo de Remessa");
        setModal(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReliased(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                LayoutRemessaDialog.this.windowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados"));

        edtId.setEditable(false);
        edtId.setEnabled(false);
        edtId.setFocusable(false);
        edtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtIdActionPerformed(evt);
            }
        });

        jLabel10.setText("id:");

        jLabel1.setText("Tipo de Registro:");

        jLabel2.setText("Sequencia");

        jLabel3.setText("Pos Inicial");

        jLabel4.setText("Pos Final");

        jLabel5.setText("Tamanho");

        jLabel6.setText("Tipo de Dado");

        jLabel7.setText("Campo SQL:");

        jLabel8.setText("Valor Padrão");

        jLabel9.setText("Mascara");

        cmbTipoRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "H - Header", "D - Detalhe", "T - Treller" }));
        cmbTipoRegistro.setToolTipText("H - Header\\nD - Detalhe\\nT - Treller");
        cmbTipoRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoRegistroItemStateChanged(evt);
            }
        });

        edtPosInicial.setText("0");
        edtPosInicial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtPosInicialFocusGained(evt);
            }
        });

        edtSequencia.setText("0");
        edtSequencia.setToolTipText("Sequencia de execucao da geracao do layout");
        edtSequencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtSequenciaFocusGained(evt);
            }
        });

        edtPosFinal.setText("0");
        edtPosFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtPosFinalFocusGained(evt);
            }
        });

        edtTamanho.setText("0");
        edtTamanho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtTamanhoFocusGained(evt);
            }
        });

        cmbTipoDado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALF - alfa", "NMR - numerico", "DTA - data", "VZA - VAZIO / BRANCOS", "ZRO - ZEROS", "SQL - Comando SQL", "NLN - Nova Linha", "VAR - Variavel Interna" }));
        cmbTipoDado.setToolTipText("ALF - alfa\\nNMR - numerico\\nDTA - data\\nVZA - VAZIO / BRANCOS\\nZRO - ZEROS");

        edtSqlComando.setToolTipText("Campo da tabela ou comando SQL a ser executado");
        edtSqlComando.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtSqlComandoFocusGained(evt);
            }
        });

        edtValorDefault.setToolTipText("Valor default quando o retorno do campo for vazio ou null");
        edtValorDefault.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtValorDefaultFocusGained(evt);
            }
        });

        edtMascara.setToolTipText("Masca de exibicao para numeros e datas");
        edtMascara.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtMascaraFocusGained(evt);
            }
        });

        edtComentario.setColumns(20);
        edtComentario.setLineWrap(true);
        edtComentario.setRows(5);
        edtComentario.setToolTipText("Comentario a respeito do campo a ser gerado");
        edtComentario.setWrapStyleWord(true);
        jScrollPane2.setViewportView(edtComentario);

        btnNovo.setText("Limpar");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        btnGravar.setText("Gravar");
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt);
            }
        });

        btnRemove.setText("Remover");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        jLabel11.setText("Tipo Doc:");

        edtTipoDoc.setToolTipText("BOL - Boleto\\nAPAC - Apac Sus");

        jLabel12.setText("Codigo Documento Referenciado:");

        edtIdDocumento.setToolTipText("Codigo do documento originador exemplo boleto banco brasil como o id do boleto");

        btnSqlEdit.setText("SQL Editor");
        btnSqlEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSqlEditActionPerformed(evt);
            }
        });

        lblTamanhoTotal.setText("...");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(edtSequencia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(jLabel3)
                                        .add(18, 18, 18)
                                        .add(jLabel4)
                                        .add(18, 18, 18)
                                        .add(jLabel5)
                                        .add(18, 18, 18)
                                        .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(18, 18, 18)
                                        .add(jLabel8)
                                        .add(227, 227, 227))
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(edtPosInicial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(18, 18, 18)
                                        .add(edtPosFinal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(edtTamanho, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(cmbTipoDado, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(18, 18, 18)
                                        .add(edtValorDefault)
                                        .add(15, 15, 15))))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmbTipoRegistro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 213, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel10)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jLabel11)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtTipoDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jLabel12)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(edtIdDocumento))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(btnSqlEdit)
                                .add(18, 18, 18)
                                .add(lblTamanhoTotal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnNovo)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnGravar)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnRemove, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jScrollPane2)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel7)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtSqlComando)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jLabel9)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(edtMascara, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(edtId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10)
                    .add(jLabel11)
                    .add(edtTipoDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel12)
                    .add(edtIdDocumento, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(cmbTipoRegistro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(jLabel6)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(edtPosInicial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtSequencia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtPosFinal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtTamanho, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbTipoDado, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(edtValorDefault, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel7)
                    .add(edtSqlComando, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9)
                    .add(edtMascara, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(btnGravar)
                        .add(btnRemove)
                        .add(btnNovo))
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(btnSqlEdit)
                        .add(lblTamanhoTotal)))
                .addContainerGap())
        );

        tableLayout.setToolTipText("");
        tableLayout.setCellSelectionEnabled(true);
        tableLayout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableLayoutMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableLayout);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 782, Short.MAX_VALUE)
                    .add(jScrollPane3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void KeyReliased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyReliased

    }//GEN-LAST:event_KeyReliased

    private void windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosing
// TODO add your handling code here:
    }//GEN-LAST:event_windowClosing

    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed
        // TODO add your handling code here:
        if (edtId.getText().isEmpty()) {
            adicionaLinha();
        } else {
            alterarLinha(Formato.strToInt(edtId.getText()));
        }
    }//GEN-LAST:event_btnGravarActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        removeLinha();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void edtTamanhoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtTamanhoFocusGained
        // TODO add your handling code here:
        int tam = 0;
        try {
            tam = Formato.strToInt(edtPosFinal.getText()) - Formato.strToInt(edtPosInicial.getText());
            edtTamanho.setText(Formato.intToStr(tam));
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtTamanhoFocusGained");
        }

    }//GEN-LAST:event_edtTamanhoFocusGained

    private void edtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtIdActionPerformed

    private void tableLayoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLayoutMouseClicked
        try {
            // TODO add your handling code here:
            edtId.setText(tableLayout.getConteudoRowSelected("id").toString());
            edtIdDocumento.setText(tableLayout.getConteudoRowSelected("idDoc").toString());
            edtTipoDoc.setText(tableLayout.getConteudoRowSelected("tipoDoc").toString());
            edtSequencia.setText(tableLayout.getConteudoRowSelected("sequencia").toString());
            edtPosInicial.setText(tableLayout.getConteudoRowSelected("posIncial").toString());
            edtPosFinal.setText(tableLayout.getConteudoRowSelected("posFinal").toString());
            edtTamanho.setText(tableLayout.getConteudoRowSelected("tamanho").toString());
            edtSqlComando.setText(tableLayout.getConteudoRowSelected("sqlCampo").toString());
            edtComentario.setText(tableLayout.getConteudoRowSelected("comentario").toString());
            edtMascara.setText(tableLayout.getConteudoRowSelected("mascara").toString());
            edtValorDefault.setText(tableLayout.getConteudoRowSelected("valor_default").toString());
            Formato.comboBoxSelectedValue(cmbTipoDado, tableLayout.getConteudoRowSelected("tipoDado").toString());
            Formato.comboBoxSelectedValue(cmbTipoRegistro, tableLayout.getConteudoRowSelected("tipoRegistro").toString());

        } catch (Exception ex) {
            trataErro.trataException(ex, "tableLayoutMouseClicked");
        }

    }//GEN-LAST:event_tableLayoutMouseClicked

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        // TODO add your handling code here:
        limpaTela();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void cmbTipoRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoRegistroItemStateChanged
        // TODO add your handling code here:
        exibeLayout();
    }//GEN-LAST:event_cmbTipoRegistroItemStateChanged

    private void edtSequenciaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtSequenciaFocusGained
        // TODO add your handling code here:
        edtSequencia.selectAll();
    }//GEN-LAST:event_edtSequenciaFocusGained

    private void edtPosInicialFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtPosInicialFocusGained
        // TODO add your handling code here:
        edtPosInicial.selectAll();
    }//GEN-LAST:event_edtPosInicialFocusGained

    private void edtPosFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtPosFinalFocusGained
        // TODO add your handling code here:
        edtPosFinal.selectAll();
    }//GEN-LAST:event_edtPosFinalFocusGained

    private void edtValorDefaultFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtValorDefaultFocusGained
        // TODO add your handling code here:
        edtValorDefault.selectAll();
    }//GEN-LAST:event_edtValorDefaultFocusGained

    private void edtSqlComandoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtSqlComandoFocusGained
        // TODO add your handling code here:
        edtSqlComando.selectAll();
    }//GEN-LAST:event_edtSqlComandoFocusGained

    private void edtMascaraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtMascaraFocusGained
        // TODO add your handling code here:
        edtMascara.selectAll();
    }//GEN-LAST:event_edtMascaraFocusGained

    private void tableLayoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLayoutMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tableLayoutMouseEntered

    private void btnSqlEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSqlEditActionPerformed
        if ((!edtIdDocumento.getText().isEmpty()) || (!edtTipoDoc.getText().isEmpty())) {
            LayoutDocSqlDialog.open(edtIdDocumento.getText(), edtTipoDoc.getText());
        }
    }//GEN-LAST:event_btnSqlEditActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LayoutRemessaDialog(new javax.swing.JFrame(), true, 0, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGravar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSqlEdit;
    private javax.swing.JComboBox<String> cmbTipoDado;
    private javax.swing.JComboBox<String> cmbTipoRegistro;
    private javax.swing.JTextArea edtComentario;
    private javax.swing.JTextField edtId;
    private javax.swing.JTextField edtIdDocumento;
    private javax.swing.JTextField edtMascara;
    private javax.swing.JTextField edtPosFinal;
    private javax.swing.JTextField edtPosInicial;
    private javax.swing.JTextField edtSequencia;
    private javax.swing.JTextField edtSqlComando;
    private javax.swing.JTextField edtTamanho;
    private javax.swing.JTextField edtTipoDoc;
    private javax.swing.JTextField edtValorDefault;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTamanhoTotal;
    private winthorDb.util.CustomTable tableLayout;
    // End of variables declaration//GEN-END:variables

}
