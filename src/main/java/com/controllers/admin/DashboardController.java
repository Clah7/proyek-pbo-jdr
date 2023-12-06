package com.controllers.admin;

import com.models.Transaksi;
import com.utils.Koneksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private TextField searchField;
    @FXML
    Label lblStatus;
    @FXML
    private TableView<Transaksi> tblData;

    @FXML
    private TableColumn<Transaksi, String> colIdTransaksi;

    @FXML
    private TableColumn<Transaksi, String> colUsername;
    @FXML
    private TableColumn<Transaksi, String> colIdBuku;
    @FXML
    private TableColumn<Transaksi, Date> colTanggalPinjam;
    @FXML
    private TableColumn<Transaksi, Date> colTanggalKembali;

    @FXML
    private TableColumn<Transaksi, String> colStatus;
    private Connection connection;
    private Transaksi selectedTransaksi;

    public DashboardController() {
        connection = Koneksi.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetRowList("");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            fetRowList(newValue);
        });
    }

    @FXML
    private void handleRefreshData() {
        fetRowList("");
    }

    @FXML
    private void handleTableClick(MouseEvent event) throws SQLException {
        selectedTransaksi = tblData.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void btnPengembalianOnClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/perpustakaan/fxml/admin/Pengembalian.fxml"));
            Parent root = loader.load();

            PengembalianController pengembalianController = loader.getController();
            pengembalianController.setDashboardController(this);

            pengembalianController.setSelectedBooking(selectedTransaksi);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private ObservableList<Transaksi> data;
    String SQL = "SELECT * from transaksi";

    public void fetRowList(String keyword) {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setIdTransaksi(rs.getString("id_transaksi"));
                transaksi.setUsername(rs.getString("username"));
                transaksi.setIdBuku(rs.getString("id_buku"));
                transaksi.setTanggalPeminjaman(rs.getDate("tanggal_peminjaman"));
                transaksi.setTanggalPengembalian(rs.getDate("tanggal_pengembalian"));

                if (transaksiContainsKeyword(transaksi, keyword)) {
                    data.add(transaksi);
                }
            }

            colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colIdBuku.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
            colTanggalPinjam.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
            colTanggalKembali.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private boolean transaksiContainsKeyword(Transaksi transaksi, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return transaksi.getIdTransaksi().toLowerCase().contains(lowerCaseKeyword) ||
                transaksi.getUsername().toLowerCase().contains(lowerCaseKeyword) ||
                transaksi.getIdBuku().toLowerCase().contains(lowerCaseKeyword) ||
                transaksi.getStatus().toLowerCase().contains(lowerCaseKeyword);
    }
}
