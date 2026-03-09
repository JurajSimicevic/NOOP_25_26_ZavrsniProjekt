package Decorators.BorrowBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji se koristi pri posudbi knjiga.
 * Obavještava korisnika da proces ne može biti završen jer nije
 * odabran član (korisnik) koji posuđuje knjigu.
 */
public class CustomerNotSelectedDecorator extends MessageDecorator {
    public CustomerNotSelectedDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Korisnik nije izabran!";
    }
}
