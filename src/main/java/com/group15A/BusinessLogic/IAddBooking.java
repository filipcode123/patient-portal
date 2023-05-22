package com.group15A.BusinessLogic;

import com.group15A.DataModel.Booking;
import com.group15A.DataModel.Doctor;
import com.group15A.DataModel.Patient;

/**
 * The interface for AddBookingLogic
 *
 * @author Milovan Gveric
 */
public interface IAddBooking {
    void createNewBooking(String date, String hour, String minute, String type, Integer patientID) throws Exception;

    void rescheduleBooking(String date, String hour, String minute, String type, Integer patientID, Booking booking) throws Exception;

    Doctor getPatientDoctor(Patient patient) throws Exception;

    Patient getPatient(Integer patientID) throws Exception;
}