package controllers;
import model.Main;
import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.TimeZone;

public class UserLoginController implements Initializable {

    public TextField usernameField;
    public TextField passwordField;
    public Text errorMsg, timeZone;
    public AnchorPane userLogin;

    /**Retrieves entered username and password and checks logins against ones found in database users table. If a corresponding login is found the
     * primary view is initialized*/
    public void login(ActionEvent actionEvent) throws IOException{

        String usernameInput = usernameField.getText();
        String passwordInput = passwordField.getText();
            if(UserDAO.authorizeLogin(usernameInput, passwordInput)){
                System.out.println("Login Successful");
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Appointment");
                stage.setScene(scene);
                stage.show();
                Stage stage2 = (Stage) usernameField.getScene().getWindow();
                stage2.close();
            } else {
                System.out.println("Login Unsuccessful");
                errorMsg.setVisible(true);
                errorMsg.setStyle("-fx-text-fill: red;");
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeZone userTimeZone = TimeZone.getDefault();
        timeZone.setText(userTimeZone.getDisplayName());
    }

}
