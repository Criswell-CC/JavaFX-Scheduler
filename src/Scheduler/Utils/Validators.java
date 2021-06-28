package Scheduler.Utils;

import Scheduler.DAO.AppointmentDAO;
import Scheduler.DAO.CustomerDAO;
import Scheduler.DAO.DivisionDAO;
import Scheduler.DAO.UserDAO;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * Utility class with static methods for validating user input across forms.
 *
 * @author Chris Criswell
 */
public class Validators {

    //https://regexlib.com/REDetails.aspx?regexp_id=21
    public static String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    //https://stackoverflow.com/questions/2385701/regular-expression-for-first-and-last-name
    //allows for international names
    public static String namePattern = "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð .'-]+$";

    //https://regexlib.com/REDetails.aspx?regexp_id=58
    public static String phonePattern = "^([0-9]( |-)?)?(\\(?[0-9]{3}\\)?|[0-9]{3})( |-)?([0-9]{3}( |-)?[0-9]{4}|[a-zA-Z0-9]{7})$";

    //https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s14.html
    public static String usPostalCodePattern = "^[0-9]{5}(?:-[0-9]{4})?$";

    //https://regexlib.com/Search.aspx?k=canadian+postal+code
    public static String canadaPostalCodePattern = "^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$";

    //https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s16.html
    public static String ukPostalCodePattern = "^[A-Z]{1,2}[0-9R][0-9A-Z]?●[0-9][ABD-HJLNP-UW-Z]{2}$";

    public static String datePattern = "^(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";

    public static String timePattern = "^([1-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

    public static String positiveIntegerPattern = "^[1-9]\\d*$";

    /**
     * Method to validate address input field: checks whether address starts with a street number and doesn't contain
     * any divisions/states
     * @param address user-provided address string
     * @return bool indicating result of address validation
     */
    private static boolean validateAddress(String address) {

        DivisionDAO divisionDAO = new DivisionDAO();

        if (!Character.isDigit(address.charAt(0)) || Character.getNumericValue(address.charAt(0)) == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid street number");
            alert.showAndWait();
            return false;
        }

        ObservableList<String> listOfDivisions = divisionDAO.getAllDivisionList();

        for (String division : listOfDivisions) {
            if (address.contains(", " + division + ", ")) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter state or division data through combo box");
                alert.showAndWait();
                return false;

            }
        }

