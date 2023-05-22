package com.group15A.BusinessLogic;

import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataAccess.DataAccess;
import com.group15A.DataModel.Booking;
import com.group15A.DataModel.Doctor;
import com.group15A.Utils.ErrorCode;
import com.group15A.Validator.Validator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Contains backend functionality for getting bookings to display on the ViewBookingsPanel
 *
 * @author Milovan Gveric
 */
public class ViewBookingLogic implements IViewBooking {
    private final DataAccess dataAccessLayer;
    private final Validator validator;

    /**
     * Constructor for the view booking logic
     * @throws DatabaseException if issues connecting to the database
     */
    public ViewBookingLogic() throws DatabaseException {
        this.dataAccessLayer = new DataAccess();
        this.validator = new Validator();
    }

    /**
     * Gets all the bookings belonging to the patient
     * @param patientID
     * @param pastBookingFlag flag to get either all past or future bookings
     * @return the list of all the patient's bookings
     * @throws CustomException if issues getting bookings, or with patient
     */
    @Override
    public List<Booking> getBookings(Integer patientID, Boolean pastBookingFlag) throws CustomException {
        List<Booking> patientBookings = this.dataAccessLayer.getBookings(this.dataAccessLayer.getPatient(patientID));
        List<Booking> patientNewBookings = new ArrayList<>();
        Timestamp today = new Timestamp(System.currentTimeMillis());

        // Remove all old bookings from the return list
        for (Booking b : patientBookings) {

            if (pastBookingFlag) {
                if (b.getBookingTime().before(today)) {
                    patientNewBookings.add(b);
                }
            } else {
                if (b.getBookingTime().after(today)) {
                    patientNewBookings.add(b);
                }
            }
        }

        return patientNewBookings;
    }

    /**
     * Filters the past/future bookings based on month and year dropdown selections
     * @param month could be integer (in string) or string: 'Month (All)'
     * @param year could be integer (in string) or string: 'Year (All)'
     * @param patientID
     * @param pastBookingFlag flag to get either all past or future bookings
     * @return the list of filtered bookings
     * @throws CustomException if any issues with getBookings DAL method
     */
    @Override
    public List<Booking> filterBookings(String month, String year, Integer patientID, Boolean pastBookingFlag) throws CustomException {
        if (!this.validator.isNum(year) && !this.validator.isNum(month) && !month.equals("Month (All)") && !year.equals("Year (All)")) {
            throw new CustomException("Month or Year aren't numbers", List.of(ErrorCode.WRONG_DATE));
        }

        List<Booking> allBookings = this.getBookings(patientID, pastBookingFlag);
        List<Booking> newBookings = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        Timestamp filterDate;
        int filterMonth = -1;
        int filterYear = -1;

        if (!month.equals("Month (All)")) {
            filterDate = Timestamp.valueOf("1900" + "-" + month + "-" + "01 " + "00:00:00");
            cal.setTimeInMillis(filterDate.getTime());
            filterMonth = cal.get(Calendar.MONTH);
        }

        if (!year.equals("Year (All)")) {
            filterDate = Timestamp.valueOf(year + "-" + "01" + "-" + "01 " + "00:00:00");
            cal.setTimeInMillis(filterDate.getTime());
            filterYear = cal.get(Calendar.YEAR);
        }

        for (Booking b : allBookings) {
            cal.setTimeInMillis(b.getBookingTime().getTime());
            // Check booking is in the same month and year
            if ( (cal.get(Calendar.MONTH) == filterMonth && cal.get(Calendar.YEAR) == filterYear) ||
                 (cal.get(Calendar.MONTH) == filterMonth && year.equals("Year (All)"))                   ||
                 (month.equals("Month (All)") && cal.get(Calendar.YEAR) == filterYear)                    ||
                 (month.equals("Month (All)") && year.equals("Year (All)"))
            ) {
                newBookings.add(b);
            }
        }

        return newBookings;
    }

    /**
     * Gets the doctor that the patient is meeting with in the booking
     * @param doctorID
     * @return doctor involved in the booking
     * @throws CustomException if doctor is not found or database connection issues
     */
    @Override
    public Doctor getDoctor(Integer doctorID) throws CustomException {
        return this.dataAccessLayer.getDoctor(doctorID);
    }

    /**
     * Updates the booking record in the database using a Booking instance
     * @param booking the booking instance
     * @throws CustomException if issues updating booking record
     */
    @Override
    public void updateBooking(Booking booking) throws CustomException {
        this.dataAccessLayer.updateBooking(booking);
    }
}
