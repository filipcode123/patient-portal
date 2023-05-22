package com.group15A.GUI;

import com.group15A.BusinessLogic.AddBookingLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.CustomExceptions.DoctorNotFoundException;
import com.group15A.CustomExceptions.ExistingBookingException;
import com.group15A.DataModel.Booking;
import com.group15A.DataModel.Doctor;
import com.group15A.DataModel.Patient;
import com.group15A.Utils.JWidgetShortcuts;
import com.group15A.Utils.PageType;
import com.group15A.Utils.ReceivePair;
import com.group15A.Utils.ReceiveType;
import javax.swing.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * addBookingPanel is the actual panel that gets provided to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Filip Fois
 * @author Milovan Gveric
 */
public class AddBookingPanel extends BasePanel {
    private JPanel addBookingPanel;
    private JButton goBackButton;
    private JButton createBookingButton;
    private JPanel contentPanel;
    private JPanel bookingSelectionPanel;
    private JComboBox dayCombo;
    private JPanel dateSelectionPanel;
    private JComboBox monthCombo;
    private JComboBox yearCombo;
    private JComboBox hourCombo;
    private JComboBox minuteCombo;
    private JPanel timeSelectionPanel;
    private JLabel bookingErrorLabel;
    private JLabel promptLabel;
    private JLabel bookingTitle;
    private JComboBox typeComboBox;
    private JPanel typeLabel;
    private JLabel typeTitle;

    private AddBookingLogic addBookingLogic;
    private PageType returningPage;
    private Booking bookingToEdit;

    /**
     * Constructor for AddBookingPanel class
     *
     * Add options to combo boxes,
     * create action listeners,
     * create addBookingLogic field
     */
    public AddBookingPanel(MultiPanelWindow panelController)
    {
        super("Make Booking", "addBookingPanel", panelController);
        JWidgetShortcuts.addItemsToCombo(dayCombo,1,31,1,"Day");
        JWidgetShortcuts.addItemsToCombo(monthCombo,1,12,1,"Month");
        int year = 2022;
        JWidgetShortcuts.addItemsToCombo(yearCombo,year,year+10,1,null);

        JWidgetShortcuts.addItemsToCombo(hourCombo,9,17,1,"Hour");
        JWidgetShortcuts.addItemsToCombo(minuteCombo,0,55,5,"Minute");
        JWidgetShortcuts.addItemsToCombo(typeComboBox, new String[]{
                "Other", "Routine Checkup", "Emergency Checkup",
                "Telephone Session", "Surgery", "Physical Checkup",
                "Mental Health Checkup", "Blood Testing",
                "General Consultation"
        }, false, "Type");
        createActionListeners();

        try {
            addBookingLogic = new AddBookingLogic();
        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(addBookingPanel);
        }
    }

    /**
     * @return addBookingPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.addBookingPanel;
    }

    /**
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair)
    {
        if (pair.getFirst().equals(ReceiveType.PATIENT_ID)) {
            this.updateDoctorLabels((Integer) pair.getSecond());
        } else if (pair.getFirst().equals(ReceiveType.RETURN_PAGE)) {
            this.resetBookingForm();
            this.returningPage = (PageType) pair.getSecond();
            this.updateBookingLabels(
                (this.returningPage.equals(PageType.VIEW_BOOKINGS)) ? "Reschedule Booking" : "Create Booking"
            );
        } else if (pair.getFirst().equals(ReceiveType.BOOKING)) {
            this.bookingToEdit = (Booking) pair.getSecond();
            this.populateBookingForm(bookingToEdit);
        }
    }

    /**
     * Populates the booking form with the information of the booking to be rescheduled
     * @param booking The booking's information to use to populate the add booking page
     */
    private void populateBookingForm(Booking booking) {
        Timestamp timestamp = booking.getBookingTime();
        yearCombo.setSelectedItem((new SimpleDateFormat("yyyy")).format(timestamp));
        monthCombo.setSelectedItem((new SimpleDateFormat("MM")).format(timestamp));
        dayCombo.setSelectedItem((new SimpleDateFormat("dd")).format(timestamp));
        hourCombo.setSelectedItem((new SimpleDateFormat("HH")).format(timestamp));
        minuteCombo.setSelectedItem((new SimpleDateFormat("mm")).format(timestamp));
        typeComboBox.setSelectedItem(booking.getType());
    }

    /**
     * Set dropdowns to their default values
     */
    private void resetBookingForm() {
        yearCombo.setSelectedItem(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        monthCombo.setSelectedItem("Month");
        dayCombo.setSelectedItem("Day");
        hourCombo.setSelectedItem("Hour");
        minuteCombo.setSelectedItem("Minute");
        typeComboBox.setSelectedItem("Type");
    }

    /**
     * Switches between 'reschedule' and 'create' booking strings for descriptive labels
     * @param newBookingPageText The text that the components will display
     */
    private void updateBookingLabels(String newBookingPageText) {
        this.bookingTitle.setText(newBookingPageText);
        this.createBookingButton.setText(newBookingPageText);
    }

    /**
     * Update the name of the doctor in the title label
     * @param patientID The ID of the patient
     */
    private void updateDoctorLabels(Integer patientID) {
        try {
            Patient patient = this.addBookingLogic.getPatient(patientID);
            Doctor patientDoctor = this.addBookingLogic.getPatientDoctor(patient);
            this.promptLabel.setText("Make your appointment with Dr "+patientDoctor.getFullName());
            this.bookingErrorLabel.setVisible(false);
        } catch (CustomException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(addBookingPanel);
        }
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        goBackButton.addActionListener(e -> {
            panelController.showPage(this.returningPage);
            bookingToEdit = null;
        });
        createBookingButton.addActionListener(e -> this.createOrEditBooking());
    }

    /**
     * Reads inputs
     *
     * If successful:
     * - calls createNewBooking or rescheduleBooking if a booking to be edited is not provided,
     * - hide error label
     * - Go to view booking page to show the booking that was just created
     *
     * Otherwise, shows different error messages depending on the thrown exception
     */
    private void createOrEditBooking() {
        try {

            String date = yearCombo.getSelectedItem().toString()+"-"+
                          monthCombo.getSelectedItem().toString()+"-"+
                          dayCombo.getSelectedItem().toString();

            String hour = hourCombo.getSelectedItem().toString();
            String minute = minuteCombo.getSelectedItem().toString();
            Integer patientID = this.panelController.getSession().getLoggedInPatientID();
            String type = typeComboBox.getSelectedItem().toString();

            if (bookingToEdit == null) {
                this.addBookingLogic.createNewBooking(date, hour, minute, type, patientID);
            } else {
                this.addBookingLogic.rescheduleBooking(date, hour, minute, type, patientID, bookingToEdit);
            }

            this.bookingErrorLabel.setVisible(false);
            this.panelController.showPage(
                    PageType.VIEW_BOOKINGS,
                    new ReceivePair(ReceiveType.PATIENT_ID, patientID)
            );

            bookingToEdit = null;
        } catch (DoctorNotFoundException e) {
            this.bookingErrorLabel.setVisible(true);
            this.bookingErrorLabel.setText("The requested doctor is unavailable");

        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(addBookingPanel);

        } catch (ExistingBookingException e) {
            this.bookingErrorLabel.setVisible(true);
            this.bookingErrorLabel.setText("A booking with that time already exists");

        } catch (CustomException e) {
            this.bookingErrorLabel.setVisible(true);
            this.bookingErrorLabel.setText("The requested booking slot is unavailable");
        }
    }

}