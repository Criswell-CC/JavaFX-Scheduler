package Scheduler.DAO;

import Scheduler.Utils.DatabaseHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for handling country data in MySQL database
 *
 * @author Chris Criswell
 */
public class CountryDAO implements CountryDAOInterface {

    /**
     * Constructs and executes query to retrieve all country names
     * @return list of all country names
     */
    @Override
    public ObservableList<String> getAllCountries() {

        String query = "SELECT Country FROM countries";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            ObservableList<String> countryList = FXCollections.observableArrayList();

            while (results.next()) {

                countryList.add(results.getString("Country"));

            }

            return countryList;
        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return null;

    }

    /**
     * Constructs and executes query to retrieve country ID for a given country name
     * @param country country name
     * @return ID of a country given country name
     */
    @Override
    public int getIDByCountry(String country) {

        String query = "SELECT Country from countries WHERE Country = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setString(1, country);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {

                return results.getInt("Country_ID");

            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return -1;
    }

    /**
     * Constructs and executes query to retrieve name of a country a given first level division is in
     * @param id id of a first level division
     * @return name of country the given first level division is in
     */
    public String getCountryNameByDivisionID(int id) {

        String firstQuery = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID = ? ";

        int countryID = -1;

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(firstQuery);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet result = preparedStatement.getResultSet();

            if (result.next()) {

                countryID = result.getInt(1);

            }

            if (countryID != -1) {

                String secondQuery = "SELECT Country FROM countries WHERE Country_ID = ?";

                preparedStatement = DatabaseHandler.getConnection().prepareStatement(secondQuery);

                preparedStatement.setInt(1, countryID);

                preparedStatement.execute();

                result = preparedStatement.getResultSet();

                if (result.next()) {

                    return result.getString(1);

                }

            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return null;

    }
}
