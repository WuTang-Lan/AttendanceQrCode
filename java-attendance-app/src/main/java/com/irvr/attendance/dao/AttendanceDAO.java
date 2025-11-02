package com.irvr.attendance.dao;

import com.irvr.attendance.database.DatabaseConnection;
import com.irvr.attendance.model.Attendance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    private final Connection connection;

    public AttendanceDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean markAttendance(String studentName, int sessionId) {
        String query = "INSERT INTO attendance (student_name, session_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentName);
            stmt.setInt(2, sessionId);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Attendance already marked for this session");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public List<Attendance> getAttendanceByStudent(String studentName) {
        List<Attendance> records = new ArrayList<>();
        String query = "SELECT a.*, s.course_name FROM attendance a " +
                      "JOIN sessions s ON a.session_id = s.id " +
                      "WHERE a.student_name = ? ORDER BY a.time_marked DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getInt("id"));
                attendance.setStudentName(rs.getString("student_name"));
                attendance.setSessionId(rs.getInt("session_id"));
                attendance.setCourseName(rs.getString("course_name"));
                attendance.setTimeMarked(rs.getTimestamp("time_marked").toLocalDateTime());
                records.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<Attendance> getAttendanceByLecturer(String lecturerId) {
        List<Attendance> records = new ArrayList<>();
        String query = "SELECT a.*, s.course_name FROM attendance a " +
                      "JOIN sessions s ON a.session_id = s.id " +
                      "WHERE s.lecturer_id = ? ORDER BY a.time_marked DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getInt("id"));
                attendance.setStudentName(rs.getString("student_name"));
                attendance.setSessionId(rs.getInt("session_id"));
                attendance.setCourseName(rs.getString("course_name"));
                attendance.setTimeMarked(rs.getTimestamp("time_marked").toLocalDateTime());
                records.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<Attendance> getAttendanceBySession(int sessionId) {
        List<Attendance> records = new ArrayList<>();
        String query = "SELECT * FROM attendance WHERE session_id = ? ORDER BY time_marked DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getInt("id"));
                attendance.setStudentName(rs.getString("student_name"));
                attendance.setSessionId(rs.getInt("session_id"));
                attendance.setTimeMarked(rs.getTimestamp("time_marked").toLocalDateTime());
                records.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}
