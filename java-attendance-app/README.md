# IRVR Attendance System - Java Desktop Application

## Overview
A JavaFX desktop application for Interactive Real-time Verifiable Response (IRVR) attendance tracking. This system implements time-sensitive, dynamic QR codes that automatically refresh to prevent attendance fraud.

**💡 Easy-to-Understand UI:** The user interface is built with **HTML, CSS, and JavaScript** instead of FXML, making it much easier to understand and modify. JavaFX WebView renders the HTML pages, and JavaScript communicates with Java code through a simple bridge.

## Features
✅ **Auto-refreshing IRVR Codes** - Codes expire every 5 minutes and regenerate automatically  
✅ **Session Management** - Lecturers can start/stop sessions with unique session IDs  
✅ **Student Registration** - Students and lecturers can self-register  
✅ **Attendance Tracking** - Real-time attendance viewing and history  
✅ **Fraud Prevention** - One mark per session, time validation, session-specific codes  
✅ **MySQL Database** - Stores data in XAMPP MySQL database  
✅ **HTML-Based UI** - Easy to understand and customize HTML/CSS/JavaScript interface  

## Prerequisites

### 1. Java Development Kit (JDK) 17
- Download and install JDK 17 or higher
- Verify installation: `java -version`

### 2. IntelliJ IDEA
- Download IntelliJ IDEA (Community or Ultimate Edition)
- Install Maven plugin (usually pre-installed)

### 3. XAMPP
- Download and install XAMPP from https://www.apachefriends.org/
- Start Apache and MySQL services in XAMPP Control Panel

### 4. Maven
- Maven is included with IntelliJ IDEA
- Or download from https://maven.apache.org/download.cgi

## Setup Instructions

### Step 1: Download the Project
1. Download and extract the `java-attendance-app` folder to your computer
2. Open IntelliJ IDEA

### Step 2: Open Project in IntelliJ
1. Click **File → Open**
2. Navigate to the `java-attendance-app` folder
3. Select the folder and click **OK**
4. IntelliJ will detect it's a Maven project and automatically import dependencies
5. Wait for Maven to download all dependencies (check bottom-right progress bar)

### Step 3: Setup XAMPP MySQL Database
1. Start XAMPP Control Panel
2. Click **Start** on Apache and MySQL modules
3. Open your browser and go to: `http://localhost/phpmyadmin`
4. Click on **SQL** tab
5. Copy and paste the entire content from `database/schema.sql` file
6. Click **Go** to execute the SQL script
7. Verify the `attendance_db` database is created with 3 tables:
   - `users`
   - `sessions`
   - `attendance`

### Step 4: Configure Database Connection (Optional)
If your XAMPP MySQL settings are different from defaults:

1. Open `src/main/resources/database.properties`
2. Update these values if needed:
   ```properties
   db.url=jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC
   db.username=root          # Change if different
   db.password=              # Change if you set a password
   ```

### Step 5: Run the Application

#### Option A: Run from IntelliJ (Recommended)
1. Open `src/main/java/com/irvr/attendance/Main.java`
2. Right-click anywhere in the file
3. Select **Run 'Main.main()'**
4. The application window should appear

#### Option B: Run with Maven
1. Open Terminal in IntelliJ (View → Tool Windows → Terminal)
2. Run:
   ```bash
   mvn clean javafx:run
   ```

#### Option C: Build JAR File
1. Open Terminal in IntelliJ
2. Build the project:
   ```bash
   mvn clean package
   ```
3. The JAR file will be in `target/attendance-system-1.0.0.jar`
4. Run the JAR:
   ```bash
   java -jar target/attendance-system-1.0.0.jar
   ```

## Usage Guide

### For Lecturers:
1. **Login** with lecturer credentials (or register a new account)
2. **Enter course name** (e.g., "Computer Science 101")
3. **Click "Start New Session"**
4. A QR code will be displayed - **project this on the screen** for students
5. The code **automatically refreshes every 5 minutes** (no need to restart!)
6. View real-time attendance as students scan
7. **Click "Stop Session"** when class ends
8. View attendance records in the table below

### For Students:
1. **Login** with student credentials (or register)
2. **Scan the lecturer's QR code** with your phone
3. **Copy the session code** from the QR code
4. **Enter the session code** in the text field
5. **Click "Mark Attendance"**
6. You can only mark attendance once per session
7. View your attendance history in the table below

## Demo Accounts

Pre-created accounts in the database:

| Username   | Password | Role     |
|------------|----------|----------|
| lecturer1  | pass123  | Lecturer |
| student1   | pass123  | Student  |
| student2   | pass123  | Student  |

## Project Structure

