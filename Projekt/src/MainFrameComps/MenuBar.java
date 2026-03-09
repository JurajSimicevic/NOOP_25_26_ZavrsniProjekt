package MainFrameComps;

import BackEnd.DatabaseConnectionManager;
import Commands.ActionCommandsEnum;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;

/**
 * Glavna traka izbornika (Top Menu Bar) smještena na vrhu MainFrame-a.
 * <p>
 * Objedinjuje funkcije za upravljanje aplikacijom i statusom mrežne veze.
 * </p>
 * <b>Ključne značajke:</b>
 * <ul>
 * <li>Upravljanje sesijom (Odjava, Izlaz)</li>
 * <li>Kontrola veze s bazom putem {@link DatabaseConnectionManager}</li>
 * <li>Dinamičko osvježavanje sučelja ovisno o stanju konekcije</li>
 * </ul>
 */
public class MenuBar extends JMenuBar {

    private JButton logoutBtn;
    private JButton connectionBtn;
    private ActionCommandListener menuListener;

    /**
     * Konstruktor klase {@code MenuBar}.
     * <p>
     * Pokreće standardni proces izgradnje komponente kroz tri faze inicijalizacije.
     * </p>
     */
    public MenuBar() {
        initComps();
        layoutComps();
        activateComps();
    }

    /**
     * Inicijalizira stavke izbornika (JMenu i JMenuItem).
     * <p>
     * Postavlja tekstualne oznake za datotečni sustav i upravljanje serverom.
     * </p>
     */
    private void initComps() {
        // Koristimo gumbe unutar JMenuBar-a za "flat" izgled i direktan klik
        logoutBtn = new JButton("Odjava");
//        logoutBtn.setBorderPainted(false);
//        logoutBtn.setContentAreaFilled(false);
//        logoutBtn.setFocusPainted(false);

        connectionBtn = new JButton();
//        connectionBtn.setBorderPainted(false);
//        connectionBtn.setContentAreaFilled(false);
//        connectionBtn.setFocusPainted(false);

        updateConnectionStatus(); // Postavlja početni tekst za connectionBtn
    }

    /**
     * Raspoređuje stavke unutar izbornika i dodaje ih na glavnu traku.
     * <p>
     * Organizira elemente u logičke cjeline koristeći separatore za bolju preglednost.
     * </p>
     */
    private void layoutComps() {
        // Dodajemo gumbe izravno na traku
        add(logoutBtn);
        add(Box.createHorizontalStrut(10)); // Mali razmak
        add(connectionBtn);
    }

    /**
     * Aktivira funkcionalnost stavki izbornika postavljanjem osluškivača događaja.
     * <p>
     * Implementira izravne akcije poput {@code System.exit(0)} te delegira
     * kompleksnije akcije (odjava) prema {@link MainFrame}-u.
     * </p>
     */
    private void activateComps() {
        logoutBtn.addActionListener(e -> {
            if (menuListener != null) {
                menuListener.eventOccurred(ActionCommandsEnum.LOGOUT); //
            }
        });

        connectionBtn.addActionListener(e -> {
            boolean isAlive = DatabaseConnectionManager.getInstance().isConnectionAlive(); //
            if (isAlive) {
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Jeste li sigurni da se želite odspojiti sa servera?",
                        "Odspoji?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if(dialogResult == JOptionPane.YES_OPTION){
                    if(DatabaseConnectionManager.getInstance().disconnect()){
                        JOptionPane.showMessageDialog(null, "Odspojeno!", null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                if(DatabaseConnectionManager.getInstance().connect()){
                    JOptionPane.showMessageDialog(null, "Spojeno!", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
            updateConnectionStatus();
        });
    }

    /**
     * Ažurira vidljivost stavki unutar "Server" izbornika ovisno o trenutnom stanju veze.
     * <p>
     * Provjerava je li veza s bazom aktivna putem {@link DatabaseConnectionManager#isConnectionAlive()}
     * te na temelju toga prikazuje odgovarajuću opciju (Connect ili Disconnect).
     * </p>
     */
    private void updateConnectionStatus() {
        boolean isAlive = DatabaseConnectionManager.getInstance().isConnectionAlive(); //

        if (isAlive) {
            connectionBtn.setText("Odspoji se");
        } else {
            connectionBtn.setText("Poveži se");
        }

        revalidate();
        repaint(); //
    }

    /**
     * Postavlja listener koji omogućuje komunikaciju između izbornika i glavnog prozora.
     * @param listener Implementacija {@link ActionCommandListener} sučelja.
     */
    public void setMenuListener(ActionCommandListener listener) {
        this.menuListener = listener;
    }
}