package Decorators.AddBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator zadužen za signalizaciju pogrešno upisane/ne upisane cijene knjige.
 * Nadograđuje poruku s informacijom da format ili vrijednost cijene nije ispravna.
 */
public class EmptyCijenaDecorator extends MessageDecorator {
    public EmptyCijenaDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\nCijena je krivo upisana!";
    }
}
