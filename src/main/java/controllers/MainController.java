package controllers;

import helper.JDBC;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Main;
import DAO.AppointmentDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    public AnchorPane primaryView;
    public TableView appointmentsTable = new TableView<>();
    public TableColumn appointment_ID, title, description, contact, location, type, start, end, customerId, userId;
    public MenuButton viewMenu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuItem appointmentsView = new MenuItem("Appointments View");
        MenuItem contactsView = new MenuItem("Customer View");
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
        contact.setCellValueFactory(new PropertyValueFactory<>("location"));
        location.setCellValueFactory(new PropertyValueFactory<>("type"));
        type.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        start.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userId.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        try {
            appointmentsTable.setItems(appointmentDAO.getAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
        try {
            appointmentDAO.getAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void handleNewAppointmentBtn(ActionEvent actionEvent) throws IOException, SQLException{
            appointmentsTable.refresh();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-appointment-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("New Appointment");
            stage.setScene(scene);
        stage.showAndWait();
        appointmentsTable.setItems(appointmentDAO.getAll());
    }
    public void handleModifyAppointment(ActionEvent actionEvent) throws IOException {
        appointmentsTable.refresh();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("modify-appointment-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)primaryView.getScene().getWindow();
        stage.setTitle("New Appointment");
        stage.setScene(scene);
        stage.show();
    }
    public void setMonthView(ActionEvent actionEvent) {
        //TODO: SELECT **** FROM **** WHERE -CONDITION: TIME BETWEEN CURRENT MONTH -
        //Probably run instance from DAO
    }

    public void setWeekView(ActionEvent actionEvent) {
    }

    public void setAllItems(ActionEvent actionEvent) {
    }

    public void handleReportBtn(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage)primaryView.getScene().getWindow();
        JDBC.closeConnection();
        stage.close();
    }

    public void previousMonth(ActionEvent actionEvent) {
    }

    public void nextMonth(ActionEvent actionEvent) {
    }



    public void handleRemoveBtn(ActionEvent actionEvent) {
    }

    public void setTypeCount(ActionEvent actionEvent) {
    }
}

