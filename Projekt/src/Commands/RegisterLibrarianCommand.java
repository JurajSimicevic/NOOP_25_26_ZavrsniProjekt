package Commands;

import BackEnd.*;
import BackEnd.FactoryComps.LibrarianFactory;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.BasicReturnMessage;
import Decorators.RegisterUserDecorators.*;
import Decorators.ReturnMessage;
import javax.swing.*;
import java.awt.*;

/**
 * Naredba zadužena za registraciju novog zaposlenika (knjižničara) u sustav.
 * <p>
 * Ova klasa implementira {@link Command} uzorak koji omogućuje naknadno poništavanje registracije.
 * Proces uključuje strogu provjeru jedinstvenosti korisničkog imena i dinamičku validaciju
 * svih osobnih i login podataka putem dekoratora.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Provjera jedinstvenosti:</b> Koristi Stream API unutar metode {@code exists()}
 * kako bi se spriječilo dupliciranje korisničkih imena (Username).</li>
 * <li><b>Decorator uzorak:</b> Detaljno provjerava svako polje (korisničko ime, lozinka,
 * ime, prezime, dob, grad, adresa) prije dopuštanja kreiranja objekta.</li>
 * <li><b>Upravljanje stanjem:</b> Čuva referencu na stvorenog {@link Librarian} agenta
 * kako bi {@code undo()} mogao precizno ukloniti račun bez nuspojava.</li>
 * </ul>
 * <b>Tijek izvršavanja:</b>
 * <ol>
 * <li>Poziv {@code execute()}: Validacija polja -> Provjera zauzetosti korisničkog imena -> Upis u sustav.</li>
 * <li>Poziv {@code undo()}: Korisnička potvrda -> Trajno uklanjanje korisničkog računa iz baze.</li>
 * </ol>
 */
public class RegisterLibrarianCommand extends BaseCommand implements Command {

    private String userName, password, ime, prezime, grad, adresa;
    private int dob;

    /** Referenca na novostvorenog knjižničara, nužna za preciznu Undo operaciju. */
    private Librarian librarian;

    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();
    private LibrarianFactory librarianFactory = LibrarianFactory.getInstance();

    /**
     * Konstruktor koji prima sirove podatke potrebne za kreiranje novog zaposlenika.
     */
    public RegisterLibrarianCommand(String userName, String password, String ime, String prezime, int dob, String grad, String adresa) {
        this.userName = userName;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.dob = dob;
        this.grad = grad;
        this.adresa = adresa;
    }

    /**
     * *{@inheritDoc}:
     * <ul>
     * <li>Pokreće {@link #createMessage()} za provjeru jesu li sva polja ispravno popunjena.</li>
     * <li>Provjerava je li korisničko ime već zauzeto u sustavu putem metode {@link #exists()}.</li>
     * </ul>
     * @return {@code true} ako su podaci validni i korisničko ime je slobodno.
     */
    @Override
    public boolean canExecute() {
        if (userName.isEmpty() || password.isEmpty() || ime.isEmpty() || prezime.isEmpty() || dob < 15 || grad.isEmpty() || adresa.isEmpty()) {
            createMessage();
            return false;
        }
        if (exists()) {
            JOptionPane.showMessageDialog(null, "Korisničko ime je zauzeto!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Instancira novi objekt {@link Librarian} s dodijeljenim parametrima.</li>
     * <li>Sprema zaposlenika u bazu podataka putem {@link LibraryManager}-a.</li>
     * </ul>
     * @return {@code true} ako je novi knjižničar uspješno dodan u sustav.
     */
    @Override
    public boolean execute() {
        System.out.println(userName + password + ime + prezime + dob + grad + adresa);
        librarianFactory.setCredentials(userName, password);
        librarian = (Librarian) librarianFactory.createUser(ime, prezime, dob, grad, adresa);
        if (libraryManager.addLibrarian(librarian)) {
            JOptionPane.showMessageDialog(null, "Knjižničar uspješno registriran!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Traži potvrdu operatera za poništavanje registracije.</li>
     * <li>Uklanja točno određenu instancu knjižničara kreiranu u ovoj naredbi.</li>
     * </ul>
     * @return {@code true} ako je korisnički račun uspješno obrisan iz sustava.
     */
    @Override
    public boolean undo() {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite poništiti registraciju knjižničara: " + librarian.getUsername() + "?",
                "UNDO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (dialogResult == JOptionPane.YES_OPTION) {
            Librarian loggedInUser = SessionManager.getInstance().getLoggedInUser();
            if (loggedInUser != null) {
                if (loggedInUser.getUsername().equals(librarian.getUsername())) {
                    JOptionPane.showMessageDialog(null, "Ne možete izbrisati račun na koji ste trenutno prijavljeni!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(libraryManager.deleteLibrarian(librarian)){
                JOptionPane.showMessageDialog(null, "Poništeno registriranje knjižničara!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Ponavlja postupak registracije zaposlenika uz ponovnu potvrdu dijalogom.</li>
     * </ul>
     * @return {@code true} ako je ponovna registracija uspješna.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponovno registrirati knjižničara: " + librarian.getUsername() + "?",
                "REDO",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            return execute();
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected void createMessage() {

        ReturnMessage message = new BasicReturnMessage("Greška pri unosu:");

        if (userName.isEmpty()) message = new UsernameMissingDecorator(message);
        if (password.isEmpty()) message = new PasswordMissingDecorator(message);
        if (ime.isEmpty()) message = new NameMissingDecorator(message);
        if (prezime.isEmpty()) message = new LastnameMissingDecorator(message);
        if (dob < 15) message = new InvalidAgeDecorator(message);
        if (grad.isEmpty()) message = new CityMissingDecorator(message);
        if (adresa.isEmpty()) message = new AddressMissingDecorator(message);

        JOptionPane.showMessageDialog(null, message.getMessage(), "Neispravan unos", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Provjerava postojanje knjižničara s istim korisničkim imenom.
     * @return {@code true} ako korisničko ime već postoji u listi.
     */
    private boolean exists() {
        return libraryManager.getLibrarians().stream()
                .anyMatch(l -> l.getUsername().equalsIgnoreCase(userName));
    }
}