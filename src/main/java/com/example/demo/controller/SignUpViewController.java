package com.example.demo.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpViewController {
	@FXML
	private PasswordField txtpwd1, txtpwd2;	
	@FXML
	private TextField txtpwd_a1, txtpwd_a2;	
	
	private Boolean isPasswordVisible1 = false;
	private Boolean isPasswordVisible2 = false;
	
	@FXML
	private void handleSignUp(){
		System.out.println("Đăng ký!!!");
	}
	
	@FXML
	private void handleLogIn() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogInView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) txtpwd1.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Trang Đăng Nhập");
        stage.setResizable(false);
        stage.show();
	}
	
	@FXML
	private void handleTogglePassword1() {
	    if (isPasswordVisible1) {
	    	txtpwd1.setText(txtpwd_a1.getText());
	    	txtpwd1.setVisible(true);
	    	txtpwd1.setManaged(true);
	    	txtpwd_a1.setVisible(false);
	    	txtpwd_a1.setManaged(false);
	    	isPasswordVisible1 = false;
	    } else {
	    	txtpwd_a1.setText(txtpwd1.getText());
	    	txtpwd_a1.setVisible(true);
	    	txtpwd_a1.setManaged(true);
	    	txtpwd1.setVisible(false);
	    	txtpwd1.setManaged(false);
	    	isPasswordVisible1 = false;
	    	isPasswordVisible1 = true;
	    }
	}
	
	@FXML
	private void handleTogglePassword2() {
	    if (isPasswordVisible2) {
	    	txtpwd2.setText(txtpwd_a2.getText());
	    	txtpwd2.setVisible(true);
	    	txtpwd2.setManaged(true);
	    	txtpwd_a2.setVisible(false);
	    	txtpwd_a2.setManaged(false);
	    	isPasswordVisible2 = false;
	    } else {
	    	txtpwd_a2.setText(txtpwd2.getText());
	    	txtpwd_a2.setVisible(true);
	    	txtpwd_a2.setManaged(true);
	    	txtpwd2.setVisible(false);
	    	txtpwd2.setManaged(false);
	    	isPasswordVisible2 = false;
	    	isPasswordVisible2 = true;
	    }
	}
}
