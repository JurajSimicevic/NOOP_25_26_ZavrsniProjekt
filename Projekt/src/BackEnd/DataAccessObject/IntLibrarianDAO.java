package BackEnd.DataAccessObject;

import BackEnd.Librarian;
import java.util.List;

/**
 * Sučelje koje definira operacije nad podacima o knjižničarima.
 * <p>
 * Pruža apstrakcijski sloj za upravljanje računima zaposlenika, njihovim
 * ovlastima i autentifikacijskim podacima unutar sustava.
 * </p>
 */
public interface IntLibrarianDAO {

    /**
     * Dohvaća popis svih knjižničara registriranih u sustavu.
     * @return {@link List} objekata {@link Librarian}.
     */
    List<Librarian> getAll();

    /**
     * Registrira novog knjižničara u izvor podataka.
     * @param l Objekt knjižničara kojeg treba pohraniti.
     * @return {@code true} ako je pohrana uspjela, {@code false} inače.
     */
    boolean insert(Librarian l);

    /**
     * Trajno uklanja račun knjižničara na temelju ID-a.
     * @param id Jedinstveni identifikator knjižničara.
     * @return {@code true} ako je brisanje izvršeno, {@code false} inače.
     */
    boolean delete(int id);

    /**
     * Ažurira postojeće podatke o knjižničaru (npr. lozinku ili administratorski status).
     * @param l Objekt s ažuriranim podacima i ispravnim identifikatorom.
     * @return {@code true} ako je ažuriranje uspjelo, {@code false} inače.
     */
    boolean update(Librarian l);
}