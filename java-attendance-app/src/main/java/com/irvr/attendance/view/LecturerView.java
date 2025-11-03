package com.irvr.attendance.view;

import com.irvr.attendance.dao.AttendanceDAO;
import com.irvr.attendance.model.Attendance;
import com.irvr.attendance.model.Session;
import com.irvr.attendance.model.User;
import com.irvr.attendance.service.SessionService;
import com.irvr.attendance.util.QRCodeGenerator;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LecturerView {
    private WebView webView;
    private WebEngine webEngine;
    private Stage stage;
    private User lecturer;
    private final SessionService sessionService;
    private final AttendanceDAO attendanceDAO;
    private Timer refreshTimer;
    private Session currentSession;

    public LecturerView(Stage stage, User lecturer) {
        this.stage = stage;
        this.lecturer = lecturer;
        this.sessionService = new SessionService();
        this.attendanceDAO = new AttendanceDAO();
        
        webView = new WebView();
        webEngine = webView.getEngine();
        
        String htmlPath = getClass().getResource("/html/lecturer.html").toExternalForm();
        webEngine.load(htmlPath);
        
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", new JavaScriptBridge());
                
                webEngine.executeScript("setWelcomeText('" + lecturer.getUsername() + "')");
                
                checkActiveSession();
            }
        });
    }

    public WebView getView() {
        return webView;
    }

    private void checkActiveSession() {
        currentSession = sessionService.getActiveSession(lecturer.getUsername());
        if (currentSession != null) {
            updateUIForActiveSession();
            startAutoRefresh();
        }
    }

    private void updateUIForActiveSession() {
        String sessionData = String.format("{active: true, courseName: '%s'}", 
            currentSession.getCourseName().replace("'", "\\'"));
        webEngine.executeScript("updateUI(" + sessionData + ")");
        
        updateQRCode();
        loadAttendance();
    }

    private void updateQRCode() {
        if (currentSession != null) {
            Image qrImage = QRCodeGenerator.generateQRCodeImage(currentSession.getSessionCode());
            if (qrImage != null) {
                String base64Image = convertImageToBase64(qrImage);
                webEngine.executeScript("updateQRCode('" + base64Image + "')");
            }
        }
    }

    private String convertImageToBase64(Image image) {
        try {
            BufferedImage bImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void startAutoRefresh() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
        
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Session updatedSession = sessionService.getActiveSession(lecturer.getUsername());
                    if (updatedSession != null) {
                        if (!updatedSession.getSessionCode().equals(currentSession.getSessionCode())) {
                            currentSession = updatedSession;
                            updateQRCode();
                            System.out.println("IRVR code refreshed automatically");
                        }
                        updateTimer();
                        loadAttendance();
                    } else {
                        stopAutoRefresh();
                        String sessionData = "{active: false}";
                        webEngine.executeScript("updateUI(" + sessionData + ")");
                    }
                });
            }
        }, 0, 3000);
    }

    private void stopAutoRefresh() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null;
        }
    }

    private void updateTimer() {
        if (currentSession != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = currentSession.getExpiresAt();
            
            if (now.isBefore(expiresAt)) {
                Duration duration = Duration.between(now, expiresAt);
                long seconds = duration.getSeconds();
                webEngine.executeScript("updateTimer(" + seconds + ")");
            }
        }
    }

    private void loadAttendance() {
        List<Attendance> records = attendanceDAO.getAttendanceByLecturer(lecturer.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        StringBuilder jsonArray = new StringBuilder("[");
        for (int i = 0; i < records.size(); i++) {
            Attendance att = records.get(i);
            if (i > 0) jsonArray.append(",");
            jsonArray.append(String.format(
                "{studentName: '%s', courseName: '%s', timeMarked: '%s'}",
                att.getStudentName().replace("'", "\\'"),
                att.getCourseName().replace("'", "\\'"),
                att.getTimeMarked().format(formatter)
            ));
        }
        jsonArray.append("]");
        
        webEngine.executeScript("updateAttendanceTable(" + jsonArray + ")");
    }

    public class JavaScriptBridge {
        public void startSession(String courseName) {
            currentSession = sessionService.startNewSession(lecturer.getUsername(), courseName);
            if (currentSession != null) {
                updateUIForActiveSession();
                startAutoRefresh();
            }
        }

        public void stopSession() {
            sessionService.stopSession(lecturer.getUsername());
            currentSession = null;
            stopAutoRefresh();
            
            String sessionData = "{active: false}";
            webEngine.executeScript("updateUI(" + sessionData + ")");
        }

        public void refreshSession() {
            // Called by JavaScript auto-refresh timer
        }

        public void logout() {
            stopAutoRefresh();
            Platform.runLater(() -> {
                LoginView loginView = new LoginView(stage);
                Scene scene = new Scene(loginView.getView(), 600, 500);
                stage.setScene(scene);
            });
        }
    }
}
