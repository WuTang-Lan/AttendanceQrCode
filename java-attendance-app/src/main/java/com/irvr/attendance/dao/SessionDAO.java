package com.irvr.attendance.dao;

import com.irvr.attendance.database.DatabaseConnection;
import com.irvr.attendance.model.Session;

import java.sql.*;
import java.time.LocalDateTime;

public class SessionDAO {
    private final Connection connection;

    public SessionDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public int createSession(Session session) {
        String query = "INSERT INTO sessions (session_code, course_name, lecturer_id, expires_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, session.getSessionCode());
            stmt.setString(2, session.getCourseName());
            stmt.setString(3, session.getLecturerId());
            stmt.setTimestamp(4, Timestamp.valueOf(session.getExpiresAt()));
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Session getActiveSession(String lecturerId) {
        String query = "SELECT * FROM sessions WHERE lecturer_id = ? AND is_active = TRUE ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Session session = new Session();
                session.setId(rs.getInt("id"));
                session.setSessionCode(rs.getString("session_code"));
                session.setCourseName(rs.getString("course_name"));
                session.setLecturerId(rs.getString("lecturer_id"));
                session.setActive(rs.getBoolean("is_active"));
                session.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
                session.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateSessionCode(int sessionId, String newCode, LocalDateTime newExpiry) {
        String query = "UPDATE sessions SET session_code = ?, expires_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newCode);
            stmt.setTimestamp(2, Timestamp.valueOf(newExpiry));
            stmt.setInt(3, sessionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stopSession(String lecturerId) {
        String query = "UPDATE sessions SET is_active = FALSE WHERE lecturer_id = ? AND is_active = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, lecturerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Session getSessionByCode(String sessionCode) {
        String query = "SELECT * FROM sessions WHERE session_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, sessionCode);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Session session = new Session();
                session.setId(rs.getInt("id"));
                session.setSessionCode(rs.getString("session_code"));
                session.setCourseName(rs.getString("course_name"));
                session.setLecturerId(rs.getString("lecturer_id"));
                session.setActive(rs.getBoolean("is_active"));
                session.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
