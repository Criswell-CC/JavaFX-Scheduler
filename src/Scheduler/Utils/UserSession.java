package Scheduler.Utils;

import Scheduler.Models.User;
import javafx.scene.control.Alert;

/**
 * Singleton class providing session management for currently logged-in user
 *
 * @author Chris Criswell
 */
public class UserSession {

    /**
     * User object representing currently logged-in user
     */
    static User currentUser = null;

    /**
     * Name of user
     */
    String name;
    /**
     * ID of user
     */
    int id;

    /**
     * Gets already logged-in user or initializes user
     * @return currently logged-in user or null if no user currently logged-in
     */
    public static User getCurrentUser() {

        //not logged in
        if (currentUser == null) {
            return null;
        }

        return currentUser;
    };

    /**
     * Initializes current user on login
     * @param name name of user
     * @param id ID of user
     * @return currently logged-in user
     */
    public static User loginUser(String name, int id) {

        if (currentUser == null) {
            currentUser = new User(name, id);
        }

        return currentUser;

    }

    /**
     * Accessor for user ID
     * @return user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Accessor for user name
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator for user ID
     * @param id int value of user ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Mutator for user name
     * @param name user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if a user is currently signed-in
     * @return bool indicating whether a user is currently signed-in
     */
    public static boolean checkUserSignedIn() {

        if (UserSession.getCurrentUser() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: No currently signed in user. Please sign in again.");
            alert.showAndWait();

            return false;

        }

        return true;

    }

}
