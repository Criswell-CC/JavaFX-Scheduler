package Scheduler.Controllers;

import Scheduler.Utils.DatabaseHandler;
import Scheduler.Utils.LoginTracker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for login window
 *
 * @author Chris Criswell
 */
public class LoginFormController implements Initializable {

    /**
     * Error message string to display on failed login attempt
     */
    private String errorMessage;

    /**
     * Confirmation message to show in alert before exiting
     */
    private String exitConfirmationMessage;

    /**
     * Main text for login form
     */
    @FXML
    private Label loginLabel;
    /**
     * Text to show user timezone information
     */
    @FXML
    private Label timezoneLabel;
    /**
     * Text to show error message
     */
    @FXML
    private Label errorLabel;

    /**
     * Text input field for username string
     */
    @FXML
    private TextField usernameText;
    /**
     * Text input field for password
     */
    @FXML
    private PasswordField passwordText;

    /**
     * Login button
     */
    @FXML
    private Button loginButton;
    /**
     * Exit button
     */
    @FXML
    private Button exitButton;

    /**
     * Login button click handler that checks username and password against database, logs login attempt, and loads/shows main
     * dashboard window if login successful
     * @param event button click event
     */
    private void onLogin(ActionEvent event) {

        boolean result = DatabaseHandler.login(usernameText.getText(), passwordText.getText());

        LoginTracker.getLoginTracker().logActivity(usernameText.getText(), result);

        if (result) {

            try {

                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                Parent root;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/dashboard.fxml"));

                root = loader.load();

                stage.setScene(new Scene(root, 750, 600));
                stage.centerOnScreen();
                stage.show();

            }

            catch (IOException exception) {
                System.out.println(exception.getStackTrace());
                System.out.println(exception.getMessage());
            }
        }

        else {
            errorLabel.setText(errorMessage);
        }

    }

    /**
     * Exit button click handler showing confirmation alert and closes program
     * @param event button click event
     */
    private void onExit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, exitConfirmationMessage, ButtonType.YES, ButtonType.CANCEL);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

    /**
     * Method to set button handlers
     */
    private void setHandlers() {

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onLogin(event);
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onExit(event);
            }
        });

    }

    /**
     * Override of standard JavaFX method to initialize UI. Handles internationalization by setting UI text through language
     * resource bundles.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Scheduler.application", Locale.getDefault());

        //for easy testing
        //ResourceBundle resourceBundle = ResourceBundle.getBundle("Scheduler.application", Locale.FRENCH);

        errorMessage = resourceBundle.getString("loginError");

        loginLabel.setText(resourceBundle.getString("mainLabel"));
        timezoneLabel.setText("Current timezone: " + ZoneId.systemDefault().toString());

        loginButton.setText(resourceBundle.getString("loginButton"));
        exitButton.setText(resourceBundle.getString("exitButton"));

        usernameText.setPromptText(resourceBundle.getString("username"));
        passwordText.setPromptText(resourceBundle.getString("password"));

        exitConfirmationMessage = resourceBundle.getString("exitConfirmationMessage");

        if (resourceBundle.getLocale().toString().equals("en")) {

            usernameText.setPrefWidth(170.0);
            passwordText.setPrefWidth(170.0);

        }

        //for French
        else {

            usernameText.setPrefWidth(220.0);
            passwordText.setPrefWidth(220.0);

        }

        setHandlers();

    }
}
