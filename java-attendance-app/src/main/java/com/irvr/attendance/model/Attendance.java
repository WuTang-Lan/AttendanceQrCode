package com.irvr.attendance.model;

import java.time.LocalDateTime;

public class Attendance {
    private int id;
    private String studentName;
    private int sessionId;
    private String courseName;
    private LocalDateTime timeMarked;

    public Attendance() {}

    public Attendance(String studentName, int sessionId) {
        this.studentName = studentName;
        this.sessionId = sessionId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public LocalDateTime getTimeMarked() { return timeMarked; }
    public void setTimeMarked(LocalDateTime timeMarked) { this.timeMarked = timeMarked; }
}
