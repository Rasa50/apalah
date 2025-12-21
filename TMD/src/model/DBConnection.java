package model;

import java.sql.*;

public class DBConnection {
    // Sesuaikan dengan nama database yang dibuat di XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/db_hide_and_seek";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            // Driver untuk MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }
}