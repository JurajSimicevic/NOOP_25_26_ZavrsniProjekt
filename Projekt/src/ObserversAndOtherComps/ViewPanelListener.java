package ObserversAndOtherComps;

import java.util.EventListener;

/**
 * Generičko sučelje za osluškivanje specifičnih događaja na razini prikaznih panela.
 * <p>
 * Koristi <b>generics</b> kako bi omogućilo tipski sigurnu (type-safe) komunikaciju
 * između liste (View) i kontrolera (npr. prozora koji koristi taj panel).
 * </p>
 * <b>Primjena:</b>
 * <p>Kada korisnik klikne na element u listi, panel koristi ovo sučelje kako bi
 * proslijedio cijeli objekt (npr. {@code Knjiga} ili {@code Customer}) onome tko ga sluša.</p>
 *
 * @param <E> Tip objekta koji se prenosi putem događaja (npr. Knjiga, Korisnik).
 */
public interface ViewPanelListener<E> extends EventListener {

    /**
     * Poziva se kada se dogodi značajan događaj nad objektom u panelu.
     * @param user Objekt tipa {@code E} nad kojim je izvršena akcija ili selekcija.
     */
    void eventOccurred(E user);
}
