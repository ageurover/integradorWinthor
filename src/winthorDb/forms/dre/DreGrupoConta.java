package winthorDb.forms.dre;

import java.awt.Cursor;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.oracleDb.IntegracaoWinthorDb;

/**
 *
 * @author ageur
 */
public class DreGrupoConta extends javax.swing.JFrame {

    /**
     * Creates new form DreGrupoConta
     */
    public DreGrupoConta() {
        initComponents();
        initGuid();
    }

    private void initGuid() {
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
        buscaGrupoContaWinthor();
        buscaFilialWinthor();
        buscaDeptoWinthor();
    }

    private void buscaFilialWinthor() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    cmbFilial.removeAllItems();
                    String strSelect = "select (CODIGO || ' - ' || razaosocial ) AS DESCRICAO "
                            + " FROM PCFILIAL WHERE CODIGO BETWEEN 1 AND 90 "
                            + " ORDER BY TO_NUMBER(CODIGO)";

                    wint.openConectOracle();

                    List filiais = wint.selectDados(strSelect);
                    String itens[] = new String[filiais.size()];
                    for (int i = 0; i < filiais.size(); i++) {
                        itens[i] = (filiais.get(i).toString().replace("[", "").replace("]", "")).trim();
                    }
                    cmbFilial.setModel(new DefaultComboBoxModel(itens));

                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaFilialWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaFilialWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void buscaDeptoWinthor() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    cmbDepto.removeAllItems();
                    String strSelect = "select (CODEPTO || ' - ' || DESCRICAO ) AS DEPARTAMENTO "
                            + " from pcdepto "
                            + " where codepto not in (9999) AND TIPOMERC IN ('RT') "
                            + " order by codepto";

                    wint.openConectOracle();

                    List deptos = wint.selectDados(strSelect);
                    String itens[] = new String[deptos.size()];
                    for (int i = 0; i < deptos.size(); i++) {
                        itens[i] = (deptos.get(i).toString().replace("[", "").replace("]", "")).trim();
                    }
                    cmbDepto.setModel(new DefaultComboBoxModel(itens));

                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaDeptoWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaDeptoWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void buscaSecaoWinthor() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    cmbSecao.removeAllItems();
                    String strSelect = "Select (CODSEC || ' - ' || DESCRICAO ) AS SECAO from pcsecao where 1=1 "
                            + " AND codepto = " + (cmbDepto.getSelectedItem().toString().substring(0, cmbDepto.getSelectedItem().toString().indexOf("-"))).trim()
                            + " order BY CODSEC";

                    wint.openConectOracle();

