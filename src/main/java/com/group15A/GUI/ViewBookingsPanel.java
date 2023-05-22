package com.group15A.GUI;

import com.group15A.BusinessLogic.ViewBookingLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataModel.Booking;
import com.group15A.DataModel.Doctor;
import com.group15A.Utils.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * To allow for communication to the business layer and to take care of event handling

 * viewBookingPanel is the actual panel that gets provided to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class ViewBookingsPanel extends BasePanel {
    private JButton goHomeButton;
    private JPanel viewBookingsPanel;
    private JPanel contentPanel;
    private JPanel bookingsPanel;
    private JLabel messageLabel;
    private JButton newBookingButton;
    private JPanel bookingsDisplayPanel;
    private JLabel titleLabel;
    private JComboBox monthComboBox;
    private JButton searchButton;
    private JComboBox yearComboBox;
    private JLabel dateErrorLabel;
    private JLabel monthLabel;
    private JLabel yearLabel;

    private ViewBookingLogic viewBookingLogic;
    private List<Booking> bookingsList;
    private List<JPanel> bookingLabelsList;

    private final MessageListPanel messageListPanel;

    private Boolean pastBookingFlag = false;

    /**
     * Constructor for ViewBookingsPanel
     *
     * Set options for combo-boxes and
     * sets up messageListPanel and logic class
     */
    public ViewBookingsPanel(MultiPanelWindow panelController)
    {
        super("My bookings", "viewBookingPanel", panelController);

        dateErrorLabel.setVisible(false);

        messageListPanel = new MessageListPanel(
                "My bookings",
                "No bookings.",
                true
        );
        JWidgetShortcuts.addItemsToCombo(monthComboBox,1,12,1,"Month (All)");
        int year = 2022;
        JWidgetShortcuts.addItemsToCombo(yearComboBox,2022,year+10,1,"Year (All)");
        bookingsPanel.add(messageListPanel.getPanel());

        createActionListeners();

        try {
            bookingLabelsList = new ArrayList<>();
            viewBookingLogic = new ViewBookingLogic();
        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(viewBookingsPanel);
        }
    }

    /**
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair)
    {
        if (pair.getFirst().equals(ReceiveType.PATIENT_ID)) {
            Integer patientID = (Integer) pair.getSecond();
            try {
                bookingsList = this.viewBookingLogic.getBookings(patientID, pastBookingFlag);
                messageListPanel.hideNoMessagesLabel();
                this.displayBookings();
            } catch (CustomException e) {
                JWidgetShortcuts.showDatabaseExceptionPopupAndExit(viewBookingsPanel);
            }
        } else if (pair.getFirst().equals(ReceiveType.NEW_BOOKINGS)) {
            pastBookingFlag = false;
            this.updateBookingLabels("My current bookings");

        } else if (pair.getFirst().equals(ReceiveType.PAST_BOOKINGS)) {
            pastBookingFlag = true;
            this.updateBookingLabels("My past bookings");

        }
    }

    /**
     * Switches between 'My current bookings' and 'My past bookings' booking strings for descriptive labels
     * @param newBookingPageText The text to change the titleLabel to
     */
    private void updateBookingLabels(String newBookingPageText) {
        this.titleLabel.setText(newBookingPageText);
    }

    /**
     * For each notification in `notifList`,
     * add a message to the new MessageListPanel
     * and assign an action to the "Reschedule" button
     *
     * @throws CustomException when patient's doctor cannot be accessed
     */
    public void displayBookings() throws CustomException {
        messageListPanel.clearMessages();
        messageListPanel.showNoMessagesLabel();

        String message;
        Randomiser randomiser = new Randomiser();

        if(!bookingsList.isEmpty()){
            messageListPanel.hideNoMessagesLabel();
            for (Booking b : bookingsList) {
                Doctor doctor = this.viewBookingLogic.getDoctor(b.getDoctorID());

                if (pastBookingFlag) {
                    if (b.getPrescription() == null) {
                        b.setPrescription(randomiser.getRandPrescription());
                        b.setDetails(randomiser.getRandDetails());
                        this.viewBookingLogic.updateBooking(b);
                    }

                    message = "Booking at "+
                            DataModification.getTime(b.getBookingTime())+
                            " on "+DataModification.fullDate(b.getBookingTime())+"."+
                            "Doctor assigned prescription: "+b.getPrescription()+
                            "details include: "+b.getDetails();
                } else {
                    message = "Booking at "+
                            DataModification.getTime(b.getBookingTime())+
                            " on "+DataModification.fullDate(b.getBookingTime());
                }

                MessagePanel bookingMessage = messageListPanel.addMessage(
                        "",
                        "With Dr. "+doctor.getFullName()+" ("+b.getType()+")",
                        message,
                        "Reschedule");

                bookingLabelsList.add(bookingMessage.getMainPanel());

                if (!pastBookingFlag) {
                    bookingMessage.getButton().addActionListener(e -> this.rescheduleBooking(b));
                } else {
                    bookingMessage.getButton().setVisible(false);
                }
            }
        }
    }

    /**
     * Goes to add booking page to reschedule booking
     *
     * @param booking The booking to be rescheduled
     */
    private void rescheduleBooking(Booking booking)
    {
        this.panelController.showPage(
                PageType.ADD_BOOKING,
                new ReceivePair(ReceiveType.PATIENT_ID, this.panelController.getSession().getLoggedInPatientID()),
                new ReceivePair(ReceiveType.RETURN_PAGE, PageType.VIEW_BOOKINGS),
                new ReceivePair(ReceiveType.BOOKING, booking)
        );
    }

    /**
     * Display only bookings within the given month and year
     *
     * "Month (All)" and "Year (All)" can be mixed with each other
     * and given months and years to, for example get bookings for all months in the year 2021
     */
    private void filterBookings()
    {
        try {
            bookingsList = this.viewBookingLogic.filterBookings(
                    monthComboBox.getSelectedItem().toString(),
                    yearComboBox.getSelectedItem().toString(),
                    panelController.getSession().getLoggedInPatientID(),
                    pastBookingFlag
            );
            dateErrorLabel.setVisible(false);
            this.displayBookings();
        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(viewBookingsPanel);
        } catch (CustomException e) {
            dateErrorLabel.setVisible(true);
        }
    }

    /**
     * @return viewBookingsPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.viewBookingsPanel;
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        goHomeButton.addActionListener(e -> panelController.showPage(PageType.HOME));
        searchButton.addActionListener(e -> filterBookings());
    }

}