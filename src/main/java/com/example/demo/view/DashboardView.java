package com.example.demo.view;

import com.example.demo.RunApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class DashboardView extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        System.out.println("DashboardView: Initializing Spring Context...");
        applicationContext = new SpringApplicationBuilder(RunApplication.class).run();
        System.out.println("DashboardView: Spring Context initialized: " + applicationContext);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("DashboardView: Starting...");
        if (applicationContext == null) {
            throw new IllegalStateException("Spring ApplicationContext is not initialized!");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        loader.setControllerFactory(applicationContext::getBean);
        Scene scene = new Scene(loader.load(), 1250, 750);
        primaryStage.setTitle("Thống kê dự án bất động sản");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("DashboardView: Started successfully");
    }

    @Override
    public void stop() {
        System.out.println("DashboardView: Stopping...");
        if (applicationContext != null) {
            applicationContext.close();
        }
        System.out.println("DashboardView: Stopped successfully");
    }

    public static void main(String[] args) {
        launch(args);
    }
}