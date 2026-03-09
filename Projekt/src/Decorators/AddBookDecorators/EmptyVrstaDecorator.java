package Decorators.AddBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji upozorava operatera da vrsta djela
 * nije odabrana iz ponuđenog izbornika.
 */
public class EmptyVrstaDecorator extends MessageDecorator {
    public EmptyVrstaDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\nVrsta djela nije odabrana!";
    }
}
