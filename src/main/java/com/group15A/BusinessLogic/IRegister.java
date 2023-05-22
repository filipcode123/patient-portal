package com.group15A.BusinessLogic;

import com.group15A.DataModel.Doctor;
import com.group15A.DataModel.Patient;

/**
 * The interface for RegisterLogic
 *
 * @author Milovan Gveric
 * @author Wenbo Wu
 */
public interface IRegister {
    Patient register(String fName, String mName, String lName, String DoB, String gender, String phoneNo,
                     String email, String confirmEmail, String password, String confirmPassword, Doctor chosenDoctor) throws Exception;
}
