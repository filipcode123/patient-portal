package com.group15A.DataModel;

import java.sql.Timestamp;
import java.util.Objects;

/**
 *  Used to represent a booking within the system, will be used to pass and change
 *  information between the business logic and data access layer.
 *
 *  @author Wenbo Wu
 */
public class Booking {

    private final Integer bookingID;
    private final Integer doctorID;
    private Integer patientID;
    private Timestamp bookingTime;
    private final Timestamp timestamp;
    private String type;
    private String details;
    private String prescription;

    /**
     *
     * @param bookingID the bookingID when patient do the booking
     * @param doctorID the doctor id
     * @param patientID the patient id
     * @param bookingTime the time the booking
     * @param timestamp time booking has been created
     * @param type the type of the booking
     * @param details the details for the booking
     * @param prescription the prescription from the doctor
     */
    public Booking(Integer bookingID, Integer doctorID, Integer patientID, Timestamp bookingTime, Timestamp timestamp, String type, String details, String prescription) {
        this.bookingID = bookingID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.bookingTime = bookingTime;
        this.timestamp = timestamp;
        this.type = type;
        this.details = details;
        this.prescription = prescription;
    }

    public Integer getBookingID() {return bookingID;}

    public Integer getDoctorID() {return doctorID;}


    public Integer getPatientID() {return patientID;}

    public void setPatientID(Integer patientID) {this.patientID = patientID;}

    public Timestamp getBookingTime() {return bookingTime;}

    public void setBookingTime(Timestamp bookingTime) {this.bookingTime = bookingTime;}

    public Timestamp getTimestamp() {return timestamp;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getDetails() {return details;}

    public void setDetails(String details) {this.details = details;}

    public String getPrescription() {return prescription;}

    public void setPrescription(String prescription) {this.prescription = prescription;}

    /**
     * Method for equality testing
     *
     * @param o
     * @return whether object 'o' is equal to Booking 'this'
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return getBookingID().equals(booking.getBookingID()) && getDoctorID().equals(booking.getDoctorID()) && getPatientID().equals(booking.getPatientID()) && getBookingTime().equals(booking.getBookingTime()) && getTimestamp().equals(booking.getTimestamp()) && getType().equals(booking.getType()) && getDetails().equals(booking.getDetails()) && getPrescription().equals(booking.getPrescription());
    }

    /**
     * Hashing for Booking object
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getBookingID(), getDoctorID(), getPatientID(), getBookingTime(), getTimestamp(), getType(), getDetails(), getPrescription());
    }

    /**
     * toString method for Booking
     *
     * @return a textual representation of Booking and its data
     */
    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + bookingID +
                ", doctorID=" + doctorID +
                ", patientID=" + patientID +
                ", bookingTime=" + bookingTime +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", prescription='" + prescription + '\'' +
                '}';
    }
}
