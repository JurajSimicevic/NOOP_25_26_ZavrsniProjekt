package BackEnd.Management;

import BackEnd.Customer;
import BackEnd.Knjiga;

import java.util.List;

/**
 * Krajnje sučelje za upravljanje fondom knjiga i procesima posudbe.
 * Proširuje {@link Filterable} za podršku pretraživanja knjiga.
 */
public interface BookManagerInt extends Filterable<Knjiga> {

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
}
