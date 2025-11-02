# QR Attendance System

## Overview
A modern web-based QR code attendance tracking system. This application was converted from a JavaFX desktop application to a web application to work seamlessly in the Replit environment.

## Purpose
Allow lecturers to track student attendance using QR codes. Lecturers can display a QR code that students scan to mark their attendance.

## Current State
- Fully functional web application running on Node.js/Express
- SQLite database for data persistence
- Modern, responsive web interface
- QR code generation and scanning functionality
- Real-time attendance tracking

## ⚠️ IMPORTANT SECURITY NOTICE
**This is a demonstration/learning project. The current implementation has known security limitations:**

1. **No Authentication on API Endpoints**: All API endpoints are publicly accessible
2. **Plaintext Passwords**: User passwords are stored without hashing
3. **No Session Management**: Login state is only stored in browser localStorage
4. **Public Attendance Marking**: Anyone can mark attendance without proper authorization
5. **Hard-coded Session IDs**: No per-class or per-session token validation

**Before using in production, you MUST:**
- Implement password hashing (bcrypt)
- Add server-side session management or JWT tokens
- Protect API endpoints with authentication middleware
- Generate unique, signed QR codes per session
- Add role-based access control (RBAC)

**This version preserves the original GitHub project's demo functionality while converting it to a web format.**

## Recent Changes (November 2, 2025)
- Rebuilt entire system from JavaFX desktop app to web application
- Migrated from MySQL to SQLite database
- Created Express.js backend with REST API
- Built responsive HTML/CSS/JS frontend
- Implemented QR code generation for web
- Added real-time attendance dashboard for lecturers
- Set up deployment configuration

## Project Architecture

### Backend (Node.js/Express)
- **server.js**: Main Express server
  - API endpoints for login, QR generation, attendance marking
  - SQLite database integration
  - Serves static frontend files

### Database (SQLite)
- **Tables**:
  - `users`: Stores user accounts (lecturers and students)
  - `attendance`: Records attendance entries with timestamps

### Frontend (HTML/CSS/JS)
- **public/index.html**: Login page
- **public/lecturer.html**: Lecturer dashboard with QR code display
- **public/student.html**: Student welcome page
- **public/scan.html**: Attendance marking page (accessed via QR code)
- **public/css/style.css**: Modern, responsive styling
- **public/js/**: Client-side JavaScript for interactivity

## Demo Accounts
- **Lecturer**: lecturer1 / pass123
- **Student 1**: student1 / pass123
- **Student 2**: student2 / pass123

## How to Use

### For Lecturers:
1. Login with lecturer credentials
2. Display the QR code on screen/projector
3. Students scan the QR code to mark attendance
4. View real-time attendance records on the dashboard

### For Students:
1. Scan the lecturer's QR code with your phone
2. Enter your student ID (e.g., student1)
3. Submit to mark your attendance

## Technical Stack
- **Backend**: Node.js, Express.js
- **Database**: SQLite (better-sqlite3)
- **QR Generation**: qrcode library
- **Frontend**: Vanilla HTML, CSS, JavaScript
- **Port**: 5000 (configured for Replit webview)

## Dependencies
- express: Web framework
- better-sqlite3: SQLite database driver
- qrcode: QR code generation
- body-parser: Request body parsing

## Project Structure
```
/
├── server.js              # Express backend server
├── package.json           # Node.js dependencies
├── attendance.db          # SQLite database (auto-created)
├── public/                # Frontend files
│   ├── index.html         # Login page
│   ├── lecturer.html      # Lecturer dashboard
│   ├── student.html       # Student page
│   ├── scan.html          # Attendance marking
│   ├── css/
│   │   └── style.css      # Styles
│   └── js/
│       ├── login.js       # Login logic
│       ├── lecturer.js    # Dashboard logic
│       ├── student.js     # Student page logic
│       └── scan.js        # Attendance marking logic
└── replit.md             # This file
```

## Deployment
Configured for Replit's VM (Always-On) deployment. Using VM instead of autoscale because:
- SQLite is a file-based database that requires a single persistent instance
- Autoscale with multiple instances would cause database synchronization issues
- VM deployment ensures the SQLite database file remains consistent

## Future Enhancements (Security & Features)

### Critical Security Improvements:
- **Password hashing**: Implement bcrypt for secure password storage
- **JWT or session tokens**: Add proper server-side authentication
- **API authentication**: Protect all endpoints with auth middleware
- **Secure QR codes**: Generate signed tokens per session
- **Role-based access control**: Enforce lecturer/student permissions

### Feature Enhancements:
- Multiple session support with unique session IDs
- Attendance reports and analytics
- Export attendance data (CSV/PDF)
- Mobile app version
- Email notifications
- Admin panel for user management
