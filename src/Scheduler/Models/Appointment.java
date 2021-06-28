package Scheduler.Models;

import Scheduler.DAO.ContactDAO;
import Scheduler.DAO.CustomerDAO;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for appointment objects
 *
 * @author Chris Criswell
 */

public class Appointment {

    /**
     * appointment ID
     */
    private int appointmentID;
    /**
     * ID of customer associated with appointment
     */
    private int customerID;
    /**
     * ID of contact associated with appointment
     */
    private int contactID;

    /**
     * id of user who created appointment
     */
    private int userID;

    /**
     * Title of appointment
     */
    private String title;
    /**
     * Description of appointment
     */
    private String description;
    /**
     * Location of appointment
     */
    private String location;
    /**
     * Type of appointment
     */
    private String type;

    /**
     * Name of contact associated with appointment
     */
    private String contactName;
    /**
     * Name of customer associated with appointment
     */
    private String customerName;

    /**
     * Date and time of appointment start with user's timezone information
     */
    private ZonedDateTime startDateTime;
    /**
     * Date and time of appointment end with user's timezone information
     */
    private ZonedDateTime endDateTime;

    /**
     * Formatted date of appointment start in user's timezone
     */
    private String formattedStartDate;
    /**
     * Formatted date of appointment end in user's timezone
     */
    private String formattedEndDate;
    /**
     * Formatted time of appointment start in user's timezone
     */
    private String formattedStartTime;
    /**
     * Formatted time of appointment end in user's timezone
     */
    private String formattedEndTime;

    /**
     * Appointment constructor
     * @param appointmentID ID of appointment
     * @param customerID ID of customer associated with appointment
     * @param contactID ID of contact associated with appointment
     * @param userID ID of user who created appointment
     * @param title title of appointment
     * @param description description of appointment
     * @param location location of appointment
     * @param type type of appointment
     * @param startDateTime appointment start date/time with user timezone information
     * @param endDateTime appointment end date/time with user timezone information
     */
    public Appointment(int appointmentID, int customerID, int contactID, int userID, String title, String description,
                       String location, String type, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {

        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.contactID = contactID;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;

        setDates();
        setNamesFromDatabase();

    }

    /**
     * Accessor for appointment ID
     * @return appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Accessor for customer ID
     * @return customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Accessor for contact ID
     * @return contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Accessor for user ID
     * @return user ID
     */
    public int getUserID() { return userID; }

    /**
     * Accessor for appointment title
     * @return appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Accessor for appointment description
     * @return appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Accessor for appointment location
     * @return appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Accessor for appointment type
     * @return appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * Accessor for contact name
     * @return contact name
     */
    public String getContactName() { return contactName; }

    /**
     * Accessor for customer name
     * @return customer name
     */
    public String getCustomerName() { return customerName; }

    /**
     * Accessor for formatted start date
     * @return formatted start date
     */
    public String getFormattedStartDate() { return formattedStartDate; }

    /**
     * Accessor for formatted end date
     * @return formatted end date
     */
    public String getFormattedEndDate() { return formattedEndDate; }

    /**
     * Accessor for formatted start time
     * @return formatted start time
     */
    public String getFormattedStartTime() { return formattedStartTime; }

    /**
     * Accessor for formatted end time
     * @return formatted end time
     */
    public String getFormattedEndTime() { return formattedEndTime; }

    /**
     * Accessor for start date time
     * @return start date time
     */
    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Accessor for end date time
     * @return formatted end date time
     */
    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Mutator for appointment ID
     * @param appointmentID int value of appointment ID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Mutator for customer ID
     * @param customerID customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Mutator for contact ID
     * @param contactID contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Mutator for user ID
     * @param userID user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Mutator for contact name
     * @param contactName contact name
     */
    public void setContactName(String contactName) { this.customerName = contactName; }

    /**
     * Mutator for customer name
     * @param customerName customer name
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     * Mutator for appointment title
     * @param title appointment title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Mutator for appointment description
     * @param description appointment description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Mutator for appointment location
     * @param location appointment location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Mutator for appointment type
     * @param type appointment type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Mutator for start date time with timezone information
     * @param startDateTime Mutator for start date time with timezone information
     */
    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Mutator for end date time with timezone information
     * @param endDateTime end date time with timezone information
     */
    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Formats start and end date times into strings
     */
    private void setDates() {

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("MM-dd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:mm");

        this.formattedStartDate = startDateTime.format(formatterDate);
        this.formattedEndDate = endDateTime.format(formatterDate);

        this.formattedStartTime = startDateTime.format(formatterTime);
        this.formattedEndTime = endDateTime.format(formatterTime);

    };

    /**
     * Sets customer and contact name strings given contact and customer IDs
     */
    private void setNamesFromDatabase() {

        CustomerDAO customerDAO = new CustomerDAO();
        this.customerName = customerDAO.getCustomerNameByID(customerID);

        ContactDAO contactDAO = new ContactDAO();
        this.contactName = contactDAO.getContactNameByID(contactID);

    }

}
