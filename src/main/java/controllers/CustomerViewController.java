package controllers;

import DAO.AppointmentDAOImpl;
import DAO.CustomerDAOImpl;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import model.Main;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.stage.Stage;

public class CustomerViewController implements Initializable {
    AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    public TableView customerView;
    public TableColumn customerId, name, address, postal, phone, division;
    public MenuButton viewMenu;
    public AnchorPane customerViewPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            buildTable();
            MenuItem appointmentsView = new MenuItem("Appointments View");
            MenuItem contactsView = new MenuItem("Contacts View");
            appointmentsView.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
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
        } catch (SQLException e) {e.printStackTrace();}
    }
    /**Generates scene for new customer*/
    public void handleNewCustomer(ActionEvent actionEvent) throws IOException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-customer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Customer");
        stage.setScene(scene);
        stage.showAndWait();
        buildTable();
    }
    /**Generates table of customers*/
    public void buildTable() throws SQLException {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        division.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        customerView.setItems((ObservableList) customerDAO.getAll());
    }
    /**Generates scene for remove customer*/
    public void handleRemoveCustomer(ActionEvent actionEvent) throws SQLException {
        Customer customer = (Customer) customerView.getSelectionModel().getSelectedItem();

        if(customer == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the selected customer?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            if(appointmentDAO.getAssociated(customer.getCustomerId()).size() < 1) {
                customerDAO.delete(customer.getCustomerId());
                System.out.println("Customer removed");
                buildTable();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Removal error");
                alert1.setHeaderText("Error removing customer");
                alert1.setContentText("Could not remove. Customer has associated appointments");
                alert1.show();
            }
        } else {
            System.out.println("User cancelled remove request");
        }
    }
    /**Gets selected customer from table view and generates new scene for modifying selected customer*/
    public void handleModifyCustomer(ActionEvent actionEvent) throws IOException, SQLException{
        Customer customer = (Customer) customerView.getSelectionModel().getSelectedItem();
        if(customer == null){
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("modify-customer-view.fxml"));
        Parent root = fxmlLoader.load();

        ModifyCustomerController modifyCustomerController = fxmlLoader.getController();
        modifyCustomerController.getSelectedCustomer(customer);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
        customerView.setItems((ObservableList) customerDAO.getAll());
    }
    /**Gets selected customer and prompts user with confirmation to clear appointments associated with customer*/
    public void handleClearAppointments(ActionEvent actionEvent) throws SQLException {
        Customer customer = (Customer) customerView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you would like to remove all appointments associated with " + customer.getCustomerName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            if (appointmentDAO.getAssociated(customer.getCustomerId()).size() >= 1) {
                alert.setContentText(appointmentDAO.getAssociated(customer.getCustomerId()).size() + " removed");
                alert.show();
                appointmentDAO.deleteAll(appointmentDAO.getAssociated(customer.getCustomerId()));
            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Removal error");
                alert1.setHeaderText("Error removing appointments");
                alert1.setContentText("Customer already has 0 appointments.");
                alert1.show();
            }
        } else {
            System.out.println("User cancelled remove request");
        }
    }

}
