package controllers;

import com.company.dbclientappv2.Customer;
import com.company.dbclientappv2.Main;
import helper.ControllerFunction;
import helper.InstanceHandling;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lists.CountryList;
import lists.CustomerList;
import lists.FirstLevelDivList;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;


public class ModifyCustomerController implements Initializable, ControllerFunction, InstanceHandling {
    Customer selectedCustomer = CustomerViewController.selectedCustomer;
    public AnchorPane newCustomer;
    public TextField customerId, NameField, AddressField, PostalField, PhoneField;
    public ComboBox countryBox, Division;
    String errorMsg = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryBox.setItems(CountryList.getCountryNames());
        countryBox.setValue(CountryList.getCountry(FirstLevelDivList.getDivision(selectedCustomer.getDivisionId())).getCountry());
        setDivisionCombo();
        Division.setValue(FirstLevelDivList.getDivision(selectedCustomer.getDivisionId()).getDivision());
        NameField.setText(selectedCustomer.getCustomerName());
        AddressField.setText(selectedCustomer.getAddress());
        PostalField.setText(selectedCustomer.getPostalCode());
        PhoneField.setText(selectedCustomer.getPhone());
        customerId.setText(String.valueOf(Main.newCustomerId));
    }
    /**Creates Customer object after fields have been verified and then grabs existing customer and updates with the new information. The updates are
     * then queried against the Customer table in the database*/
    @Override
    public void setInstance() {
        errorMsg = "";
        ObservableList<Control> controls = FXCollections.observableArrayList();
        controls.addAll(NameField, AddressField, PostalField, PhoneField, Division);
        try {
            while (true) {
                for (Control control : controls) {
                    checkControl(control);
                }
                String name = NameField.getText();
                String address = AddressField.getText();
                String postal = PostalField.getText();
                String phone = PhoneField.getText();
                int divisionId = (FirstLevelDivList.getDivisionId(Division.getSelectionModel().getSelectedItem().toString()));
                Customer newCustomer = new Customer(Main.newCustomerId, name, address, postal, phone, divisionId);
                CustomerList.modifyCustomerQuery(CustomerList.getIndex(selectedCustomer), newCustomer);
                closeStage();
                break;
            }
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Creation error");
            alert.setHeaderText("Customer");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        } catch(ParseException e){e.printStackTrace();}
    }
    /**Checks controls for input validation*/
    @Override
    public boolean checkControl(Control control) {
        if (control instanceof TextField) {
            if (((TextField) control).getText() == "") {
                System.out.println(control.getId() + " is null");
                errorMsg = errorMsg + control.getId() + " cannot be null\n";
                return false;
            }
        } else if (control instanceof ComboBox<?>){
            if (((ComboBox) control).getSelectionModel().isEmpty()) {
                System.out.println(control.getId() + " is null");
                errorMsg = errorMsg + control.getId() + " cannot be null\n";
                return false;
            }
        }
        return true;
    }
    /**Gets existing stage and closes it*/
    @Override
    public void closeStage() {
        Stage stage = (Stage)newCustomer.getScene().getWindow();
        stage.close();
    }
    /**Sets the division combo box to the appropriate values after the country is selected*/
    public void setDivisionCombo(){
        System.out.println(CountryList.getCountryId((countryBox.getSelectionModel().getSelectedItem().toString())));
        Division.setItems(FirstLevelDivList.getDivisionNames(FirstLevelDivList.getMatchingDivisions(CountryList.getCountryId((countryBox.getSelectionModel().getSelectedItem().toString())))));
    }
}
