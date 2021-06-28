package Scheduler.DAO;

import Scheduler.Models.Customer;
import Scheduler.Utils.DatabaseHandler;
import Scheduler.Utils.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for handling customer data in MySQL database
 *
 * @author Chris Criswell
 */
public class CustomerDAO implements CustomerDAOInterface {

    /**
     * Constructs and executes query to return all customers from customer table
     * @return list of all customers
     */
    public ObservableList<Customer> getAllCustomers() {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        String query = "SELECT * FROM customers ORDER BY Customer_ID";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            while (results.next()) {

                customerList.add(new Customer(results.getInt("Customer_ID"), results.getString("Customer_Name"),
                        results.getString("Address"), results.getString("Postal_Code"),
                        results.getInt("Division_ID"), results.getString("Phone")));

            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return customerList;

    }

    /**
     * Constructs and executes query to add new customer in database
     * @param customer customer to add
     * @return bool indicating success of database write
     */
    public boolean addCustomer(Customer customer) {

        String query = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, customer.getCustomerName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getPostalCode());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6, UserSession.getCurrentUser().getName());
            preparedStatement.setString(7, UserSession.getCurrentUser().getName());
            preparedStatement.setInt(8, customer.getDivisionID());

            preparedStatement.execute();

            return true;

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false;

    }

    /**
     * Constructs and executes query to delete existing customer in database. Method first deletes all appointments
     * associated with the customer.
     * @param customer customer to delete
     * @return bool indicating success of database delete
     */
    public boolean deleteCustomer(Customer customer) {

        String query = "DELETE FROM appointments WHERE Customer_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, customer.getId());

            preparedStatement.execute();

            query = "DELETE FROM customers WHERE Customer_ID = ?";

            preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, customer.getId());

            preparedStatement.execute();

            return true;

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false;

    }

    /**
     * Constructs and executes query to update existing customer in database
     * @param customer updated customer
     * @return bool indicating success of database write
     */
    public boolean updateCustomer(Customer customer) {

        String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPostalCode());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setString(5, UserSession.getCurrentUser().getName());
            preparedStatement.setInt(6, customer.getDivisionID());
            preparedStatement.setInt(7, customer.getId());

            preparedStatement.execute();

            return true;

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false;

    }

    /**
     *  Constructs and executes query to check whether a customer ID exists in customers table
     * @param id customer ID
     * @return bool indicating whether customer ID is valid
     */
    @Override
    public boolean checkCustomerIDValid(int id) {

        String query = "SELECT * FROM customers WHERE Customer_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet results = preparedStatement.getResultSet();

            if (results.next()) {
                return true;
            }
        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false;
    }

    /**
     * Constructs and executes query to find customer ID given a customer's name
     * @param name customer name
     * @return customer ID for given customer name
     */
    public int getIDByCustomerName(String name) {

        String query = "SELECT Customer_ID FROM customers WHERE Customer_Name = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setString(1, name);

            preparedStatement.execute();

            ResultSet result = preparedStatement.getResultSet();

            if (result.next()) {
                int id = result.getInt(1);
                return id + 1;
            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return -1;
    }

    /**
     * Constructs and executes query to find customer name given a customer ID
     * @param id customer ID
     * @return customer name for given customer ID
     */
    public String getCustomerNameByID(int id) {

        String query = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            ResultSet result = preparedStatement.getResultSet();

            if (result.next()) {
                return result.getString(1);
            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return null;

    }

    /**
     * Utility method to automatically generate customer ID
     * @return customer ID for new customer
     */
    public int generateCustomerID() {

        //https://stackoverflow.com/questions/5113450/last-id-value-in-a-table-sql-server/46346432
        String query = "SELECT MAX(Customer_ID) FROM customers";

        try {

            PreparedStatement preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);

            preparedStatement.execute();

            ResultSet result = preparedStatement.getResultSet();

            if (result.next()) {
                int id = result.getInt(1);
                return id + 1;
            }

        }

        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return -1;
    }

}
