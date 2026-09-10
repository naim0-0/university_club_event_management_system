package com.eventmgmt.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:event_management.db";

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
            try (var statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not open the event database", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not reconnect to the event database", e);
        }
        return connection;
    }
}
