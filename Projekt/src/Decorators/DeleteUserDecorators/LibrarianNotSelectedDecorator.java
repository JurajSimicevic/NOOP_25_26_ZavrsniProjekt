package Decorators.DeleteUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/** Dekorator koji obaviještava da knjižničar nije odabran,*/
public class LibrarianNotSelectedDecorator extends MessageDecorator {
    public LibrarianNotSelectedDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n knjižničar nije izabran!";
    }
}
