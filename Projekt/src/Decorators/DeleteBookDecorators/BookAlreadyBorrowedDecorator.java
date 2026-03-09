package Decorators.DeleteBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji signalizira nemogućnost brisanja ili ponovne posudbe knjige.
 * Dodaje informaciju da je odabrana knjiga trenutno u statusu "posuđena".
 */
public class BookAlreadyBorrowedDecorator extends MessageDecorator {
    public BookAlreadyBorrowedDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Odabrana knjiga je već posuđena!";
    }
}
