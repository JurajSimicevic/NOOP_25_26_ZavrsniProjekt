package LibrarianManagementComps;

import GUI_Comps.ActiveBasePanel;
import Commands.*;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Donja upravljačka ploča (panel) s akcijskim gumbima za rad s knjižničarima.
 * <p>
 * Služi kao čista prezentacijska komponenta. Njezin jedini zadatak je prikupiti
 * korisnički klik i proslijediti ga putem {@link ActionCommandListener} sučelja
 * roditeljskom prozoru na daljnju obradu.
 * </p>
 * <b>Dizajnerske odluke:</b>
 * <ul>
 * <li><b>ActionCommand Mapping:</b> Svaki gumb je povezan s unikatnim Stringom iz
 * {@code ActionCommandsEnum} radi izbjegavanja "Magic Strings" u kodu.</li>
 * <li><b>Callback mehanizam:</b> Koristi se sučelje za komunikaciju prema gore,
 * što omogućuje ponovnu upotrebu panela u drugim prozorima.</li>
 * </ul>
 */
public class LibrarianManagementPanel extends ActiveBasePanel implements ActionListener {

    private JButton addBtn, deleteBtn, backBtn;
    private ActionCommandListener listener;

    /** Kontruktor za {@code LibrarianManagementPanel}*/
    public LibrarianManagementPanel() {
        // Postavljanje obruba radi boljeg vizualnog odvajanja od liste
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
    }

    /** {@inheritDoc} */
    @Override
    protected void initComps() {
        addBtn = new JButton("Dodaj novog");
        deleteBtn = new JButton("Izbriši odabranog");
        backBtn = new JButton("Natrag");
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>Gradi vizualni raspored gumba koristeći {@link FlowLayout}.</li>
     * <li>Postavlja vizualne obrube za jasno odvajanje od tabličnog prikaza.</li>
     * </ul>
     */
    @Override
    protected void layoutComps() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Razmak od 10px
        add(addBtn);
        add(deleteBtn);
        add(backBtn);
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>Povezuje fizičke gumbe s logičkim akcijama iz {@code ActionCommandsEnum}.</li>
     * <li>Registrira {@code this} kao ActionListener za interne klikove.</li>
     * </ul>
     */
    @Override
    protected void activateComps() {
        addBtn.setActionCommand(ActionCommandsEnum.ADD.toString());
        addBtn.addActionListener(this);

        deleteBtn.setActionCommand(ActionCommandsEnum.DELETE.toString());
        deleteBtn.addActionListener(this);

        backBtn.setActionCommand(ActionCommandsEnum.BACK.toString());
        backBtn.addActionListener(this);
    }

    /**
     * Postavlja callback listener.
     * @param listener Implementacija sučelja koja će obraditi događaje (obično LibrarianManagementWindow).
     */
    void setListener(ActionCommandListener listener) {
        this.listener = listener;
    }

    /**
     * Centralna metoda za hvatanje događaja unutar panela.
     * <ul>
     * <li>Presreće klik na bilo koji od tri gumba (Dodaj, Briši, Natrag).</li>
     * <li>Delegira obradu registriranom {@link ActionCommandListener}-u (u ovom slučaju {@link LibrarianManagementWindow}).</li>
     * </ul>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            listener.eventOccurred(ActionCommandsEnum.valueOf(e.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(null, "Listener is not defined!", " Listener Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
