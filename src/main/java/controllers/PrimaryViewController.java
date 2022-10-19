package controllers;

import com.company.dbclientappv2.Appointment;
import helper.JDBC;
import javafx.scene.Parent;
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
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrimaryViewController implements Initializable {
    public AnchorPane primaryView;
    public TableView appointmentView = new TableView();
    public TableColumn appointment_ID, title, description, location, contact, type, start, end, customerId, userId;
    public MenuButton viewMenu;
    public static Appointment selectedAppointment;
    public Button modifyBtn;
    public Text appointmentsTitle, reportTxt, typeCountTxt;
    public TextField typeCountField;
    public Button nextBtn, previousBtn;

    LocalDate nowDateTime = LocalDate.now();

    static int currentMonthValue;
    static int currentWeekValue;
    int selectedDayValue, originalDay, originalMonth;
    int selectedTableViewValue =1, appointmentSize;
    String nextApptName;
    String reportString;
    int setMonthViewCount;
    int setWeekViewCount;
    static ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();
    /**Loads scene with new-appointment.fxml to create a new appointment*/
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
    /**Loads scene with modify-appointment.fxml to modify an existing appointment*/
    public void handleModifyAppointment(ActionEvent actionEvent) throws IOException{
        selectedAppointment = (Appointment)appointmentView.getSelectionModel().getSelectedItem();
        if(selectedAppointment==null){
            return;
        }
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modify-appointment-view.fxml"));
        Parent root = loader.load();

        ModifyAppointmentController modifyAppointmentController = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modify Appointment");
        stage.setScene(scene);
        stage.showAndWait();
        appointmentView.refresh();
    }
    public void handleRemoveBtn(ActionEvent actionEvent) throws SQLException {
        Appointment appointment = (Appointment) appointmentView.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the selected appointment? \nID: " + appointment.getAppointmentId() + "\nType: " + appointment.getType());
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
    public void handleReportBtn(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("report-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Report");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**Filters appointments by month and changes arrows to toggle through months*/
    public void setMonthView(){
        setMonthViewCount = 0;
        selectedTableViewValue = 1;
        previousBtn.setOnAction(this::previousMonth);
        nextBtn.setOnAction(this::nextMonth);
        selectedAppointments.clear();
        appointmentsTitle.setText(String.valueOf(Month.of(currentMonthValue)));
        ZonedDateTime startTime  = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(1).atStartOfDay()).atZone(JDBC.getUserTimeZone().toZoneId());
        ZonedDateTime endTime  = (nowDateTime.withMonth(currentMonthValue).withDayOfMonth(Month.of(currentMonthValue).length(nowDateTime.isLeapYear())).atTime(LocalTime.MAX)).atZone(JDBC.getUserTimeZone().toZoneId());
        selectedAppointments.addAll(AppointmentList.getSelectedAppointments(startTime, endTime));
        setMonthViewCount = selectedAppointments.size();
        appointmentView.setItems(selectedAppointments);
        appointmentView.refresh();
        generateReport();
    }
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

    /**Filters appointments by week and changes arrows to toggle through weeks*/
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
        generateReport();
    }
    public void previousWeek(ActionEvent actionEvent){
        selectedDayValue -= 7;
        System.out.println(selectedDayValue);
        setWeekView();
    }


    /**Shows all appointments*/
    public void setAllItems(){
        selectedTableViewValue = 3;
        selectedAppointments.clear();
        selectedAppointments.setAll(AppointmentList.getAllAppointments());
        appointmentsTitle.setText("All Appointments");
        appointmentView.setItems(selectedAppointments);
        appointmentView.refresh();
        generateReport();
   }
   /**Sets table view and other buttons and adds their functionality*/
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
        MenuItem contactsView = new MenuItem("Customer View");
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
    /**Refreshes table view with specified filter type*/
    public void refreshTable(){
        if(selectedTableViewValue == 1 ){
            setMonthView();
        } else if(selectedTableViewValue == 2){
            setWeekView();
        } else if(selectedTableViewValue ==3){
            setAllItems();
        }
    }
    public void generateReport(){
        try{
       appointmentSize = selectedAppointments.size();
            nextApptName = selectedAppointments.get(appointmentSize-1).getTitle();
        }catch (IndexOutOfBoundsException e) {
            reportString = "No appointments. No upcomming uppointments";
        }
        int monthCount = 0;
        for(Appointment appointment : AppointmentList.getAllAppointments()){
            if(appointment.getStartTime().getMonthValue() == currentMonthValue || appointment.getEndTime().getMonthValue() == currentMonthValue){
                monthCount++;
            }
        }
        reportString ="Number of appointments in current month: " + setMonthViewCount +
                "\nNumber of appointments in current selected view: " + appointmentSize +
                "\nLast appointment title: " + nextApptName;
        reportTxt.setText(reportString);
    }
    public void setTypeCount(){
        typeCountTxt.setDisable(false);
        int i=0;
        for(Appointment appointment : selectedAppointments){
            if(appointment.getType().contains(typeCountField.getText())){
                i++;
            }
        }
        typeCountTxt.setText("Total ammount of " + typeCountField.getText() + " appointments in current view: " + i);
    }
    public void logOut(){
        Stage stage = (Stage)primaryView.getScene().getWindow();
        stage.close();
        JDBC.closeConnection();
    }
    public void setContactSchedule() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("contact-schedule-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Contact Schedule");
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedAppointments.clear();
        currentMonthValue = LocalDate.now().getMonthValue();
        currentWeekValue = nowDateTime.getDayOfWeek().getValue();
        selectedDayValue = nowDateTime.getDayOfMonth();
        originalMonth = currentMonthValue;
        originalDay = selectedDayValue;

        setMonthView();
        setUI();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcomming appointments");
        alert.setHeaderText("Appointments");
        int i = 0;
        for (Appointment appointment : selectedAppointments) {
            if (appointment.getStartTime().isBefore(ZonedDateTime.now().plusMinutes(15)) && appointment.getStartTime().isAfter(ZonedDateTime.now()) || appointment.getStartTime().equals(ZonedDateTime.now())) {
                alert.setContentText("Upcoming appointment with id: "+ appointment.getAppointmentId() + " is scheduled at " + appointment.getStartTime());
                i++;
                alert.showAndWait();
            }

        }
        if (i < 1) {
            alert.setContentText("No upcoming appointments");
            alert.showAndWait();
        }

        //String reportString = "Number of appointments: " + appointmentSize + "\n Next appointment title:" + nextApptName;
//        reports.setText(reportString);
    }
}

