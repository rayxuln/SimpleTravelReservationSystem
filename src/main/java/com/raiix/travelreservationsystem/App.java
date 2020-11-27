package com.raiix.travelreservationsystem;

import com.raiix.travelreservationsystem.model.BusTableModel;
import com.raiix.travelreservationsystem.model.CustomersTableModel;
import com.raiix.travelreservationsystem.model.FlightTableModel;
import com.raiix.travelreservationsystem.model.HotelsTableModel;
import com.raiix.travelreservationsystem.window.MainWindow;

import java.sql.SQLException;

public class App {

    MySQLDriver mysql;

    // windows
    public MainWindow mainWindow;

    // models
    public FlightTableModel flightTableModel;
    public BusTableModel busTableModel;
    public HotelsTableModel hotelsTableModel;
    public CustomersTableModel customersTableModel;

    public void run(String[] args) {

        // init mysql
        mysql = new MySQLDriver();
        try {
            mysql.init();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        // init models
        flightTableModel = new FlightTableModel(this);
        flightTableModel.refresh();
        busTableModel = new BusTableModel(this);
        busTableModel.refresh();
        hotelsTableModel = new HotelsTableModel(this);
        hotelsTableModel.refresh();
        customersTableModel = new CustomersTableModel(this);
        customersTableModel.refresh();

        mainWindow = new MainWindow(this);
        mainWindow.setVisible(true);
    }

    public MySQLDriver MySql(){
        return mysql;
    }

    public void quit(){
        System.exit(0);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }
}
