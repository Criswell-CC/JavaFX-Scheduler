package Scheduler;

import Scheduler.Utils.DatabaseHandler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 *
 * Main application class responsible for initializing data and launching JavaFX application.
 *
 * @author Chris Criswell
 */

public class Main extends Application {

    /**
     * Standard JavaFX method to load FXML for initial form and show window
     *
     * Two lambda expressions are used in conjunction with method chaining to provide a single line of code to control
     * the flow of the generated event. Specifically, the lambdas provide a shorthand way to show the predicate function
     * of the filter() function, which filters for which buttons the user presses on the alert box, and the ifPresent()
     * uses a lambda expression to show concisely that the propagation of the event should not be passed on through
     * the application.
     *
     * @param primaryStage primary application window automatically passed
     * @throws Exception IO exception if fxml file not found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("./Views/loginForm.fxml"));
        primaryStage.setTitle("Scheduler");
        primaryStage.setScene(new Scene(root, 400, 325));
        primaryStage.centerOnScreen();
        primaryStage.show();

        //https://stackoverflow.com/questions/38371117/close-event-in-java-fx-when-close-button-is-pressed
        //https://stackoverflow.com/questions/41233886/display-a-confirmation-dialogue-when-the-user-try-to-exit-the-application-press
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent event) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);

                alert.showAndWait().filter(response -> response != ButtonType.YES).ifPresent(response->event.consume());

                if (alert.getResult() == ButtonType.YES) {
                    Platform.exit();
                }
            }
        });
    }

    /**
     * Main method initializes database, launches application, and closes the databse connection on program close
     * @param args passed command line arguments
     */
    public static void main(String[] args) {

        DatabaseHandler.getConnection();
        launch(args);
        DatabaseHandler.endConnection();

    }
}