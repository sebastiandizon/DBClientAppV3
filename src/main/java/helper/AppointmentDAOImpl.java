package helper;

import com.company.dbclientappv2.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static helper.JDBC.connection;

public class AppointmentDAOImpl implements Dao{
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

    @Override
    public ObservableList<Appointment> getAll() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM client_schedule.appointments;";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            Appointment appointment = new Appointment();
            appointment.setAppointment_ID(rs.getInt(0));
            appointment.setTitle(rs.getString(1));
            appointment.setDescription(rs.getString(2));
            appointment.setLocation(rs.getString(3));
            appointment.setType(rs.getString(4));

            appointment.setStartTime(rs.getTimestamp(4).toLocalDateTime());
            appointment.setEndTime(rs.getTimestamp(5).toLocalDateTime());

            appointment.setCreateDate(rs.getTimestamp(6).toInstant());
            appointment.setCreatedBy(rs.getString(7));
            appointment.setLastUpdate(rs.getTimestamp(8).toInstant());
            appointment.setLastUpdateBy(rs.getString(9));

            appointment.setCustomerId(rs.getInt(10));
            appointment.setUserId(rs.getInt(11));
            appointment.setContactId(rs.getInt(12));
        }
        return appointmentList;
    }

    @Override
    public void save(Object o) {

    }

    @Override
    public void update(Object o, String[] params) {

    }

    @Override
    public void delete(Object o) {

    }
}
