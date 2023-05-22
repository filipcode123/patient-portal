package com.group15A.GUI;

import com.group15A.BusinessLogic.RegisterLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataModel.Doctor;
import com.group15A.DataModel.Patient;
import com.group15A.Session;
import com.group15A.Utils.*;

import javax.swing.*;
import java.awt.*;

import java.util.HashMap;
import java.util.List;

/**
 * To allow for communication to the business layer and to take care of event handling

 * registerPanel is the actual panel that gets provided to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class RegisterPanel extends BasePanel {
    private JPanel registerPanel;

    private JLabel registerTitleLabel;
    private JButton continueButton;
    private JTextField firstNameField;
    private JTextArea passwordTextArea;
    private JButton logInButton;
    private JLabel personalSectionLabel;
    private JLabel accountSectionLabel;
    private JLabel firstNameLabel;
    private JLabel middleNameLabel;
    private JLabel lastNameLabel;
    private JLabel sexLabel;
    private JLabel dateOfBirth;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel confirmEmailLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    private JLabel firstNameErrorLabel;
    private JLabel middleNameErrorLabel;
    private JLabel lastNameErrorLabel;
    private JLabel sexErrorLabel;
    private JLabel dateOfBirthErrorLabel;
    private JLabel phoneErrorLabel;
    private JLabel emailErrorLabel;
    private JLabel confirmEmailErrorLabel;
    private JLabel passwordErrorLabel;
    private JLabel confirmPasswordErrorLabel;
    private JSeparator leftSeparator;
    private JSeparator topSeparator;
    private JSeparator bottomSeparator;
    private JSeparator rightSeparator;
    private JPanel personalPanel;
    private JPanel accountPanel;
    private JPanel registerButtonPanel;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JComboBox sexCombo;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField confirmEmailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JPanel contentPanel;
    private JScrollPane contentScrollPane;
    private JPanel formPanel;
    private JComboBox dayCombo;
    private JComboBox monthCombo;
    private JComboBox yearCombo;
    private JPanel datePanel;
    private JLabel doctorLabel;
    private JButton chooseDoctorButton;
    private JLabel doctorErrorLabel;
    private JComboBox doctorCombo;

    private RegisterLogic registerLogic;
    private Doctor chosenDoctor;
    private HashMap<ErrorCode,JLabel> errorLabelCodes;

    /**
     * Constructor for RegisterPanel class
     *
     * Adds items to date combo boxes,
     * adds doctors to combo box,
     * displays error pop up if database isn't connected then closes the program
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public RegisterPanel(MultiPanelWindow panelController) {
        super("Register", "registerPanel", panelController);

        logInButton.setMargin(new Insets(0,0,0,0));

        JWidgetShortcuts.addItemsToCombo(dayCombo,1,31,1,"Day");
        JWidgetShortcuts.addItemsToCombo(monthCombo,1,12,1,"Month");
        JWidgetShortcuts.addItemsToCombo(yearCombo,2022,1900,1,"Year");

        createErrorMap();
        createActionListeners();

        try {
            registerLogic = new RegisterLogic();

        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(registerPanel);
        }
    }

    /**
     * Adds error codes : error label key-value pairs to hash map
     *
     * Error labels can be accessed by giving their respective error code
     */
    private void createErrorMap() {
        errorLabelCodes = new HashMap<>(){{
            put(ErrorCode.WRONG_FIRST_NAME, firstNameErrorLabel);
            put(ErrorCode.WRONG_MIDDLE_NAME, middleNameErrorLabel);
            put(ErrorCode.WRONG_LAST_NAME, lastNameErrorLabel);
            put(ErrorCode.WRONG_GENDER, sexErrorLabel);
            put(ErrorCode.WRONG_DATE, dateOfBirthErrorLabel);
            put(ErrorCode.WRONG_PHONE_NO, phoneErrorLabel);
            put(ErrorCode.WRONG_EMAIL, emailErrorLabel);
            put(ErrorCode.EMAIL_IN_USE, emailErrorLabel);
            put(ErrorCode.WRONG_CONFIRMED_EMAIL, confirmEmailErrorLabel);
            put(ErrorCode.WRONG_PASSWORD, passwordErrorLabel);
            put(ErrorCode.WRONG_CONFIRMED_PASSWORD, confirmPasswordErrorLabel);
            put(ErrorCode.DOCTOR_NOT_CHOSEN, doctorErrorLabel);
        }};
    }

    /**
     * @return return registerPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.registerPanel;
    }

    /**
     * Receives:
     *  - The Doctor instance that was chosen by the user
     *
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair) {
        if (pair.getFirst().equals(ReceiveType.DOCTOR)) {
            this.chosenDoctor = (Doctor) pair.getSecond();
            this.chooseDoctorButton.setText(this.chosenDoctor.getFullName());
        }
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        logInButton.addActionListener( e -> panelController.showPage(PageType.LOGIN));
        continueButton.addActionListener(e -> this.registerNewPatient());
        chooseDoctorButton.addActionListener(e ->
                panelController.showPage(
                        PageType.CHOOSE_DOCTOR,
                        new ReceivePair(ReceiveType.RETURN_PAGE, PageType.REGISTER)
                )
        );
    }

    /**
     * Try to register patient by passing given inputs to
     * registerLogic.register() method
     *
     * If registration is successful, go to the home panel,
     * otherwise, show relevant error labels for invalid inputs
     */
    private void registerNewPatient() {
        try {
            Patient newPatient = registerLogic.register(
                firstNameField.getText(),
                middleNameField.getText(),
                lastNameField.getText(),
            yearCombo.getSelectedItem().toString()+"-"+
                monthCombo.getSelectedItem().toString()+"-"+
                dayCombo.getSelectedItem().toString(),
                sexCombo.getSelectedItem().toString(),
                phoneField.getText(),
                emailField.getText(),
                confirmEmailField.getText(),
                new String(passwordField.getPassword()),
                new String(confirmPasswordField.getPassword()),
                chosenDoctor
            );

            Session currentSession = panelController.getSession();
            currentSession.setLoggedInPatient(newPatient);
            currentSession.setKeepLoggedIn(false);
            registerLogic.registerNotification(newPatient);
            registerLogic.registerLog(newPatient);
            panelController.refreshPages();
            currentSession.saveToFile();
            panelController.showPage(PageType.HOME);
        } catch (CustomException e) {
            clearErrorLabels();
            setErrorLabels(e);
        }

    }

    /**
     * Sets the visibility of each error label
     *
     * Will do so depending on if its respective error code is
     * in the list that is returned by the CustomException
     *
     * @param e The customException, contains a list of error codes.
     */
    private void setErrorLabels(CustomException e)
    {
        List<ErrorCode> errorCodes = e.getErrorList();
        for (ErrorCode errorCode : errorCodes) {
            System.out.println(errorCode);
            JLabel errLabel = errorLabelCodes.get(errorCode);
            if (errLabel != null) {
                errLabel.setVisible(true);
            }
        }
    }

    /**
     * Make all error labels invisible
     */
    private void clearErrorLabels() {
        for (JLabel errorLabel : errorLabelCodes.values()) {
            errorLabel.setVisible(false);
        }
    }

}
