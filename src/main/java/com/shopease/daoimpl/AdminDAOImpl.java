package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shopease.dao.AdminDAO;
import com.shopease.model.Admin;
import com.shopease.utility.DBConnection;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin getByUsername(String username) {
        String sql = "SELECT admin_id, username, email, password, status, created_at "
                   + "FROM admins WHERE username = ? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setEmail(rs.getString("email"));
                    admin.setPassword(rs.getString("password"));
                    admin.setStatus(rs.getString("status"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve administrator.", e);
        }
        return null;
    }

    @Override
    public boolean createAdmin(Admin admin) {
        String sql = "INSERT INTO admins (username, email, password, status) "
                   + "VALUES (?, ?, ?, 'ACTIVE')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPassword());

            if (ps.executeUpdate() != 1) {
                return false;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    admin.setAdminId(keys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            // Duplicate username/email and database errors are reported to the service caller.
            throw new RuntimeException("Unable to create administrator. Check for duplicate username/email.", e);
        }
    }
}
