package Scheduler.DAO;

import Scheduler.Models.Customer;
import javafx.collections.ObservableList;

interface CustomerDAOInterface {

    ObservableList<Customer> getAllCustomers();

    boolean addCustomer(Customer customer);
    boolean deleteCustomer(Customer customer);
    boolean updateCustomer(Customer customer);

    int getIDByCustomerName(String string);
    String getCustomerNameByID(int id);

    boolean checkCustomerIDValid(int id);
    int generateCustomerID();

}
