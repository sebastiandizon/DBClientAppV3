module com.company.dbclientappv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.company.dbclientappv2 to javafx.fxml;
    exports com.company.dbclientappv2;
    exports controllers;
    opens controllers to javafx.fxml;
}