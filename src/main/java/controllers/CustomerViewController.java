package controllers;

import com.company.dbclientappv2.Main;
import helper.InstanceHandling;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lists.CustomerList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable{
    public TableView customerView = new TableView();
    public TableColumn customer_id, customer_name, address, postal_code, phone, division_id;
    public MenuButton viewMenu;
    public AnchorPane customerViewPane;
    private Button newCustomer, modifyCustomer, removeCustomer;

    public void handleNewCustomer(ActionEvent actionEvent){

    }

    public void setUI(){
        customer_id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customer_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postal_code.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        division_id.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        customerView.setItems(CustomerList.getAllCustomers());

        MenuItem appointmentsView = new MenuItem("Appointments View");
        MenuItem contactsView = new MenuItem("Contacts View");
        appointmentsView.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("primary-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage)customerViewPane.getScene().getWindow();
                stage.setTitle("Appointments");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioException) {ioException.printStackTrace();}
        });
        contactsView.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());
                Stage stage = (Stage)customerViewPane.getScene().getWindow();
                stage.setTitle("Customers");
                stage.setScene(newScene);
                stage.show();
            } catch (IOException ioException) {ioException.printStackTrace();}
        });

        viewMenu.getItems().addAll(appointmentsView, contactsView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUI();



    }
}
