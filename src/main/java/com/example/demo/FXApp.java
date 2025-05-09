package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FXApp extends Application {

    private static FXApp instance;
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        mainStage = primaryStage;
        
        // Xử lý sự kiện đóng cửa sổ
        mainStage.setOnCloseRequest(event -> {
            event.consume();  // Ngăn không cho cửa sổ thực sự đóng
            mainStage.hide(); // Ẩn cửa sổ thay vì đóng
        });

        showWindow();
    }

    public static void showWindow() {
        Platform.runLater(() -> {
            try {
                if (mainStage == null) {
                    mainStage = new Stage();
                }
                FXMLLoader loader = new FXMLLoader(FXApp.class.getResource("/FXML/BuildingManagerView.fxml"));
                Parent root = loader.load();
                mainStage.setTitle("Trang Quản Trị - Tìm Kiếm Tòa Nhà");
                mainStage.setScene(new Scene(root));
                mainStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
