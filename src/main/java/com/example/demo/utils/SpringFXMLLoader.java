//package com.example.demo.utils;
//
//import javafx.fxml.FXMLLoader;
//import org.apache.catalina.core.ApplicationContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class SpringFXMLLoader {
//
//    @Autowired
//    private ApplicationContext context;
//
//    public Object load(String fxmlPath) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//        loader.setControllerFactory(context::getBean); // Lấy controller từ Spring
//        return loader.load();
//    }
//}
//
