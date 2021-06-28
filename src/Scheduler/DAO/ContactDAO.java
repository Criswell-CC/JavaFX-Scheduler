package Scheduler.DAO;

import Scheduler.Utils.DatabaseHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for handling contact data in MySQL database
 *
 * @author Chris Criswell
 */
public class ContactDAO implements ContactDAOInterface {

    /**
     * Constructs and executes query to retrieve all contact names
     * @return list of all contact names
     */
    public ObservableList<String> getAllContactNames() {

        String query = "SELECT Contact_Name FROM contacts";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            ObservableList<String> contactNames = FXCollections.observableArrayList();

            while (results.next()) {

                contactNames.add(results.getString(1));

            }

            return contactNames;
        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return null;

    }

    /**
     * Constructs and executes query to retrieve the name of a contact given his/her ID
     * @param id id of contact
     * @return name of contact
     */
    public String getContactNameByID(int id) {

        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {

                return results.getString("Contact_Name");

            }
        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return null;

    }

    /**
     * Constructs and executes query to retrieve the ID of a contact given his/her name
     * @param name name of contact
     * @return ID of contact
     */
    public int getContactIDByName(String name) {

        String query = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setString(1, name);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {

                return results.getInt("Contact_ID");

            }
        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

        }

        return -1;

    }

}
