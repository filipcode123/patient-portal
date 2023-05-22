package com.group15A.BusinessLogic;

import com.group15A.DataModel.Doctor;

/**
 * The interface for ViewProfileLogic
 *
 * @author Milovan Gveric
 */
public interface IViewProfile {
    void updatePatientDoctor(Integer patientID, Doctor newDoctor) throws Exception;
}
