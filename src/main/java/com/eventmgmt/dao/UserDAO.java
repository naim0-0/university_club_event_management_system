package com.eventmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.eventmgmt.models.User;
import com.eventmgmt.utils.DatabaseConnection;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, role, password_hash, active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getPasswordHash());
            stmt.setInt(5, user.isActive() ? 1 : 0);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        }
    }

    public User getById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role"), rs.getString("password_hash"), rs.getInt("active") == 1
                );
            }
        }
        return null;
    }

    public List<User> getAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role"), rs.getString("password_hash"), rs.getInt("active") == 1
                ));
            }
        }
        return list;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE lower(email) = lower(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("role"), rs.getString("password_hash"), rs.getInt("active") == 1) : null;
        }
    }

    public void updatePasswordHash(int userId, String hash) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE users SET password_hash = ? WHERE id = ?")) {
            stmt.setString(1, hash); stmt.setInt(2, userId); stmt.executeUpdate();
        }
    }
}
