package Scheduler.DAO;

import Scheduler.Models.Appointment;
import javafx.collections.ObservableList;

import java.time.ZonedDateTime;

interface AppointmentDAOInterface {

    boolean addAppointment(Appointment appointment);
    boolean deleteAppointment(Appointment appointment);
    boolean updateAppointment(Appointment appointment);

    ObservableList<Appointment> getAllAppointments();
    ObservableList<Appointment> getAllAppointmentsByMonth();
    ObservableList<Appointment> getAllAppointmentsByWeek();
    ObservableList<Appointment> getAppointmentsWithinRange(ZonedDateTime start, ZonedDateTime end);

    boolean checkIfOtherAppointmentsInRange(int appointmentID, int customerID, ZonedDateTime start, ZonedDateTime end);

    int generateAppointmentID();

}
