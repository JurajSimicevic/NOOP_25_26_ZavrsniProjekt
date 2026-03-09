package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.BasicReturnMessage;
import Decorators.DeleteBookDecorators.BookAlreadyBorrowedDecorator;
import Decorators.DeleteBookDecorators.BookNotSelectedDecorator;
import Decorators.ReturnMessage;

import javax.swing.*;

/**
 * Naredba koja simulira proces kupnje (trajne prodaje) knjige iz knjižnice.
 * <p>
 * Ova klasa pretvara akciju prodaje u objekt koji se može poništiti. Specifičnost ove
 * naredbe je u tome što uspješna kupnja rezultira trajnim uklanjanjem entiteta {@link Knjiga}
 * iz kataloga, dok {@code undo()} operacija vrši restauraciju tog istog objekta.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Restauracija stanja:</b> Operacija {@code undo()} koristi metodu za dodavanje
 * knjige kako bi se poništio efekt prodaje i knjiga vratila u inventar.</li>
 * <li><b>Validacija statusa:</b> Sprječava prodaju knjiga koje su trenutno posuđene,
 * osiguravajući da se prodati može samo fizički dostupna građa.</li>
 * <li><b>Nasljeđivanje:</b> Koristi {@link BaseCommand} za unificiranu obradu pogrešaka
 * putem Decorator uzorka.</li>
 * </ul>
 */
public class BuyBookCommand extends BaseCommand implements Command {

    /** Referenca na knjigu koja je odabrana za kupnju. */
    private Knjiga selectedBook;
    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();

    /**
     * Konstruktor naredbe za kupnju.
     * @param book Objekt knjige koji korisnik želi kupiti.
     */
    public BuyBookCommand(Knjiga book) {
        this.selectedBook = book;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Provjerava je li knjiga uopće selektirana i je li dostupna za kupnju.</li>
     * <li>Knjiga koja je trenutno posuđena ne može biti predmet prodaje.</li>
     * </ul>
     * @return {@code true} ako nema prepreka za izvršenje kupnje.
     */
    @Override
    public boolean canExecute() {
        if (selectedBook == null || selectedBook.isPosudjena()) {
            createMessage();
            return false;
        }
        System.out.println(selectedBook);
        System.out.println("Je li null?");
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>Izvršava operaciju prodaje uklanjanjem knjige iz sustava.</p>
     * @return {@code true} ako je knjiga uspješno uklonjena iz baze i memorije.
     */
    @Override
    public boolean execute() {
        if(LibraryManager.getInstance().removeBook(selectedBook)){
            JOptionPane.showMessageDialog(null, "Kupnja knjige uspješna! \n Iznos: " + selectedBook.getCijena());
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>Budući da je kupnja obrisala knjigu, poništavanje je zapravo ponovno dodavanje knjige.</p>
     * @return {@code true} ako je knjiga uspješno vraćena u katalog.
     */
    @Override
    public boolean undo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite poništiti kupovinu knjige: " + selectedBook.getNazivDjela() + "?",
                "UNDO?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            if(libraryManager.addBook(selectedBook)){
                JOptionPane.showMessageDialog(null, "Poništavanje kupnje uspješno!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc} uz korisničku potvrdu.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponoviti kupnju knjige: " + selectedBook.getNazivDjela() + "?",
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
    protected void createMessage() {

        ReturnMessage msg = new BasicReturnMessage("Greška pri pokušaju kupovine:");

        if(selectedBook == null){
            msg = new BookNotSelectedDecorator(msg);
        } else{
            if(selectedBook.isPosudjena()){
                msg = new BookAlreadyBorrowedDecorator(msg);
            }
        }

        JOptionPane.showMessageDialog(null, msg.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
}