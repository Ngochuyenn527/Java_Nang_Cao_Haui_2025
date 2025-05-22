package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RunApplication extends Application {

    // ✅ Sử dụng ConfigurableApplicationContext thay vì ApplicationContext
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        // Khởi tạo Spring context
        springContext = SpringApplication.run(RunApplication.class);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogInView.fxml"));
        
        // ✅ Gán controller factory để Spring inject được @Autowired
        loader.setControllerFactory(springContext::getBean);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Đăng nhập");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // ✅ Đóng Spring context khi JavaFX dừng
        springContext.close();
    }

    public static void main(String[] args) {
        // Khởi động JavaFX app
        launch(args);
    }
}
