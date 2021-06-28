package Scheduler.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Singleton class handling establishing and ending connections to MySQL database and login verification
 * @author Chris Criswell
 */
public class DatabaseHandler {

    /**
     * Single app-wide instance of database connection
     */
    private static Connection dbConnection = null;

    /**
     * Server domain name string
     */
    private static final String server = "";
    /**
     * Database name string
     */
    private static final String dbName = "";
    /**
     * Username string
     */
    private static final String username = "";
    /**
     * Password string
     */
    private static String password = "";

    /**
     * Full URL to connect to database
     */
    private static final String jdbcUrl = "jdbc:mysql://" + server + '/' + dbName;

    /**
     * Static bool used for input forms to communicate to panel controllers to refresh tables to load newly entered data
     */
    public static boolean databaseUpdated = false;

    /**
     * Returns instance of database connection or initializes connection if none started
     * @return database connection instance
     */
    public static Connection getConnection() {

        if (dbConnection == null) {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                dbConnection = DriverManager.getConnection(jdbcUrl, username, password);
                System.out.println("DB connection successful");

            }

            catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

        return dbConnection;

    }

    /**
     * Closes database connection
     */
    public static void endConnection() {

        try {

            dbConnection.close();
            System.out.println("DB connection ended");

        }

        catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    /**
     * Handles login validation by checking if username and password pair exist in database
     * @param username user-provided username
     * @param password user-provided password
     * @return bool indicating success of login attempt
     */
    public static boolean login(String username, String password) {

        boolean usernameResult = Validators.validateUsername(username);
        boolean passwordResult = Validators.validatePassword(password);

        if (!usernameResult || !passwordResult) {
            return false;
        }

        try {

            PreparedStatement loginQuery = dbConnection.prepareStatement("SELECT * FROM users WHERE User_Name=? AND Password=?");
            loginQuery.setString(1, username);
            loginQuery.setString(2, password);
            ResultSet result = loginQuery.executeQuery();

            if (result.next()) {

                UserSession.loginUser(username, result.getInt("User_ID"));

                return true;

            }

            else {
                return false;
            }
        }

        catch (SQLException exception) {

            System.out.println(exception.getMessage());

            return false;

        }
    }

    /**
     * Mutator for database updated bool member
     * @param value boolean value indicating whether the database has been updated by user
     */
    public static void setDatabaseUpdated(boolean value) {

        databaseUpdated = value;

    }
}
