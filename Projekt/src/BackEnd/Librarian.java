package BackEnd;

import java.io.Serial;

/**
 * Klasa modela koja predstavlja knjižničara (zaposlenika sustava).
 * <p>
 * Nasljeđuje baznu klasu {@link User} čime dobiva osnovne demografske atribute,
 * dok interno proširuje funkcionalnost podacima potrebnim za autentifikaciju
 * i autorizaciju unutar aplikacije.
 * </p>
 * <b>Sigurnost i autentifikacija:</b>
 * <ul>
 * <li><b>Korisnički podaci:</b> {@code username} i {@code password} koji omogućuju pristup sustavu.</li>
 * <li><b>Autorizacija:</b> {@code isAdmin} flag koji definira razinu privilegija (npr. upravljanje drugim knjižničarima).</li>
 * </ul>
 * <b>Arhitektonska uloga:</b>
 * <ul>
 * <li><b>Identitet:</b> Služi kao aktivni subjekt koji vrši promjene u katalogu ili nad korisnicima.</li>
 * <li><b>Nasljeđivanje:</b> Koristi <i>Reusability</i> princip preuzimanjem polja poput imena i adrese iz klase {@code User}.</li>
 * </ul>
 */
public class Librarian extends User {

    /** Korisničko ime knjižničara koje se koristi za prijavu u sustav. */
    private String username;

    /** Lozinka knjižničara u običnom tekstualnom formatu. */
    private String password;

    /** Zastavica koja određuje ima li knjižničar administrativne ovlasti (npr. dodavanje novih zaposlenika). */
    private boolean isAdmin;

    /** Identifikator verzije serijalizacije za očuvanje integriteta objekta pri spremanju. */
    @Serial
    private static final long serialVersionUID = -5L;

    /**
     * Konstruktor za kreiranje običnog knjižničara (bez admin prava po defaultu).
     * @param username         Korisničko ime za login.
     * @param password         Lozinka za login.
     * @param ime              Ime zaposlenika (naslijeđeno iz User).
     * @param prezime          Prezime zaposlenika (naslijeđeno iz User).
     * @param dob              Starost zaposlenika (naslijeđeno iz User).
     * @param grad             Grad stanovanja (naslijeđeno iz User).
     * @param adresaStanovanja Ulica i broj (naslijeđeno iz User).
     */
    public Librarian(String username, String password, String ime, String prezime, int dob, String grad, String adresaStanovanja){
        super(ime, prezime, dob, grad, adresaStanovanja);
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    /**
     * Prošireni konstruktor koji omogućuje ručno postavljanje administratorskih prava.
     * @param username         Korisničko ime za login.
     * @param password         Lozinka za login.
     * @param ime              Ime zaposlenika.
     * @param prezime          Prezime zaposlenika.
     * @param dob              Starost zaposlenika.
     * @param grad             Grad stanovanja.
     * @param adresaStanovanja Ulica i broj.
     * @param isAdmin          Logička vrijednost za administratorska prava.
     */
    public Librarian(String username, String password, String ime, String prezime, int dob, String grad, String adresaStanovanja, boolean isAdmin) {
        this(username, password, ime, prezime, dob, grad, adresaStanovanja);
        this.isAdmin = isAdmin;
    }

    /**
     * Vraća {@link #username}
     * @return {@code String} korisničko ime ovog objekta */
    public String getUsername() { return username; }

    /**
     * Vraća {@link #password}
     * @return {@code String} lozinka ovog objekta */
    public String getPassword() { return password; }

    /**
     * Provjerava ima li zaposlenik admin privilegije.
     * @return {@code true} ako je admin, {@code false} ako je obični korisnik.
     */
    public boolean isAdmin() { return isAdmin; }

    /**
     * Vraća formatirani tekstualni prikaz knjižničara.
     * Ova metoda se primarno koristi za prikaz u JListama unutar administratorskog panela.
     * @return "ID: x | Username: y | Ime i prezime: z..."
     */
    @Override
    public String toString() {
        return "ID: " + id + " | Username: " + username + " | Ime i prezime: " + ime + " " + prezime;
    }
}