package com.controllers.admin;

import com.models.Buku;
import com.utils.Koneksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TransaksiController implements Initializable {
    @FXML
    private ComboBox<String> listUsername;
    @FXML
    private ComboBox<String> listBuku;
    @FXML
    Label lblStatus;
    @FXML
    private DatePicker tanggalPinjam;
    @FXML
    private DatePicker tanggalPengembalian;
    private PreparedStatement preparedStatement;
    private Connection connection;

    public TransaksiController() {
        connection = Koneksi.conDB();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateBukuComboBox();
        populateUserComboBox();
    }

    @FXML
    private void handleRefreshUser() {
        populateUserComboBox();
    }

    private void populateUserComboBox() {
        Connection connection = Koneksi.conDB();

        if (connection != null) {
            try {
                String sql = "SELECT username FROM user where kode = 1";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> dataUser = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    dataUser.add(resultSet.getString("username"));
                }

                listUsername.setItems(dataUser);

                preparedStatement.close();
                resultSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateBukuComboBox() {
        Connection connection = Koneksi.conDB();

        if (connection != null) {
            try {
                String sql = "SELECT nama_buku FROM buku";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> dataNamaBuku = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    dataNamaBuku.add(resultSet.getString("nama_buku"));
                }

                listBuku.setItems(dataNamaBuku);

                preparedStatement.close();
                resultSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        listUsername.setValue("");
        listBuku.setValue("");
        tanggalPinjam.getEditor().clear();
        tanggalPengembalian.getEditor().clear();
    }

    private String getBookId(String namaBuku) {
        String idBuku = null;
        try {
            String sql = "SELECT id_buku FROM buku WHERE nama_buku = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, namaBuku);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        idBuku = resultSet.getString("id_buku");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idBuku;
    }

    @FXML
    private void handleAdd() {
        if (listUsername.getValue().isEmpty() || listBuku.getValue().isEmpty() || tanggalPinjam.getValue() == null || tanggalPengembalian.getValue() == null) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            addTransaksi();
        }
    }

    private String addTransaksi() {
        try {
            String st = "INSERT INTO transaksi (username, id_buku, tanggal_peminjaman, tanggal_pengembalian) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(st);
            preparedStatement.setString(1, listUsername.getValue());
            preparedStatement.setString(2, getBookId(listBuku.getValue()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(tanggalPinjam.getValue()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(tanggalPengembalian.getValue()));

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
}
