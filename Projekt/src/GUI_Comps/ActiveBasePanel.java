package GUI_Comps;

/**
 * Specijalizirana apstraktna klasa za interaktivne panele.
 * <p>
 * Proširuje {@link BasePanel} dodavanjem treće faze životnog ciklusa — aktivacije.
 * Namijenjena je panelima koji sadrže gumbe, polja za unos ili druge elemente
 * koji zahtijevaju {@code ActionListener}-e ili {@code KeyListener}-e.
 * </p>
 * <b>Nasljeđivanje:</b>
 * <ul>
 * <li>Nasljeđuje {@code initComps()} i {@code layoutComps()} iz baze.</li>
 * <li>Uvodi {@code activateComps()} za povezivanje poslovne logike s GUI-jem.</li>
 * </ul>
 */
public abstract class ActiveBasePanel extends BasePanel{

    /**
     * Konstruktor koji nadograđuje bazu i pokreće aktivaciju komponenti.
     * <p>
     * Zbog {@code super()} poziva, redoslijed izvršavanja je uvijek:
     * Inicijalizacija &rarr; Raspored &rarr; Aktivacija.
     * </p>
     */
    protected ActiveBasePanel() {
        super();
        activateComps();
    }

    /** Aktivira  komponente panela*/
    protected abstract void activateComps();
}
