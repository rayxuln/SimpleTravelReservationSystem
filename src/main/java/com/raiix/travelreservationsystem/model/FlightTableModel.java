package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class FlightTableModel extends BasicTableModel {
    public FlightTableModel(App a, boolean e) {
        super(a, e);
        columns_name = new String[]{"航班号", "价格", "座位数", "剩余座位数", "起飞地", "目的地"};

        tableName = "flights";
        primaryKey = "flightNum";

        refresh();
    }
    public FlightTableModel(App a) {
        super(a, true);
        columns_name = new String[]{"航班号", "价格", "座位数", "剩余座位数", "起飞地", "目的地"};

        tableName = "flights";
        primaryKey = "flightNum";

        refresh();
    }

    public void add(String flightNum, double price, int num, String fromCity, String arivCity)
    {
        try {
            app.MySql().prepare("insert into "+tableName+" values (?,?,?,?,?,?)")
                    .setString(flightNum)
                    .setDouble(price)
                    .setInt(num)
                    .setInt(num)
                    .setString(fromCity)
                    .setString(arivCity)
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
