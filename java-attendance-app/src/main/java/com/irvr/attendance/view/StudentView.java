package com.irvr.attendance.view;

import com.irvr.attendance.dao.AttendanceDAO;
import com.irvr.attendance.model.Attendance;
import com.irvr.attendance.model.Session;
import com.irvr.attendance.model.User;
import com.irvr.attendance.service.SessionService;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentView {
    private WebView webView;
    private WebEngine webEngine;
    private Stage stage;
    private User student;
    private final SessionService sessionService;
    private final AttendanceDAO attendanceDAO;

    public StudentView(Stage stage, User student) {
        this.stage = stage;
        this.student = student;
        this.sessionService = new SessionService();
        this.attendanceDAO = new AttendanceDAO();
        
        webView = new WebView();
        webEngine = webView.getEngine();
        
        String htmlPath = getClass().getResource("/html/student.html").toExternalForm();
        webEngine.load(htmlPath);
        
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", new JavaScriptBridge());
                
                webEngine.executeScript("setWelcomeText('" + student.getUsername() + "')");
                
                loadAttendanceHistory();
            }
        });
    }

    public WebView getView() {
        return webView;
    }

    private void loadAttendanceHistory() {
        List<Attendance> records = attendanceDAO.getAttendanceByStudent(student.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        StringBuilder jsonArray = new StringBuilder("[");
        for (int i = 0; i < records.size(); i++) {
            Attendance att = records.get(i);
            if (i > 0) jsonArray.append(",");
            jsonArray.append(String.format(
                "{courseName: '%s', timeMarked: '%s'}",
                att.getCourseName().replace("'", "\\'"),
                att.getTimeMarked().format(formatter)
            ));
        }
        jsonArray.append("]");
        
        webEngine.executeScript("updateAttendanceTable(" + jsonArray + ")");
    }

    public class JavaScriptBridge {
        public void markAttendance(String sessionCode) {
            Session session = sessionService.validateSessionCode(sessionCode);
            if (session == null) {
                webEngine.executeScript("showStatus('❌ Invalid or expired session code', 'error')");
                return;
            }

            boolean success = attendanceDAO.markAttendance(student.getUsername(), session.getId());
            if (success) {
                webEngine.executeScript("showStatus('✅ Attendance marked successfully!', 'success')");
                webEngine.executeScript("clearSessionCode()");
                loadAttendanceHistory();
            } else {
                webEngine.executeScript("showStatus('❌ Attendance already marked for this session', 'error')");
            }
        }

        public void refreshAttendance() {
            loadAttendanceHistory();
        }

        public void logout() {
            Platform.runLater(() -> {
                LoginView loginView = new LoginView(stage);
                Scene scene = new Scene(loginView.getView(), 600, 500);
                stage.setScene(scene);
            });
        }
    }
}
