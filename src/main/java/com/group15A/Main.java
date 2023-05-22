package com.group15A;

import com.group15A.GUI.MultiPanelWindow;

import javax.swing.*;

/**
 * @author Milovan Gveric
 */
public class Main {

    /**
     * Entry into the GUI
     */
    public static void main(String[] args) {
        JFrame panelHandler = new MultiPanelWindow();
        panelHandler.setVisible(true);
    }

}
