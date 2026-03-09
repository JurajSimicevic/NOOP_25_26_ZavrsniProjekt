package Commands;

/**
 * Apstraktna bazna klasa za sve naredbe koje se oslanjaju na "Dekorator" za slaganje povratne poruke.
 * <p>
 * Služi kao proširenje osnovnog komandnog uzorka, uvodeći standardizirani mehanizam
 * za pravljenje poruke u slučaju grešaka. Osigurava da svaka izvedena naredba
 * (npr. {@link AddBookCommand}) ima definiranu logiku za generiranje povratnih poruka.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Smanjenje redundancije:</b> Centralizira zajednička obilježja naredbi koje
 * komuniciraju s korisnikom putem dijaloga o pogreškama.</li>
 * </ul>
 * <b>Tijek izvršavanja unutar hijerarhije:</b>
 * <li>Poziv {@code canExecute()}: Konkretna naredba koristi {@code createMessage()} za pravljenje poruke o grešci.</li>
 */
public abstract class BaseCommand {

    /**
     * Metoda zadužena za konstrukciju poruka o pogreškama.
     * <p>
     * Svaka konkretna naredba ovdje treba implementirati vlastitu logiku (npr. korištenjem
     * Decorator uzorka) za izgradnju poruke.
     * </p>
     */
    protected abstract void createMessage();
}