```
java-attendance-app/
├── pom.xml                                 # Maven configuration
├── database/
│   └── schema.sql                          # Database schema and setup
├── src/
│   └── main/
│       ├── java/com/irvr/attendance/
│       │   ├── Main.java                   # Application entry point
│       │   ├── model/
│       │   │   ├── User.java               # User model
│       │   │   ├── Session.java            # Session model
│       │   │   └── Attendance.java         # Attendance model
│       │   ├── dao/
│       │   │   ├── UserDAO.java            # User database operations
│       │   │   ├── SessionDAO.java         # Session database operations
│       │   │   └── AttendanceDAO.java      # Attendance database operations
│       │   ├── service/
│       │   │   └── SessionService.java     # Business logic for sessions
│       │   ├── view/
│       │   │   ├── LoginView.java          # Login screen with WebView
│       │   │   ├── RegisterView.java       # Registration screen with WebView
│       │   │   ├── LecturerView.java       # Lecturer dashboard with WebView
│       │   │   └── StudentView.java        # Student dashboard with WebView
│       │   ├── database/
│       │   │   └── DatabaseConnection.java # MySQL connection manager
│       │   └── util/
│       │       └── QRCodeGenerator.java    # QR code generation utility
│       └── resources/
│           ├── database.properties         # Database configuration
│           └── html/                       # HTML UI files (easy to understand!)
│               ├── login.html              # Login screen (HTML/CSS/JS)
│               ├── register.html           # Registration screen (HTML/CSS/JS)
│               ├── lecturer.html           # Lecturer dashboard (HTML/CSS/JS)
│               └── student.html            # Student dashboard (HTML/CSS/JS)
└── README.md                               # This file
```

## UI Architecture (HTML-Based)

This application uses **HTML pages** for the user interface instead of FXML, making it much easier to understand:

- **HTML Files** (`src/main/resources/html/`): Contains the UI layout and embedded CSS styling
- **JavaScript**: Embedded in HTML files, handles user interactions
- **JavaFX WebView**: Renders the HTML pages inside the Java desktop application
- **JavaScript Bridge**: Connects JavaScript in HTML to Java methods

**Example of how it works:**
1. HTML button calls JavaScript function: `onclick="startSession()"`
2. JavaScript calls Java method: `window.javaApp.startSession(courseName)`
3. Java code executes and returns result
4. JavaScript updates the HTML: `document.getElementById('status').textContent = 'Success!'`

This makes the code very easy to read and modify - if you know HTML/CSS/JS, you can customize the UI!

## Dependencies

The project uses these main dependencies (auto-downloaded by Maven):

- **JavaFX 17.0.2** - Desktop UI framework
- **MySQL Connector 8.0.33** - MySQL database driver
- **ZXing 3.5.1** - QR code generation library

## Troubleshooting

### Issue: "Cannot connect to database"
**Solution:**
- Ensure XAMPP MySQL is running in XAMPP Control Panel
- Check if database `attendance_db` exists in phpMyAdmin
- Run the `database/schema.sql` script if database doesn't exist
- Verify `database.properties` has correct username/password

### Issue: "Maven dependencies not downloading"
**Solution:**
- Right-click `pom.xml` → Maven → Reload Project
- File → Invalidate Caches → Invalidate and Restart
- Check internet connection

### Issue: "JavaFX runtime components are missing"
**Solution:**
- In IntelliJ: Run → Edit Configurations
- Add VM options: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml`
- Or use Maven: `mvn clean javafx:run`

### Issue: "Class not found: Main"
**Solution:**
- Build the project: Build → Build Project
- Rebuild if needed: Build → Rebuild Project
- Ensure JDK 17 is configured: File → Project Structure → Project SDK

### Issue: "Port 3306 already in use"
**Solution:**
- Another MySQL instance is running
- Stop other MySQL services
- Or change port in XAMPP config and `database.properties`

## IRVR Features Explained

### Auto-Refreshing Codes
- Each IRVR code is valid for **5 minutes**
- Codes **automatically regenerate** when expired (no manual restart needed)
- The lecturer's screen updates the new QR code automatically
- Session remains active until manually stopped

### Fraud Prevention
- **One attendance per session**: Database constraint prevents duplicate marking
- **Time validation**: Codes expire after 5 minutes
- **Session-specific**: Each class has a unique session ID
- **Auto-refresh**: Prevents sharing codes outside class time

### Session Management
- Lecturers control when sessions start/stop
- All attendance during a session links to the same session ID
- Students cannot mark attendance for expired or inactive sessions

## Security Notes

⚠️ **This is a demonstration/learning project**

Current limitations:
- Passwords are stored in **plain text** (no hashing)
- No authentication tokens or session security
- Direct database access without sanitization layers

**For production use, implement:**
- Password hashing (BCrypt)
- Prepared statements (already implemented)
- User session tokens
- Input validation and sanitization
- Role-based access control
- HTTPS/SSL for network security

## Support

For issues or questions:
1. Check the Troubleshooting section above
2. Verify XAMPP MySQL is running
3. Check IntelliJ Maven console for errors
4. Ensure JDK 17 is properly configured

## License

This project is for educational purposes. Feel free to modify and use as needed.

---

**Built with JavaFX and MySQL for desktop attendance tracking**
