package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static helper.JDBC.connection;


public class PrimaryViewController implements Initializable {
    public TableView appointmentView = new TableView();
    public TableColumn title, description, location, type, start, end;
    ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

    public void retrieveExistingAppointments(){
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM client_schedule.appointments";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
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
                System.out.println(apptId + title + description + location + type + start + end + customerId + userId + contactId);
                allAppointments.add(new Appointments(apptId, title, description, location, type, start, end, customerId, userId, contactId));
            }
        }catch (SQLException e) {e.printStackTrace();}
    }
    public void generateTable(){
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        retrieveExistingAppointments();
        appointmentView.setItems(allAppointments);
        generateTable();


    }
}
