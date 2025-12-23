package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BenefitDAO {
    public BenefitDAO() {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;

        try (Statement stmt = conn.createStatement()) {
            // Pastikan tabel memiliki struktur yang benar
            stmt.execute("CREATE TABLE IF NOT EXISTS tbenefit (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "skor INT DEFAULT 0, " +
                    "peluru_meleset INT DEFAULT 0, " +
                    "peluru_akhir INT DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE data: Gunakan WHERE username agar tidak mengubah data orang lain!
    public void updateScore(String user, int s, int m, int a) {
        // Spesifikasi: skor & meleset AKUMULATIF (ditambah), peluru_akhir sisa terakhir (ditimpa)
        String sql = "UPDATE tbenefit SET skor = skor + ?, peluru_meleset = peluru_meleset + ?, peluru_akhir = ? WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, s);    // Tambah skor
            pstmt.setInt(2, m);    // Tambah meleset
            pstmt.setInt(3, a);    // Set sisa amunisi akhir
            pstmt.setString(4, user); // Kunci agar hanya user ini yang berubah

            int rowsAffected = pstmt.executeUpdate();

            // Opsional: Jika user belum ada (karena suatu alasan), lakukan insert
            if (rowsAffected == 0) {
                insertNewUser(user, s, m, a);
            }
            System.out.println("Data berhasil diupdate untuk: " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menjamin user terdaftar di awal tanpa merusak data yang sudah ada
    public void upsertUser(String user) {
        // INSERT IGNORE memastikan jika "Rex ID" sudah ada, tidak akan terjadi apa-apa (data lama aman)
        String sql = "INSERT IGNORE INTO tbenefit(username, skor, peluru_meleset, peluru_akhir) VALUES(?, 0, 0, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertNewUser(String user, int s, int m, int a) {
        String sql = "INSERT INTO tbenefit(username, skor, peluru_meleset, peluru_akhir) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setInt(2, s);
            pstmt.setInt(3, m);
            pstmt.setInt(4, a);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Benefit> getAll() {
        List<Benefit> list = new ArrayList<>();
        String sql = "SELECT * FROM tbenefit ORDER BY skor DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Benefit(
                        rs.getString("username"),
                        rs.getInt("skor"),
                        rs.getInt("peluru_meleset"),
                        rs.getInt("peluru_akhir")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}