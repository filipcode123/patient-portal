package com.group15A.Utils;

/**
 * Error codes to be used with Custom Exceptions
 *
 * @author Andrei Constantin
 */
public enum ErrorCode
{
    // Form input errors
    WRONG_EMAIL, WRONG_PASSWORD, WRONG_DATE, WRONG_FIRST_NAME, WRONG_LAST_NAME,
    WRONG_MIDDLE_NAME, WRONG_GENDER, WRONG_PHONE_NO, WRONG_CONFIRMED_EMAIL, WRONG_CONFIRMED_PASSWORD,
    EMAIL_IN_USE, DOCTOR_NOT_CHOSEN, WRONG_TIME, IMPOSSIBLE_BOOKING, WRONG_BOOKING_TYPE,

    // Other errors
    DATABASE_ERROR, USER_NOT_FOUND
}
