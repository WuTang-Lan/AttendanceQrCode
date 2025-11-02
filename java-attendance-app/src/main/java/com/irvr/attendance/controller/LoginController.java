package com.irvr.attendance.controller;

import com.irvr.attendance.dao.UserDAO;
import com.irvr.attendance.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter username and password", true);
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            showStatus("Login successful!", false);
            openDashboard(user);
        } else {
            showStatus("Invalid credentials", true);
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDashboard(User user) {
        try {
            String fxmlFile = user.getRole().equals("lecturer") ? 
                "/fxml/lecturer.fxml" : "/fxml/student.fxml";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), 800, 600);
            
            if (user.getRole().equals("lecturer")) {
                LecturerController controller = loader.getController();
                controller.setLecturer(user);
            } else {
                StudentController controller = loader.getController();
                controller.setStudent(user);
            }
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}
