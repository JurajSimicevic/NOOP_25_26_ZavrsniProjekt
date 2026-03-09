package Decorators;

/**
 * Apstraktna klasa koja služi kao temelj za sve konkretne dekoratore poruka.
 * Implementira {@link ReturnMessage} sučelje, ali istovremeno sadrži referencu na
 * drugi objekt istog tipa.
 * * * Ključna uloga:
 * Omogućuje "omatanje" (wrapping) osnovne poruke ili drugog dekoratora,
 * stvarajući lanac objekata koji zajedno grade konačni tekst poruke.
 */
public abstract class MessageDecorator implements ReturnMessage {

    /** * Referenca na objekt koji se dekorira.
     * Koristimo 'protected' kako bi konkretni dekoratori (npr. LateFeeDecorator)
     * mogli izravno pristupiti ovom objektu.
     */
    protected ReturnMessage decoratedMessage;

    /**
     * Konstruktor koji prima objekt koji želimo proširiti.
     * @param message Objekt tipa ReturnMessage (može biti BasicReturnMessage ili drugi dekorator).
     */
    public MessageDecorator(ReturnMessage message) {
        this.decoratedMessage = message;
    }
}