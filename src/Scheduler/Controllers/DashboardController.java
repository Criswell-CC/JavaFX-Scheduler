package Scheduler.Controllers;

import Scheduler.DAO.ReportsDAO;
import Scheduler.Models.Appointment;
import Scheduler.Models.MonthlyAppointmentsByTypeCounter;
import Scheduler.Utils.TableHelper;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for main application dashboard
 *
 * @author Chris Criswell
 */
public class DashboardController implements Initializable {

    @FXML
    AppointmentPanelController appointmentPanelController;
    @FXML
    CustomerPanelController customerPanelController;

    /**
     * Handler for menu item report click for generating monthly appointments for customers
     * @param event menu item click event
     */
    @FXML
    public void onMonthlyCustomerAppointmentsByType(ActionEvent event) {

        try {

            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/reports.fxml"));

            ReportsController monthlyCustomerAppointmentsController = new ReportsController();

            TableView<MonthlyAppointmentsByTypeCounter> monthlyCustomerAppointmentsTable = TableHelper.initializeMonthlyReportTable();

            String[] columnOrder = {"Month", "Type", "Total"};

            monthlyCustomerAppointmentsTable = TableHelper.orderTable(columnOrder, monthlyCustomerAppointmentsTable);

            ObservableList<MonthlyAppointmentsByTypeCounter> reportData = new ReportsDAO().getMonthlyCustomerAppointmentsByType();

            for (MonthlyAppointmentsByTypeCounter report : reportData) {
                monthlyCustomerAppointmentsTable.getItems().add(report);
            }

            monthlyCustomerAppointmentsController.setTable(monthlyCustomerAppointmentsTable);
            monthlyCustomerAppointmentsController.setTableInsetValues(new double[]{0.0, 0, 0.0, 165.0});
            monthlyCustomerAppointmentsController.setMainLabelText("Customer Appointments by Month Report");

            loader.setController(monthlyCustomerAppointmentsController);

            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Monthly Customer Appointments by Type");
            stage.setScene(new Scene(root, 560, 450));
            stage.centerOnScreen();
            stage.show();

        }

        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Handler for menu item report click for generating schedules for all contacts
     * @param event menu item click event
     */
    @FXML
    public void onSchedulesForAllContacts(ActionEvent event) {

        try {

            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/reports.fxml"));

            ReportsController schedulesForAllContactsController = new ReportsController();

            TableView<Appointment> scheduleForAllContactsTable = TableHelper.initializeAppointmentTable();

            String[] columnOrder = {"Contact ID", "Contact Name", "Appointment ID", "Title", "Type", "Description", "Location", "Customer ID", "Start Date", "Start Time", "End Date", "End Time"};

            scheduleForAllContactsTable = TableHelper.orderTable(columnOrder, scheduleForAllContactsTable);

            ObservableList<Appointment> reportData = new ReportsDAO().getAppointmentsByContact();

            for (Appointment appointment : reportData) {
                scheduleForAllContactsTable.getItems().add(appointment);
            }

            schedulesForAllContactsController.setTable(scheduleForAllContactsTable);
            schedulesForAllContactsController.setMainLabelText("Monthly Schedule per Contact Report");

            loader.setController(schedulesForAllContactsController);

            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Monthly Schedule for Contacts");
            stage.setScene(new Scene(root, 560, 450));
            stage.centerOnScreen();
            stage.show();

        }

        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Handler for menu item report click for generating all appointments ordered by IDs of users who created them
     * @param event menu item click event
     */
    @FXML
    public void onAppointmentsByUserCreated(ActionEvent event) {

        try {

            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/reports.fxml"));

            ReportsController appointmentsByUserCreatedController = new ReportsController();

            TableView<Appointment> appointmentsByUserCreatedTable = TableHelper.initializeAppointmentTable();

            String[] columnOrder = {"User ID", "Appointment ID", "Type", "Title", "Description", "Location", "Contact ID", "Start Date", "Start Time", "End Date", "End Time"};

            appointmentsByUserCreatedTable = TableHelper.orderTable(columnOrder, appointmentsByUserCreatedTable);

            ObservableList<Appointment> reportData = new ReportsDAO().getAppointmentsByUserCreated();

            for (Appointment appointment : reportData) {
                appointmentsByUserCreatedTable.getItems().add(appointment);
            }

            appointmentsByUserCreatedController.setTable(appointmentsByUserCreatedTable);
            appointmentsByUserCreatedController.setMainLabelText("Appointments by User Created Report");

            loader.setController(appointmentsByUserCreatedController);

            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Monthly Customer Appointments by Type");
            stage.setScene(new Scene(root, 560, 450));
            stage.centerOnScreen();
            stage.show();

        }

        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    /**
     * Override of standard JavaFX method to initialize UI. Assigns a reference to the appointment panel controller on
     * the dashboard to the customer panel controller to allow customer panel to update appointment panel table.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerPanelController.setAppointmentPanelController(appointmentPanelController);

    }
}
