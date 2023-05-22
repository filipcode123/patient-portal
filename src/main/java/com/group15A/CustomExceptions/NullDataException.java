package com.group15A.CustomExceptions;

import com.group15A.Utils.ErrorCode;

import java.util.List;

/**
 * Thrown when null data was sent for a non-nullable value in the database
 * @author Andrei Constantin
 */
public class NullDataException extends CustomException
{
    /**
     * Constructor for the NullDataException. Creates a Custom Exception with no error codes and the given error message
     * @param errorMessage The error message
     */
    public NullDataException(String errorMessage)
    {
        super(errorMessage);
    }
}
