package com.group15A.GUI;

import com.group15A.Utils.ReceivePair;
import javax.swing.*;

/**
 * All panels in the card layout inherit from this, allows using JPanels
 * in card layout that are written in different files
 *
 * windowTitle is the title of the login panel, usually the same.
 *
 * panelController is the instance of multiPanelWindow in order for
 * events from this panel to call showPage
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public abstract class BasePanel {
    protected MultiPanelWindow panelController;
    protected String windowTitle;
    private final String panelFieldName;


    /**
     * Constructor for BasePanel class
     *
     * @param windowTitle the title of the window
     * @param panelFieldName the string used to find the panel in the card layout
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from subclass panels to call showPage
     */
    public BasePanel(String windowTitle, String panelFieldName, MultiPanelWindow panelController)
    {
        this.panelController = panelController;
        this.panelFieldName = panelFieldName;
        this.windowTitle = windowTitle;
    }

    public String getPanelFieldName()
    {
        return this.panelFieldName;
    }

    public String getWindowTitle()
    {
        return this.windowTitle;
    }

    public abstract JPanel getPagePanel();

    public abstract void receiveData(ReceivePair pair);

    public abstract void createActionListeners();
}
