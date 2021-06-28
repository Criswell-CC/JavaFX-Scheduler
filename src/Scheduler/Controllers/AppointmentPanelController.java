package Scheduler.Controllers;

import Scheduler.DAO.AppointmentDAO;
import Scheduler.Models.Appointment;
import Scheduler.Utils.DatabaseHandler;
import Scheduler.Utils.UserSession;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for appointment panel (component of dashboard window) responsible for handling appointment table data
 *
 * @author Chris Criswell
 */
public class AppointmentPanelController implements Initializable {

    /**
     * Main appointment table
     */
    @FXML
    TableView<Appointment> appointmentTableView;

    /**
     * Appointment ID table column
     */
    @FXML
    TableColumn<Appointment, Integer> appointmentIDColumn;
    /**
     * Appointment title table column
     */
    @FXML
    TableColumn<Appointment, String> titleColumn;
    /**
     * Appointment description table column
     */
    @FXML
    TableColumn<Appointment, String> descriptionColumn;
    /**
     * Appointment location table column
     */
    @FXML
    TableColumn<Appointment, String> locationColumn;
    /**
     * Appointment contact table column
     */
    @FXML
    TableColumn<Appointment, String> contactColumn;
    /**
     * Appointment type table column
     */
    @FXML
    TableColumn<Appointment, String> typeColumn;
    /**
     * Appointment start date table column
     */
    @FXML
    TableColumn<Appointment, String> startDateColumn;
    /**
     * Appointment time table column
     */
    @FXML
    TableColumn<Appointment, String> startTimeColumn;
    /**
     * Appointment end date table column
     */
    @FXML
    TableColumn<Appointment, String> endDateColumn;
    /**
     * Appointment end time table column
     */
    @FXML
    TableColumn<Appointment, String> endTimeColumn;
    /**
     * Customer ID for customer associated with appointment table column
     */
    @FXML
    TableColumn<Appointment, Integer> customerIDColumn;

    /**
     * Radio button for showing all appointments
     */
    @FXML
    RadioButton allRadioButton;
    /**
     * Radio button for showing all appointments for the month
     */
    @FXML
    RadioButton monthlyRadioButton;
    /**
     * Radio button for showing all appointments for the week
     */
    @FXML
    RadioButton weeklyRadioButton;

