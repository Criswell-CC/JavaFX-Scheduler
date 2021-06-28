package Scheduler.Controllers;

import Scheduler.DAO.AppointmentDAO;
import Scheduler.DAO.ContactDAO;
import Scheduler.Models.Appointment;
import Scheduler.Utils.DatabaseHandler;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for update appointment form
 *
 * @author Chris Criswell
 */
public class UpdateAppointmentController extends AppointmentController {

    /**
     * Save button click handler that validates appointment object and writes to database
     * @param event button click event
     * @return bool indicating success of appointment validation and database write
     */
    boolean onSave(ActionEvent event) {

        Appointment appointment = prepareForSave();

        if (appointment != null) {

            AppointmentDAO appointmentDAO = new AppointmentDAO();

            if (appointmentDAO.updateAppointment(appointment)) {
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
     * Method to set UI components, particularly text input fields with given appointment information.
     */
    private void setUI() {

        mainLabel.setText("Update appointment information");

        enteredID.setText(String.valueOf(appointmentToAddOrUpdate.getAppointmentID()));
        enteredTitle.setText(appointmentToAddOrUpdate.getTitle());
        enteredDescription.setText(appointmentToAddOrUpdate.getDescription());
        enteredLocation.setText(appointmentToAddOrUpdate.getLocation());
        enteredType.setText(appointmentToAddOrUpdate.getType());
        enteredCustomerID.setText(String.valueOf(appointmentToAddOrUpdate.getCustomerID()));
        enteredUserID.setText(String.valueOf(appointmentToAddOrUpdate.getUserID()));

        ContactDAO contactDAO = new ContactDAO();

        String contactName = contactDAO.getContactNameByID(appointmentToAddOrUpdate.getContactID());

        if (contactName != null) {

            contactComboBox.getSelectionModel().select(contactName);

        }

        enteredStartDate.setText(appointmentToAddOrUpdate.getFormattedStartDate());

        enteredStartTime.setText(appointmentToAddOrUpdate.getFormattedStartTime());

        enteredEndDate.setText(appointmentToAddOrUpdate.getFormattedEndDate());

        enteredEndTime.setText(appointmentToAddOrUpdate.getFormattedEndTime());

    }

    /**
     * Override of standard JavaFX method to initialize UI. Sets UI and button handlers
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCommonUI();
        setUI();
        setHandlers();

    }

}
