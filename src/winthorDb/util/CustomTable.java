/*
 * CustomTable.java
 *
 * Created on 16 de Fevereiro de 2006, 23:08
 */
package winthorDb.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import winthorDb.Main;
import winthorDb.error.trataErro;
import winthorDb.jpa.dao.DaoDirect;
import winthorDb.oracleDb.IntegracaoWinthorDb;

/**
 *
 * @author Ageu Rover
 */
public class CustomTable extends javax.swing.JTable {

    private String[] columns;
    private boolean zeroRows = true;
    private boolean autoFit = true;
    private int tamanhoColuna[] = null;
    private int[] columnWidths = null;
    private Color color;
    public String valor = "S";

    /**
     * Creates new form BeanForm
     */
    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public CustomTable() {
        initComponents();
        Main.idColunaColor = -1;
        Main.nameColunaColor = null;
        Main.valorColunaColor = null;
        Main.typeColunaColor = 0;

        setAutoscrolls(true);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        getTableHeader().setDefaultRenderer(new HeaderRenderer());
        ToolTipManager.sharedInstance().unregisterComponent(this);
        ToolTipManager.sharedInstance().unregisterComponent(getTableHeader());
    }

    @Override
    public boolean isCellEditable(int rowIndex, int vColIndex) {
        return false;
    }

    public void setColumnWidths(int[] width) {
        autoFit = false;
        columnWidths = width;
        if (columnWidths.length <= 0) {
            return;
        }
        try {
            for (int i = 0; i < columnWidths.length; i++) {
                TableColumn col = getColumnModel().getColumn(i);
                col.setMinWidth(columnWidths[i]);
                col.setMaxWidth(columnWidths[i]);
                col.setPreferredWidth(columnWidths[i]);
            }
        } catch (Exception ex) {
            //trataErro.trataException(ex, "setColumnWidths");
        }
    }

    public void setColumnWidthsAndOffset(int[] width) {
        autoFit = false;
        columnWidths = width;
        if (columnWidths.length <= 0) {
            return;
        }
        if (columnWidths.length <= 0) {
            return;
        }
        try {
            for (int i = 0; i < columnWidths.length; i++) {
                columnWidths[i] *= getDefaultWidth();
                columnWidths[i] += 5;
                TableColumn col = getColumnModel().getColumn(i);
                col.setMinWidth(columnWidths[i]);
                col.setMaxWidth(columnWidths[i]);
                col.setPreferredWidth(columnWidths[i]);
            }
        } catch (Exception ex) {
            //trataErro.trataException(ex, "setColumnWidthsAndOffset");
        }
    }

    /**
     * Pinta a linha conforme a cor informada setColorRow
     *
     * @param typeColuna operadores: 1 =, 2 >, 3 <, 4!=, 5 is empty, 6 is not
     * empty, 0 default contem; @param coluna nome da coluna para ser validad a
     * @param coluna @param valor valor a ser comparado @param cor cor da linha
     * conforme tabe l
     * a AWT
     * @param cor
     *
     */
    public void setColorRow(int typeColuna, String coluna, String valor, Color cor) {
        try {
            Main.typeColunaColor = typeColuna;
            Main.nameColunaColor = coluna;
            Main.valorColunaColor = valor;
            int col = getColummnNumber(coluna);
            Main.idColunaColor = col;
            Main.corColuna = cor;
            //set custom renderer on table
            setDefaultRenderer(Object.class, new CustomColorTableRenderer());
            repaint();
        } catch (Exception ex) {
            trataErro.trataException(ex, "setColorRow");
        }
    }

    public int[] getColumnWidths() {
        return columnWidths;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
        setModel(new javax.swing.table.DefaultTableModel(
                new Object[1][columns.length],
                columns));
        if (autoFit) {
            autoFitTable(this);
        }

        if (columnWidths != null) {
            setColumnWidths(columnWidths);
        }
    }

    public void clearTableData() {
        Object[][] ptr = null;
        ptr = new Object[1][];
        for (int i = 0; i < ptr.length - 1; i++) {
            ptr[i] = new Object[i];
        }

        zeroRows = true;

        setModel(new javax.swing.table.DefaultTableModel(ptr, columns));
        if (autoFit) {
            autoFitTable(this);
        }

        if (columnWidths != null) {
            setColumnWidths(columnWidths);
        }
    }

