package Decorators.ReturnBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji osnovnoj potvrdi o povratu knjige pridružuje informaciju
 * o obračunatoj zakasnini (npr. $5) zbog prekoračenja roka posudbe.
 */
public class DamageFeeDecorator extends MessageDecorator {
    public DamageFeeDecorator(ReturnMessage message) { super(message); }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n(Naknada od $10 zbog oštećenja)";
    }
}