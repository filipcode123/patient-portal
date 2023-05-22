package com.group15A.GUI;

import javax.swing.*;
import java.util.*;

/**
 * A JPanel containing information for a message
 *
 * Used in MessageListPanel
 *
 * Contains the following optional components:
 * - header label
 * - subheading label
 * - message text panel
 * - button
 */
public class MessagePanel
{
    public JPanel contentPanel;
    public JPanel headingContentPanel;
    public JLabel headingLabel;
    public JLabel subheadingLabel;
    public JTextPane messageTextPane;
    public JButton button;
    public JPanel messagePanel;

    /**
     * The constructor for the MessagePanel class
     *
     * If the string for a given component's text is blank,
     * make the component invisible
     *
     * @param heading The text for the message heading label
     * @param subheading The text for the message subheading label
     * @param message The text for the message text pane
     * @param buttonText The text for the message button
     */
    public MessagePanel(String heading, String subheading, String message, String buttonText)
    {
        HashMap<JComponent, String> widgets = new HashMap<>() {{
            put(headingLabel, heading);
            put(subheadingLabel, subheading);
            put(messageTextPane, message);
            put(button, buttonText);
        }};
        
        for(JComponent component : widgets.keySet()){
            if(widgets.get(component).equals("")){
               component.setVisible(false);
            }
            else{
                if(component instanceof JButton){
                    ((JButton)component).setText(widgets.get(component));
                }
                else if(component instanceof JLabel){
                    ((JLabel)component).setText(widgets.get(component));
                }
                else if(component instanceof JTextPane){
                    ((JTextPane)component).setText(widgets.get(component));
                }
            }

        }
    }

    public JPanel getMainPanel()
    {
        return messagePanel;
    }

    public JButton getButton()
    {
        return button;
    }

}
