package com.group15A.DataModel;

import java.util.Date;
import java.util.Objects;

/**
 * Used to represent a doctor within the system, will be used to pass and change
 * information between the business logic and data access layer.
 *
 * @author Milovan Gveric
 * @author Wenbo Wu
 */

public class Doctor {
    private final Integer doctorID;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private String gender;
    private String phoneNo;

    /**
     * Constructor for Doctor class
     *
     * @param doctorID Dr ID
     * @param email Dr email
     * @param firstName Dr first name
     * @param middleName Dr middle name
     * @param lastName Dr last name
     * @param dob Dr date of birth
     * @param gender Dr gender
     * @param phoneNo Dr phone number
     */
    public Doctor(Integer doctorID, String email, String firstName, String middleName, String lastName, Date dob, String gender, String phoneNo) {
        this.doctorID = doctorID;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    /**
     * toString method for Doctor
     *
     * @return a textual representation of Doctor and its data
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorID=" + doctorID +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }

    /**
     * Method for equality testing
     *
     * @param o
     * @return whether object 'o' is equal to Doctor 'this'
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return doctorID.equals(doctor.doctorID) && email.equals(doctor.email) && firstName.equals(doctor.firstName) && Objects.equals(middleName, doctor.middleName) && lastName.equals(doctor.lastName) && dob.equals(doctor.dob) && gender.equals(doctor.gender) && phoneNo.equals(doctor.phoneNo);
    }

    /**
     * Hashing for Doctor object
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(doctorID, email, firstName, middleName, lastName, dob, gender, phoneNo);
    }

    public String getFullName() {
        return this.firstName+" "+this.lastName;
    }

    // All getters and setters for attributes below

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDoctorID() {
        return doctorID;
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
}
