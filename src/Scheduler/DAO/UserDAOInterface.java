package Scheduler.DAO;

import Scheduler.Models.User;

interface UserDAOInterface {

    public User getUserByID(int id);

    public boolean checkUserIDValid(int id);

}
