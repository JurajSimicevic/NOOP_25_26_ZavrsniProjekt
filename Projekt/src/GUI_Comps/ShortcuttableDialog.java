package GUI_Comps;

import Commands.CommandManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Proširena verzija dijaloga koja implementira podršku za sustav naredbi (Command Pattern).
 * <p>
 * Namijenjena je dijalozima unutar kojih korisnik vrši promjene podataka (npr. registracija, brisanje),
 * omogućujući mu korištenje globalnih prečaca za poništavanje radnji.
 * </p>
 * <b>Implementirani prečaci:</b>
 * <ul>
 * <li>{@code Ctrl + Z}: Poziva {@code undo()} metodu u {@link CommandManager}-u.</li>
 * <li>{@code Ctrl + Y}: Poziva {@code redo()} metodu u {@link CommandManager}-u.</li>
 * </ul>
 */
public abstract class ShortcuttableDialog extends BaseDialog{

    /**
     * Konstruktor za inicijalizaciju dijaloga čiji je vlasnik glavni prozor ({@link JFrame}).
     * <p>
     * Automatski poziva {@code setupGlobalShortcuts()} nakon što se izvrši
     * inicijalizacija baze putem super-poziva.
     * </p>
     * @param owner Glavni prozor (obično {@code MainFrame}) koji posjeduje ovaj dijalog.
     * @param title Naslov koji će biti prikazan na vrhu dijaloga.
     * @param modal Ako je {@code true}, zaustavlja interakciju s ostatkom aplikacije dok se dijalog ne zatvori.
     */
    public ShortcuttableDialog(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setupGlobalShortcuts();
    }

    /**
     * Konstruktor za inicijalizaciju dijaloga čiji je vlasnik drugi dijalog ({@link JDialog}).
     * <p>
     * Koristi se u slučajevima ugniježđenih prozora (npr. prozor za potvrdu unutar prozora za upravljanje).
     * </p>
     * @param owner Roditeljski dijalog koji je stvorio ovaj objekt.
     * @param title Naslov dijaloga.
     * @param modal Postavlja razinu modalnosti dijaloga.
     */
    public ShortcuttableDialog(JDialog owner, String title, boolean modal) {
        super(owner, title, modal);
        setupGlobalShortcuts();
    }

    /**
     * Konfigurira Key Bindings na razini {@code RootPane}-a.
     * <p>
     * Za razliku od KeyListenera, ovaj sustav koristi {@link InputMap} i {@link ActionMap},
     * što jamči da će prečaci raditi čak i ako fokus nije na samom dijalogu,
     * već na nekoj od njegovih unutrašnjih komponenti.
     * </p>
     */
    private void setupGlobalShortcuts() {
        JComponent root = getRootPane();

        // --- KONFIGURACIJA ZA UNDO (Ctrl + Z) ---

        // Povezujemo fizičku kombinaciju tipki (KeyStroke) s logičkim imenom akcije "undoAction"
        // WHEN_IN_FOCUSED_WINDOW osigurava da prečac radi bilo gdje unutar ovog dijaloga
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undoAction");

        // Definiramo što se zapravo događa kada se aktivira "undoAction"
        root.getActionMap().put("undoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandManager.getInstance().undo();
            }
        });

        // --- KONFIGURACIJA ZA REDO (Ctrl + Y) ---

        // Povezujemo Ctrl+Y s logičkim imenom "redoAction"
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redoAction");

        // Definiramo izvršavanje Redo operacije
        root.getActionMap().put("redoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandManager.getInstance().redo();
            }
        });
    }
}
