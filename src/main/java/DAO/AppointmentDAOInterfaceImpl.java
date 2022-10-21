package DAO;

import DAO.DAOInterface;
import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static helper.JDBC.connection;

public class AppointmentDAOInterfaceImpl implements DAOInterface {

    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

    @Override
    public ObservableList<Appointment> getAll() throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM client_schedule.appointments;";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            Appointment appointment = new Appointment();
            appointment.setAppointment_ID(rs.getInt(1));
            appointment.setTitle(rs.getString(2));
            appointment.setDescription(rs.getString(3));
            appointment.setLocation(rs.getString(4));
            appointment.setType(rs.getString(5));

            appointment.setStartTime(rs.getTimestamp(6).toLocalDateTime());
            appointment.setEndTime(rs.getTimestamp(7).toLocalDateTime());

            appointment.setCreateDate(rs.getTimestamp(8).toInstant());
            appointment.setCreatedBy(rs.getString(9));
            appointment.setLastUpdate(rs.getTimestamp(10).toInstant());
            appointment.setLastUpdateBy(rs.getString(11));

            appointment.setCustomerId(rs.getInt(12));
            appointment.setUserId(rs.getInt(13));
            appointment.setContactId(rs.getInt(14));
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    @Override
    public void save(Object o) {

    }
    public ObservableList<Appointment> getAssociated(int customerId) throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for(Appointment appointment : getAll()){
            if(appointment.getCustomerId() == customerId){
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    @Override
    public void update(Object o, String[] params) {

    }

    @Override
    public void delete(Object o) {

    }
}
