package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class FlightTableModel extends BasicTableModel {
    public FlightTableModel(App a) {
        super(a, true);
        columns_name = new String[]{"航班号", "价格", "座位数", "剩余座位数", "起飞地", "目的地"};

        tableName = "flights";
        primaryKey = "flightNum";

        refresh();
    }

    public String getFromCity(String flightNum)
    {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()){
                if(resultSet.getString(1).equals(flightNum))
                {
                    return resultSet.getString(5);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getArivCity(String flightNum)
    {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()){
                if(resultSet.getString(1).equals(flightNum))
                {
                    return resultSet.getString(6);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
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
