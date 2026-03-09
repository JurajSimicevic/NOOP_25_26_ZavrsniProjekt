package Decorators.ReturnBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji proširuje poruku o povratu informacijom o naplati
 * štete na knjizi (npr. $10) ako je knjižničar zabilježio oštećenje.
 */
public class LateFeeDecorator extends MessageDecorator {
    public LateFeeDecorator(ReturnMessage message) { super(message); }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n(Naknada od $5 zbog kašnjenja)";
    }
}
