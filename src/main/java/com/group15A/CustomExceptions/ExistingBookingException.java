package com.group15A.CustomExceptions;

/**
 * Exception encountered when making new booking conflicts with the booking time of a
 * previously made booking
 *
 * @author Milovan Gveric
 */
public class ExistingBookingException extends CustomException
{
    /**
     * Constructor for the ExistingBookingException. It creates a Custom Exception with no error codes and a suitable error message
     */
    public ExistingBookingException()
    {
        super("A booking at that time already exists");
    }
}
