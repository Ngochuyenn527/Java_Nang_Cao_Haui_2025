package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class UserManagerViewController {

    @FXML
    private Button btnAdd;

    @FXML
    public void handleAddUser() {
        try {
            // Tải FXML của cửa sổ quản lý tài khoản
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserAddView.fxml"));
            Parent root = loader.load();

            // Tạo một Stage mới cho cửa sổ Quản lý tài khoản
            Stage userManagerStage = new Stage();
            userManagerStage.setTitle("Thêm tài khoản");
            userManagerStage.setScene(new Scene(root));

            // Hiển thị cửa sổ
            userManagerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

