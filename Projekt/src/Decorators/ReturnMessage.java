package Decorators;

/**
 * Osnovno sučelje koje definira objekt poruke.
 * Omogućuje uniforman pristup tekstu poruke bez obzira na to je li
 * poruka osnovna ili proširena dekoratorima.
 */
public interface ReturnMessage {

    /**
     * Vraća trenutni sadržaj poruke
     * @return String koji predstavlja bazu poruke.
     */
    String getMessage();
}
