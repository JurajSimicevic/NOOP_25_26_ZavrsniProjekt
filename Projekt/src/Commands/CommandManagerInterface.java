package Commands;

/**
 * Sučelje koje definira upravljački sloj za rad s naredbama (Command pattern).
 * Pruža metode za izvršavanje novih radnji te upravljanje poviješću operacija
 * kroz funkcionalnosti poništavanja (Undo) i ponovnog izvršavanja (Redo).
 * * Svrha ovog sučelja je odvojiti (decoupling) pošiljatelja naredbe (GUI gumb)
 * od njezine izvedbe, omogućujući praćenje stanja aplikacije.
 */
public interface CommandManagerInterface {

    /**
     * Prima novu naredbu, izvršava je i pohranjuje u stog (stack) povijesti.
     * @param command Objekt koji implementira {@link Command} sučelje (npr. AddBookCommand).
     * @return {@code true} ako je naredba uspješno izvršena.
     */
    boolean executeCommand(Command command);

    /**
     * Poništava zadnju izvršenu operaciju uzimajući je s vrha 'undo' stoga.
     * Nakon poništavanja, naredba se seli u 'redo' stog.
     * @return {@code true} ako postoji radnja za poništavanje i ako je uspješno izvedena.
     */
    boolean undo();

    /**
     * Ponovno izvršava radnju koja je prethodno bila poništena (Undo).
     * Uzima naredbu s vrha 'redo' stoga i ponovno je izvršava.
     * @return {@code true} ako postoji radnja za ponavljanje.
     */
    boolean redo();

    /**
     * Koristi se radi sigurnosti. Poništava undo i redo stogove pri odjavljivanju korisnika:
     * <p>
     * Ne želimo da odjavljeni korisnik može izvršavati undo i redo prethodno izvedenih komandi.
     * </p>
     */
    void clearSessions();
}