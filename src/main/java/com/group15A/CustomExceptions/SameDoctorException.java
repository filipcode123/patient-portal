package com.group15A.CustomExceptions;

/**
 * Exception encountered when patient tries to change from their doctor to the same doctor
 *
 * @author Milovan Gveric
 */
public class SameDoctorException extends CustomException
{
    /**
     * Constructor for the SameDoctorException. It creates a Custom Exception with no error codes and a suitable error message
     */
    public SameDoctorException()
    {
        super("You can't change to the same doctor");
    }
}
