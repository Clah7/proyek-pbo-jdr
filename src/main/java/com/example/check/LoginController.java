package com.example.check;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Button exitButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    public void loginButtonOnAction(ActionEvent e){
        if(!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()){
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }
    public void exitButtonOnAction(ActionEvent e){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        DatabaseConnection connect = new DatabaseConnection();
        Connection connectDb = connect.getConnection();

        String verifyLogin = "SELECT count(1) FROM admin WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connectDb.prepareStatement(verifyLogin);
            preparedStatement.setString(1, usernameTextField.getText());
            preparedStatement.setString(2, passwordPasswordField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText("Welcome!");
                } else {
                    loginMessageLabel.setText("Invalid login. Please try again");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}