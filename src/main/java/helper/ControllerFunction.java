package helper;

import javafx.scene.control.Control;

public interface ControllerFunction {
    /**lambda expression that handles input validation of controls*/
    boolean checkControl(Control control);
    /**lambda expression that grabs window and closes it*/
    void closeStage();
}
