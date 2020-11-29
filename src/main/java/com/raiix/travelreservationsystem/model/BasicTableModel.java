package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Vector;

public abstract class BasicTableModel extends AbstractTableModel {

    protected App app;

    protected int rowCount;
    protected int columnCount;
    protected ResultSet resultSet;

    protected String[] columns_name;
    protected Vector<String> columns;

    protected String tableName;
    protected String primaryKey;

    protected boolean editable;

    public BasicTableModel(App a, boolean e)
    {
        app = a;
        columns = new Vector<String>();
        editable = e;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable;
    }

    public void refresh(){
        try {
            resultSet = app.MySql().query("select * from "+tableName);

            columns.clear();
            for(int i=0; i<resultSet.getMetaData().getColumnCount(); ++i)
            {
                columns.add(resultSet.getMetaData().getColumnName(i+1));
            }

            resultSet.last();
            rowCount = resultSet.getRow();
            columnCount = resultSet.getMetaData().getColumnCount();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex+1);
            return resultSet.getString(columnIndex+1);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getColumnsName(){
        return columns_name;
    }
    public String getColumnsName(int id){
        return columns_name[id];
    }

    @Override
    public String getColumnName(int column) {
        return columns_name[column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String key = (String) getValueAt(rowIndex, 0);
        String column = columns.get(columnIndex);
        String value = (String) aValue;
        try {
            app.MySql().prepare("update "+tableName+" set "+column+"=? where "+primaryKey+"=?")
                    .setObject(value)
                    .setString(key)
                    .execute();

            refresh();

            fireTableCellUpdated(rowIndex, columnIndex);
        }
        catch (SQLIntegrityConstraintViolationException e)
        {
            fireTableChanged(new InvalidChangedKeyTableModelEvent(this, rowIndex, rowIndex, columnIndex));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(String key)
    {
        try {
            int row = 0;
            resultSet.first();
            while (resultSet.next())
            {
                if(resultSet.getString(1) == key)
                {
                    row = resultSet.getRow()-1;
                }
            }

            app.MySql().prepare("delete from "+tableName+" where "+primaryKey+"=?")
                    .setString(key)
                    .execute();

            refresh();

            fireTableRowsDeleted(row, row);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateAvail(String key, int delta) throws InvalidKeyException, InvalidAvailDeltaException
    {
        try {
            ResultSet rs = app.MySql().prepare("select num, avail from "+tableName+" where "+primaryKey+"=?;")
                                        .setString(key)
                                        .execute();
            if(!rs.next())
                throw new InvalidKeyException();
            int num =rs.getInt(1);
            int avail = rs.getInt(2);
            int new_avail = avail + delta;

            if(new_avail < 0 || new_avail > num)
            {
                throw new InvalidAvailDeltaException();
            }

            app.MySql().prepare("update "+tableName+" set avail=? where "+primaryKey+"=?;")
                        .setInt(new_avail)
                        .setString(key)
                        .execute();

            refresh();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static class InvalidKeyException extends Exception { }
    public static class InvalidAvailDeltaException extends Exception { }

    public static class InvalidChangedKeyTableModelEvent extends TableModelEvent {
        public InvalidChangedKeyTableModelEvent(TableModel source, int firstRow, int lastRow, int column) {
            super(source, firstRow, lastRow, column);
        }
    }

    public static class InvalidAddKeyTableModelEvent extends TableModelEvent {
        public InvalidAddKeyTableModelEvent(TableModel source) {
            super(source);
        }
    }
}
