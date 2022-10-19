package lists;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.Contact;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static helper.JDBC.connection;
import static helper.JDBC.getUserTimeZone;

public class AppointmentList implements Initializable {
    String startDate;
    String endDate;


    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    /**@return Retrieves appointments from appointments table in the database and appends them to allAppointments list*/
    public static ObservableList<Appointment> retrieveAppointments() {
        try {
            ResultSet rs = DatabaseQueries.retrieveTable("appointments");
            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Instant start = rs.getTimestamp("Start").toInstant();
                ZonedDateTime zonedStartTime = start.atZone(getUserTimeZone().toZoneId());
                Instant end = rs.getTimestamp("End").toInstant();
                ZonedDateTime zonedEndTime = end.atZone(getUserTimeZone().toZoneId());
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                allAppointments.add(new Appointment(apptId, title, description, location, type, zonedStartTime, zonedEndTime, customerId, contactId, userId));
                System.out.println("Added Appointment");
                System.out.println(start + " " + end);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }
    /**@return ObservableList of all existing appointments*/
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }
    /**filters appointments based on given start and end time
     * @param start ZonedDateTime start time value
     * @param end ZonedDateTime end time value
     * @return  ObservableList of appointments fitting between start and end*/
    public static ObservableList<Appointment> getSelectedAppointments(ZonedDateTime start, ZonedDateTime end) {
        ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();
        System.out.println(start);
        System.out.println(end);
        for (Appointment appointment : allAppointments) {
            System.out.println("Appointment start time: " + appointment.getStartTime());
            System.out.println("Appointment end time: " + appointment.getEndTime());
            if (!(appointment.getStartTime().isBefore(start) || appointment.getStartTime().isAfter(end) && appointment.getEndTime().isBefore(start) || appointment.getEndTime().isAfter(end))) {
                selectedAppointments.add(appointment);
                System.out.println("Added appointment " + appointment.getTitle());
            } else {
                System.out.println("Not added");
            }
        }
        return selectedAppointments;
    }
    /**returns the index of an appointment in the list*/
//    public static ObservableList<Appointment> getSelectedAppointments(Contact contact){
//
//    }
    public static int getIndex(Appointment appointment) {
        return allAppointments.indexOf(appointment);
    }
    /**@param appointment specified appointment is added to allAppointments list and added to database table appointments*/
    public static void addNewAppointment(Appointment appointment) {
        allAppointments.add(appointment);
        try {
            DatabaseQueries.pushNewAppointment(appointment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**@param index used to locate index of existing appointment
     * @param appointment appointment with desired values
     * updates existing appointment at given index with desired values retrieved from appointment parameter*/
    public static void updateApointment(int index, Appointment appointment) throws SQLException, ParseException {
        AppointmentList.getAllAppointments().get(index).setAppointmentId(appointment.getAppointmentId());
        AppointmentList.getAllAppointments().get(index).setTitle(appointment.getTitle());
        AppointmentList.getAllAppointments().get(index).setDescription(appointment.getDescription());
        AppointmentList.getAllAppointments().get(index).setLocation(appointment.getLocation());
        AppointmentList.getAllAppointments().get(index).setType(appointment.getType());
        AppointmentList.getAllAppointments().get(index).setStartTime(appointment.getStartTime());
        AppointmentList.getAllAppointments().get(index).setEndTime(appointment.getEndTime());
        AppointmentList.getAllAppointments().get(index).getModifyRecord().setLastUpdate(appointment.getModifyRecord().getLastUpdate());
        AppointmentList.getAllAppointments().get(index).getModifyRecord().setLastUpdateBy(appointment.getModifyRecord().getLastUpdateBy());
        DatabaseQueries.updateExistingAppointment(appointment);

    }
    /**@param index removes appointment at this index*/
    public static void removeAppointment(int index) throws SQLException{
        DatabaseQueries.deleteAppointment(allAppointments.get(index));
        allAppointments.remove(index);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM client_schedule.appointments;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Instant start = rs.getTimestamp("Start").toInstant();
                ZonedDateTime zonedStartTime = start.atZone(getUserTimeZone().toZoneId());
                Instant end = rs.getTimestamp("End").toInstant();
                ZonedDateTime zonedEndTime = end.atZone(getUserTimeZone().toZoneId());
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                allAppointments.add(new Appointment(apptId, title, description, location, type, zonedStartTime, zonedEndTime, customerId, contactId, userId));
                System.out.println("Added Appointment");
                System.out.println(start + " " + end);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
