package DAO;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static helper.JDBC.connection;

public class AppointmentDAOImpl implements DAOInterface {

    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

    @Override
    public ObservableList<Appointment> getAll() throws SQLException {
        String query = "SELECT * FROM client_schedule.appointments;";
        return getAppointments(query);
    }

    public void deleteAll(ObservableList<Appointment> appointments) throws SQLException {
        for(Appointment appointment : appointments){
            delete(appointment);
        }
    }

    @Override
    public void save(Object o) {
        try {
            String query = "INSERT INTO client_schedule.appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ((Appointment) o).getTitle());
            ps.setString(2, ((Appointment) o).getDescription());
            ps.setString(3, ((Appointment) o).getLocation());
            ps.setString(4, ((Appointment) o).getType());
            ps.setString(5, ((Appointment) o).getStartQueryFormat());
            ps.setString(6, ((Appointment) o).getEndQueryFormat());
            ps.setString(7, ((Appointment) o).getModifyRecord().getSimpleCreateDate());
            ps.setString(8, ((Appointment) o).getModifyRecord().getCreatedBy());
            ps.setString(9, ((Appointment) o).getModifyRecord().getSimpleLastUpdate());
            ps.setString(10, ((Appointment) o).getModifyRecord().getLastUpdateBy());
            ps.setString(11, (String.valueOf(((Appointment) o).getCustomerId())));
            ps.setString(12, (String.valueOf(((Appointment) o).getUserId())));
            ps.setString(13, (String.valueOf(((Appointment) o).getContactId())));
            System.out.println(ps);
            ps.executeUpdate();
        }catch (SQLException e) {e.printStackTrace();}
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
    public void update(Object o) throws SQLException{
        PreparedStatement query;
        query = connection.prepareStatement("UPDATE appointments " +
        "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
        " WHERE Appointment_ID = " + ((Appointment)o).getAppointmentId());
        query.setString(1,((Appointment)o).getTitle());
        query.setString(2,((Appointment)o).getDescription());
        query.setString(3,((Appointment)o).getLocation());
        query.setString(4,((Appointment)o).getType());
        query.setString(5,((Appointment)o).getStartQueryFormat());
        query.setString(6,((Appointment)o).getEndQueryFormat());
        query.setString(7,((Appointment)o).getModifyRecord().getSimpleLastUpdate());
        query.setString(8,((Appointment)o).getModifyRecord().getLastUpdateBy());
        query.setInt(9,((Appointment)o).getCustomerId());
        query.setInt(10,((Appointment)o).getUserId());
        query.setInt(11,((Appointment)o).getContactId());
        System.out.println(query);
        query.executeUpdate();
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
    public ObservableList<Appointment> selectBetween(Appointment appointment) throws SQLException {
        String query = "SELECT * FROM appointments WHERE Start BETWEEN '"+appointment.getStartQueryFormat()+"' AND '"+appointment.getEndQueryFormat() +
                "' OR End BETWEEN '"+ appointment.getStartQueryFormat() +"' AND '"+appointment.getEndQueryFormat()+"' AND Appointment_ID != " + appointment.getAppointmentId();
        System.out.println(query);
        return getAppointments(query);
    }

    public ObservableList<Appointment> getAppointmentMonth(LocalDate givenDate) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStart = givenDate.withDayOfMonth(1).format(formatter);
        String formattedEnd = givenDate.plusMonths(1).format(formatter);
        String query = "SELECT * FROM appointments WHERE Start BETWEEN '"+formattedStart+"' AND '"+formattedEnd+
                "' OR End BETWEEN '"+ formattedStart +"' AND '"+formattedEnd+"';";
        return getAppointments(query);
    }
    public ObservableList<Appointment> getAppointmentWeek(LocalDate givenDate) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStart = givenDate.withDayOfMonth(1).format(formatter);
        String formattedEnd = givenDate.plusWeeks(1).format(formatter);
        String query = "SELECT * FROM appointments WHERE Start BETWEEN '"+formattedStart+"' AND '"+formattedEnd+
                "' OR End BETWEEN '"+ formattedStart +"' AND '"+formattedEnd+"';";
        return getAppointments(query);
    }
    public ObservableList<Integer> getContIDs() throws SQLException{
        ObservableList<Integer> contactIds = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT Contact_ID FROM contacts";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            contactIds.add(rs.getInt("Contact_ID"));
        }
        return contactIds;
    }

    public ObservableList<Appointment> getAppointments(String query) throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
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
            appointments.add(appointment);
        }
        return appointments;
    }
}
