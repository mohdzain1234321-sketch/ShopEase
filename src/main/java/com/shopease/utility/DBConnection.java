package com.shopease.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/shopease"
    );

    private static final String USERNAME = System.getenv().getOrDefault(
            "DB_USERNAME",
            "root"
    );

    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    URL, USERNAME, PASSWORD
            );

            System.out.println("Database connected successfully!");

            return connection;

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Database connection failed.", e);
        }
    }
}