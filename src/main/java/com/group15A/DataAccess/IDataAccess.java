package com.group15A.DataAccess;

import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.PatientNotFoundException;
import com.group15A.DataModel.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * An interface to the Data Access Layer
 *
 * @author Andrei Constantin
 */
public interface IDataAccess
{
    Patient getPatient(String email) throws CustomException;

    Patient getPatient(int patientID) throws CustomException;

    Patient registerPatient(Patient patient, Doctor doctor) throws CustomException;

    Patient updatePatient(Patient patient) throws CustomException;

    Patient changeDoctor(Patient patient, Doctor doctor) throws CustomException;

    List<Doctor> getDoctors() throws CustomException;

    Doctor getDoctor(Patient patient) throws CustomException;

    Doctor getDoctor(int doctorID) throws CustomException;

    List<Certification> getCertifications(Doctor doctor) throws CustomException;

    Booking getBooking(int bookingID) throws CustomException;

    List<Booking> getBookings() throws CustomException;

    List<Booking> getBookings(Doctor doctor) throws CustomException;

    List<Booking> getBookings(Patient patient) throws CustomException;

    Booking createBooking(Patient patient, Doctor doctor, Timestamp bookingTime, String type) throws CustomException;

    Booking updateBooking(Booking booking) throws CustomException;

    Notification getNotification(int notificationID) throws CustomException;

    Notification createNotification(Patient patient, String header, String message) throws CustomException;

    List<Notification> getNotifications(Patient patient) throws CustomException;

    Notification setNotificationSeen(Notification notification) throws CustomException;

    List<Log> getLogs(Patient patient) throws CustomException;

    List<Log> getLogs() throws CustomException;

    Log createLog(Patient patient, String message) throws CustomException;
}
