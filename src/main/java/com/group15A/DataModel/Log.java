package com.group15A.DataModel;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Used to represent a log within the system, will be used to pass and change
 * information between the business logic and data access layer.
 *
 * @author Wenbo Wu
 */
public class Log {
    private final Integer logID;
    private String message;
    private Integer patientID;
    private final Timestamp timestamp;

    /**
     * Constructor for a log
     *
     * @param logID the id
     * @param message the content of the log
     * @param patientID id for patient
     * @param timestamp time log has been created
     */
    public Log(Integer logID, String message, Integer patientID, Timestamp timestamp) {
        this.logID = logID;
        this.message = message;
        this.patientID = patientID;
        this.timestamp = timestamp;
    }

    public Integer getLogID() {return logID;}

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public Integer getPatientID() {return patientID;}

    public void setPatientID(Integer patientID) {this.patientID = patientID;}

    public Timestamp getTimestamp() {return timestamp;}

    /**
     * Method for equality testing
     *
     * @param o
     * @return whether object 'o' is equal to logs 'this'
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(getLogID(), log.getLogID()) && Objects.equals(getMessage(), log.getMessage()) && Objects.equals(getPatientID(), log.getPatientID()) && Objects.equals(getTimestamp(), log.getTimestamp());
    }

    /**
     *  Hashing for logs object
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLogID(), getMessage(), getPatientID(), getTimestamp());
    }

    /**
     * toString method for logs
     *
     * @return a textual representation of logs and its data
     */
    @Override
    public String toString() {
        return "Log{" +
                "logID=" + logID +
                ", message='" + message + '\'' +
                ", patientID=" + patientID +
                ", timestamp=" + timestamp +
                '}';
    }
}
