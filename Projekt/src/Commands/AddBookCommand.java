package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.*;
import Decorators.AddBookDecorators.*;

import javax.swing.*;

/**
 * Konkretna implementacija naredbe za dodavanje nove knjige u katalog.
 * <p>
 * Ova klasa enkapsulira cjelokupni proces kreiranja entiteta {@link Knjiga}.
 * Korištenjem <b>Command uzorka</b>, omogućuje se pohrana stanja sustava prije i poslije
 * izvršenja, što je temelj za <i>Undo/Redo</i> funkcionalnost.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Undo/Redo Logika:</b> Pohranjuje referencu na dodanu knjigu kako bi se u slučaju
 * poništavanja mogla precizno ukloniti iz {@link LibraryManager}-a.</li>
 * <li><b>Decorator Uzorak:</b> Koristi omotač za dinamičku validaciju korisničkog unosa
 * (npr. provjera ISBN formata ili cijene) prije samog upisa u bazu podataka.</li>
 * <li><b>Enkapsulacija:</b> Svi parametri potrebni za kreiranje (naslov, autor, ISBN)
 * čuvaju se unutar instance naredbe.</li>
 * </ul>
 */
public class AddBookCommand extends BaseCommand implements Command {

    private String autor, naziv, isbn, vrsta;
    private double cijena;

    /** Referenca na stvorenu knjigu;
     * <p> Ključno za {@code undo()} operaciju (da znamo što obrisati).</p>
     */
    private Knjiga newlyCreatedBook;

    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();

    /**
     * Konstruktor koji prima sirove podatke iz GUI formi.
     * @param autor Autor knjige.
     *@param naziv Naslov djela.
     * @param isbn ISBN broj.
     * @param vrsta Žanr knjige.
     * @param cijena Cijena knjige.
     */
    public AddBookCommand(String autor, String naziv, String isbn, String vrsta, double cijena) {
        this.autor = autor;
        this.naziv = naziv;
        this.isbn = isbn;
        this.vrsta = vrsta;
        this.cijena = cijena;
    }

    /**
     * {@inheritDoc}
     * <ol>
     * <li>Pokreće {@link #createMessage()} za validaciju praznih polja putem Decoratora.</li>
     * <li>Koristi <b>Java Streams</b> za provjeru postoji li već knjiga s istim naslovom i autorom.</li>
     * </ol>
     * @return {@code true} ako su podaci ispravni i knjiga je unikatna u zbirci.
     */
    @Override
    public boolean canExecute() {
        if (autor.isEmpty() || naziv.isEmpty() || isbn.isEmpty() || vrsta == null || vrsta.isEmpty() || cijena < 0) {
            createMessage();
            return false;
        }
        boolean exists = libraryManager.getKnjige().stream().anyMatch(k -> k.getNazivDjela().equalsIgnoreCase(naziv) && k.getAutor().equalsIgnoreCase(autor));

        if (exists) {
            JOptionPane.showMessageDialog(null, "Knjiga već postoji u zbirci!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     *     <li>Instancira objekt {@link Knjiga} i trajno ga pohranjuje u sustav.</li>
     * </ul>
     * @return {@code true} ako je {@link LibraryManager} potvrdio uspješan upis.
     */
    @Override
    public boolean execute() {
        newlyCreatedBook = new Knjiga(autor, naziv, isbn, vrsta, cijena);
        if(libraryManager.addBook(newlyCreatedBook)){
            JOptionPane.showMessageDialog(null, "Knjiga uspješno dodana!", "Uspjeh!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc} uz korisničku potvrdu.
     * <ul>
     * <li>Brisanje se vrši nad originalnom referencom objekta stvorenog u {@code execute()}.</li>
     * </ul>
     * @return {@code true} ako je brisanje iz sustava uspjelo.
     */
    @Override
    public boolean undo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite poništiti dodavanje knjige: " + newlyCreatedBook.getNazivDjela() + "?",
                "UNDO?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            if(libraryManager.removeBook(newlyCreatedBook)){
                JOptionPane.showMessageDialog(null, "Knjiga uspješno izbrisana!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
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
                "Jeste li sigurni da želite vratiti knjigu: " + newlyCreatedBook.getNazivDjela() + "?",
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

        ReturnMessage message = new BasicReturnMessage("Greška pri unosu:");

        if (autor.isEmpty()) {
            message = new EmptyAuthorDecorator(message);
        }
        if (naziv.isEmpty()) {
            message = new EmptyNazivDecorator(message);
        }
        if (isbn.isEmpty()) {
            message = new EmptyISBNdecorator(message);
        }
        if (vrsta == null || vrsta.isEmpty()) {
            message = new EmptyVrstaDecorator(message);
        }
        if (cijena == -1) {
            message = new EmptyCijenaDecorator(message);
        }
        if (cijena == -2) {
            message = new CijenaWrongFormatDecorator(message);
        }


        JOptionPane.showMessageDialog(null, message.getMessage(), "Neispravan unos", JOptionPane.ERROR_MESSAGE);
    }
}