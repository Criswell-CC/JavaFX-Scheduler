package Scheduler.Controllers;

import Scheduler.DAO.AppointmentDAO;
import Scheduler.Models.Appointment;
import Scheduler.Utils.DatabaseHandler;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for add appointment form
 *
 * @author Chris Criswell
 */
public class AddAppointmentController extends AppointmentController {

    /**
     * Indicator for whether a database error occurred generating new appointment ID while setting UI
     */
    boolean generateIDError = false;

    /**
     * Save button click handler that validates appointment object and writes to database
     * @param event button click event
     * @return bool indicating success of appointment validation and database write
     */
    boolean onSave(ActionEvent event) {

        Appointment appointment = prepareForSave();

        if (appointment != null) {

            AppointmentDAO appointmentDAO = new AppointmentDAO();

            if (appointmentDAO.addAppointment(appointment)) {
                DatabaseHandler.setDatabaseUpdated(true);
                return true;
            }

            else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error saving appointment");
                alert.showAndWait();

                return false;

            }

        }

        return false;

    }

    /**
     * Method to set UI components and retrieve automatically generated ID for new appointment. If a database error occurs,
     * class member generateIDError is flagged as true in order for initialize method to handle the error and show
     * an alert
     */
    private void setUI() {

        mainLabel.setText("Add a new appointment");

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        int appointmentID = appointmentDAO.generateAppointmentID();

        if (appointmentID != -1) {

            enteredID.setText(String.valueOf(appointmentID));

        }

        else {

            generateIDError = true;

        }

    }

    /**
     * Override of standard JavaFX method to initialize UI. Sets UI and button handlers and checks if there was a database
     * error with generating automatic appointment ID for new customer
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCommonUI();
        setUI();
        setHandlers();

        //should only happen if no data in database or no connection to database
        if (generateIDError) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Database error generating ID for appointment. Check your connection and try opening the form again.");
            alert.getDialogPane().setMinWidth(450.0);
            alert.getDialogPane().setMinHeight(220.0);
            alert.showAndWait();

        }

    }
}
