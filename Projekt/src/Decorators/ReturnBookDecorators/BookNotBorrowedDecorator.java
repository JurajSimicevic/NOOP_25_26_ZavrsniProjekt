package Decorators.ReturnBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji dodaje poruku da odabrana knjiga nije posuđena.
 */
public class BookNotBorrowedDecorator extends MessageDecorator {
    public BookNotBorrowedDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n knjiga nije posuđena!";
    }
}
