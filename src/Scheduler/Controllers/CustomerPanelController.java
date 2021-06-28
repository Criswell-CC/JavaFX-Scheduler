package Scheduler.Controllers;

import Scheduler.DAO.CustomerDAO;
import Scheduler.Models.Customer;

import Scheduler.Utils.DatabaseHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for customer panel on dashboard that handles initializing customer table and handling button clicks
 *
 * @author Chris Criswell
 */
public class CustomerPanelController implements Initializable {

    /**
     * Main customer table
     */
    @FXML
    TableView<Customer> customerTableView;

    /**
     * Customer ID table column
     */
    @FXML
    TableColumn<Customer, Integer> customerIDColumn;
    /**
     * Customer name table column
     */
    @FXML
    TableColumn<Customer, String> customerNameColumn;
    /**
     * Customer address table column
     */
    @FXML
    TableColumn<Customer, String> addressColumn;
    /**
     * Customer postal code table column
     */
    @FXML
    TableColumn<Customer, String> postalCodeColumn;
    /**
     * Customer division name table column
     */
    @FXML
    TableColumn<Customer, String> divisionNameColumn;
    /**
     * Customer country name table column
     */
    @FXML
    TableColumn<Customer, String> countryNameColumn;
    /**
     * Customer phone table column
     */
    @FXML
    TableColumn<Customer, String> phoneColumn;

    AppointmentPanelController appointmentPanelController;

    /**
     * Mutator to set reference to linked appointment panel controller
     * @param controller appointment panel controller object on same dashboard
     */
    public void setAppointmentPanelController(AppointmentPanelController controller) {
        this.appointmentPanelController = controller;
    }

    /**
     * Handles add customer button click by launching add customer form window
     * @param event button click event
     */
    @FXML
    private void onAddCustomer(ActionEvent event) {

        try {

            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/customerForm.fxml"));

            loader.setController(new Scheduler.Controllers.AddCustomerController());

            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Customer");
            stage.setScene(new Scene(root, 450, 450));
            stage.centerOnScreen();
            stage.showAndWait();

            if (DatabaseHandler.databaseUpdated == true) {

                setTable();
                DatabaseHandler.setDatabaseUpdated(false);

            }

        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles update customer button click by checking if a customer is selected and launching customer update form window
     * @param event button click event
     */
    @FXML
    private void onUpdateCustomer(ActionEvent event) {

        if (customerTableView.getSelectionModel().isEmpty() == true) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();

        }

        else {

            try {

                Parent root;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/customerForm.fxml"));

                UpdateCustomerController controller = new Scheduler.Controllers.UpdateCustomerController();
                Customer customer = customerTableView.getSelectionModel().getSelectedItem();
                controller.setCustomer(customer);
                loader.setController(controller);

                root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Update Customer Information");
                stage.setScene(new Scene(root, 450, 450));
                stage.centerOnScreen();
                stage.showAndWait();

                if (DatabaseHandler.databaseUpdated == true) {
                    setTable();
                    DatabaseHandler.setDatabaseUpdated(false);
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Handles delete customer button click by checking if a customer is selected, showing delete confirmation, and handling
     * database delete operation
     * @param event button click event
     */
    @FXML
    private void onDeleteCustomer(ActionEvent event) {

        if (customerTableView.getSelectionModel().isEmpty() == true) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();

        }

        else {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Are you sure you want to delete this customer?");
            confirmationAlert.showAndWait();

            if (confirmationAlert.getResult() == ButtonType.OK) {

                int customerID = customerTableView.getSelectionModel().getSelectedItem().getId();

                CustomerDAO customerDAO = new CustomerDAO();

                if (customerDAO.deleteCustomer(customerTableView.getSelectionModel().getSelectedItem())) {
                    Alert confirmationDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDeleteAlert.setContentText("Customer " + customerID + " deleted");
                    confirmationDeleteAlert.showAndWait();
                    setTable();
                    appointmentPanelController.setTable();
                }

                else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error deleting customer");
                    errorAlert.showAndWait();
                }

            }
        }

    }

    /**
     * Retrieves all customer data from database and assigns them to TableView object
     */
    private void setTable() {

        CustomerDAO customerDAO = new CustomerDAO();

        ObservableList<Customer> customers = customerDAO.getAllCustomers();

        if (customers != null) {

            customerTableView.setItems(customerDAO.getAllCustomers());

        }
    }

    /**
     * Override of standard JavaFX method to initialize UI. Sets customer table and initializes columns
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTable();

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionNameColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        countryNameColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

    }
}
