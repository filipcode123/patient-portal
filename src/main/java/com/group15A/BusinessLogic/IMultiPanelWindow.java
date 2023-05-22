package com.group15A.BusinessLogic;

import com.group15A.CustomExceptions.CustomException;
import com.group15A.DataModel.Patient;

/**
 * The interface for MultiPanelWindowLogic
 *
 * @author Milovan Gveric
 */
public interface IMultiPanelWindow {
    void logOut() throws Exception;

    void createLog(Patient patient, String msg) throws CustomException;

    Patient getPatient(Integer patientID) throws Exception;

}

