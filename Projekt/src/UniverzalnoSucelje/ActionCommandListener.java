package UniverzalnoSucelje;

import Commands.ActionCommandsEnum;

import java.util.EventListener;

/**
 * Univerzalno sučelje koje služi kao komunikacijski most unutar cijelog sustava.
 * <p>
 * Implementacijom ovog sučelja, objekt postaje sposoban primati akcijske naredbe
 * od različitih UI komponenti (gumbi, izbornici, forme). Ovo je ključna komponenta
 * koja omogućuje razdvajanje logike sučelja od poslovne logike.
 * </p>
 * * <b>Dizajn i uloga:</b>
 * <ul>
 * <li><b>EventListener:</b> Proširuje standardno Java sučelje za događaje.</li>
 * <li><b>Decoupling:</b> Omogućuje panelima da šalju signale bez poznavanja implementacije primatelja.</li>
 * <li><b>Command Pattern:</b> Često radi u paru s {@code ActionCommandsEnum} za slanje tipiziranih naredbi.</li>
 * </ul>
 */
public interface ActionCommandListener extends EventListener {

    /**
     * Metoda koja se poziva kada se dogodi akcijski događaj unutar komponente.
     * <p>
     * Primatelj (primjerice {@code MainFrame}) u ovoj metodi odlučuje što učiniti
     * na temelju proslijeđenog {@code actionCommand}-a.
     * </p>
     * @param actionCommand ActionCommandsEnum identifikator naredbe (npr. "REGISTER", "DELETE", "BACK").
     */
    void eventOccurred(ActionCommandsEnum actionCommand);
}
