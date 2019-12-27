/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winthorDb.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ageurover
 */
public class ResultSetTableModel1 extends AbstractTableModel {

    /**
     * Constructs the table model.
     *
     * @param aResultSet the result set to display.
     */
    public ResultSetTableModel1(ResultSet aResultSet) {
        rs = aResultSet;
        try {
            rsmd = rs.getMetaData();
        } catch (SQLException e) {
        }
    }

    @Override
    public String getColumnName(int c) {
        try {
            return rsmd.getColumnName(c + 1);
        } catch (SQLException e) {
            return "";
        }
    }

    @Override
    public int getColumnCount() {
        try {
            return rsmd.getColumnCount();
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public Object getValueAt(int r, int c) {
        try {
            rs.absolute(r + 1);
            return rs.getObject(c + 1);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public int getRowCount() {
        try {
            rs.last();
            return rs.getRow();
        } catch (SQLException e) {
            return 0;
        }
    }

    private ResultSet rs;
    private ResultSetMetaData rsmd;
}
