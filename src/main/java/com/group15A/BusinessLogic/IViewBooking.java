package com.group15A.BusinessLogic;

import com.group15A.DataModel.Booking;
import com.group15A.DataModel.Doctor;
import java.util.List;

/**
 * The interface for ViewBookingLogic
 *
 * @author Milovan Gveric
 */
public interface IViewBooking {
    List<Booking> getBookings(Integer patientID, Boolean viewPastBooking) throws Exception;

    List<Booking> filterBookings(String month, String year, Integer patientID, Boolean pastBookingFlag) throws Exception;

    Doctor getDoctor(Integer doctorID) throws Exception;

    void updateBooking(Booking booking) throws Exception;
}
