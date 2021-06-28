package Scheduler.DAO;

import Scheduler.Models.User;
import Scheduler.Utils.DatabaseHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for handling user data in MySQL database
 *
 * @author Chris Criswell
 */
public class UserDAO implements UserDAOInterface {

    /**
     * Constructs and executes query to get user data from user ID
     * @param id user ID
     * @return User object contained returned data
     */
    @Override
    public User getUserByID(int id) {

        String query = "SELECT * FROM users WHERE User_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {
                return new User(results.getString("Name"), results.getInt("User_ID"));
            }
        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return null;

    }

    /**
     * Constructs and executes query to check whether a user with a given ID exists in database
     * @param id user ID
     * @return bool indicating whether user with given ID exists
     */
    @Override
    public boolean checkUserIDValid(int id) {

        String query = "SELECT * FROM users WHERE User_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {
                return true;
            }
        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false;
    }
}
