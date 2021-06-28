package Scheduler.Controllers;

import Scheduler.DAO.ContactDAO;
import Scheduler.Models.Appointment;
import Scheduler.Utils.UserSession;
import Scheduler.Utils.Validators;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract controller class for appointment input forms, handling setting common UI and button handlers.
 *
 * @author Chris Criswell
 */
public abstract class AppointmentController implements Initializable {

    /**
     * Appointment object used for loading existing appointments into update form and for saving to database
     */
    Appointment appointmentToAddOrUpdate;

    /**
     * Main header for appointment forms
     */
    @FXML
    Label mainLabel;

    /**
     * Button for cancelling and closing window
     */
    @FXML
    Button cancelButton;
    /**
     * Button for saving user input to database
     */
    @FXML
    Button saveButton;

    /**
     * Input field for appointment ID
     */
    @FXML
    TextField enteredID;
    /**
     * Input field for appointment title
     */
    @FXML
    TextField enteredTitle;
    /**
     * Input field for appointment description
     */
    @FXML
    TextField enteredDescription;
    /**
     * Input field for appointment location
     */
    @FXML
    TextField enteredLocation;
    /**
     * Input field for appointment type
     */
    @FXML
    TextField enteredType;
    /**
     * Input field for appointment start date
     */
    @FXML
    TextField enteredStartDate;
    /**
     * Input field for appointment start time
     */
    @FXML
    TextField enteredStartTime;
    /**
     * Input field for appointment end date
     */
    @FXML
    TextField enteredEndDate;
    /**
     * Input field for appointment end time
     */
    @FXML
    TextField enteredEndTime;
    /**
     * Input field for customer ID associated with appointment
     */
    @FXML
    TextField enteredCustomerID;
    /**
     * Input field for user ID of creator of appointment
     */
    @FXML
    TextField enteredUserID;

    /**
     * Combo box that lists available contacts
     */
    @FXML
    ComboBox contactComboBox;

    /**
     * Start date and time with user's timezone information
     */
    ZonedDateTime startDateTime;
    /**
     * End date and time with user's timezone information
     */
    ZonedDateTime endDateTime;

    /**
     * Accessor for class appointment object
     * @return current appointment object associated with input form
     */
    public Appointment getAppointmentToAddOrUpdate() {
        return appointmentToAddOrUpdate;
    }

    /**
     * Access for zoned start date and time object
     * @return zoned start date and time of appointment
     */
    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Access for zoned end date and time object
     * @return zoned end date and time of appointment
     */
    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Mutator for zoned start date and time object
     * @param startDateTime ZonedDateTime object representing start date and time of appointment
     */
    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Mutator for zoned end date and time object
     * @param endDateTime ZonedDateTime object representing end date and time of appointment
     */
    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Mutator for appointmentToAddOrUpdate object
     * @param appointment appointment object representing appointment associated with given input form
     */
    public void setAppointmentToAddOrUpdate(Appointment appointment) {

        appointmentToAddOrUpdate = appointment;

    }

    /**
     * Abstract save button handler
     * @param event button click event
     * @return bool indicating success of save operation
     */
    abstract boolean onSave(ActionEvent event);

    /**
     * Cancel button handler that prompts cancel confirmation and closes current window
     *
     * Two lambda expressions are used in conjunction with method chaining to provide a single line of code to control
     * the flow of the generated event. Specifically, the lambdas provide a shorthand way to show the predicate function
     * of the filter() function, which filters for which buttons the user presses on the alert box, and the ifPresent()
     * uses a lambda expression to show concisely that the propagation of the event should not be passed on through
     * the application.
     *
     * https://stackoverflow.com/questions/41233886/display-a-confirmation-dialogue-when-the-user-try-to-exit-the-application-press
     *
     * @param event button click event
     */
    public void onCancel(ActionEvent event)  {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?", ButtonType.YES, ButtonType.CANCEL);

        alert.showAndWait().filter(response -> response != ButtonType.YES).ifPresent(response->event.consume());

        if (alert.getResult() == ButtonType.YES) {
            onFinish(event);
        }
    }

    /**
     * Method to perform further operations after onSave and onCancel handlers are called; currently simply closes the window
     * @param event button click event
     */
    public void onFinish(ActionEvent event) {

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    /**
     * Initializes UI components common to all appointment forms
     */
    public void setCommonUI() {

        ContactDAO contactDAO = new ContactDAO();

        ObservableList<String> contactNames = contactDAO.getAllContactNames();

        if (contactNames != null) {

            contactComboBox.setItems(contactNames);

        }

        if (UserSession.getCurrentUser() != null) {

            String userID = Integer.toString(UserSession.getCurrentUser().getId());
            enteredUserID.setText(userID);

        }

    }

    /**
     * Sets button handlers common to all appointment forms
     *
     *  Lambda expressions are used with the setOnAction() method to bypass instantiating a EventHandler class and overriding
     *  a handle method.
     */
    public void setHandlers() {

        saveButton.setOnAction(event -> {
            if (onSave(event)) {
                onFinish(event);
            }
        });

        cancelButton.setOnAction(event->{
            onCancel(event);
        });
    }

    /**
     * Validates user-provided input strings for dates and times and sets class dates objects
     * @return bool indicating input strings are correct format and date objects successfully created
     */
    public boolean setDates() {

        if (!Validators.validateDates(enteredStartDate.getText().trim(), enteredStartTime.getText().trim(),
                enteredEndDate.getText().trim(), enteredEndTime.getText().trim())) {

            return false;

        }

        try {

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M-d-yyyy H:mm");
            LocalDateTime startLocal = LocalDateTime.parse(enteredStartDate.getText() + "-" + LocalDateTime.now().getYear() +
                    " " + enteredStartTime.getText(), dateFormat);
            LocalDateTime endLocal = LocalDateTime.parse(enteredEndDate.getText() + "-" + LocalDateTime.now().getYear() + " "
                    + enteredEndTime.getText(), dateFormat);

            ZonedDateTime start = startLocal.atZone(ZoneId.systemDefault());
            ZonedDateTime end = endLocal.atZone(ZoneId.systemDefault());

            setStartDateTime(start);
            setEndDateTime(end);

            return true;

        }

        catch (Exception exception) {

            System.out.println(exception.getMessage());

        }

        return false;

    }

    /**
     * Validates user input by calling static Validators method
     * @return bool indicating success of appointment form validation
     */
    public boolean validateInput() {

        if (!Validators.validateAppointmentInput(Integer.parseInt(enteredID.getText()), enteredTitle.getText().trim(),
                enteredDescription.getText().trim(), enteredLocation.getText().trim(), enteredType.getText().trim(),
                startDateTime, endDateTime, enteredCustomerID.getText().trim(), enteredUserID.getText().trim())) {

            return false;

        }

        return true;

    }

    /**
     * Method to check if user is logged in, validate user input, and create appointment object to be saved in database
     * @return appointment object initialized with user-provided data or null if error
     */
    public Appointment prepareForSave() {

        if (!UserSession.checkUserSignedIn()) {
            return null;
        }

        if (setDates() && validateInput()) {

            int userID = UserSession.getCurrentUser().getId();

            int contactID = new ContactDAO().getContactIDByName(contactComboBox.getValue().toString());

            Appointment appointment = new Appointment(Integer.parseInt(enteredID.getText()),
                    Integer.parseInt(enteredCustomerID.getText()), contactID, userID, enteredTitle.getText().trim(),
                    enteredDescription.getText().trim(), enteredLocation.getText().trim(), enteredType.getText().trim(),
                    startDateTime, endDateTime);

            return appointment;

        }

        return null;

    }
}
