package com.irvr.attendance.controller;

import com.irvr.attendance.dao.AttendanceDAO;
import com.irvr.attendance.model.Attendance;
import com.irvr.attendance.model.Session;
import com.irvr.attendance.model.User;
import com.irvr.attendance.service.SessionService;
import com.irvr.attendance.util.QRCodeGenerator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LecturerController {
    @FXML private Label welcomeLabel;
    @FXML private Label sessionStatusLabel;
    @FXML private Label timerLabel;
    @FXML private TextField courseNameField;
    @FXML private Button startSessionButton;
    @FXML private Button stopSessionButton;
    @FXML private ImageView qrCodeImageView;
    @FXML private TableView<Attendance> attendanceTable;
    @FXML private TableColumn<Attendance, String> studentColumn;
    @FXML private TableColumn<Attendance, String> courseColumn;
    @FXML private TableColumn<Attendance, String> timeColumn;
    @FXML private Button logoutButton;

    private User lecturer;
    private final SessionService sessionService = new SessionService();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private Timer refreshTimer;
    private Session currentSession;

    public void setLecturer(User lecturer) {
        this.lecturer = lecturer;
        welcomeLabel.setText("Welcome, " + lecturer.getUsername() + " (Lecturer)");
        checkActiveSession();
        setupTable();
    }

    private void setupTable() {
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        timeColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTimeMarked().format(formatter)
            );
        });
    }

    @FXML
    private void handleStartSession() {
        String courseName = courseNameField.getText().trim();
        if (courseName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a course name");
            alert.showAndWait();
            return;
        }

        currentSession = sessionService.startNewSession(lecturer.getUsername(), courseName);
        if (currentSession != null) {
            sessionStatusLabel.setText("Active Session: " + courseName);
            sessionStatusLabel.setStyle("-fx-text-fill: green;");
            startSessionButton.setVisible(false);
            stopSessionButton.setVisible(true);
            qrCodeImageView.setVisible(true);
            
            updateQRCode();
            startAutoRefresh();
            loadAttendance();
        }
    }

    @FXML
    private void handleStopSession() {
        sessionService.stopSession(lecturer.getUsername());
        currentSession = null;
        stopAutoRefresh();
        
        sessionStatusLabel.setText("No active session");
        sessionStatusLabel.setStyle("-fx-text-fill: gray;");
        startSessionButton.setVisible(true);
        stopSessionButton.setVisible(false);
        qrCodeImageView.setVisible(false);
        timerLabel.setText("");
    }

    private void checkActiveSession() {
        currentSession = sessionService.getActiveSession(lecturer.getUsername());
        if (currentSession != null) {
            sessionStatusLabel.setText("Active Session: " + currentSession.getCourseName());
            sessionStatusLabel.setStyle("-fx-text-fill: green;");
            startSessionButton.setVisible(false);
            stopSessionButton.setVisible(true);
            qrCodeImageView.setVisible(true);
            
            updateQRCode();
            startAutoRefresh();
            loadAttendance();
        } else {
            startSessionButton.setVisible(true);
            stopSessionButton.setVisible(false);
            qrCodeImageView.setVisible(false);
        }
    }

    private void updateQRCode() {
        if (currentSession != null) {
            String qrData = currentSession.getSessionCode();
            qrCodeImageView.setImage(QRCodeGenerator.generateQRCodeImage(qrData));
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
                        handleStopSession();
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
                long minutes = seconds / 60;
                long secs = seconds % 60;
                
                String color = minutes < 1 ? "red" : "green";
                timerLabel.setText(String.format("Code expires in: %d:%02d", minutes, secs));
                timerLabel.setStyle("-fx-text-fill: " + color + ";");
            } else {
                timerLabel.setText("Code refreshing...");
                timerLabel.setStyle("-fx-text-fill: orange;");
            }
        }
    }

    private void loadAttendance() {
        List<Attendance> records = attendanceDAO.getAttendanceByLecturer(lecturer.getUsername());
        ObservableList<Attendance> data = FXCollections.observableArrayList(records);
        attendanceTable.setItems(data);
    }

    @FXML
    private void handleLogout() {
        stopAutoRefresh();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
