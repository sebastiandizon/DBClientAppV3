package controllers;

import DAO.AppointmentDAOImpl;
import DAO.CustomerDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import model.Appointment;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable{
    ObservableList<Appointment> reportList = FXCollections.observableArrayList();
    AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    CustomerDAOImpl customerDAO = new CustomerDAOImpl();

    public TableView scheduleView = new TableView<>();
    public TableColumn appointment_ID, title, description, location, contact, type, start, end, customerId, userId;
    public Text totalApptsTxt;
    public ComboBox<Integer> contactCombo;

    public void setContactSchedule() throws SQLException{
        reportList.clear();
        for(Appointment appointment : appointmentDAO.getAll()){
            if(appointment.getContactId() == contactCombo.getSelectionModel().getSelectedItem()){
                reportList.add(appointment);
            }
        }
        totalApptsTxt.setText("Total appointments: " + reportList.size());
        scheduleView.setItems(reportList);
        scheduleView.refresh();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointment_ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactCombo.setValue(1);
        try {
            setContactSchedule();
            contactCombo.setItems(customerDAO.getContactIds());
        }catch (SQLException e) {e.printStackTrace();}


        scheduleView.setItems(reportList);




    }
}
