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

    public ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }

    public static ObservableList<Appointment> getSelectedAppointments(Timestamp start, Timestamp end){
        ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();
        for(Appointment appointment : allAppointments){
            if(appointment.startTime.after(start) || appointment.startTime.equals(start) && appointment.endTime.equals(end) || appointment.endTime.before(end)) {
                selectedAppointments.add(appointment);
            }
        }
        return selectedAppointments;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                allAppointments.add(new Appointment(apptId, title, description, location, type, start, end, customerId, contactId));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }
}
