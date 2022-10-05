package helper;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.AppointmentsRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;

public class DatabaseQueries {

    public static ObservableList<Integer> getCustomerIds() throws SQLException {
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM client_schedule.customers;";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            userIdList.add(rs.getInt("Customer_ID"));
        }
        return userIdList;
    }

    public static ObservableList<Integer> getContactIds() throws SQLException {
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM client_schedule.contacts;";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            userIdList.add(rs.getInt("Contact_ID"));
        }
        return userIdList;
    }

}
