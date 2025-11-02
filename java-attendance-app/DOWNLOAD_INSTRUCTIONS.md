# How to Download This Project to Your Local Machine

## Method 1: Download as ZIP (Easiest)

### From Replit Interface:
1. In the Replit file explorer (left sidebar), locate the **java-attendance-app** folder
2. Right-click on the **java-attendance-app** folder
3. Select **"Download"** or **"Download as ZIP"**
4. The folder will be downloaded to your Downloads folder
5. Extract the ZIP file to your desired location

### Alternative - Using Replit Shell:
1. Click the **Shell** tab in Replit
2. Run these commands to create a ZIP file:
   ```bash
   cd ~
   zip -r java-attendance-app.zip java-attendance-app/
   ```
3. The ZIP file will appear in the file explorer
4. Right-click `java-attendance-app.zip` → Download
5. Extract on your local machine

## Method 2: Download Individual Files

If you can't download the whole folder:

1. Manually create the folder structure on your computer:
   ```
   java-attendance-app/
   ├── database/
   ├── src/main/java/com/irvr/attendance/
   │   ├── model/
   │   ├── dao/
   │   ├── service/
   │   ├── controller/
   │   ├── database/
   │   └── util/
   └── src/main/resources/
       └── fxml/
   ```

2. Download each file individually:
   - Click on each file in Replit
   - Copy the content
   - Create the file in the correct location on your computer
   - Paste the content

3. Essential files to download:
   - `pom.xml`
   - `README.md`
   - `SETUP_GUIDE.txt`
   - All files in `database/`
   - All Java files in `src/main/java/`
   - All FXML files in `src/main/resources/`

## Method 3: Git Clone (If Available)

If this Replit has Git enabled:

```bash
git clone <your-replit-url>
cd java-attendance-app
```

## After Downloading

1. **Verify folder structure** - Make sure all files are in the correct locations
2. **Open in IntelliJ** - Follow the steps in SETUP_GUIDE.txt
3. **Setup XAMPP** - Install XAMPP and run the database schema
4. **Run the application** - Use IntelliJ to run Main.java

## File Checklist

Make sure you have these critical files:

- [ ] pom.xml (Maven configuration)
- [ ] README.md (Full documentation)
- [ ] SETUP_GUIDE.txt (Quick start guide)
- [ ] database/schema.sql (Database setup)
- [ ] src/main/resources/database.properties (DB config)
- [ ] src/main/java/com/irvr/attendance/Main.java (Entry point)
- [ ] All controller files (LoginController, LecturerController, etc.)
- [ ] All FXML files (login.fxml, lecturer.fxml, etc.)
- [ ] All model files (User, Session, Attendance)
- [ ] All DAO files (UserDAO, SessionDAO, AttendanceDAO)

## Next Steps

Once downloaded, see **SETUP_GUIDE.txt** for complete setup instructions!

---

**Need Help?**
- Check README.md for detailed documentation
- See SETUP_GUIDE.txt for step-by-step instructions
- Ensure Java JDK 17, IntelliJ IDEA, and XAMPP are installed
