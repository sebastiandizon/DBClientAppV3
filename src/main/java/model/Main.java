package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import static helper.JDBC.*;

/**@author Sebastian Dizon*/


public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Locale locale = Locale.getDefault();
            System.out.println(locale.getLanguage());
            if (locale.getLanguage().equals("fr")) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view-fr.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Login");
                stage.setScene(scene);
                stage.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Login");
                stage.setScene(scene);
                stage.show();
            }

    }

    public static void main(String[] args) {
        openConnection();

        try {
            FileWriter writer = new FileWriter("login_activity.txt", true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        launch();
    }
}