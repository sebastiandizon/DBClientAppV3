package controllers;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.Customer;
import com.company.dbclientappv2.Main;

import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lists.AppointmentList;
import lists.CustomerList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable{
    static Customer selectedCustomer;
    public TableView customerView = new TableView();
    public TableColumn customer_id, customer_name, address, postal_code, phone, division_id;
    public MenuButton viewMenu;
    public AnchorPane customerViewPane;
    public Button newCustomer, modifyCustomer, removeCustomer;
    /**Handler for new customer button that generates a New Customer Window*/
    public void handleNewCustomer(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-customer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Customer");
        stage.setScene(scene);
        stage.show();
    }
    /**Handler for remove customer button that deletes the customer the user has selected and prompts a confirmation of deletion*/
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
            if(getAssociatedAppointments(customer).size() < 1) {
                CustomerList.removeCustomer(CustomerList.getIndex(customer));
                System.out.println("Customer removed");
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
    public void handleClearAppointments(ActionEvent actionEvent) throws SQLException{
        Customer customer = (Customer) customerView.getSelectionModel().getSelectedItem();
        if(customer == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you would like to remove all appointments associated with " + customer.getCustomerName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            if(getAssociatedAppointments(customer).size() >= 1) {
                alert.setContentText(getAssociatedAppointments(customer).size() + " removed");
                alert.show();
                for(Appointment appointment : getAssociatedAppointments(customer)){
                    DatabaseQueries.deleteAppointment(appointment);
                    AppointmentList.getAllAppointments().removeAll(appointment);
                }
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
    /**@return Returns a list of appointments that a customer has under their name
     * @param customer specifies the customer to find appointments frmo*/
    public ObservableList<Appointment> getAssociatedAppointments(Customer customer){
        ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();
        for(Appointment appointment : AppointmentList.getAllAppointments()){
            if(customer.getCustomerId() == appointment.getCustomerId()){
                associatedAppointments.add(appointment);
            }
        }
        return associatedAppointments;
    }
    /**sets tables and action events*/
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
    /**Gets selected customer and generates a window with fields filled with that customer's info for modification*/
    public void handleModifyCustomer(ActionEvent actionEvent) throws IOException{
        selectedCustomer = (Customer)customerView.getSelectionModel().getSelectedItem();
        if(customerView==null){
            return;
        }
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modify-customer-view.fxml"));
        Parent root = loader.load();

        ModifyCustomerController modifyCustomerController = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modify Customer");
        stage.setScene(scene);
        stage.showAndWait();
        customerView.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUI();
    }
}
