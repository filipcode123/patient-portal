package com.group15A.CustomExceptions;

import com.group15A.Utils.ErrorCode;

import java.util.ArrayList;

/**
 * Exception encountered when connecting to/querying the database.
 *
 * @author Andrei Constantin
 */
public class DatabaseException extends CustomException
{
    /**
     * Constructor for the DatabaseException. It will create a custom exception with the DATABASE_ERROR error code.
     * @param errorMessage The error message
     */
    public DatabaseException(String errorMessage)
    {
        super(errorMessage, new ArrayList<>(){{add(ErrorCode.DATABASE_ERROR);}});
    }
}
