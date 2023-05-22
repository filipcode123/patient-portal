package com.group15A.BusinessLogic;

import com.group15A.DataModel.Notification;
import com.group15A.DataModel.Patient;
import java.util.List;

/**
 * The interface for HomeLogic
 *
 * @author Milovan Gveric
 */
public interface IHome {
    List<Notification> getNotifications(Patient patient) throws Exception;

    Patient getPatient(Integer patientID) throws Exception;

    void readNotification(Notification notification) throws Exception;

    void logOut() throws Exception;
}