        return true;
    }

    /**
     * Method to validate postal code input
     * @param postalCode user-provided postal code string
     * @return bool indicating result of postal code validation
     */
    private static boolean validatePostalCode(String postalCode) {

        if (postalCode.matches(usPostalCodePattern) || postalCode.matches(canadaPostalCodePattern) || postalCode.matches(ukPostalCodePattern)) {

            return true;

        }

        return false;
    }

    /**
     * Method to validate all fields for customer form
     * @param name user-provided name string
     * @param address user-provided address string
     * @param postalCode user-provided postal code string
     * @param phoneNumber user-provided phone number string
     * @param divisionID automatically generated divisionID from combo box
     * @return bool indicating result of customer input validation
     */
    public static boolean validateCustomerInput(String name, String address, String postalCode, String phoneNumber,
                                                String divisionID) {

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phoneNumber.isEmpty() ||
            divisionID.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please give each field a value");
            alert.showAndWait();
            return false;

        }

        if (!name.matches(namePattern)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid name");
            alert.showAndWait();
            return false;

        }

        int numSpaces = 0;

        //https://stackoverflow.com/questions/9655753/how-to-count-the-spaces-in-a-java-string
        for (char space : name.toCharArray()) {

            if (space == ' ') {
                numSpaces++;
            }

        }

        if (numSpaces == 0) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter both a first and last name");
            alert.showAndWait();
            return false;

        }

        if (numSpaces > 1) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter only first and last name");
            alert.showAndWait();
            return false;

        }

        if (!phoneNumber.matches(phonePattern)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please entere a valid phone number");
            alert.showAndWait();
            return false;

        }

        if (!validateAddress(address)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid address");
            alert.showAndWait();
            return false;

        }

        if (!validatePostalCode(postalCode)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid postal code");
            alert.showAndWait();
            return false;
        }

        return true;

    }

    /**
     * Method to validate all fields from appointment input form, including checking whether appointment start and end
     * are within business hours
     * @param appointmentID int value of automatically generated appointment ID
     * @param title user-provided title string
     * @param description user-provided description string
     * @param location user-provided location
     * @param type user-provided type
     * @param startTime time and date of appointment start with user's timezone information
     * @param endTime time and date of appointment end with user's timezone information
     * @param customerID int value for ID of customer associated with appointment
     * @param userID int value for ID of user who created appointment
     * @return bool indicating success of appointment input validation
     */
    public static boolean validateAppointmentInput(int appointmentID, String title, String description, String location,
                                                   String type, ZonedDateTime startTime, ZonedDateTime endTime,
                                                   String customerID, String userID) {

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() || customerID.isEmpty() || userID.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please give each field a value");
            alert.showAndWait();
            return false;

        }

        if (!userID.matches(positiveIntegerPattern)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid user ID number");
            alert.showAndWait();
            return false;

        }

        if (!customerID.matches(positiveIntegerPattern)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid user ID number");
            alert.showAndWait();
            return false;

        }

        UserDAO userDAO = new UserDAO();

        if (!userDAO.checkUserIDValid(Integer.parseInt(userID))) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid user ID");
            alert.showAndWait();
            return false;

        }

        CustomerDAO customerDAO = new CustomerDAO();

        if (!customerDAO.checkCustomerIDValid(Integer.parseInt(customerID))) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid customer ID number");
            alert.showAndWait();
            return false;

        }

        if (startTime.isAfter(endTime)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Start time cannot be after end time");
            alert.showAndWait();
            return false;

        }

        ZonedDateTime startAppointmentTimeInET = startTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endAppointmentTimeInET = endTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        if (startAppointmentTimeInET.getHour() < 8 ||  endAppointmentTimeInET.getHour() < 8 ||
                startAppointmentTimeInET.getHour() >= 22 || endAppointmentTimeInET.getHour() >= 22 ||
                startAppointmentTimeInET.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                startAppointmentTimeInET.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                endAppointmentTimeInET.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                endAppointmentTimeInET.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setMinWidth(450.0);
            alert.getDialogPane().setMinHeight(180.0);
            alert.setContentText("Appointment time cannot be scheduled outside of business hours or on weekends.");
            alert.showAndWait();
            return false;
        }

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        if (appointmentDAO.checkIfOtherAppointmentsInRange(appointmentID, Integer.parseInt(customerID), startTime, endTime)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An appointment is already scheduled in entered time slot");
            alert.showAndWait();
            return false;

        }

        return true;

    }

    /**
     * Method to validate user-entered date and time strings for appointment form
     * @param startDate string of appointment start date in month-year format
     * @param startTime string of appointment start time in military time
     * @param endDate string of appointment end date in month-year format
     * @param endTime string of appointment end time in military time
     * @return bool indicating success of date validation
     */
    public static boolean validateDates(String startDate, String startTime, String endDate, String endTime) {

        if (startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endDate.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please give each field a value");
            alert.showAndWait();
            return false;

        }

        if (!startDate.matches(datePattern) || !endDate.matches(datePattern)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure dates are of format: 'MM-dd'");
            alert.showAndWait();
            return false;

        }

        if (!startTime.matches(timePattern) || !endTime.matches(timePattern)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure entered times are of format: 'h:mm'");
            alert.showAndWait();
            return false;

        }

        return true;
    }

    /**
     * Method to check if a username is provided on login screen
     * @param username user-provided username string
     * @return bool indicating success of username validation
     */
    public static boolean validateUsername(String username) {

        if (username.isEmpty()) {
            return false;
        }

        return true;

    }

    /**
     * Method to check if a password is provided on login screen
     * @param password user-provided password string
     * @return bool indicating success of username validation
     */
    public static boolean validatePassword(String password) {

        if (password.isEmpty()) {
            return false;
        }

        return true;

    }

}
