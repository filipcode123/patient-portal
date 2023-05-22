package com.group15A.BusinessLogic;

import com.group15A.DataModel.Doctor;
import java.util.List;

/**
 * The interface for DoctorLogic
 *
 * @author Milovan Gveric
 * @author Wenbo Wu
 */
public interface IDoctor {
    List<Doctor> getDoctors() throws Exception;
}
