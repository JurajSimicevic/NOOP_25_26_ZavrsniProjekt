package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.*;
import Decorators.DeleteBookDecorators.BookNotSelectedDecorator;
import Decorators.ReturnBookDecorators.BookNotBorrowedDecorator;
import Decorators.ReturnBookDecorators.DamageFeeDecorator;
import Decorators.ReturnBookDecorators.LateFeeDecorator;

import javax.swing.*;

/**
 * Naredba zadužena za proces povrata posuđene knjige natrag u knjižnicu.
 * <p>
 * Implementacijom {@link Command} uzorka, povrat knjige postaje reverzibilna operacija.
 * Budući da povrat knjige briše poveznicu s korisnikom u bazi, ova naredba unutar
 * svoje instance čuva referencu na korisnika i trajanje posudbe kako bi {@code undo()}
 * mogao precizno vratiti sustav u stanje "posuđeno".
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Očuvanje povijesnog stanja:</b> Pamti {@link Customer} objekt i originalni broj
 * dana posudbe radi vjerne rekonstrukcije stanja pri poništavanju.</li>
 * <li><b>Decorator uzorak:</b> Dinamički dodaje informacije o penalima (zakasnina,
 * fizičko oštećenje) u završnu poruku korisniku.</li>
 * <li><b>Logička simetrija:</b> Poništavanje povrata (Undo) automatski ponovno pokreće
 * proces posudbe (Borrow) za istog korisnika.</li>
 * </ul>
 * <b>Tijek izvršavanja:</b>
 * <ol>
 * <li>Poziv {@code execute()}: Detekcija korisnika -> Kalkulacija naknada -> Razduživanje u bazi.</li>
 * <li>Poziv {@code undo()}: Korisnička potvrda -> Ponovna posudba knjige istom članu.</li>
 * </ol>
 */
public class ReturnBookCommand extends BaseCommand implements Command {

    /** Knjiga koja se vraća */
    private Knjiga selectedBook;

    /** Kupac koji je imao knjigu; pamtimo ga u execute() radi mogućeg Undo-a. */
    private Customer customer;

    /** Originalni broj dana posudbe; pamtimo radi Undo-a. */
    private int danaPosudbe;

    /** Služi samo za dekoraciju poruke*/
    private boolean hasLateFee, hasDamageFee;
    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();

    /**
     * Konstruktor koji inicijalizira naredbu s podacima o povratu.
     * @param selectedBook Knjiga koja se razdužuje.
     * @param kasnjenje Indikator postoji li zakasnina.
     * @param steta Indikator postoji li oštećenje na građi.
     */
    public ReturnBookCommand(Knjiga selectedBook, boolean kasnjenje, boolean steta) {
        this.selectedBook = selectedBook;
        this.hasLateFee = kasnjenje;
        this.hasDamageFee = steta;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Provjerava je li ikoja knjiga uopće selektirana</li>
     * <li>Osigurava da se operacija provodi samo nad knjigama koje su u statusu {@code isPosudjena}.</li>
     * </ul>
     * @return {@code true} ako su iznad navedeni uvjeti istiniti.
     */
    @Override
    public boolean canExecute() {
        if (selectedBook == null || !selectedBook.isPosudjena()) {
            createMessage();
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Pronalazi vlasnika zaduženja i sprema njegove podatke za potencijalni {@code undo()}.</li>
     * <li>Poziva {@code returnBook} metodu unutar {@link LibraryManager}-a.</li>
     * <li>Gradi informativnu poruku o povratu koristeći {@link LateFeeDecorator} i {@link DamageFeeDecorator}.</li>
     * </ul>
     * @return {@code true} ako je knjiga uspješno razdužena u sustavu.
     */
    @Override
    public boolean execute() {
        this.customer = selectedBook.getPosudioCustomer();
        this.danaPosudbe = selectedBook.getDanaPosudbe();
        if(libraryManager.returnBook(selectedBook)){
            ReturnMessage msg = new BasicReturnMessage("Knjiga je uspješno vraćena!");
            if(hasLateFee) msg = new LateFeeDecorator(msg);
            if(hasDamageFee) msg = new DamageFeeDecorator(msg);
            JOptionPane.showMessageDialog(null, msg.getMessage(), "Obavijest", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Logički poništava povrat ponovnim kreiranjem zaduženja za istog korisnika.</li>
     * <li>Vraća knjigu u status "posuđeno" uz originalno trajanje posudbe.</li>
     * </ul>
     * @return {@code true} ako je knjiga uspješno vraćena u posjed korisnika.
     */
    @Override
    public boolean undo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite poništiti vraćanje knjige: " + selectedBook.getNazivDjela() + "?",
                "UNDO?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(choice == JOptionPane.YES_OPTION){
            if(libraryManager.borrowBook(selectedBook, customer, danaPosudbe)){
                JOptionPane.showMessageDialog(null, "Vraćanje knjige poništeno!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Ponavlja proces razduživanja knjige uz ponovnu kalkulaciju naknada ako je potrebno.</li>
     * </ul>
     * @return {@code true} ako je ponovljeni povrat uspješno izvršen.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponoviti vraćanje knjige: " + selectedBook.getNazivDjela() + "?",
                "REDO?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(choice == JOptionPane.YES_OPTION){
            return execute();
        }
        return false;
    }

    @Override
    protected void createMessage() {

        ReturnMessage message = new BasicReturnMessage("Greška pri unosu:");

        if (selectedBook == null){
            message = new BookNotSelectedDecorator(message);
        } else {
            if (!selectedBook.isPosudjena()) message = new BookNotBorrowedDecorator(message);
        }

        JOptionPane.showMessageDialog(null, message.getMessage(), "Neispravan unos", JOptionPane.ERROR_MESSAGE);
    }
}