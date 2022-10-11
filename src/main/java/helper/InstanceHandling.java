package helper;

import javafx.scene.control.Control;

public interface InstanceHandling {
    void setInstance();
    boolean checkControl(Control control);
    void closeStage();
}
