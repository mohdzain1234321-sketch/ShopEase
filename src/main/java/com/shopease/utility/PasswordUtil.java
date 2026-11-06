package com.shopease.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    // Generate BCrypt password hash
    public static String hashPassword(String password) {

        return encoder.encode(password);
    }

    // Verify password
    public static boolean verifyPassword(
            String rawPassword, String hashedPassword) {

        return encoder.matches(rawPassword, hashedPassword);
    }
}