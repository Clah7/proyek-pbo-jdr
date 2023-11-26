module com.example.check {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.check to javafx.fxml;
    exports com.example.check;
}