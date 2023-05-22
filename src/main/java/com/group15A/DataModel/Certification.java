package com.group15A.DataModel;

import java.util.Date;
import java.util.Objects;

/**
 * Used to represent a doctor certification within the system, will be used to pass and change
 * information between the business logic and data access layer.
 *
 * @author Milovan Gveric
 * @author Wenbo Wu
 */
public class Certification {
    private final Integer doctorID;
    private final Integer certID;
    private String name;
    private String field;
    private Date dateObtained;

    /**
     * Constructor for Certification class
     *
     * @param doctorID ID of the doctor to which the certificate belongs
     * @param certID ID of the certificate
     * @param name title of the certificate
     * @param field field of study of the certificate
     * @param dateObtained date certificate was obtained by doctor
     */
    public Certification(Integer doctorID, Integer certID, String name, String field, Date dateObtained) {
        this.doctorID = doctorID;
        this.certID = certID;
        this.name = name;
        this.field = field;
        this.dateObtained = dateObtained;
    }

    /**
     * toString method for Certification
     *
     * @return a textual representation of Certification and its data
     */
    @Override
    public String toString() {
        return "Certification{" +
                "doctorID=" + doctorID +
                ", certID=" + certID +
                ", name='" + name + '\'' +
                ", field='" + field + '\'' +
                ", dateObtained='" + dateObtained + '\'' +
                '}';
    }

    /**
     * Method for equality testing
     *
     * @param o
     * @return whether object 'o' is equal to Certification 'this'
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certification that = (Certification) o;
        return doctorID.equals(that.doctorID) && certID.equals(that.certID) && name.equals(that.name) && field.equals(that.field) && dateObtained.equals(that.dateObtained);
    }

    /**
     * Hashing for Certification object
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(doctorID, certID, name, field, dateObtained);
    }

    // All getters and setters for attributes below

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getDateObtained() {
        return dateObtained;
    }

    public void setDateObtained(Date dateObtained) {
        this.dateObtained = dateObtained;
    }

    public Integer getCertID() {
        return certID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }
}
