package Scheduler.DAO;

import Scheduler.Models.Appointment;
import Scheduler.Models.MonthlyAppointmentsByTypeCounter;
import Scheduler.Utils.DatabaseHandler;
import Scheduler.Utils.StringPair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.HashMap;

/**
 * DAO class for handling generating report data from MySQL database
 *
 * @author Chris Criswell
 */
public class ReportsDAO implements ReportsDAOInterface {

    /**
     * Constructs and executes query to get all appointments ordered by contact ID and start date
     * @return List of all appointments sorted by contact ID and start date
     */
    @Override
    public ObservableList<Appointment> getAppointmentsByContact() {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments ORDER BY Contact_ID, Start";

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
     * Constructs and executes query to get all appointments by type and start date and then runs an algorithm to count
     * instances where appointments of same type happen within the same month
     * @return List of counted appointment types per month
     */
    @Override
    public ObservableList<MonthlyAppointmentsByTypeCounter> getMonthlyCustomerAppointmentsByType() {

        ObservableList<MonthlyAppointmentsByTypeCounter> countedList = FXCollections.observableArrayList();

        String query = "SELECT Type, Start FROM appointments";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            HashMap<StringPair, Integer> counterMap = new HashMap<>();

            while (results.next()) {

                ZonedDateTime zonedStartDate = ZonedDateTime.ofInstant( results.getTimestamp("Start").toInstant(), ZoneId.systemDefault());

                StringPair pair = new StringPair(zonedStartDate.getMonth().toString(), results.getString("Type"));

                if (counterMap.containsKey(pair)) {
                    counterMap.put(pair, counterMap.get(pair) + 1);
                }

                else {
                    counterMap.put(pair, 1);
                }

            }

            counterMap.forEach((key, value) -> {

                countedList.add(new MonthlyAppointmentsByTypeCounter(key.getFirst().toString(), key.getSecond().toString(), value));

            });

        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return countedList;

    }

    /**
     * Constructs and executes query to get all appointments ordered by ID of user who created them
     * @return List of all appointments ordered by user ID and start date
     */
    @Override
    public ObservableList<Appointment> getAppointmentsByUserCreated() {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments ORDER BY User_ID, Start";

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

}
