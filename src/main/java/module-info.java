module com.perpustakaan {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;

    opens com.perpustakaan to javafx.fxml;
    opens com.controllers to javafx.fxml;
    exports com.perpustakaan;
    exports com.controllers;
    exports com.controllers.admin;
//    exports com.controllers.user;
    exports com.models;
    opens com.controllers.admin to javafx.fxml;
//    opens com.controllers.user to javafx.fxml;

    opens com.models to javafx.base;
}