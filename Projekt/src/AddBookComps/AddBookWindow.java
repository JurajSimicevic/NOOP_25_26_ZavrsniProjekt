package AddBookComps;

import BackEnd.*;
import GUI_Comps.BaseFrame;
import GUI_Comps.ShortcuttableFrame;
import Commands.*;

import MainFrameComps.*;
import UniverzalnoSucelje.ActionCommandListener;

import java.awt.*;


/**
 * Glavni okvir (Frame) zadužen za upravljanje procesom dodavanja i brisanja knjiga.
 * <p>
 * Nasljeđuje {@link BaseFrame} i služi kao centralni koordinator koji povezuje
 * korisnički unos s prikazom podataka i izvršnom logikom sustava.
 * </p>
 * <b>Arhitektonska uloga i komponente:</b>
 * <ul>
 * <li><b>Lijeva strana:</b> {@link AddBookFormPanel} - služi za primarni unos podataka o knjizi.</li>
 * <li><b>Desna strana:</b> {@link AddBookRightPanel} - služi za pregled kataloga i upravljanje akcijama.</li>
 * <li><b>Logika izvršavanja:</b> Prikuplja podatke iz sučelja te delegira zadatke {@link CommandManager}-u.</li>
 * </ul>
 * <b>Podržane operacije (Command Pattern):</b>
 * <ul>
 * <li><b>Dodavanje:</b> Kreira i izvršava {@code AddBookCommand} s podacima iz forme.</li>
 * <li><b>Brisanje:</b> Dohvaća selektiranu knjigu iz prikaza i pokreće {@code DeleteBookCommand}.</li>
 * </ul>
 */
public class AddBookWindow extends ShortcuttableFrame {

    /** Panel koji sadrži tekstualna polja za unos podataka o novoj knjizi. */
    private AddBookFormPanel addBookFormPanel;
    /** Panel koji prikazuje listu knjiga i sadrži gumbe za akcije (Dodaj, Obriši, Natrag). */
    private AddBookRightPanel addBookRightPanel;

    /**
     * Konstruktor za {@link AddBookWindow}.
     * <p>
     * Postavlja osnovna svojstva prozora (naslov, veličinu, centriranje) i
     * inicijalizira sve grafičke podkomponente.
     * </p>
     */
    public AddBookWindow() {
        super("Dodaj/Izbriši knjigu");
        setSize(1018, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>{@link AddBookFormPanel}</li>
     * <li>{@link AddBookRightPanel}.</li>
     * </ul>
     */
    @Override
    protected void initComps() {
        addBookFormPanel = new AddBookFormPanel();
        addBookRightPanel = new AddBookRightPanel();

    }

    /**
     * Postavlja raspored komponenti unutar prozora.
     * <p>
     * Koristi {@link BorderLayout} gdje je forma na lijevoj strani (WEST),
     * a pregled i kontrolni gumbi u sredini/desno (CENTER).
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        add(addBookFormPanel, BorderLayout.WEST);
        add(addBookRightPanel, BorderLayout.CENTER);
    }

    /**
     * Aktivira slušače događaja koji dolaze iz {@link AddBookRightPanel}.
     * Na temelju primljene naredbe (ADD, DELETE, BACK), prozor odlučuje
     * hoće li:
     * <ul>
     *  <li>pokrenuti unos nove knjige</li>
     *  <li>brisati selektiranu</li>
     *  <li>vratiti se na glavni prozor</li>
     * </ul>
     */
    @Override
    protected void activateComps(){
        addBookRightPanel.setAddBookWindowListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                // Akcija za dodavanje nove knjige
                if (actionCommand == ActionCommandsEnum.ADD) {
                    izvrsiUnos();

                }
                // Akcija za brisanje trenutno odabrane knjige iz liste
                if (actionCommand == ActionCommandsEnum.DELETE) {
                    Knjiga k = addBookRightPanel.getSelectedBook();
                    CommandManager.getInstance().executeCommand(new DeleteBookCommand(k));
                }
                // Zatvaranje ovog prozora i povratak na MainFrame
                if (actionCommand == ActionCommandsEnum.BACK) {
                    new MainFrame();
                    disposeFadeOut();
                }
            }
        });
    }

    /**
     * Prikuplja sve podatke unesene u {@link AddBookFormPanel} (autor, naziv, ISBN, cijena, vrsta)
     * i prosljeđuje ih {@link CommandManager}-u putem {@link AddBookCommand} naredbe.
     */
    private void izvrsiUnos() {
        String autor = addBookFormPanel.getAutor();
        String naziv = addBookFormPanel.getNaziv();
        String isbn = addBookFormPanel.getIsbn();
        double cijena = addBookFormPanel.getCijena();
        String vrsta = addBookFormPanel.getVrsta();

        CommandManager.getInstance().executeCommand(new AddBookCommand(autor, naziv, isbn, vrsta, cijena));
    }
}
