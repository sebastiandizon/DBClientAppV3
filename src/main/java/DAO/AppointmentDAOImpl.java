package DAO;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static helper.JDBC.connection;
import static helper.JDBC.userTimeZone;

public class AppointmentDAOImpl implements DAOInterface<Appointment> {
    /**Retrieves all appointments in appointments table*/
    @Override
    public ObservableList<Appointment> getAll() throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM client_schedule.appointments;");
        return getAppointments(query);
    }
    /**Deletes appointments in given list
     * @param appointments List of appointments to delete*/
    public void deleteAll(ObservableList<Appointment> appointments) throws SQLException {
        for(Appointment appointment : appointments){
            delete(appointment);
        }
    }
    /**Inserts new appointment into table*/
    @Override
    public void save(Appointment o) {
        try {
            String query = "INSERT INTO client_schedule.appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, o.getTitle());
            ps.setString(2, o.getDescription());
            ps.setString(3, o.getLocation());
            ps.setString(4, o.getType());
            ps.setTimestamp(5, Timestamp.valueOf(o.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(o.getEndTime()));
            ps.setString(7, o.getModifyRecord().getSimpleCreateDate());
            ps.setString(8, o.getModifyRecord().getCreatedBy());
            ps.setString(9, o.getModifyRecord().getSimpleLastUpdate());
            ps.setString(10, o.getModifyRecord().getLastUpdateBy());
            ps.setString(11, (String.valueOf(o.getCustomerId())));
            ps.setString(12, (String.valueOf(o.getUserId())));
            ps.setString(13, (String.valueOf(o.getContactId())));
            System.out.println(ps);
            ps.executeUpdate();
        }catch (SQLException e) {e.printStackTrace();}
    }
    /**Gets appointments associated with specified customer
     * @param customerId ID of customer
     * @return List of appoointments with matching customer id field*/
    public ObservableList<Appointment> getAssociated(int customerId) throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for(Appointment appointment : getAll()){
            if(appointment.getCustomerId() == customerId){
                appointments.add(appointment);
            }
        }
        return appointments;
    }
    /**Changes columns to new values where appointment ID matches
     * @param o with desired values & new*/
    @Override
    public void update(Appointment o) throws SQLException{
        PreparedStatement query;
        query = connection.prepareStatement("UPDATE appointments " +
        "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
        " WHERE Appointment_ID = " + o.getAppointmentId());
        query.setString(1,o.getTitle());
        query.setString(2,o.getDescription());
        query.setString(3,o.getLocation());
        query.setString(4,o.getType());
        query.setTimestamp(5,Timestamp.valueOf(o.getStartTime()));
        query.setTimestamp(6,Timestamp.valueOf(o.getEndTime()));
        query.setString(7,o.getModifyRecord().getSimpleLastUpdate());
        query.setString(8,o.getModifyRecord().getLastUpdateBy());
        query.setInt(9,o.getCustomerId());
        query.setInt(10,o.getUserId());
        query.setInt(11,o.getContactId());
        System.out.println(query);
        query.executeUpdate();
    }
    /**@param o Appointment to be deleted*/
    @Override
    public void delete(Appointment o) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + o.getAppointmentId();
        System.out.println("Appointment with ID " + o.getAppointmentId() + " deleted successfully");
        statement.executeUpdate(query);
    }
    /**Retrieves appointments that overlap with specified appointment hours
     * @param appointment Appointment with desired hours
     * @return list of appointments with hours lying in between appointment Start & End values*/
    public ObservableList<Appointment> getOverlaps(Appointment appointment) throws SQLException {
        PreparedStatement query = connection.prepareStatement( "SELECT * FROM appointments WHERE Start BETWEEN ? AND ? OR End BETWEEN ? AND ?");
        query.setTimestamp(1, Timestamp.valueOf(appointment.getStartTime()));
        query.setTimestamp(2, Timestamp.valueOf(appointment.getEndTime()));

        query.setTimestamp(3, Timestamp.valueOf(appointment.getStartTime()));
        query.setTimestamp(4, Timestamp.valueOf(appointment.getEndTime()));
        return getAppointments(query);
    }
    /**@param givenDate LocalDate to retrieve month value from
     * @return appointments with given month in LocalDate format*/
    public ObservableList<Appointment> getAppointmentMonth(LocalDate givenDate) throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ? OR End BETWEEN ? AND ?");
        query.setDate(1, Date.valueOf(givenDate.withDayOfMonth(1)));
        query.setDate(2, Date.valueOf(givenDate.withDayOfMonth(givenDate.lengthOfMonth())));

        query.setDate(3, Date.valueOf(givenDate.withDayOfMonth(1)));
        query.setDate(4, Date.valueOf(givenDate.withDayOfMonth(givenDate.lengthOfMonth())));
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formattedStart = givenDate.withDayOfMonth(1).format(formatter);
//        String formattedEnd = givenDate.plusMonths(1).format(formatter);
//        String query = "SELECT * FROM appointments WHERE Start BETWEEN '"+formattedStart+"' AND '"+formattedEnd+
//                "' OR End BETWEEN '"+ formattedStart +"' AND '"+formattedEnd+"';";
        return getAppointments(query);
    }
    /**@param givenDate LocalDate to retrieve week value from
     * @return appointments within given week in LocalDate format*/
    public ObservableList<Appointment> getAppointmentWeek(LocalDate givenDate) throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ? OR End BETWEEN ? AND ?");
        query.setDate(1, Date.valueOf(givenDate));
        query.setDate(2, Date.valueOf(givenDate.plusWeeks(1)));

        query.setDate(3, Date.valueOf(givenDate));
        query.setDate(4, Date.valueOf(givenDate.plusWeeks(1)));
//        String formattedStart = givenDate.withDayOfMonth(1).format(formatter);
//        String formattedEnd = givenDate.plusWeeks(1).format(formatter);
        return getAppointments(query);
    }
    /**@return List of contact IDs*/
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
    /**@param query SQL for retrieving specific appointments
     * @return list of appointments based on query
     * Reusable method for building specific Select statements*/
    public ObservableList<Appointment> getAppointments(PreparedStatement query) throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ResultSet rs = query.executeQuery();
        while(rs.next()){
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getInt(1));
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
            appointments.add(appointment);
        }
        return appointments;
    }
    /**@return List of strings for report page*/
    public ObservableList<String> getMonthReport(int contactId) throws SQLException{
        ObservableList<String> monthReport = FXCollections.observableArrayList();
        String query = "SELECT Contact_ID AS contact, Type AS Type, MONTH(Start) AS Month, Count(*) AS Amount FROM appointments GROUP BY MONTH(Start), Type, Contact_ID \n";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            String report = "Contact ID: "+ rs.getString("contact") + "\n Type: " + rs.getString("Type") + "\n MONTH: " + Month.of(rs.getInt("Month")) + "\n Amount: " + rs.getString("Amount") + "\n";
            monthReport.add(report);
        }
        return monthReport;
    }
    /**@return String of last appointment */
    public String getNextContact(int contactId) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("SELECT Title, Start FROM client_schedule.appointments WHERE Contact_ID = " + contactId + " AND Start > ? ORDER BY Start ASC LIMIT 1");
        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ResultSet rs = ps.executeQuery();
        rs.next();
        if(rs.getString("Title").equals("")){
            return "None";
        }
        return rs.getString("Title");
    }
    public ObservableList<Appointment> getNextAlert() throws SQLException{
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM client_schedule.appointments WHERE Start <= ? AND START >= ? OR End <= ? AND End >= ?");
        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)));
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)));
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        return getAppointments(ps);
    }
}
