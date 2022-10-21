package controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import static helper.JDBC.connection;

public class CustomerViewController implements Initializable {
    private ObservableList<ObservableList> customer;

    public TableView customerView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildData();
    }
        public void buildData() {
            customer = FXCollections.observableArrayList();
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM client_schedule.customers";
                ResultSet rs = statement.executeQuery(query);
                //Dynamic column insertion
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

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

    public void handleNewCustomer(ActionEvent actionEvent) {
    }

    public void handleRemoveCustomer(ActionEvent actionEvent) {
    }

    public void handleModifyCustomer(ActionEvent actionEvent) {
    }

    public void handleClearAppointments(ActionEvent actionEvent) {
    }

}
