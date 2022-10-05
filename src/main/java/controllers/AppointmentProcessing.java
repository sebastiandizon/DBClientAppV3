package controllers;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public interface AppointmentProcessing {
    void generateAppointment();
    boolean checkControl(Control control);
    void closeStage();
}
