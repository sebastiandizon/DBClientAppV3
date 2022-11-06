package helper;

import DAO.AppointmentDAOImpl;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.time.LocalDate;

/**Implementation of functional interfaces for filtering month & week*/
public class RetrieveImpl implements RetrieveMonth, RetrieveWeek {

    @Override
    public void setMonthTable(TableView table, LocalDate currentMonth) throws SQLException {
        AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
        table.setItems(appointmentDAO.getAppointmentMonth(currentMonth));
    }

    @Override
    public void setWeekTable(TableView table, LocalDate currentWeek) throws SQLException {
        AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
        table.setItems(appointmentDAO.getAppointmentMonth(currentWeek));
    }

}
