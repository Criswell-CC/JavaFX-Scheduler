package Scheduler.Controllers;

import Scheduler.DAO.CustomerDAO;
import Scheduler.DAO.DivisionDAO;
import Scheduler.Models.Customer;
import Scheduler.Utils.DatabaseHandler;
import Scheduler.Utils.UserSession;
import Scheduler.Utils.Validators;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for add customer form
 *
 * @author Chris Criswell
 */
public class AddCustomerController extends CustomerController {

    /**
     * Indicator for whether a database error occurred generating new customer ID while setting UI
     */
    boolean generateIDError = false;

    /**
     * Save button click handler that validates customer object and writes to database
     * @param event button click event
     * @return bool indicating success of customer validation and database write
     */
    public boolean onSave(ActionEvent event) {

        if (!UserSession.checkUserSignedIn()) {
            return false;
        }

        if (Validators.validateCustomerInput(enteredName.getText().trim(),
                enteredAddress.getText().trim(), enteredPostalCode.getText().trim(), enteredPhone.getText().trim(),
                divisionComboBox.getValue().toString())) {

            String name = enteredName.getText().trim();

            //make sure first letter of first name is capitalized
            if (Character.isLowerCase(enteredName.getText().charAt(0))) {

                name = name.substring(0, 1).toUpperCase() + name.substring(1);

            }

            //make sure first letter of last name is capitalized
            if (Character.isLowerCase(name.indexOf(' ') + 1)) {
                name = name.substring(0, name.indexOf(' ')) + name.substring(name.indexOf(' ') + 1, name.indexOf(' ') + 2).toUpperCase()
                        + name.substring(name.indexOf(' ') + 2, name.length() - 1);
            }

            DivisionDAO divisionDAO = new DivisionDAO();

            int divisionID = divisionDAO.getIDByDivision(divisionComboBox.getValue().toString());

            if (divisionID != -1) {

                Customer customerToAdd = new Customer(Integer.parseInt(enteredID.getText().trim()), name,
                        enteredAddress.getText().trim(), enteredPostalCode.getText().trim(), divisionID,
                        enteredPhone.getText().trim());

                CustomerDAO customerDAO = new CustomerDAO();

                if (customerDAO.addCustomer(customerToAdd)) {
                    DatabaseHandler.setDatabaseUpdated(true);
                    return true;
                }

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Database error. Check your connection and try again.");
                alert.showAndWait();

            }
        }

        return false;

    }

    /**
     * Method to set UI components and retrieve automatically generated ID for new user. If a database error occurs,
     * class member generateIDError is flagged as true in order for initialize method to handle the error and show
     * an alert
     */
    private void setUI() {

        mainLabel.setText("Add New Customer Information");

        int customerID = new CustomerDAO().generateCustomerID();

        if (customerID != -1) {

            enteredID.setText(String.valueOf(customerID));

        }

        else {

            generateIDError = true;

        }

    }

    /**
     * Override of standard JavaFX method to initialize UI. Sets UI and button handlers and checks if there was a database
     * error with generating automatic user ID for new customer
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
            alert.setContentText("Database error generating ID for customer. Check your connection and try opening the form again.");
            alert.getDialogPane().setMinWidth(450.0);
            alert.getDialogPane().setMinHeight(220.0);
            alert.showAndWait();

        }

    }
}
