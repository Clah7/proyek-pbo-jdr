package com.controllers.admin;

import com.utils.Koneksi;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertBukuController {
    @FXML
    private TextField txtNamaBuku;
    @FXML
    private TextField txtPenulis;
    @FXML
    private TextField txtPenerbit;
    @FXML
    private TextField txtTahunTerbit;
    @FXML
    private Label lblStatus;
    private PreparedStatement preparedStatement;
    private Connection connection;

    public InsertBukuController() {
        connection = Koneksi.conDB();
    }

    @FXML
    private void handleInsert() {
        if (txtPenerbit.getText().isEmpty() || txtNamaBuku.getText().isEmpty() || txtPenulis.getText().isEmpty() || txtTahunTerbit.getText().isEmpty()) {
            lblStatus.setTextFill(javafx.scene.paint.Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            addBuku();
        }
    }

    private String addBuku() {
        try {
            String st = "INSERT INTO book (nama_buku, penulis, penerbit, tahun_terbit) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(st);
            preparedStatement.setString(1, txtNamaBuku.getText());
            preparedStatement.setString(2, txtPenulis.getText());
            preparedStatement.setString(3, txtPenerbit.getText());
            preparedStatement.setString(4, txtTahunTerbit.getText());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }


    private void clearFields() {
        txtNamaBuku.clear();
        txtPenulis.clear();
        txtPenerbit.clear();
        txtTahunTerbit.clear();
    }
}
