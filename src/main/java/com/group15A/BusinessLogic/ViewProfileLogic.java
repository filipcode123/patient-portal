package com.group15A.BusinessLogic;

import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.CustomExceptions.SameDoctorException;
import com.group15A.DataAccess.DataAccess;
import com.group15A.DataModel.Doctor;
import com.group15A.DataModel.Patient;

/**
 * Contains backend functionality for changing a patient's doctor and viewing
 * past bookings and activities
 *
 * @author Milovan Gveric
 */
public class ViewProfileLogic implements IViewProfile {
    private final DataAccess dataAccessLayer;

    /**
     * Constructor for view profile logic
     * @throws DatabaseException if issues connecting to the database
     */
    public ViewProfileLogic() throws DatabaseException {
        this.dataAccessLayer = new DataAccess();
    }

    /**
     * Changes the patient's current doctor to a new doctor
     * @param patientID the patient
     * @param newDoctor the patient's new doctor
     * @throws CustomException if issue with finding the patient, doctor, updating the patient's
     * new doctor or when finally creating a notification
     */
    @Override
    public void updatePatientDoctor(Integer patientID, Doctor newDoctor) throws CustomException {
        Patient patient = this.dataAccessLayer.getPatient(patientID);
        Doctor oldDoctor = this.dataAccessLayer.getDoctor(patient);

        // Patient should not be able to switch to their existing doctor
        if (oldDoctor.getDoctorID().equals(newDoctor.getDoctorID())) {
            throw new SameDoctorException();
        }

        this.dataAccessLayer.changeDoctor(patient, newDoctor);
        this.dataAccessLayer.createNotification(patient, "Doctor Changed", "You changed your doctor from "+oldDoctor.getFullName()+" to "+newDoctor.getFullName());
        this.dataAccessLayer.createLog(patient, "Patient " + patient.getFirstName() + " " + patient.getLastName() + " has changed their doctor from Dr. " +oldDoctor.getLastName() + " to Dr. " + newDoctor.getLastName());
    }
}
