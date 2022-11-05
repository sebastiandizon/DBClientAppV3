package controllers;

import DAO.CustomerDAOImpl;
import DAO.LocationDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class NewCustomerController implements Initializable {
    LocationDAO locationDAO = new LocationDAO();
    public TextField NameField, AddressField, PostalField, PhoneField;

    public ComboBox countryBox;
    public ComboBox division;
    int customerID;
    String errorMsg = "";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            LocationDAO locationDAO = new LocationDAO();
            countryBox.setItems(locationDAO.getCountryNames());
        }catch (SQLException e){e.printStackTrace();}

    }
    /**Gets user inputs and updates customer with matching ID to desired fields*/
    public void getNewCustomer(ActionEvent actionEvent) throws SQLException{
        errorMsg = "";
        CustomerDAOImpl customerDAOInterface = new CustomerDAOImpl();
        int errorValue = 0;
        ObservableList<Control> controls = FXCollections.observableArrayList();
        controls.addAll(NameField, AddressField, PostalField, PhoneField, division);
        errorValue += checkValues(controls);
        if(errorValue <= 0) {
            Customer customer = new Customer();
            customer.setCustomerName(NameField.getText());
            customer.setAddress(AddressField.getText());
            customer.setPostalCode(PostalField.getText());
            customer.setPhone(PhoneField.getText());
            customer.setDivisionId(locationDAO.getDivisionId((String)division.getSelectionModel().getSelectedItem()));
            customerDAOInterface.save(customer);
            Stage stage = (Stage)NameField.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }
    /**Sets division combo box to display correct values based on selected country*/
    public void setDivisionFunction(ActionEvent actionEvent) throws SQLException {
        division.setItems(locationDAO.getMatchingDivisions((String)countryBox.getSelectionModel().getSelectedItem()));
    }
    /**Checks controls for null values*/
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
