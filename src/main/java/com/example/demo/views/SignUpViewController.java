package com.example.demo.views;

import com.example.demo.config.SpringContext;
import com.example.demo.model.dto.UserRequestDTO;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class SignUpViewController {

    @FXML
    private TextField txtEmail, txtPhone, txtpwd_a1, txtpwd_a2, txtOtpCode;
    @FXML
    private TextField txtFullName, txtUsername;

    @FXML
    private PasswordField txtpwd1, txtpwd2;

    @FXML
    private Button btnSendCode, btnSignUp, btnLogIn, btnToggle1, btnToggle2;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    private Boolean isPasswordVisible1 = false;
    private Boolean isPasswordVisible2 = false;

    @FXML
    public void handleSendCode(ActionEvent event) {
        String email = txtEmail.getText();
        if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Lỗi", "Email không hợp lệ", Alert.AlertType.ERROR);
            return;
        }

        if (otpService == null) {
            showAlert("Lỗi", "OtpService không được khởi tạo", Alert.AlertType.ERROR);
            return;
        }
        if (emailService == null) {
            showAlert("Lỗi", "EmailService không được khởi tạo", Alert.AlertType.ERROR);
            return;
        }

        String otp = otpService.generateOtp(email);

        try {
            emailService.sendOtpEmail(email, otp);
            showAlert("Thành công", "Đã gửi mã OTP đến: " + email, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Lỗi", "Gửi OTP thất bại: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleSignUp() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String otpInput = txtOtpCode.getText().trim();
        String phone = txtPhone.getText().trim();

        String password = isPasswordVisible1 ? txtpwd_a1.getText() : txtpwd1.getText();
        String confirmPassword = isPasswordVisible2 ? txtpwd_a2.getText() : txtpwd2.getText();

        if (fullName.isEmpty() || username.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập họ tên và tên đăng nhập", Alert.AlertType.ERROR);
            return;
        }

        if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Lỗi", "Email không hợp lệ", Alert.AlertType.ERROR);
            return;
        }

        if (!otpService.validateOtp(email, otpInput)) {
            showAlert("Lỗi", "Mã OTP không đúng hoặc đã hết hạn", Alert.AlertType.ERROR);
            return;
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập mật khẩu và xác nhận mật khẩu", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Lỗi", "Mật khẩu không khớp", Alert.AlertType.ERROR);
            return;
        }

        if (password.length() < 6) {
            showAlert("Lỗi", "Mật khẩu phải có ít nhất 6 ký tự", Alert.AlertType.ERROR);
            return;
        }

        if (!phone.matches("\\d{10}")) {
            showAlert("Lỗi", "Số điện thoại phải gồm 10 chữ số", Alert.AlertType.ERROR);
            return;
        }

        if (userService == null) {
            showAlert("Lỗi", "UserService không được khởi tạo", Alert.AlertType.ERROR);
            return;
        }

        try {
            UserRequestDTO requestDTO = new UserRequestDTO();
            requestDTO.setFullName(fullName);
            requestDTO.setUserName(username);
            requestDTO.setEmail(email);
            requestDTO.setOtp(otpInput);
            requestDTO.setPhone(phone);
            requestDTO.setPassword(password);

            userService.register(requestDTO);

            showAlert("Thành công", "Đăng ký thành công cho " + fullName, Alert.AlertType.INFORMATION);
            handleLogin();
        } catch (Exception e) {
            showAlert("Lỗi", "Đăng ký thất bại: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogInView.fxml"));
            loader.setControllerFactory(SpringContext.getContext()::getBean); // Spring inject controller

            Parent root = loader.load();

            // Lấy stage hiện tại từ một control bất kỳ, ví dụ: btnSignUp
            Stage stage = (Stage) btnSignUp.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể chuyển sang trang đăng nhập: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void showLogIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogInView.fxml"));
            loader.setControllerFactory(SpringContext.getContext()::getBean);
            Stage stage = (Stage) btnLogIn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Trang Đăng Nhập");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể chuyển sang trang đăng nhập: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleTogglePassword1() {
        if (txtpwd1 == null || txtpwd_a1 == null) {
            showAlert("Lỗi", "Một trong các trường mật khẩu không được tải", Alert.AlertType.ERROR);
            return;
        }
        if (isPasswordVisible1) {
            txtpwd1.setText(txtpwd_a1.getText());
            txtpwd1.setVisible(true);
            txtpwd1.setManaged(true);
            txtpwd_a1.setVisible(false);
            txtpwd_a1.setManaged(false);
        } else {
            txtpwd_a1.setText(txtpwd1.getText());
            txtpwd_a1.setVisible(true);
            txtpwd_a1.setManaged(true);
            txtpwd1.setVisible(false);
            txtpwd1.setManaged(false);
        }
        isPasswordVisible1 = !isPasswordVisible1;
    }

    @FXML
    public void handleTogglePassword2() {
        if (txtpwd2 == null || txtpwd_a2 == null) {
            showAlert("Lỗi", "Một trong các trường mật khẩu không được tải", Alert.AlertType.ERROR);
            return;
        }
        if (isPasswordVisible2) {
            txtpwd2.setText(txtpwd_a2.getText());
            txtpwd2.setVisible(true);
            txtpwd2.setManaged(true);
            txtpwd_a2.setVisible(false);
            txtpwd_a2.setManaged(false);
        } else {
            txtpwd_a2.setText(txtpwd2.getText());
            txtpwd_a2.setVisible(true);
            txtpwd_a2.setManaged(true);
            txtpwd2.setVisible(false);
            txtpwd2.setManaged(false);
        }
        isPasswordVisible2 = !isPasswordVisible2;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}