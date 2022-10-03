package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static helper.JDBC.connection;

public class AppointmentsRegistry implements Initializable {
    String startDate;
    String endDate;
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAllAppointments(){
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM client_schedule.appointments;";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                allAppointments.add(new Appointment(apptId, title, description, location, type, start, end, customerId, userId, contactId));
                System.out.println("Added Appointment");
            }
        } catch (SQLException e) {e.printStackTrace();}
        return allAppointments;
    }

    public static ObservableList<Appointment> getSelectedAppointments(Timestamp start, Timestamp end) {
        ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();
        for(Appointment appointment : allAppointments){
            if(!(appointment.getStartTime().before(start) || appointment.getEndTime().after(end))) {
                selectedAppointments.add(appointment);
                System.out.println("Added");
            } else {
                System.out.println("Not added");
            }
        }
        return selectedAppointments;
    }
    // TODO: 10/2/22 on appointment add: Add to SQL database
    public static void addNewAppointment(Appointment appointment){
        allAppointments.add(appointment);
        try {
            Statement statement = connection.createStatement();
            String query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" +
                    " VALUES (" + appointment.getTitle() + ", " + appointment.getDescription() + ", " + appointment.getLocation() + ", " + appointment.getType()
                    + ", " + appointment.getStartTime() + ", " + appointment.getEndTime() + ", " + appointment.getUserID() + ", " + appointment.getContactId() + ")";
            System.out.println(query);

        } catch (SQLException e) {e.printStackTrace();}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAllAppointments();
    }
}
