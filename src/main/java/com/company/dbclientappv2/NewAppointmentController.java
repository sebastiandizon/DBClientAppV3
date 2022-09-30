package com.company.dbclientappv2;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class NewAppointmentController implements AppointmentProcessing, Initializable {
    public TextField titleInput, descInput, locationInput, typeInput;
    public DatePicker startDateInput, endDateInput;


    public Spinner<Integer> startHour;


    // TODO: 9/28/22 fix spinner, retrieve text to turn into new Appointment
    public int appointmentId;

    @Override
    public void generateAppointment(){
        //validate fields - if true ->
        String title = titleInput.toString();
        String desc = descInput.toString();
        String location = locationInput.toString();
        String type = typeInput.toString();
        Timestamp start = Timestamp.valueOf(startDateInput.getValue().atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDateInput.getValue().atStartOfDay());
        Appointment newAppointment = new Appointment(42343, title, desc, location, type, start, end, 1, 1);
    }

    @Override
    public void validateAppointment() {
        //check fields not null. if null throw error
        //check time overlap if overlapping throw error
        //check start time is earlier than end time
        //check
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24);
        valueFactory.setValue(LocalTime.now().getHour());
        startHour.setValueFactory(valueFactory);
    }
}
