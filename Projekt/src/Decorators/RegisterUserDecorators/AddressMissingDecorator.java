package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji dodaje informaciju o nedostatku adrese stanovanja
 * na osnovnu poruku o neispravnom unosu.
 */
public class AddressMissingDecorator extends MessageDecorator {
    public AddressMissingDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Nedostaje adresa!";
    }
}
