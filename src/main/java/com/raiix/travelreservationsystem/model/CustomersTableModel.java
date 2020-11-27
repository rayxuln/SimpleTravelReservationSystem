package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CustomersTableModel extends BasicTableModel {
    public CustomersTableModel(App a) {
        super(a);
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
}
