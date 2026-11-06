package com.shopease.dao;

import com.shopease.model.Admin;

public interface AdminDAO {
    Admin getByUsername(String username);
    boolean createAdmin(Admin admin);
}
