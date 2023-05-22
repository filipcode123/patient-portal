package com.group15A.BusinessLogic;

import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.CustomExceptions.InvalidDataException;
import com.group15A.CustomExceptions.PatientNotFoundException;
import com.group15A.DataAccess.DataAccess;
import com.group15A.DataModel.Patient;
import com.group15A.Session;

/**
 * Contains backend functionality that relates to logging out and getting
 * patient information
 *
 * @author Milovan Gveric
 */
public class MultiPanelWindowLogic implements IMultiPanelWindow {
    private final DataAccess dataAccessLayer;

    /**
     * Constructor for MultiPanelWindowLogic
     *
     * @throws DatabaseException if issues connecting to the database
     */
    public MultiPanelWindowLogic() throws DatabaseException {
        this.dataAccessLayer = new DataAccess();
    }

    /**
     * Get the patient from the integer id
     * @param patientID The id of the patient
     * @return the patient
     * @throws InvalidDataException if the data is invalid
     * @throws DatabaseException if issues connecting to the database
     * @throws PatientNotFoundException if the patient with the passed in ID was not found
     */
    @Override
    public Patient getPatient(Integer patientID) throws CustomException {
        return this.dataAccessLayer.getPatient(patientID);
    }

    /**
     * Logs the log-out of the patient in the current session
     * @throws Exception if any issues loading the session file or creating a log
     */
    @Override
    public void logOut() throws Exception {
        Patient patient = this.getPatient(Session.loadFromFile().getLoggedInPatientID());
        this.createLog(patient,"Patient " + patient.getFirstName() + " " + patient.getLastName() + " has logged out");
    }


    /**
     * Creates a log for the current patient
     * @param patient
     * @param msg the log message
     * @throws CustomException if any issues creating the log
     */
    @Override
    public void createLog(Patient patient, String msg) throws CustomException {
        this.dataAccessLayer.createLog(patient, msg);
    }
}
