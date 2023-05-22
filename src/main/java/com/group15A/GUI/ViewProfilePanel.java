package com.group15A.GUI;

import com.group15A.BusinessLogic.ViewProfileLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.CustomExceptions.SameDoctorException;
import com.group15A.DataModel.Doctor;
import com.group15A.Utils.JWidgetShortcuts;
import com.group15A.Utils.PageType;
import com.group15A.Utils.ReceivePair;
import com.group15A.Utils.ReceiveType;

import javax.swing.*;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * viewProfilePanel is the actual panel that gets provided to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class ViewProfilePanel extends BasePanel {
    private JPanel viewProfilePanel;
    private JButton backButton;
    private JPanel contentPanel;
    private JPanel doctorPanel;
    private JButton changeDoctorButton;
    private JLabel titleLabel;
    private JLabel doctorSectionLabel;
    private JPanel activityPanel;
    private JLabel activitySectionLabel;
    private JButton viewActivityButton;
    private JPanel sectionsPanel;
    private JPanel bookingsPanel;
    private JButton viewPastBookingsButton;

    private ViewProfileLogic viewProfileLogic;

    /**
     * Constructor for the ProfilePanel class
     *
     * Creates action listeners for widgets and set up viewProfileLogic object
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public ViewProfilePanel(MultiPanelWindow panelController) {
        super("My profile", "viewProfilePanel", panelController);
        createActionListeners();

        try {
            this.viewProfileLogic = new ViewProfileLogic();
        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(viewProfilePanel);
        }
    }
    /**
     * @return return viewProfilePanel
     */
    @Override
    public JPanel getPagePanel() {
        return this.viewProfilePanel;
    }

    /**
     * Receives chosen doctor and update's the patient's doctor in the database,
     * then goes back to the previous page (i.e., the home page)
     * @param pair consists of a key of what data to expect and the value of the data (i.e., the doctor)
     */
    @Override
    public void receiveData(ReceivePair pair) {
        if (pair.getFirst().equals(ReceiveType.DOCTOR)) {
            try {
                this.viewProfileLogic.updatePatientDoctor(
                        this.panelController.getSession().getLoggedInPatientID(),
                        (Doctor) pair.getSecond()
                );
                this.panelController.showPage(PageType.HOME);
            } catch (SameDoctorException e) {
                JOptionPane.showMessageDialog(
                        viewProfilePanel,
                        "You can't change to the same doctor as you already have",
                        "ERROR: Same Doctor",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (CustomException e) {
                JWidgetShortcuts.showDatabaseExceptionPopupAndExit(viewProfilePanel);
            }
        }
    }

    /**
     * Add action listeners to the given events
     */
    @Override
    public void createActionListeners() {
        backButton.addActionListener(e -> this.panelController.showPage(PageType.HOME));

        changeDoctorButton.addActionListener(e -> this.panelController.showPage(
                PageType.CHOOSE_DOCTOR,
                new ReceivePair(ReceiveType.RETURN_PAGE, PageType.VIEW_PROFILE)
        ));

        viewActivityButton.addActionListener(e -> this.panelController.showPage(PageType.LOG));

        viewPastBookingsButton.addActionListener(e -> this.panelController.showPage(
                PageType.VIEW_BOOKINGS,
                new ReceivePair(ReceiveType.PAST_BOOKINGS, null),
                new ReceivePair(ReceiveType.PATIENT_ID, this.panelController.getSession().getLoggedInPatientID())
        ));
    }
}
