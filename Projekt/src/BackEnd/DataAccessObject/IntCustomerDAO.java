package BackEnd.DataAccessObject;

import BackEnd.Customer;
import java.util.List;

/**
 * Sučelje koje definira standardni skup operacija za upravljanje podacima o korisnicima.
 * <p>
 * Služi kao ugovor za sve implementacije pristupa podacima (DAO) koje rukuju
 * entitetom {@link Customer}, omogućujući CRUD operacije nad bazom podataka.
 * </p>
 */
public interface IntCustomerDAO {

    /**
     * Dohvaća sve registrirane korisnike iz izvora podataka.
     * @return {@link List} svih objekata tipa {@link Customer}.
     */
    List<Customer> getAll();

    /**
     * Trajno pohranjuje novog korisnika u sustav.
     * @param c Objekt korisnika kojeg je potrebno perzistirati.
     * @return {@code true} ako je operacija uspješno izvršena, {@code false} inače.
     */
    boolean insert(Customer c);

    /**
     * Brisanje korisnika iz sustava na temelju njegovog primarnog ključa.
     * @param id Jedinstveni identifikator (ID) korisnika kojeg treba ukloniti.
     * @return {@code true} ako je korisnik uspješno obrisan, {@code false} inače.
     */
    boolean delete(int id);
}