package Scheduler.Utils;

import Scheduler.Models.Appointment;
import Scheduler.Models.Customer;
import Scheduler.Models.MonthlyAppointmentsByTypeCounter;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to facilitate dynamic table generation for reports
 *
 * @author Chris Criswell
 */
public class TableHelper {

    /**
     * Initializes an appointment TableView object through using reflection to create table columns for each property in Appointment class and add it to a TableView object
     * @return a fully-initialized appointment TableView object
     */
    public static TableView<Appointment> initializeAppointmentTable() {

        //https://stackoverflow.com/questions/51414861/java-converting-class-member-variables-values-into-map-reflection

        TableView<Appointment> appointmentsTable = new TableView();

        HashMap<String, TableColumn<Appointment, ?>> columnMap = new HashMap<>();

        for (Field field : Appointment.class.getDeclaredFields()) {

            field.setAccessible(true);

            if (field.getType().equals(String.class)){

                switch (field.getName()) {
                    case ("formattedStartDate"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("Start Date"));
                        break;
                    case ("formattedStartTime"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("Start Time"));
                        break;
                    case("formattedEndDate"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("End Date"));
                        break;
                    case("formattedEndTime"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("End Time"));
                        break;
                    case("customerName"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("Customer Name"));
                        break;
                    case("contactName"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("Contact Name"));
                        break;
                    default:
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                        break;
                }

            }

            else if (field.getType().equals(int.class)) {

                switch (field.getName()) {
                    case("appointmentID"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, Integer>("Appointment ID"));
                        break;
                    case("customerID"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, Integer>("Customer ID"));
                        break;
                    case("contactID"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, Integer>("Contact ID"));
                        break;
                    case("userID"):
                        columnMap.put(field.getName(), new TableColumn<Appointment, String>("User ID"));
                        break;
                    default:
                        columnMap.put(field.getName(), new TableColumn<Appointment, Integer>(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                        break;
                }
            }
        }

        for (Map.Entry<String, TableColumn<Appointment, ?>> column : columnMap.entrySet()) {

            column.getValue().setCellValueFactory(new PropertyValueFactory<>(column.getKey()));
            appointmentsTable.getColumns().add(column.getValue());

        }

        return appointmentsTable;

    }

    /**
     * Initializes a customer TableView object through using reflection to create table columns for each property in Customer class and add it to a TableView object
     * @return a fully-initialized customer TableView object
     */
    public static TableView<Customer> initializeCustomerTable() {

        //https://stackoverflow.com/questions/51414861/java-converting-class-member-variables-values-into-map-reflection

        TableView<Customer> customerTable = new TableView();

        HashMap<String, TableColumn<Customer, ?>> columnMap = new HashMap<>();

        for (Field field : Customer.class.getDeclaredFields()) {

            field.setAccessible(true);

            if (field.getType().equals(String.class)){

                //special naming cases

                if (field.getName().equals("postalCode")) {
                    columnMap.put(field.getName(), new TableColumn<Customer, String>("Postal Code"));
                }

                else if (field.getName().equals("phoneNumber")) {
                    columnMap.put(field.getName(), new TableColumn<Customer, String>("Phone Number"));
                }

                else {
                    columnMap.put(field.getName(), new TableColumn<Customer, String>(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                }

            }

            else if (field.getType().equals(int.class)) {

                if (field.getName().equals("divisionID")) {
                    continue;
                }

                else {
                    columnMap.put(field.getName(), new TableColumn<Customer, Integer>(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                }
            }

        }

        for (Map.Entry<String, TableColumn<Customer, ?>> column : columnMap.entrySet()) {

            column.getValue().setCellValueFactory(new PropertyValueFactory<>(column.getKey()));
            customerTable.getColumns().add(column.getValue());

        }

        return customerTable;

    }

    public static TableView<MonthlyAppointmentsByTypeCounter> initializeMonthlyReportTable() {

        //https://stackoverflow.com/questions/51414861/java-converting-class-member-variables-values-into-map-reflection

        TableView<MonthlyAppointmentsByTypeCounter> monthlyReportTable = new TableView();

        HashMap<String, TableColumn<MonthlyAppointmentsByTypeCounter, ?>> columnMap = new HashMap<>();

        for (Field field : MonthlyAppointmentsByTypeCounter.class.getDeclaredFields()) {

            field.setAccessible(true);

            if (field.getType().equals(String.class)){

                switch (field.getName()) {

                    default:
                        columnMap.put(field.getName(), new TableColumn<MonthlyAppointmentsByTypeCounter, String>(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                        break;
                }

            }

            else if (field.getType().equals(int.class)) {

                switch (field.getName()) {
                    default:
                        columnMap.put(field.getName(), new TableColumn<MonthlyAppointmentsByTypeCounter, Integer>(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                        break;
                }
            }
        }

        for (Map.Entry<String, TableColumn<MonthlyAppointmentsByTypeCounter, ?>> column : columnMap.entrySet()) {

            column.getValue().setCellValueFactory(new PropertyValueFactory<>(column.getKey()));
            monthlyReportTable.getColumns().add(column.getValue());

        }

        return monthlyReportTable;

    }


    /**
     * Generic method to order the columns of any TableView object given a list of columns and a TableView object to
     * initialize from
     * @param orderList Ordered list of column title strings
     * @param table table object whose columns will be ordered
     * @return TableView object with ordered columns
     */
    public static TableView orderTable(String[] orderList, TableView table) {

        ObservableList<TableColumn<?, ?>> columnList = table.getColumns();

        HashMap<String, TableColumn> columnMap = new HashMap<>();
        HashMap<Integer, TableColumn> columnMapIndexed = new HashMap<>();

        TableView<Customer> sortedTable = new TableView();

        for (TableColumn column : columnList) {

            columnMap.put(column.getText(), column);

        }

        for (String column : orderList) {

            columnMapIndexed.put(Arrays.asList(orderList).indexOf(column), columnMap.get(column));

        }

        for (Map.Entry<Integer, TableColumn> column : columnMapIndexed.entrySet()) {

            sortedTable.getColumns().add(column.getValue());

        }

        return sortedTable;

    }

}
