package LibrarianManagementComps;

import BackEnd.*;
import GUI_Comps.ShortcuttableDialog;
import Commands.*;
import ObserversAndOtherComps.*;
import RegisterLibrarianComps.*;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;

/**
 * Glavni dijaloški prozor za administraciju knjižničara u sustavu.
 * <p>
 * Ova klasa djeluje kao kontroler koji povezuje vizualne komponente (tablicu i gumbe)
 * s pozadinskom logikom. Ključna je točka spajanja GUI sučelja s {@link CommandManager}-om,
 * čime se omogućuje da akcije brisanja i dodavanja budu dio <i>Undo/Redo</i> povijesti.
 * </p>
 * <b>Arhitektonske značajke:</b>
 * <ul>
 * <li><b>Separation of Concerns:</b> Window ne iscrtava gumbe direktno, već koristi
 * {@link LibrarianManagementPanel} za delegaciju UI elemenata.</li>
 * <li><b>Event Handling:</b> Implementira {@link ActionCommandListener} kako bi
 * centralizirano obrađivao zahtjeve pristigle iz pod-panela.</li>
 * <li><b>Integracija s Command Patternom:</b> Metoda {@code deleteButtonProcedure()}
 * izravno instancira konkretne naredbe i šalje ih na izvršenje.</li>
 * </ul>
 */
public class LibrarianManagementWindow extends ShortcuttableDialog {

    private SearchField searchField;
    private LibrarianViewPanel librarianViewPanel;
    private LibrarianManagementPanel librarianManagementPanel;

    /**
     * Inicijalizira prozor i postavlja osnovne GUI parametre.
     * <ul>
     * <li>Postavlja modalnost na {@code true} kako bi se spriječila interakcija s glavnim prozorom tijekom rada.</li>
     * <li>Poziva {@code activateComps()} za povezivanje listenera.</li>
     * </ul>
     */
    public LibrarianManagementWindow(JFrame parent) {

        super(parent, "Upravljanje knjižničarima", true);

        setSize(400, 500);
        setResizable(false);
        setLocationRelativeTo(parent);

        initComps();
        layoutComps();
        activateComps();
        setVisible(true);
    }

    /**
     * {@inheritDoc}:
     * <p>Tražilicu, listu/tablicu i gumbe.</p>
     */
    @Override
    protected void initComps() {
        librarianViewPanel = new LibrarianViewPanel();
        searchField = new SearchField(FilePathEnum.LIBRARIANS);
        librarianManagementPanel = new LibrarianManagementPanel();
    }

    /**
     * {@inheritDoc} u {@code BorderLayout} raspored.
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());

        add(searchField, BorderLayout.NORTH);
        add(librarianViewPanel, BorderLayout.CENTER);
        add(librarianManagementPanel, BorderLayout.SOUTH);
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Vrši mapiranje tekstualnih naredbi (ADD, DELETE, BACK) na konkretne metode prozora.</li>
     * <li>Osigurava tipsku sigurnost korištenjem {@code ActionCommandsEnum}.</li>
     * </ul>
     */
    @Override
    protected void activateComps() {

        librarianManagementPanel.setListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                // Otvaranje prozora za novog knjižničara
                if (actionCommand == ActionCommandsEnum.ADD) {
                    new RegisterLibrarianWindow(LibrarianManagementWindow.this);
                }
                // Pokretanje procedure brisanja
                if (actionCommand == ActionCommandsEnum.DELETE) {
                    Librarian selected = librarianViewPanel.getSelectedValue();
                    CommandManager.getInstance().executeCommand(new DeleteLibrarianCommand(selected));
                }
                // Zatvaranje trenutnog prozora
                if (actionCommand == ActionCommandsEnum.BACK) {
                    animateClose();
                }
            }
        });
    }
}
