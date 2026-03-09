package Commands;

/**
 * Enumeracija koja definira sve podržane akcije unutar GUI sučelja aplikacije.
 * <p>
 * Služi kao centralni rječnik za mapiranje korisničkih interakcija (klikovi na gumbe, prečaci)
 * s konkretnim naredbama za manipulaciju podacima i upravljanje prozorima.
 * </p>
 * <b>Arhitektonske prednosti:</b>
 * <ul>
 * <li><b>Type Safety:</b> Sprječava pogreške pri upisu (<i>typos</i>) koje su česte kod korištenja
 * običnih Stringova te omogućuje validaciju u fazi kompilacije.</li>
 * <li><b>Centralizacija:</b> Sve dostupne operacije sustava vidljive su na jednom mjestu,
 * što olakšava održavanje i proširivanje aplikacije.</li>
 * <li><b>Switch-case optimizacija:</b> Idealno za korištenje unutar {@code ActionListener}-a
 * za čisto usmjeravanje logike (<i>routing</i>).</li>
 * </ul>
 * <b>Primjeri konstanti:</b>
 * <ul>
 * <li>{@code ADD_BOOK}: Pokreće dijalog za unos nove knjige u sustav.</li>
 * <li>{@code DELETE}: Inicira proces uklanjanja (bilo knjiga, bilo netko od korisnika) uz prethodnu validaciju.</li>
 * <li>{@code OPEN_RETURN_WINDOW}: Otvara novi {@code ReturnBookWindow}</li>
 * </ul>
 */
public enum ActionCommandsEnum {

    // --- Naredbe za upravljanje prozorima (Navigacija) ---

    /** Otvara dijalog za unos nove knjige u sustav. */
    OPEN_ADD_WINDOW,

    /** Otvara prozor za proces povrata posuđene knjige. */
    OPEN_RETURN_WINDOW,

    /** Otvara formu za registraciju novog člana (Customer). */
    OPEN_REGISTER_WINDOW,

    /** Otvara administratorski panel za upravljanje zaposlenicima. */
    OPEN_MANAGE_LIBRARIANS_WINDOW,


    // --- Naredbe za manipulaciju podacima (CRUD & Poslovanje) ---

    /** Potvrđuje dodavanje novog entiteta u bazu. */
    ADD,

    /** Trajno uklanja odabrani entitet iz sustava. */
    DELETE,

    /** Pokreće proces izdavanja knjige korisniku. */
    BORROW_BOOK,

    /** Pokreće proceduru prodaje knjige. */
    BUY_BOOK,

    /** Završava proces povrata i razdužuje korisnika. */
    RETURN_BOOK,


    // --- Opće sustavske akcije ---

    /** Sprema unesene podatke iz registracijske forme. */
    REGISTER,

    /** Vraća korisnika na prethodni ekran ili zatvara trenutni dijalog. */
    BACK,

    /** Prekida trenutnu sesiju i vraća aplikaciju na login ekran. */
    LOGOUT,

    /** Gasi program. */
    EXIT,

}