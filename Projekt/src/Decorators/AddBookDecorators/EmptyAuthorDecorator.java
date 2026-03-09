package Decorators.AddBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji osnovnoj poruci dodaje informaciju o nedostatku autora.
 * Koristi se prilikom validacije forme za unos nove knjige.
 */
public class EmptyAuthorDecorator extends MessageDecorator {
    public EmptyAuthorDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\nAutor polje je prazno!";
    }
}
