package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji signalizira da polje za osobno ime korisnika ili
 * knjižničara ne smije ostati prazno.
 */
public class NameMissingDecorator extends MessageDecorator {
    public NameMissingDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Nedostaje ime!";
    }
}
