package com.raiix.travelreservationsystem;

import com.raiix.travelreservationsystem.model.*;
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
        busTableModel = new BusTableModel(this);
        hotelsTableModel = new HotelsTableModel(this);
        customersTableModel = new CustomersTableModel(this);


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
