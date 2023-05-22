package com.group15A.GUI;

import com.group15A.BusinessLogic.LogLogic;
import com.group15A.CustomExceptions.*;
import com.group15A.DataModel.Log;
import com.group15A.Utils.*;

import javax.swing.*;
import java.util.List;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * loggingPanel is the actual panel that gets passed to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class LogPanel extends BasePanel {
    private JPanel loggingPanel;
    private JButton homeButton;
    private JPanel contentPanel;
    private JPanel logsPanel;
    private final MessageListPanel messageListPanel;

    private List<Log> userLogs;
    private LogLogic logLogic;

    /**
     * Constructor for the LogPanel class
     *
     * Creates action listeners for widgets
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public LogPanel(MultiPanelWindow panelController)
    {
        super("Activity logs", "loggingPanel", panelController);

        messageListPanel = new MessageListPanel("My activity","No logs.", false);
        logsPanel.add(messageListPanel.getPanel());

        try{
            this.logLogic = new LogLogic();
        } catch(DatabaseException e) {
            JWidgetShortcuts.showDatabaseExceptionPopupAndExit(loggingPanel);
        }

        createActionListeners();
    }

    /**
     * For each message provided by the LogLogic object,
     * create a LogDisplay object and add it to the log display panel.
     */
    private void displayLogs() throws CustomException {
        messageListPanel.clearMessages();
        if(!userLogs.isEmpty()){
            messageListPanel.hideNoMessagesLabel();
            for (int i = userLogs.size()-1; i >= 0; i--) {
                Log log = userLogs.get(i);
                messageListPanel.addMessage(
                        "",
                        DataModification.shortDateTime(log.getTimestamp()),
                        log.getMessage(),
                        ""
                );
            }
        }

    }


    /**
     * @return homePanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.loggingPanel;
    }

    /**
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair)
    {
        if (pair.getFirst().equals(ReceiveType.EVENT)) {
            try {
                this.userLogs = this.logLogic.getLogs(panelController.getSession().getLoggedInPatientID());
                this.displayLogs();
            } catch (CustomException e) {
                e.printStackTrace();
                JWidgetShortcuts.showDatabaseExceptionPopupAndExit(loggingPanel);
            }
        }
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        homeButton.addActionListener(e -> this.panelController.showPage(PageType.VIEW_PROFILE));
    }

}
