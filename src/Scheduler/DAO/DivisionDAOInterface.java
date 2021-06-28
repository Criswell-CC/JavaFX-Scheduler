package Scheduler.DAO;

import javafx.collections.ObservableList;

interface DivisionDAOInterface {

    ObservableList<String> getAllDivisionList();
    ObservableList<String> getUSDivisionList();
    ObservableList<String> getCADivisionList();
    ObservableList<String> getUKDivisionList();

    int getIDByDivision(String division);

}
