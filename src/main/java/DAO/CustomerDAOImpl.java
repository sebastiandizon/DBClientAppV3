package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;

import java.sql.*;
import java.util.List;

import static helper.JDBC.connection;

public class CustomerDAOImpl implements DAOInterface {

    @Override
    public List getAll() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM client_schedule.customers;";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt(1));
            customer.setCustomerName(rs.getString(2));
            customer.setAddress(rs.getString(3));
            customer.setPostalCode(rs.getString(4));
            customer.setPhone(rs.getString(5));
            customer.getModifyRecord().setCreateDate(rs.getTimestamp(6).toInstant());
            customer.getModifyRecord().setCreatedBy(rs.getString(7));
            customer.getModifyRecord().setLastUpdate(rs.getTimestamp(8).toInstant());
            customer.getModifyRecord().setLastUpdateBy(rs.getString(9));
            customer.setDivisionId(rs.getInt(10));
            allCustomers.add(customer);
        }
        return allCustomers;
    }


    @Override
    public void save(Object o) throws SQLException{
        String query = "INSERT INTO client_schedule.customers VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ((Customer)o).getCustomerName());
        ps.setString(2, ((Customer)o).getAddress());
        ps.setString(3, ((Customer)o).getPostalCode());
        ps.setString(4, ((Customer)o).getPhone());
        ps.setString(5, ((Customer)o).getModifyRecord().getSimpleCreateDate());
        ps.setString(6, ((Customer)o).getModifyRecord().getCreatedBy());
        ps.setString(7, ((Customer)o).getModifyRecord().getSimpleLastUpdate());
        ps.setString(8, ((Customer)o).getModifyRecord().getLastUpdateBy());
        ps.setString(9, (String.valueOf(((Customer)o).getDivisionId())));
        System.out.println(ps);
        ps.execute();
    }

    @Override
    public void update(Object o) throws SQLException{
        PreparedStatement query;
        query = connection.prepareStatement("UPDATE client_schedule.customers" +
        " SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
        " WHERE Customer_ID = " + ((Customer)o).getCustomerId());
        query.setString(1,((Customer)o).getCustomerName());
        query.setString(2,((Customer)o).getAddress());
        query.setString(3,((Customer)o).getPostalCode());
        query.setString(4,((Customer)o).getPhone());
        query.setString(5,((Customer)o).getModifyRecord().getSimpleLastUpdate());
        query.setString(6,((Customer)o).getModifyRecord().getLastUpdateBy());
        query.setInt(7,((Customer)o).getDivisionId());
        System.out.println(query);
        query.executeUpdate();
    }

    @Override
    public void delete(Object o) throws SQLException{
        String query = "DELETE FROM Customers WHERE Customer_ID = " + ((Customer)o).getCustomerId();
        Statement statement = connection.createStatement();
        statement.executeQuery(query);
    }
    public void delete(int customerID) throws SQLException{
        String query = "DELETE FROM Customers WHERE Customer_ID = " + customerID;
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    public ObservableList<Integer> getCustIds() throws SQLException{
        ObservableList<Integer> custIds = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT Customer_ID FROM customers ORDER BY Customer_ID ASC";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            custIds.add(rs.getInt("Customer_ID"));
        }
        return custIds;
    }

    public ObservableList<Contact> getContacts() throws SQLException{
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts;";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()){
            Contact contact = new Contact(rs.getInt(1), rs.getString(2), rs.getString(3));
            contacts.add(contact);
        }
        return contacts;
    }
    public ObservableList<Integer> getContactIds() throws SQLException{
        ObservableList<Integer> contactIDs = FXCollections.observableArrayList();
        String query = "SELECT Contact_ID FROM contacts;";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()){
            contactIDs.add(rs.getInt(1));
        }
        return contactIDs;
    }

}
