-- IRVR Attendance System Database Schema
-- For XAMPP MySQL Database

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

-- Users table (stores both students and lecturers)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('student', 'lecturer') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sessions table (stores active IRVR sessions)
CREATE TABLE IF NOT EXISTS sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    session_code VARCHAR(100) UNIQUE NOT NULL,
    course_name VARCHAR(200) NOT NULL,
    lecturer_id VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    expires_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lecturer_id) REFERENCES users(username) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Attendance table (stores attendance records)
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(50) NOT NULL,
    session_id INT NOT NULL,
    time_marked TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_attendance (student_name, session_id),
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (student_name) REFERENCES users(username) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert demo accounts (passwords are plain text for demo purposes)
INSERT IGNORE INTO users (username, password, role) VALUES
('lecturer1', 'pass123', 'lecturer'),
('student1', 'pass123', 'student'),
('student2', 'pass123', 'student');

-- Create indexes for performance
CREATE INDEX idx_session_lecturer ON sessions(lecturer_id, is_active);
CREATE INDEX idx_attendance_student ON attendance(student_name);
CREATE INDEX idx_session_code ON sessions(session_code);
