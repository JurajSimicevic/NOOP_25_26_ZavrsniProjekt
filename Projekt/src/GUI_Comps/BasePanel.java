package GUI_Comps;

import javax.swing.*;

/**
 * Osnovna apstraktna klasa za sve panelne komponente {@link JPanel} u sustavu.
 * <p>
 * Služi kao temeljni predložak za vizualne komponente koje ne zahtijevaju
 * interaktivnu logiku ili osluškivače događaja (npr. paneli s čistim prikazom teksta).
 * </p>
 * <b>Dizajn:</b>
 * <ul>
 * <li>Osigurava da se inicijalizacija komponenti i postavljanje rasporeda
 * uvijek izvršavaju u ispravnom redoslijedu unutar konstruktora.</li>
 * </ul>
 */
public abstract class BasePanel extends JPanel {

    /**
     * Konstruktor koji automatizira proces izgradnje panela.
     * Poziva {@link #initComps()}, pa {@link #layoutComps()}.
     */
    protected BasePanel() {
        super();
        initComps();
        layoutComps();
    }

    /** Inicijalizira komponente panela*/
    protected abstract void initComps();

    /** Raspoređuje komponente panela*/
    protected abstract void layoutComps();
}
