package Scheduler.DAO;

import javafx.collections.ObservableList;

public interface ContactDAOInterface {

    ObservableList<String> getAllContactNames();

    String getContactNameByID(int id);

    int getContactIDByName(String name);

}
