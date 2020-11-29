package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class HotelsTableModel extends BasicTableModel {
    public HotelsTableModel(App a) {
        super(a, true);
        columns_name = new String[]{"地方名", "价格", "房间数", "剩余房间数"};

        tableName = "hotels";
        primaryKey = "location";

        refresh();
    }

    public void add(String location, double price, int num)
    {
        try {
            app.MySql().prepare("insert into "+tableName+" values (?,?,?,?)")
                    .setString(location)
                    .setDouble(price)
                    .setInt(num)
                    .setInt(num)
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
