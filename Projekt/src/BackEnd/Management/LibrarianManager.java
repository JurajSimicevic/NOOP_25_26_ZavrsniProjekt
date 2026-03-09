package BackEnd.Management;

import BackEnd.DataAccessObject.IntLibrarianDAO;
import BackEnd.DataAccessObject.LibrarianDAO;
import BackEnd.DatabaseConnectionManager;
import BackEnd.Librarian;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacija managera za knjižničare. Koristi Singleton uzorak.
 * Podaci se sinkroniziraju između liste u RAM-u i baze podataka putem DAO sloja.
 */
public class LibrarianManager implements LibrarianManagerInt{

    /** Lista {@link Librarian} koja čuva podatke u radnoj memoriji aplikacije. */
    private List<Librarian> librarians;

    IntLibrarianDAO lDao;

    /** Trenutni tekstualni filteri za pretragu liste "librarians" unutar GUI-ja. */
    private String librarianFilter = "";

    private LibrarianManager() {
        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            lDao = LibrarianDAO.getInstance();
            librarians = lDao.getAll();
        }
    }

    /** Bill Pugh Singleton holder. */
    private static class Holder {
        private static final LibrarianManager INSTANCE = new LibrarianManager();
    }

    /** @return Jedinstvena instanca managera. */
    public static LibrarianManagerInt getInstance() {
        return Holder.INSTANCE;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link LibrarianDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean addLibrarian(Librarian l) {
        // 1. Ako je dodavanje u bazu uspješno:
        if (lDao.insert(l)) {
            // 2. Dodaj ga i u radnu memoriju
            librarians.add(l);
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link LibrarianDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean deleteLibrarian(Librarian l) {
        // 1. Ako je brisanje knjižničara iz baze uspješno:
        if (lDao.delete(l.getId())) {
            // 2. I ako je brisanje knjižničara iz radne memorije uspješno:
            return librarians.removeIf(librarian -> librarian.getId() == l.getId());
            }
        return false;
    }

    @Override
    public List<Librarian> getLibrarians() {
        return librarians;
    }

    @Override
    public void setFilter(String filter) {
        this.librarianFilter = filter;
    }

    @Override
    public String getFilter() {
        return librarianFilter;
    }

    @Override
    public List<Librarian> getFilteredList(String text) {
        if (text == null || text.isEmpty()) {
            return getLibrarians(); // Vraća originalnu listu svih knjiga
        }

        String filter = text.toLowerCase();
        List<Librarian> filtrirane = new ArrayList<>();

        for (Librarian l : librarians) {
            if (l.getUsername().toLowerCase().contains(filter) ||
                    l.getIme().toLowerCase().contains(filter) ||
                    l.getPrezime().toLowerCase().contains(filter)) {
                filtrirane.add(l);
            }
        }
        return filtrirane;
    }
}
