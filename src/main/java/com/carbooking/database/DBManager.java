package com.carbooking.database;
import com.carbooking.utils.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    String url = PropertiesLoader.loadProperty("db.url");
                    String user = PropertiesLoader.loadProperty("db.user");
                    String password = PropertiesLoader.loadProperty("db.password");

                    connection = DriverManager.getConnection(url, user, password);

                } catch (SQLException e) {
                    throw new RuntimeException("Cannot connect to database", e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}