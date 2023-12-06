//package com.controllers.admin;
//
//import com.models.Buku;
//import com.utils.Koneksi;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.net.URL;
//import java.sql.*;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ResourceBundle;
//
//public class ActionBukuController implements Initializable {
//    public Button btnGenerate;
//    private String id_mobil;
//    @FXML
//    private Button btnSaveBuku;
//    @FXML
//    private Button btnUpdateBuku;
//    @FXML
//    private Button btnDeleteBuku;
//    @FXML
//    private Button btnPengembalian;
//    @FXML
//    private TextField txtIdBuku;
//    @FXML
//    private TextField txtIdPelanggan;
//    @FXML
//    private TextField txtIdPegawai;
//    @FXML
//    private ComboBox<String> txtJenisMobil;
//    @FXML
//    private DatePicker datePinjam;
//    @FXML
//    private TextField txtLamaPinjam;
//    @FXML
//    Label lblStatus;
//    @FXML
//    Label LabelBiayaSewa;
//    @FXML
//    Label LabelTotalHarga;
//    @FXML
//    Label LabelTanggalKembali;
//
//    @FXML
//    private TableView<Buku> tblData;
//    private PreparedStatement preparedStatement;
//    private Connection connection;
//    private Buku selectedBuku;
//
//    public ActionBukuController() {
//        connection = Koneksi.conDB();
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        populateComboBox();
//
//        btnUpdateBuku.setDisable(true);
//        btnDeleteBuku.setDisable(true);
//        btnPengembalian.setDisable(true);
//
//        tblData.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                btnUpdateBuku.setDisable(false);
//                btnDeleteBuku.setDisable(false);
//                btnDeleteBuku.setDisable(false);
//                btnPengembalian.setDisable(false);
//            }
//        });
//
//        fetColumnList();
//        fetRowList();
//    }
//
//    private void populateComboBox() {
//        Connection connection = Koneksi.conDB();
//
//        if (connection != null) {
//            try {
//                String sql = "SELECT jenis FROM mobil";
//
//                PreparedStatement preparedStatement = connection.prepareStatement(sql);
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                ObservableList<String> jenisMobilList = FXCollections.observableArrayList();
//
//                while (resultSet.next()) {
//                    jenisMobilList.add(resultSet.getString("jenis"));
//                }
//
//                txtJenisMobil.setItems(jenisMobilList);
//
//                if (!jenisMobilList.isEmpty()) {
//                    txtJenisMobil.getSelectionModel().select(0);
//                }
//
//                preparedStatement.close();
//                resultSet.close();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @FXML
//    private void handleTableClick(MouseEvent event) throws SQLException {
//        selectedBuku = tblData.getSelectionModel().getSelectedItem();
//        if (selectedBuku != null) {
//            fillForm(selectedBuku);
//            btnUpdateBuku.setDisable(false);
//            btnDeleteBuku.setDisable(false);
//            btnPengembalian.setDisable(false);
//            btnSaveBuku.setDisable(true);
//        }
//    }
//
//    private void fillForm(Buku peminjaman) throws SQLException {
//        txtIdBuku.setText(peminjaman.getIdBuku());
//        txtIdBuku.setEditable(false);
//
//        txtIdPelanggan.setText(peminjaman.getIdPelanggan());
//        txtJenisMobil.setValue(getJenisMobilById());
//        txtIdPegawai.setText(peminjaman.getIdPetugas());
//        datePinjam.setValue(peminjaman.getTanggalPinjam());
//        LabelTanggalKembali.setText(peminjaman.getTanggalKembali().toString());
//        txtLamaPinjam.setText(peminjaman.getLama());
//
//        DecimalFormat decimalFormat = new DecimalFormat("#,###");
//        String formattedSewa = "Rp " + decimalFormat.format(peminjaman.getHargaHarian());
//        String formattedBiaya = "Rp " + decimalFormat.format(new BigDecimal(peminjaman.getBiaya()));
//
//        LabelBiayaSewa.setText(formattedSewa);
//        LabelTotalHarga.setText(formattedBiaya);
//    }
//
//    @FXML
//    private void btnGenerateOnClick(MouseEvent event) {
//        Koneksi kon = new Koneksi();
//        ResultSet rs = null;
//
//        try {
//            String selectedJenis = txtJenisMobil.getSelectionModel().getSelectedItem();
//
//            rs = kon.getData("SELECT * FROM mobil WHERE jenis = '" + selectedJenis + "'");
//
//            while (rs.next()) {
//                String sewa = rs.getString("sewa");
//                id_mobil = rs.getString("id_mobil");
//
//                int lama = Integer.parseInt(txtLamaPinjam.getText());
//                int total_biaya = lama * Integer.parseInt(sewa);
//
//                LocalDate startDate = datePinjam.getValue();
//                LocalDate returnDate = startDate.plusDays(lama);
//                LabelTanggalKembali.setText(returnDate.toString());
//
//                DecimalFormat decimalFormat = new DecimalFormat("#,###");
//                String formattedSewa = "Rp " + decimalFormat.format(Integer.parseInt(sewa));
//                String formattedBiaya = "Rp " + decimalFormat.format(total_biaya);
//
//                LabelBiayaSewa.setText(formattedSewa);
//                LabelTotalHarga.setText(formattedBiaya);
//
//                lblStatus.setTextFill(Color.GREEN);
//                lblStatus.setText("Data Fetched Successfully");
//            }
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//            lblStatus.setTextFill(Color.TOMATO);
//            lblStatus.setText("Error Fetching Data");
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException ex) {
//                System.err.println("Error closing ResultSet: " + ex.getMessage());
//            }
//        }
//    }
//
//    @FXML
//    private void btnPengembalianOnClick(MouseEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rental/fxml/pegawai/Pengembalian.fxml"));
//            Parent root = loader.load();
//
//            PengembalianController pengembalianController = loader.getController();
//            pengembalianController.setDataBukuController(this);
//
//            pengembalianController.setSelectedBuku(selectedBuku);
//            Stage stage = new Stage();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setResizable(false);
//            stage.show();
//        } catch (IOException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }
//
//    @FXML
//    private String getJenisMobilById() throws SQLException {
//        String st = "SELECT jenis FROM mobil WHERE id_mobil = '" + selectedBuku.getIdMobil() + "'";
//
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(st);
//
//        if (resultSet.next()) {
//            return resultSet.getString("jenis");
//        } else {
//            return "ID not found";
//        }
//    }
//
//    @FXML
//    private void handleAdd(MouseEvent event) throws SQLException {
//        if (txtIdPegawai.getText().isEmpty() ||
//                txtIdPelanggan.getText().isEmpty() ||
//                txtIdBuku.getText().isEmpty() ||
//                txtIdBuku.getText().isEmpty() ||
//                datePinjam.getValue() == null ||
//                txtLamaPinjam.getText().isEmpty()) {
//            lblStatus.setTextFill(Color.TOMATO);
//            lblStatus.setText("Enter all details");
//        } else {
//            addPelanggan();
//        }
//    }
//
//    @FXML
//    private String addPelanggan() {
//        try {
//            String st = "INSERT INTO peminjaman VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, 0)";
//            preparedStatement = connection.prepareStatement(st);
//
//            preparedStatement.setString(1, txtIdBuku.getText());
//            preparedStatement.setString(2, txtIdPelanggan.getText());
//            preparedStatement.setString(3, id_mobil);
//            preparedStatement.setString(4, txtIdPegawai.getText());
//            preparedStatement.setDate(5, Date.valueOf(datePinjam.getValue()));
//
//            String dateString = LabelTanggalKembali.getText();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate tanggalKembali = LocalDate.parse(dateString, formatter);
//            System.out.println(dateString);
//
//            preparedStatement.setDate(6, Date.valueOf(tanggalKembali));
//
//            preparedStatement.setString(7, txtLamaPinjam.getText());
//
//            String cleanedBiayaSewaString = LabelTotalHarga.getText().replaceAll("[^0-9.]", "");
//            BigDecimal biayaSewa = new BigDecimal(cleanedBiayaSewaString);
//
//            preparedStatement.setBigDecimal(8, biayaSewa);
//
//            preparedStatement.executeUpdate();
//            lblStatus.setTextFill(Color.GREEN);
//            lblStatus.setText("Added Successfully");
//
//            fetRowList();
//            clearFields();
//            return "Success";
//
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            lblStatus.setTextFill(Color.TOMATO);
//            lblStatus.setText(ex.getMessage());
//            return "Exception";
//        }
//    }
//
//    @FXML
//    private void handleUpdate() {
//        if (selectedBuku != null) {
//            if (txtIdPegawai.getText().isEmpty() ||
//                    txtIdPelanggan.getText().isEmpty() ||
//                    txtIdBuku.getText().isEmpty() ||
//                    txtIdBuku.getText().isEmpty() ||
//                    datePinjam.getValue() == null ||
//                    txtLamaPinjam.getText().isEmpty()) {
//                lblStatus.setTextFill(Color.TOMATO);
//                lblStatus.setText("Enter all details");
//            } else {
//                if (updateBuku() == "Success") {
//                    txtIdBuku.setEditable(true);
//                    btnUpdateBuku.setDisable(true);
//                    btnDeleteBuku.setDisable(true);
//                    btnPengembalian.setDisable(true);
//                    btnSaveBuku.setDisable(false);
//                    selectedBuku = null;
//                }
//            }
//
//        } else {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Peringatan");
//            alert.setHeaderText(null);
//            alert.setContentText("Pilih pelanggan yang akan diupdate!");
//            alert.showAndWait();
//        }
//    }
//
//    private String updateBuku() {
//        try {
//            String query = "UPDATE peminjaman SET id_pelanggan = ?, id_mobil = ?, id_petugas = ?, tgl_pinjam = ?, tgl_hrs_kembali = ?, lama = ?, biaya = ? WHERE id_buku = ?";
//
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, txtIdPelanggan.getText());
//            preparedStatement.setString(2, id_mobil);
//            preparedStatement.setString(3, txtIdPegawai.getText());
//            preparedStatement.setDate(4, Date.valueOf(datePinjam.getValue()));
//
//            String dateString = LabelTanggalKembali.getText();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate tanggalKembali = LocalDate.parse(dateString, formatter);
//
//            preparedStatement.setDate(5, Date.valueOf(tanggalKembali));
//            preparedStatement.setString(6, txtLamaPinjam.getText());
//
//            String cleanedBiayaSewaString = LabelTotalHarga.getText().replaceAll("[^0-9.]", "");
//            BigDecimal biayaSewa = new BigDecimal(cleanedBiayaSewaString);
//
//            preparedStatement.setBigDecimal(7, biayaSewa);
//            preparedStatement.setString(8, txtIdBuku.getText());
//
//            preparedStatement.executeUpdate();
//            lblStatus.setTextFill(Color.GREEN);
//            lblStatus.setText("Updated Successfully");
//
//            fetRowList();
//            clearFields();
//            return "Success";
//
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            lblStatus.setTextFill(Color.TOMATO);
//            lblStatus.setText(ex.getMessage());
//            return "Exception";
//        }
//    }
//
//    private ObservableList<Buku> data;
//    String SQL = "SELECT * from buku";
//
//    private void fetColumnList() {
//
//        try {
//            tblData.getColumns().clear();
//
//            String[] columnNames = {"Id Buku", "Nama Buku", "Penulis", "Penerbit", "Tahun Terbit"};
//
//            for (String columnName : columnNames) {
//                TableColumn<Buku, String> column = new TableColumn<>(columnName);
//                column.setCellValueFactory(new PropertyValueFactory<>(columnName.toLowerCase().replace(" ", "")));
//                tblData.getColumns().add(column);
//            }
//        } catch (Exception e) {
//            System.out.println("Error " + e.getMessage());
//        }
//    }
//
//    public void fetRowList() {
//        data = FXCollections.observableArrayList();
//        ResultSet rs;
//        try {
//            rs = connection.createStatement().executeQuery(SQL);
//
//            while (rs.next()) {
//                Buku peminjaman = new Buku();
//                peminjaman.setIdBuku(rs.getString("id_buku"));
//                peminjaman.setNamaBuku(rs.getString("nama_buku"));
//                peminjaman.setPenulis(rs.getString("penulis"));
//                peminjaman.setPenerbit(rs.getString("penerbit"));
//                peminjaman.setTahunTerbit(String.valueOf(rs.getInt("tahun_terbit")));
//
//                data.add(peminjaman);
//            }
//
//            tblData.setItems(data);
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }
//}
