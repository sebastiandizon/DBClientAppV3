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

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.*;

import java.time.temporal.ValueRange;
import java.util.ConcurrentModificationException;
import java.util.ResourceBundle;


public class ModifyAppointmentController implements InstanceHandling, ControllerFunction, Initializable {
    Appointment appointment = PrimaryViewController.selectedAppointment;
    public TextField appointmentId, Title, Description, Location, Type;
    public DatePicker StartDate, EndDate;
    public Button saveBtn;
    public Spinner<Integer> StartHour, StartMinute, EndHour, EndMinute;
    public ComboBox CustomerID, ContactID;
    String errorMsg;
    /**Grabs user entered text and inputs from text fields and combo boxes and creates an appointment object which is used to modify the
     * corresponding existing appointment in AppointmentList and is then queried into the appointments table in the database*/
    @Override
    public void setInstance() {
        errorMsg = "";
        ObservableList<Control> controlList = FXCollections.observableArrayList();
        controlList.addAll(appointmentId, Title, Description, Location, Type, StartDate, StartHour, StartMinute, EndDate, EndHour, EndMinute, CustomerID, ContactID);
        try {
            while (true) {
                for (Control control : controlList) {
                    checkControl(control);
                }
                String title = Title.getText();
                String desc = Description.getText();
                String location = Location.getText();
                String type = Type.getText();

                ZonedDateTime startRange = ((StartDate.getValue().atTime(StartHour.getValue(), StartMinute.getValue())).atZone(JDBC.getUserTimeZone().toZoneId()));
                ZonedDateTime endRange = (EndDate.getValue().atTime(EndHour.getValue(), EndMinute.getValue()).atZone(JDBC.getUserTimeZone().toZoneId()));
                checkTimes(startRange, endRange);

                int selectedCustomerId = Integer.valueOf(CustomerID.getSelectionModel().getSelectedItem().toString());
                int selectedContactId = Integer.valueOf(ContactID.getSelectionModel().getSelectedItem().toString());
                Appointment newAppointment = new Appointment(Main.appointmentId, title, desc, location, type, startRange, endRange, selectedCustomerId, selectedContactId, Users.userId);
                if (checkOverlap(newAppointment)) {
                    AppointmentList.updateApointment(AppointmentList.getIndex(PrimaryViewController.selectedAppointment), newAppointment);
                } else {
                    errorMsg = errorMsg + "One or more appointments overlap";
                    throw new ConcurrentModificationException("Appointments are overlapping");
                }
                closeStage();
                break;
            }
        } catch (NullPointerException n) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Appointment");
            alert.setTitle("Appointment creation error");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        } catch (ConcurrentModificationException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Appointment");
            alert.setTitle("Appointment creation error");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**Checks controls for input validation*/
    @Override
    public boolean checkControl(Control control) {
        if (control instanceof TextField) {
            if (((TextField) control).getText() == "") {
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
        ObservableList<Appointment> temp = FXCollections.observableArrayList();
        temp.addAll(AppointmentList.getAllAppointments());
        System.out.println(AppointmentList.getIndex(PrimaryViewController.selectedAppointment));
        temp.remove(PrimaryViewController.selectedAppointment);
        for(Appointment existingAppointments : temp){
            if(existingAppointments.getStartTime().isBefore(appointment.getEndTime()) && existingAppointments.getEndTime().isAfter(appointment.getEndTime()) ||
                    existingAppointments.getStartTime().isBefore(appointment.getEndTime()) && existingAppointments.getEndTime().isAfter(appointment.startTime) ||
                    existingAppointments.getStartTime().equals(appointment.getStartTime()) && existingAppointments.getEndTime().equals(appointment.getEndTime())){
                return false;
            }
        }
        return true;

    }
    /**Checks if the time of the appointment will conflict with business hours*/
    public boolean checkTimes(ZonedDateTime start, ZonedDateTime end){
        ValueRange hourRange = ValueRange.of(8, 22);
        if(!start.isBefore(end)){
            errorMsg = errorMsg + "Start must be before end\n";
        }
        if(!(hourRange.isValidValue(start.getHour()) && hourRange.isValidValue(end.getHour()))){
            errorMsg = errorMsg + "Hours must be between 8AM and 10PM\n";
            return false;
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

        appointmentId.setText(String.valueOf(appointment.getAppointmentId()));
        Title.setText(appointment.getTitle());
        Description.setText(appointment.getDescription());
        Location.setText(appointment.getLocation());
        Type.setText(appointment.getType());
        CustomerID.setValue(appointment.getCustomerId());
        ContactID.setValue(appointment.getContactId());

        SpinnerValueFactory<Integer> startHourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        SpinnerValueFactory<Integer> endHourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);

        SpinnerValueFactory<Integer> startMinuteValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> endMinuteValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);

        startHourValue.setValue(appointment.getStartTime().getHour());
        endHourValue.setValue(appointment.getEndTime().getHour());
        startMinuteValue.setValue(appointment.getStartTime().getMinute());
        endMinuteValue.setValue(appointment.getEndTime().getMinute());

        StartHour.setValueFactory(startHourValue);
        StartMinute.setValueFactory(startMinuteValue);
        EndHour.setValueFactory(endHourValue);
        EndMinute.setValueFactory(endMinuteValue);

        StartDate.setValue(appointment.getStartTime().toLocalDate());
        EndDate.setValue(appointment.getEndTime().toLocalDate());


        try {
            CustomerID.setItems(DatabaseQueries.getCustomerIds());
            ContactID.setItems(DatabaseQueries.getContactIds());
        } catch (SQLException se) {
        }



    }
    }