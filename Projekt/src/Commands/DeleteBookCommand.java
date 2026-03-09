package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.*;
import Decorators.DeleteBookDecorators.BookAlreadyBorrowedDecorator;
import Decorators.DeleteBookDecorators.BookNotSelectedDecorator;
import javax.swing.*;

/**
 * Naredba za trajno uklanjanje knjige iz inventara knjižnice.
 * <p>
 * Implementacijom {@link Command} sučelja, proces brisanja postaje reverzibilna operacija.
 * Budući da se cijeli objekt {@link Knjiga} čuva unutar instance naredbe, sustav može
 * izvršiti potpuni povrat podataka u katalog čak i nakon što su oni uklonjeni iz baze.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Očuvanje stanja:</b> Čuva referencu na {@code bookToDelete} izvan glavne liste
 * kako bi {@code undo()} mogao vratiti identičan objekt sa svim njegovim atributima.</li>
 * <li><b>Poslovno pravilo:</b> Implementira strogu validaciju koja sprječava brisanje
 * knjiga koje su trenutno kod korisnika (posuđene).</li>
 * <li><b>Nasljeđivanje:</b> Proširuje {@link BaseCommand} radi unificiranog sustava
 * poruka o pogreškama putem Decoratora.</li>
 * </ul>
 * <b>Tijek izvršavanja:</b>
 * <ol>
 * <li>Poziv {@code execute()}: Validacija statusa posudbe -> Trajno uklanjanje iz sustava -> Obavijest o uspjehu.</li>
 * <li>Poziv {@code undo()}: Korisnička potvrda -> Ponovno ubrizgavanje sačuvanog objekta u katalog.</li>
 * </ol>
 */
public class DeleteBookCommand extends BaseCommand implements Command {

    /**Referenca na objekt {@code Knjiga} koji je meta brisanja.
     * Čuvamo cijeli objekt u memoriji čak i nakon brisanja iz liste kako bi ga undo() mogao vratiti.
     */
    private Knjiga bookToDelete;
    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();

    /**
     * Konstruktor koji definira metu brisanja na temelju korisničke selekcije.
     * @param book Knjiga koju je korisnik selektirao za uklanjanje.
     */
    public DeleteBookCommand(Knjiga book) {
        this.bookToDelete = book;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Provjerava status selekcije i dostupnost knjige pozivom metode {@link #createMessage()}.</li>
     * <li>Onemogućuje brisanje ako je knjiga u statusu posudbe kako bi se očuvao integritet baze.</li>
     * </ul>
     * @return {@code true} ako nema validacijskih prepreka za uklanjanje knjige.
     */
    @Override
    public boolean canExecute() {
        if (bookToDelete == null || bookToDelete.isPosudjena()) {
            createMessage();
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Inicira trajno uklanjanje objekta {@link Knjiga} iz memorijske liste i baze podataka.</li>
     * <li>Prikazuje vizualnu potvrdu korisniku o uspješnom brisanju.</li>
     * </ul>
     * @return {@code true} ako je {@link LibraryManager} potvrdio uspješno brisanje.
     */
    @Override
    public boolean execute() {
        if(libraryManager.removeBook(bookToDelete)){
            JOptionPane.showMessageDialog(null, "Knjiga uspješno izbrisana!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Zahtijeva eksplicitnu potvrdu korisnika prije vraćanja obrisanog objekta.</li>
     * <li>Vraća prethodno sačuvanu referencu knjige natrag u sustav bez gubitka podataka.</li>
     * </ul>
     * @return {@code true} ako je objekt uspješno reinstaliran u katalog knjiga.
     */
    @Override
    public boolean undo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite poništiti brisanje knjige: " + bookToDelete.getNazivDjela() + "?",
                "Poništi brisanje (Undo)",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            if(libraryManager.addBook(bookToDelete)){
                JOptionPane.showMessageDialog(null, "Knjiga uspješno dodana natrag!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Ponavlja proces brisanja za knjigu koja je prethodno bila vraćena putem undo operacije.</li>
     * <li>Koristi postojeću logiku {@link #execute()} metode uz dodatnu potvrdu dijalogom.</li>
     * </ul>
     * @return {@code true} ako je ponovljeno brisanje uspješno izvršeno.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponoviti brisanje knjige: " + bookToDelete.getNazivDjela() + "?",
                "Ponovi brisanje (Redo)",
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

        ReturnMessage msg = new BasicReturnMessage("Greška pri brisanju:");

        if (bookToDelete == null) {
            msg = new BookNotSelectedDecorator(msg);
        } else if (bookToDelete.isPosudjena()) {
            msg = new BookAlreadyBorrowedDecorator(msg);
        }

        JOptionPane.showMessageDialog(null, msg.getMessage(), "Neuspjela validacija", JOptionPane.ERROR_MESSAGE);
    }
}