package controllers;

import helper.InstanceHandling;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewCustomerController implements Initializable, InstanceHandling {
    public TextField nameField, addressField,postalField, phoneField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void setInstance() {

    }

    @Override
    public boolean checkControl(Control control) {
        return false;
    }

    @Override
    public void closeStage() {

    }
}
