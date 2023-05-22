package com.group15A.CustomExceptions;

import com.group15A.Utils.ErrorCode;

import java.util.ArrayList;

/**
 * Exception encountered when trying to register a patient with an email account that is already in use.
 *
 * @author Andrei Constantin
 */
public class EmailInUseException extends CustomException
{
    /**
     * Constructor for the EmailInUseException. It creates a Custom Exception with the EMAIL_IN_USE error code and a
     * suitable error message
     */
    public EmailInUseException() {
        super("Email already in use", new ArrayList<>(){{add(ErrorCode.EMAIL_IN_USE);}});
    }
}
