package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
public class RunApplication extends Application{

    private ApplicationContext applicationContext;

    @Override
    public void init() {
        // Khởi tạo Spring context trong giai đoạn init của JavaFX
        applicationContext = SpringApplication.run(RunApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Tạo FXMLLoader và sử dụng Spring ApplicationContext làm controller factory
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ManagerView.fxml"));
        loader.setControllerFactory(applicationContext::getBean); // Spring tạo controller
        Parent root = loader.load();

        // Thiết lập scene và hiển thị
        Scene scene = new Scene(root);
        primaryStage.setTitle("Trang Quản Trị");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Khởi chạy Spring Boot backend
//        SpringApplication.run(RunApplication.class, args);

        // Mở giao diện JavaFX
        launch(args);
    }
}