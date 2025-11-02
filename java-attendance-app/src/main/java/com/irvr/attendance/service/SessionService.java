package com.irvr.attendance.service;

import com.irvr.attendance.dao.SessionDAO;
import com.irvr.attendance.model.Session;
import com.irvr.attendance.util.QRCodeGenerator;

import java.time.LocalDateTime;

public class SessionService {
    private final SessionDAO sessionDAO;
    private static final int CODE_VALIDITY_MINUTES = 5;

    public SessionService() {
        this.sessionDAO = new SessionDAO();
    }

    public Session startNewSession(String lecturerId, String courseName) {
        sessionDAO.stopSession(lecturerId);
        
        String sessionCode = QRCodeGenerator.generateSessionCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_VALIDITY_MINUTES);
        
        Session session = new Session(sessionCode, courseName, lecturerId, expiresAt);
        int sessionId = sessionDAO.createSession(session);
        
        if (sessionId > 0) {
            session.setId(sessionId);
            return session;
        }
        return null;
    }

    public Session getActiveSession(String lecturerId) {
        Session session = sessionDAO.getActiveSession(lecturerId);
        if (session == null) {
            return null;
        }
        
        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            String newCode = QRCodeGenerator.generateSessionCode();
            LocalDateTime newExpiry = LocalDateTime.now().plusMinutes(CODE_VALIDITY_MINUTES);
            sessionDAO.updateSessionCode(session.getId(), newCode, newExpiry);
            session.setSessionCode(newCode);
            session.setExpiresAt(newExpiry);
        }
        
        return session;
    }

    public void stopSession(String lecturerId) {
        sessionDAO.stopSession(lecturerId);
    }

    public Session validateSessionCode(String sessionCode) {
        Session session = sessionDAO.getSessionByCode(sessionCode);
        if (session == null || !session.isActive()) {
            return null;
        }
        
        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            return null;
        }
        
        return session;
    }
}
