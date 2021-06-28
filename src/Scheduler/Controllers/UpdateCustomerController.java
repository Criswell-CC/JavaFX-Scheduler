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
 * Controller class for update customer form
 *
 * @author Chris Criswell
 */
public class UpdateCustomerController extends CustomerController {

    /**
     * Customer object to be updated
     */
    Customer customerToAddOrUpdate;

    /**
     * Save button click handler that validates appointment object and writes to database
     * @param event button click event
     * @return bool indicating success of appointment validation and database write
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

                if (customerDAO.updateCustomer(customerToAdd)) {
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
     * Sets the customer to be updated
     * @param customer customer to be updated
     */
    public void setCustomer(Customer customer) {
        customerToAddOrUpdate = customer;
    }

    /**
     * Method to set UI components, particularly text input fields with given customer information.
     */
    private void setUI() {

        mainLabel.setText("Update Customer Information");

        if (customerToAddOrUpdate != null) {

            enteredID.setText(String.valueOf(customerToAddOrUpdate.getId()));
            enteredName.setText(customerToAddOrUpdate.getCustomerName());
            enteredAddress.setText(customerToAddOrUpdate.getAddress());
            enteredPostalCode.setText(customerToAddOrUpdate.getPostalCode());
            enteredPhone.setText(customerToAddOrUpdate.getPhoneNumber());

            String divisionName = customerToAddOrUpdate.getDivisionName();

            int divisionID = customerToAddOrUpdate.getDivisionID();

            if (divisionID < 55) {
                countryComboBox.getSelectionModel().select("U.S.");
            }

            if (divisionID > 60 && divisionID < 72) {
                divisionComboBox.setItems(new DivisionDAO().getCADivisionList());
                countryComboBox.getSelectionModel().select("Canada");

            }

            if (divisionID > 100 && divisionID < 105) {
                divisionComboBox.setItems(new DivisionDAO().getUKDivisionList());
                countryComboBox.getSelectionModel().select("UK");
            }

            divisionComboBox.getSelectionModel().select(divisionName);


        }

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
