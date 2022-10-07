package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ResourceBundle;

import static helper.JDBC.connection;

public class AppointmentList implements Initializable {
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
                Instant start = rs.getTimestamp("Start").toInstant();
                Instant end = rs.getTimestamp("End").toInstant();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                allAppointments.add(new Appointment(apptId, title, description, location, type, start, end, customerId, contactId));
                System.out.println("Added Appointment");
                System.out.println(start + " " + end);
            }
        } catch (SQLException e) {e.printStackTrace();}
        return allAppointments;
    }

    public static ObservableList<Appointment> getSelectedAppointments(Instant start, Instant end) {
        ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();
        System.out.println(start);
        System.out.println(end);
        for(Appointment appointment : allAppointments){
            System.out.println("Appointment start time: " +appointment.getStartTime());
            System.out.println("Appointment end time: " +appointment.getEndTime());
            if(!(appointment.getStartTime().isBefore(start) || appointment.getStartTime().isAfter(end) && appointment.getEndTime().isBefore(start) || appointment.getEndTime().isAfter(end))) {
                selectedAppointments.add(appointment);
                System.out.println("Added appointment "+ appointment.getTitle());
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
                    + ", " + appointment.getStartTime() + ", " + appointment.getEndTime() + ", " + appointment.getUserId() + ", " + appointment.getContactId() + ")";
            System.out.println(query);

        } catch (SQLException e) {e.printStackTrace();}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}