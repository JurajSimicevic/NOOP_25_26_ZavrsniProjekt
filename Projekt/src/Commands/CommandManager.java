package Commands;

import BackEnd.DatabaseConnectionManager;
import BackEnd.Librarian;
import BackEnd.SessionManager;

import javax.swing.*;
import java.util.Stack;

/**
 * Konkretna implementacija upravljača naredbama koja koristi Singleton uzorak.
 * Klasa održava povijest svih izvršenih akcija koristeći LIFO (Last-In-First-Out)
 * strukture podataka (Stack) kako bi omogućila neograničen Undo i Redo mehanizam.
 */
public class CommandManager implements CommandManagerInterface {

    /** Stog koji čuva izvršene naredbe spremne za poništavanje. */
    private final Stack<Command> undoStack = new Stack<>();

    /** Stog koji čuva poništene naredbe spremne za ponovno izvršavanje. */
    private final Stack<Command> redoStack = new Stack<>();

    /** Privatni konstruktor za sprječavanje instanciranja izvan klase. */
    private CommandManager() {}

    /** Bill Pugh Singleton holder za thread-safe inicijalizaciju. */
    private static class Holder {
        private static final CommandManager INSTANCE = new CommandManager();
    }

    /** @return Globalno dostupna instanca CommandManagera. */
    public static CommandManagerInterface getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Izvršava proslijeđenu naredbu uz provjeru statusa baze podataka.
     * Ako je naredba uspješna, sprema je u undoStack i ČISTI redoStack.
     * @param cmd Objekt naredbe (npr. AddBookCommand, BorrowCommand).
     * @return {@code true} ako je naredba uspješno izvršena i spremljena u povijest.
     */
    @Override
    public boolean executeCommand(Command cmd) {
        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            if (cmd.canExecute()) {
                if (cmd.execute()) {
                    undoStack.push(cmd);
                    // Ključno: Svaka nova akcija briše redo povijest jer se stvara nova grana događaja
                    redoStack.clear();
                    System.out.println("Komanda izvršena i dodana u Undo stog.");
                    return true;
                }
            }
            return false;
        }
        JOptionPane.showMessageDialog(null, "Niste povezani na bazu podataka!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    /**
     * Skida zadnju naredbu s undoStacka i poziva njezinu undo() metodu.
     * Ako uspije, seli tu naredbu u redoStack.
     * @return {@code true} ako je radnja uspješno vraćena unazad.
     */
    @Override
    public boolean undo() {
        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            if (!undoStack.isEmpty()) {
                Command cmd = undoStack.pop();
                if(cmd.undo()){
                    redoStack.push(cmd);
                    System.out.println("Undo izvršen.");
                    return true;
                }else{
                    // Ako je undo otkazan, vraćamo komandu na stog da očuvamo integritet
                    undoStack.push(cmd);
                    System.out.println("Undo prekinut");
                }
            }
            return false;
        }
        JOptionPane.showMessageDialog(null, "Ne možete poništiti radnju: \n Niste povezani na bazu podataka!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    /**
     * Skida zadnju poništenu naredbu s redoStacka i ponovno je izvršava.
     * Nakon toga je vraća u undoStack.
     * @return {@code true} ako je radnja ponovno izvršena.
     */
    @Override
    public boolean redo() {
        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            if (!redoStack.isEmpty()) {
                Command cmd = redoStack.pop();
                if(cmd.redo()){
                    undoStack.push(cmd);
                    System.out.println("Redo izvršen.");
                    return true;
                }else{
                    // Ako je redo otkazan, vraćamo komandu na stog da očuvamo integritet
                    redoStack.push(cmd);
                    System.out.println("Redo prekinut");
                }
            }
            return false;
        }
        JOptionPane.showMessageDialog(null, "Ne možete ponoviti radnju: \n Niste povezani na bazu podataka!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    /** {@inheritDoc}*/
    @Override
    public void clearSessions() {
        undoStack.clear();
        redoStack.clear();
    }
}