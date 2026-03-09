package BackEnd.Management;

import BackEnd.Customer;
import BackEnd.DataAccessObject.*;
import BackEnd.DatabaseConnectionManager;
import BackEnd.Knjiga;
import BackEnd.Librarian;
import ObserversAndOtherComps.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Glavna upravljačka klasa poslovne logike aplikacije (<i>Service Layer / Controller</i>).
 * <p>
 * Implementira <b>Singleton uzorak</b> i djeluje kao centralni koordinator koji povezuje
 * <b>DAO sloj</b> (perzistencija) s <b>GUI slojem</b> (prezentacija). Ova klasa osigurava
 * integritet podataka i sinkronizaciju između baze i memorijskih lista.
 * </p>
 * <b>Ključne odgovornosti:</b>
 * <ul>
 * <li><b>Upravljanje entitetima:</b> Centralizirano čuvanje i sinkronizacija lista {@link Knjiga},
 * {@link Customer} i {@link Librarian}.</li>
 * <li><b>Relink podataka:</b> Logika koja nakon učitavanja iz baze povezuje objekte knjiga s njihovim
 * posuditeljima na temelju spremljenih ID-ova.</li>
 * <li><b>Posredovanje (Broker):</b> Prosljeđuje zahtjeve iz grafičkog sučelja prema odgovarajućim DAO
 * objektima i vraća rezultate.</li>
 * </ul>
 * <b>Dizajnerski obrasci:</b>
 * <ul>
 * <li><b>Singleton:</b> Osigurava da cijela aplikacija koristi istu instancu poslovne logike.</li>
 * <li><b>Facade:</b> Pruža jednostavno sučelje prema kompleksnom sustavu baze podataka.</li>
 * <li><b>Observer:</b> (Preko nasljeđivanja) Obavještava prozor o promjenama koje zahtijevaju osvježavanje prikaza.</li>
 * </ul>
 */
/**
 * Glavna upravljačka klasa sustava koja implementira <b>Facade (Pročelje)</b> i
 * <b>Singleton</b> dizajnerske obrasce.
 * <p>
 * Nakon refaktoriranja, ova klasa više ne upravlja izravno kolekcijama podataka.
 * Umjesto toga, ona služi kao centralna točka pristupa (Hub) koja delegira specifične
 * poslovne operacije specijaliziranim managerima:
 * <ul>
 * <li>{@link BookManagerInt} - za logiku knjiga i posudbi.</li>
 * <li>{@link CustomerManagerInt} - za upravljanje članovima.</li>
 * <li>{@link LibrarianManagerInt} - za upravljanje osobljem.</li>
 * </ul>
 * </p>
 * * <b>Ključne odgovornosti:</b>
 * <ul>
 * <li><b>Delegacija:</b> Prosljeđuje zahtjeve GUI sloja odgovarajućim managerima.</li>
 * <li><b>Sinkronizacija (Observer):</b> Upravlja listom promatrača i obavještava
 * grafičko sučelje o svim promjenama u podacima kako bi se osigurao konzistentan prikaz.</li>
 * <li><b>Objektno povezivanje (Relinking):</b> Prilikom inicijalizacije, povezuje
 * strane ključeve iz baze (ID-ove) sa stvarnim objektima u memoriji.</li>
 * </ul>
 * @author Juraj Šimičević
 * @version 2.0 (Refaktorirano s delegacijom)
 */
public class LibraryManager implements LibraryManagerInterface {

    /** Lista {@link Observer}-a (panela) koje treba obavijestiti o promjenama u podacima. */
    private final List<Observer> observers;

    // Specijalizirani manageri kojima se delegira posao
    private BookManagerInt bookManager;
    private LibrarianManagerInt librarianManager;
    private CustomerManagerInt customerManager;

