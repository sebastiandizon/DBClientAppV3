package lists;

import com.company.dbclientappv2.Customer;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerList implements Initializable {
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static void retrieveAllCustomers(){
        try {
            ResultSet rs = DatabaseQueries.retrieveTable("customers");
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customer newCustomer = new Customer(customerId, name, address, postalCode, phone, divisionId);
                allCustomers.add(newCustomer);
            }
        } catch (SQLException e){ e.printStackTrace(); }
    }

    public void addCustomerQuery(Customer customer){}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CustomerList Init");
        retrieveAllCustomers();
    }
}
