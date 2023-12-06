package com.controllers.admin;

import com.models.Transaksi;
import com.utils.Koneksi;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PengembalianController implements Initializable {
    @FXML
    private ComboBox<String> comboStatus;
    private Transaksi selectedTransaksi;
    private Connection connection;
    private PreparedStatement preparedStatement;

    private DashboardController dashboardController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("Menunggu", "Dikembalikan", "Terlambat");

        comboStatus.setItems(options);
        comboStatus.setValue("");
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController= dashboardController;
    }

    public PengembalianController() {
        connection = Koneksi.conDB();
    }

    public void setSelectedBooking(Transaksi transaksi) {
        this.selectedTransaksi = transaksi;
    }

    @FXML
    private void handleSave(MouseEvent event) {
        updateTransaksi(selectedTransaksi);
        showAlert("Pengembalian berhasil diupdate.");
//        dashboardController.fetRowList("");

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private void updateTransaksi(Transaksi transaksi) {
        try {
            String query = "UPDATE transaksi SET status = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, comboStatus.getValue());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Peringatan");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
