package com.company.dbclientappv2;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class UserLoginController {
    Scanner check = new Scanner(System.in);
    public TextField usernameField;
    public TextField passwordField;
    public Text errorMsg;

    public void login(ActionEvent actionEvent){
        String usernameInput = usernameField.getText();
        String passwordInput = passwordField.getText();
            if(Users.authorizeLogin(usernameInput, passwordInput)){
                System.out.println("Login Successful");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("primary-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Appointment");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) { e.printStackTrace(); }
            } else {
                System.out.println("Login Unsuccessful");
                errorMsg.setVisible(true);
                errorMsg.setStyle("-fx-text-fill: red;");
            }
    }

    public void newUser(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new-user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New User");
        stage.setScene(scene);
        stage.show();
    }

}
