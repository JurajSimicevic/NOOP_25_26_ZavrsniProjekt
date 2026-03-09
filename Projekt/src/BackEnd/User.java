package BackEnd;

import java.io.Serializable;

/**
 * Apstraktna osnovna klasa koja definira zajednički identitet svih korisnika u sustavu.
 * <p>
 * Dizajnirana je kao korijenski entitet unutar hijerarhije nasljeđivanja. Budući da je
 * {@code abstract}, sprječava izravno instanciranje općenitih korisnika, čime osigurava
 * da svaki korisnik u sustavu mora biti strogo definiran kao {@link Customer} ili {@link Librarian}.
 * </p>
 * <b>Arhitektonske značajke:</b>
 * <ul>
 * <li><b>Zajedničko stanje:</b> Objedinjuje demografske podatke poput imena, adrese i starosti
 * kako bi se izbjeglo dupliciranje koda (DRY princip - <i>Don't Repeat Yourself</i>).</li>
 * <li><b>Identifikacija:</b> Čuva jedinstveni {@code id} koji služi kao primarni ključ za sinkronizaciju s bazom podataka.</li>
 * <li><b>Perzistencija:</b> Implementira {@link Serializable} za serijalizaciju stanja objekta u tokove podataka.</li>
 * </ul>
 */
public abstract class User implements Serializable {

    /** Jedinstveni identifikator korisnika (Primary Key iz baze podataka). */
    protected int id;

    /** Ime korisnika. */
    protected String ime;

    /** Prezime korisnika. */
    protected String prezime;

    /** Broj godina korisnika. */
    protected int dob;

    /** Grad u kojem korisnik prebiva. */
    protected String grad;

    /** Puna adresa stanovanja korisnika. */
    protected String adresaStanovanja;

    /**
     * Zaštićeni konstruktor koji pozivaju podklase:
     * <ul>
     * <li>{@link Customer}</li>
     * <li>{@link Librarian}</li>
     * </ul>
     * @param ime              Ime korisnika.
     * @param prezime          Prezime korisnika.
     * @param dob              Starost korisnika.
     * @param grad             Mjesto stanovanja.
     * @param adresaStanovanja Ulica i kućni broj.
     */
    protected User(String ime, String prezime, int dob, String grad, String adresaStanovanja) {
        this.ime = ime;
        this.prezime = prezime;
        this.dob = dob;
        this.grad = grad;
        this.adresaStanovanja = adresaStanovanja;
    }

    /**
     * Vraća {@link #id}
     * @return {@link #id}*/
    public int getId() {
        return id;
    }

    /** Postavlja {@link #id} ovog objekta
     * @param id    novi id
     * */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vraća {@link #ime}
     * @return {@link #ime}*/
    public String getIme() {
        return ime;
    }

    /**
     * Vraća {@link #prezime}
     * @return {@link #prezime}*/
    public String getPrezime() {
        return prezime;
    }

    /**
     * Vraća {@link #dob}
     * @return {@link #dob}*/
    public int getDob() {
        return dob;
    }

    /**
     * Vraća {@link #grad}
     * @return {@link #grad}*/
    public String getGrad() {
        return grad;
    }

    /**
     * Vraća {@link #adresaStanovanja}
     * @return {@link #adresaStanovanja}*/
    public String getAdresaStanovanja() {
        return adresaStanovanja;
    }
}
