package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BenefitDAO {
    public BenefitDAO() {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("Error: Koneksi database gagal! Pastikan XAMPP (MySQL) sudah Start.");
            return; // Keluar dari constructor agar tidak NullPointer
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tbenefit (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "skor INT DEFAULT 0, " +
                    "peluru_meleset INT DEFAULT 0, " +
                    "peluru_akhir INT DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Benefit> getAll() {
        List<Benefit> list = new ArrayList<>();
        String sql = "SELECT * FROM tbenefit ORDER BY skor DESC";

        Connection conn = DBConnection.getConnection();
        if (conn == null) return list; // Kembalikan list kosong saja, jangan lanjut ke query

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                list.add(new Benefit(rs.getString("username"), rs.getInt("skor"),
                        rs.getInt("peluru_meleset"), rs.getInt("peluru_akhir")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void upsertUser(String user) {
        String sql = "INSERT OR IGNORE INTO tbenefit(username) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateScore(String user, int s, int m, int a) {
        String sql = "UPDATE tbenefit SET skor = skor + ?, peluru_meleset = peluru_meleset + ?, peluru_akhir = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s);
            pstmt.setInt(2, m);
            pstmt.setInt(3, a);
            pstmt.setString(4, user);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}