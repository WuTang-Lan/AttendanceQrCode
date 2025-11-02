package com.irvr.attendance.model;

import java.time.LocalDateTime;

public class Session {
    private int id;
    private String sessionCode;
    private String courseName;
    private String lecturerId;
    private boolean isActive;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public Session() {}

    public Session(String sessionCode, String courseName, String lecturerId, LocalDateTime expiresAt) {
        this.sessionCode = sessionCode;
        this.courseName = courseName;
        this.lecturerId = lecturerId;
        this.expiresAt = expiresAt;
        this.isActive = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
