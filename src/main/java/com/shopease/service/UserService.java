package com.shopease.service;

import java.util.List;

import com.shopease.dao.UserDAO;
import com.shopease.daoimpl.UserDAOImpl;
import com.shopease.model.User;
import com.shopease.utility.PasswordUtil;

public class UserService {

    private UserDAO userDAO = new UserDAOImpl();

    // 1. Register User
    public boolean registerUser(User user) {

        if (user == null) {
            return false;
        }

        if (user.getName() == null || user.getName().trim().isEmpty()
                || user.getEmail() == null || user.getEmail().trim().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()
                || user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return false;
        }

        String email = user.getEmail().trim();

        // Check duplicate email
        if (userDAO.emailExists(email)) {
            return false;
        }

        user.setEmail(email);

        // Encrypt password using BCrypt
        String hashedPassword =
                PasswordUtil.hashPassword(user.getPassword());

        user.setPassword(hashedPassword);

        return userDAO.addUser(user);
    }

    // 2. Login User
    public User loginUser(String email, String password) {

        if (email == null || password == null
                || email.trim().isEmpty() || password.isEmpty()) {
            return null;
        }

        User user = userDAO.getUserByEmail(email.trim());

        if (user == null) {
            return null;
        }

        // Verify password using BCrypt
        if (PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    // 3. Get User By ID
    public User getUserById(int userId) {

        if (userId <= 0) {
            return null;
        }

        return userDAO.getUserById(userId);
    }

    // 4. Get User By Email
    public User getUserByEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return userDAO.getUserByEmail(email.trim());
    }

    // 5. Get All Users
    public List<User> getAllUsers() {

        return userDAO.getAllUsers();
    }

    // 6. Update User Profile
    public boolean updateUser(User user) {

        if (user == null || user.getUserId() <= 0) {
            return false;
        }

        if (user.getName() == null || user.getName().trim().isEmpty()
                || user.getEmail() == null || user.getEmail().trim().isEmpty()
                || user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return false;
        }

        // Get existing user details
        User existingUser = userDAO.getUserById(user.getUserId());

        if (existingUser == null) {
            return false;
        }

        String newEmail = user.getEmail().trim();

        // Check whether the new email belongs to another user
        if (!existingUser.getEmail().equalsIgnoreCase(newEmail)
                && userDAO.emailExists(newEmail)) {
            return false;
        }

        // Preserve existing BCrypt password
        user.setEmail(newEmail);
        user.setPassword(existingUser.getPassword());

        return userDAO.updateUser(user);
    }

    // 7. Change Password
    public boolean changePassword(int userId, String currentPassword,
                                  String newPassword) {

        if (userId <= 0 || currentPassword == null
                || currentPassword.isEmpty()
                || newPassword == null || newPassword.isEmpty()) {
            return false;
        }

        // Get existing user
        User user = userDAO.getUserById(userId);

        if (user == null) {
            return false;
        }

        // Verify current password
        if (!PasswordUtil.verifyPassword(
                currentPassword, user.getPassword())) {
            return false;
        }

        // Hash new password
        String hashedPassword = PasswordUtil.hashPassword(newPassword);

        user.setPassword(hashedPassword);

        return userDAO.updateUser(user);
    }

    // 8. Delete User
    public boolean deleteUser(int userId) {

        if (userId <= 0) {
            return false;
        }

        return userDAO.deleteUser(userId);
    }

    // 9. Check Email Exists
    public boolean emailExists(String email) {

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return userDAO.emailExists(email.trim());
    }
}