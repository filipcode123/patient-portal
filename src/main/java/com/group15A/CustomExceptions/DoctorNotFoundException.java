package com.group15A.CustomExceptions;

/**
 * Exception encountered when trying to get a specific doctor from the database.
 *
 * @author Andrei Constantin
 */
public class DoctorNotFoundException extends CustomException
{
    /**
     * Constructor for the DoctorNotFoundException. It creates a Custom Exception with no error codes and a suitable error message
     */
    public DoctorNotFoundException()
    {
        super("The doctor with the given details was not found.");
    }
}
