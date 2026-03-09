package Commands;

import BackEnd.*;
import BackEnd.FactoryComps.CustomerFactory;
import BackEnd.FactoryComps.UserFactory;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.BasicReturnMessage;
import Decorators.RegisterUserDecorators.*;
import Decorators.ReturnMessage;
import javax.swing.*;

/**
 * Naredba zadužena za registraciju novog člana (Customer) u sustav knjižnice.
 * <p>
 * Implementacijom {@link Command} uzorka, registracija postaje reverzibilna radnja.
 * Klasa koristi naprednu validaciju podataka i provjeru postojanja duplikata u bazi
 * prije samog upisa, čime se održava integritet korisničkih podataka.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Decorator uzorak:</b> Dinamički gradi hijerarhiju poruka o pogreškama za svako
 * neispunjeno polje (ime, prezime, dob, grad, adresa).</li>
 * <li><b>Provjera duplikata:</b> Koristi Java Streams za detekciju članova s identičnim
 * imenom i prezimenom te zahtijeva potvrdu operatera za nastavak.</li>
 * <li><b>Precizan Undo:</b> Čuva referencu na točno stvoreni objekt {@link Customer}
 * kako bi ga poništavanje moglo sigurno ukloniti iz sustava.</li>
 * </ul>
 * <b>Tijek izvršavanja:</b>
 * <ol>
 * <li>Poziv {@code execute()}: Validacija unosa -> Provjera duplikata -> Kreiranje objekta -> Upis u bazu.</li>
 * <li>Poziv {@code undo()}: Korisnička potvrda -> Uklanjanje stvorenog člana iz aktivne liste.</li>
 * </ol>
 */
public class RegisterCustomerCommand extends BaseCommand implements Command {

    private String ime, prezime, grad, adresaStanovanja;
    private int dob;

    /** Referenca na novostvorenog kupca; ključna za undo() operaciju. */
    private Customer newlyCreatedCustomer;

    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();
    private UserFactory customerFactory = CustomerFactory.getInstance();

    /**
     * Konstruktor koji prima sirove podatke o novom članu iz GUI forme.
     */
    public RegisterCustomerCommand(String ime, String prezime, int dob, String grad, String adresaStanovanja) {
        this.ime = ime;
        this.prezime = prezime;
        this.dob = dob;
        this.grad = grad;
        this.adresaStanovanja = adresaStanovanja;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Pokreće metodu {@link #createMessage()} za validaciju svih obaveznih polja.</li>
     * <li>U slučaju pronalaska člana s istim imenom i prezimenom, traži potvrdu operatera za nastavak registracije.</li>
     * </ul>
     * @return {@code true} ako su podaci validni i operater dopušta upis unatoč potencijalnim duplikatima.
     */
    @Override
    public boolean canExecute() {
        if (ime.isEmpty() || prezime.isEmpty() || dob < 15 || grad.isEmpty() || adresaStanovanja.isEmpty()) {
            createMessage();
            return false;
        }

        if(sameNameCustomer()){
            int choice = JOptionPane.showConfirmDialog(null,
                    "Korisnik s tim imenom i prezimenom već postoji. Želite li svejedno nastaviti?",
                    "Potencijalni duplikat",
                    JOptionPane.YES_NO_OPTION);
            return choice == JOptionPane.YES_OPTION;
        }

        return true;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Instancira novi objekt {@link Customer} i sprema ga u sustav putem managera.</li>
     * <li>Pohranjuje referencu na stvorenog člana za potrebe naknadnog poništavanja.</li>
     * </ul>
     * @return {@code true} ako je registracija uspješno provedena u bazi podataka.
     */
    @Override
    public boolean execute() {
        newlyCreatedCustomer = (Customer) customerFactory.createUser(ime, prezime, dob, grad, adresaStanovanja);
        if(libraryManager.addCustomer(newlyCreatedCustomer)){
            JOptionPane.showMessageDialog(null, "Član uspješno registriran!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Poništava registraciju uklanjanjem točno određene instance člana iz sustava.</li>
     * <li>Zahtijeva potvrdu operatera kako bi se spriječilo slučajno brisanje tek unesenih podataka.</li>
     * </ul>
     * @return {@code true} ako je uklanjanje iz sustava uspješno obavljeno.
     */
    @Override
    public boolean undo() {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite poništiti registraciju člana " + newlyCreatedCustomer.getIme() + "?",
                "UNDO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(dialogResult == JOptionPane.YES_OPTION){
            if(libraryManager.deleteCustomer(newlyCreatedCustomer)){
                JOptionPane.showMessageDialog(null, "Registracija poništena!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Ponavlja postupak registracije člana koristeći identične parametre iz originalne naredbe.</li>
     * </ul>
     * @return {@code true} ako je ponovna registracija uspješna.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponovno registrirati člana: " + newlyCreatedCustomer.getIme() + "?",
                "REDO",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            return execute();
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected void createMessage() {

        ReturnMessage message = new BasicReturnMessage("Greška pri unosu:");

        if (ime.isEmpty()) message = new NameMissingDecorator(message);
        if (prezime.isEmpty()) message = new LastnameMissingDecorator(message);
        if (dob <= 0) message = new InvalidAgeDecorator(message);
        if (grad.isEmpty()) message = new CityMissingDecorator(message);
        if (adresaStanovanja.isEmpty()) message = new AddressMissingDecorator(message);

        JOptionPane.showMessageDialog(null, message.getMessage(), "Neispravan unos", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Interna pomoćna metoda koja koristi Stream API za provjeru duplikata.
     */
    private boolean sameNameCustomer(){
        return libraryManager.getCustomers().stream()
                .anyMatch(c -> c.getIme().equalsIgnoreCase(ime) && c.getPrezime().equalsIgnoreCase(prezime));
    }
}