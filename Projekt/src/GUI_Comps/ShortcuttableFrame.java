package GUI_Comps;

import Commands.CommandManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Specijalizirana osnovna klasa za prozore koji podržavaju sustav naredbi (Command Pattern).
 * <p>
 * Nasljeđuje {@link BaseFrame} i proširuje ga automatiziranom podrškom za globalne
 * tipkovničke prečace. Idealna je za prozore u kojima se vrše operacije nad podacima
 * (poput {@code MainFrame}) jer korisniku omogućuje brz Undo/Redo pristup.
 * </p>
 * <b>Funkcionalnosti:</b>
 * <ul>
 * <li><b>Ctrl + Z:</b> Automatski poziva {@code undo()} metodu u {@link CommandManager}-u.</li>
 * <li><b>Ctrl + Y:</b> Automatski poziva {@code redo()} metodu u {@link CommandManager}-u.</li>
 * </ul>
 * <b>Arhitektonska prednost:</b>
 * <ul>
 * <li>Logika prečaca je centralizirana; promjena kombinacije tipki na jednom mjestu
 * ažurira sve naslijeđene prozore.</li>
 * </ul>
 */
public abstract class ShortcuttableFrame extends BaseFrame {

    public ShortcuttableFrame(String title) {
        super(title);
        setupGlobalShortcuts();
    }

    /**
     * Konfiguracija i implementacija <b>Key Bindings</b> mehanizma.
     * <p>
     * Koristi {@link JRootPane} sustav kako bi osigurao da prečaci budu aktivni
     * na razini cijelog prozora, neovisno o tome koja podkomponenta trenutno drži fokus.
     * </p>
     * <b>Tehnički detalji implementacije:</b>
     * <ul>
     * <li><b>InputMap:</b> Povezuje fizičke tipke ({@link KeyStroke}) s apstraktnim nazivima akcija.</li>
     * <li><b>ActionMap:</b> Povezuje naziv akcije s konkretnom logikom izvršavanja (Undo/Redo).</li>
     * <li><b>Fokusna razina:</b> Postavljeno na {@code WHEN_IN_FOCUSED_WINDOW} za maksimalnu responzivnost.</li>
     * </ul>
     * <b>Prednosti nad KeyListenerom:</b>
     * <ul>
     * <li>Radi ispravno čak i kada korisnik upisuje tekst u polja ili navigira tablicom.</li>
     * <li>Izbjegava probleme s "gutanjem" događaja od strane dječjih komponenti.</li>
     * <li>Omogućuje centralizirano upravljanje prečacima na jednom mjestu.</li>
     * </ul>
     */
    private void setupGlobalShortcuts() {
        JComponent root = getRootPane();

        // --- Postavljanje UNDO naredbe (Ctrl + Z) ---

        // Povezivanje kombinacije tipki s logičkim ključem "undoAction"
        // WHEN_IN_FOCUSED_WINDOW osigurava detekciju unutar bilo kojeg dijela prozora
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undoAction");

        // Definiranje akcije koja se izvršava pozivom ključa "undoAction"
        root.getActionMap().put("undoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandManager.getInstance().undo();
            }
        });

        // --- Postavljanje REDO naredbe (Ctrl + Y) ---

        // Povezivanje Ctrl + Y s logičkim ključem "redoAction"
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redoAction");

        // Izvršavanje Redo operacije kroz CommandManager
        root.getActionMap().put("redoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandManager.getInstance().redo();
            }
        });
    }
}
