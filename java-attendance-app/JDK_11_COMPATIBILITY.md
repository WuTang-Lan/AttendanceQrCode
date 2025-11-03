# JDK 11 Compatibility Guide

## System Requirements

This application is configured to run on **Java JDK 11** or higher.

### Why JDK 11?

- **Long-Term Support (LTS)**: JDK 11 is an LTS release with extended support
- **Wide Compatibility**: Works on most systems and is widely available
- **Stable**: Well-tested and production-ready
- **JavaFX Compatible**: JavaFX 11 is fully compatible with JDK 11

## Download JDK 11

### Option 1: Oracle JDK 11
- Visit: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
- Download the installer for your operating system
- Run the installer

### Option 2: OpenJDK 11 (Free Alternative)
- Visit: https://adoptium.net/temurin/releases/?version=11
- Select your operating system
- Download and install

### Verify Installation

Open terminal/command prompt and run:
```bash
java -version
```

You should see output similar to:
```
java version "11.0.x"
Java(TM) SE Runtime Environment (build 11.0.x)
Java HotSpot(TM) 64-Bit Server VM (build 11.0.x)
```

## Maven Configuration

The `pom.xml` file is configured for JDK 11:

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <javafx.version>11.0.2</javafx.version>
</properties>
```

## JavaFX for JDK 11

This project uses **JavaFX 11.0.2**, which includes:
- `javafx-controls` - UI controls
- `javafx-web` - WebView for HTML rendering
- `javafx-swing` - Image conversion utilities
- `javafx-fxml` - FXML support (for compatibility)

Maven automatically downloads these dependencies when you build the project.

## IntelliJ IDEA Setup

### 1. Configure Project SDK

1. Open the project in IntelliJ IDEA
2. Go to **File → Project Structure** (or press `Ctrl+Alt+Shift+S`)
3. Under **Project Settings → Project**:
   - Set **SDK** to JDK 11
   - Set **Language Level** to "11 - Local variable syntax for lambda parameters"
4. Click **OK**

### 2. Configure Maven

IntelliJ automatically detects the Maven configuration from `pom.xml`. No additional setup needed!

### 3. Download Dependencies

1. Open the `pom.xml` file
2. Right-click anywhere in the file
3. Select **Maven → Reload Project**
4. Wait for dependencies to download (see progress in bottom-right corner)

## Running the Application

### Method 1: Run from IntelliJ (Easiest)
```
1. Navigate to: src/main/java/com/irvr/attendance/Main.java
2. Right-click → Run 'Main.main()'
3. Application window opens!
```

### Method 2: Maven Command
```bash
mvn clean javafx:run
```

### Method 3: Build JAR
```bash
mvn clean package
java -jar target/attendance-system-1.0.0.jar
```

## Compatibility Notes

### What Works:
✅ JavaFX WebView (HTML rendering)
✅ MySQL JDBC connection
✅ QR code generation
✅ All IRVR features
✅ Windows, macOS, Linux

### Known Limitations:
- **Modular JDK**: JavaFX 11 uses Java modules, Maven plugin handles this automatically
- **WebView**: Requires platform-specific native libraries (auto-downloaded by Maven)

## Troubleshooting

### Error: "Release version 11 not supported"
**Solution:** Update IntelliJ's compiler settings:
1. **File → Settings → Build, Execution, Deployment → Compiler → Java Compiler**
2. Set **Project bytecode version** to "11"

### Error: "JavaFX runtime components are missing"
**Solution:** Run with Maven:
```bash
mvn clean javafx:run
```

### Error: "package javafx.application does not exist"
**Solution:** 
1. Right-click `pom.xml` → **Maven → Reload Project**
2. **File → Invalidate Caches → Invalidate and Restart**

### Error: "Could not find or load main class"
**Solution:** Rebuild the project:
1. **Build → Rebuild Project**
2. Try running again

## Dependencies Version Table

| Dependency | Version | JDK 11 Compatible? |
|------------|---------|-------------------|
| JavaFX | 11.0.2 | ✅ Yes |
| MySQL Connector | 8.0.33 | ✅ Yes |
| ZXing Core | 3.5.1 | ✅ Yes |
| ZXing JavaSE | 3.5.1 | ✅ Yes |

## Upgrading to Newer JDK (Optional)

If you want to use JDK 17 or later:

1. Update `pom.xml`:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <javafx.version>17.0.2</javafx.version>
</properties>
```

2. Update compiler plugin:
```xml
<configuration>
    <source>17</source>
    <target>17</target>
</configuration>
```

3. Reload Maven project in IntelliJ

## Platform-Specific Notes

### Windows
- Requires Visual C++ Redistributable (usually pre-installed)
- JavaFX WebView uses IE11/Edge rendering engine

### macOS
- Requires macOS 10.10 or later
- JavaFX WebView uses WebKit rendering engine

### Linux
- Requires GTK 3 libraries
- Install with: `sudo apt-get install libgtk-3-0 libwebkit2gtk-4.0-37`

## Summary

✅ **JDK 11 is fully supported** for this application
✅ **Maven handles all JavaFX dependencies** automatically
✅ **Compatible with Windows, macOS, and Linux**
✅ **No additional configuration needed** beyond standard IntelliJ setup

For any issues, see the README.md troubleshooting section or SETUP_GUIDE.txt for step-by-step instructions.

---

**Tested with:**
- Oracle JDK 11.0.20
- OpenJDK 11.0.19
- IntelliJ IDEA 2023.x
- Maven 3.6+
