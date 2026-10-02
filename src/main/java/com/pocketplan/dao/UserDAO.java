package com.pocketplan.dao;

import com.pocketplan.model.User;
import com.pocketplan.util.DBConnection;

import java.sql.*;

public class UserDAO {

    public boolean register(User user) {
        String sql =
                "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Registration error: " + e.getMessage()
            );
            return false;
        }
    }

    public User findByEmail(String email) {
        String sql =
                "SELECT id, name, email, password " +
                "FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Find user error: " + e.getMessage()
            );
        }

        return null;
    }

    public boolean emailExists(String email) {
        String sql =
                "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println(
                    "Email check error: " + e.getMessage()
            );
            return false;
        }
    }
}
