package com.example.attendance;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    @Override
    public void start(Stage stage) {
        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();
        Label lblPass = new Label("Password:");
        PasswordField txtPass = new PasswordField();
        Button btnLogin = new Button("Login");
        Label lblStatus = new Label();

        btnLogin.setOnAction(e -> {
            String username = txtUser.getText();
            String password = txtPass.getText();

            String role = DatabaseManager.validateUser(username, password);
            if (role != null) {
                lblStatus.setText("✅ Logged in as " + role);
                if (role.equals("lecturer")) {
                    try {
                        AttendanceDashboard dash = new AttendanceDashboard();
                        dash.start(new Stage());
                        stage.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    StudentPage.show(username);
                    stage.close();
                }
            } else {
                lblStatus.setText("❌ Invalid login. Try again.");
            }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(lblUser, 0, 0);
        grid.add(txtUser, 1, 0);
        grid.add(lblPass, 0, 1);
        grid.add(txtPass, 1, 1);
        grid.add(btnLogin, 1, 2); // ✅ Only added once now
        grid.add(lblStatus, 1, 3);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.setTitle("QR Attendance Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
