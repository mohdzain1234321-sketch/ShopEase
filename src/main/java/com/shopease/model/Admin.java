package com.shopease.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private int adminId;
    private String username;
    private String email;
    private String password;
    private String status;
    private Timestamp createdAt;

    public Admin() {}

    public Admin(int adminId, String username, String email,
                 String password, String status, Timestamp createdAt) {
        this.adminId = adminId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
