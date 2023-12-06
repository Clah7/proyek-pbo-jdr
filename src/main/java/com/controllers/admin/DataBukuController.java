package com.controllers.admin;

import com.models.Buku;
import com.utils.Koneksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DataBukuController implements Initializable {
    public Button btnGenerate;
    private String id_mobil;
    @FXML
    private Button btnSaveBuku;
    @FXML
    private Button btnTambahBuku;
    @FXML
    private Button btnDeleteBuku;
    @FXML
    private Button btnEditBuku;
    @FXML
    private TextField txtIdBuku;
    @FXML
    private TextField txtIdPelanggan;
    @FXML
    private TextField txtIdPegawai;
    @FXML
    private ComboBox<String> txtJenisMobil;
    @FXML
    private DatePicker datePinjam;
    @FXML
    private TextField txtLamaPinjam;
    @FXML
    Label lblStatus;
    @FXML
    private TableView<Buku> tblData;

    @FXML
    private TableColumn<Buku, String> colIdBuku;

    @FXML
    private TableColumn<Buku, String> colNamaBuku;

    @FXML
    private TableColumn<Buku, String> colPenulis;

    @FXML
    private TableColumn<Buku, String> colPenerbit;

    @FXML
    private TableColumn<Buku, Integer> colTahunTerbit;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private Buku selectedBuku;

    public DataBukuController() {
        connection = Koneksi.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        btnDeleteBuku.setDisable(true);
        btnEditBuku.setDisable(true);

        fetRowList();
    }

    @FXML
    private void btnTambahBukuOnClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/perpustakaan/fxml/admin/EditTambahBuku.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void btnEditBukuOnClick(MouseEvent event) {

    }

//    @FXML
//    private void handleTableClick(MouseEvent event) throws SQLException {
//        selectedBuku = tblData.getSelectionModel().getSelectedItem();
//        if (selectedBuku != null) {
//            fillForm(selectedBuku);
//            btnTambahBuku.setDisable(false);
//            btnDeleteBuku.setDisable(false);
//            btnEditBuku.setDisable(false);
//            btnSaveBuku.setDisable(true);
//        }
//    }

    private ObservableList<Buku> data;
    String SQL = "SELECT * from buku";

    public void fetRowList() {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdBuku(rs.getString("id_buku"));
                buku.setNamaBuku(rs.getString("nama_buku"));
                buku.setPenulis(rs.getString("penulis"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setTahunTerbit(rs.getInt("tahun_terbit"));

                data.add(buku);
            }

            colIdBuku.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
            colNamaBuku.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
            colPenulis.setCellValueFactory(new PropertyValueFactory<>("penulis"));
            colPenerbit.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
            colTahunTerbit.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }


}
