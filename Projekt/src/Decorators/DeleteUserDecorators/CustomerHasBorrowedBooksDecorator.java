package Decorators.DeleteUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/** Decorator koji obaviještava da korisnik ima posuđenih knjiga (te da ga se zato ne može obrisati iz baze naprimjer...)*/
public class CustomerHasBorrowedBooksDecorator extends MessageDecorator {
    public CustomerHasBorrowedBooksDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n korisnik ima posuđenih knjiga!";
    }
}
