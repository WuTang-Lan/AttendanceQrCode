package com.irvr.attendance.view;

import com.irvr.attendance.dao.UserDAO;
import com.irvr.attendance.model.User;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class LoginView {
    private WebView webView;
    private WebEngine webEngine;
    private Stage stage;
    private final UserDAO userDAO;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        
        webView = new WebView();
        webEngine = webView.getEngine();
        
        String htmlPath = getClass().getResource("/html/login.html").toExternalForm();
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
        public void login(String username, String password) {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                webEngine.executeScript("showStatus('Login successful!', 'success')");
                openDashboard(user);
            } else {
                webEngine.executeScript("showStatus('Invalid credentials', 'error')");
            }
        }

        public void goToRegister() {
            RegisterView registerView = new RegisterView(stage);
            Scene scene = new Scene(registerView.getView(), 600, 500);
            stage.setScene(scene);
        }
    }

    private void openDashboard(User user) {
        javafx.application.Platform.runLater(() -> {
            if (user.getRole().equals("lecturer")) {
                LecturerView lecturerView = new LecturerView(stage, user);
                Scene scene = new Scene(lecturerView.getView(), 900, 700);
                stage.setScene(scene);
            } else {
                StudentView studentView = new StudentView(stage, user);
                Scene scene = new Scene(studentView.getView(), 900, 700);
                stage.setScene(scene);
            }
        });
    }
}
