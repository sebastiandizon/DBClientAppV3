package controllers;

import com.company.dbclientappv2.Main;
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
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public AnchorPane primaryView;
    public TableView appointmentView = new TableView<>();
    public MenuButton viewMenu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
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

    public void setMonthView(ActionEvent actionEvent) {
    }

    public void setWeekView(ActionEvent actionEvent) {
    }

    public void setAllItems(ActionEvent actionEvent) {
    }

    public void handleReportBtn(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) {
    }

    public void previousMonth(ActionEvent actionEvent) {
    }

    public void nextMonth(ActionEvent actionEvent) {
    }

    public void handleNewAppointmentBtn(ActionEvent actionEvent) {
    }

    public void handleRemoveBtn(ActionEvent actionEvent) {
    }

    public void handleModifyAppointment(ActionEvent actionEvent) {
    }

    public void setTypeCount(ActionEvent actionEvent) {
    }
}

