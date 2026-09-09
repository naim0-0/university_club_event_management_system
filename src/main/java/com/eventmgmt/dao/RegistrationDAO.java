package com.eventmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.eventmgmt.models.Registration;
import com.eventmgmt.utils.DatabaseConnection;

public class RegistrationDAO {
    private final Connection connection;

    public RegistrationDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insert(Registration reg) throws SQLException {
        String sql = "INSERT INTO registrations (user_id, event_id, fee) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reg.getUserId());
            stmt.setInt(2, reg.getEventId());
            stmt.setDouble(3, reg.getFee());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reg.setId(rs.getInt(1));
            }
        }
    }

    public List<Registration> getByEventId(int eventId) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT * FROM registrations WHERE event_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Registration(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("event_id"),
                    rs.getDouble("fee")
                ));
            }
        }
        return list;
    }

    public List<Registration> getAll() throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT * FROM registrations ORDER BY id DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Registration(rs.getInt("id"), rs.getInt("user_id"),
                        rs.getInt("event_id"), rs.getDouble("fee")));
            }
        }
        return list;
    }

    public boolean exists(int userId, int eventId) throws SQLException {
        String sql = "SELECT 1 FROM registrations WHERE user_id = ? AND event_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            return stmt.executeQuery().next();
        }
    }
}
