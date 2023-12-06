package com.controllers.admin;

import com.models.User;
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

public class UserController implements Initializable {
    @FXML
    private Button btnTambahUser;
    @FXML
    private Button btnDeleteUser;
    @FXML
    private Button btnUpdateUser;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtAlamat;
    @FXML
    private TextField txtKode;
    @FXML
    private TextField searchField;
    @FXML
    Label lblStatus;
    @FXML
    private TableView<User> tblData;

    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colNama;

    @FXML
    private TableColumn<User, String> colAlamat;

    @FXML
    private TableColumn<User, Integer> colKode;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private User selectedUser;

    public UserController() {
        connection = Koneksi.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDeleteUser.setDisable(true);
        btnUpdateUser.setDisable(true);

        fetRowList("");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            fetRowList(newValue);
        });
    }

    @FXML
    private void handleTableClick(MouseEvent event) throws SQLException {
        selectedUser = tblData.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            fillForm(selectedUser);
            btnTambahUser.setDisable(true);
            btnDeleteUser.setDisable(false);
            btnUpdateUser.setDisable(false);
        }
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        txtAlamat.clear();
        txtNama.clear();
        txtKode.clear();
    }

    private void fillForm(User user) throws SQLException {
        txtUsername.setText(user.getUsername());
        txtUsername.setEditable(false);

        txtNama.setText(user.getNamaLengkap());
        txtPassword.setText(user.getNamaLengkap());
        txtAlamat.setText(user.getAlamat());
        txtKode.setText(user.getKode());
    }

    @FXML
    private void handleAdd() {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtAlamat.getText().isEmpty() || txtKode.getText().isEmpty() || txtNama.getText().isEmpty()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            addUser();
            txtAlamat.setEditable(true);
            btnUpdateUser.setDisable(true);
            btnDeleteUser.setDisable(true);
            btnTambahUser.setDisable(false);
        }
    }

    private String addUser() {
        try {
            String st = "INSERT INTO user VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(st);
            preparedStatement.setString(1, txtUsername.getText());
            preparedStatement.setString(2, txtPassword.getText());
            preparedStatement.setString(3, txtNama.getText());
            preparedStatement.setString(4, txtAlamat.getText());
            preparedStatement.setString(5, txtKode.getText());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            fetRowList("");
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
        if (selectedUser != null) {
            if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtAlamat.getText().isEmpty() || txtKode.getText().isEmpty() || txtNama.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                updateUser();
                txtUsername.setEditable(true);
                btnUpdateUser.setDisable(true);
                btnDeleteUser.setDisable(true);
                btnTambahUser.setDisable(false);
                selectedUser = null;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih user yang akan diupdate!");
            alert.showAndWait();
        }
    }

    private String updateUser() {
        try {
            String query = "UPDATE user SET password = ?, nama_lengkap = ?, alamat = ?, kode = ? WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, txtPassword.getText());
            preparedStatement.setString(2, txtNama.getText());
            preparedStatement.setString(3, txtAlamat.getText());
            preparedStatement.setString(4, txtKode.getText());
            preparedStatement.setString(5, txtUsername.getText());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Updated Successfully");

            fetRowList("");
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
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Proceed to Delete?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteUser(selectedUser);
                txtUsername.setEditable(true);
                btnUpdateUser.setDisable(true);
                btnDeleteUser.setDisable(true);
                btnTambahUser.setDisable(false);
                selectedUser = null;
            }
        }
    }

    private String deleteUser(User user) {
        try {
            String query = "Delete from user WHERE id_user = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getIdUser());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Deleted Successfully");

            fetRowList("");
            clearFields();
            return "Success";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private ObservableList<User> data;
    String SQL = "SELECT * from user";

    private void fetRowList(String keyword) {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setNamaLengkap(rs.getString("nama_lengkap"));
                user.setAlamat(rs.getString("alamat"));
                user.setKode(String.valueOf(rs.getInt("kode")));

                // Filter the data based on the keyword
                if (userContainsKeyword(user, keyword)) {
                    data.add(user);
                }
            }

            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colNama.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
            colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
            colKode.setCellValueFactory(new PropertyValueFactory<>("kode"));

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private boolean userContainsKeyword(User user, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return user.getUsername().toLowerCase().contains(lowerCaseKeyword) ||
                user.getNamaLengkap().toLowerCase().contains(lowerCaseKeyword) ||
                user.getAlamat().toLowerCase().contains(lowerCaseKeyword) ||
                user.getKode().contains(lowerCaseKeyword);
    }
}
