package com.pocketplan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            getEnv("POCKETPLAN_DB_URL",
                    "jdbc:mysql://localhost:3306/pocketplan?useSSL=false&serverTimezone=Asia/Kolkata");

    private static final String USER =
            getEnv("POCKETPLAN_DB_USER", "root");

    private static final String PASSWORD =
            getEnv("POCKETPLAN_DB_PASSWORD", "");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
