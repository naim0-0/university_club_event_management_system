package com.eventmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.eventmgmt.models.Event;
import com.eventmgmt.utils.DatabaseConnection;

public class EventDAO {
    private final Connection connection;

    public EventDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insert(Event event) throws SQLException {
        String sql = "INSERT INTO events (title, budget, status, club_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, event.getTitle());
            stmt.setDouble(2, event.getBudget());
            stmt.setString(3, event.getStatus());
            stmt.setInt(4, event.getClubId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                event.setId(rs.getInt(1));
            }
        }
    }

    public void updateStatus(int eventId, String newStatus) throws SQLException {
        String sql = "UPDATE events SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();
        }
    }

    public Event getById(int id) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDouble("budget"),
                    rs.getString("status"),
                    rs.getInt("club_id")
                );
            }
        }
        return null;
    }

    public List<Event> getAll() throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Event(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDouble("budget"),
                    rs.getString("status"),
                    rs.getInt("club_id")
                ));
            }
        }
        return list;
    }

    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
