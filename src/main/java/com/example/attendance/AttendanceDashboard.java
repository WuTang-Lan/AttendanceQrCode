 package com.example.attendance;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;

public class AttendanceDashboard extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SimpleHttpServer server = new SimpleHttpServer();
        server.start();

        BufferedImage qr = QRGenerator.generate("http://localhost:8000/scan");
        ImageView qrView = new ImageView(SwingFXUtils.toFXImage(qr, null));
        qrView.setFitWidth(200);
        qrView.setPreserveRatio(true);

        VBox layout = new VBox(15, qrView);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 300, 300));
        stage.setTitle("Lecturer Dashboard - QR Attendance");
        stage.show();
    }
}
