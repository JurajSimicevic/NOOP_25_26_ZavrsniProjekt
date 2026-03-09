package AddBookComps;

import GUI_Comps.ActiveBasePanel;
import BackEnd.Knjiga;
import Commands.*;
import ObserversAndOtherComps.*;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;



/**
 * Desni panel {@link AddBookWindow} koji služi za interakciju i pregled knjiga.
 * <p>
 * Ovaj panel djeluje kao most između korisničkog unosa i baze podataka,
 * omogućujući trenutačni uvid u promjene nakon izvršavanja naredbi.
 * </p>
 * <b>Ključne komponente i odgovornosti:</b>
 * <ul>
 * <li><b>Prikaz podataka:</b> Sadrži {@link KnjigeViewPanel} za vizualizaciju knjiga u sustavu.</li>
 * <li><b>Kontrole:</b> Upravlja gumbima za unos nove knjige, brisanje postojeće i navigaciju (Natrag).</li>
 * <li><b>Komunikacija:</b> Koristi {@link ActionCommandListener} za prosljeđivanje događaja nadređenom prozoru.</li>
 * </ul>
 */
public class AddBookRightPanel extends ActiveBasePanel {

    /** Panel koji prikazuje listu svih knjiga u sustavu. */
    private KnjigeViewPanel knjigeViewPanel;

    /** Gumb za pokretanje akcije unosa nove knjige. */
    private JButton unesiButton;

    /** Gumb za brisanje odabrane knjige iz liste. */
    private JButton izbrisiButton;

    /** Gumb za zatvaranje prozora i povratak na prethodni ekran. */
    private JButton natragButton;

    /** Slušač događaja koji prosljeđuje naredbe (ADD, DELETE, BACK) AddBookWindow prozoru. */
    private ActionCommandListener listener;

    /**
     * Konstruktor klase AddBookRightPanel.
     * <p>Inicijalizira grafičke komponente, postavlja njihov raspored i aktivira slušače.</p>
     */
    public AddBookRightPanel() {}

    /**
     * {@inheritDoc}
     * <p>Stvara instancu panela za prikaz knjiga i kontrolne gumbe s odgovarajućim natpisima.</p>
     */
    @Override
    protected void initComps() {
        knjigeViewPanel = new KnjigeViewPanel();
        unesiButton = new JButton("Unesi novu knjigu");
        izbrisiButton = new JButton("Izbriši knjigu");
        natragButton = new JButton("Natrag");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Koristi {@link BorderLayout} gdje je prikaz knjiga u sredini (CENTER),
     * a gumbi su grupirani u donjem dijelu (SOUTH) unutar FlowLayouta.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());

        // Gornji dio s okvirom i prikazom liste
        JPanel previewPlaceholder = new JPanel(new BorderLayout());
        previewPlaceholder.add(knjigeViewPanel, BorderLayout.CENTER);
        previewPlaceholder.setBorder(BorderFactory.createTitledBorder("Pregled"));
        add(previewPlaceholder, BorderLayout.CENTER);

        // Donji dio s gumbima poravnatim udesno
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(unesiButton);
        buttonPanel.add(izbrisiButton);
        buttonPanel.add(natragButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Klik na gumbe okida metodu {@code eventOccurred} na listeneru koristeći
     * konstante iz {@link ActionCommandsEnum}.
     * </p>
     */
    protected void activateComps() {
        unesiButton.addActionListener(e -> {
            if (listener != null) listener.eventOccurred(ActionCommandsEnum.ADD);
        });

        izbrisiButton.addActionListener(e -> {
            if (listener != null) listener.eventOccurred(ActionCommandsEnum.DELETE);
        });

        natragButton.addActionListener(e -> {
            if (listener != null) listener.eventOccurred(ActionCommandsEnum.BACK);
        });
    }

    /**
     * Dohvaća trenutno odabranu knjigu iz liste.
     * @return {@link Knjiga} objekt koji je korisnik selektirao, ili null ako ništa nije odabrano.
     */
    Knjiga getSelectedBook() {
        return knjigeViewPanel.getSelectedValue();
    }

    /**
     * Postavlja vanjski slušač koji će obrađivati klikove na gumbe ovog panela.
     * @param listener Implementacija ActionCommandListener sučelja.
     */
    public void setAddBookWindowListener(ActionCommandListener listener) {
        this.listener = listener;
    }
}
