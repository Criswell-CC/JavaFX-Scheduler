package Scheduler.Controllers;

import Scheduler.DAO.CountryDAO;
import Scheduler.DAO.DivisionDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * Abstract controller class for customer input forms, handling setting common UI and button handlers.
 *
 * @author Chris Criswell
 */
public abstract class CustomerController implements Initializable {

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

    @FXML
    ComboBox countryComboBox;
    @FXML
    ComboBox divisionComboBox;

    /**
     * Main header for customer forms
     */
    @FXML
    Label mainLabel;

    /**
     * Input field for customer ID
     */
    @FXML
    TextField enteredID;
    /**
     * Input field for customer name
     */
    @FXML
    TextField enteredName;
    /**
     * Input field for customer address
     */
    @FXML
    TextField enteredAddress;
    /**
     * Input field for customer postal code
     */
    @FXML
    TextField enteredPostalCode;
    /**
     * Input field for customer phone number
     */
    @FXML
    TextField enteredPhone;

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
    public void onCancel(ActionEvent event) {

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
     * Initializes UI components common to all customer forms, including setting country and division combo boxes
     *
     * A lambda expression makes the setOnAction() method call for the countryComboBox more readable and easy-to-follow
     * by by-passing the full instantiation of an EventHandler object and not having to provide an override of the handle()
     * method.
     */
    public void setCommonUI() {

        countryComboBox.setItems(new CountryDAO().getAllCountries());

        //should be US
        countryComboBox.getSelectionModel().selectFirst();

        DivisionDAO divisionDAO = new DivisionDAO();

        divisionComboBox.setItems(divisionDAO.getUSDivisionList());

        divisionComboBox.getSelectionModel().selectFirst();

        //lambda expression
        countryComboBox.setOnAction(event -> {

            switch (countryComboBox.getValue().toString()) {

                case "U.S":

                    divisionComboBox.setItems(divisionDAO.getUSDivisionList());
                    divisionComboBox.getSelectionModel().selectFirst();
                    break;

                case "Canada":
                    divisionComboBox.setItems(divisionDAO.getCADivisionList());
                    divisionComboBox.getSelectionModel().selectFirst();
                    break;

                case "UK":
                    divisionComboBox.setItems(divisionDAO.getUKDivisionList());
                    divisionComboBox.getSelectionModel().selectFirst();
                    break;

                default:
                    divisionComboBox.setItems(null);
                    break;

            }
        });
    }

    /**
     * Sets button handlers common to all customer forms
     *
     * Lambda expressions are used with the setOnAction() method to bypass instantiating a EventHandler class and overriding
     * a handle method.
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

}
