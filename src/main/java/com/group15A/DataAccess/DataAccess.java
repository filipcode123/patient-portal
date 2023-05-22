package com.group15A.DataAccess;

import com.group15A.CustomExceptions.*;
import com.group15A.DataModel.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.group15A.DataAccess.DataAccessValidator.*;

/**
 * Used to connect to the database and query the database using stored procedures.
 *
 * @author Andrei Constantin
 */
public class DataAccess implements IDataAccess
{
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection connection;

    /**
     * Constructor for the DataAccess class.
     * It sets up the connection to the database
     * @throws DatabaseException if there was a problem connecting to the database
     */
    public DataAccess() throws DatabaseException
    {
        setupConnection();
    }

    /**
     * Set up the connection to the database
     * @throws DatabaseException if the connection could not be established
     */
    private void setupConnection() throws DatabaseException
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/thegeneralpractitioner?user="+DB_USER+"&password="+DB_PASSWORD+"");
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not connect to the database");
        }
    }

    //region Patient
    /**
     * Get the patient with the given email and password
     * @param email The patient's email
     * @return The patient
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws PatientNotFoundException if the user was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient getPatient(String email) throws NullDataException, PatientNotFoundException, DatabaseException
    {
        if(email==null || email.isBlank() || email.isEmpty())
            throw new NullDataException("Null email in getPatient()");
        try {
            String query = "CALL find_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            return getPatientFromDB(result);
        } catch (PatientNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get patient from the database");
        }
    }

    /**
     * Get the patient from the given result set
     * @param result The result set from the database
     * @return The patient
     * @throws PatientNotFoundException if the patient was not found
     */
    private Patient getPatientFromDB(ResultSet result) throws PatientNotFoundException
    {
        Patient patient;
        try {
            result.next();
            patient = new Patient(
                    result.getInt("id_patient"),
                    result.getString("email"),
                    result.getString("password"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getDate("date_of_birth"),
                    result.getString("gender"),
                    result.getString("telephone_number")
            );
        } catch(Exception ex)
        {
            throw new PatientNotFoundException();
        }
        return patient;
    }

    /**
     * Get the patient with the given id
     * @param patientID the patient's id
     * @return The patient
     * @throws InvalidDataException if an invalid value was sent as a parameter
     * @throws PatientNotFoundException if the user was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient getPatient(int patientID) throws InvalidDataException, PatientNotFoundException, DatabaseException
    {
        if(patientID<0)
            throw new InvalidDataException("Negative patient ID in the getPatient method");
        try {
            String query = "CALL get_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patientID);

            ResultSet result = statement.executeQuery();

            return getPatientFromDB(result);
        } catch (PatientNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Could not get patient from the database");
        }
    }

    /**
     * Registers a new patient
     * @param patient The new patient
     * @param doctor The doctor assigned to the patient
     * @return The corresponding patient from the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws InvalidDataException if the data is invalid
     * @throws DatabaseException if there was a problem querying the database
     * @throws EmailInUseException if the email address is already in use
     */
    @Override
    public Patient registerPatient(Patient patient, Doctor doctor) throws NullDataException, EmailInUseException, DatabaseException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the registerPatient method");
        if(doctor==null)
            throw new NullDataException("Null doctor in the registerPatient method");

        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the registerPatient method");
        if(isInvalidDoctor(doctor))
            throw new InvalidDataException("Invalid doctor in the registerPatient method");

        try {
            String query = "CALL insert_patient(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setString(1, patient.getEmail());
            statement.setString(2, patient.getPassHash());
            statement.setString(3, patient.getFirstName());
            statement.setString(4, patient.getMiddleName());
            statement.setString(5, patient.getLastName());
            statement.setDate(6, new Date(patient.getDob().getTime()));
            statement.setString(7, patient.getGender());
            statement.setString(8, patient.getPhoneNo());
            statement.setInt(9, doctor.getDoctorID());

            statement.executeQuery();

            return getPatient(patient.getEmail());
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new EmailInUseException();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not register patient in the database");
        }
    }

    /**
     * Update the given patient with the new information provided in the object
     * @param patient The modified patient
     * @return The corresponding patient from the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws EmailInUseException if the email of the patient does not exist
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public Patient updatePatient(Patient patient) throws CustomException
    {
        updatePatientFull(patient, getDoctor(patient));

        return getPatient(patient.getEmail());
    }


    /**
     * Change the doctor of the given patient
     * @param patient The patient
     * @param doctor The new doctor
     * @return The corresponding patient from the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public Patient changeDoctor(Patient patient, Doctor doctor) throws CustomException
    {
        updatePatientFull(patient, doctor);
        return getPatient(patient.getPatientID());
    }

    /**
     * Update the given patient with the new information, including a new doctor
     * @param patient The modified patient
     * @param doctor The new doctor
     * @throws InvalidDataException if the data is invalid
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was a problem querying the database
     * @throws EmailInUseException if the email address is already in use
     */
    private void updatePatientFull(Patient patient, Doctor doctor) throws NullDataException, DatabaseException, EmailInUseException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the updatePatient method");
        if(doctor==null)
            throw new NullDataException("Null doctor in the updatePatient method");
        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the updatePatientFull method");
        if(isInvalidDoctor(doctor))
            throw new InvalidDataException("Invalid doctor in the updatePatientFull method");

        try {
            String query = "CALL update_patient(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            statement.setString(2, patient.getEmail());
            statement.setString(3, patient.getPassHash());
            statement.setString(4, patient.getFirstName());
            statement.setString(5, patient.getMiddleName());
            statement.setString(6, patient.getLastName());
            if(patient.getDob()==null)
                throw new CustomException("Date cannot be null");
            statement.setDate(7, new Date(patient.getDob().getTime()));
            statement.setString(8, patient.getGender());
            statement.setString(9, patient.getPhoneNo());
            statement.setInt(10, doctor.getDoctorID());

            statement.executeQuery();
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new EmailInUseException();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not update the patient");
        }
    }

    /**
     * Delete the patient with the given id
     * @param patientID The patient id
     * @throws InvalidDataException if an invalid value was sent as a parameter
     * @throws DatabaseException if there was a problem querying the database
     */
    public void deletePatient(int patientID) throws InvalidDataException, DatabaseException
    {
        if(patientID<0)
            throw new InvalidDataException("Negative patient ID in the deletePatient method");

        try{
            String query = "CALL delete_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patientID);
            statement.executeQuery();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not delete the patient from the database");
        }
    }

    //endregion

    //region Doctor
    /**
     * Get the corresponding doctor for the given patient
     * @param patient The patient
     * @return The patient's doctor
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DoctorNotFoundException if the doctor was not found
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    public Doctor getDoctor(Patient patient) throws NullDataException, DoctorNotFoundException, DatabaseException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the getDoctor(patient) method overload");
        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the getDoctor(patient) method overload");

        try {
            String query = "CALL find_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());

            ResultSet result = statement.executeQuery();

            return getDoctorFromDB(result);
        } catch (DoctorNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get doctor from the database");
        }
    }

    /**
     * Get the doctor with the given id
     * @param doctorID The id of the doctor
     * @return The doctor
     * @throws InvalidDataException if the data is invalid
     * @throws DoctorNotFoundException if the doctor was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Doctor getDoctor(int doctorID) throws DoctorNotFoundException, DatabaseException, InvalidDataException {
        if(doctorID<0)
            throw new InvalidDataException("Negative doctor ID in the getDoctor(doctorID) method overload");

        try {
            String query = "CALL get_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, doctorID);
            ResultSet result = statement.executeQuery();

            return getDoctorFromDB(result);
        } catch (DoctorNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get the doctor from the database");
        }
    }

    /**
     * Get the doctor from the given result set
     * @param result The result set from the database
     * @return The doctor
     * @throws DoctorNotFoundException if the doctor was not found
     */
    private Doctor getDoctorFromDB(ResultSet result) throws DoctorNotFoundException
    {
        Doctor doctor;
        try {
            result.next();
            doctor = new Doctor(
                    result.getInt("id_doctor"),
                    result.getString("email"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getDate("date_of_birth"),
                    result.getString("gender"),
                    result.getString("telephone_number")
            );
        } catch(Exception ex)
        {
            throw new DoctorNotFoundException();
        }

        return doctor;
    }


    /**
     * Get the full list of doctors from the database
     * @return The list of doctors
     * @throws DatabaseException if there was a problem querying the database
     */
    public List<Doctor> getDoctors() throws DatabaseException
    {
        try {
            String query = "CALL get_doctors();";
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet result = statement.executeQuery();
            var doctors = new ArrayList<Doctor>();
            while (result.next()) {
                doctors.add(new Doctor(
                        result.getInt("id_doctor"),
                        result.getString("email"),
                        result.getString("first_name"),
                        result.getString("middle_name"),
                        result.getString("last_name"),
                        result.getDate("date_of_birth"),
                        result.getString("gender"),
                        result.getString("telephone_number")
                ));
            }

            return doctors;
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get doctors from the database");
        }
    }

    //endregion

    //region Certification
    /**
     * Get the certifications of the specified doctor
     * @param doctor The doctor
     * @return The certifications
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public List<Certification> getCertifications(Doctor doctor) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(doctor==null)
            throw new NullDataException("Null doctor in the getCertifications method");
        if(isInvalidDoctor(doctor))
            throw new InvalidDataException("Invalid doctor in the getCertifications method");

        try {
            String query = "CALL get_certifications_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, doctor.getDoctorID());
            ResultSet result = statement.executeQuery();
            var certifications = new ArrayList<Certification>();
            while (result.next()) {
                certifications.add(new Certification(
                        result.getInt("id_doctor"),
                        result.getInt("id_cert"),
                        result.getString("name"),
                        result.getString("field"),
                        result.getDate("dateObtained")
                ));
            }

            return certifications;
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get certifications from the database");
        }
    }

    //endregion

    //region Booking
    /**
     * Get the booking with the specified id
     * @param bookingID The booking id
     * @return The booking
     * @throws InvalidDataException if the data is invalid
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Booking getBooking(int bookingID) throws DatabaseException, InvalidDataException
    {
        if(bookingID<0)
            throw new InvalidDataException("Negative booking ID in the getBooking(bookingID) method");
        try {
            String query = "CALL get_booking(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, bookingID);
            ResultSet result = statement.executeQuery();

            result.next();

            return new Booking(
                    result.getInt("id_booking"),
                    result.getInt("id_doctor"),
                    result.getInt("id_patient"),
                    result.getTimestamp("booking_time"),
                    result.getTimestamp("timestamp"),
                    result.getString("type"),
                    result.getString("details"),
                    result.getString("prescription")
            );
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get booking from the database");
        }
    }


    /**
     * Get all bookings from the database
     * @return The bookings
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public List<Booking> getBookings() throws DatabaseException
    {
        try {
            String query = "CALL get_bookings();";
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet result = statement.executeQuery();

            return getBookingsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get bookings from the database");
        }
    }

    /**
     * Get all bookings of the given doctor
     * @return The bookings
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public List<Booking> getBookings(Doctor doctor) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(doctor==null)
            throw new NullDataException("Null doctor in the getBookings(doctor) method overload");
        if(isInvalidDoctor(doctor))
            throw new InvalidDataException("Invalid doctor in the getBookings(doctor) method overload");

        try {
            String query = "CALL get_bookings_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, doctor.getDoctorID());
            ResultSet result = statement.executeQuery();

            return getBookingsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get bookings from the database");
        }
    }

    /**
     * Get all bookings of the given patient
     * @return The bookings
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public List<Booking> getBookings(Patient patient) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the getBookings(patient) method overload.");
        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the getBookings(patient) method overload");

        try {
            String query = "CALL get_bookings_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            ResultSet result = statement.executeQuery();

            return getBookingsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get bookings from the database");
        }
    }

    /**
     * Get a list of bookings from the given result set
     * @param result The result set
     * @return The list of bookings
     * @throws SQLException if there was a problem retrieving the bookings
     */
    private ArrayList<Booking> getBookingsFromDB(ResultSet result) throws SQLException
    {
        var bookings = new ArrayList<Booking>();
        while (result.next()) {
            bookings.add(new Booking(
                    result.getInt("id_booking"),
                    result.getInt("id_doctor"),
                    result.getInt("id_patient"),
                    result.getTimestamp("booking_time"),
                    result.getTimestamp("timestamp"),
                    result.getString("type"),
                    result.getString("details"),
                    result.getString("prescription")
            ));
        }
        return bookings;
    }


    /**
     * Create booking
     * @param patient The patient
     * @param doctor The doctor
     * @param bookingTime The date and time of the booking
     * @param type The type of booking
     * @return The Booking from the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was an error querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public Booking createBooking(Patient patient, Doctor doctor, Timestamp bookingTime, String type) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the createBooking method");
        if(doctor==null)
            throw new NullDataException("Null doctor in the createBooking method");
        if(bookingTime==null)
            throw new NullDataException("Null booking time in the createBooking method");
        if(isNullOrEmpty(type))
            throw new NullDataException("Null type in the createBooking method");

        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the createBooking method");
        if(isInvalidDoctor(doctor))
            throw new InvalidDataException("Invalid doctor in the createBooking method");

        try {
            String query = "CALL insert_booking(?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            statement.setInt(2, doctor.getDoctorID());
            statement.setTimestamp(3, bookingTime);
            statement.setString(4, type);
            statement.setString(5, null);
            statement.setString(6, null);

            statement.executeQuery();

            var bookings = getBookings();
            return bookings.get(bookings.size()-1);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new DatabaseException("Could not insert booking in the database");
        }
    }

    /**
     * Update the booking with the new details
     * @param booking The modified booking
     * @return The corresponding booking from the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was an error querying the database
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public Booking updateBooking(Booking booking) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(booking==null)
            throw new NullDataException("Null booking in the updateBooking method");
        if(isInvalidBooking(booking))
            throw new InvalidDataException("Invalid booking the updateBooking method");

        try {
            String query = "CALL update_booking(?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, booking.getBookingID());
            statement.setInt(2, booking.getPatientID());
            statement.setInt(3, booking.getDoctorID());
            statement.setTimestamp(4, booking.getBookingTime());
            statement.setString(5, booking.getType());
            statement.setString(6, booking.getDetails());
            statement.setString(7, booking.getPrescription());

            statement.executeQuery();

            return getBooking(booking.getBookingID());
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new DatabaseException("Could not update booking in the database");
        }
    }

    /**
     * Delete the booking from the database
     * @param booking The booking
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    public void deleteBooking(Booking booking) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(booking==null)
            throw new NullDataException("Null booking in the deleteBooking method");
        if(isInvalidBooking(booking))
            throw new InvalidDataException("Invalid booking in the deleteBooking method");

        try{
            String query = "CALL delete_booking(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, booking.getBookingID());

            statement.executeQuery();
        } catch(Exception ex)
        {
            throw new DatabaseException("Could not delete the booking");
        }
    }


    //endregion

    //region Notification
    /**
     * Get the notification with the given id from the database
     * @param notificationID The id of the notification
     * @return The notification
     * @throws DatabaseException if there was an error querying the database
     */
    @Override
    public Notification getNotification(int notificationID) throws DatabaseException
    {
        try {
            String query = "CALL get_notification(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, notificationID);
            ResultSet result = statement.executeQuery();

            result.next();

            return new Notification(
                    result.getInt("id_notif"),
                    result.getInt("id_patient"),
                    result.getString("header"),
                    result.getString("message"),
                    result.getTimestamp("booking_time"),
                    result.getBoolean("is_new")
            );
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get notification from the database");
        }
    }


    /**
     * Create notification
     * @param patient The patient
     * @param header The header of the notification
     * @param message The main body of the notification
     * @return The Notification from the database
     * @throws DatabaseException if there was a problem querying the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public Notification createNotification(Patient patient, String header, String message) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the createNotification method");
        if(isNullOrEmpty(header))
            throw new NullDataException("Null header in the createNotification method");
        if(isNullOrEmpty(message))
            throw new NullDataException("Null message in the createNotification method");

        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the createNotification method");

        try {
            String query = "CALL insert_notification(?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            statement.setString(2, header);
            statement.setString(3, message);

            statement.executeQuery();

            var notifications = getNotifications();
            return notifications.get(notifications.size()-1);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new DatabaseException("Could not insert booking in the database");
        }
    }

    /**
     * Get the notifications of the given patient
     * @param patient The patient
     * @return The patient's notifications
     * @throws DatabaseException if there was a problem querying the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public List<Notification> getNotifications(Patient patient) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the getNotifications method");
        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the getNotification method");
        try {
            String query = "CALL get_notifications_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            ResultSet result = statement.executeQuery();

            return getNotificationsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get notifications from the database");
        }
    }

    /**
     * Set the given notification as seen
     * @param notification The notification
     * @return The corresponding notification in the database
     * @throws DatabaseException if there was a problem querying the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public Notification setNotificationSeen(Notification notification) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(notification==null)
            throw new NullDataException("Null notification in the setNotificationSeen method");
        if(!validateNotification(notification))
            throw new InvalidDataException("Invalid notification in the setNotification method");

        try{
            String query = "CALL notificationNotNew(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, notification.getNotifID());

            statement.executeQuery();

            notification.setIsNew(false);
            return notification;
        } catch(Exception ex)
        {
            ex.printStackTrace();
            throw new DatabaseException("Could not update the notification");
        }
    }

    /**
     * Get all notifications from the database
     * @return The notifications
     * @throws DatabaseException if there was a problem querying the database
     */
    private List<Notification> getNotifications() throws DatabaseException
    {
        try {
            String query = "CALL get_notifications();";
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet result = statement.executeQuery();

            return getNotificationsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get notifications from the database");
        }
    }

    /**
     * Get a list of notifications from the given result set
     * @param result The result set
     * @return The list of notifications
     * @throws SQLException if there was a problem retrieving the notifications
     */
    private List<Notification> getNotificationsFromDB(ResultSet result) throws SQLException
    {
        var notifications = new ArrayList<Notification>();
        while (result.next()) {
            notifications.add(new Notification(
                    result.getInt("id_notif"),
                    result.getInt("id_patient"),
                    result.getString("header"),
                    result.getString("message"),
                    result.getTimestamp("timestamp"),
                    result.getBoolean("is_new")
            ));
        }

        return notifications;
    }


    /**
     * Delete the notification with the given id
     * @param notificationID The notification id
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    public void deleteNotification(int notificationID) throws DatabaseException, InvalidDataException
    {
        if(notificationID<0)
            throw new InvalidDataException("Negative notification ID in the deleteNotification method");
        try{
            String query = "CALL delete_notification(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, notificationID);
            statement.executeQuery();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not delete the notification from the database");
        }
    }

    //endregion

    //region Log

    /**
     * Get all logs
     * @return The logs
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public List<Log> getLogs() throws DatabaseException
    {
        try {
            String query = "CALL get_logs();";
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet result = statement.executeQuery();

            return getLogsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get logs from the database");
        }
    }

    /**
     * Get the logs of the given patient
     * @param patient The patient
     * @return The patient's logs
     * @throws DatabaseException if there was a problem querying the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public List<Log> getLogs(Patient patient) throws DatabaseException, NullDataException, InvalidDataException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the getLogs method");
        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the getLogs method");
        try {
            String query = "CALL get_logs_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            ResultSet result = statement.executeQuery();

            return getLogsFromDB(result);
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get logs from the database");
        }
    }

    /**
     * Get a list of logs from the given result set
     * @param result The result set
     * @return The list of logs
     * @throws SQLException if there was a problem retrieving the logs
     */
    private List<Log> getLogsFromDB(ResultSet result) throws SQLException
    {
        var logs = new ArrayList<Log>();
        while (result.next()) {
            logs.add(new Log(
                    result.getInt("id_log"),
                    result.getString("message"),
                    result.getInt("id_patient"),
                    result.getTimestamp("timestamp")
            ));
        }

        return logs;
    }

    /**
     * Create notification
     * @param patient The patient
     * @param message The content of the log
     * @return The log from the database
     * @throws DatabaseException if there was a problem querying the database
     * @throws NullDataException if a null value was sent as a parameter where a non-null value is expected
     * @throws InvalidDataException if the data is invalid
     */
    public Log createLog(Patient patient, String message) throws NullDataException, InvalidDataException, DatabaseException
    {
        if(patient==null)
            throw new NullDataException("Null patient in the createLog method");
        if(isNullOrEmpty(message))
            throw new NullDataException("Null message in the createLog method");

        if(isInvalidPatient(patient))
            throw new InvalidDataException("Invalid patient in the createLog method");

        try {
            String query = "CALL insert_log(?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setString(1, message);
            statement.setInt(2, patient.getPatientID());

            statement.executeQuery();

            var logs = getLogs();
            return logs.get(logs.size()-1);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new DatabaseException("Could not insert log in the database");
        }
    }

    /**
     * Delete the log with the given id
     * @param logID The log id
     * @throws DatabaseException if there was a problem querying the database
     * @throws InvalidDataException if the data is invalid
     */
    public void deleteLog(int logID) throws DatabaseException, InvalidDataException
    {
        if(logID<0)
            throw new InvalidDataException("Negative log ID in the deleteLog method");
        try{
            String query = "CALL delete_log(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, logID);
            statement.executeQuery();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not delete the log from the database");
        }
    }


    //endregion

}
