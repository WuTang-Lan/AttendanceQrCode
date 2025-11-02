package com.irvr.attendance;

import com.irvr.attendance.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!DatabaseConnection.getInstance().testConnection()) {
            System.err.println("❌ Cannot connect to database. Please ensure:");
            System.err.println("1. XAMPP MySQL is running");
            System.err.println("2. Database 'attendance_db' exists");
            System.err.println("3. Run the schema.sql script in phpMyAdmin");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load(), 600, 500);
        
        primaryStage.setTitle("IRVR Attendance System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.getInstance().closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
