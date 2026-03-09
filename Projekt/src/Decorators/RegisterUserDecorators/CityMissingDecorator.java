package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji upozorava operatera da polje za grad stanovanja
 * nije popunjeno tijekom procesa registracije.
 */
public class CityMissingDecorator extends MessageDecorator {
    public CityMissingDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\nUnos grada je prazan!";
    }
}
