package BackEnd.FactoryComps;

import BackEnd.Librarian;
import BackEnd.User;

/**
 * Konkretna tvornica za kreiranje objekata tipa {@link Librarian}.
 * <p>
 * Ova klasa implementira sučelje {@link UserFactory} i koristi <b>Singleton</b> dizajn
 * uzorak kako bi osigurala jedinstvenu točku stvaranja zaposlenika u sustavu.
 * </p>
 * <b>Specifičnost:</b>
 * <p>Budući da knjižničar zahtijeva podatke za autentifikaciju koji nisu dio
 * osnovnog {@code UserFactory} sučelja, ova tvornica uvodi metodu {@code setCredentials}
 * za privremeno pohranjivanje podataka prije samog čina kreiranja objekta.</p>
 */
public class LibrarianFactory implements UserFactory {

    // Dodatna polja jer knjižničar treba login podatke
    private static LibrarianFactory instance;
    private String username, password;

    /**
     * Privatni konstruktor koji sprječava instanciranje izvan klase.
     */
    private LibrarianFactory() {}

    /**
     * Vraća jedinstvenu i nitno sigurnu (thread-safe) instancu tvornice.
     * @return instanca {@code LibrarianFactory}.
     */
    public static synchronized LibrarianFactory getInstance() {
        if (instance == null) {
            instance = new LibrarianFactory();
        }
        return instance;
    }

    /**
     * Postavlja login podatke koji će biti dodijeljeni sljedećem kreiranom knjižničaru.
     * <p>Ovu metodu je potrebno pozvati neposredno prije {@link #createUser} metode.</p>
     * @param username Korisničko ime za pristup sustavu.
     * @param password Zaporka za pristup sustavu.
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * {@inheritDoc}
     * <p>Stvara novu instancu klase {@link Librarian} koristeći prethodno
     * postavljene vjerodajnice i proslijeđene osobne podatke.</p>
     * @return Novoizgrađeni objekt {@code Librarian} (upakiran kao {@link User}).
     */
    @Override
    public User createUser(String ime, String prezime, int dob, String grad, String adresa) {
        return new Librarian(username, password, ime, prezime, dob, grad, adresa);
    }
}