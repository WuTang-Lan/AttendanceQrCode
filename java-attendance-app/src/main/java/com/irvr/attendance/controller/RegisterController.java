package com.irvr.attendance.controller;

import com.irvr.attendance.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label statusLabel;
    @FXML private Button registerButton;
    @FXML private Hyperlink loginLink;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("student", "lecturer");
        roleComboBox.setValue("student");
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all fields", true);
            return;
        }

        if (username.length() < 3) {
            showStatus("Username must be at least 3 characters", true);
            return;
        }

        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showStatus("Passwords do not match", true);
            return;
        }

        if (userDAO.userExists(username)) {
            showStatus("Username already exists", true);
            return;
        }

        boolean success = userDAO.register(username, password, role);
        if (success) {
            showStatus("Registration successful! Redirecting to login...", false);
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::goToLogin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showStatus("Registration failed. Please try again.", true);
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            
            Stage stage = (Stage) loginLink.getScene().getWindow();
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
