package Decorators;

/**
 * Konkretna implementacija sučelja {@link ReturnMessage}.
 * Ova klasa predstavlja osnovni objekt (jezgru) poruke.
 */
public class BasicReturnMessage implements ReturnMessage {

    /** Sadržaj osnovne poruke (npr. "Greška pri unosu:" ili "Knjiga vraćena:"). */
    private String message;

    /**
     * Konstruktor koji postavlja početni tekst poruke.
     * @param msg Početni niz znakova koji će biti osnova za daljnje dekoriranje.
     */
    public BasicReturnMessage(String msg) {
        this.message = msg;
    }

    /** {@inheritDoc}*/
    @Override
    public String getMessage() {
        return message;
    }
}