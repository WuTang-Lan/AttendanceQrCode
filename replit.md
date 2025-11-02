# IRVR Attendance System

## Overview
A modern web-based Interactive Real-time Verifiable Response (IRVR) attendance tracking system. This application implements time-sensitive, dynamic codes to prevent attendance fraud and ensure only physically present students can mark attendance.

## Purpose
Allow lecturers to track student attendance using dynamic IRVR codes (QR-based) that expire and refresh automatically. The system prevents proxy attendance through time-sensitive validation and session management.

## Current State
- Fully functional IRVR-based web application running on Node.js/Express
- Dynamic time-sensitive codes (5-minute expiration)
- Session management with unique session codes
- Student registration and authentication
- Student attendance viewing capability
- Lecturer session control (start/stop)
- SQLite database for data persistence
- Modern, responsive web interface
- Real-time attendance tracking and validation

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
- Implemented IRVR (Interactive Real-time Verifiable Response) code system
- Added dynamic time-sensitive codes with 5-minute expiration
- Implemented session management with unique session IDs per class
- Added student registration functionality for both students and lecturers
- Created student attendance viewing page
- Added lecturer session control (start/stop sessions)
- Implemented time validation to prevent expired code scanning
- Migrated from MySQL to SQLite database
- Created Express.js backend with comprehensive REST API
- Built responsive HTML/CSS/JS frontend
- Set up deployment configuration for VM (Always-On)

## Project Architecture

### Backend (Node.js/Express)
- **server.js**: Main Express server
  - User authentication (login/registration)
  - Session management (start/stop/validate)
  - Dynamic IRVR code generation with crypto tokens
  - Time-sensitive attendance marking with validation
  - Student-specific and lecturer-specific attendance queries
  - SQLite database integration
  - Serves static frontend files

### Database (SQLite)
- **Tables**:
  - `users`: Stores user accounts (lecturers and students)
  - `sessions`: Manages active IRVR sessions with expiration times
  - `attendance`: Records attendance entries with session codes and timestamps (prevents duplicate marking per session)

### Frontend (HTML/CSS/JS)
- **public/index.html**: Login page with registration link
- **public/register.html**: Registration page for students and lecturers
- **public/lecturer.html**: Lecturer dashboard with session control and dynamic IRVR code
- **public/student.html**: Student dashboard with attendance viewing access
- **public/student-attendance.html**: Student's personal attendance records
- **public/scan.html**: Attendance marking page (accessed via IRVR code scan)
- **public/css/style.css**: Modern, responsive styling
- **public/js/**: Client-side JavaScript for real-time interactivity and validation

## Demo Accounts
- **Lecturer**: lecturer1 / pass123
- **Student 1**: student1 / pass123
- **Student 2**: student2 / pass123

## How to Use

### Registration (New Users):
1. Click "Register here" on the login page
2. Enter username, password (min 6 characters), and select role (Student/Lecturer)
3. After successful registration, login with your credentials

### For Lecturers:
1. Login with lecturer credentials
2. Enter a course name and click "Start New Session"
3. A dynamic IRVR code will be generated (valid for 5 minutes)
4. Display the code on screen/projector for students to scan
5. View real-time attendance as students mark their presence
6. Click "Stop Session" when class ends
7. View all attendance records organized by course and session

### For Students:
1. Login with student credentials
2. Click "View My Attendance" to see all your attendance records
3. To mark attendance: Scan the lecturer's IRVR code with your phone
4. Enter your student ID when prompted
5. Submit to mark your attendance (can only mark once per session)
6. Codes expire after 5 minutes for security

## Key IRVR Features

### Time-Sensitive Validation
- Each session code expires after 5 minutes
- Prevents students from sharing codes outside of class time
- Automatic session expiration and cleanup

### Session Management
- Unique crypto-generated session codes per class
- Lecturers can start/stop sessions with course names
- Students cannot mark attendance for expired sessions

### Fraud Prevention
- One attendance mark per student per session (database constraint)
- Time validation ensures codes are only valid during active class
- Session-specific codes prevent code reuse

### Student Transparency
- Students can view their complete attendance history
- Records show course name and timestamp for each attendance
- Easy access to personal attendance data

## Technical Stack
- **Backend**: Node.js, Express.js
- **Database**: SQLite (better-sqlite3)
- **QR Generation**: qrcode library
- **Frontend**: Vanilla HTML, CSS, JavaScript
- **Port**: 5000 (configured for Replit webview)

## Dependencies
- express: Web framework
- better-sqlite3: SQLite database driver
- qrcode: Dynamic IRVR code generation
- body-parser: Request body parsing
- crypto: Secure session token generation (built-in Node.js module)

## Project Structure
```
/
├── server.js                      # Express backend server with IRVR logic
├── package.json                   # Node.js dependencies
├── attendance.db                  # SQLite database (auto-created)
├── public/                        # Frontend files
│   ├── index.html                 # Login page
│   ├── register.html              # Registration page
│   ├── lecturer.html              # Lecturer dashboard with session control
│   ├── student.html               # Student dashboard
│   ├── student-attendance.html    # Student attendance viewing page
│   ├── scan.html                  # IRVR code attendance marking
│   ├── css/
│   │   └── style.css              # Responsive styles
│   └── js/
│       ├── login.js               # Login logic
│       ├── register.js            # Registration logic
│       ├── lecturer.js            # Session management & IRVR code refresh
│       ├── student.js             # Student dashboard logic
│       ├── student-attendance.js  # Student attendance viewing
│       └── scan.js                # Time-sensitive attendance marking
└── replit.md                      # This file
```

## Deployment
Configured for Replit's VM (Always-On) deployment. Using VM instead of autoscale because:
- SQLite is a file-based database that requires a single persistent instance
- Autoscale with multiple instances would cause database synchronization issues
- VM deployment ensures the SQLite database file remains consistent

## Implemented IRVR Features ✅
- ✅ Dynamic time-sensitive codes (5-minute expiration)
- ✅ Unique session management per class
- ✅ Student registration system
- ✅ Student attendance viewing
- ✅ Fraud prevention (one mark per session, time validation)
- ✅ Lecturer session control (start/stop)
- ✅ Real-time attendance tracking
- ✅ Secure session token generation (crypto)

## Future Enhancements (Security & Features)

### Critical Security Improvements:
- **Password hashing**: Implement bcrypt for secure password storage
- **JWT or session tokens**: Add proper server-side authentication for API endpoints
- **API authentication**: Protect all endpoints with auth middleware
- **Role-based access control**: Enforce lecturer/student permissions on backend
- **Geolocation verification**: Optional proximity check for attendance marking

### Feature Enhancements:
- Attendance reports and analytics dashboard
- Export attendance data (CSV/PDF format)
- Email/SMS notifications for attendance updates
- Admin panel for user management
- Attendance percentage calculations
- Course management system
- Multiple concurrent sessions per lecturer
- Attendance alerts for low attendance
