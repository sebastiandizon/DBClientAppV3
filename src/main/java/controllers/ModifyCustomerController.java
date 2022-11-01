package controllers;

import DAO.CustomerDAOImpl;
import DAO.LocationDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    LocationDAO locationDAO = new LocationDAO();
    @FXML
    public TextField customerId;
    public TextField NameField, AddressField, PostalField, PhoneField;
    public ComboBox countryBox;
    public ComboBox division;
    String errorMsg = "";
    int customerIdInt;
    int divisionId;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void getSelectedCustomer(Customer customer){

        try {
            countryBox.setItems(locationDAO.getCountryNames());
            customerIdInt = customer.getCustomerId();
            customerId.setText(String.valueOf(customerIdInt));
            NameField.setText(customer.getCustomerName());
            AddressField.setText(customer.getAddress());
            PostalField.setText(customer.getPostalCode());
            PhoneField.setText(customer.getPhone());
            divisionId = customer.getDivisionId();
            division.setValue(locationDAO.getDivisionName(customer.getDivisionId()));
            countryBox.setValue(locationDAO.getCountryName(customer.getDivisionId()));
            setDivisionFunction();
        }catch (SQLException e) {e.printStackTrace();}
    }
    public void getUpdatedCustomer(ActionEvent actionEvent) throws SQLException{
        errorMsg = "";
        CustomerDAOImpl customerDAOInterface = new CustomerDAOImpl();
        int errorValue = 0;
        ObservableList<Control> controls = FXCollections.observableArrayList();
        controls.addAll(NameField, AddressField, PostalField, PhoneField, division);
        errorValue += checkValues(controls);
        if(errorValue <= 0) {
            Customer customer = new Customer();
            customer.setCustomerId(customerIdInt);
            customer.setCustomerName(NameField.getText());
            customer.setAddress(AddressField.getText());
            customer.setPostalCode(PostalField.getText());
            customer.setPhone(PhoneField.getText());
            customer.setDivisionId(locationDAO.getDivisionId((String)division.getSelectionModel().getSelectedItem()));
            customerDAOInterface.update(customer);
            Stage stage = (Stage)NameField.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }
    public void setDivisionFunction() throws SQLException {
        division.setItems(locationDAO.getMatchingDivisions((String)countryBox.getSelectionModel().getSelectedItem()));
    }
    public int checkValues(ObservableList<Control> controls) {
        int i = 0;
        for(Control control : controls) {
            if (control instanceof TextField) {
                if (((TextField) control).getText() == "") {
                    System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                    i++;
                }
            } else if (control instanceof ComboBox<?>) {
                if (((ComboBox) control).getSelectionModel().isEmpty()) {
                    System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                    i++;
                }
            }
        }
        return i;
    }
}
