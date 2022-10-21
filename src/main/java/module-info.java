module com.company.dbclientappv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens model to javafx.fxml;
    exports model;
    exports controllers;
    opens controllers to javafx.fxml;
    exports helper;
    opens helper to javafx.fxml;
    exports DAO;
    opens DAO to javafx.fxml;
}