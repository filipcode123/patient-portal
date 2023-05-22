package com.group15A.BusinessLogic;

import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataAccess.DataAccess;
import com.group15A.DataModel.Log;
import com.group15A.DataModel.Patient;

import java.util.List;

/**
 * Contains backend functionality that relates to getting a user's logs
 *
 * @author Milovan Gveric
 */
public class LogLogic implements ILog {
    private final DataAccess dataAccessLayer;

    /**
     * Constructor for LogLogic
     *
     * @throws DatabaseException if issues connecting to the database
     */
    public LogLogic() throws DatabaseException {
        this.dataAccessLayer = new DataAccess();
    }

    /**
     * Gets a user's logs
     * @param patientID
     * @return all logs of the user
     * @throws CustomException if issues getting logs from DAL
     */
    @Override
    public List<Log> getLogs(Integer patientID) throws CustomException {
        Patient patient = this.dataAccessLayer.getPatient(patientID);
        return this.dataAccessLayer.getLogs(patient);
    }
}
