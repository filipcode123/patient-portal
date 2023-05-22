package com.group15A.BusinessLogic;

import com.group15A.DataModel.Log;
import java.util.List;

/**
 * The interface for LogLogic
 *
 * @author Milovan Gveric
 */
public interface ILog {
    List<Log> getLogs(Integer patientID) throws Exception;
}