    /**
     * Toggle group controlling radio buttons to set timeframe for appointments to display
     */
    @FXML
    ToggleGroup toggleGroup;

    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * Handles add appointment button click by launching add appointment form window
     * @param event button click event
     */
    @FXML
    private void onAddAppointment(ActionEvent event) {

        try {

            Parent root;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/appointmentForm.fxml"));

            AddAppointmentController controller = new Scheduler.Controllers.AddAppointmentController();

            loader.setController(controller);

            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Appointment");
            stage.setScene(new Scene(root, 450, 500));
            stage.centerOnScreen();
            stage.showAndWait();
            setTable();

        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles update appointment button click by checking if an appointment is selected and launching appointment update
     * form window
     * @param event button click event
     */
    @FXML
    private void onUpdateAppointment(ActionEvent event) {

        if (appointmentTableView.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        }

        else {

            try {

                Parent root;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/appointmentForm.fxml"));

                UpdateAppointmentController controller = new Scheduler.Controllers.UpdateAppointmentController();
                Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
                controller.setAppointmentToAddOrUpdate(appointment);
                loader.setController(controller);

                root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Update Appointment Information");
                stage.setScene(new Scene(root, 450, 500));
                stage.centerOnScreen();
                stage.showAndWait();

                if (DatabaseHandler.databaseUpdated == true) {

                    setTable();
                    allRadioButton.setSelected(true);
                    DatabaseHandler.setDatabaseUpdated(false);

                }
            }

            catch (IOException exception) {
                System.out.println(exception.getMessage());
            }

        }

    }

    /**
     * Handles delete appointment button click by checking if an appointment is selected, showing delete confirmation, and handling
     * database delete operation
     * @param event button click event
     */
    @FXML
    private void onDeleteAppointment(ActionEvent event) {

        if (appointmentTableView.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
        }

        else {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Are you sure you want to delete this appointment?");
            confirmationAlert.showAndWait();

            if (confirmationAlert.getResult() == ButtonType.OK) {

                int appointmentID = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentID();
                String appointmentType = appointmentTableView.getSelectionModel().getSelectedItem().getType();

                AppointmentDAO appointmentDAO = new AppointmentDAO();

                if (appointmentDAO.deleteAppointment(appointmentTableView.getSelectionModel().getSelectedItem())) {

                    Alert confirmationDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDeleteAlert.setContentText("Appointment " + appointmentID + " of type " + appointmentType + " canceled");
                    confirmationDeleteAlert.showAndWait();
                    setTable();

                }

                else {

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error deleting appointment");
                    errorAlert.showAndWait();

                }
            }
        }
    }

    /**
     * Method to check for upcoming appointments through creating a filtered list object with a predicate lambda expression
     * to check if given appointment element has same date and is within a 15 minute time range.
     *
     * The lambda expression allows for a shorter way to show the condition for what elements should be included in the
     * filtered list, bypassing instantiating a Predicate object
     */
    private void checkAppointmentAlert() {

        //lambda
        FilteredList<Appointment> appointmentsToAlert = new FilteredList<>(appointments, appointment -> {

            if (appointment.getUserID() == UserSession.getCurrentUser().getId() && appointment.getStartDateTime().getMonth() == LocalDateTime.now().getMonth() &&
                    appointment.getStartDateTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()
                    && appointment.getStartDateTime().getHour() == LocalDateTime.now().getHour() &&
                    appointment.getStartDateTime().getMinute() >= LocalDateTime.now().getMinute() &&
                    appointment.getStartDateTime().getMinute() <= LocalDateTime.now().getMinute() + 15)
            {
                return true;
            }

            return false;

        });

        if (appointmentsToAlert.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You have no upcoming appointments");
            alert.showAndWait();

        }

        else {

            for (Appointment appointment : appointmentsToAlert) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setMinWidth(450.0);
                alert.getDialogPane().setMinHeight(220.0);
                alert.setContentText("Upcoming appointment alert: Appointment " + appointment.getAppointmentID() + " starts at "
                        + appointment.getStartDateTime().getHour() + ":" + appointment.getStartDateTime().getMinute());
                alert.showAndWait();

            }

        }
    }

    /**
     * Retrieves all appointment data from database and assigns them to TableView object
     */
    public void setTable() {

        appointments = new AppointmentDAO().getAllAppointments();

        if (appointments != null) {

            appointmentTableView.setItems(appointments);

        }

    }

    /**
     * Override of standard JavaFX method to initialize UI. Sets appointment table, initializes columns and toggle group for
     * selected by month/by week views, and checks for upcoming appointments
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTable();

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStartDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEndDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldValue, Toggle newValue) {

                RadioButton selectedButton = (RadioButton)newValue.getToggleGroup().getSelectedToggle();

                if (Objects.equals("All", selectedButton.getText())) {

                    appointmentTableView.setItems(appointments);

                }

                if (Objects.equals("Monthly", selectedButton.getText())) {

                    FilteredList<Appointment> appointmentsByMonth = new FilteredList<>(appointments, appointment -> {

                        if (appointment.getStartDateTime().toLocalDateTime().getMonth() == LocalDateTime.now().getMonth() &&
                                appointment.getStartDateTime().toLocalDateTime().getDayOfMonth() >=
                                        LocalDateTime.now().getDayOfMonth()) {
                            return true;
                        }

                        return false;
                    });

                    appointmentTableView.setItems(appointmentsByMonth);

                }

                if (Objects.equals("Weekly", selectedButton.getText())) {

                    FilteredList<Appointment> appointmentsByWeek = new FilteredList<>(appointments, appointment -> {

                        //https://stackoverflow.com/questions/26012434/get-week-number-of-localdate-java-8/26013129
                        LocalDateTime currentDate = LocalDateTime.now();
                        TemporalField week = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                        int currentWeekNumber = currentDate.get(week);

                        if (appointment.getStartDateTime().toLocalDateTime().getMonth() == currentDate.getMonth() &&
                                appointment.getStartDateTime().toLocalDateTime().get(week) == currentWeekNumber &&
                                appointment.getStartDateTime().toLocalDateTime().getDayOfMonth() >= currentDate.getDayOfMonth()) {
                            return true;
                        }

                        return false;
                    });

                    appointmentTableView.setItems(appointmentsByWeek);

                }
            }
        });

        allRadioButton.setSelected(true);

        checkAppointmentAlert();

    }
}
