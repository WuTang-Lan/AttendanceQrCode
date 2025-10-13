package com.example.attendance;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentPage {
    public static void show(String username) {
        Stage stage = new Stage();
        Label msg = new Label("👋 Welcome, " + username + "!\nYou are now logged in.");
        VBox layout = new VBox(20, msg);
        layout.setStyle("-fx-alignment:center; -fx-padding:20;");
        stage.setScene(new Scene(layout, 300, 200));
        stage.setTitle("Student Dashboard");
        stage.show();
    }
}
