package com.group15A;

import com.group15A.DataModel.Patient;

import javax.swing.*;
import java.io.*;

/**
 * The session stores the logged in patient and other useful information. It can be easily saved and loaded from a file.
 *
 * @author Andrei Constantin
 */
public class Session implements Serializable {
    private int loggedInPatientID;
    private boolean keepLoggedIn = false;

    /**
     * Constructor for the Session class.
     * @param patient The logged in patient
     * @param keepLoggedIn Whether to keep the user logged in
     */
    public Session(Patient patient, boolean keepLoggedIn)
    {
        setKeepLoggedIn(keepLoggedIn);
        setLoggedInPatient(patient);
    }

    public int getLoggedInPatientID() {
        return loggedInPatientID;
    }

    public boolean isKeepLoggedIn() {
        return keepLoggedIn;
    }

    public void setKeepLoggedIn(boolean keepLoggedIn) {
        this.keepLoggedIn = keepLoggedIn;
    }

    public void setLoggedInPatient(Patient patient) {
        this.loggedInPatientID = (patient == null) ? -1 : patient.getPatientID();
    }

    /**
     * Delete the session from disk
     */
    public static void deleteSession()
    {
        new File(getFileName()).delete();
    }

    /**
     * Save the session to disk
     */
    public void saveToFile()
    {
        var fileName = getFileName();
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(fileName, false)))
        {
            outStream.writeObject(this);
        } catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Load the session from disk
     * @return The session. If the session was not found, return null
     */
    public static Session loadFromFile() throws Exception
    {
        var fileName = getFileName();
        try(ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (Session) inStream.readObject();
        }
        catch(FileNotFoundException e){
            System.out.println("No session file found.");
        }
        return null;
    }

    /**
     * Get the file name + path for the session save location
     * @return The file name + path
     */
    private static String getFileName()
    {
        File directory = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString());
        if(!directory.exists())
            directory.mkdir();

        return directory + "/LoggedUser.bin";
    }
}
