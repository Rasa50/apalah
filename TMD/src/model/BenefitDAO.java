package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BenefitDAO {
    public BenefitDAO() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tbenefit (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "skor INT DEFAULT 0, " +
                    "peluru_meleset INT DEFAULT 0, " +
                    "peluru_akhir INT DEFAULT 0)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Benefit getUserData(String user) {
        String sql = "SELECT * FROM tbenefit WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Benefit(rs.getString("username"), rs.getInt("skor"),
                        rs.getInt("peluru_meleset"), rs.getInt("peluru_akhir"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void upsertUser(String user) {
        String sql = "INSERT IGNORE INTO tbenefit(username, skor, peluru_meleset, peluru_akhir) VALUES(?, 0, 0, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateScore(String user, int s, int m, int a) {
        String sql = "UPDATE tbenefit SET skor = ?, peluru_meleset = ?, peluru_akhir = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s);
            pstmt.setInt(2, m);
            pstmt.setInt(3, a);
            pstmt.setString(4, user);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Benefit> getAll() {
        List<Benefit> list = new ArrayList<>();
        String sql = "SELECT * FROM tbenefit ORDER BY skor DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Benefit(rs.getString("username"), rs.getInt("skor"),
                        rs.getInt("peluru_meleset"), rs.getInt("peluru_akhir")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}