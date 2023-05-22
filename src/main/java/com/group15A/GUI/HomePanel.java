package com.group15A.GUI;

import com.group15A.BusinessLogic.HomeLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.DataModel.Notification;
import com.group15A.DataModel.Patient;
import com.group15A.Session;
import com.group15A.Utils.*;

import javax.swing.*;
import java.util.List;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * homePanel is the actual panel that gets provided to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class HomePanel extends BasePanel {
    private JPanel homePanel;
    private JButton logOutButton;
    private JPanel contentScrollPane;
    private JLabel titleLabel;
    private JPanel notificationPanel;
    private JPanel navigationPanel;
    private JButton viewBookingsButton;
    private JButton newBookingButton;
    private JPanel messageContentPanel;
    private JLabel messageLabel;
    private JLabel noMessagesLabel;
    private JButton viewProfileButton;
    private JTabbedPane messagesTabbedPane;
    private JPanel inboxPanel;
    private JPanel archivePanel;
    private JButton myActivityButton;

    private HomeLogic homeLogic;
    private List<Notification> notifList;

    private final MessageListPanel newMessageList;
    private final MessageListPanel oldMessageList;

    /**
     * Constructor for the HomePanel class
     *
     * Creates action listeners for widgets
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public HomePanel(MultiPanelWindow panelController) {
        super("Home", "homePanel", panelController);

        newMessageList = new MessageListPanel(
                "New messages",
                "No new messages.",
                true
        );

        oldMessageList = new MessageListPanel(
                "Archived messages",
                "No archived messages.",
                true
        );

        inboxPanel.add(newMessageList.getPanel());
        archivePanel.add(oldMessageList.getPanel());

        try {
            this.homeLogic = new HomeLogic();
        } catch (CustomException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(homePanel);
        }

        createActionListeners();
    }

    /**
     * For each notification in `notifList`,
     * if it's marked as new, add a message to the new MessageListPanel
     * and assign an action to the "mark as read" button,
     * otherwise, add it the old MessageListPanel
     */
    private void displayNotifications() {
        newMessageList.clearMessages();
        oldMessageList.clearMessages();
        newMessageList.showNoMessagesLabel();
        oldMessageList.showNoMessagesLabel();

        if(!notifList.isEmpty()) {
            for (int i=notifList.size()-1; i>=0; i--) {
                Notification notification = notifList.get(i);
                if (!notification.isNew()) {
                    oldMessageList.hideNoMessagesLabel();
                    oldMessageList.addMessage(
                            notification.getHeader(),
                            "(" + DataModification.shortDateTime(notification.getTimestamp()) + ")",
                            notification.getMessage(),
                            ""
                    );
                }
                else{
                    newMessageList.hideNoMessagesLabel();
                    MessagePanel newNotificationDisplay = newMessageList.addMessage(
                            notification.getHeader(),
                            "(" + DataModification.shortDateTime(notification.getTimestamp()) + ")",
                            notification.getMessage(),
                            "Mark as read"
                    );

                    newNotificationDisplay.getButton().addActionListener(e -> {
                        newNotificationDisplay.getButton().setVisible(false);
                        this.markAsRead(notification);
                    });
                }

            }

        }
    }


    /**
     * Mark a given notification's isNew attribute to false
     */
    private void markAsRead(Notification notification) {
        try {
            this.homeLogic.readNotification(notification);
            this.receiveData(null);
        } catch (CustomException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(homePanel);
        }
    }

    /**
     * @return homePanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.homePanel;
    }

    /**
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair) {
        try {
            Patient patient = homeLogic.getPatient(panelController.getSession().getLoggedInPatientID());
            titleLabel.setText("Welcome, " + patient.getFirstName() + ".");
            this.notifList = this.homeLogic.getNotifications(patient);
            this.displayNotifications();
        } catch (CustomException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(homePanel);
        }

    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners() {
        logOutButton.addActionListener(e -> logOutUser());
        viewBookingsButton.addActionListener(e -> panelController.showPage(
                PageType.VIEW_BOOKINGS,
                new ReceivePair(ReceiveType.NEW_BOOKINGS, null),
                new ReceivePair(ReceiveType.PATIENT_ID, this.panelController.getSession().getLoggedInPatientID())
        ));

        newBookingButton.addActionListener(e -> panelController.showPage(
                PageType.ADD_BOOKING,
                new ReceivePair(ReceiveType.RETURN_PAGE, PageType.HOME),
                new ReceivePair(ReceiveType.PATIENT_ID, this.panelController.getSession().getLoggedInPatientID())
        ));

        viewProfileButton.addActionListener(e -> this.panelController.showPage(PageType.VIEW_PROFILE));
    }

    /**
     * Delete the log-in session file and go to log in page.
     */
    private void logOutUser()
    {
        try {
            homeLogic.logOut();
        } catch(Exception e) {
            System.out.println("Error logging out");
        } finally {
            Session.deleteSession();
        }

        panelController.refreshSession();
        panelController.showPage(PageType.LOGIN);
    }

}
