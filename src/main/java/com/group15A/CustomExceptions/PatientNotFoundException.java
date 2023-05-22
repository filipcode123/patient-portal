package com.group15A.CustomExceptions;

import com.group15A.Utils.ErrorCode;

import java.util.ArrayList;

/**
 * Exception encountered when trying to get a specific patient from the database.
 *
 * @author Andrei Constantin
 */
public class PatientNotFoundException extends CustomException
{
    /**
     * Constructor for the PatientNotFoundException. It creates a new Custom Exception with the USER_NOT_FOUND error code
     * and a suitable error message.
     */
    public PatientNotFoundException()
    {
        super("The patient with the given details was not found.", new ArrayList<>(){{
            add(ErrorCode.USER_NOT_FOUND);
        }});
    }
}
