package com.example.demo.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInViewController {
	@FXML
	private PasswordField txtpwd;	
	@FXML
	private TextField txtpwd_a;	
		
	private Boolean isPasswordVisible = false;
	
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
	    	isPasswordVisible = false;
	    	isPasswordVisible = true;
	    }
	}
	
	@FXML
	private void handleLogIn(){
		System.out.println("Đăng nhập!!!");
	}
	
	@FXML
	private void handleSignUp() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SignUpView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) txtpwd.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Trang Đăng Ký");
        stage.setResizable(false);
        stage.show();
	}
}
