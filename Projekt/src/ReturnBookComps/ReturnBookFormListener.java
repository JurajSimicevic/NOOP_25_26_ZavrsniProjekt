package ReturnBookComps;

import java.util.EventListener;

/**
 * Specijalizirano sučelje za osluškivanje događaja unutar forme za povrat knjiga.
 * <p>
 * Za razliku od standardnih listenera, {@code ReturnBookFormListener} omogućuje
 * prijenos dodatnih metapodataka o stanju vraćene knjige (kašnjenje, oštećenje)
 * direktno pri okidanju događaja.
 * </p>
 * <b>Uloga u sustavu:</b>
 * <p>Povezuje {@link ReturnBookFormPanel} s logikom za izvršavanje naredbi,
 * omogućujući precizan obračun penala pri povratu.</p>
 */
public interface ReturnBookFormListener extends EventListener {

    /**
     * Poziva se kada korisnik potvrdi povrat knjige.
     * @param actionCommand String identifikator gumba (npr. "RETURN").
     * @param hasLateFee    Indikator je li označena kvačica za zakasninu.
     * @param hasDamageFee  Indikator je li označena kvačica za fizičko oštećenje knjige.
     */
    void returnBookFormEventOccured(String actionCommand, boolean hasLateFee, boolean hasDamageFee);

}
