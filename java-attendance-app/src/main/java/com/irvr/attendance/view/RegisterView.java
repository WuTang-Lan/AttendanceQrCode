package com.irvr.attendance.view;

import com.irvr.attendance.dao.UserDAO;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class RegisterView {
    private WebView webView;
    private WebEngine webEngine;
    private Stage stage;
    private final UserDAO userDAO;

    public RegisterView(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        
        webView = new WebView();
        webEngine = webView.getEngine();
        
        String htmlPath = getClass().getResource("/html/register.html").toExternalForm();
        webEngine.load(htmlPath);
        
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", new JavaScriptBridge());
            }
        });
    }

    public WebView getView() {
        return webView;
    }

    public class JavaScriptBridge {
        public void register(String username, String password, String role) {
            if (userDAO.userExists(username)) {
                webEngine.executeScript("showStatus('Username already exists', 'error')");
                return;
            }

            boolean success = userDAO.register(username, password, role);
            if (success) {
                webEngine.executeScript("showStatus('Registration successful! Redirecting to login...', 'success')");
                
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(() -> goToLogin());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                webEngine.executeScript("showStatus('Registration failed. Please try again.', 'error')");
            }
        }

        public void goToLogin() {
            LoginView loginView = new LoginView(stage);
            Scene scene = new Scene(loginView.getView(), 600, 500);
            stage.setScene(scene);
        }
    }
}
