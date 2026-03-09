package ObserversAndOtherComps;

/**
 * Sučelje koje definira <b>Subjekt</b> (promatrani objekt) unutar <i>Observer</i> uzorka.
 * <p>
 * Osigurava mehanizam za uspostavu komunikacije između poslovne logike i grafičkog sučelja.
 * Omogućuje objektima koji implementiraju {@link Observer} sučelje da se pretplate na
 * obavijesti o promjenama u stanju sustava (npr. ažuriranje liste knjiga).
 * </p>
 * <b>Glavne metode i mehanizmi:</b>
 * <ul>
 * <li>{@link #addObserver(Observer)}</li>
 * <li>{@link #removeObserver(Observer)}</li>
 * <li>{@link #notifyObservers()}</li>
 * </ul>
 * <b>Prednosti primjene:</b>
 * <ul>
 * <li><b>Loose Coupling:</b> Subjekt ne mora znati detalje o tome tko ga promatra, već samo da oni implementiraju {@code Observer} sučelje.</li>
 * <li><b>Reaktivnost:</b> Grafičko sučelje se automatski osvježava čim se dogodi promjena u bazi podataka bez ručnog pozivanja metoda.</li>
 * </ul>
 */
public interface Observable {

    /**
     * Registrira novog promatrača u internu listu pretplatnika.
     * @param observer Objekt koji želi primati obavijesti o promjenama.
     */
    void addObserver(Observer observer);

    /**
     * Uklanja promatrača iz liste, zaustavljajući daljnje obavještavanje o promjenama.
     * @param observer Objekt koji se želi odjaviti s liste promatrača.
     */
    void removeObserver(Observer observer);

    /**
     * Obavještava sve registrirane promatrače da je došlo do promjene podataka.
     */
    void notifyObservers();
}
