package com.raiix.travelreservationsystem.model;

import com.raiix.travelreservationsystem.App;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

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

    public boolean has(int type, String key)
    {
        try {
            ResultSet rs = app.MySql().prepare("select * from "+tableName+" where custID=? and type=? and `key`=?;")
                                    .setInt(customerID)
                                    .setInt(type)
                                    .setString(key)
                                    .execute();
            if(rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public void book(int type, String key) throws BasicTableModel.InvalidAvailDeltaException, RecordAlreadyExistException{
        try {
            if(has(type, key)) throw new RecordAlreadyExistException();

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
            if(resultSet.isAfterLast()) return;
            unbook(resultSet.getInt(2), resultSet.getString(3));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void unbookAll()
    {
        try {
            ArrayList<Integer> type_list = new ArrayList<Integer>();
            ArrayList<String> key_list = new ArrayList<String>();

            getTypeKeyList(type_list, key_list);

            for(int i=0; i<type_list.size(); ++i)
            {
                unbook(type_list.get(i), key_list.get(i));
            }
        } catch (BasicTableModel.InvalidAvailDeltaException e) {
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

            app.MySql().prepare("delete from " + tableName + " where custID=? and type=? and `key`=?;")
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
            int type = resultSet.getInt(2);
            String key = resultSet.getString(3);
            switch (type)
            {
                case FLIGHT:
                    String from = app.flightTableModel.getFromCity(key);
                    String to = app.flightTableModel.getArivCity(key);
                    return "从 "+from+" 到 "+to+" 的航班"+key;
                case BUS:
                    return key + " 的巴士";
                case HOTEL:
                    return key + " 的宾馆";
            }
            return "无效的预定信息";
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void getTypeKeyList(ArrayList<Integer> type_list, ArrayList<String> key_list)
    {
        try {
            resultSet.beforeFirst();
            while (resultSet.next())
            {
                type_list.add(resultSet.getInt(2));
                key_list.add(resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class RecordAlreadyExistException extends Exception {};
}
