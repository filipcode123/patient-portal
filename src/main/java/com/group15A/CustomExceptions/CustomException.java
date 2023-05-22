package com.group15A.CustomExceptions;

import com.group15A.Utils.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom exception that includes a list of error codes.
 *
 * @author Andrei Constantin
 */
public class CustomException extends Exception
{
    private final List<ErrorCode> errorList;

    /**
     * Custom exception constructor with a list of error codes.
     * @param errorMessage The error message
     * @param errorList The list of error codes
     */
    public CustomException(String errorMessage, List<ErrorCode> errorList)
    {
        super(errorMessage);
        this.errorList = errorList;
    }

    /**
     * Custom exception with no error codes
     * @param errorMessage The error message
     */
    public CustomException(String errorMessage)
    {
        super(errorMessage);
        this.errorList = new ArrayList<>();
    }

    public List<ErrorCode> getErrorList()
    {
        return errorList;
    }
}
