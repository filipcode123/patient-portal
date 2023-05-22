package com.group15A.CustomExceptions;

/**
 * Exception encountered when trying to get a specific booking from the database.
 *
 * @author Andrei Constantin
 */
public class BookingNotFoundException extends CustomException
{
    /**
     * Constructor for the BookingNotFoundException. It creates a Custom Exception with no error codes and a suitable error message
     */
    public BookingNotFoundException()
    {
        super("The booking with the given details was not found.");
    }
}
