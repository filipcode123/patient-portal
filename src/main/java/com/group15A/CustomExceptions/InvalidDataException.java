package com.group15A.CustomExceptions;

/**
 * Thrown when invalid data was sent to the Data Access Layer
 * @author Andrei Constantin
 */
public class InvalidDataException extends CustomException
{
    /**
     * Constructor for the InvalidDataException. It creates a CustomException with no error codes and the given message
     * @param errorMessage The error message
     */
    public InvalidDataException(String errorMessage)
    {
        super(errorMessage);
    }
}
