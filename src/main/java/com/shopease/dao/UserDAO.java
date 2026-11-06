package com.shopease.dao;

import java.util.List;
import com.shopease.model.User;

public interface UserDAO {

    // CREATE
    boolean addUser(User user);

    // READ
    User getUserById(int userId);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    // UPDATE
    boolean updateUser(User user);

    // DELETE
    boolean deleteUser(int userId);

    // Additional validation
    boolean emailExists(String email);
}