package Scheduler.Models;

import Scheduler.DAO.CountryDAO;
import Scheduler.DAO.DivisionDAO;

/**
 *
 * Model for Customer objects
 *
 * @author Chris Criswell
 */
public class Customer {

    int id;
    int divisionID;

    String customerName;
    String address;
    String phoneNumber;
    String postalCode;
    String divisionName;
    String countryName;

    public Customer(int id, String customerName, String address, String postalCode, String division, String phoneNumber) {
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.divisionName = division;
        this.phoneNumber = phoneNumber;

        int divisionID = new DivisionDAO().getIDByDivision(division);

        if (divisionID != -1) {
            this.divisionID = divisionID;
            this.countryName = new CountryDAO().getCountryNameByDivisionID(divisionID);
        }

    }

    public Customer(int id, String customerName, String address, String postalCode, int divisionID, String phoneNumber) {

        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.divisionID = divisionID;
        this.phoneNumber = phoneNumber;
        this.countryName = new CountryDAO().getCountryNameByDivisionID(divisionID);

        String division = new DivisionDAO().getDivisionNameByID(divisionID);

        if (division != null) {
            this.divisionName = division;
        }

    }

    public int getId() {
        return id;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDivisionName() { return divisionName; }

    public String getCountryName() { return countryName; }

    public String getPostalCode() {
        return postalCode;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    public void setCountryName(String countryName) { this.countryName = countryName; }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
