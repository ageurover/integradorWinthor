/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.forms.coletor;

import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import winthorDb.error.MessageDialog;
import winthorDb.error.trataErro;
import winthorDb.jpa.bean.BeanEnderecoLoja;
import winthorDb.jpa.bean.BeanEnderecoWms;
import winthorDb.jpa.bean.BeanProduto;
import winthorDb.jpa.dao.DaoEnderecoLoja;
import winthorDb.jpa.dao.DaoEnderecoWms;
import winthorDb.jpa.dao.DaoProduto;
import winthorDb.util.Formato;

/**
 *
 * @author ageurover
 */
public class vinculaEnderecoLoja extends javax.swing.JFrame {

    int qtdCheque = 0;
    BeanProduto prod = null;
    BeanEnderecoWms endWms = null;
    BeanEnderecoLoja endLoja = null;

    /**
     * Creates new form Brz001
     */
    public vinculaEnderecoLoja() {
        initComponents();
        initGuid();
    }

    private void initGuid() {
        try {
            setLocationRelativeTo(null);
            setIconImage(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/RoverTecnologiaIcone.png")).getImage());
            btnAddProduto.setEnabled(false);
            edtEndereco.requestFocus();
        } catch (Exception ex) {
            trataErro.trataException(ex, "initGuid");
        }
    }

    private void calculaCapacidade(double frente, double fundo, double altura) {
        double capacidade = Formato.doubleToRound((frente * fundo * altura), 2) ;
        double pontorepos = calculaPontoReposicao(capacidade, Double.parseDouble(edtPercPontoRep.getText()));

        edtCapacidade.setText(Formato.doubleToCurrStr(capacidade, 2));
        edtPontoReposicao.setText(Formato.doubleToCurrStr(pontorepos, 2));

        if (prod != null) {
            prod.setQtfrentegondula(frente);
            prod.setQtfundogondula(fundo);
            prod.setQtalturagondula(altura);
            prod.setCapacidade(capacidade);
            prod.setPontoreposicao(pontorepos);
            
            lblCapacidadeMaster.setText(Formato.doubleToCurrStr(capacidade / prod.getQtUnidadeMaster(),2));
        }
    }

    private double calculaPontoReposicao(double capacidade, double percReposicao) {
        return Formato.doubleToRound(( capacidade * (percReposicao / 100)), 2);
    }

    private void limpaProduto() {
        prod = null;
        edtCodBarras.setText("");
        lblProduto.setText("...");
        lblCapacidadeMaster.setText("...");
        lblCodProduto.setText("...");
        edtCapFrente.setText("1");
        edtCapFundo.setText("1");
        edtCapAltura.setText("1");
        edtCapacidade.setText("1");
        edtPercPontoRep.setText("40");
        edtPontoReposicao.setText("0");
        edtEmbalagemVenda.setText("");
        edtEmbalagemMaster.setText("");
        btnAddProduto.setEnabled(false);
        edtCodBarras.requestFocus();
    }

    private void limpaEnderecoWms() {
        limpaProduto();
        endWms = null;
        edtDeposito.setText("");
        edtRua.setText("");
        edtPredio.setText("");
        edtNivel.setText("");
        edtApto.setText("");
        tblProdutoEndereco.clearTableData();
        edtEndereco.requestFocus();
    }

    private void localizaProduto(String idFilial, String idProduto, String idEndereco) {
        try {
            DaoProduto daoProduto = new DaoProduto();
            if (idFilial != null && !idFilial.isEmpty()
                    && idProduto != null && !idProduto.isEmpty()) {
                prod = daoProduto.consultar(idFilial, idProduto, idEndereco);
                if (prod != null) {
                    lblProduto.setText(prod.getDescricao());
                    lblCodProduto.setText(prod.getCodprod().toString());
                    edtCapFrente.setText(Double.toString(prod.getQtfrentegondula()));
                    edtCapFundo.setText(Double.toString(prod.getQtfundogondula()));
                    edtCapAltura.setText(Double.toString(prod.getQtalturagondula()));
                    edtEmbalagemVenda.setText(prod.getEmbalagem());
                    edtEmbalagemMaster.setText(prod.getEmbalagemmaster());
                    btnAddProduto.setEnabled(true);
                    edtCapFrente.requestFocus();
                    if (endWms != null){
                        prod.setCodenderecoloja(endWms.getCodendereco());
                    }
                } else {
                    MessageDialog.error("Produto não localizado para a filial");
                    limpaProduto();
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnFiltraEnderecoActionPerformed");

        }
    }

    private void localizaEnderecoWms(String idFilial, String idEndereco) {
        try {
            DaoEnderecoWms daoEndWms = new DaoEnderecoWms();
            if (idEndereco != null && !idEndereco.isEmpty()) {
                endWms = daoEndWms.consultar(idFilial, idEndereco);
                if (endWms != null) {
                    edtDeposito.setText(Integer.toString(endWms.getDeposito()));
                    edtRua.setText(Integer.toString(endWms.getRua()));
                    edtPredio.setText(Integer.toString(endWms.getPredio()));
                    edtNivel.setText(Integer.toString(endWms.getNivel()));
                    edtApto.setText(Integer.toString(endWms.getApto()));
                    localizaEnderecoLoja(idFilial, idEndereco);
                    edtCodBarras.requestFocus();
                } else {
                    MessageDialog.error("Endereço não localizado!");
                    limpaEnderecoWms();
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnFiltraEnderecoActionPerformed");

        }
    }

        private void localizaEnderecoLoja(String idFilial, String idEndereco) {
        try {
            DaoEnderecoLoja daoEndLoja = new DaoEnderecoLoja();            
            if (idEndereco != null && !idEndereco.isEmpty() && idFilial != null && !idFilial.isEmpty()) {
                tblProdutoEndereco.setTableData( daoEndLoja.listar(idFilial, idEndereco));
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnFiltraEnderecoActionPerformed");
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        edtEndereco = new javax.swing.JTextField();
        btnFiltraEndereco = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        edtDeposito = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        edtRua = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        edtPredio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        edtNivel = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        edtApto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        edtCodBarras = new javax.swing.JTextField();
        btnFiltraProduto = new javax.swing.JButton();
        lblProduto = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        edtCapFrente = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edtCapFundo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtCapAltura = new javax.swing.JTextField();
        btnAddProduto = new javax.swing.JButton();
        edtCapacidade = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        edtFilial = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        edtEmbalagemVenda = new javax.swing.JTextField();
        edtEmbalagemMaster = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        edtPercPontoRep = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        edtPontoReposicao = new javax.swing.JTextField();
        lblCodProduto = new javax.swing.JLabel();
        lblCapacidadeMaster = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProdutoEndereco = new winthorDb.util.CustomTable();

        setTitle("Endereço Loja");
        setMinimumSize(new java.awt.Dimension(230, 250));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel6.setText("Endereço:");

        edtEndereco.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtEndereco.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtEnderecoFocusGained(evt);
            }
        });
        edtEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtEnderecoKeyPressed(evt);
            }
        });

        btnFiltraEndereco.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        btnFiltraEndereco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/filter_18dp.png"))); // NOI18N
        btnFiltraEndereco.setPreferredSize(new java.awt.Dimension(32, 32));
        btnFiltraEndereco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltraEnderecoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setText("Dep.");

        edtDeposito.setEditable(false);
        edtDeposito.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText("Rua");

        edtRua.setEditable(false);
        edtRua.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setText("Pred");

        edtPredio.setEditable(false);
        edtPredio.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel4.setText("Niv");

        edtNivel.setEditable(false);
        edtNivel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel14.setText("Apto");

        edtApto.setEditable(false);
        edtApto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel5.setText("Produto:");

        edtCodBarras.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtCodBarras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtCodBarrasFocusGained(evt);
            }
        });
        edtCodBarras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtCodBarrasActionPerformed(evt);
            }
        });
        edtCodBarras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtCodBarrasKeyPressed(evt);
            }
        });

        btnFiltraProduto.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        btnFiltraProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/filter_18dp.png"))); // NOI18N
        btnFiltraProduto.setPreferredSize(new java.awt.Dimension(32, 32));
        btnFiltraProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltraProdutoActionPerformed(evt);
            }
        });

        lblProduto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblProduto.setText("...");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel7.setText("Frente:");

        edtCapFrente.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtCapFrente.setText("1");
        edtCapFrente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtCapFrenteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtCapFrenteFocusLost(evt);
            }
        });
        edtCapFrente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtCapFrenteKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel8.setText("Fundo:");

        edtCapFundo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtCapFundo.setText("1");
        edtCapFundo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtCapFundoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtCapFundoFocusLost(evt);
            }
        });
        edtCapFundo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtCapFundoKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel9.setText("Altura:");

        edtCapAltura.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtCapAltura.setText("1");
        edtCapAltura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                edtCapAlturaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                edtCapAlturaFocusLost(evt);
            }
        });
        edtCapAltura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edtCapAlturaKeyPressed(evt);
            }
        });

        btnAddProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winthorDb/forms/icons/save_18dp.png"))); // NOI18N
        btnAddProduto.setMaximumSize(new java.awt.Dimension(32, 32));
        btnAddProduto.setMinimumSize(new java.awt.Dimension(32, 32));
        btnAddProduto.setPreferredSize(new java.awt.Dimension(32, 32));
        btnAddProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProdutoActionPerformed(evt);
            }
        });
        btnAddProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddProdutoKeyPressed(evt);
            }
        });

        edtCapacidade.setEditable(false);
        edtCapacidade.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtCapacidade.setText("1");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel12.setText("Capacidade");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel15.setText("Filial:");

        edtFilial.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtFilial.setText("51");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel16.setText("Vd:");

        edtEmbalagemVenda.setEditable(false);
        edtEmbalagemVenda.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        edtEmbalagemMaster.setEditable(false);
        edtEmbalagemMaster.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel17.setText("Mst:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel10.setText("%  Rep.");

        edtPercPontoRep.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtPercPontoRep.setText("40");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel11.setText("Reposição:");

        edtPontoReposicao.setEditable(false);
        edtPontoReposicao.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        edtPontoReposicao.setText("1");

        lblCodProduto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblCodProduto.setText("...");

        lblCapacidadeMaster.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblCapacidadeMaster.setText("...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(edtDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edtRua, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(edtPredio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edtNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edtApto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFiltraEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtCodBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnFiltraProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtEmbalagemVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edtEmbalagemMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblProduto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(edtPercPontoRep, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(edtPontoReposicao))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(edtCapacidade))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblCapacidadeMaster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(edtCapFrente, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(edtCapFundo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(edtCapAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnAddProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(edtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltraEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(edtFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel14)
                    .addComponent(lblCodProduto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtRua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtPredio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtApto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(edtCodBarras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltraProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProduto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(edtEmbalagemVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(edtEmbalagemMaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edtCapFrente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCapFundo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtCapAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)))
                    .addComponent(btnAddProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtPercPontoRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtPontoReposicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtCapacidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCapacidadeMaster))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Produto", jPanel1);

        tblProdutoEndereco.setToolTipText("");
        tblProdutoEndereco.setCellSelectionEnabled(true);
        tblProdutoEndereco.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jScrollPane3.setViewportView(tblProdutoEndereco);

        jTabbedPane1.addTab("Produto -> Endereço", jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnFiltraEnderecoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltraEnderecoActionPerformed
        try {
            if (!edtEndereco.getText().isEmpty() && !edtFilial.getText().isEmpty()) {
                localizaEnderecoWms(edtFilial.getText(), edtEndereco.getText());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnFiltraEnderecoActionPerformed");

        }
    }//GEN-LAST:event_btnFiltraEnderecoActionPerformed

    private void edtEnderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtEnderecoKeyPressed
        try {
            if (evt.getKeyCode() == KeyEvent.VK_ACCEPT || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
                if (!edtEndereco.getText().isEmpty() && !edtFilial.getText().isEmpty()) {
                    localizaEnderecoWms(edtFilial.getText(), edtEndereco.getText());
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtEnderecoKeyPressed");

        }
    }//GEN-LAST:event_edtEnderecoKeyPressed

    private void edtCodBarrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtCodBarrasKeyPressed
        try {
            if (evt.getKeyCode() == KeyEvent.VK_ACCEPT || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
                if (!edtCodBarras.getText().isEmpty() && !edtFilial.getText().isEmpty() && !edtEndereco.getText().isEmpty()) {
                    localizaProduto(edtFilial.getText(), edtCodBarras.getText(), edtEndereco.getText());
                }
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCodBarrasKeyPressed");
        }

    }//GEN-LAST:event_edtCodBarrasKeyPressed

    private void btnFiltraProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltraProdutoActionPerformed
        try {
            if (!edtCodBarras.getText().isEmpty() && !edtFilial.getText().isEmpty() && !edtEndereco.getText().isEmpty()) {
                localizaProduto(edtFilial.getText(), edtCodBarras.getText(), edtEndereco.getText());
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnFiltraProdutoActionPerformed");
        }
    }//GEN-LAST:event_btnFiltraProdutoActionPerformed

    private void edtEnderecoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtEnderecoFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edtEndereco.selectAll();
            }
        });
    }//GEN-LAST:event_edtEnderecoFocusGained

    private void edtCodBarrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtCodBarrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtCodBarrasActionPerformed

    private void edtCodBarrasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCodBarrasFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edtEndereco.selectAll();
            }
        });
    }//GEN-LAST:event_edtCodBarrasFocusGained

    private void edtCapFrenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCapFrenteFocusLost
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                calculaCapacidade(Double.parseDouble(edtCapFrente.getText()), Double.parseDouble(edtCapFundo.getText()), Double.parseDouble(edtCapAltura.getText()));
            }
        });
    }//GEN-LAST:event_edtCapFrenteFocusLost

    private void edtCapFundoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCapFundoFocusLost
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                calculaCapacidade(Double.parseDouble(edtCapFrente.getText()), Double.parseDouble(edtCapFundo.getText()), Double.parseDouble(edtCapAltura.getText()));
            }
        });
    }//GEN-LAST:event_edtCapFundoFocusLost

    private void edtCapAlturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCapAlturaFocusLost
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                calculaCapacidade(Double.parseDouble(edtCapFrente.getText()), Double.parseDouble(edtCapFundo.getText()), Double.parseDouble(edtCapAltura.getText()));
            }
        });
    }//GEN-LAST:event_edtCapAlturaFocusLost

    private void btnAddProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProdutoActionPerformed
        try {
            DaoProduto daoProduto = new DaoProduto();
            if (endWms != null && prod != null) {
                daoProduto.atualizar(prod);
            }
            daoProduto = null;
            MessageDialog.saveSucess();
            limpaProduto();
        } catch (Exception ex) {
            trataErro.trataException(ex, "btnAddProdutoActionPerformed");
        }
    }//GEN-LAST:event_btnAddProdutoActionPerformed

    private void edtCapFrenteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCapFrenteFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edtCapFrente.selectAll();
            }
        });
    }//GEN-LAST:event_edtCapFrenteFocusGained

    private void edtCapFundoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCapFundoFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edtCapFundo.selectAll();
            }
        });
    }//GEN-LAST:event_edtCapFundoFocusGained

    private void edtCapAlturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edtCapAlturaFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edtCapAltura.selectAll();
            }
        });
    }//GEN-LAST:event_edtCapAlturaFocusGained

    private void edtCapFrenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtCapFrenteKeyPressed
        try {
            if (evt.getKeyCode() == KeyEvent.VK_ACCEPT || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
                edtCapFundo.requestFocus();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCapFrenteKeyPressed");
        }
    }//GEN-LAST:event_edtCapFrenteKeyPressed

    private void edtCapFundoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtCapFundoKeyPressed
        try {
            if (evt.getKeyCode() == KeyEvent.VK_ACCEPT || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
                edtCapAltura.requestFocus();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCapFundoKeyPressed");
        }
    }//GEN-LAST:event_edtCapFundoKeyPressed

    private void edtCapAlturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtCapAlturaKeyPressed
        try {
            if (evt.getKeyCode() == KeyEvent.VK_ACCEPT || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
                btnAddProduto.requestFocus();
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCapAlturaKeyPressed");
        }
    }//GEN-LAST:event_edtCapAlturaKeyPressed

    private void btnAddProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddProdutoKeyPressed
                try {
            if (evt.getKeyCode() == KeyEvent.VK_ACCEPT || evt.getKeyCode() == KeyEvent.VK_ENTER) {
                btnAddProdutoActionPerformed(null);
            }
        } catch (Exception ex) {
            trataErro.trataException(ex, "edtCapAlturaKeyPressed");
        }
    }//GEN-LAST:event_btnAddProdutoKeyPressed

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
                new vinculaEnderecoLoja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProduto;
    private javax.swing.JButton btnFiltraEndereco;
    private javax.swing.JButton btnFiltraProduto;
    private javax.swing.JTextField edtApto;
    private javax.swing.JTextField edtCapAltura;
    private javax.swing.JTextField edtCapFrente;
    private javax.swing.JTextField edtCapFundo;
    private javax.swing.JTextField edtCapacidade;
    private javax.swing.JTextField edtCodBarras;
    private javax.swing.JTextField edtDeposito;
    private javax.swing.JTextField edtEmbalagemMaster;
    private javax.swing.JTextField edtEmbalagemVenda;
    private javax.swing.JTextField edtEndereco;
    private javax.swing.JTextField edtFilial;
    private javax.swing.JTextField edtNivel;
    private javax.swing.JTextField edtPercPontoRep;
    private javax.swing.JTextField edtPontoReposicao;
    private javax.swing.JTextField edtPredio;
    private javax.swing.JTextField edtRua;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCapacidadeMaster;
    private javax.swing.JLabel lblCodProduto;
    private javax.swing.JLabel lblProduto;
    private winthorDb.util.CustomTable tblProdutoEndereco;
    // End of variables declaration//GEN-END:variables
}
