package com.shopease.service;

import com.shopease.dao.AdminDAO;
import com.shopease.daoimpl.AdminDAOImpl;
import com.shopease.model.Admin;
import com.shopease.utility.PasswordUtil;

public class AdminService {

    private final AdminDAO adminDAO = new AdminDAOImpl();

    public Admin authenticate(String username, String rawPassword) {
        if (username == null || rawPassword == null
                || username.trim().isEmpty() || rawPassword.isEmpty()) {
            return null;
        }

        Admin admin = adminDAO.getByUsername(username.trim());

        if (admin == null || !"ACTIVE".equalsIgnoreCase(admin.getStatus())) {
            return null;
        }

        if (!PasswordUtil.verifyPassword(rawPassword, admin.getPassword())) {
            return null;
        }

        // Never keep the password hash in the HTTP session.
        admin.setPassword(null);
        return admin;
    }

    /**
     * Call only from a trusted setup utility, not from a public web form.
     * Password is hashed with the same BCrypt utility used by customer accounts.
     */
    public boolean createAdmin(String username, String email, String rawPassword) {
        if (username == null || email == null || rawPassword == null
                || username.trim().isEmpty() || email.trim().isEmpty()
                || rawPassword.length() < 12) {
            return false;
        }

        Admin admin = new Admin();
        admin.setUsername(username.trim());
        admin.setEmail(email.trim());
        admin.setPassword(PasswordUtil.hashPassword(rawPassword));
        admin.setStatus("ACTIVE");
        return adminDAO.createAdmin(admin);
    }
}
