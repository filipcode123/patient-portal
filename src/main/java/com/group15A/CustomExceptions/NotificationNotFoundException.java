package com.group15A.CustomExceptions;

/**
 * Exception encountered when trying to get a specific notification from the database.
 *
 * @author Andrei Constantin
 */
public class NotificationNotFoundException extends CustomException
{
    /**
     * Constructor for the NotificationNotFoundException. It creates a Custom Exception with no error codes and a suitable error message
     */
    public NotificationNotFoundException()
    {
        super("The notification with the given details was not found.");
    }
}
