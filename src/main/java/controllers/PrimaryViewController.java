package controllers;

import com.company.dbclientappv2.Appointment;
import com.company.dbclientappv2.AppointmentList;
import com.company.dbclientappv2.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.ResourceBundle;

public class PrimaryViewController implements Initializable {
    public TableView appointmentView = new TableView();
    public TableColumn appointment_ID, title, description, location, contact, type, start, end, customerId, userId;
    public Text appointmentsTitle;
    LocalDate nowDateTime = LocalDate.now();
    Calendar calendar = Calendar.getInstance();
    static int currentMonthValue;
    static int currentWeekValue;
    int selectedDayValue, originalDay, originalMonth;
    public Button nextBtn, previousBtn;
    boolean monthViewTrue;
    ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();


    // TODO: 9/26/22 add new fxml to create new appointment
    public void handleNewAppointmentBtn(ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-appointment-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("New Appointment");
            stage.setScene(scene);
            stage.show();
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if(monthViewTrue) {
                    setMonthView();
                } else {
                    setWeekView();
                }
                appointmentView.refresh();
                System.out.println("Table successfully updated");
            }
        });
    }


    // TODO: 9/28/22 Check if filtering methods work after newappt is done
    public void nextMonth(ActionEvent actionEvent){
        if(currentMonthValue < 12) {
            currentMonthValue++;
            setMonthView();
        }
    }
    public void previousMonth(ActionEvent actionEvent){
        if(currentMonthValue > 1) {
            currentMonthValue--;
            setMonthView();
        }
    }
    public void nextWeek(ActionEvent actionEvent){
        selectedDayValue += 7;
        System.out.println(selectedDayValue);
        setWeekView();
    }
    public void previousWeek(ActionEvent actionEvent){
        selectedDayValue -= 7;
        System.out.println(selectedDayValue);
        setWeekView();
    }

    public void setMonthView(){
        monthViewTrue = true;
        previousBtn.setOnAction(this::previousMonth);
        nextBtn.setOnAction(this::nextMonth);
        selectedAppointments.clear();
        appointmentsTitle.setText(String.valueOf(Month.of(currentMonthValue)));
        Instant startTime  = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(1).atStartOfDay()).toInstant(ZoneOffset.UTC);
        Instant endTime  = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(Month.of(currentMonthValue).length(nowDateTime.isLeapYear())).atTime(LocalTime.MAX)).toInstant(ZoneOffset.UTC);
        selectedAppointments.addAll(AppointmentList.getSelectedAppointments(startTime, endTime));
        appointmentView.setItems(selectedAppointments);
        appointmentView.refresh();
    }
    public void setWeekView(){
        monthViewTrue = false;
        previousBtn.setOnAction(this::previousWeek);
        nextBtn.setOnAction(this::nextWeek);
        selectedAppointments.clear();
        int leftDays = Calendar.FRIDAY - currentWeekValue;
        Instant startTime  = (nowDateTime.withMonth(originalMonth).withDayOfMonth(selectedDayValue).atStartOfDay()).toInstant(ZoneOffset.UTC);
        Instant endTime = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(selectedDayValue + leftDays).atTime(LocalTime.MAX)).toInstant(ZoneOffset.UTC);
        appointmentsTitle.setText(startTime.atZone(ZoneId.systemDefault()).getMonth().toString());
        selectedAppointments.addAll(AppointmentList.getSelectedAppointments(startTime, endTime));
        appointmentView.setItems(selectedAppointments);
        appointmentView.refresh();
//        selectedAppointments.addAll(AppointmentList.getSelectedAppointments(startTime, endTime));
//        appointmentView.setItems(selectedAppointments);
//        appointmentView.refresh();
   }
   public void setAllItems(){
       appointmentsTitle.setText("All Appointments");
       appointmentView.setItems(AppointmentList.allAppointments);
       appointmentView.refresh();
   }
    public void generateTable(){
        appointment_ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }
    public void handleRefreshBtn(ActionEvent actionEvent){
        appointmentView.refresh();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Instant instant = Instant.now();
        AppointmentList.getAllAppointments();
        currentMonthValue = LocalDate.now().getMonthValue();
        currentWeekValue = nowDateTime.getDayOfWeek().getValue();
        selectedDayValue = nowDateTime.getDayOfMonth();
        originalMonth = currentMonthValue;
        originalDay = selectedDayValue;
        Appointment newAppointment = new Appointment(1, "New Appointment", "description", "France", "Test appointment", Instant.now(), Instant.now().plus(10, ChronoUnit.MINUTES), 1, 1);
        AppointmentList.addNewAppointment(newAppointment);
        setMonthView();
        generateTable();



        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        int i= 0;
        for(Appointment appointment : selectedAppointments) {
            if (appointment.getStartTime().isBefore(instant.plus(15, ChronoUnit.MINUTES)) && appointment.getStartTime().isAfter(instant) || appointment.getStartTime().equals(instant)) {
                alert.setContentText("Upcoming appointment is scheduled at " + appointment.getStartTime());
                i++;
                alert.showAndWait();
            }
        }

        if(i<1) {
            alert.setContentText("No upcoming appointments");
            alert.showAndWait();
        }
    }

}

