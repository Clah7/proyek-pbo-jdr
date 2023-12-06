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


}
