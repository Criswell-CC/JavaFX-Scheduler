package Scheduler.DAO;

import Scheduler.Models.Appointment;
import Scheduler.Utils.DatabaseHandler;
import Scheduler.Utils.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.*;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

import java.util.Locale;

/**
 * DAO class for handling appointment data in MySQL database
 *
 * @author Chris Criswell
 */
public class AppointmentDAO implements AppointmentDAOInterface {

    /**
     * Constructs and executes query to add a given appointment
     * @param appointment appointment to add
     * @return bool indicating success of database write
     */
    public boolean addAppointment(Appointment appointment) {

        String query = "INSERT INTO appointments " +
                "(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, appointment.getAppointmentID());
            preparedStatement.setString(2, appointment.getTitle());
            preparedStatement.setString(3, appointment.getDescription());
            preparedStatement.setString(4, appointment.getLocation());
            preparedStatement.setString(5, appointment.getType());
            preparedStatement.setTimestamp(6, Timestamp.from(appointment.getStartDateTime().toInstant()));
            preparedStatement.setTimestamp(7, Timestamp.from(appointment.getEndDateTime().toInstant()));

            preparedStatement.setString(8, UserSession.getCurrentUser().getName());
            preparedStatement.setString(9, UserSession.getCurrentUser().getName());

            preparedStatement.setInt(10, appointment.getCustomerID());
            preparedStatement.setInt(11, UserSession.getCurrentUser().getId());
            preparedStatement.setInt(12, appointment.getContactID());

            preparedStatement.execute();

            return true;

        }
        catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return false;
        }

    }

    /**
     * Constructs and executes query to delete a given appointment
     * @param appointment appointment to delete
     * @return bool indicating success of database delete
     */
    public boolean deleteAppointment(Appointment appointment) {

        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, appointment.getAppointmentID());

            preparedStatement.execute();

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return false;
        }

        return true;

    }

    /**
     * Constructs and executes query to update a given appointment
     * @param appointment appointment to update
     * @return bool indicating success of database write
     */
    public boolean updateAppointment(Appointment appointment) {

        String query = "UPDATE appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setString(1, appointment.getTitle());
            preparedStatement.setString(2, appointment.getDescription());
            preparedStatement.setString(3, appointment.getLocation());
            preparedStatement.setString(4, appointment.getType());
            preparedStatement.setTimestamp(5, Timestamp.from(appointment.getStartDateTime().toInstant()));
            preparedStatement.setTimestamp(6, Timestamp.from(appointment.getEndDateTime().toInstant()));
            preparedStatement.setString(7, UserSession.getCurrentUser().getName());
            preparedStatement.setInt(8, appointment.getCustomerID());
            preparedStatement.setInt(9, UserSession.getCurrentUser().getId());
            preparedStatement.setInt(10, appointment.getContactID());
            preparedStatement.setInt(11, appointment.getAppointmentID());

            preparedStatement.execute();

            return true;

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false;

    }

    /**
     * Constructs and executes query to return appointment data for specified appointment ID
     * @param appointmentID ID of specified appointment
     * @return appointment object
     */
    public Appointment getAppointment(int appointmentID) {

        String query = "SELECT * FROM appointments WHERE Appointment_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, appointmentID);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {

                ZonedDateTime zonedStartDate = ZonedDateTime.ofInstant( results.getTimestamp("Start").toInstant(), ZoneId.systemDefault());
                ZonedDateTime zonedEndDate = ZonedDateTime.ofInstant( results.getTimestamp("End").toInstant(), ZoneId.systemDefault());

                Appointment appointment = new Appointment(results.getInt("Appointment_ID"),
                        results.getInt("Customer_ID"), results.getInt("Contact_ID"),
                        results.getInt("User_ID"), results.getString("Title"),
                        results.getString("Description"), results.getString("Location"),
                        results.getString("Type"), zonedStartDate, zonedEndDate);

                return appointment;

            }
        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return null;

    }

    /**
     * Constructs and executes query to return all appointments
     * @return list of all appointments
     */
    public ObservableList<Appointment> getAllAppointments() {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments ORDER BY Start, Appointment_ID";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            while (results.next()) {

                ZonedDateTime zonedStartDate = ZonedDateTime.ofInstant( results.getTimestamp("Start").toInstant(), ZoneId.systemDefault());
                ZonedDateTime zonedEndDate = ZonedDateTime.ofInstant( results.getTimestamp("End").toInstant(), ZoneId.systemDefault());

                Appointment appointment = new Appointment(results.getInt("Appointment_ID"),
                        results.getInt("Customer_ID"), results.getInt("Contact_ID"),
                        results.getInt("User_ID"), results.getString("Title"),
                        results.getString("Description"), results.getString("Location"),
                        results.getString("Type"), zonedStartDate, zonedEndDate);

                appointmentList.add(appointment);

            }

        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return appointmentList;

    }

    /**
     * Constructs and executes query to retrieve all appointments falling within current week
     * @return list of all appointments occurring during current week
     */
    public ObservableList<Appointment> getAllAppointmentsByWeek() {

        String query = "SELECT * FROM appointments WHERE MONTH(Start) = ? AND WEEK = ? AND DAY(Start) => ? AND HOUR(Start) => ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            //https://stackoverflow.com/questions/26012434/get-week-number-of-localdate-java-8/26013129
            LocalDateTime currentDate = LocalDateTime.now();
            TemporalField week = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int weekNumber = currentDate.get(week);

            preparedStatement.setInt(1, currentDate.getMonthValue());
            preparedStatement.setInt(2, weekNumber);
            preparedStatement.setInt(3, currentDate.getDayOfMonth());
            preparedStatement.setInt(4, currentDate.getHour());

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            ObservableList<Appointment> appointmentsInMonth = FXCollections.observableArrayList();

            while (results.next()) {

                ZonedDateTime zonedStartDate = ZonedDateTime.ofInstant( results.getTimestamp("Start").toInstant(), ZoneId.systemDefault());
                ZonedDateTime zonedEndDate = ZonedDateTime.ofInstant( results.getTimestamp("End").toInstant(), ZoneId.systemDefault());

                Appointment appointment = new Appointment(results.getInt("Appointment_ID"),
                        results.getInt("Customer_ID"), results.getInt("Contact_ID"),
                        results.getInt("User_ID"), results.getString("Title"),
                        results.getString("Description"), results.getString("Location"),
                        results.getString("Type"), zonedStartDate, zonedEndDate);

                appointmentsInMonth.add(appointment);

            }

            return appointmentsInMonth;


        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return null;
    }

    /**
     * Constructs and executes query to retrieve all appointments falling within current month
     * @return list of all appointments occurring during current month
     */
    public ObservableList<Appointment> getAllAppointmentsByMonth() {

        String query = "SELECT * FROM appointments WHERE MONTH(Start) = ? AND DAY(Start) => ? AND HOUR(Start) => ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            ZonedDateTime currentDate = ZonedDateTime.now();

            preparedStatement.setInt(1, currentDate.getMonthValue());
            preparedStatement.setInt(2, currentDate.getDayOfMonth());
            preparedStatement.setInt(3, currentDate.getHour());

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            ObservableList<Appointment> appointmentsInMonth = FXCollections.observableArrayList();

            while (results.next()) {

                ZonedDateTime zonedStartDate = ZonedDateTime.ofInstant( results.getTimestamp("Start").toInstant(), ZoneId.systemDefault());
                ZonedDateTime zonedEndDate = ZonedDateTime.ofInstant( results.getTimestamp("End").toInstant(), ZoneId.systemDefault());

                Appointment appointment = new Appointment(results.getInt("Appointment_ID"),
                        results.getInt("Customer_ID"), results.getInt("Contact_ID"),
                        results.getInt("User_ID"), results.getString("Title"),
                        results.getString("Description"), results.getString("Location"),
                        results.getString("Type"), zonedStartDate, zonedEndDate);

                appointmentsInMonth.add(appointment);

            }

            return appointmentsInMonth;


        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return null;
    }

    /**
     * Constructs and executes query to retrieve all appointments falling within a specified range
     * @param start start date/time of range
     * @param end end date/time of range
     * @return list of all appointments falling within specified range
     */
    @Override
    public ObservableList<Appointment> getAppointmentsWithinRange(ZonedDateTime start, ZonedDateTime end) {

        String query = "SELECT * FROM appointments WHERE ((Start > ? AND CAST(Start as TIME) > ?) AND (Start < ? AND CAST(Start as TIME) < ?)) " +
                "OR ((End > ? AND CAST(End as TIME) > ?) AND (End < ? AND CAST(End as TIME) < ?)) " +
                "OR ((Start > ? AND CAST(Start as TIME) < ?) AND (End > ? AND CAST(End as TIME) > ?)) " +
                "OR ((Start < ? AND CAST(Start as TIME) < ?) AND (End > ? AND CAST(END as TIME) > ?))";


        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            LocalDate appointmentStartDateCutoff = end.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
            //hack to get appointments within same day to return
            LocalDate appointmentEndDateCutoff = end.toInstant().atZone(ZoneOffset.UTC).toLocalDate().plusDays(1);

            LocalTime appointmentStartTimeCutoff = start.toInstant().atZone(ZoneOffset.UTC).toLocalTime();
            LocalTime appointmentEndTimeCutoff = end.toInstant().atZone(ZoneOffset.UTC).toLocalTime();

            //hack to get appointments at the same time to return
            LocalTime appointmentEndTimeCutoffPlusOne = end.toInstant().atZone(ZoneOffset.UTC).toLocalTime().plusMinutes(1);

            preparedStatement.setString(1, appointmentStartDateCutoff.toString());
            preparedStatement.setString(2, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(3, appointmentEndDateCutoff.toString());
            preparedStatement.setString(4, appointmentEndTimeCutoff.toString());

            preparedStatement.setString(5, appointmentStartDateCutoff.toString());
            preparedStatement.setString(6, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(7, appointmentEndDateCutoff.toString());
            preparedStatement.setString(8, appointmentEndTimeCutoffPlusOne.toString());

            preparedStatement.setString(9, appointmentStartDateCutoff.toString());
            preparedStatement.setString(10, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(11, appointmentStartDateCutoff.toString());
            preparedStatement.setString(12, appointmentEndTimeCutoffPlusOne.toString());

            preparedStatement.setString(13, appointmentStartDateCutoff.toString());
            preparedStatement.setString(14, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(15, appointmentStartDateCutoff.toString());
            preparedStatement.setString(16, appointmentEndTimeCutoff.toString());

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            ObservableList<Appointment> appointmentsInRange = FXCollections.observableArrayList();

            while (results.next()) {

                ZonedDateTime zonedStartDate = ZonedDateTime.ofInstant( results.getTimestamp("Start").toInstant(), ZoneId.systemDefault());
                ZonedDateTime zonedEndDate = ZonedDateTime.ofInstant( results.getTimestamp("End").toInstant(), ZoneId.systemDefault());

                Appointment appointment = new Appointment(results.getInt("Appointment_ID"),
                        results.getInt("Customer_ID"), results.getInt("Contact_ID"),
                        results.getInt("User_ID"), results.getString("Title"),
                        results.getString("Description"), results.getString("Location"),
                        results.getString("Type"), zonedStartDate, zonedEndDate);

                appointmentsInRange.add(appointment);

            }

            return appointmentsInRange;


        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return null;

    }

    /**
     * Constructs and executes query to check if a given appointment overlaps with any others
     * @param id id of a given appointment to check overlap on
     * @param start start date/time of given appointment
     * @param end end date/time of given appointment
     * @return bool indicating whether given appointment overlaps with any others
     */
    public boolean checkIfOtherAppointmentsInRange(int id, int customerID, ZonedDateTime start, ZonedDateTime end) {

        String query = "SELECT * FROM appointments WHERE (Customer_ID = ?) AND ((Start > ? AND CAST(Start as TIME) > ?) AND (Start < ? AND CAST(Start as TIME) < ?)) " +
                "OR ((End > ? AND CAST(End as TIME) > ?) AND (End < ? AND CAST(End as TIME) < ?)) " +
                "OR ((Start > ? AND CAST(Start as TIME) < ?) AND (End > ? AND CAST(End as TIME) > ?)) " +
                "OR ((Start < ? AND CAST(Start as TIME) < ?) AND (End > ? AND CAST(END as TIME) > ?))";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            LocalDate appointmentStartDateCutoff = end.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
            //hack to get appointments within same day to return
            LocalDate appointmentEndDateCutoff = end.toInstant().atZone(ZoneOffset.UTC).toLocalDate().plusDays(1);

            LocalTime appointmentStartTimeCutoff = start.toInstant().atZone(ZoneOffset.UTC).toLocalTime();
            LocalTime appointmentEndTimeCutoff = end.toInstant().atZone(ZoneOffset.UTC).toLocalTime();

            //hack to get appointments at the same time to return
            LocalTime appointmentEndTimeCutoffPlusOne = end.toInstant().atZone(ZoneOffset.UTC).toLocalTime().plusMinutes(1);

            preparedStatement.setInt(1, customerID);
            preparedStatement.setString(2, appointmentStartDateCutoff.toString());
            preparedStatement.setString(3, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(4, appointmentEndDateCutoff.toString());
            preparedStatement.setString(5, appointmentEndTimeCutoff.toString());

            preparedStatement.setString(6, appointmentStartDateCutoff.toString());
            preparedStatement.setString(7, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(8, appointmentEndDateCutoff.toString());
            preparedStatement.setString(9, appointmentEndTimeCutoffPlusOne.toString());

            preparedStatement.setString(10, appointmentStartDateCutoff.toString());
            preparedStatement.setString(11, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(12, appointmentStartDateCutoff.toString());
            preparedStatement.setString(13, appointmentEndTimeCutoffPlusOne.toString());

            preparedStatement.setString(14, appointmentStartDateCutoff.toString());
            preparedStatement.setString(15, appointmentStartTimeCutoff.toString());
            preparedStatement.setString(16, appointmentStartDateCutoff.toString());
            preparedStatement.setString(17, appointmentEndTimeCutoff.toString());

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            while (results.next()) {

                System.out.println(results.getInt(1));

                //check if result is same appointment
                if (results.getInt(1) == id) {
                    continue;
                }

                return true;

            }

        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return false;

    }

    /**
     * Utility method to automatically generate appointment ID
     * @return ID for new appointment
     */
    public int generateAppointmentID() {

        //https://stackoverflow.com/questions/5113450/last-id-value-in-a-table-sql-server/46346432
        String query = "SELECT MAX(Appointment_ID) FROM appointments";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet result = preparedStatement.getResultSet();

            if (result.next()) {
                int id = result.getInt(1);
                return id + 1;
            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return -1;
    }

}
