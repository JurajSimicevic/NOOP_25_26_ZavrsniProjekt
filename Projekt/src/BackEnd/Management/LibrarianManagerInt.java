package BackEnd.Management;

import BackEnd.Librarian;

import java.util.List;

/**
 * Krajnje sučelje za upravljanje računima knjižničara (osoblja).
 * Proširuje {@link Filterable} za podršku pretraživanja knjižničara.
 */
public interface LibrarianManagerInt extends Filterable<Librarian> {

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
