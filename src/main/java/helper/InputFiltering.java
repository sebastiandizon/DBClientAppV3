package helper;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;

@FunctionalInterface
public interface InputFiltering {
    boolean filterInputs(ObservableList<Control> controls);
}
