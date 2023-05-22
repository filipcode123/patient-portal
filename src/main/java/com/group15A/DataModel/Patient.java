package com.group15A.DataModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Used to represent a patient within the system, will be used to pass and change
 * information between the business logic and data access layer.
 *
 * @author Milovan Gveric
 * @author Wenbo Wu
 */
public class Patient implements Serializable {
    private final Integer patientID;
    private String email;
    private String passHash;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private String gender;
    private String phoneNo;

    private static final Integer UNKNOWN_PATIENT_ID = -1;

    /**
     * Constructor for a patient
     *
     * @param patientID The ID
     * @param email The email address
     * @param passHash The hashed password
     * @param firstName The first name
     * @param middleName The middle name
     * @param lastName The last name
     * @param dob The date of birth
     * @param gender The gender
     * @param phoneNo The phone number
     */
    public Patient(Integer patientID, String email, String passHash, String firstName, String middleName, String lastName, Date dob, String gender, String phoneNo) {
        this.patientID = patientID;
        this.email = email;
        this.passHash = passHash;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    /**
     * Constructor for a patient with an unknown id
     *
     * @param email The email address
     * @param passHash The hashed password
     * @param firstName The first name
     * @param middleName The middle name
     * @param lastName The last name
     * @param dob The date of birth
     * @param gender The gender
     * @param phoneNo The phone number
     */
    public Patient(String email, String passHash, String firstName, String middleName, String lastName, Date dob, String gender, String phoneNo)
    {
        this(UNKNOWN_PATIENT_ID, email, passHash, firstName, middleName, lastName, dob, gender, phoneNo);
    }

    /**
     * toString method for Patient
     *
     * @return a textual representation of Patient and its data
     */
    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", email='" + email + '\'' +
                ", passHash='" + passHash + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + getSimpleDate(dob) + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }

    /**
     * Method for equality testing
     *
     * @param o
     * @return whether object 'o' is equal to Patient 'this'
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return email.equals(patient.email) && passHash.equals(patient.passHash) && firstName.equals(patient.firstName) && Objects.equals(middleName, patient.middleName) && lastName.equals(patient.lastName) && getSimpleDate(dob).equals(getSimpleDate(patient.dob)) && gender.equals(patient.gender) && phoneNo.equals(patient.phoneNo);
    }

    /**
     * Hashing for Patient object
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(patientID, email, passHash, firstName, middleName, lastName, dob, gender, phoneNo);
    }

    // All getters and setters for attributes below

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    private static String getSimpleDate(Date date)
    {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
    }
}
