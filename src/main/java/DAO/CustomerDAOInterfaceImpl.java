package DAO;

import DAO.DAOInterface;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static helper.JDBC.connection;

public class CustomerDAOInterfaceImpl implements DAOInterface {
    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

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
    public void update(Object o, String[] params) {

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
        statement.executeQuery(query);
    }
}
