package lists;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.Customer;
import com.company.dbclientappv2.Users;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ResourceBundle;

public class CustomerList implements Initializable {

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
    /**retrieves all customers from database*/
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

    /**removes customer at specified index from database and list*/
    public static void removeCustomer(int index)throws SQLException{
        DatabaseQueries.deleteCustomer(allCustomers.get(index));
        allCustomers.remove(index);
    }

    /**@param customer used to find index
     * @return int value of customer index*/
    public static int getIndex(Customer customer) {
        return allCustomers.indexOf(customer);
    }
    /**adds customer to allCustomers list and adds customer to database
     * @param customer desired customer to be added to databse and list*/
    public static void addCustomerQuery(Customer customer){
        try {
            allCustomers.add(customer);
            DatabaseQueries.pushNewCustomer(customer);
        }catch (SQLException e){}
    }
    /**modifies customer in allCustomers list and updates customer data to database
     * @param index index of existing customer in allCustomers list
     * @param customer desired customer to be queried in databse and updated in list*/
    public static void modifyCustomerQuery(int index, Customer customer) throws ParseException {
        try {
            allCustomers.get(index).setCustomerId(customer.getCustomerId());
            allCustomers.get(index).setCustomerName(customer.getCustomerName());
            allCustomers.get(index).setAddress(customer.getAddress());
            allCustomers.get(index).setPostalCode(customer.getPostalCode());
            allCustomers.get(index).setPhone(customer.getPhone());
            allCustomers.get(index).getObjectModify().setLastUpdateBy(Users.userName);
            allCustomers.get(index).getObjectModify().setLastUpdate(Instant.now());
            DatabaseQueries.updateExistingCustomer(customer);

        }catch (SQLException e){}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CustomerList Init");
        retrieveAllCustomers();
    }
}
