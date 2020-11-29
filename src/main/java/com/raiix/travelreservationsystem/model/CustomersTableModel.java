package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CustomersTableModel extends BasicTableModel {
    public CustomersTableModel(App a, boolean e) {
        super(a, e);
        columns_name = new String[]{"编号", "姓名"};

        tableName = "customers";
        primaryKey = "id";

        refresh();
    }

    public CustomersTableModel(App a) {
        super(a, true);
        columns_name = new String[]{"编号", "姓名"};

        tableName = "customers";
        primaryKey = "id";

        refresh();
    }

    public void add(String name)
    {
        try {
            app.MySql().prepare("insert into "+tableName+"(name) values (?)")
                    .setString(name)
                    .execute();

            refresh();

            fireTableRowsInserted(0, rowCount-1);
        }
        catch (SQLIntegrityConstraintViolationException e)
        {
            fireTableChanged(new InvalidAddKeyTableModelEvent(this));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String getName(int id)
    {
        try {
            ResultSet rs = app.MySql().prepare("select name from "+tableName+" where id=?;")
                    .setInt(id)
                    .execute();

            if(rs.next())
            {
                return rs.getString(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
