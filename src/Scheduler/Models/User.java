package Scheduler.Models;

/**
 * Model of User object
 *
 * @author Chris Criswell
 */
public class User {

    /**
     * Name of user
     */
    String name;
    /**
     * id of User
     */
    int id;

    /**
     * Constructor with name and id parameters
     * @param name name of user
     * @param id id of user
     */
    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Accessor for ID member
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     * Accessor for user name
     * @return name of user
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
     * @param name string for user name
     */
    public void setName(String name) {
        this.name = name;
    }

}
