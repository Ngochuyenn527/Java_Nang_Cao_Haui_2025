package com.example.demo;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunApplication {

    public static void main(String[] args) {
        // Khởi chạy Spring Boot backend
        SpringApplication.run(RunApplication.class, args);
        System.out.println("Spring Boot backend đã chạy!");

        // Mở giao diện JavaFX lần đầu
        FXLauncher.launchUI();

        // Giao diện console để mở lại FX UI
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Nhập 1 để mở lại giao diện, 0 để thoát:");
            int choice = scanner.nextInt();
            if (choice == 1) {
                FXLauncher.launchUI();
                System.out.println("Giao diện đã mở lại!");
            } else {
                break;
            }
        }
        scanner.close();
        System.exit(0);
    }
}
