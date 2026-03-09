package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator zadužen za validaciju dobi korisnika.
 * Dodaje poruku o pogrešci ako su godine neispravno unesene ili nedostaju.
 */
public class InvalidAgeDecorator extends MessageDecorator {
    public InvalidAgeDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Korisnik je premlad ili je unos godina krivo napisan!";
    }
}
