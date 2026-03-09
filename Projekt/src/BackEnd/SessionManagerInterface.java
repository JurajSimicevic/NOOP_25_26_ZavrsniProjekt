package BackEnd;

/**
 * Sučelje koje definira standardni ugovor za upravljanje korisničkom sesijom.
 * <p>
 * Osigurava metode za autentifikaciju (prijava), terminaciju sesije (odjava)
 * te provjeru razine autorizacije korisnika. Služi kao <b>sigurnosni sloj</b>
 * između pozadinske logike i grafičkog sučelja.
 * </p>
 * <b>Ključne metode:</b>
 * <ul>
 * <li> {@link #authenticate(String, String)}</li>
 * <li> {@link #getLoggedInUser()}</li>
 * <li> {@link #setLoggedInUser(Librarian)}</li>
 * <li> {@link #isCurrentUserAdmin()}</li>
 * <li> {@link #logout()}</li>
 * </ul>
 * <b>Glavne funkcionalnosti:</b>
 * <ul>
 * <li><b>Pristup podacima:</b> Dohvat objekta trenutno prijavljenog korisnika.</li>
 * <li><b>Kontrola ovlasti:</b> Provjera administratorskih prava za dinamičku modifikaciju GUI elemenata.</li>
 * <li><b>Sigurnosni životni ciklus:</b> Upravljanje početkom i završetkom korisničkog rada.</li>
 * </ul>
 */
public interface SessionManagerInterface {


    /**
     * Provjerava vjerodajnice korisnika i inicijalizira sesiju.
     * @param username Korisničko ime uneseno u Login prozor.
     * @param password Lozinka unesena u Login prozor.
     * @return {@code true} ako su podaci točni i sesija je uspješno započeta.
     */
    boolean authenticate(String username, String password);

    /**
     * Dohvaća referencu na korisnika koji je trenutno prijavljen u sustav.
     * @return Objekt koji nasljeđuje klasu {@link User}, ili null ako nitko nije prijavljen.
     */
    User getLoggedInUser();


    /**
     * Metoda za developer testing.
     */
    void setLoggedInUser(Librarian user);

    /**
     * Provjerava ima li trenutni korisnik prava administratora.
     * Ova metoda je ključna za dinamičko prilagođavanje GUI-ja (prikaz/skrivanje opcija).
     * @return {@code true} ako prijavljeni korisnik ima admin ovlasti.
     */
    boolean isCurrentUserAdmin();

    /**
     * Prekida trenutnu sesiju i briše podatke o prijavljenom korisniku.
     * Nakon poziva ove metode, sustav bi se trebao vratiti na ekran za prijavu.
     */
    void logout();
}
