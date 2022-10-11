package controllers;

import com.company.dbclientappv2.Appointment;
import helper.JDBC;
import javafx.scene.layout.AnchorPane;
import lists.AppointmentList;
import com.company.dbclientappv2.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrimaryViewController implements Initializable {
    public AnchorPane primaryView;
    public TableView appointmentView = new TableView();
    public TableColumn appointment_ID, title, description, location, contact, type, start, end, customerId, userId;
    public MenuButton viewMenu;
    ZonedDateTime localizedTime = ZonedDateTime.now(JDBC.getUserTimeZone().toZoneId());

    public Text appointmentsTitle;
    LocalDate nowDateTime = LocalDate.now();
    Calendar calendar = Calendar.getInstance();
    static int currentMonthValue;
    static int currentWeekValue;
    int selectedDayValue, originalDay, originalMonth;
    public Button nextBtn, previousBtn;
    int selectedTableViewValue =1 ;
    static ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();


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
                refreshTable();
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
        if(selectedDayValue+7 < Month.of(currentMonthValue).length(nowDateTime.isLeapYear())) {
            selectedDayValue += 7;
            System.out.println(selectedDayValue);
            setWeekView();
        } else {
            System.out.println("Limit reached");
        }
    }
    public void previousWeek(ActionEvent actionEvent){
        selectedDayValue -= 7;
        System.out.println(selectedDayValue);
        setWeekView();
    }
    public void handleRemoveBtn(ActionEvent actionEvent) {
        Appointment appointment = (Appointment) appointmentView.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the selected appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            AppointmentList.removeAppointment(AppointmentList.getIndex(appointment));
            refreshTable();
            System.out.println("Part removed");
        } else {
            System.out.println("User cancelled remove request");
        }

    }

    public void setMonthView(){
        selectedTableViewValue = 1;
        previousBtn.setOnAction(this::previousMonth);
        nextBtn.setOnAction(this::nextMonth);
        selectedAppointments.clear();
        appointmentsTitle.setText(String.valueOf(Month.of(currentMonthValue)));
        ZonedDateTime startTime  = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(1).atStartOfDay()).atZone(JDBC.getUserTimeZone().toZoneId());
        ZonedDateTime endTime  = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(Month.of(currentMonthValue).length(nowDateTime.isLeapYear())).atTime(LocalTime.MAX)).atZone(JDBC.getUserTimeZone().toZoneId());
        selectedAppointments.addAll(AppointmentList.getSelectedAppointments(startTime, endTime));
        appointmentView.setItems(selectedAppointments);
        appointmentView.refresh();
    }
    public void setWeekView(){
        selectedTableViewValue = 2;
        previousBtn.setOnAction(this::previousWeek);
        nextBtn.setOnAction(this::nextWeek);
        selectedAppointments.clear();
        int leftDays = Calendar.FRIDAY - currentWeekValue;
        ZonedDateTime startTime  = (nowDateTime.withMonth(originalMonth).withDayOfMonth(selectedDayValue).atStartOfDay()).atZone(JDBC.getUserTimeZone().toZoneId());
        ZonedDateTime endTime = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(selectedDayValue + leftDays).atTime(LocalTime.MAX)).atZone(JDBC.getUserTimeZone().toZoneId());
        appointmentsTitle.setText(startTime.getMonth().toString());
        selectedAppointments.addAll(AppointmentList.getSelectedAppointments(startTime, endTime));
        appointmentView.setItems(selectedAppointments);
        appointmentView.refresh();
   }
   public void setAllItems(){
        selectedTableViewValue = 3;
        appointmentsTitle.setText("All Appointments");
        appointmentView.setItems(AppointmentList.allAppointments);
        appointmentView.refresh();
   }
    public void setUI(){
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

        MenuItem appointmentsView = new MenuItem("Appointments View");
        MenuItem contactsView = new MenuItem("Contacts View");
        appointmentsView.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("primary-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage)primaryView.getScene().getWindow();
                stage.setTitle("Appointments");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioException) {ioException.printStackTrace();}
        });
        contactsView.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());
                Stage stage = (Stage)primaryView.getScene().getWindow();
                stage.setTitle("Customers");
                stage.setScene(newScene);
                stage.show();
            } catch (IOException ioException) {ioException.printStackTrace();}
        });
        viewMenu.getItems().addAll(appointmentsView, contactsView);
    }
    public void refreshTable(){
        if(selectedTableViewValue == 1 ){
            setMonthView();
        } else if(selectedTableViewValue == 2){
            setWeekView();
        } else if(selectedTableViewValue ==3){
            setAllItems();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedAppointments.clear();
        Instant instant = Instant.now();
        currentMonthValue = LocalDate.now().getMonthValue();
        currentWeekValue = nowDateTime.getDayOfWeek().getValue();
        selectedDayValue = nowDateTime.getDayOfMonth();
        originalMonth = currentMonthValue;
        originalDay = selectedDayValue;

        setMonthView();
        setUI();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        int i= 0;
        for(Appointment appointment : selectedAppointments) {
            if (appointment.getStartTime().isBefore(instant.plus(15, ChronoUnit.MINUTES).atZone(JDBC.getUserTimeZone().toZoneId())) && appointment.getStartTime().isAfter(instant.atZone(JDBC.getUserTimeZone().toZoneId())) || appointment.getStartTime().equals(instant)) {
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

