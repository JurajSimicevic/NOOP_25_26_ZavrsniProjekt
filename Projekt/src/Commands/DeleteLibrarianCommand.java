package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.BasicReturnMessage;
import Decorators.DeleteUserDecorators.LibrarianLoggedInDecorator;
import Decorators.DeleteUserDecorators.LibrarianNotSelectedDecorator;
import Decorators.ReturnMessage;

import javax.swing.JOptionPane;

/**
 * Konkretna naredba zadužena za uklanjanje zaposlenika (knjižničara) iz sustava.
 * <p>
 * Implementacijom {@link Command} uzorka, brisanje knjižničara postaje reverzibilna operacija.
 * Ova naredba uključuje kritičnu sigurnosnu logiku koja sprječava trenutno prijavljenog
 * korisnika da obriše vlastiti račun, čime se čuva integritet sesije.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Sigurnosni mehanizam:</b> Koristi {@link SessionManagerInterface} za provjeru
 * identiteta prije dopuštanja brisanja.</li>
 * <li><b>Očuvanje podataka:</b> {@code undo()} operacija vraća originalni objekt
 * {@link Librarian} sa svim njegovim ovlastima i poviješću u katalog.</li>
 * <li><b>Command uzorak:</b> Omogućuje administratoru sustava da poništi slučajno
 * uklanjanje kolege bez ponovnog unosa podataka.</li>
 * </ul>
 * <b>Tijek izvršavanja:</b>
 * <ol>
 * <li>Poziv {@code execute()}: Provjera identiteta -> Brisanje iz baze -> Obavijest o uspjehu.</li>
 * <li>Poziv {@code undo()}: Korisnička potvrda -> Restauracija računa u sustav.</li>
 * </ol>
 */
public class DeleteLibrarianCommand extends BaseCommand implements Command {

    /** Referenca na knjižničara kojeg treba obrisati. */
    private Librarian librarian;

    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();
    private SessionManagerInterface sessionManager = SessionManager.getInstance();

    /**
     * Konstruktor naredbe koji inicijalizira metu brisanja.
     * @param librarian Objekt zaposlenika odabran za uklanjanje.
     */
    public DeleteLibrarianCommand(Librarian librarian) {
        this.librarian = librarian;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Provjerava je li knjižničar selektiran u GUI tablici.</li>
     * <li>Uspoređuje odabranog knjižničara s trenutno prijavljenim korisnikom u sesiji.</li>
     * <li>Sprječava izvršavanje ako korisnik pokuša obrisati samoga sebe.</li>
     * </ul>
     * @return {@code true} ako je brisanje sigurno i logički ispravno.
     */
    @Override
    public boolean canExecute() {
        if (librarian == null || librarian.equals(sessionManager.getLoggedInUser())){
            createMessage();
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Trajno uklanja knjižničara iz radne memorije i baze podataka.</li>
     * <li>Prikazuje vizualnu potvrdu o uspješno obavljenoj operaciji.</li>
     * </ul>
     * @return {@code true} ako je {@link LibraryManager} potvrdio brisanje.
     */
    @Override
    public boolean execute() {
        if (libraryManager.deleteLibrarian(librarian)) {
            JOptionPane.showMessageDialog(null, "Knjižničar uspješno obrisan!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Zahtijeva eksplicitnu potvrdu prije vraćanja korisničkog računa u sustav.</li>
     * <li>Vraća prethodno sačuvanu referencu knjižničara natrag u bazu putem managera.</li>
     * </ul>
     * @return {@code true} ako je račun knjižničara uspješno reaktiviran.
     */
    @Override
    public boolean undo() {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite poništiti brisanje knjižničara: " + librarian.getUsername() + "?",
                "UNDO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(dialogResult == JOptionPane.YES_OPTION){
            if(libraryManager.addLibrarian(librarian)){
                JOptionPane.showMessageDialog(null, "Knjižničar uspješno vraćen natrag!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Ponavlja proces uklanjanja knjižničara uz ponovnu sigurnosnu provjeru dijalogom.</li>
     * <li>Koristi već definiranu logiku metode {@link #execute()}.</li>
     * </ul>
     * @return {@code true} ako je ponovljeno brisanje uspješno izvršeno.
     */
    @Override
    public boolean redo() {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite ponoviti brisanje knjižničara: " + librarian.getUsername() + "?",
                "REDO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(dialogResult == JOptionPane.YES_OPTION){
            return execute();
        }
        return false;
    }

    @Override
    protected void createMessage() {
        ReturnMessage msg = new BasicReturnMessage("Greška pri brisanju:");

        if (librarian == null) {
            msg = new LibrarianNotSelectedDecorator(msg);
        } else if (librarian.equals(sessionManager.getLoggedInUser())) {
            msg = new LibrarianLoggedInDecorator(msg);
        }

        JOptionPane.showMessageDialog(null, msg.getMessage(), "Neuspjela validacija", JOptionPane.ERROR_MESSAGE);
    }
}