package controllers;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lists.AppointmentList;
import lists.ContactList;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static helper.JDBC.connection;

public class ReportViewController extends PrimaryViewController implements Initializable  {
    ObservableList<Appointment> reportList = FXCollections.observableArrayList();

    public TableView scheduleView = new TableView<>();
    public TableColumn appointment_ID, title, description, location, contact, type, start, end, customerId, userId;
    public Text totalApptsTxt;
    public ComboBox<Integer> contactCombo;

//    public void getContactScheduleList() throws SQLException {
//        Statement statement = connection.createStatement();
//        String query = "SELECT Appointment_ID, Title, Type, Start, End, Customer_ID From client_schedule.appointments Where Customer_ID = " + contactCombo.getSelectionModel().getSelectedItem();
//        System.out.println(query);
//        ResultSet rs = statement.executeQuery(query);
//        while(rs.next()){
//
//
//        }
//    }
    public void setContactSchedule(){
        reportList.clear();
        for(Appointment appointment : AppointmentList.getAllAppointments()){
            if(appointment.getContactId() == contactCombo.getSelectionModel().getSelectedItem()){
                System.out.println(appointment.getAppointmentId());
                reportList.add(appointment);
            }
        }
        totalApptsTxt.setText("Total appointments: " + reportList.size());
        scheduleView.setItems(reportList);
        scheduleView.refresh();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointment_ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactCombo.setValue(1);
        setContactSchedule();

        scheduleView.setItems(reportList);


        contactCombo.setItems(ContactList.getContactIdList());

    }
}
