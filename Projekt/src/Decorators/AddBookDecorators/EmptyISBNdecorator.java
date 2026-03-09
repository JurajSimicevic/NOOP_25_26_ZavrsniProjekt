package Decorators.AddBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji proširuje poruku upozorenjem o praznom polju za ISBN.
 * Osigurava da korisnik unese jedinstveni identifikator knjige.
 */
public class EmptyISBNdecorator extends MessageDecorator {
    public EmptyISBNdecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\nISBN je prazan!";
    }
}
