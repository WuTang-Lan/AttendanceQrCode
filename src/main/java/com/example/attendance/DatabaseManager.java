package com.example.attendance;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP default

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static String validateUser(String username, String password) {
        String sql = "SELECT role FROM users WHERE username=? AND password=?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // returns 'lecturer' or 'student'
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveAttendance(String studentId, String studentName, String sessionId) {
        String sql = "INSERT INTO attendance (student_id, student_name, session_id, time_marked) VALUES (?, ?, ?, NOW())";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, studentName);
            stmt.setString(3, sessionId);
            stmt.executeUpdate();
            System.out.println("✅ Attendance saved for " + studentName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
