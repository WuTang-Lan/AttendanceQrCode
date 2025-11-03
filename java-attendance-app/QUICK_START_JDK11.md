# ⚡ Quick Start Guide - JDK 11 Edition

## ✅ What's Been Updated

This project now uses **JDK 11** instead of JDK 17, making it compatible with more systems and environments.

### Key Changes:
- ✅ **Maven compiler** set to Java 11
- ✅ **JavaFX 11.0.2** (compatible with JDK 11)
- ✅ **All dependencies** verified for JDK 11 compatibility
- ✅ **Documentation** updated to reflect JDK 11 requirements

## 🚀 Get Started in 3 Steps

### Step 1: Install JDK 11
Download and install JDK 11:
- **Oracle JDK 11**: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
- **OpenJDK 11** (Free): https://adoptium.net/temurin/releases/?version=11

Verify installation:
```bash
java -version
# Should show: java version "11.0.x"
```

### Step 2: Download This Project
1. Right-click the **`java-attendance-app`** folder in Replit
2. Click **"Download"** or **"Download as ZIP"**
3. Extract to your computer

### Step 3: Open in IntelliJ IDEA
1. Open IntelliJ IDEA
2. **File → Open** → Select `java-attendance-app` folder
3. Wait for Maven to download dependencies (progress bar bottom-right)
4. Navigate to `src/main/java/com/irvr/attendance/Main.java`
5. Right-click → **Run 'Main.main()'**

**That's it!** 🎉

## 📦 What's Included

```
java-attendance-app/
├── pom.xml                      # JDK 11 Maven configuration ✅
├── README.md                    # Full documentation
├── SETUP_GUIDE.txt              # Detailed setup steps
├── JDK_11_COMPATIBILITY.md      # JDK 11 specific guide
├── HTML_UI_GUIDE.md             # How the HTML UI works
├── database/schema.sql          # MySQL database setup
└── src/main/
    ├── java/                    # 14 Java source files
    │   ├── Main.java
    │   ├── model/               # User, Session, Attendance
    │   ├── dao/                 # Database access
    │   ├── service/             # Business logic
    │   ├── view/                # HTML-based UI views
    │   ├── database/            # MySQL connection
    │   └── util/                # QR code generator
    └── resources/
        ├── database.properties  # MySQL config
        └── html/                # 4 HTML UI pages
            ├── login.html       # Easy-to-read HTML!
            ├── register.html
            ├── lecturer.html
            └── student.html
```

## 🎯 Key Features

✅ **HTML-Based UI** - Easy to understand (no FXML!)
✅ **Auto-Refreshing IRVR Codes** - 5-minute expiry with auto-regeneration
✅ **Session Management** - Start/stop sessions with unique IDs
✅ **MySQL Database** - Works with XAMPP
✅ **QR Code Generation** - Display codes for students to scan
✅ **Fraud Prevention** - Time validation, one mark per session

## 💡 Why HTML Instead of FXML?

This app uses **HTML, CSS, and JavaScript** for the UI instead of JavaFX FXML:

### Easier to Understand:
```html
<!-- login.html - You can read this! -->
<button onclick="handleLogin()">Login</button>

<script>
function handleLogin() {
    var username = document.getElementById('username').value;
    window.javaApp.login(username, password);  // Calls Java
}
</script>
```

### Easier to Customize:
```css
/* Change button color */
button {
    background: #667eea;  /* Change this! */
    color: white;
}
```

### Clear Separation:
- **HTML/CSS/JS** = User Interface
- **Java** = Business Logic & Database

See **HTML_UI_GUIDE.md** for complete details!

## 🗄️ Database Setup

### 1. Install XAMPP
Download from: https://www.apachefriends.org/

### 2. Start MySQL
Open XAMPP Control Panel → Click **Start** next to MySQL

### 3. Create Database
1. Open browser: http://localhost/phpmyadmin
2. Click **SQL** tab
3. Copy/paste entire `database/schema.sql` file
4. Click **Go**

### 4. Verify
You should see `attendance_db` database with 3 tables:
- `users` (pre-loaded with demo accounts)
- `sessions`
- `attendance`

## 🧪 Test with Demo Accounts

| Username  | Password | Role     |
|-----------|----------|----------|
| lecturer1 | pass123  | Lecturer |
| student1  | pass123  | Student  |
| student2  | pass123  | Student  |

## 🛠️ Maven Commands

### Run application:
```bash
mvn clean javafx:run
```

### Build JAR file:
```bash
mvn clean package
```

### Reload dependencies:
```bash
mvn clean install
```

## ❓ Troubleshooting

### "Release version 11 not supported"
→ **IntelliJ:** File → Project Structure → Set SDK to JDK 11

### "JavaFX runtime components missing"
→ Run with: `mvn clean javafx:run`

### "Cannot connect to database"
→ Make sure XAMPP MySQL is running

### "Dependencies not downloading"
→ Right-click `pom.xml` → Maven → Reload Project

## 📚 Documentation Files

- **README.md** - Complete documentation with troubleshooting
- **SETUP_GUIDE.txt** - Step-by-step setup (beginner-friendly)
- **JDK_11_COMPATIBILITY.md** - JDK 11 specific information
- **HTML_UI_GUIDE.md** - How the HTML UI works
- **DOWNLOAD_INSTRUCTIONS.md** - Multiple download methods
- **PROJECT_SUMMARY.md** - Project overview

## 🎓 Perfect for Learning

### Students Learn:
- Java backend development
- MySQL database integration
- HTML/CSS/JavaScript UI
- JavaFX WebView bridge
- Maven project structure
- IRVR attendance concepts

### Instructors Get:
- Easy-to-modify codebase
- Clear separation of concerns
- HTML UI (familiar to students)
- Complete documentation
- Working demo system

## 🔧 System Requirements

- **Java**: JDK 11 or higher
- **IDE**: IntelliJ IDEA (recommended)
- **Database**: XAMPP with MySQL
- **OS**: Windows, macOS, or Linux
- **RAM**: 4GB minimum, 8GB recommended

## 📝 Next Steps

1. ✅ Download the `java-attendance-app` folder
2. ✅ Install JDK 11 and IntelliJ IDEA
3. ✅ Setup XAMPP MySQL database
4. ✅ Open project in IntelliJ
5. ✅ Run and enjoy!

---

**Everything is configured and ready to run locally on JDK 11!** 🚀

For detailed instructions, see:
- **SETUP_GUIDE.txt** (quick start)
- **README.md** (full documentation)
- **JDK_11_COMPATIBILITY.md** (JDK 11 specifics)
