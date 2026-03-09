package RegisterLibrarianComps;

import GUI_Comps.BasePanel;
import BackEnd.FilePathEnum;
import BackEnd.Librarian;
import GUI_Comps.EffectUtils;
import ObserversAndOtherComps.*;

import java.awt.*;

/**
 * Kompozitni panel koji objedinjuje komponente za pregled i pretragu knjižničara.
 * Ovaj panel služi kao centralni prikaz u administrativnom sučelju. Integrira:
 * <ul>
 * <li>{@link SearchField} - polje za filtriranje zaposlenika u realnom vremenu.</li>
 * <li>{@link LibrarianViewPanel} - grafički prikaz liste knjižničara iz baze.</li>
 * </ul>
 * <b>Mehanizam rada:</b>
 * <p>
 * Panel koristi {@link BorderLayout} kako bi pretragu fiksirao na vrh, dok lista
 * zauzima sav preostali prostor. Budući da nasljeđuje {@link BasePanel},
 * inicijalizacija se događa automatski.
 * </p>
 */
public class ConjoinedRegisterLibrarianPanel extends BasePanel {

    private ViewPanel<Librarian> librarianViewPanel;
    private SearchField librarianSearchField;

    /**
     * Konstruktor klase.
     * <p>Poziva se super-konstruktor {@link BasePanel}-a koji pokreće
     * metode za inicijalizaciju i slaganje komponenti.</p>
     */
    public ConjoinedRegisterLibrarianPanel() {}

    /**
     * {@inheritDoc}
     * <p>
     * Stvara specijalizirani panel za prikaz knjižničara i polje za pretragu
     * povezano s bazom podataka zaposlenika putem {@link FilePathEnum#LIBRARIANS}.
     * </p>
     */
    @Override
    protected void initComps() {
        librarianViewPanel = new LibrarianViewPanel();
        librarianSearchField = new SearchField(FilePathEnum.LIBRARIANS, 30);
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li><b>North:</b> Polje za pretragu (SearchField).</li>
     * <li><b>Center:</b> Lista knjižničara (ViewPanel) unutar scroll okna.</li>
     * </ul>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        add(librarianSearchField, BorderLayout.NORTH);
        add(librarianViewPanel, BorderLayout.CENTER);
    }

}
