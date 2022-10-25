package DAO;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Optional;

import static helper.JDBC.connection;

public class AppointmentDAOImpl implements DAOInterface {

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
            appointment.setAppointmentId(rs.getInt(1));
            appointment.setTitle(rs.getString(2));
            appointment.setDescription(rs.getString(3));
            appointment.setLocation(rs.getString(4));
            appointment.setType(rs.getString(5));

            appointment.setStartTime(rs.getTimestamp(6).toLocalDateTime().atZone(ZoneId.systemDefault()));
            appointment.setEndTime(rs.getTimestamp(7).toLocalDateTime().atZone(ZoneId.systemDefault()));

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
    public void deleteAll(ObservableList<Appointment> appointments) throws SQLException {
        for(Appointment appointment : appointments){
            delete(appointment);
        }
    }

    @Override
    public void save(Object o) throws SQLException {
        String query = "INSERT INTO client_schedule.appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ((Appointment)o).getTitle());
        ps.setString(2, ((Appointment)o).getDescription());
        ps.setString(3, ((Appointment)o).getLocation());
        ps.setString(4, ((Appointment)o).getType());
        ps.setString(5, ((Appointment)o).getStartQueryFormat());
        ps.setString(6, ((Appointment)o).getEndQueryFormat());
        ps.setString(7, ((Appointment)o).getModifyRecord().getSimpleCreateDate());
        ps.setString(8, ((Appointment)o).getModifyRecord().getCreatedBy());
        ps.setString(9, ((Appointment)o).getModifyRecord().getSimpleLastUpdate());
        ps.setString(10, ((Appointment)o).getModifyRecord().getLastUpdateBy());
        ps.setString(11, (String.valueOf(((Appointment)o).getCustomerId())));
        ps.setString(12, (String.valueOf(((Appointment)o).getUserId())));
        ps.setString(13, (String.valueOf(((Appointment)o).getContactId())));
        System.out.println(ps);
        ps.execute();
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
    public void delete(Object o) throws SQLException{
        for(Appointment appointment : getAll()){
            if(appointment.getAppointmentId() == ((Appointment)o).getAppointmentId()){
                Statement statement = connection.createStatement();
                String query = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + appointment.getAppointmentId();
                System.out.println("Appointment with ID " + appointment.getAppointmentId() + " deleted successfully");
                statement.executeUpdate(query);

            }
        }
    }
}
