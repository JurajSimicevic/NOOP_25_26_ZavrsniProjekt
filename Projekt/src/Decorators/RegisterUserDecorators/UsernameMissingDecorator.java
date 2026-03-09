package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji dodaje upozorenje o nedostatku korisničkog imena.
 * Koristi se pri registraciji knjižničara kako bi se osigurao unos
 * podataka potrebnih za autentifikaciju.
 */
public class UsernameMissingDecorator extends MessageDecorator {
    public UsernameMissingDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Nedostaje username!";
    }
}
