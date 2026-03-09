package BackEnd.Management;

import BackEnd.Customer;
import BackEnd.DataAccessObject.BookDAO;
import BackEnd.DataAccessObject.CustomerDAO;
import BackEnd.DataAccessObject.IntBookDAO;
import BackEnd.DataAccessObject.LibrarianDAO;
import BackEnd.DatabaseConnectionManager;
import BackEnd.Knjiga;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacija managera za knjige. Koristi Singleton uzorak.
 * Podaci se sinkroniziraju između liste u RAM-u i baze podataka putem DAO sloja.
 */
public class BookManager implements BookManagerInt{

    /** Lista {@link Knjiga} koja čuva podatke u radnoj memoriji aplikacije. */
    private List<Knjiga> knjige;

    IntBookDAO bDao;

    /** Trenutni tekstualni filteri za pretragu liste "knjige" unutar GUI-ja. */
    private String bookFilter = "";

    private BookManager() {
        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            bDao = BookDAO.getInstance();
            knjige = bDao.getAll();
        }
    }

    /** Bill Pugh Singleton holder. */
    private static class Holder {
        private static final BookManager INSTANCE = new BookManager();
    }

    /** @return Jedinstvena instanca managera. */
    public static BookManagerInt getInstance() {
        return Holder.INSTANCE;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean addBook(Knjiga k) {
        // 1. Ako je dodavanje u bazu uspješno:
        if (bDao.insert(k)) {

            // 2. Dodajemo je u radnu memoriju
            knjige.add(k);
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean borrowBook(Knjiga k, Customer c, int days) {
        // 1. Provjera je li "posuđivanje" knjige uspješno u bazi
        if (bDao.updateBorrowStatus(k, c.getId())) {

            // 2. Ako je, namjesti knjigu da je posuđena (na 'days' broj dana)
            k.posudi(days);

            // 3. Postavi knjizi kupca koji ju posuđuje
            k.setPosudioCustomer(c);

            // 4. Postavi kupcu da je posudio tu knjigu
            c.posudiKnjigu(k);

            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean returnBook(Knjiga k) {

        Customer c = k.getPosudioCustomer();

        // 1. Prvo ažuriraj bazu (šaljemo null za customerId)
        if (bDao.updateBorrowStatus(k, null)) {

            // 2. Makni knjigu iz kupčeve liste u memoriji
            c.vratiKnjigu(k);

            // 3. Resetiraj status knjige u memoriji (posudjena=false, customer=null)
            k.vrati();

            return true;
        }

        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean removeBook(Knjiga k) {
        // 1. Provjeravamo je li brisanje knjige iz baze uspješno
        if (bDao.delete(k.getIsbn())) {

            // 2. Ako je, brišemo je iz radne memorije.
            knjige.remove(k);

            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * Vraća spremljenu listu knjiga
     */
    @Override
    public List<Knjiga> getKnjige() {
        return knjige;
    }

    /** {@inheritDoc} */
    @Override
    public void setFilter(String filter) {
        this.bookFilter = filter;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilter() {
        return bookFilter;
    }

    /** {@inheritDoc} */
    @Override
    public List<Knjiga> getFilteredList(String text) {
        if (text == null || text.isEmpty()) {
            return getKnjige(); // Vraća originalnu listu svih knjiga
        }

        String filter = text.toLowerCase();
        List<Knjiga> filtrirane = new ArrayList<>();

        for (Knjiga k : knjige) {
            if (k.getNazivDjela().toLowerCase().contains(filter) ||
                    k.getAutor().toLowerCase().contains(filter)) {
                filtrirane.add(k);
            }
        }
        return filtrirane;
    }
}
