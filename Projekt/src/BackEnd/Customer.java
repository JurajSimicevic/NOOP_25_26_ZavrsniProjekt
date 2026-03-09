package BackEnd;

import ObserversAndOtherComps.CustomerViewPanel;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa modela koja predstavlja krajnjeg korisnika knjižnice (član knjižnice).
 * <p>
 * Nasljeđuje baznu klasu {@link User} čime dobiva osnovne atribute poput imena i prezimena,
 * dok interno upravlja kolekcijom posuđenih naslova.
 * </p>
 * <b>Ključne odgovornosti:</b>
 * <ul>
 * <li><b>Identifikacija:</b> Čuva jedinstvene podatke specifične za klijenta knjižnice.</li>
 * <li><b>Evidencija posudbi:</b> Održava {@link List} objekata {@link Knjiga} koje korisnik trenutno posjeduje.</li>
 * </ul>
 * <b>Struktura podataka:</b>
 * <ul>
 * <li>Nasljeđuje: {@code User} (osnovni podaci).</li>
 * <li>Sadrži: {@code ArrayList<Knjiga>} (aktivna zaduženja).</li>
 * </ul>
 */
public class Customer extends User{

    /** Lista objekata klase Knjiga koje su trenutno kod korisnika. */
    private List<Knjiga> posudeneKnjige;

    /**Identifikator verzije za serijalizaciju.
     * <p>
     * Koristi se ako se objekti spremaju u binarne datoteke.
     * </p>
     */
    @Serial
    private static final long serialVersionUID = -4L;

    /**
     * Konstruktor za stvaranje novog člana knjižnice.
     * <p>
     * Poziva super-konstruktor roditeljske klase User i inicijalizira praznu listu knjiga.
     * </p>
     */
    public Customer(String ime, String prezime, int dob, String grad, String adresaStanovanja) {
        super(ime, prezime, dob, grad, adresaStanovanja);
        posudeneKnjige = new ArrayList<>();
    }

    /**
     * @return {@link #posudeneKnjige}.
     */
    public List<Knjiga> getPosudeneKnjige() {
        return posudeneKnjige;
    }

    /**
     * Dodaje knjigu u internu listu posuđenih knjiga korisnika.
     * @param knjiga Objekt knjige koji se dodaje.
     */
    public void posudiKnjigu(Knjiga knjiga) {
        this.posudeneKnjige.add(knjiga);
    }

    /**
     * Uklanja knjigu iz liste posuđenih knjiga (razduživanje).
     * @param knjiga Objekt knjige koji korisnik vraća.
     */
    public void vratiKnjigu(Knjiga knjiga) {
        this.posudeneKnjige.remove(knjiga);
    }

    /**
     *{@inheritDoc}
     * @return "Ime Prezime (ID)"
     */
    @Override
    public String toString() {
        return ime + " " + prezime + " (" + id + ")";
    }
}
