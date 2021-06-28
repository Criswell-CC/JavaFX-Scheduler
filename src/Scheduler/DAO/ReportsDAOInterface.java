package Scheduler.DAO;

import Scheduler.Models.Appointment;
import Scheduler.Models.MonthlyAppointmentsByTypeCounter;
import javafx.collections.ObservableList;

interface ReportsDAOInterface {

    ObservableList<Appointment> getAppointmentsByContact();
    ObservableList<Appointment> getAppointmentsByUserCreated();
    ObservableList<MonthlyAppointmentsByTypeCounter> getMonthlyCustomerAppointmentsByType();

}
