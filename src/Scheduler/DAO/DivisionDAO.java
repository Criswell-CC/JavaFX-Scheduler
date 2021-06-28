package Scheduler.DAO;

import Scheduler.Utils.DatabaseHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for handling division data in MySQL database
 *
 * @author Chris Criswell
 */
public class DivisionDAO implements DivisionDAOInterface {

    /**
     * Executes any query on division data
     * @param query provided query on division data
     * @return list of all division names returned
     */
    private ObservableList<String> executeQueryGeneral(String query) {

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            ObservableList<String> firstLevelDivisionList = FXCollections.observableArrayList();

            while (results.next()) {

                firstLevelDivisionList.add(results.getString("Division"));

            }

            return firstLevelDivisionList;
        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return null;
    }

    /**
     * Constructs and executes query to get all division data
     * @return List of all division names
     */
    @Override
    public ObservableList<String> getAllDivisionList() {

        String query = "SELECT Division FROM first_level_divisions";

        return executeQueryGeneral(query);

    }

    /**
     * Constructs and executes query to get all division data for US
     * @return List of all division names for US
     */
    public ObservableList<String> getUSDivisionList() {

        String query = "SELECT Division FROM first_level_divisions WHERE Division_ID BETWEEN 0 AND 55";

        return executeQueryGeneral(query);

    }

    /**
     * Constructs and executes query to get all division data for Canada
     * @return List of all division names for Canada
     */
    public ObservableList<String> getCADivisionList() {

        String query = "SELECT Division FROM first_level_divisions WHERE Division_ID BETWEEN 60 AND 72";

        return executeQueryGeneral(query);

    }

    /**
     * Constructs and executes query to get all division data for UK
     * @return List of all division names for UK
     */
    public ObservableList<String> getUKDivisionList() {

        String query = "SELECT Division FROM first_level_divisions WHERE Division_ID BETWEEN 100 AND 105";

        return executeQueryGeneral(query);

    }

    /**
     * Constructs and executes query to get division ID given a division name
     * @param division division name
     * @return division ID for specified division name
     */
    public int getIDByDivision(String division) {

        String query = "SELECT Division_ID from first_level_divisions WHERE Division = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setString(1, division);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {

                return results.getInt(1);

            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return -1;
    }

    /**
     * Constructs and executes query to get division name given a division ID
     * @param id division name
     * @return name of division for specified ID
     */
    public String getDivisionNameByID(int id) {

        String query = "SELECT Division from first_level_divisions WHERE Division_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {

                return results.getString("Division");

            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return null;
    }

}