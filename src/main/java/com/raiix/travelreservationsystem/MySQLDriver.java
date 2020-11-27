package com.raiix.travelreservationsystem;

import java.sql.*;

public class MySQLDriver {

    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://localhost:3306/simple_travel_reservation?useSSL=false&serverTimezone=UTC";

    final String USER = "root";
    final String PWD = "ipwis123";

    Connection connection;

    public void init() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PWD);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            try {
                if(connection != null)
                {
                    connection.close();
                }
            }catch (SQLException ignored) { }
            throw e;
        }
    }

    public Prepare prepare(String sql) throws SQLException {
        return new Prepare(connection.prepareCall(sql));
    }

    public ResultSet query(String sql) throws SQLException
    {
        Statement s = connection.createStatement();
        s.execute(sql);
        return s.getResultSet();
    }

    public static class Prepare {
        private final PreparedStatement statement;
        private int count;

        public Prepare(PreparedStatement s)
        {
            statement = s;
            count = 1;
        }

        public Prepare setObject(Object v) throws SQLException {
            statement.setObject(count++, v);
            return this;
        }

        public Prepare setInt(int v) throws SQLException {
            statement.setInt(count++, v);
            return this;
        }
        public Prepare setString(String v) throws SQLException {
            statement.setString(count++, v);
            return this;
        }
        public Prepare setDouble(double v) throws SQLException {
            statement.setDouble(count++, v);
            return this;
        }

        public ResultSet execute() throws SQLException {
            count = 1;
            statement.execute();
            return statement.getResultSet();
        }
    }

}