                    List secoes = wint.selectDados(strSelect);
                    String itens[] = new String[secoes.size()];
                    for (int i = 0; i < secoes.size(); i++) {
                        itens[i] = secoes.get(i).toString().replace("[", "").replace("]", "");
                    }
                    cmbSecao.setModel(new DefaultComboBoxModel(itens));

                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaSecaoWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaSecaoWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void buscaGrupoContaWinthor() {
        new Thread() {//instancia nova thread já implementando o método run()
            @Override
            public void run() {//sobrescreve o método run()
                //FUnções e comandos de append do JTextArea
                IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

                try {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    cmbGrupoConta.removeAllItems();
                    String strSelect = "select (CODGRUPO || ' - ' || GRUPO) DESCRICAO "
                            + " from pcgrupo "
                            + " where codgrupo BETWEEN 199 and 899 "
                            + " and codgrupo in (select grupoconta from pcconta where tipo not in ('I') group by grupoconta having count(1)>0) "
                            + " order by codgrupo";

                    wint.openConectOracle();

                    List grupos = wint.selectDados(strSelect);
                    String itens[] = new String[grupos.size()];
                    for (int i = 0; i < grupos.size(); i++) {
                        itens[i] = (grupos.get(i).toString().replace("[", "").replace("]", "")).trim();
                    }
                    cmbGrupoConta.setModel(new DefaultComboBoxModel(itens));

                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } catch (Exception ex) {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    trataErro.trataException(ex, "buscaGrupoContaWinthor");
                } finally {
                    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        wint.closeConectOracle();
                    } catch (Exception ex) {
                        trataErro.trataException(ex, "buscaGrupoContaWinthor");
                    }
                }
            }//- Fim do Run
        }.start();//Fim Thread
    }

    private void buscaContaWinthor() {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            tblContaWinthor.clearTableData();

            String strSelect = "select codconta, conta, grupoconta "
                    + " from pcconta "
                    + " where tipo not in ('I') "
                    + " AND grupoconta = " + (cmbGrupoConta.getSelectedItem().toString().substring(0, cmbGrupoConta.getSelectedItem().toString().indexOf("-"))).trim()
                    + " order by codconta";

            wint.openConectOracle();
            tblContaWinthor.clearTableData();
            tblContaWinthor.setTableData(wint.selectResultSet(strSelect));

            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (Exception ex) {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            trataErro.trataException(ex, "buscaContaWinthor");
        } finally {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            try {
                wint.closeConectOracle();
            } catch (Exception ex) {
                trataErro.trataException(ex, "buscaContaWinthor");
            }
        }
    }

    private void buscaDreGrupoConta() {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();

        try {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            tblDreGrupoConta.clearTableData();

            String strSelect = "select codgrupo,tipo,codfilial,codigo,codepto,codsec "
                    + " from kt_tbl_grupoconta "
                    + " where 1=1 "
                    + " and codfilial = '" + (cmbFilial.getSelectedItem().toString().substring(0, cmbFilial.getSelectedItem().toString().indexOf("-"))).trim() + "'"
                    + " and codgrupo = " + (cmbDreGrupo.getSelectedItem().toString().substring(0, cmbDreGrupo.getSelectedItem().toString().indexOf("-"))).trim()
                    + " order by codgrupo";

            wint.openConectOracle();
            tblDreGrupoConta.clearTableData();
            tblDreGrupoConta.setTableData(wint.selectResultSet(strSelect));

            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (Exception ex) {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            trataErro.trataException(ex, "buscaDreGrupoConta");
        } finally {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            try {
                wint.closeConectOracle();
            } catch (Exception ex) {
                trataErro.trataException(ex, "buscaDreGrupoConta");
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
        jpnFilial = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbFilial = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbDepto = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cmbSecao = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbGrupoConta = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblContaWinthor = new winthorDb.util.CustomTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbDreGrupo = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        edtCodigo = new javax.swing.JTextField();
        btnAtualizar = new javax.swing.JButton();
        btnAddDreGrupo = new javax.swing.JButton();
        btnRemDreGrupo = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDreGrupoConta = new winthorDb.util.CustomTable();

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png"))); // NOI18N
        jLabel2.setToolTipText("Brz011");

        jLabel1.setText("Vinculação Grupo Conta - DRE");

        jLabel3.setText("* *  Faz a vinculação das Contas da 570 com os grupos das Contas do DRE - Departamento/Seção");

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

        jLabel6.setText("Filial:");

        cmbFilial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Departamento:");

        cmbDepto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDepto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDeptoItemStateChanged(evt);
            }
        });

        jLabel9.setText("Seção:");

        cmbSecao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFilial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDepto, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSecao, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbDepto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(cmbSecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Conta Contabil Winthor"));

        jLabel4.setText("Grupo:");

        cmbGrupoConta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGrupoConta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGrupoContaItemStateChanged(evt);
            }
        });

        tblContaWinthor.setToolTipText("");
        tblContaWinthor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblContaWinthor.setCellSelectionEnabled(true);
        jScrollPane3.setViewportView(tblContaWinthor);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbGrupoConta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbGrupoConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setText("DRE - Grupo:");

        cmbDreGrupo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 - FATURAMENTO", "2 - PERDAS", "3 - ENERGIA", "4 - CONSUMO_INTERNO", "5 - MANUTENCAO", "6 - INVENTARIO" }));
        cmbDreGrupo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDreGrupoItemStateChanged(evt);
            }
        });

        jLabel8.setText("Codigo Agrupador:");

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/refresh.png"))); // NOI18N
        btnAtualizar.setToolTipText("Atualiza DRE Grupo Contas");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        btnAddDreGrupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/check.png"))); // NOI18N
        btnAddDreGrupo.setToolTipText("Adiciona Novo Grupo Conta");
        btnAddDreGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDreGrupoActionPerformed(evt);
            }
        });

        btnRemDreGrupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/less.png"))); // NOI18N
        btnRemDreGrupo.setToolTipText("Remove Grupo Conta");
        btnRemDreGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemDreGrupoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDreGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAtualizar)
                .addGap(18, 18, 18)
                .addComponent(btnAddDreGrupo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemDreGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(cmbDreGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8)
                .addComponent(edtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnAddDreGrupo)
            .addComponent(btnRemDreGrupo)
            .addComponent(btnAtualizar)
        );

        tblDreGrupoConta.setToolTipText("");
        tblDreGrupoConta.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDreGrupoConta.setCellSelectionEnabled(true);
        jScrollPane4.setViewportView(tblDreGrupoConta);

        javax.swing.GroupLayout jpnFilialLayout = new javax.swing.GroupLayout(jpnFilial);
        jpnFilial.setLayout(jpnFilialLayout);
        jpnFilialLayout.setHorizontalGroup(
            jpnFilialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnFilialLayout.createSequentialGroup()
                .addGroup(jpnFilialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpnFilialLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpnFilialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jpnFilialLayout.setVerticalGroup(
            jpnFilialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnFilialLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpnFilial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnFilial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddDreGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDreGrupoActionPerformed
        // TODO add your handling code here:
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        try {
            String codFilial = "", codGrupo = "", tipo = "", codigo = "", codepto = "", codsec = "";
            String insert = "";
            int ret = 0;

            // verifica se ambos registros estao selecionados
            if ((tblContaWinthor.getSelectedRowCount() > 0) && (cmbFilial.getSelectedIndex() > 0)) {
                codFilial = (cmbFilial.getSelectedItem().toString().substring(0, cmbFilial.getSelectedItem().toString().indexOf("-"))).trim();
                codGrupo = (cmbDreGrupo.getSelectedItem().toString().substring(0, cmbDreGrupo.getSelectedItem().toString().indexOf("-"))).trim();
                tipo = (cmbDreGrupo.getSelectedItem().toString().substring(cmbDreGrupo.getSelectedItem().toString().indexOf("-") + 1, cmbDreGrupo.getSelectedItem().toString().length())).trim();
                codigo = edtCodigo.getText();
                codepto = (cmbDepto.getSelectedItem().toString().substring(0, cmbDepto.getSelectedItem().toString().indexOf("-"))).trim();
                codsec = (cmbSecao.getSelectedItem().toString().substring(0, cmbSecao.getSelectedItem().toString().indexOf("-"))).trim();

                switch (tipo) {
                    case "ENERGIA":
                        // Carrega os dados para o tipo global de despesa
                        if ((!codFilial.isEmpty()) && (!codGrupo.isEmpty()) && (!tipo.isEmpty()) && (!codigo.isEmpty())) {
                            insert = "INSERT INTO KT_TBL_GRUPOCONTA (CODGRUPO,TIPO,CODFILIAL,CODIGO) \n"
                                    + "VALUES( "
                                    + codGrupo
                                    + " , '" + tipo + "'"
                                    + " , '" + codFilial + "'"
                                    + " , " + codigo
                                    + " ) ";
                        } else {
                            insert = "";
                        }
                        break;
                    case "INVENTARIO":
                        // Carrega os dados para o tipo global de despesa
                        if ((!codFilial.isEmpty()) && (!codGrupo.isEmpty()) && (!tipo.isEmpty()) && (!codigo.isEmpty())) {
                            insert = "INSERT INTO KT_TBL_GRUPOCONTA (CODGRUPO,TIPO,CODFILIAL,CODIGO) \n"
                                    + "VALUES( "
                                    + codGrupo
                                    + " , '" + tipo + "'"
                                    + " , '" + codFilial + "'"
                                    + " , " + codigo
                                    + " ) ";
                        } else {
                            insert = "";
                        }
                        break;
                    default:
                        // Carrega os dados para o tipo FATURAMENTO / PERDAS / CONSUMO_INTERNO / MANUTENCAO
                        if ((!codFilial.isEmpty()) && (!codGrupo.isEmpty()) && (!tipo.isEmpty()) && (!codigo.isEmpty())
                                && (!codepto.isEmpty()) && (!codsec.isEmpty())) {
                            insert = "INSERT INTO KT_TBL_GRUPOCONTA (CODGRUPO,TIPO,CODFILIAL,CODIGO,CODEPTO,CODSEC) \n"
                                    + "VALUES( "
                                    + codGrupo
                                    + " , '" + tipo + "'"
                                    + " , '" + codFilial + "'"
                                    + " , " + codigo
                                    + " , " + codepto
                                    + " , " + codsec
                                    + " ) ";
                        } else {
                            insert = "";
                        }
                }

                if (!insert.isEmpty()) {
                    wint.openConectOracle();
                    ret = wint.insertDados(insert);

                    if (ret > 0) {
                        MessageDialog.saveSucess();
                        buscaDreGrupoConta();
                    } else {
                        MessageDialog.error("Atenção! \nErro ao gravar DRE - Grupo Conta ! \n" + insert);
                    }
                } else {
                    MessageDialog.error("Atenção! \nErro ao gravar DRE - Grupo Conta !");
                }

            } else {
                MessageDialog.error("Atenção! \nFaltam dados para criar o DRE - Grupo Conta!");
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "btnAddDreGrupoActionPerformed");
        } finally {
        }
    }//GEN-LAST:event_btnAddDreGrupoActionPerformed

    private void btnRemDreGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemDreGrupoActionPerformed
        // TODO add your handling code here:
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        try {
            String codFilial = "", codGrupo = "", codigo = "", depto = "", secao = "", tipo = "";
            String delete = "";
            int ret = 0;

            // verifica se o registro de vinculacao esta selecionado
            if ((tblDreGrupoConta.getSelectedRowCount() > 0)) {
                // Chave Primaria orCodFilial,dsCodFilial
                codFilial = tblDreGrupoConta.getConteudoRowSelected("CODFILIAL").toString();
                codGrupo = tblDreGrupoConta.getConteudoRowSelected("CODGRUPO").toString();
                tipo = tblDreGrupoConta.getConteudoRowSelected("TIPO").toString();
                codigo = tblDreGrupoConta.getConteudoRowSelected("CODIGO").toString();
                depto = tblDreGrupoConta.getConteudoRowSelected("CODEPTO").toString();
                secao = tblDreGrupoConta.getConteudoRowSelected("CODSEC").toString();

                switch (tipo) {
                    case "ENERGIA":
                        if ((!codFilial.isEmpty()) && (!codGrupo.isEmpty()) && (!codigo.isEmpty())) {
                            delete = "DELETE FROM KT_TBL_GRUPOCONTA "
                                    + " WHERE CODFILIAL = '" + codFilial + "'"
                                    + " AND CODGRUPO = " + codGrupo
                                    + " AND CODIGO = " + codigo;
                        } else {
                            delete = "";
                        }
                        break;
                    case "INVENTARIO":
                        if ((!codFilial.isEmpty()) && (!codGrupo.isEmpty()) && (!codigo.isEmpty())) {
                            delete = "DELETE FROM KT_TBL_GRUPOCONTA "
                                    + " WHERE CODFILIAL = '" + codFilial + "'"
                                    + " AND CODGRUPO = " + codGrupo
                                    + " AND CODIGO = " + codigo;
                        } else {
                            delete = "";
                        }
                        break;
                    default:
                        if ((!codFilial.isEmpty()) && (!codGrupo.isEmpty()) && (!codigo.isEmpty())
                                && (!depto.isEmpty()) && (!secao.isEmpty())) {
                            delete = "DELETE FROM KT_TBL_GRUPOCONTA "
                                    + " WHERE CODFILIAL = '" + codFilial + "'"
                                    + " AND CODGRUPO = " + codGrupo
                                    + " AND CODIGO = " + codigo
                                    + " AND CODEPTO = " + depto
                                    + " AND CODSEC = " + secao;
                        } else {
                            delete = "";
                        }
                }
                // monta comando delete de dados se todos os campos estiverem preenchidos
                if ((!delete.isEmpty())) {

                    wint.openConectOracle();
                    ret = wint.deleteDados(delete);

                    if (ret > 0) {
                        MessageDialog.deleteSucess();
                        buscaDreGrupoConta();
                    } else {
                        MessageDialog.error("Atenção! \nErro ao remover DRE - Grupo Conta ! \n" + delete);
                    }

                } else {
                    MessageDialog.error("Atenção! \nFaltam dados para remover o DRE - Grupo Conta!");
                }
            }

        } catch (Exception ex) {
            trataErro.trataException(ex, "btnRemDreGrupoActionPerformed");
        } finally {

        }
    }//GEN-LAST:event_btnRemDreGrupoActionPerformed

    private void tblDsFilialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDsFilialMouseClicked
        // TODO add your handling code here:
        // Se o botï¿½o direito do mouse foi pressionado

    }//GEN-LAST:event_tblDsFilialMouseClicked

    private void cmbGrupoContaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGrupoContaItemStateChanged
        // TODO add your handling code here:
        if (cmbGrupoConta.getItemCount() > 0) {
            buscaContaWinthor();
        }
    }//GEN-LAST:event_cmbGrupoContaItemStateChanged

    private void cmbDeptoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDeptoItemStateChanged
        // TODO add your handling code here:
        if (cmbDepto.getItemCount() > 0) {
            buscaSecaoWinthor();
        }
    }//GEN-LAST:event_cmbDeptoItemStateChanged

    private void cmbDreGrupoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDreGrupoItemStateChanged
        // TODO add your handling code here:
        if (cmbGrupoConta.getItemCount() > 0) {
            buscaDreGrupoConta();
        }
    }//GEN-LAST:event_cmbDreGrupoItemStateChanged

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        if (cmbGrupoConta.getItemCount() > 0) {
            buscaDreGrupoConta();
        }
    }//GEN-LAST:event_btnAtualizarActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DreGrupoConta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DreGrupoConta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DreGrupoConta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DreGrupoConta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DreGrupoConta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDreGrupo;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnRemDreGrupo;
    private javax.swing.JComboBox<String> cmbDepto;
    private javax.swing.JComboBox<String> cmbDreGrupo;
    private javax.swing.JComboBox<String> cmbFilial;
    private javax.swing.JComboBox<String> cmbGrupoConta;
    private javax.swing.JComboBox<String> cmbSecao;
    private javax.swing.JTextField edtCodigo;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel jpnFilial;
    private winthorDb.util.CustomTable tblContaWinthor;
    private winthorDb.util.CustomTable tblDreGrupoConta;
    // End of variables declaration//GEN-END:variables
}
