package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.BasicReturnMessage;
import Decorators.BorrowBookDecorators.CustomerNotSelectedDecorator;
import Decorators.DeleteBookDecorators.BookAlreadyBorrowedDecorator;
import Decorators.DeleteBookDecorators.BookNotSelectedDecorator;
import Decorators.ReturnMessage;

import javax.swing.*;

/**
 * Naredba koja enkapsulira proces posudbe knjige korisniku.
 * <p>
 * Implementacijom {@link Command} sučelja, svaka posudba postaje objekt koji se može
 * pohraniti u povijest akcija. Omogućuje precizno praćenje tko je posudio koju knjigu
 * te podržava potpuno poništavanje transakcije.
 * </p>
 * <b>Arhitektonska rješenja:</b>
 * <ul>
 * <li><b>Logička inverzija:</b> Metoda {@code undo()} koristi logiku vraćanja knjige
 * kako bi sustav vratio u stanje prije posudbe.</li>
 * <li><b>Decorator validacija:</b> Dinamički provjerava selekciju entiteta i dostupnost
 * knjige, sprječavajući dvostruku posudbu istog naslova.</li>
 * <li><b>Interakcija s Managerom:</b> Komunicira isključivo s {@link LibraryManagerInterface}
 * radi očuvanja integriteta podataka.</li>
 * </ul>
 */
public class BorrowBookCommand extends BaseCommand implements Command {

    /** Referenca na {@link Customer} objekt koji vrši posudbu. */
    private Customer customer;

    /** Referenca na {@link Knjiga} objekt koji se iznajmljuje. */
    private Knjiga selectedBook;

    /** Definirano trajanje posudbe u danima. */
    private int brojDanaPosudbe;


    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();

    /**
     * Inicijalizira naredbu sa svim parametrima potrebnim za uspješnu transakciju.
     * @param selectedBook Knjiga odabrana u GUI tablici.
     * @param customer Korisnik odabran u listi kupaca.
     * @param brojDanaPosudbe Broj dana (rok) za povratak knjige.
     */
    public BorrowBookCommand(Knjiga selectedBook, Customer customer, int brojDanaPosudbe) {
        this.selectedBook = selectedBook;
        this.customer = customer;
        this.brojDanaPosudbe = brojDanaPosudbe;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     *     <li>Odabrani objekti i dostupnost knjige.</li>
     * </ul>
     * @return {@code true} ako su svi podaci ispravni.
     */
    @Override
    public boolean canExecute() {
        if(selectedBook == null || customer == null || selectedBook.isPosudjena()){
            createMessage();
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * @return {@code true} ako je posudba uspješno zabilježena.
     */
    @Override
    public boolean execute() {
        if(libraryManager.borrowBook(selectedBook, customer, brojDanaPosudbe)){
            JOptionPane.showMessageDialog(null, "Knjiga uspješno posuđena!", "Uspjeh!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <ul>
     *     <li>Logički, poništavanje posudbe je isto što i vraćanje knjige, pa to i radimo s ovom metodom.</li>
     * </ul>
     * @return {@code true} ako je knjiga uspješno razdužena.
     */
    @Override
    public boolean undo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite poništiti posuđivanje knjige: " + selectedBook.getNazivDjela() + "?",
                "UNDO?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            if(libraryManager.returnBook(selectedBook)){
                JOptionPane.showMessageDialog(null, "Poništavanje posuđivanja uspješno!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * uz korisničku potvrdu.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponovno posuditi knjigu: " + selectedBook.getNazivDjela() + "?",
                "REDO?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            return execute();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createMessage(){

        ReturnMessage msg = new BasicReturnMessage("Greška pri posuđivanju:");
        if (selectedBook == null){
            msg = new BookNotSelectedDecorator(msg);
        }
        if (customer == null) {
            msg = new CustomerNotSelectedDecorator(msg);
        }
        try{
            if (selectedBook.isPosudjena()) {
                msg = new BookAlreadyBorrowedDecorator(msg);
            }
        } catch (NullPointerException e) {
            System.out.println();
        }


        JOptionPane.showMessageDialog(null, msg.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
}