package com.group15A.Utils;

/**
 * The type of data to expected to be received by the current page
 *
 * @author Milovan Gveric
 */
public enum ReceiveType {
    // Register Page
    DOCTOR,

    // Choose Doctor Page
    RETURN_PAGE,

    // View Booking Page
    PATIENT_ID,
    NEW_BOOKINGS,
    PAST_BOOKINGS,

    // Create Booking Page
    BOOKING,

    // For Dynamic Events
    EVENT
}
