package com.example.attendance;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:attendance.db";

    static {
        initializeDatabase();
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL" +
                    ")";
            
            String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "student_name TEXT NOT NULL," +
                    "session_id TEXT NOT NULL," +
                    "time_marked DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            
            stmt.execute(createUsersTable);
            stmt.execute(createAttendanceTable);
            
            String checkUsers = "SELECT COUNT(*) as count FROM users";
            ResultSet rs = stmt.executeQuery(checkUsers);
            if (rs.next() && rs.getInt("count") == 0) {
                String insertSampleUsers = "INSERT INTO users (username, password, role) VALUES " +
                        "('lecturer1', 'pass123', 'lecturer')," +
                        "('student1', 'pass123', 'student')," +
                        "('student2', 'pass123', 'student')";
                stmt.execute(insertSampleUsers);
                System.out.println("✅ Sample users created (lecturer1/pass123, student1/pass123, student2/pass123)");
            }
            
            System.out.println("✅ Database initialized successfully");
        } catch (SQLException e) {
            System.out.println("❌ Database initialization failed: " + e.getMessage());
            e.printStackTrace();
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
