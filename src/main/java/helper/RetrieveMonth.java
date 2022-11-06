package helper;

import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.time.LocalDate;
/**Functional interface to implement filtering by time range*/
@FunctionalInterface
public interface RetrieveMonth {
    void setMonthTable(TableView table, LocalDate currentMonth) throws SQLException;
}
