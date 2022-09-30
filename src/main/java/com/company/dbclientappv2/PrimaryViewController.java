package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.ResourceBundle;

import static helper.JDBC.connection;

public class PrimaryViewController implements Initializable {
    public TableView appointmentView = new TableView();
    public TableColumn title, description, location, type, start, end;
    public Text appointmentsTitle;
    LocalDate nowDateTime = LocalDate.now();
    int weekValue;

    // TODO: 9/26/22 add new fxml to create new appointment
    public void handleNewAppointmentBtn(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-appointment-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Appointment");
        stage.setScene(scene);
        stage.show();
    }

    // TODO: 9/28/22 Check if filtering methods work after newappt is done
    public void toggleMonthView(){
        appointmentsTitle.setText(String.valueOf(nowDateTime.getMonth()));
        Timestamp startTime  = Timestamp.valueOf(nowDateTime.withDayOfMonth(1).atStartOfDay());
        Timestamp endTime  = Timestamp.valueOf(nowDateTime.withDayOfMonth(nowDateTime.getMonth().length(nowDateTime.isLeapYear())).atTime(11,59,59));
        appointmentView.setItems(AppointmentsRegistry.getSelectedAppointments(startTime, endTime));
        appointmentView.refresh();
    }
    public void handleWeekViewBtn(ActionEvent actionEvent){
        Timestamp startTime  = Timestamp.valueOf(nowDateTime.withDayOfMonth(nowDateTime.getDayOfMonth()).atStartOfDay());
        Timestamp endTime  = Timestamp.valueOf(nowDateTime.withDayOfMonth(nowDateTime.getDayOfMonth()+6).atTime(11,59,59));
        appointmentView.setItems(AppointmentsRegistry.getSelectedAppointments(startTime, endTime));
        appointmentView.refresh();
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
        toggleMonthView();
        generateTable();


    }
}
