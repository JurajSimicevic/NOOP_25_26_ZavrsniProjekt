package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji dodaje napomenu o obaveznom unosa prezimena u
 * sustavsku poruku o pogrešci.
 */
public class LastnameMissingDecorator extends MessageDecorator {
    public LastnameMissingDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Nedostaje prezime!";
    }
}
