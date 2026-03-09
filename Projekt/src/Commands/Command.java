package Commands;

/**
 * Osnovno sučelje koje definira ugovor za sve naredbe u sustavu.
 * Omogućuje enkapsulaciju zahtjeva kao objekta, što olakšava
 * implementaciju povijesti radnji (Undo/Redo).
 */
public interface Command {

    /** Provjerava jesu li ispunjeni svi uvjeti za izvršavanje
     *  @return true ako je provjera uspjela, inače false.
     */
    boolean canExecute();

    /** Izvršava glavnu poslovnu logiku naredbe
     * @return true ako je operacija uspjela, inače false.
     */
    boolean execute();

    /** Poništava efekte izvršene naredbe i vraća sustav u prethodno stanje
     * @return true ako je operacija uspjela, inače false.
     */
    boolean undo();

    /** Ponovno izvršava prethodno poništenu radnju
     * @return true ako je operacija uspjela, inače false.
     */
    boolean redo();
}
