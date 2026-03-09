package Decorators.DeleteUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/** Dekorator koji obaviještava da je knjižničar ulogiran.*/
public class LibrarianLoggedInDecorator extends MessageDecorator {
    public LibrarianLoggedInDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n knjižničar je trenutno ulogiran!";
    }
}
