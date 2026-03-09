package Decorators.DeleteBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji se koristi kada korisnik pokuša izvršiti akciju (brisanje,
 * kupnja ili posudba) bez da je prethodno označio knjigu u tablici/listi.
 */
public class BookNotSelectedDecorator extends MessageDecorator {
    public BookNotSelectedDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Knjiga nije odabrana!";
    }
}
