package com.group15A.CustomExceptions;

/**
 * Exception encountered when trying to access MultiPanelWindow.java's session field when it is not set (i.e. empty)
 *
 * @author Filip Fois
 */
public class SessionEmptyException extends CustomException
{
    /**
     * Constructor for the SessionEmptyException. It creates a Custom Exception with no error codes and a suitable error message
     */
    public SessionEmptyException()
    {
        super("The session has not been set.");
    }
}
