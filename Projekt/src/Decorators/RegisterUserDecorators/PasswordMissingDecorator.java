package Decorators.RegisterUserDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

/**
 * Dekorator koji proširuje poruku informacijom da polje za lozinku nije popunjeno.
 * Ključan za osiguravanje sigurnosnih standarda pri kreiranju novih računa.
 */
public class PasswordMissingDecorator extends MessageDecorator {
    public PasswordMissingDecorator(ReturnMessage message) {
        super(message);
    }

    /** {@inheritDoc} + poruka koju sam nadodaje.*/
    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n Nedostaje lozinka!";
    }
}
