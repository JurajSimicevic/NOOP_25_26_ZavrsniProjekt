package BackEnd.Management;

import BackEnd.Customer;
import BackEnd.Knjiga;
import BackEnd.Librarian;

import java.util.List;

/**
 * Sučelje koje definira metode za filtriranje knjiga, korisnika i knjižničara.
 * <p>Služi kao ugovor za klase koje obrađuju logiku pretraživanja, omogućujući
 * konzistentno upravljanje filtriranim podacima unutar različitih panela aplikacije.
 * </p>
 */
public interface FilterInterface {

    /** Postavlja trenutni kriterij (String) za filtriranje knjiga.
     * @param filter    dobiveni tekst koji se traži
     */
    void setBookFilter(String filter);

    /** Postavlja trenutni kriterij (String) za filtriranje korisnika (članova).
     * @param filter    dobiveni tekst koji se traži
     */
    void setCustomerFilter(String filter);

    /** Postavlja trenutni kriterij (String) za filtriranje knjižničara (zaposlenika).
     * @param filter    dobiveni tekst koji se traži
     */
    void setLibrarianFilter(String filter);

    /** @return Dohvaća zadnji postavljeni filter za knjige. */
    String getBookFilter();

    /** @return Dohvaća zadnji postavljeni filter za korisnike. */
    String getCustomerFilter();

    /** @return Dohvaća zadnji postavljeni filter za knjižničare. */
    String getLibrarianFilter();

    /**Filtrira listu knjiga na temelju prosljeđenog teksta (npr. po naslovu ili autoru).
     * @param text Tekst koji se traži.
     * @return Lista knjiga koje zadovoljavaju uvjet pretrage.
     */
    List<Knjiga> getFilteredBooks(String text);

    /**Filtrira listu korisnika na temelju prosljeđenog teksta (npr. po imenu ili prezimenu).
     * @param text Tekst koji se traži.
     * @return Lista korisnika koji zadovoljavaju uvjet pretrage.
     */
    List<Customer> getFilteredCustomers(String text);

    /**Filtrira listu knjižničara na temelju prosljeđenog teksta (npr. po korisničkom imenu).
     * @param text Tekst koji se traži.
     * @return Lista knjižničara koji zadovoljavaju uvjet pretrage.
     */
    List<Librarian> getFilteredLibrarians(String text);
}
