package MainFrameComps;

import GUI_Comps.ActiveBasePanel;
import BackEnd.SessionManager;
import Commands.ActionCommandsEnum;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Alatna traka (ToolBar) smještena na dnu glavnog prozora aplikacije.
 * <p>
 * Služi kao centralno čvorište za brzi pristup ključnim modulima sustava:
 * dodavanje i povrat knjiga, registracija kupaca te administracija zaposlenika.
 * </p>
 * <b>Funkcionalnosti:</b>
 * <ul>
 * <li><b>RBAC (Role-Based Access Control):</b> Gumb za upravljanje knjižničarima je
 * omogućen isključivo korisnicima s administratorskim pravima.</li>
 * <li><b>Standardizacija:</b> Nasljeđuje {@link ActiveBasePanel} osiguravajući
 * dosljedan životni ciklus komponente.</li>
 * <li><b>Callback komunikacija:</b> Koristi {@link ActionCommandListener} za
 * prosljeđivanje korisničkih akcija prema {@link MainFrame}-u.</li>
 * </ul>
 */
public class ToolBar extends ActiveBasePanel implements ActionListener {

    private JButton addButton;
    private JButton returnButton;
    private JButton registerButton;
    private JButton manageLibrariansButton;
    private ActionCommandListener toolBarListener;

    /**
     * Konstruktor klase {@code ToolBar}.
     * <p>
     * Definira preferiranu visinu panela na 200px. Sve ostale faze izgradnje
     * (inicijalizacija, raspored, aktivacija) automatizirane su kroz baznu klasu.
     * </p>
     */
    public ToolBar() {
        Dimension dims = getPreferredSize();
        dims.height = 200;
        setSize(dims);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Instancira gumbe s opisnim tekstovima za svaku od ključnih akcija sustava.
     * </p>
     */
    @Override
    protected void initComps() {
        addButton = new JButton("Dodaj/Izbriši knjigu");
        returnButton = new JButton("Vrati knjigu");
        registerButton = new JButton("Registriraj novog kupca");
        manageLibrariansButton = new JButton("Upravljanje knjižničarima");
        manageLibrariansButton.setEnabled(false);
        checkAdminRights();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Postavlja gumbe horizontalno koristeći standardni {@link FlowLayout}.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new GridLayout(1, 0, 5, 0));

        add(addButton);
        add(returnButton);
        add(registerButton);
        add(manageLibrariansButton);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dodjeljuje {@link ActionCommandsEnum} svakom gumbu i provjerava
     * prava pristupa trenutno ulogiranog korisnika.
     * </p>
     */
    @Override
    protected void activateComps(){
        addButton.setActionCommand(ActionCommandsEnum.OPEN_ADD_WINDOW.toString());
        addButton.addActionListener(this);

        returnButton.setActionCommand(ActionCommandsEnum.OPEN_RETURN_WINDOW.toString());
        returnButton.addActionListener(this);

        registerButton.setActionCommand(ActionCommandsEnum.OPEN_REGISTER_WINDOW.toString());
        registerButton.addActionListener(this);

        manageLibrariansButton.setActionCommand(ActionCommandsEnum.OPEN_MANAGE_LIBRARIANS_WINDOW.toString());
        manageLibrariansButton.addActionListener(this);

    }

    /**
     * Provjerava ulogu trenutnog korisnika putem {@link SessionManager}-a.
     * <p>
     * Ako korisnik ima administratorske ovlasti, gumb za upravljanje knjižničarima
     * postaje aktivan.
     * </p>
     */
    private void checkAdminRights() {
        if (SessionManager.getInstance().isCurrentUserAdmin()) {
            manageLibrariansButton.setEnabled(true);
        }
    }

    /**
     * Hvata događaje pritiska gumba na alatnoj traci.
     * <p>
     * Prosljeđuje akcijsku naredbu (ActionCommand) registriranom listeneru.
     * Ako listener nije definiran, prikazuje upozorenje korisniku.
     * </p>
     * @param ae Objekt događaja koji sadrži informaciju o izvoru akcije.
     */
    public void actionPerformed(ActionEvent ae) {
        if (toolBarListener != null) {
            toolBarListener.eventOccurred(ActionCommandsEnum.valueOf(ae.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(ToolBar.this, "MainFrameComps.ToolBar listener is not defined!", "MainFrameComps.ToolBar Listener Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Postavlja slušač događaja za komunikaciju s glavnim okvirom.
     * @param lst Implementacija {@link ActionCommandListener} sučelja.
     */
    public void setToolBarListener(ActionCommandListener lst) {
        this.toolBarListener = lst;
    }
}
