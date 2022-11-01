package controllers;

import DAO.AppointmentDAOImpl;
import DAO.CustomerDAOImpl;
import helper.InputFiltering;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Users;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;

import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ValueRange;
import java.util.ResourceBundle;

public class NewAppointmentController implements InputFiltering, Initializable {
    AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    public TextField Title, Description, Location, Type;
    public DatePicker StartDate, EndDate;
    public Spinner<Integer> StartHour, StartMinute, EndHour, EndMinute;
    public ComboBox CustomerID,  ContactID;
    String errorMsg ="";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StartDate.setValue(LocalDate.now());
        EndDate.setValue(LocalDate.now());
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
        try{
            CustomerID.setItems(customerDAO.getCustIds());
            ContactID.setItems(appointmentDAO.getContIDs());
        }catch (SQLException e){e.printStackTrace();}
    }
    public void getNewAppointment(ActionEvent actionEvent) throws SQLException {
        errorMsg = "";
        ObservableList<Control> controls = FXCollections.observableArrayList();
        controls.addAll(Title, Description, Location, Type, StartDate, EndDate, StartHour, StartMinute, EndHour, EndMinute, CustomerID,  ContactID);
        try {
            while (true) {
                filterInputs(controls);
                //get textfields
                String title = Title.getText();
                String description = Description.getText();
                String location = Location.getText();
                String type = Type.getText();

                //construct start time
                LocalDate startLocalDate = LocalDate.of(StartDate.getValue().getYear(), StartDate.getValue().getMonthValue(), StartDate.getValue().getDayOfMonth());
                LocalTime startLocalTime = LocalTime.of(StartHour.getValue(), StartMinute.getValue());
                ZonedDateTime startDateTime = ZonedDateTime.of(startLocalDate, startLocalTime, ZoneId.systemDefault());

                //construct end time
                LocalDate endLocalDate = LocalDate.of(EndDate.getValue().getYear(), EndDate.getValue().getMonthValue(), EndDate.getValue().getDayOfMonth());
                LocalTime endLocalTime = LocalTime.of(EndHour.getValue(), EndMinute.getValue());
                ZonedDateTime endDateTime = ZonedDateTime.of(endLocalDate, endLocalTime, ZoneId.systemDefault());

                //get combo box selected
                int customerId = (int)CustomerID.getSelectionModel().getSelectedItem();
                int contactId = (int)ContactID.getSelectionModel().getSelectedItem();

                //create new appointment
                Appointment appointment = new Appointment();
                appointment.setTitle(title);
                appointment.setDescription(description);
                appointment.setLocation(location);
                appointment.setType(type);
                appointment.setStartTime(startDateTime);
                appointment.setEndTime(endDateTime);
                appointment.setCustomerId(customerId);
                appointment.setContactId(contactId);
                appointment.setUserId(Users.userId);

                //check if it is in business hours
                checkHours(appointment);

                //check if it overlaps
                checkCollision(appointment);

                //save appointment to database
                appointmentDAO.save(appointment);
                System.out.println("Appointment saved");
                Stage stage = (Stage) Type.getScene().getWindow();
                stage.close();
                break;
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Appointment");
            alert.setTitle("Appointment creation error");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }
    @Override
    public boolean filterInputs(ObservableList<Control> controls) {
        int i = 0;
        for (Control control : controls) {
            if (control instanceof TextField) {
                if (((TextField) control).getText().trim().isEmpty() || ((TextField) control).getText() == null) {
                    System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                    i++;
                }
            } else if (control instanceof Spinner) {
                if (((Spinner<?>) control).getValue() == null) {
                    System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                    i++;
                }
            } else if (control instanceof DatePicker) {
                if (((DatePicker) control).getValue() == null) {
                    System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                    i++;
                }
            } else if (control instanceof ComboBox<?>) {
                if (((ComboBox<?>) control).getValue() == null) {
                    System.out.println(control.getId() + " is null");
                    errorMsg = errorMsg + control.getId() + " cannot be null\n";
                    i++;
                }
            }
            if (i < 0) {
                return true;
            }
        }
        return false;
    }
    public void checkCollision(Appointment appointment) throws SQLException{
        System.out.println("Size " + appointmentDAO.selectBetween(appointment).size());
        if(appointmentDAO.selectBetween(appointment).size() > 0) {
            errorMsg = errorMsg + "Appointment overlaps with existing appointments\n";
            throw new IllegalArgumentException("Appointments overlap");
        }
    }

    public boolean checkHours(Appointment appointment){
        ValueRange hourRange = ValueRange.of(8, 22);
        if(!appointment.getStartTime().isBefore(appointment.getEndTime())){
            errorMsg = errorMsg + "Start must be before end\n";
        }
        if(!(hourRange.isValidValue(appointment.getStartTime().getHour()) && hourRange.isValidValue(appointment.getEndTime().getHour()))){
            errorMsg = errorMsg + "Hours must be between 8AM and 10PM\n";
            return false;
        }
        return true;
    }
}