package com.eventmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.eventmgmt.models.Club;
import com.eventmgmt.utils.DatabaseConnection;

public class ClubDAO {
    private final Connection connection;

    public ClubDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insert(Club club) throws SQLException {
        String sql = "INSERT INTO clubs (name, president_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, club.getName());
            stmt.setInt(2, club.getPresidentId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                club.setId(rs.getInt(1));
            }
        }
    }

    public List<Club> getAll() throws SQLException {
        List<Club> list = new ArrayList<>();
        String sql = "SELECT * FROM clubs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Club(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("president_id")
                ));
            }
        }
        return list;
    }

    public Club findByName(String name) throws SQLException {
        String sql = "SELECT * FROM clubs WHERE lower(name) = lower(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? new Club(rs.getInt("id"), rs.getString("name"), rs.getInt("president_id")) : null;
        }
    }
}
