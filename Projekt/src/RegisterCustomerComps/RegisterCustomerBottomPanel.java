package RegisterCustomerComps;

import GUI_Comps.ActiveBasePanel;
import BackEnd.FilePathEnum;
import Commands.ActionCommandsEnum;
import ObserversAndOtherComps.SearchField;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Donji kontrolni panel unutar sučelja za registraciju kupaca.
 * Ovaj panel služi kao sekundarna traka s alatima koja korisniku omogućuje:
 * <ul>
 * <li><b>Pretragu:</b> Brzo filtriranje liste kupaca putem {@link SearchField}-a.</li>
 * <li><b>Brisanje:</b> Sigurno brisanje selektiranog člana iz baze podataka.</li>
 * </ul>
 * <b>Dizajn:</b>
 * <p>
 * Nasljeđuje {@link ActiveBasePanel}, što znači da se njegova aktivacija i iscrtavanje
 * događaju automatski prilikom instanciranja.
 * </p>
 */
public class RegisterCustomerBottomPanel extends ActiveBasePanel implements ActionListener {

    private JButton deleteBtn;
    private SearchField customerSearchField;
    private ActionCommandListener listener;


    RegisterCustomerBottomPanel() {}

    /**
     * {@inheritDoc}
     * <p>
     * Inicijalizira polje za pretragu specijalizirano za {@link FilePathEnum#CUSTOMERS}
     * te gumb za brisanje s uočljivom crvenom bojom pozadine.
     * </p>
     */
    @Override
    protected void initComps() {
        customerSearchField = new SearchField(FilePathEnum.CUSTOMERS, 15);

        deleteBtn = new JButton("Obriši člana");
        deleteBtn.setBackground(new Color(255, 150, 150));

    }

    /**
     * {@inheritDoc}
     * <p>
     * Postavlja komponente s lijevim poravnanjem unutar panela.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(customerSearchField);
        add(deleteBtn);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Povezuje gumb za brisanje s {@link ActionCommandsEnum#DELETE} naredbom.
     * </p>
     */
    @Override
    protected void activateComps() {
        deleteBtn.setActionCommand(ActionCommandsEnum.DELETE.toString());
        deleteBtn.addActionListener(this);

    }

    /**
     * Obrađuje klik na gumb za brisanje i prosljeđuje događaj listeneru.
     * <p>
     * Ako listener nije postavljen, prikazuje se {@link JOptionPane} s obavijesti o grešci.
     * </p>
     * @param e Akcijski događaj gumba.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(listener != null) {
            listener.eventOccurred(ActionCommandsEnum.valueOf(e.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(this, this.getClass().getSimpleName() + " listener not registered!", "Listener Error", JOptionPane.ERROR_MESSAGE );
        }
    }

    /**
     * Postavlja listener koji će obrađivati zahtjeve za brisanje (obično {@link RegisterCustomerWindow}).
     * @param listener Implementacija {@link ActionCommandListener} sučelja.
     */
    void setActionListener(ActionCommandListener listener) {
            this.listener = listener;
    }
}
