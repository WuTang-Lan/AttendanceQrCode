package com.irvr.attendance.controller;

import com.irvr.attendance.dao.AttendanceDAO;
import com.irvr.attendance.model.Attendance;
import com.irvr.attendance.model.Session;
import com.irvr.attendance.model.User;
import com.irvr.attendance.service.SessionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentController {
    @FXML private Label welcomeLabel;
    @FXML private TextField sessionCodeField;
    @FXML private Button markAttendanceButton;
    @FXML private Label statusLabel;
    @FXML private TableView<Attendance> attendanceTable;
    @FXML private TableColumn<Attendance, String> courseColumn;
    @FXML private TableColumn<Attendance, String> timeColumn;
    @FXML private Button logoutButton;

    private User student;
    private final SessionService sessionService = new SessionService();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public void setStudent(User student) {
        this.student = student;
        welcomeLabel.setText("Welcome, " + student.getUsername() + " (Student)");
        setupTable();
        loadAttendanceHistory();
    }

    private void setupTable() {
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        timeColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTimeMarked().format(formatter)
            );
        });
    }

    @FXML
    private void handleMarkAttendance() {
        String sessionCode = sessionCodeField.getText().trim();
        
        if (sessionCode.isEmpty()) {
            showStatus("Please enter session code", true);
            return;
        }

        Session session = sessionService.validateSessionCode(sessionCode);
        if (session == null) {
            showStatus("❌ Invalid or expired session code", true);
            return;
        }

        boolean success = attendanceDAO.markAttendance(student.getUsername(), session.getId());
        if (success) {
            showStatus("✅ Attendance marked successfully!", false);
            sessionCodeField.clear();
            loadAttendanceHistory();
        } else {
            showStatus("❌ Attendance already marked for this session", true);
        }
    }

    private void loadAttendanceHistory() {
        List<Attendance> records = attendanceDAO.getAttendanceByStudent(student.getUsername());
        ObservableList<Attendance> data = FXCollections.observableArrayList(records);
        attendanceTable.setItems(data);
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: red; -fx-font-weight: bold;" : 
                                       "-fx-text-fill: green; -fx-font-weight: bold;");
    }
}
