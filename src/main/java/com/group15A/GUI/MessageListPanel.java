package com.group15A.GUI;

import com.group15A.Utils.JWidgetShortcuts;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel containing:
 * - a JPanel to store a list of MessagePanels surrounded with a JScrollPane
 * - a JLabel title
 * - a JLabel "no messages" message
 *
 * @author Filip Fois
 */
public class MessageListPanel {
    private JLabel headerLabel;
    private JPanel messageContentPanel;
    private JLabel noMessagesLabel;
    private JPanel messageListPanel;
    private JScrollPane messageScrollPanel;
    private JPanel messageExtraPanel;
    private final GridBagConstraints gbc;
    private final String headerText;
    private final String noMessagesText;
    private final boolean showCount;

    /**
     * The constructor for the MessageListPanel
     *
     * @param headerText The text to be shown in the header label
     * @param noMessagesText The text to be shown in the "no messages" label
     * @param showCount A boolean determining if the MessagePanel count will be shown
     */
    public MessageListPanel(String headerText, String noMessagesText, boolean showCount)
    {
        this.headerText = headerText;
        this.showCount = showCount;
        this.noMessagesText = noMessagesText;
        this.headerLabel.setText(this.headerText);
        this.noMessagesLabel.setText(this.noMessagesText);
        gbc = JWidgetShortcuts.getStackGBC();
    }

    /**
     * Add a MessagePanel's JPanel to the messageContentPanel JPanel
     *
     * @param heading The text for the message heading label
     * @param subheading The text for the message subheading label
     * @param message The text for the message text pane
     * @param buttonText The text for the message button
     * @return The MessagePanel object
     */
    public MessagePanel addMessage(String heading, String subheading, String message, String buttonText)
    {
        MessagePanel messagePanel = new MessagePanel(heading,subheading,message,buttonText);
        messageContentPanel.add(messagePanel.getMainPanel(),gbc);
        updateCount();
        return messagePanel;
    }

    /**
     * If the count is to be shown,
     * get the number of components in the messageContentPanel JPanel (besides the "no message" label)
     * and display the count in the panel header
     */
    private void updateCount()
    {
        if(showCount) {
            setHeaderText(headerText + " (" + (messageContentPanel.getComponentCount()-1) + ")");
        }
    }


    public JPanel getPanel()
    {
        return messageListPanel;
    }

    public JLabel getHeaderLabel()
    {
        return headerLabel;
    }

    public JLabel getNoMessagesLabel()
    {
        return noMessagesLabel;
    }

    public JPanel getContentPanel()
    {
        return messageContentPanel;
    }

    /**
     * Remove all elements from the panel given by getContentPanel()
     * Add the "no messages" label after it was removed
     * Update the count
     * Show the no messages label
     */
    public void clearMessages()
    {
        JWidgetShortcuts.clearJPanel(getContentPanel());
        messageContentPanel.add(noMessagesLabel);
        updateCount();
        showNoMessagesLabel();

    }

    public void hideNoMessagesLabel()
    {
        getNoMessagesLabel().setVisible(false);
    }

    public void showNoMessagesLabel()
    {
        getNoMessagesLabel().setVisible(true);
    }

    public void setHeaderText(String string)
    {
        getHeaderLabel().setText(string);
    }

    private void createUIComponents() {
        noMessagesLabel = new JLabel(noMessagesText);
    }
}
