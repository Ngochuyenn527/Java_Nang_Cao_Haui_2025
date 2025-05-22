package com.example.demo.views;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.util.Duration;

import com.example.demo.config.SpringContext;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.service.UserService;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class LogInViewController {
    @FXML
    private TextField txtLoginName;
    @FXML
    private PasswordField txtpwd;
    @FXML
    private TextField txtpwd_a;
    @FXML
    private Button btnLogIn;
    @FXML
    private Button btnToggle;
    
    @Autowired
    private UserService userService;

    private boolean isPasswordVisible = false;

    @FXML
    private void handleTogglePassword() {
        if (isPasswordVisible) {
            txtpwd.setText(txtpwd_a.getText());
            txtpwd.setVisible(true);
            txtpwd.setManaged(true);
            txtpwd_a.setVisible(false);
            txtpwd_a.setManaged(false);
            isPasswordVisible = false;
        } else {
            txtpwd_a.setText(txtpwd.getText());
            txtpwd_a.setVisible(true);
            txtpwd_a.setManaged(true);
            txtpwd.setVisible(false);
            txtpwd.setManaged(false);
            isPasswordVisible = true;
        }
    }

    @FXML
    private void handleLogIn() {
        String username = txtLoginName.getText().trim();
        String password = isPasswordVisible ? txtpwd_a.getText() : txtpwd.getText();

        // Kiểm tra đầu vào
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập tên đăng nhập và mật khẩu.");
            return;
        }

        try {
            UserDTO user = userService.login(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng nhập thành công!");
            switchToMainView();
        } catch (RuntimeException e) {
            // Xử lý các ngoại lệ từ phương thức login
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Tên đăng nhập không tồn tại hoặc bị khóa")) {
                showAlert(Alert.AlertType.ERROR, "Đăng nhập thất bại", "Tên đăng nhập không tồn tại hoặc tài khoản bị khóa.");
            } else if (errorMessage.contains("Mật khẩu không đúng")) {
                showAlert(Alert.AlertType.ERROR, "Đăng nhập thất bại", "Mật khẩu không đúng.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Đăng nhập thất bại: " + errorMessage);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kết nối đến server: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ManagerView.fxml"));
            loader.setControllerFactory(SpringContext.getContext()::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) txtLoginName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang chính");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang chính.");
        }
    }

    @FXML
    private void showSignUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SignUpView.fxml"));
        loader.setControllerFactory(SpringContext.getContext()::getBean);
        Parent root = loader.load();

        Stage stage = (Stage) txtLoginName.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Trang Đăng Ký");
        stage.setResizable(false);
        stage.show();
    }
}
