package ObserversAndOtherComps;

/**
 * Sučelje unutar <i>Observer</i> uzorka koje definira objekte koji "slušaju" promjene.
 * <p>
 * Svaka klasa koja želi automatski reagirati na promjene u bazi podataka ili stanju
 * aplikacije (poput {@link ViewPanel}) mora implementirati ovo sučelje.
 * </p>
 * <b>Uloga:</b>
 * <p>Djeluje kao reaktivna komponenta koja čeka signal od {@link Observable} subjekta
 * kako bi izvršila sinkronizaciju prikaza s najnovijim podacima.</p>
 */
public interface Observer {

    /**
     * Metoda koja se poziva od strane subjekta kada se detektira promjena stanja
     */
    void update();
}
