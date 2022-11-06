package helper;

import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.time.LocalDate;
/**Functional interface to implement filtering by time range*/
public interface RetrieveWeek {
     void setWeekTable(TableView table, LocalDate currentWeek) throws SQLException;
}
