package com.eventmgmt.utils;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "role TEXT NOT NULL);";

        String createClubsTable = "CREATE TABLE IF NOT EXISTS clubs ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "president_id INTEGER, "
                + "FOREIGN KEY(president_id) REFERENCES users(id));";

        String createEventsTable = "CREATE TABLE IF NOT EXISTS events ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "budget REAL, "
                + "status TEXT NOT NULL, "
                + "club_id INTEGER, "
                + "FOREIGN KEY(club_id) REFERENCES clubs(id));";

        String createRegistrationsTable = "CREATE TABLE IF NOT EXISTS registrations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "event_id INTEGER, "
                + "fee REAL, "
                + "FOREIGN KEY(user_id) REFERENCES users(id), "
                + "FOREIGN KEY(event_id) REFERENCES events(id));";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(createUsersTable);
            stmt.execute(createClubsTable);
            stmt.execute(createEventsTable);
            stmt.execute(createRegistrationsTable);
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS uq_registrations_user_event "
                    + "ON registrations(user_id, event_id)");
            addColumnIfMissing(conn, "users", "password_hash", "TEXT");
            addColumnIfMissing(conn, "users", "active", "INTEGER NOT NULL DEFAULT 1");
            
        } catch (Exception e) {
            throw new IllegalStateException("Database initialization failed", e);
        }
    }

    private static void addColumnIfMissing(Connection conn, String table, String column, String definition)
            throws Exception {
        try (var rs = conn.createStatement().executeQuery("PRAGMA table_info(" + table + ")")) {
            while (rs.next()) if (column.equalsIgnoreCase(rs.getString("name"))) return;
        }
        try (Statement statement = conn.createStatement()) {
            statement.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
        }
    }
}
