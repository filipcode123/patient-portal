package com.group15A.DataModel;

import java.sql.Timestamp;
import java.util.Objects;

/**
 *  Used to represent a notification within the system, will be used to pass and change
 *  information between the business logic and data access layer.
 *
 *  @author Wenbo Wu
 */
public class Notification {

    private final Integer notifID;
    private final Integer patientID;
    private String message;
    private String header;
    private final Timestamp timestamp;
    private boolean isNew;

    /**
     *
     * @param notifID Notification id
     * @param patientID patient id
     * @param header the title of the notification
     * @param message the contant of notification
     * @param timestamp the time of the notification has been created
     * @param isNew has not been read
     */
    public Notification(Integer notifID, Integer patientID, String header, String message, Timestamp timestamp, Boolean isNew) {
        this.notifID = notifID;
        this.patientID = patientID;
        this.header = header;
        this.message = message;
        this.timestamp = timestamp;
        this.isNew = isNew;
    }

    public Integer getNotifID() {
        return notifID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void setIsNew(boolean isNew) {this.isNew = isNew;}

    public String getHeader() {return header;}

    public void setHeader(String header) {this.header = header;}

    /**
     * Method for equality testing
     *
     * @param o
     * @return whether object 'o' is equal to Notification 'this'
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(notifID, that.notifID) && Objects.equals(patientID, that.patientID) && Objects.equals(message, that.message) && Objects.equals(header, that.header) && Objects.equals(timestamp, that.timestamp) && isNew==that.isNew;
    }

    /**
     * Hashing for Notification object
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(notifID, patientID, header, message, timestamp, isNew);
    }

    /**
     * toString method for Notification
     *
     * @return a textual representation of Notification and its data
     */
    @Override
    public String toString() {
        return "Notification{" +
                "NotifID=" + notifID +
                ", patientID=" + patientID +
                ", header='" + header + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", isNew=" + isNew +
                '}';
    }
}
