package Commands;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import Decorators.BasicReturnMessage;
import Decorators.DeleteUserDecorators.CustomerHasBorrowedBooksDecorator;
import Decorators.DeleteUserDecorators.CustomerNotSelected;
import Decorators.ReturnMessage;

import javax.swing.*;

/**
 * Naredba za trajno uklanjanje člana (Customer) iz baze podataka knjižnice.
 * <p>
 * Implementacijom {@link Command} sučelja, proces brisanja korisnika postaje reverzibilna operacija.
 * Budući da objekt {@link Customer} perzistira unutar instance naredbe, sustav omogućuje
 * potpuni povrat podataka o članu (uključujući povijest i osobne podatke) čak i nakon brisanja.
 * </p>
 * <b>Ključne značajke i obrasci:</b>
 * <ul>
 * <li><b>Sigurnosna validacija:</b> Onemogućuje brisanje članova koji imaju aktivna zaduženja,
 * sprječavajući gubitak traga o posuđenoj građi.</li>
 * <li><b>Očuvanje identiteta:</b> {@code undo()} operacija vraća originalnu referencu objekta,
 * čime se zadržava integritet relacija u memoriji.</li>
 * <li><b>Interakcija s Managerom:</b> Izravno komunicira s {@link LibraryManagerInterface}
 * za ažuriranje liste korisnika.</li>
 * </ul>
 * <b>Tijek izvršavanja:</b>
 * <ol>
 * <li>Poziv {@code execute()}: Provjera zaduženja -> Potvrda operatera -> Uklanjanje iz sustava.</li>
 * <li>Poziv {@code undo()}: Korisnička potvrda -> Reinstalacija člana u aktivnu listu.</li>
 * </ol>
 */
public class DeleteCustomerCommand extends BaseCommand implements Command {

    /** Referenca na objekt {@link Customer} koji je meta brisanja. */
    private Customer customer;

    private LibraryManagerInterface libraryManager = LibraryManager.getInstance();

    /**
     * Konstruktor naredbe koji prima odabranog korisnika iz GUI sučelja.
     * @param customer Objekt korisnika predviđen za uklanjanje.
     */
    public DeleteCustomerCommand(Customer customer) {
        this.customer = customer;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Provjerava je li korisnik uopće selektiran u sustavu prije nastavka.</li>
     * <li>Vrši uvid u listu posuđenih knjiga; brisanje je zabranjeno ako lista nije prazna.</li>
     * <li>Zahtijeva finalnu potvrdu operatera putem modalnog dijaloga.</li>
     * </ul>
     * @return {@code true} ako je korisnik slobodan za brisanje i operater je potvrdio akciju.
     */
    @Override
    public boolean canExecute() {
        if (customer == null || !customer.getPosudeneKnjige().isEmpty())  {
            createMessage();
            return false;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite obrisati korisnika: " + customer.getIme() + "?",
                "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

        return dialogResult == JOptionPane.YES_OPTION;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Poziva metodu {@code deleteCustomer} unutar {@link LibraryManager}-a.</li>
     * <li>Uklanja člana iz memorijskih listi i perzistentne pohrane.</li>
     * </ul>
     * @return {@code true} ako je operacija brisanja uspješno potvrđena od strane managera.
     */
    @Override
    public boolean execute() {
        if(libraryManager.deleteCustomer(customer)){
            JOptionPane.showMessageDialog(null, "Član uspješno izbrisan!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Prikazuje dijalog za potvrdu povrata obrisanog člana u sustav.</li>
     * <li>Koristi {@code addCustomer} metodu za vraćanje originalne reference u katalog korisnika.</li>
     * </ul>
     * @return {@code true} ako je član uspješno vraćen u listu aktivnih korisnika.
     */
    @Override
    public boolean undo() {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite poništiti brisanje člana " + customer.getIme() + "?",
                "UNDO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(dialogResult == JOptionPane.YES_OPTION){
            System.out.println("dijalog == yes");
            if(libraryManager.addCustomer(customer)){
                System.out.println("libraryManager.addCustomer uspio");
                JOptionPane.showMessageDialog(null, "Poništeno brisanje člana!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            System.out.println("libraryManager.addCustomer nije uspio");
        }
        System.out.println("dijalog == no");
        return false;
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Omogućuje brzo ponavljanje brisanja za člana koji je prethodno vraćen putem {@code undo()} naredbe.</li>
     * <li>Ponovno prolazi kroz proces potvrde kako bi se spriječile slučajne operacije.</li>
     * </ul>
     * @return {@code true} ako je ponovljeno brisanje uspješno izvršeno.
     */
    @Override
    public boolean redo() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Jeste li sigurni da želite ponovno obrisati člana: " + customer.getIme() + "?",
                "REDO",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(choice == JOptionPane.YES_OPTION){
            return execute();
        }
        return false;
    }

    @Override
    protected void createMessage() {

        ReturnMessage msg = new BasicReturnMessage("Greška pri brisanju:");

        if (customer == null) {
            msg = new CustomerNotSelected(msg);
        } else if (!customer.getPosudeneKnjige().isEmpty()) {
            msg = new CustomerHasBorrowedBooksDecorator(msg);
        }

        JOptionPane.showMessageDialog(null, msg.getMessage(), "Neuspjela validacija", JOptionPane.ERROR_MESSAGE);
    }
}