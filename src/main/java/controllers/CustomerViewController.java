package controllers;

import DAO.AppointmentDAOInterfaceImpl;
import DAO.CustomerDAOInterfaceImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;
import model.Main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

import static helper.JDBC.connection;

public class CustomerViewController implements Initializable {
    CustomerDAOInterfaceImpl customerDAO = new CustomerDAOInterfaceImpl();
    private ObservableList<ObservableList> customer;
    public TableView customerView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildTable();
    }
    public void handleNewCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-customer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Customer");
        stage.setScene(scene);
        stage.showAndWait();
        buildTable();
    }

    public void buildTable() {
            customer = FXCollections.observableArrayList();
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM client_schedule.customers";
                ResultSet rs = statement.executeQuery(query);
                //Dynamic column insertion
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().get(j)));

                    customerView.getColumns().addAll(col);
                    System.out.println("Column " + i + " ");
                }
            //inserting data to ObservableList

                while (rs.next()) {
                    int n =0;
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        n=i;
                        row.add(rs.getString(i));
                    }
                    System.out.println("Row [" + n + "] added " + "with attributes: "+ row);
                    customer.add(row);
                }
                customerView.setItems(customer);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on Building Data");
            }
    }

    public void handleRemoveCustomer(ActionEvent actionEvent) throws SQLException {
        Object o = customerView.getSelectionModel().getSelectedItem();

        if(customer == null){
            return;
        }
        AppointmentDAOInterfaceImpl appointmentDAO = new AppointmentDAOInterfaceImpl();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the selected customer?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            if(appointmentDAO.getAssociated(customer.getCustomerId()).size() < 1) {
                customerDAO.delete(customer.getCustomerId());
                System.out.println("Customer removed");
            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Removal error");
                alert1.setHeaderText("Error removing customer");
                alert1.setContentText("Could not remove. Customer has associated appointments");
                alert1.show();
            }
        } else {
            System.out.println("User cancelled remove request");
        }
    }



    public void handleModifyCustomer(ActionEvent actionEvent) {
    }

    public void handleClearAppointments(ActionEvent actionEvent) {
    }

}
