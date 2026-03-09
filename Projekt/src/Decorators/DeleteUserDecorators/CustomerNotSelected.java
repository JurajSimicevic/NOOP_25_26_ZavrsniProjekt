package Decorators.DeleteUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/** Dekorator koji obaviještava da kupac nije izabran.*/
public class CustomerNotSelected extends MessageDecorator {
    public CustomerNotSelected(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n korisnik nije izabran!";
    }
}
