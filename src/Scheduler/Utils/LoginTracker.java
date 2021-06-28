package Scheduler.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger class to track and log login activity
 *
 * @author Chris Criswell
 */
public class LoginTracker {

    /**
     * Singleton instance of LoginTracker
     */
    private static LoginTracker loginTracker = null;
    /**
     * Name of file to write to
     */
    private static String filename = "login_activity.txt";

    /**
     * Private default constructor for singleton class
     */
    private LoginTracker() {

    }

    /**
     * Gets initialized LoginTracker instance or initializes if null
     * @return LoginTracker instance
     */
    public static LoginTracker getLoginTracker() {

        if (loginTracker == null) {
            loginTracker = new LoginTracker();
            createFile();
        }

        return loginTracker;

    }

    /**
     * Creates login log file if none currently exists
     */
    private static void createFile() {

        File logFile = new File(filename);

        if (logFile.exists()) {
            return;
        }

        else {

            try {
                logFile.createNewFile();
            }

            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Logs login attempts
     * @param username user-inputted username
     * @param result success of login attempt
     */
    public void logActivity(String username, boolean result) {

        try {

            FileWriter logger = new FileWriter(filename, true);

            //https://www.javatpoint.com/java-get-current-date
            logger.append("Username: " + username + '\t' + "Date/Time: " + java.time.Clock.systemUTC().instant() + '\t');

            if (result) {
                logger.append("Result: success" + '\n');
            }

            else {
                logger.append("Result: denied"  + '\n');
            }

            logger.close();

        }

        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
