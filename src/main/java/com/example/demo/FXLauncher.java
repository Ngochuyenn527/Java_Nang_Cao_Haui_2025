package com.example.demo;

public class FXLauncher {

    private static boolean started = false;

    public static void launchUI() {
        if (!started) {
            started = true;
            new Thread(() -> javafx.application.Application.launch(FXApp.class)).start();
        } else {
            FXApp.showWindow();
        }
    }
}