    /**
     * Privatni konstruktor koji inicijalizira managere i pokreće relink proces.
     * Koristi se unutar Bill Pugh Singleton holdera.
     */
    private LibraryManager() {
        this.bookManager = BookManager.getInstance();
        this.librarianManager = LibrarianManager.getInstance();
        this.customerManager = CustomerManager.getInstance();

        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            // Relinking: povezivanje objekata knjiga s njihovim vlasnicima (Customerima)
            // jer baza vraća samo ID-ove, a nama u Javi trebaju prave reference.
            relinkBooks2Customers();
        }
        this.observers = new ArrayList<>();
    }

    /** Bill Pugh Singleton holder. */
    private static class Holder {
        private static final LibraryManager INSTANCE = new LibraryManager();
    }

    /** @return Jedinstvena instanca managera. */
    public static LibraryManagerInterface getInstance() {
        return Holder.INSTANCE;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookManagerInt} za obradu ove operacije</p>
     */
    @Override
    public boolean addBook(Knjiga k) {
        // 1. Ako je operacija uspješna:
        if (bookManager.addBook(k)) {
            // 2. Obavještavamo sve Observere da se osvježe
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookManagerInt} za obradu ove operacije</p>
     */
    @Override
    public boolean borrowBook(Knjiga k, Customer c, int days) {
        // 1. Ako je operacija uspješna:
        if (bookManager.borrowBook(k, c, days)) {
            // 2. Obavještavamo sve Observere da se osvježe.
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookManagerInt} za obradu ove operacije</p>
     */
    @Override
    public boolean returnBook(Knjiga k) {
        // 1. Ako je operacija uspješna:
        if (bookManager.returnBook(k)) {
            // 2. Obavještavamo sve Observere da se osvježe
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link BookManagerInt} za obradu ove operacije</p>
     */
    @Override
    public boolean removeBook(Knjiga k) {
        // 1. Ako je operacija uspješna:
        if (bookManager.removeBook(k)) {
            // 2. Obavještavamo sve Observere da se osvježe.
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * Vraća spremljenu listu knjiga
     */
    @Override
    public List<Knjiga> getKnjige() {
        return bookManager.getKnjige();
    }

    /** {@inheritDoc}
     * <p>Poziva {@link CustomerDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean addCustomer(Customer c) {
        // 1. Ako je dodavanje u bazu uspješno:
        if (customerManager.addCustomer(c)) {
            // 2. Obavještavamo sve Observere da se osvježe.
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link CustomerDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean deleteCustomer(Customer c) {
        // 1. Ako je operacija uspješna:
        if (customerManager.deleteCustomer(c)) {
            // 2. Obavještavamo sve Observere da se osvježe.
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * Vraća spremljenu listu članova knjižnice
     */
    @Override
    public List<Customer> getCustomers() {
        return customerManager.getCustomers();
    }

    /** {@inheritDoc}
     * <p>Poziva {@link LibrarianDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean addLibrarian(Librarian l) {
        // 1. Ako je operacija uspješna:
        if (librarianManager.addLibrarian(l)) {
            // 2. Obavještavamo sve Observere da se osvježe.
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link LibrarianDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean deleteLibrarian(Librarian l) {
        // 1. Ako je operacija uspješna:
        if (librarianManager.deleteLibrarian(l)) {
            // 2. Obavještavamo sve Observere da se osvježe.
            notifyObservers();
            return true;
        }
        return false;
    }

    /** {@inheritDoc}
     * Vraća spremljenu listu knjižničara.
     */
    @Override
    public List<Librarian> getLibrarians() {
        return librarianManager.getLibrarians();
    }

    /** {@inheritDoc} */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /** {@inheritDoc} */
    @Override
    public void removeObserver(Observer observer) {
        if(observers.contains(observer)) {
            observers.remove(observer);
        } else {
            System.out.println("Error - Observer to remove does not exist!");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    /** {@inheritDoc} */
    @Override
    public void setBookFilter(String filter) {
        bookManager.setFilter(filter);
        notifyObservers(); // Ovo će natjerati ViewPanel da se osvježi!
    }

    /** {@inheritDoc} */
    @Override
    public void setCustomerFilter(String filter) {
        customerManager.setFilter(filter);
        notifyObservers();
    }

    /** {@inheritDoc} */
    @Override
    public void setLibrarianFilter(String filter) {
        librarianManager.setFilter(filter);
        notifyObservers();
    }

    /** {@inheritDoc} */
    @Override
    public String getBookFilter() {
        return bookManager.getFilter();
    }

    /** {@inheritDoc} */
    @Override
    public String getCustomerFilter() {
        return customerManager.getFilter();
    }

    /** {@inheritDoc} */
    @Override
    public String getLibrarianFilter() {
        return librarianManager.getFilter();
    }

    /** {@inheritDoc} */
    @Override
    public List<Knjiga> getFilteredBooks(String text) {
        return bookManager.getFilteredList(text);
    }

    @Override
    public List<Customer> getFilteredCustomers(String text) {
        return customerManager.getFilteredList(text);
    }

    @Override
    public List<Librarian> getFilteredLibrarians(String text) {
        return librarianManager.getFilteredList(text);
    }

    /**
     * <b>Ključna metoda:</b> Povezuje objekte u memoriji nakon čitanja iz baze.
     * <p>
     * <b>Relinking proces:</b>
     * Baza podataka je relacijska (ima ID-ove), a Java je objektna (treba reference).
     * Budući da DAO sloj iz baze dohvaća samo ID korisnika koji je posudio knjigu,
     * ova metoda prolazi kroz sve knjige i na temelju tog ID-a dodjeljuje im
     * stvarnu referencu na objekt {@link Customer} iz memorije.
     * Ovo rješava problem objektno-relacijskog jaza (impedance mismatch).
     * </p>
     */
    private void relinkBooks2Customers() {
        for (Knjiga k : getKnjige()) {
            int cid = k.getTmpCustomerId(); // ID pročitan iz baze

            if (cid != -1) {
                for (Customer c : getCustomers()) {
                    if (c.getId() == cid) {
                        k.setPosudioCustomer(c); // Poveži knjigu s kupcem
                        c.posudiKnjigu(k);   // Dodaj knjigu u listu kupca
                        break;
                    }
                }
            }
        }
    }
}
