package com.group15A.GUI;

import com.group15A.BusinessLogic.HomeLogic;
import com.group15A.BusinessLogic.MultiPanelWindowLogic;
import com.group15A.CustomExceptions.*;
import com.group15A.DataAccess.DataAccess;
import com.group15A.DataModel.Patient;
import com.group15A.Session;
import com.group15A.Utils.PageType;
import com.group15A.Utils.ReceivePair;
import com.group15A.Utils.ReceiveType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The window which will be shown, consists of a card layout
 * which can switch between different JPanels (pages)
 *
 * cardLayout is needed to add pages to be switched between
 *
 * cards is the list of BasePanels to be stored
 *
 * panelCards is the parent that holds all JPanels, of which
 * its layout is cardLayout
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class MultiPanelWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelCards;
    private Map<PageType, BasePanel> cards;
    private Session session;
    private MultiPanelWindowLogic multiPanelWindowLogic;
    private Patient patient;

    /**
     * Constructor for the MultiPanelWindow class
     *
     * Stores all pages,
     * sets default window size,
     * and goes to a certain page if the session file is still stored
     */
    public MultiPanelWindow() {
        // Create session
        this.session = new Session(null, false);

        // Set session (if file exists) and creates pages
        refreshSession();

        try {
            multiPanelWindowLogic = new MultiPanelWindowLogic();
            patient = multiPanelWindowLogic.getPatient(session.getLoggedInPatientID());
        } catch (CustomException e) {
            System.out.println("No session file found.");
        } catch(NullPointerException e){
            System.out.println("Patient not found.");
        }

        // Set response to window being closed
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeProgram();
            }
        });

        this.setContentPane(panelCards);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(
                (int)(dimension.getWidth()*0.8), // Make window width 80% that of the screen
                (int)(dimension.getHeight()*0.8) // Make window height 80% that of the screen
        );

        // Choose the page to be displayed when starting the program
        PageType pageToShow = PageType.LOGIN; // log in page
        if(getSession() != null && getSession().isKeepLoggedIn()) {
            try {
                Session savedSession = Session.loadFromFile();
                if (savedSession != null && savedSession.isKeepLoggedIn()) {
                    this.setSession(savedSession);
                    this.multiPanelWindowLogic.createLog(patient, "Patient " + patient.getFirstName() + " " + patient.getLastName() + " automatically logged in, successfully");
                    pageToShow = PageType.HOME; // home page
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        showPage(pageToShow);
    }


    /**
     * Creates a hashmap linking the types of pages to the actual
     * BasePanel instances
     */
    private void createPages() {
        this.cards = new HashMap<>();
        this.cards.put(PageType.LOGIN, new LogInPanel(this));
        this.cards.put(PageType.REGISTER, new RegisterPanel(this));
        this.cards.put(PageType.HOME, new HomePanel(this));
        this.cards.put(PageType.CHOOSE_DOCTOR, new ChooseDoctorPanel(this));
        this.cards.put(PageType.VIEW_BOOKINGS, new ViewBookingsPanel(this));
        this.cards.put(PageType.ADD_BOOKING, new AddBookingPanel(this));
        this.cards.put(PageType.VIEW_PROFILE, new ViewProfilePanel(this));
        this.cards.put(PageType.LOG, new LogPanel(this));

        PageType[] pages = PageType.values();

        this.cardLayout = (CardLayout) (panelCards.getLayout());
        for (PageType page: pages) {
            BasePanel basePanel = this.cards.get(page);
            basePanel.getPagePanel().setBorder(new EmptyBorder(20,20,20,20));
           this.panelCards.add(basePanel.getPagePanel(), basePanel.getPanelFieldName());
        }
    }

    /**
     * Switches to a given JPanel that is in the card layout
     *
     * @param page the page to switch to, contains window title and the required JPanel
     */
    public void showPage(PageType page, ReceivePair... pairs) {
        BasePanel nextPanel = this.cards.get(page);
        this.setTitle(nextPanel.getWindowTitle());
        this.cardLayout.show(panelCards, nextPanel.getPanelFieldName());
        // To trigger any passive events in pages, such as dynamically updating notifications on the home panel
        nextPanel.receiveData(new ReceivePair(ReceiveType.EVENT, null));
        for (ReceivePair pair: pairs) {
            nextPanel.receiveData(pair);
        }
    }

    /**
     * @return The current session
     */
    public Session getSession(){
        return session;
    }

    /**
     * Set session based on content of session file
     */
    public void refreshSession()
    {
        try{
            Session fromFile = Session.loadFromFile();
            if (fromFile != null) {
                setSession(fromFile);
            }
        } catch (Exception e) {
            System.out.println("No session file found. Going to log-in page.");
        }
        createPages();
    }

    /**
     * A method for other classes to call createPages()
     */
    public void refreshPages()
    {
        createPages();
    }

    /**
     * Sets session and refreshes pages.
     *
     * @param session new session (Patient object and stay-logged-in status)
     */
    public void setSession(Session session)
    {
        this.session = session;
        createPages();
    }

    /**
     * When the close window button is clicked,
     * the user will be logged out (if they don't want to stay logged in),
     * and the program will terminate.
     */
    public void closeProgram()
    {
        // Delete session file if user doesn't want to stay logged in (i.e. log out user)
        if(session != null && !session.isKeepLoggedIn()) {
            try {
                multiPanelWindowLogic.logOut();
                File sessionFile = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/LoggedUser.bin");
                sessionFile.delete();
                multiPanelWindowLogic.createLog(patient,"Patient " + patient.getFirstName() + " " + patient.getLastName() + "closed the program and logged out");
            }
            catch (Exception e){
                System.out.println("No session found to delete");
            }
        }
        else{
            try {
                if(patient != null) {
                    multiPanelWindowLogic.createLog(patient, "Patient " + patient.getFirstName() + " " + patient.getLastName() + " closed the program and stayed logged in");
                }
            } catch (CustomException e) {
                e.printStackTrace();
            }
        }

        // Exit program
        System.exit(0);
    }

}
