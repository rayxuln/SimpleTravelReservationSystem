package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ReservationListModel extends AbstractListModel {

    private App app;
    private final String tableName = "reservation";

    private int customerID;

    private ResultSet resultSet;
    private int size;

    public static final int FLIGHT = 1;
    public static final int BUS = 2;
    public static final int HOTEL = 3;

    public ReservationListModel(App _app, int _customerID)
    {
        app = _app;
        customerID = _customerID;

        refresh();
    }

    public void setCustomerID(int _customerID)
    {
        customerID = _customerID;

        refresh();
    }

    public void refresh(){
        try {
            resultSet = app.MySql().prepare("select * from "+tableName+" where custID=?;")
                                    .setInt(customerID)
                                    .execute();

            resultSet.last();
            size = resultSet.getRow();

            fireContentsChanged(this, 0, size);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void book(int type, String key) throws BasicTableModel.InvalidAvailDeltaException{
        try {

            try {
                switch (type)
                {
                    case FLIGHT:
                        app.flightTableModel.updateAvail(key, -1);
                        break;
                    case BUS:
                        app.busTableModel.updateAvail(key, -1);
                        break;
                    case HOTEL:
                        app.hotelsTableModel.updateAvail(key, -1);
                        break;
                }
            }catch (BasicTableModel.InvalidKeyException e)
            {
                e.printStackTrace();
                return;
            }

            app.MySql().prepare("insert into "+tableName+" values(?,?,?)")
                    .setInt(customerID)
                    .setInt(type)
                    .setString(key)
                    .execute();

            refresh();
        }catch (SQLIntegrityConstraintViolationException ignore) { }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void unbook(int index) throws BasicTableModel.InvalidAvailDeltaException
    {
        try {
            resultSet.absolute(index+1);
            unbook(resultSet.getInt(2), resultSet.getString(3));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void unbook(int type, String key) throws BasicTableModel.InvalidAvailDeltaException
    {
        try {
            try {
                switch (type) {
                    case FLIGHT:
                        app.flightTableModel.updateAvail(key, +1);
                        break;
                    case BUS:
                        app.busTableModel.updateAvail(key, +1);
                        break;
                    case HOTEL:
                        app.hotelsTableModel.updateAvail(key, +1);
                        break;
                }
            } catch (BasicTableModel.InvalidKeyException e) {
                e.printStackTrace();
                return;
            }


            app.MySql().prepare("delete from " + tableName + " where custID=? and type=? and key=?;")
                    .setInt(customerID)
                    .setInt(type)
                    .setString(key)
                    .execute();

            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Object getElementAt(int index) {
        try {
            resultSet.absolute(index+1);
            return resultSet.getString(3);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
