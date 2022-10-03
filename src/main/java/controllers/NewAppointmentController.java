package controllers;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.AppointmentsRegistry;
import com.company.dbclientappv2.Users;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class NewAppointmentController implements AppointmentProcessing, Initializable {
    public TextField titleInput, descInput, locationInput, typeInput;
    public DatePicker startDateInput, endDateInput;
    public Button saveBtn;


    public Spinner<Integer> startHour, endHour, startMin, endMin;


    // TODO: 9/28/22 fix spinner, retrieve text to turn into new Appointment
    public int appointmentId;

    @Override
    public void generateAppointment(){
        //validate fields - if true ->
        String title = titleInput.getText();
        String desc = descInput.getText();
        String location = locationInput.getText();
        String type = typeInput.getText();
        Timestamp startRange = Timestamp.valueOf(startDateInput.getValue().atStartOfDay());
        Timestamp endRange = Timestamp.valueOf(endDateInput.getValue().atStartOfDay());
        Appointment newAppointment = new Appointment(AppointmentsRegistry.allAppointments.size()+1, title, desc, location, type, startRange, endRange, 1, Users.userID, 1);
        AppointmentsRegistry.addNewAppointment(newAppointment);
        System.out.println("New appointment with id:" + newAppointment.getAppointmentId() + " and type: " + newAppointment.getType());
        closeStage();
    }
    public void closeStage() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public boolean validateAppointment() {
        //check fields not null. if null throw error
        //check time overlap if overlapping throw error
        //check start time is earlier than end time
        //check
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> startHourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        SpinnerValueFactory<Integer> endHourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);

        SpinnerValueFactory<Integer> startMinuteValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> endMinuteValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);

        startHourValue.setValue(LocalTime.now().getHour());
        endHourValue.setValue(LocalTime.now().getHour());
        startMinuteValue.setValue(LocalTime.now().getMinute());
        endMinuteValue.setValue(LocalTime.now().getMinute());

        startHour.setValueFactory(startHourValue);
        endHour.setValueFactory(endHourValue);
        startMin.setValueFactory(startMinuteValue);
        endMin.setValueFactory(endMinuteValue);
    }
}
