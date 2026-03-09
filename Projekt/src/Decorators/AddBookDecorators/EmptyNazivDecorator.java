package Decorators.AddBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji dodaje napomenu o obaveznom unosu naslova djela.
 * Koristi se kada sustav detektira prazno polje za naziv knjige.
 */
public class EmptyNazivDecorator extends MessageDecorator {
    public EmptyNazivDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\nUnesite naslov djela!";
    }
}
