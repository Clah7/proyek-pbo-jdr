package com.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.utils.Koneksi;

public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnSignin;
    @FXML
    private Button btnExit;
    Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public void handleLogin(MouseEvent event) {
        if (event.getSource() == btnSignin) {
            int userKode = logIn();

            if (userKode > 0) {
                redirectToPage(userKode, event);
            }
        }
    }

    @FXML
    public void handleExit(ActionEvent e){
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }

    public LoginController() {
        con = Koneksi.conDB();
    }

    private int logIn() {
        int userKode = 0;
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
        } else {
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    userKode = resultSet.getInt("kode");
                } else {
                    setLblError(Color.TOMATO, "Enter Correct Username/Password");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                setLblError(Color.TOMATO, "Exception");
            }
        }

        return userKode;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

    private void redirectToPage(int userKode, MouseEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            String resourcePath = "/com/perpustakaan/fxml/";

            switch (userKode) {
                case 1:
                    resourcePath += "User.fxml";
                    break;
                case 2:
                    resourcePath += "Admin.fxml";
                    break;
                default:
                    setLblError(Color.TOMATO, "Invalid User Level");
                    return;
            }

            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resourcePath)));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
