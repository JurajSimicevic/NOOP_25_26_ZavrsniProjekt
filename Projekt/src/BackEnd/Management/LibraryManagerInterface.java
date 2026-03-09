package BackEnd.Management;

import BackEnd.Customer;
import BackEnd.Knjiga;
import BackEnd.Librarian;
import ObserversAndOtherComps.Observable;
import ObserversAndOtherComps.Observer;

import java.util.List;

/**
 * Glavno poslovno sučelje sustava (<i>Business Logic Interface</i>).
 * <p>
 * Ovo sučelje djeluje kao centralni ugovor aplikacije, objedinjujući funkcionalnosti
 * promatranja stanja, filtriranja podataka i upravljanja entitetima.
 * Nasljeđivanjem {@link Observable} i {@link FilterInterface}, ono definira
 * kompletan okvir za rad s backend logikom.
 * </p>
 * <b>Ključne domene koje pokriva:</b>
 * <ul>
 * <li><b>CRUD operacije:</b> Standardizirane metode za kreiranje, čitanje, ažuriranje i brisanje (Knjige, Korisnici, Knjižničari).</li>
 * <li><b>Upravljanje stanjem:</b> Integracija s {@link Observer} uzorkom za automatsko osvježavanje grafičkog sučelja.</li>
 * <li><b>Pretraživanje:</b> Nasljeđivanje sučelja za napredno filtriranje kataloga i baze korisnika.</li>
 * </ul>
 * <b>Arhitektonska uloga:</b>
 * <ul>
 * <li><b>Fasada (Facade):</b> Skriva kompleksnost interakcije s više DAO objekata iza jednostavnog sučelja.</li>
 * <li><b>Decoupling:</b> Omogućuje GUI sloju da komunicira s logikom bez poznavanja detalja o bazi podataka.</li>
 * </ul>
 */
public interface LibraryManagerInterface extends Observable, FilterInterface {

    /**
     * Dodaje novu knjigu u sustav.
     * @param k Objekt klase {@link Knjiga} koji se unosi.
     * @return {@code true} ako je dodavanje uspješno.
     */
    boolean addBook(Knjiga k);

    /**
     * Obrađuje posudbu knjige određenom korisniku.
     * @param k Knjiga koja se posuđuje.
     * @param c Korisnik koji posuđuje.
     * @param days Broj dana posudbe.
     * @return {@code true} ako je knjiga uspješno posuđena.
     */
    boolean borrowBook(Knjiga k, Customer c, int days);

    /**
     * Bilježi povrat knjige i oslobađa je za nove posudbe.
     * @param k Knjiga koja se vraća u posjed knjižnice.
     * @return {@code true} ako je povrat uspješno odrađen.
     */
    boolean returnBook(Knjiga k);

    /**Trajno uklanja knjigu iz evidencije.
     * @param k Knjiga koju treba obrisati.
     * @return {@code true} ako je brisanje uspjelo.
     */
    boolean removeBook(Knjiga k);

    /**  @return Aktualnu {@code List<Knjiga>} listu svih knjiga u sustavu.*/
    List<Knjiga> getKnjige();

    /**
     * Dodaje novi {@code Customer} objekt u sustav.
     * @param c Objekt korisnika.
     * @return {@code true} ako korisnik već ne postoji i uspješno je dodan.
     */
    boolean addCustomer(Customer c);

    /**
     * Briše odabrani {@code Customer} objekt iz sustava.
     * @param c Objekt korisnika kojeg treba ukloniti.
     * @return {@code true} ako je korisnik pronađen i obrisan.
     */
    boolean deleteCustomer(Customer c);

    /**  @return Aktualnu {@code List<Customer>} listu svih članova knjižnice u sustavu.*/
    List<Customer> getCustomers();

    /**
     * Registrira odabrani objekt {@code Librarian} u sustav .
     * @param l Objekt knjižničara s login podacima.
     * @return {@code true} ako je dodavanje uspjelo.
     */
    boolean addLibrarian(Librarian l);

    /**
     * Uklanja odabrani {@code Librarian} objekt iz sustava.
     * @param l Objekt knjižničara za brisanje.
     * @return {@code true} ako je brisanje uspjelo.
     */
    boolean deleteLibrarian(Librarian l);

    /**  @return Aktualnu {@code List<Librarian>} listu svih knjižničara u sustavu.*/
    List<Librarian> getLibrarians();

}
