package com.group15A.GUI;

import com.group15A.BusinessLogic.DoctorLogic;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataModel.Doctor;
import com.group15A.Utils.JWidgetShortcuts;
import com.group15A.Utils.PageType;
import com.group15A.Utils.ReceivePair;
import com.group15A.Utils.ReceiveType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * chooseDoctorPanel is the actual panel that gets passed to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class ChooseDoctorPanel extends BasePanel {
    private JPanel chooseDoctorPanel;
    private JPanel contentPanel;
    private JButton registerButton;
    private JPanel doctorListPanel;
    private JScrollPane doctorListScrollPane;
    private JLabel promptLabel;

    private PageType returningPage;
    private DoctorLogic doctorLogic;
    private List<Doctor> doctorsList;
    private List<JButton> doctorButtons;

    /**
     * Constructor for the ChooseDoctorPanel class
     *
     * Creates action listeners for widgets
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public ChooseDoctorPanel(MultiPanelWindow panelController)
    {
        super("New doctor", "chooseDoctorPanel", panelController);
        createActionListeners();

        try {
            doctorLogic = new DoctorLogic();
            doctorButtons = new ArrayList<JButton>();
            doctorsList = doctorLogic.getDoctors();
        } catch (DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(chooseDoctorPanel);
        }

        addDoctorsToPanel();
    }

    private void chooseDoctor(JButton clickedButton) {
        Integer index = doctorButtons.indexOf(clickedButton);
        panelController.showPage(
            this.returningPage,
            new ReceivePair(ReceiveType.DOCTOR, doctorsList.get(index))
        );
    }

    /**
     * Dynamically add buttons with doctors' first and last names to the
     * scroll pane
     */
    private void addDoctorsToPanel() {
        GridBagConstraints gbc = JWidgetShortcuts.getStackGBC();

        for (Doctor d : doctorsList) {
            JButton doctorButton = new JButton();
            doctorButton.setText(d.getFullName());
            doctorButton.setFont(new Font("", Font.BOLD, 30));
            doctorButton.addActionListener(e -> this.chooseDoctor((JButton) e.getSource()));

            doctorButtons.add(doctorButton);
            doctorListPanel.add(doctorButton, gbc);
        }
    }

    /**
     * @return chooseDoctorPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.chooseDoctorPanel;
    }

    /**
     * Receives:
     *  - Which page to switch back to (as Choose Doctor Panel is shared)
     *
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair) {
        if (pair.getFirst().equals(ReceiveType.RETURN_PAGE)) {
            this.returningPage = (PageType) pair.getSecond();
        }
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        registerButton.addActionListener( e -> panelController.showPage(this.returningPage));
    }

}
