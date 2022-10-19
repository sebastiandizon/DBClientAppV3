package controllers;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.Main;
import com.company.dbclientappv2.Users;
import helper.ControllerFunction;
import helper.JDBC;
import lists.AppointmentList;
import helper.InstanceHandling;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.time.*;

import java.time.temporal.ValueRange;
import java.util.ConcurrentModificationException;
import java.util.ResourceBundle;

public class NewAppointmentController implements InstanceHandling, ControllerFunction, Initializable {
    public TextField appointmentId, Title, Description, Location, Type;
    public DatePicker StartDate, EndDate;
    public Button saveBtn;
    public Spinner<Integer> StartHour, StartMinute, EndHour, EndMinute;
    public ComboBox CustomerID, ContactID;
    String errorMsg;

    /**Checks fields and creates Appointment object with user entered parameters. Passes appointment to Appointment list which is then added to database
     * in appointments table*/
    @Override
    public void setInstance() {
        errorMsg = "";
        ObservableList<Control> controlList = FXCollections.observableArrayList();
        controlList.addAll(Title, Description, Location, Type, StartDate, StartHour, StartMinute, EndDate, EndHour, EndMinute, CustomerID, ContactID);
        try {
            while (true) {
                ObservableList<Boolean> valueCheckList = FXCollections.observableArrayList();
                for (Control control : controlList) {
                    valueCheckList.add(Boolean.valueOf(checkControl(control)));
                }
                for(boolean bool : valueCheckList){
                    if(bool==false){
                        throw new NullPointerException("Value is null");
                    }
                }

                String title = Title.getText();
                String desc = Description.getText();
                String location = Location.getText();
                String type = Type.getText();

                ZonedDateTime startRange = ((StartDate.getValue().atTime(StartHour.getValue(), StartMinute.getValue())).atZone(JDBC.getUserTimeZone().toZoneId()));
                ZonedDateTime endRange = (EndDate.getValue().atTime(EndHour.getValue(), EndMinute.getValue()).atZone(JDBC.getUserTimeZone().toZoneId()));


                int selectedCustomerId = Integer.valueOf(CustomerID.getSelectionModel().getSelectedItem().toString());
                int selectedContactId = Integer.valueOf(ContactID.getSelectionModel().getSelectedItem().toString());
                Appointment newAppointment = new Appointment(Main.appointmentId, title, desc, location, type, startRange, endRange, selectedCustomerId, selectedContactId, Users.userId);
                checkTimes(newAppointment.getStartTime(), newAppointment.getEndTime());
                if (checkOverlap(newAppointment)) {
                    AppointmentList.addNewAppointment(newAppointment);
                } else {
                    errorMsg = errorMsg + "One or more appointments overlap";
                    throw new IllegalArgumentException("Appointments are overlapping");
                }
                closeStage();
                break;
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Appointment");
            alert.setTitle("Appointment creation error");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }
    /**Checks controls for input validation*/
    @Override
    public boolean checkControl(Control control) {
        if (control instanceof TextField) {
            if (((TextField) control).getText().trim().isEmpty() || ((TextField) control).getText() ==null ) {
                System.out.println(control.getId() + " is null");
                errorMsg = errorMsg + control.getId() + " cannot be null\n";
                return false;
            }
        } else if (control instanceof Spinner) {
            if (((Spinner<?>) control).getValue() == null) {
                System.out.println(control.getId() + " is null");
                errorMsg = errorMsg + control.getId() + " cannot be null\n";
                return false;
            }
        } else if (control instanceof DatePicker) {
            if (((DatePicker) control).getValue() == null) {
                System.out.println(control.getId() + " is null");
                errorMsg = errorMsg + control.getId() + " cannot be null\n";
                return false;
            }
        } else if (control instanceof ComboBox<?>) {
            if (((ComboBox<?>) control).getValue() == null) {
                System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                return false;
            }
        }
        return true;
    }
    /**Checks if appointment will overlap with any other existing appointments*/
    public boolean checkOverlap(Appointment appointment) {
        for(Appointment existingAppointments : AppointmentList.allAppointments){
            if(existingAppointments.getStartTime().isBefore(appointment.getEndTime()) && existingAppointments.getEndTime().isAfter(appointment.getEndTime()) ||
                    existingAppointments.getStartTime().isBefore(appointment.getEndTime()) && existingAppointments.getEndTime().isAfter(appointment.startTime) ||
                    existingAppointments.getStartTime().equals(appointment.getStartTime()) && existingAppointments.getEndTime().equals(appointment.getEndTime())){
            return false;
            }
        }
    return true;

    }
    /**Checks if the time of the appointment will conflict with business hours*/
    public boolean checkTimes(ZonedDateTime start, ZonedDateTime end) throws IllegalArgumentException{
        ValueRange hourRange = ValueRange.of(8, 22);
        if(!start.isBefore(end)){
            errorMsg = errorMsg + "Start must be before end\n";
        }
        if(!(hourRange.isValidValue(start.getHour()) && hourRange.isValidValue(end.getHour()))){
            errorMsg = errorMsg + "Hours must be between 8AM and 10PM\n";
            throw new IllegalArgumentException("Hours are not between 8AM and 10PM");
        }
        return true;
    }
    /**Grabs stage window and closes it*/
    @Override
    public void closeStage() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            Main.appointmentId = AppointmentList.getAllAppointments().get(AppointmentList.getAllAppointments().size()-1).getAppointmentId();
            Main.appointmentId++;
            appointmentId.setText(String.valueOf(Main.appointmentId));
            SpinnerValueFactory<Integer> startHourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            SpinnerValueFactory<Integer> endHourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);

            SpinnerValueFactory<Integer> startMinuteValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            SpinnerValueFactory<Integer> endMinuteValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);

            startHourValue.setValue(LocalTime.now().getHour());
            endHourValue.setValue(LocalTime.now().getHour());
            startMinuteValue.setValue(LocalTime.now().getMinute());
            endMinuteValue.setValue(LocalTime.now().getMinute());
            StartDate.setValue(LocalDate.now());
            EndDate.setValue(LocalDate.now());

            StartHour.setValueFactory(startHourValue);
            EndHour.setValueFactory(endHourValue);
            StartMinute.setValueFactory(startMinuteValue);
            EndMinute.setValueFactory(endMinuteValue);

            try {
                CustomerID.setItems(DatabaseQueries.getCustomerIds());
                ContactID.setItems(DatabaseQueries.getContactIds());
            } catch (SQLException se) {
            }

        }
    }