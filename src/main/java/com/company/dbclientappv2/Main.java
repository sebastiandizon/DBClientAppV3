package com.company.dbclientappv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lists.AppointmentList;
import lists.CustomerList;

import java.io.IOException;

import static helper.JDBC.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        openConnection();

        AppointmentList.retrieveAppointments();
        CustomerList.retrieveAllCustomers();
        launch();

    }
}