package Scheduler.Models;

/**
 * Model object to contain data for report on total appointments by month and type
 *
 * @author Chris Criswell
 */
public class MonthlyAppointmentsByTypeCounter {

    String month;
    String type;

    int total;

    /**
     * Constructor with all members provided
     * @param month name of month
     * @param type type of appointment
     * @param total total occurrences
     */
    public MonthlyAppointmentsByTypeCounter(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    /**
     * Accessor for month
     * @return name of month
     */
    public String getMonth() { return month; }

    /**
     * Accessor for total count
     * @return total count of occurrences
     */
    public int getTotal() { return total; }

    /**
     * Accessor for type
     * @return type of appointment
     */
    public String getType() { return type; }

    /**
     * Mutator for month string
     * @param month name of month
     */
    public void setMonth(String month) { this.month = month; }

    /**
     * Mutator for total count
     * @param total total number of occurrences
     */
    public void setTotal(int total) { this.total = total; }

    /**
     * Mutator for type of appointment string
     * @param string type of appointment
     */
    public void setType(String string) { this.type = type; }

}
