# IRVR Attendance System

## Overview
A Java JavaFX desktop application for Interactive Real-time Verifiable Response (IRVR) attendance tracking. This application implements time-sensitive, dynamic codes to prevent attendance fraud and ensure only physically present students can mark attendance.

**UI Technology:** HTML/CSS/JavaScript (easier to understand than FXML!)  
**Target Platform:** Desktop application for Windows, macOS, and Linux  
**Database:** XAMPP MySQL for local deployment

## Purpose
Allow lecturers to track student attendance using dynamic IRVR codes (QR-based) that expire and refresh automatically. The system prevents proxy attendance through time-sensitive validation and session management.

## Current State
- Complete Java JavaFX desktop application (JDK 11 compatible)
- HTML-based UI with JavaFX WebView (easy to understand and customize!)
- Dynamic time-sensitive IRVR codes (5-minute expiration with auto-refresh)
- Session management with unique crypto-generated session codes
- Student and lecturer registration system
- MySQL database integration (XAMPP compatible)
- QR code generation with ZXing library
- Maven project structure for IntelliJ IDEA
- Real-time attendance tracking and validation
- Fraud prevention with time validation

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

## Recent Changes (November 3, 2025)
- **Updated to JDK 11** for better compatibility
- **Converted UI to HTML/CSS/JavaScript** (much easier to understand than FXML!)
- Created JavaFX WebView-based architecture with JavaScript bridge
- Complete Java Maven project for IntelliJ IDEA
- MySQL database integration for XAMPP
- Auto-refreshing IRVR codes with 5-minute expiration
- Session management with crypto-generated unique session IDs
- Student and lecturer registration with authentication
- QR code generation using ZXing library
- Time validation to prevent expired code usage
- Fraud prevention (one mark per session, session-specific codes)
- Comprehensive documentation for local deployment

## Project Architecture

### Java Application (JavaFX Desktop)
Located in: **`java-attendance-app/`** folder

#### Backend (Java)
- **Main.java**: Application entry point
- **Models** (User, Session, Attendance): Data structures
- **DAOs** (UserDAO, SessionDAO, AttendanceDAO): Database operations
- **Services** (SessionService): Business logic for IRVR sessions
- **Views** (LoginView, RegisterView, LecturerView, StudentView): WebView-based UI controllers
- **DatabaseConnection**: MySQL JDBC connection manager
- **QRCodeGenerator**: QR code generation utility using ZXing

#### Database (MySQL via XAMPP)
- **Tables**:
  - `users`: Stores user accounts (lecturers and students)
  - `sessions`: Manages active IRVR sessions with expiration times
  - `attendance`: Records attendance entries (prevents duplicate marking per session)

#### Frontend (HTML/CSS/JavaScript)
Renders inside JavaFX WebView:
- **login.html**: Login page with embedded CSS/JS
- **register.html**: Registration page for students and lecturers
- **lecturer.html**: Lecturer dashboard with auto-refreshing QR codes
- **student.html**: Student attendance marking and history viewing
- **JavaScript Bridge**: Connects HTML to Java backend methods

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

### Auto-Refreshing Time-Sensitive Codes
- Each IRVR code expires after 5 minutes and **automatically generates a new code**
- Frontend polls every 3 seconds to detect and display new codes without manual intervention
- Session remains active until lecturer manually stops it
- Prevents students from sharing codes outside of class time
- No need to restart sessions - codes refresh seamlessly during active class

### Session Management
- Unique crypto-generated session codes per class
- Lecturers can start/stop sessions with course names
- All attendance marks during a session are linked to the same session ID
- Students cannot mark attendance for expired sessions

### Fraud Prevention
- One attendance mark per student per session (database constraint)
- Time validation ensures codes are only valid during active class
- Session-specific codes prevent code reuse across different classes
- Auto-refresh prevents code sharing outside the 5-minute window

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
- ✅ **Auto-refreshing dynamic codes** (codes expire every 5 minutes and regenerate automatically)
- ✅ **Seamless code rotation** (frontend detects and displays new codes without manual restart)
- ✅ Unique session management per class (all attendance tied to same session)
- ✅ Student registration system
- ✅ Student attendance viewing
- ✅ Fraud prevention (one mark per session, time validation, session-specific codes)
- ✅ Lecturer session control (start/stop sessions manually)
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