    public void setTableData(Object[][] data) {
        Object[][] ptr = null;

        if (data.length == 0) {
            ptr = new Object[data.length + 1][];
            System.arraycopy(data, 0, ptr, 0, ptr.length - 1);

            zeroRows = true;
        } else {
            ptr = data;
            zeroRows
                    = false;
        }

        setModel(new javax.swing.table.DefaultTableModel(ptr, columns));
        if (autoFit) {
            autoFitTable(this);
        }

        if (columnWidths != null) {
            setColumnWidths(columnWidths);
        }
    }

    public void setTableDataX(String sqlString) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        try {
            wint.openConectOracle();
            //setTableData(wint.selectResultSet(sqlString));
            //setTableData(DaoDirect.select(sqlString));
            ResultSet rs = wint.selectResultSet(sqlString);
            ResultSetTableModel1 model = new ResultSetTableModel1(rs);
            setModel(model);

        } catch (Exception e) {
        } finally {
            wint.closeConectOracle();
        }
    }

    public void setTableData(String sqlString) throws Exception {
        IntegracaoWinthorDb wint = new IntegracaoWinthorDb();
        try {
            wint.openConectOracle();
            setTableData(wint.selectResultSet(sqlString));
            //setTableData(DaoDirect.select(sqlString));

        } catch (Exception e) {
        } finally {
            wint.closeConectOracle();
        }
    }

    public void setTableData(String sqlString, int Posicao, int limite) {
        try {
            setTableData(DaoDirect.selectLimited(sqlString, Posicao, limite));

        } catch (Exception e) {
        }

    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    public void setTableData(ResultSet data) {

        Vector columnHeads = new Vector();

        Vector dataRows = new Vector();

        try {
            // carega as colunas do resultset
            ResultSetMetaData rsmd = data.getMetaData();
            tamanhoColuna = new int[rsmd.getColumnCount()];
            int x = 0;
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                //columnHeads.addElement(rsmd.getColumnName(i));
                columnHeads.addElement(rsmd.getColumnLabel(i));
                // carega o tamanho das colunas do resultset
                tamanhoColuna[x] = rsmd.getColumnLabel(i).length() + 1;
//                if (rsmd.getColumnDisplaySize(i) > 60) {
//                    tamanhoColuna[x] = 60;
//                } else {
//                    if (rsmd.getColumnType(i) == Types.DECIMAL || rsmd.getColumnType(i) == Types.DATE) {
//                        tamanhoColuna[x] = 15;
//                    } else {
//                        if (rsmd.getColumnType(i) == Types.INTEGER) {
//                            tamanhoColuna[x] = 10;
//                        } else {
//                            tamanhoColuna[x] = rsmd.getColumnDisplaySize(i);
//                        }
//                    }
//                }
                x++;

            }

            while (data.next()) {
                dataRows.addElement(getNextRow(data, rsmd));
            }

//            // carega os dados do resultset
//            data.first();
//
//            do {
//                dataRows.addElement(getNextRow(data, rsmd));
//            } while (data.next());
        } catch (SQLException e) {
        }

        setModel(new DefaultTableModel(dataRows, columnHeads));

        if (tamanhoColuna != null) {
            setColumnWidthsAndOffset(tamanhoColuna);
        }

        if (autoFit) {
            autoFitTable(this);
        }

        if (columnWidths != null) {
            setColumnWidths(columnWidths);
        }

        // colcoa o foco no primero elemento da tabela
        if (getRowCount() >= 0) {
            clearSelection();
            changeSelection(0, 0, false, false);
        }

    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    public void setTableData(Vector dataRows, Vector columnHeads, int[] ColumnSize) {

        tamanhoColuna = ColumnSize;

        setModel(new DefaultTableModel(dataRows, columnHeads));

        if (tamanhoColuna != null) {
            setColumnWidthsAndOffset(tamanhoColuna);
        }

        if (autoFit) {
            autoFitTable(this);
        }

        if (columnWidths != null) {
            setColumnWidths(columnWidths);
        }
        // colcoa o foco no primero elemento da tabela
        if (getRowCount() >= 0) {
            clearSelection();
            changeSelection(0, 0, false, false);
        }
    }

    private int calculaTamanho(int Atual, int novo) {
        int tamanho = 5;
        if (novo > 250) {
            novo = 250;
        }
        if (Atual > novo) {
            tamanho = Atual;
        } else {
            tamanho = novo;
        }

        return tamanho;
    }

    @SuppressWarnings("UseSpecificCatch")
    public Vector getNextRow(ResultSet rs, ResultSetMetaData rsmd) {
        Vector currentRow = new Vector();
        int x = 0;
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                if (rs != null) {
                    switch (rsmd.getColumnType(i)) {
                        case Types.VARCHAR:
                            if (rs.getString(i) != null) {
                                currentRow.addElement(rs.getString(i));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], rs.getString(i).length() + 1);
                            } else {
                                currentRow.addElement("");
                            }

                            break;
                        case Types.LONGVARCHAR:
                            if (rs.getString(i) != null) {
                                currentRow.addElement(rs.getString(i));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], rs.getString(i).length() + 1);
                            } else {
                                currentRow.addElement("");
                            }

                            break;
                        case Types.LONGNVARCHAR:
                            if (rs.getString(i) != null) {
                                currentRow.addElement(rs.getString(i));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], rs.getString(i).length() + 1);
                            } else {
                                currentRow.addElement("");
                            }

                            break;
                        case Types.NVARCHAR:
                            if (rs.getString(i) != null) {
                                currentRow.addElement(rs.getString(i));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], rs.getString(i).length() + 1);
                            } else {
                                currentRow.addElement("");
                            }

                            break;
                        case Types.INTEGER:
                            try {
                                currentRow.addElement(Formato.intToStr(rs.getInt(i)));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], Formato.intToStr(rs.getInt(i)).length());
                            } catch (SQLException ex) {
                                currentRow.addElement(Formato.intToStr(0));
                                //ex.printStackTrace();
                            }

                            break;
                        case Types.BIGINT:
                            try {
                                currentRow.addElement(rs.getLong(i));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], 10);
                            } catch (SQLException ex) {
                                currentRow.addElement(Formato.intToStr(0));
                                //ex.printStackTrace();
                            }

                            break;
                        case Types.DATE:
                            try {
                                currentRow.addElement(Formato.dateToStr(rs.getDate(i)));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], Formato.dateToStr(rs.getDate(i)).length());
                            } catch (SQLException ex) {
                                currentRow.addElement(Formato.intToStr(0));
                                //ex.printStackTrace();
                            }

                            break;
                        case Types.DECIMAL:
                            try {
                                currentRow.addElement(Formato.decimalToCurrStr(rs.getBigDecimal(i)));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], Formato.decimalToCurrStr(rs.getBigDecimal(i)).length());
                            } catch (SQLException ex) {
                                currentRow.addElement(Formato.intToStr(0));
                                //ex.printStackTrace();
                            }

                            break;
                        case Types.DOUBLE:
                            try {
                                currentRow.addElement(Formato.doubleToCurrStr(rs.getDouble(i)));
                                tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], Formato.doubleToCurrStr(rs.getDouble(i)).length());
                            } catch (SQLException ex) {
                                currentRow.addElement(Formato.intToStr(0));
                                //ex.printStackTrace();
                            }

                            break;
                        default:
                            try {
                                if (rs.getObject(i) != null) {
                                    if (!rs.getObject(i).toString().isEmpty()) {
                                        currentRow.addElement(rs.getObject(i).toString());
                                        tamanhoColuna[x] = calculaTamanho(tamanhoColuna[x], rs.getObject(i).toString().length() + 1);
                                    } else {
                                        currentRow.addElement(" ");
                                    }
                                } else {
                                    currentRow.addElement(" ");
                                }
                            } catch (SQLException ex) {
                                currentRow.addElement("<Erro> " + ex.getMessage());
                                //ex.printStackTrace();
                            }
                    }
                }
                x++;
            }

        } catch (SQLException e) {
        }

        return currentRow;
    }

    public Object[][] getTableData() {
        int rowCount = getModel().getRowCount();
        int columnCount = getModel().getColumnCount();

        Object[][] curTableData = new Object[rowCount][columnCount];

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {

                curTableData[row][column] = getModel().getValueAt(row, column);
            }
        }

        return curTableData;
    }

    /**
     *
     * This method is responsible for writing the contents of a JTable (2d array
     * object) to disk (csv text file)
     * <p>
     *
     * @param aData - the 2D data (Jtable contents) to be stored to disk
     * @param file
     * @throws java.lang.Exception
     *
     * @see
     *
     */
    public void writeToDisk(Object[][] aData, String file) throws Exception {
        //Headers
        try (FileOutputStream fout = new FileOutputStream(file, false)) {
            //Headers
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout))) {
                //Headers
                for (int col = 0; col < getColumnCount(); col++) {
                    bw.append(getColumnName(col));
                    bw.append(';');
                }
                bw.append('\n');

                //Data
                for (Object[] aData1 : aData) {
                    for (Object item : aData1) {
                        if (item == null) {
                            bw.append("null");
                            // The comma separated value
                            bw.append(';');
                        } else {
                            /* In my particular example, I am doing some checking on
                            the 2d array for types:
                            if the data is not of type null (as checked above)
                            then it must be of type Integer.
                            This is because the 2D data array only contains data of either
                            Integer or null
                            each of these object types have a method called toString().
                            we need this in order to convert the types to a string prior to wrting them to
                            the file.
                             */
                            bw.append(item.toString());
                            bw.append(';');
                        }
                    } //end column loop (inner loop)
                    bw.append('\n');
                } //end row loop (outer loop)
            }
            fout.flush();
        }

    }//end of readFileFromDisk

    public int getColummnNumber(String nomeColuna) throws Exception {
        int coluna = 0;
        if (getSelectedRow() >= 0) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (getColumnName(i).equalsIgnoreCase(nomeColuna.toLowerCase())) {
                    coluna = i;
                }
            }
        }
        return coluna;
    }

    public Object getConteudoRowSelected(String nomeColuna) throws Exception {
        Object conteudo = null;
        int coluna = 0;
        conteudo = "";

        if (getSelectedRow() >= 0) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (getColumnName(i).equalsIgnoreCase(nomeColuna.toLowerCase())) {
                    coluna = i;
                    conteudo = getValueAt(getSelectedRow(), coluna);
                }
            }
        }
        return conteudo;
    }

    public Object getConteudoRow(String nomeColuna, int linha) throws Exception {
        Object conteudo = "";
        //int coluna = 0;
        //highlightLastRow(linha);
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equalsIgnoreCase(nomeColuna.toLowerCase())) {
                //coluna = i;
                conteudo = getValueAt(linha, i);
            }

        }
        return conteudo;
    }

    public double getSomaColunaSelecionada(String nomeColuna) throws Exception {
        double soma = 0.00;
        int select[] = getSelectedRows();
        String dado = "";
        for (int i = 0; i < select.length; i++) {
            dado = getConteudoRow(nomeColuna.toLowerCase(), select[i]).toString();
            if (dado != null) {
                if (!dado.isEmpty()) {
                    soma += Formato.doubleToCurrStr(dado.replace(".", ""));
                }
            }
        }
        return soma;
    }

    public double getSomeColuna(String nomeColuna) throws Exception {
        double soma = 0.00;

        for (int i = 0; i < getRowCount(); i++) {
            if (getConteudoRow(nomeColuna.toLowerCase(), i) != null) {
                if (!getConteudoRow(nomeColuna.toLowerCase(), i).toString().isEmpty()) {
                    soma += Formato.doubleToCurrStr(getConteudoRow(nomeColuna.toLowerCase(), i).toString().replace(".", ""));
                }
            }
        }
        return soma;
    }

    public boolean getLocalizaRow(String nomeColuna, String Dado) throws Exception {
        boolean ret = false;
        int columnIndex = 0;
        boolean includeSpacing = true;

        for (int i = 0; i < getRowCount(); i++) {
            if (getConteudoRow(nomeColuna.toLowerCase(), i) != null) {
                if (!getConteudoRow(nomeColuna.toLowerCase(), i).toString().isEmpty()) {
                    if (getConteudoRow(nomeColuna.toLowerCase(), i).toString().equalsIgnoreCase(Dado)) {
                        highlightLastRow(i);
                        ret = true;
                        // posiciona o scrol da tabela proximo da linha
                        Rectangle cellRect = getCellRect(i, columnIndex, includeSpacing);
                        scrollRectToVisible(cellRect);
                        repaint();
                        i = getRowCount();
                    }
                }
            }
        }
        return ret;
    }

    /**
     *
     * @param calculo - Somar , contar, media
     * @param somaCol - coluna a ser calculada
     * @param filtroCol - coluna com o dado a ser filtrado
     * @param dadoFiltoCol - dado que se for verdadeiro permitira o calculo
     * @return double com o calculo realizado
     * @throws Exception
     */
    public double getComputColuna(String calculo, String somaCol, String filtroCol, String dadoFiltoCol) throws Exception {
        double soma = 0.00;

        if (calculo.equalsIgnoreCase("SOMAR")) {
            for (int i = 0; i < getRowCount(); i++) {
                // verifica se a coluna a ser filtrada nao é nula
                if (getConteudoRow(filtroCol, i) != null) {
                    // verifica se a coluna a ser filtrada não esta vazia
                    if (!getConteudoRow(filtroCol, i).toString().isEmpty()) {
                        // verifica se a coluna a ser calcula não esta vazia
                        if (getConteudoRow(filtroCol, i).toString().equalsIgnoreCase(dadoFiltoCol)) {
                            if (!getConteudoRow(somaCol, i).toString().isEmpty()) {
                                soma += Formato.doubleToCurrStr(getConteudoRow(somaCol, i).toString().replace(".", ""));
                            }
                        }
                    } else {
                        if (!getConteudoRow(somaCol, i).toString().isEmpty()) {
                            soma += Formato.doubleToCurrStr(getConteudoRow(somaCol, i).toString().replace(".", ""));
                        }
                    }
                }
            }
        }

        // conta as ocorrecias para o filtro informado 
        if (calculo.equalsIgnoreCase("CONTAR")) {
            for (int i = 0; i < getRowCount(); i++) {
                // verifica se a coluna a ser filtrada nao é nula
                if (getConteudoRow(filtroCol, i) != null) {
                    // verifica se a coluna a ser filtrada não esta vazia
                    if (!getConteudoRow(filtroCol, i).toString().isEmpty()) {
                        // verifica se a coluna a ser calcula não esta vazia
                        if (getConteudoRow(filtroCol, i).toString().equalsIgnoreCase(dadoFiltoCol)) {
                            if (!getConteudoRow(somaCol, i).toString().isEmpty()) {
                                soma++;
                            }
                        }
                    } else {
                        if (!getConteudoRow(somaCol, i).toString().isEmpty()) {
                            soma++;
                        }
                    }
                }
            }
        }

        // Calcula a media aritimedica as ocorrecias para o filtro informado 
        int ocorrencia = 1;
        double somaOcor = 0.00;
        if (calculo.equalsIgnoreCase("MEDIA")) {
            for (int i = 0; i < getRowCount(); i++) {
                // verifica se a coluna a ser filtrada nao é nula
                if (getConteudoRow(filtroCol, i) != null) {
                    // verifica se a coluna a ser filtrada não esta vazia
                    if (!getConteudoRow(filtroCol, i).toString().isEmpty()) {
                        // verifica se a coluna a ser calcula não esta vazia
                        if (getConteudoRow(filtroCol, i).toString().equalsIgnoreCase(dadoFiltoCol)) {
                            ocorrencia++;
                            if (!getConteudoRow(somaCol, i).toString().isEmpty()) {
                                somaOcor += Formato.doubleToCurrStr(getConteudoRow(somaCol, i).toString().replace(".", ""));
                            }
                        }
                    } else {
                        ocorrencia++;
                        if (!getConteudoRow(somaCol, i).toString().isEmpty()) {
                            somaOcor += Formato.doubleToCurrStr(getConteudoRow(somaCol, i).toString().replace(".", ""));
                        }
                    }
                }
            }
            soma = somaOcor / ocorrencia;
        }

        return soma;
    }

    private static void autoFitTable(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxColumnWidth = 0;
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            for (int row = 0; row < table.getRowCount(); row++) {
                try {
                    JLabel defaultLabel = new JLabel((String) table.getValueAt(row, column));
                    maxColumnWidth = Math.max(maxColumnWidth, getCompMetricWidth(defaultLabel));
                } catch (Exception e) {

                    JLabel defaultLabel = new JLabel((String) table.getColumnModel().getColumn(column).getHeaderValue());
                    maxColumnWidth = Math.max(maxColumnWidth, getCompMetricWidth(defaultLabel));
                }
            }
            tableColumn.setMinWidth(maxColumnWidth);
            tableColumn.setMaxWidth(maxColumnWidth);
            tableColumn.setPreferredWidth(maxColumnWidth);
        }

    }

    private static int getDefaultWidth() {
        JLabel jl = new JLabel();
        FontMetrics fm = jl.getFontMetrics(jl.getFont());
        return fm.stringWidth("0");
    }

    private static int getCompMetricWidth(JLabel defaultLabel) {
        String text = defaultLabel.getText();
        Font font = defaultLabel.getFont();
        FontMetrics fontMetrics = defaultLabel.getFontMetrics(font);
        return SwingUtilities.computeStringWidth(fontMetrics, text) + 15; // offset
    }

    public boolean isAutoFit() {
        return autoFit;
    }

    public void setAutoFit(boolean autoFit) {
        this.autoFit = autoFit;
    }

    public void highlightLastRow(int row) {
        int lastrow = getRowCount();
        if (row == lastrow - 1) {
            setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            setRowSelectionInterval(row, row);
        }
        setColumnSelectionInterval(0, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAutoCreateRowSorter(true);
        setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Título 1", "Título 2"
            }
        ));
        setToolTipText("<F2> para selecionar o item ou Duplo clique com o mouse");
        setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        setMaximumSize(new java.awt.Dimension(90, 16));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

