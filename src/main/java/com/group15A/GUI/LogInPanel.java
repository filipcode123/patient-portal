package com.group15A.GUI;

import com.group15A.BusinessLogic.LogInLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.Session;
import com.group15A.Utils.JWidgetShortcuts;
import com.group15A.Utils.PageType;
import com.group15A.Utils.ReceivePair;

import javax.swing.*;
import java.awt.*;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * loginPanel is the actual panel that gets passed to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class LogInPanel extends BasePanel {
    private JPanel logInPanel;

    private JLabel logInTitleLabel;

    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private JButton registerButton;
    private JButton logInButton;
    private JLabel logInErrorLabel;
    private JPanel textFieldsPanel;
    private JPanel contentPanel;
    private JScrollPane contentScrollPane;
    private JCheckBox stayLoggedInCheckBox;

    private LogInLogic logInLogic;

    /**
     * Constructor for the LogInPanel class
     *
     * Creates action listeners
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public LogInPanel(MultiPanelWindow panelController)
    {
        super("Log in", "logInPanel", panelController);
        // TODO: Implement setMargin on these buttons using LogInPanel.form instead of in this file.
        registerButton.setMargin(new Insets(0,0,0,0));
        createActionListeners();

        try {
            this.logInLogic = new LogInLogic();
        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(logInPanel);
        }
    }

    /**
     * @return logInPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.logInPanel;
    }

    /**
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair) {

    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners() {
        registerButton.addActionListener( e -> panelController.showPage(PageType.REGISTER));
        logInButton.addActionListener(e -> this.logInPatient());
    }

    /**
     * Pass email and password for LogInLogic to attempt a login
     *
     * If successful, go to the home page,
     * otherwise, stay on log in page and show error label
     */
    private void logInPatient() {
        Boolean stayLoggedIn = stayLoggedInCheckBox.isSelected();
        logInErrorLabel.setVisible(false);

        try {
            Session newSession = logInLogic.login(
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    stayLoggedIn
            );
            panelController.setSession(newSession);
            panelController.showPage(PageType.HOME);
            logInErrorLabel.setVisible(false);

        } catch (CustomException e) {
            logInErrorLabel.setVisible(true);
        }

        passwordField.setText("");
    }

}
