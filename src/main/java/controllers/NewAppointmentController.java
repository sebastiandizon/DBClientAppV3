package controllers;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.AppointmentList;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;

public class NewAppointmentController implements AppointmentProcessing, Initializable {
    public TextField Title, Description, Location, Type;
    public DatePicker StartDate, EndDate;
    public Button saveBtn;
    public Spinner<Integer> StartHour, StartMinute, EndHour, EndMinute;
    public ComboBox CustomerID, ContactID;
    String errorMsg;

    @Override
    public void generateAppointment() {
        errorMsg = "";
        ObservableList<Control> controlList = FXCollections.observableArrayList();
        controlList.addAll(Title, Description, Location, Type, StartDate, StartHour, StartMinute, EndDate, EndHour, EndMinute, CustomerID, ContactID);
        try{
            while(true) {
                for (Control control : controlList) {
                    checkControl(control);
                }
                String title = Title.getText();
                String desc = Description.getText();
                String location = Location.getText();
                String type = Type.getText();

                Instant startRange = (StartDate.getValue().atTime(StartHour.getValue(), StartMinute.getValue()).toInstant(ZoneOffset.UTC));
                Instant endRange = (StartDate.getValue().atTime(StartHour.getValue(), StartMinute.getValue())).toInstant(ZoneOffset.UTC);

                int selectedCustomerId = Integer.valueOf(CustomerID.getSelectionModel().getSelectedItem().toString());
                int selectedContactId = Integer.valueOf(ContactID.getSelectionModel().getSelectedItem().toString());
                Appointment newAppointment = new Appointment(AppointmentList.allAppointments.size() + 1, title, desc, location, type, startRange, endRange, selectedCustomerId, selectedContactId);
                AppointmentList.addNewAppointment(newAppointment);
                closeStage();
                break;
            }
        } catch (NullPointerException n){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Appointment");
            alert.setTitle("Appointment creation error");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }

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

    public boolean checkOverlap(Instant start, Instant end) {

    return true;
    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }



        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
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