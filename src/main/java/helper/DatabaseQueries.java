package helper;

import com.company.dbclientappv2.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import static helper.JDBC.connection;

public class DatabaseQueries {
    /**@return list of integers of customer ids*/
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
    /**@return list of integers of contact ids*/
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
    /**gets appointment and converts fields into string format to be added to query. Query is sent to database to the appointment table*/
    public static void pushNewAppointment(Appointment appointment) throws SQLException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
        Date startDate = Date.from(appointment.getStartTime().toInstant());
        Date endDate = Date.from(appointment.getEndTime().toInstant());
        Date createdDate = Date.from(appointment.getModifyRecord().getCreateDate());
        Date lastUpdated = Date.from(appointment.getModifyRecord().getLastUpdate());

        Statement statement = connection.createStatement();
        String id = String.valueOf(appointment.getAppointmentId());
        String title = appointment.getTitle();
        String description = appointment.getDescription();
        String location = appointment.getLocation();
        String type = appointment.getType();

        String start = simpleDateFormat.format(startDate);
        String end = simpleDateFormat.format(endDate);
        String createDate = simpleDateFormat.format(createdDate);
        String createdBy = String.valueOf(appointment.getModifyRecord().getCreatedBy());
        String lastUpdate = simpleDateFormat.format(lastUpdated);
        String lastUpdatedBy = String.valueOf(appointment.getModifyRecord().getLastUpdateBy());
        String customerId = String.valueOf(appointment.getCustomerId());
        String userId = String.valueOf(appointment.getUserId());
        String contactId = String.valueOf(appointment.getContactId());

        String query = "INSERT INTO client_schedule.appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, " +
                "Last_Updated_By, Customer_ID, User_ID, Contact_ID ) " +
                "VALUES ('" + id + "', '" + title + "', '" + description + "', '" + location + "', '" + type + "', '" + start + "', '" + end + "', '" +
                createDate  + "', '" +  createdBy  + "', '" +  lastUpdate  + "', '" +  lastUpdatedBy  + "', '" +  customerId  + "', '" +  userId  + "', '" +  contactId + "');";
        System.out.println(query);
        statement.executeUpdate(query);

        System.out.println("Database appointment table updated successfully");
    }
    /**gets appointment and converts fields into string format to be added to query. Query is sent to database to the appointment table and the
     * corresponding row is updated with its new values*/
    public static void updateExistingAppointment(Appointment appointment) throws SQLException, ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date start= Date.from(appointment.getStartTime().toInstant());
        Date end= Date.from(appointment.getEndTime().toInstant());
        Date create= Date.from(appointment.getModifyRecord().getCreateDate());
        Date lastupdate= Date.from(appointment.getModifyRecord().getLastUpdate());

        String startFormatted = simpleDateFormat.format(start);
        String endFormatted = simpleDateFormat.format(end);

        String lastUpdateFormatted = simpleDateFormat.format(lastupdate);

        appointment.getAttributes(appointment).get(0);
        Statement statement = connection.createStatement();
        String query = "UPDATE appointments SET Appointment_ID = '"+appointment.getAttributes(appointment).get(0)+"', Title = '"+appointment.getAttributes(appointment).get(1)+"', Description = '"+appointment.getAttributes(appointment).get(2)+"', Location = '"+appointment.getAttributes(appointment).get(3)+"', " +
                "Type = '"+appointment.getAttributes(appointment).get(4)+"', Start = '"+startFormatted+"', End = '"+endFormatted+"', Create_Date = '"+appointment.getAttributes(appointment).get(7)+"', Created_By = '"+appointment.getAttributes(appointment).get(8)+"', Last_Update = '"+lastUpdateFormatted+"', Last_Updated_By = '"+ Users.userName+"', Customer_ID = '"+appointment.getAttributes(appointment).get(11)+"', User_ID = '"+appointment.getAttributes(appointment).get(12)+"', Contact_ID = '"+appointment.getAttributes(appointment).get(13)+"' WHERE Appointment_ID = '"+appointment.getAttributes(appointment).get(0)+"';";
        System.out.println(query);
        statement.executeUpdate(query);
    }

    /**generates new customer record in database by grabbing fields and converting them to strings. The strings are appended to a query string
     * which is then pushed to the database*/
    public static void pushNewCustomer(Customer customer) throws SQLException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date createDateFormatted = Date.from(customer.getObjectModify().getCreateDate());
        Date lastUpdateFormatted = Date.from(customer.getObjectModify().getLastUpdate());

        String customerId = String.valueOf(customer.getCustomerId());
        String customerName = customer.getCustomerName();
        String address = customer.getAddress();
        String postal = customer.getPostalCode();
        String phone = customer.getPhone();
        String createDate = simpleDateFormat.format(createDateFormatted);
        String createdBy = customer.getObjectModify().getCreatedBy();
        String lastUpdate = simpleDateFormat.format(lastUpdateFormatted);
        String lastUpdatedBy = customer.getObjectModify().getLastUpdateBy();
        String divisionId = String.valueOf(customer.getDivisionId());

        String query = "INSERT INTO client_schedule.customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES ('" + customerId + "', '" + customerName + "', '" +  address + "', '" + postal + "', '" + phone + "', '" + createDate + "', '" + createdBy + "', '" + lastUpdate + "', '" + lastUpdatedBy + "', '" + divisionId + "');";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);

        System.out.println("Database customer table updated successfully");
    }

    /**updates existing customer record in database by grabbing fields and converting them to strings. The strings are appended to a query string
     * which also locates the existing record and then the query is pushed, updating the fields to the new ones*/
    public static void updateExistingCustomer(Customer customer) throws SQLException, ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date lastupdate= Date.from(customer.getObjectModify().getLastUpdate());

        String lastUpdateFormatted = simpleDateFormat.format(lastupdate);

        String query = "UPDATE customers SET Customer_Name ='" + customer.getCustomerName() + "', Address ='" + customer.getAddress() + "', Postal_Code='" + customer.getPostalCode() + "', Last_Update ='" + lastUpdateFormatted + "', Last_Updated_By ='" + customer.getObjectModify().getLastUpdateBy() + "' WHERE Customer_ID = '" + customer.getCustomerId()+"';";
        Statement statement = connection.createStatement();

        System.out.println(query);
        statement.executeUpdate(query);
    }

    public static void deleteAppointment(Appointment appointment) throws SQLException {
        String query ="DELETE FROM appointments WHERE Appointment_ID=" + appointment.getAppointmentId();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    public static void deleteCustomer(Customer customer) throws SQLException {
        String query ="DELETE FROM customers WHERE Customer_ID=" + customer.getCustomerId();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    /**@param tableName specifies desired table name and returns ResultSet of the table*/
    public static ResultSet retrieveTable(String tableName) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + tableName;
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }
}
