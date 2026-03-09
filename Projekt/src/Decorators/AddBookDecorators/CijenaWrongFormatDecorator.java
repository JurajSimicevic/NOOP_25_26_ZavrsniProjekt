package Decorators.AddBookDecorators;

import Decorators.MessageDecorator;
import Decorators.ReturnMessage;

public class CijenaWrongFormatDecorator extends MessageDecorator {
    /**
     * Konstruktor koji prima objekt koji želimo proširiti.
     *
     * @param message Objekt tipa ReturnMessage (može biti BasicReturnMessage ili drugi dekorator).
     */
    public CijenaWrongFormatDecorator(ReturnMessage message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return decoratedMessage.getMessage() + "\n cijena je krivo upisana!";
    }
}
