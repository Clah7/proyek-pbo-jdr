package com.controllers.admin;

import com.models.Buku;
import com.utils.Koneksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransaksiController implements Initializable {
    @FXML
    private Button btnTambahBuku;
    @FXML
    private Button btnDeleteBuku;
    @FXML
    private Button btnUpdateBuku;
    @FXML
    private TextField txtIdBuku;
    @FXML
    private TextField txtPenulis;
    @FXML
    private TextField txtPenerbit;
    @FXML
    private TextField txtTahunTerbit;
    @FXML
    private TextField txtNamaBuku;
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

    public TransaksiController() {
        connection = Koneksi.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDeleteBuku.setDisable(true);
        btnUpdateBuku.setDisable(true);

        fetRowList();
    }

    @FXML
    private void handleTableClick(MouseEvent event) throws SQLException {
        selectedBuku = tblData.getSelectionModel().getSelectedItem();
        if (selectedBuku != null) {
            fillForm(selectedBuku);
            btnTambahBuku.setDisable(true);
            btnDeleteBuku.setDisable(false);
            btnUpdateBuku.setDisable(false);
        }
    }

    private void clearFields() {
        txtIdBuku.clear();
        txtNamaBuku.clear();
        txtPenulis.clear();
        txtPenerbit.clear();
        txtTahunTerbit.clear();
    }

    private void fillForm(Buku buku) throws SQLException {
        txtIdBuku.setText(buku.getIdBuku());
        txtIdBuku.setEditable(false);

        txtNamaBuku.setText(buku.getNamaBuku());
        txtPenulis.setText(buku.getPenulis());
        txtPenerbit.setText(buku.getPenerbit());
        txtTahunTerbit.setText(buku.getTahunTerbit().toString());
    }

    @FXML
    private void handleAdd() {
        if (txtIdBuku.getText().isEmpty() || txtNamaBuku.getText().isEmpty() || txtPenulis.getText().isEmpty() || txtPenerbit.getText().isEmpty() || txtTahunTerbit.getText().isEmpty()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            addBuku();
            txtNamaBuku.setEditable(true);
            btnUpdateBuku.setDisable(true);
            btnDeleteBuku.setDisable(true);
            btnTambahBuku.setDisable(false);
        }
    }


    private String addBuku() {
        try {
            String st = "INSERT INTO buku VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(st);
            preparedStatement.setString(1, txtIdBuku.getText());
            preparedStatement.setString(2, txtNamaBuku.getText());
            preparedStatement.setString(3, txtPenulis.getText());
            preparedStatement.setString(4, txtPenerbit.getText());
            preparedStatement.setString(5, txtTahunTerbit.getText());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            fetRowList();
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedBuku != null) {
            if (txtIdBuku.getText().isEmpty() || txtNamaBuku.getText().isEmpty() || txtPenulis.getText().isEmpty() || txtPenerbit.getText().isEmpty() || txtTahunTerbit.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                updateBuku();
                txtIdBuku.setEditable(true);
                btnUpdateBuku.setDisable(true);
                btnDeleteBuku.setDisable(true);
                btnTambahBuku.setDisable(false);
                selectedBuku = null;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih buku yang akan diupdate!");
            alert.showAndWait();
        }
    }

    private String updateBuku() {
        try {
            String query = "UPDATE buku SET nama_buku = ?, penulis = ?, penerbit = ?, tahun_terbit = ? WHERE id_buku = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, txtNamaBuku.getText());
            preparedStatement.setString(2, txtPenulis.getText());
            preparedStatement.setString(3, txtPenerbit.getText());
            preparedStatement.setString(4, txtTahunTerbit.getText());
            preparedStatement.setString(5, txtIdBuku.getText());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Updated Successfully");

            fetRowList();
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedBuku != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Proceed to Delete?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteBuku(selectedBuku);
                txtIdBuku.setEditable(true);
                btnUpdateBuku.setDisable(true);
                btnDeleteBuku.setDisable(true);
                btnTambahBuku.setDisable(false);
                selectedBuku = null;
            }
        }
    }

    private String deleteBuku(Buku buku) {
        try {
            String query = "Delete from buku WHERE id_buku = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, buku.getIdBuku());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Deleted Successfully");

            fetRowList();
            clearFields();
            return "Success";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

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
