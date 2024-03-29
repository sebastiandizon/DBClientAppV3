package controllers;

import helper.JDBC;
import helper.RetrieveMonth;
import helper.RetrieveWeek;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Appointment;
import model.Main;
import DAO.AppointmentDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;



public class MainController implements Initializable {
    AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    public static Appointment selectedAppointment;
    public AnchorPane primaryView;
    public TableView appointmentsTable = new TableView<>();
    public TableColumn appointment_ID, title, description, contact, location, type, start, end, customerId, userId;
    public MenuButton viewMenu;
    static LocalDate currentMonth;
    static LocalDate currentWeek;
    public Button nextBtn, previousBtn;
    public Text viewTitle;
    public RadioButton allItemsBtn;
    /**Initialize method which sets view functionality using methods as well as getting necessary variable values and setting table views*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentMonth = LocalDate.now();
        currentWeek = LocalDate.now();
        MenuItem appointmentsView = new MenuItem("Appointments View");
        MenuItem contactsView = new MenuItem("Customer View");
        /**Lambda expression sets view based on user selected item. User can pick between main and customer and the stage is generated with proper scene. Lambda is useful in changing the
         * action of the button on initialize using JavaFX lambda to make sure that functionality is set properly*/
        appointmentsView.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) primaryView.getScene().getWindow();
                stage.setTitle("Appointments");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        /**Lambda expression sets view based on user selected item. User can pick between main and customer and the stage is generated with proper scene. Lambda is useful in changing the
         * action of the button on initialize using JavaFX lambda to make sure that functionality is set properly*/
        contactsView.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) primaryView.getScene().getWindow();
                stage.setTitle("Customers");
                stage.setScene(newScene);
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        viewMenu.getItems().addAll(appointmentsView, contactsView);
        appointment_ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        try {
            appointmentsTable.setItems(appointmentDAO.getAppointmentMonth(currentMonth));
            viewTitle.setText(String.valueOf(currentMonth.getMonth()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcomming appointments");
        alert.setHeaderText("Appointments");
        //Alerts if appt is within 15 minutes of now

        try {
            if (appointmentDAO.getNextAlert().size() > 0) {
                for(Appointment appointment : appointmentDAO.getNextAlert()) {
                    alert.setContentText("Upcoming appointment with id: " + appointment.getAppointmentId() + " is scheduled from " + appointment.getStartTime() + " to " + appointment.getEndTime());
                    alert.showAndWait();
                }
            } else {
                alert.setContentText("No upcoming appointments");
                alert.showAndWait();
            }
        }catch (SQLException e){}
    }
    /**Generates scene for new appointment*/
    public void handleNewAppointmentBtn(ActionEvent actionEvent) throws IOException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-appointment-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Appointment");
        stage.setScene(scene);
        stage.showAndWait();
        appointmentsTable.setItems(appointmentDAO.getAll());
        allItemsBtn.setSelected(true);

    }
    /**Gets user selected appointment and generates modify appointment scene with selected appointment*/
    public void handleModifyAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        selectedAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("modify-appointment-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
        appointmentsTable.setItems(appointmentDAO.getAll());
        allItemsBtn.setSelected(true);
    }
    /**Sets tableview using lambda expression to show appointments for given month and changes toggles to toggle between months. The lambda expression
     * used improves the code by retrieving the current month and setting a variable which is then passed to the table and the table view is set to that month.
     * The buttons are then set with appropriate action using the setOnAction JavaFX lambda so that the arrow buttons properly change between months*/
    public void setMonthView(ActionEvent actionEvent) throws SQLException{
        RetrieveMonth retrieve = (appointmentsTable, currentMonth) -> {
            currentMonth = LocalDate.now();
            appointmentsTable.setItems(appointmentDAO.getAppointmentMonth(currentMonth));
                viewTitle.setText(String.valueOf(currentMonth.getMonth()));
                nextBtn.setOnAction(actionEvent1 -> {
                    try {
                        nextMonth(actionEvent1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                previousBtn.setOnAction(actionEvent1 -> {
                    try {
                        previousMonth(actionEvent1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                previousBtn.setDisable(false);
                nextBtn.setDisable(false);
            };
        getByMonth(retrieve);
    }
    /**Sets tableview using lambda expression to show appointments for given week and changes toggles to toggle between weeks. The lambda passed improves the code by passing
     * the week value to the method and setting the table view to the proper week, and then setting the buttons to have appropriate functionality using JavaFX setOnAction lambda
     * to allow users to change between weeks rather than months*/
    public void setWeekView(ActionEvent actionEvent) throws SQLException{
        currentWeek = LocalDate.now();
        RetrieveWeek retrieve = (appointmentsTable, currentMonth) -> {
            viewTitle.setText(String.valueOf(currentWeek.getMonth()));
            appointmentsTable.setItems(appointmentDAO.getAppointmentWeek(currentWeek));
            nextBtn.setOnAction(actionEvent1 -> {
                try {
                    nextWeek(actionEvent1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            previousBtn.setOnAction(actionEvent1 -> {
                try {
                    previousWeek(actionEvent1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            previousBtn.setDisable(false);
            nextBtn.setDisable(false);
        };
        getByWeek(retrieve);
    }
    /**Sets tableview to show all existing appointments and disables toggles*/
    public void setAllItems(ActionEvent actionEvent) throws SQLException {
        appointmentsTable.setItems(appointmentDAO.getAll());
        viewTitle.setText("All appointments");
        previousBtn.setDisable(true);
        nextBtn.setDisable(true);
    }
    /**Generates scene that shows customer reports*/
    public void handleReportBtn(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("report-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Report");
        stage.setScene(scene);
        stage.show();
    }
    /**Closes window and terminates connection safely*/
    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage)primaryView.getScene().getWindow();
        JDBC.closeConnection();
        stage.close();
    }
    /**Method for toggling back by month*/
    public void previousMonth(ActionEvent actionEvent) throws SQLException{
        currentMonth = currentMonth.plusMonths(-1);
        System.out.println(currentMonth);
        appointmentsTable.setItems(appointmentDAO.getAppointmentMonth(currentMonth));
        viewTitle.setText(String.valueOf(currentMonth.getMonth()));
    }
    /**Method for toggling forward by month*/
    public void nextMonth(ActionEvent actionEvent) throws SQLException {
        currentMonth = currentMonth.plusMonths(1);
        System.out.println(currentMonth);
        appointmentsTable.setItems(appointmentDAO.getAppointmentMonth(currentMonth));
        viewTitle.setText(String.valueOf(currentMonth.getMonth()));
    }
    /**Method for toggling back by week*/
    public void previousWeek(ActionEvent actionEvent) throws SQLException{
        currentWeek = currentWeek.plusWeeks(-1);
        System.out.println(currentWeek);
        appointmentsTable.setItems(appointmentDAO.getAppointmentWeek(currentWeek));
        viewTitle.setText(String.valueOf(currentWeek.getMonth()));
    }
    /**Method for toggling forward by week*/
    public void nextWeek(ActionEvent actionEvent) throws SQLException{
        currentWeek = currentWeek.plusWeeks(1);
        System.out.println(currentWeek);
        appointmentsTable.setItems(appointmentDAO.getAppointmentWeek(currentWeek));
        viewTitle.setText(String.valueOf(currentWeek.getMonth()));
    }
    /**Gets selected customer and asks user for confirmation before deleting*/
    public void handleRemoveBtn() throws SQLException {
        Appointment appointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the selected appointment? \nID: " + appointment.getAppointmentId() + "\nType: " + appointment.getType());
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            appointmentDAO.delete(appointment);
            appointmentsTable.refresh();
            System.out.println("Part removed");
        } else {
            System.out.println("User cancelled remove request");
        }
        appointmentsTable.refresh();
        appointmentsTable.setItems(appointmentDAO.getAll());
        allItemsBtn.setSelected(true);
    }
    /**Method implementation to store lambda expression*/
    public void getByMonth(RetrieveMonth retrieve) throws SQLException {
        retrieve.setMonthTable(appointmentsTable, currentMonth);
    }
    /**Method implementation to store lambda expression*/
    public void getByWeek(RetrieveWeek retrieve) throws SQLException{
        retrieve.setWeekTable(appointmentsTable, currentMonth);
    }

}


