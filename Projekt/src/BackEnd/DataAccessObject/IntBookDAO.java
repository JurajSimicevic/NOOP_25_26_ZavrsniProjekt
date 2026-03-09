package BackEnd.DataAccessObject;

import BackEnd.Knjiga;
import java.util.List;

/**
 * Sučelje koje definira standardne operacije za pristup podacima o knjigama.
 * <p>
 * Služi kao apstrakcijski sloj (DAO pattern) koji odvaja logiku baze podataka
 * od ostatka aplikacije, omogućujući CRUD operacije nad entitetom {@link Knjiga}.
 * </p>
 */
public interface IntBookDAO {

    /**
     * Dohvaća sve knjige iz izvora podataka.
     * @return Lista svih objekata tipa {@link Knjiga}.
     */
    List<Knjiga> getAll();

    /**
     * Pohranjuje novu knjigu u sustav.
     * @param k Objekt knjige koji se dodaje.
     * @return {@code true} ako je operacija uspjela, {@code false} inače.
     */
    boolean insert(Knjiga k);

    /**
     * Uklanja knjigu iz sustava na temelju njezinog ISBN-a.
     * @param isbn Jedinstveni identifikator knjige koju treba obrisati.
     * @return {@code true} ako je brisanje uspješno, {@code false} inače.
     */
    boolean delete(String isbn);

    /**
     * Ažurira status posudbe za određenu knjigu, uključujući povezivanje s korisnikom.
     * @param k Objekt knjige čiji se status (posuđena/dostupna) ažurira.
     * @param customerId ID korisnika koji posuđuje knjigu ili {@code null} ako se knjiga vraća.
     * @return {@code true} ako je ažuriranje uspjelo, {@code false} inače.
     */
    boolean updateBorrowStatus(Knjiga k, Integer customerId);
}