class HeaderRenderer
        extends DefaultTableCellRenderer {

    public HeaderRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT);
        setOpaque(false);

        // This call is needed because DefaultTableCellRenderer calls setBorder()
        // in its constructor, which is executed after updateUI()
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JTableHeader h = table != null ? table.getTableHeader() : null;

        if (h != null) {

            setForeground(h.getForeground());
            setBackground(h.getBackground());
            setEnabled(h.isEnabled());
            setComponentOrientation(h.getComponentOrientation());

            setFont(h.getFont());
        } else {
            /*
             * Use sensible values instead of random leftover values from the
             * last call
             */
            setEnabled(true);
            setComponentOrientation(ComponentOrientation.UNKNOWN);

            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setFont(UIManager.getFont("TableHeader.font"));
        }

        if (isSelected) {
            setForeground(Color.ORANGE);
        }

        setValue(value);

        return this;

    }
}

//Custom DefaultTableCellRenderer
class CustomColorTableRenderer extends DefaultTableCellRenderer {

    // You should override getTableCellRendererComponent
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);

        if (value != null) {

            // You know valorColunaColor column includes string
            if (Main.idColunaColor == column) {
                String dataValeu = value.toString();

                if ((dataValeu != null) && (Main.nameColunaColor != null) && (Main.valorColunaColor != null)) {

                    // 1 =, 2 >, 3 <; 4!=; 5 is empty; 6 is not empty; 0 default contem;
                    switch (Main.typeColunaColor) {
                        case 1:
                            if (Formato.strToInt(Formato.somenteNumeros(dataValeu)) == Formato.strToInt(Main.valorColunaColor)) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                            break;
                        case 2:
                            if (Formato.strToInt(Formato.somenteNumeros(dataValeu)) > Formato.strToInt(Main.valorColunaColor)) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                            break;
                        case 3:
                            if (Formato.strToInt(Formato.somenteNumeros(dataValeu)) < Formato.strToInt(Main.valorColunaColor)) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                            break;
                        case 4:
                            if (Formato.strToInt(Formato.somenteNumeros(dataValeu)) != Formato.strToInt(Main.valorColunaColor)) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                            break;
                        case 5:
                            if (dataValeu.isEmpty()) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                            break;
                        case 6:
                            if (!dataValeu.isEmpty()) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                            break;
                        default:
                            if (dataValeu.contains(Main.valorColunaColor)) {
                                c.setForeground(Main.corColuna);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                    }
                }
            }
        }
        return c;
    }
}
