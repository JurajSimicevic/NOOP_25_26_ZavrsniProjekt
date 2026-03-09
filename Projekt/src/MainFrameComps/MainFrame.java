package MainFrameComps;

import AddBookComps.*;
import BackEnd.*;
import GUI_Comps.ShortcuttableFrame;
import BuyBookComps.BuyBookWindow;
import Commands.*;
import LibrarianManagementComps.LibrarianManagementWindow;
import LoginComps.LogInWindow;
import ObserversAndOtherComps.*;
import RegisterCustomerComps.*;
import ReturnBookComps.*;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Glavni upravljački prozor aplikacije (Main View).
 * <p>
 * Služi kao centralni kontejner i kontroler koji koordinira rad svih ostalih komponenti
 * poput {@link ToolBar}-a, {@link MainFrameFormPanel}-a i {@link MenuBar}-a.
 * </p>
 * <b>Arhitektonske značajke:</b>
 * <ul>
 * <li><b>Centralni Listener:</b> Implementira {@link ActionCommandListener} kako bi na
 * jednom mjestu hvatao signale iz svih pod-komponenti i pretvarao ih u konkretne akcije.</li>
 * <li><b>Upravljanje naredbama:</b> Izravno komunicira s {@link CommandManager}-om za
 * operacije posudbe, čime se osigurava njihova prisutnost u Undo/Redo povijesti.</li>
 * <li><b>Logout logika:</b> Sigurno zatvara radnu sesiju i vraća korisnika na {@link LogInWindow}.</li>
 * </ul>
 */
public class MainFrame extends ShortcuttableFrame {

    private ToolBar toolBar;
    private MainFrameFormPanel formPanel;
    private KnjigeViewPanel knjigeViewPanel;
    private MenuBar menuBar;
    private ImageIcon imageIcon;


    /**
     * Konstruktor koji postavlja vizualni identitet i inicijalizira layout.
     * <ul>
     * <li>Postavlja fiksnu veličinu (1018x650) radi konzistentnosti prikaza elemenata.</li>
     * <li>Centrira prozor i definira zatvaranje aplikacije na 'X'.</li>
     * </ul>
     */
    public MainFrame() {
        super("Library Management Software 9000");

        setSize(1018, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initComps() {
        toolBar = new ToolBar();
        formPanel = new MainFrameFormPanel();
        knjigeViewPanel = new KnjigeViewPanel();
        menuBar = new MenuBar();

        // Logo knjižnice za gornji dio ekrana
        imageIcon = new ImageIcon("./data/logo.jpg");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        add(new JLabel(imageIcon), BorderLayout.NORTH);
        add(knjigeViewPanel, BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);
        add(toolBar, BorderLayout.SOUTH);
        setJMenuBar(menuBar);

    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Glavna petlja za obradu događaja. Prepoznaje komande poput "BORROW_BOOK" ili "LOGOUT".</li>
     * <li>Pri posudbi, prikuplja podatke iz {@code KnjigeViewPanel} i {@code FormPanel}
     * kako bi stvorio {@link BorrowBookCommand}.</li>
     * </ul>
     */
    @Override
    protected void activateComps() {

        toolBar.setToolBarListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                if (actionCommand == ActionCommandsEnum.OPEN_ADD_WINDOW) {
                    new AddBookWindow();
                    dispose();
                }
                if (actionCommand == ActionCommandsEnum.OPEN_RETURN_WINDOW) {
                    new ReturnBookWindow();
                    dispose();
                }
                if (actionCommand == ActionCommandsEnum.OPEN_REGISTER_WINDOW) {
                    new RegisterCustomerWindow();
                    dispose();
                }
                if(actionCommand == ActionCommandsEnum.OPEN_MANAGE_LIBRARIANS_WINDOW) {
                    // Modalni prozor: MainFrame ostaje u pozadini
                    new LibrarianManagementWindow(MainFrame.this);
                }
            }
        });

        formPanel.setFormPanelListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                if (actionCommand == ActionCommandsEnum.BORROW_BOOK) {

                    Knjiga selectedBook = knjigeViewPanel.getSelectedValue();
                    Customer selectedCustomer = formPanel.getCustomer();
                    CommandManager.getInstance().executeCommand(new BorrowBookCommand(selectedBook, selectedCustomer, formPanel.getBrojDana()));
                }

                if(actionCommand == ActionCommandsEnum.BUY_BOOK) {
                    new BuyBookWindow(MainFrame.this);
                }
            }
        });

        menuBar.setMenuListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum command) {
                if(command == ActionCommandsEnum.LOGOUT) {
                    handleLogout();
                }

                if(command == ActionCommandsEnum.EXIT){
                    disposeFadeOut();
                }
            }
        });
    }

    /**
     * Sigurnosna procedura za odjavu korisnika.
     * <ul>
     * <li>Prikazuje potvrdu (YES/NO) prije zatvaranja.</li>
     * <li>Poziva {@code SessionManager.logout()} kako bi očistio podatke o trenutnom knjižničaru.</li>
     * </ul>
     */
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Jeste li sigurni da se želite odjaviti?",
                "Potvrda odjave",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // 1. Poništi ulogiranog korisnika u manageru
            SessionManager.getInstance().logout();

            // 2. Zatvori MainFrame
            disposeFadeOut();

            // 3. Otvori ponovno Login prozor
            new LogInWindow();
        }
    }
}

