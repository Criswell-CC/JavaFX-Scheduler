package Scheduler.DAO;

import javafx.collections.ObservableList;

interface CountryDAOInterface {

    int getIDByCountry(String country);

    String getCountryNameByDivisionID(int id);

    ObservableList<String> getAllCountries();

}
