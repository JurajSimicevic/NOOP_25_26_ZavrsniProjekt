package RegisterCustomerComps;

import Commands.ActionCommandsEnum;
import GUI_Comps.ActiveBasePanel;
import BackEnd.Customer;
import ObserversAndOtherComps.*;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Kompozitni panel koji objedinjuje pretragu, prikaz liste kupaca i opciju brisanja.
 * <p>
 * {@code ConjoinedPanel} služi kao centralni dio {@link RegisterCustomerWindow} sučelja.
 * On ugnježđuje {@link SearchField} za filtriranje i {@link CustomerViewPanel} za prikaz,
 * pružajući korisniku cjelovito iskustvo upravljanja podacima na jednom mjestu.
 * </p>
 * <b>Komponente:</b>
 * <ul>
 * <li>{@code SearchField} - Omogućuje pretragu kupaca po imenu ili prezimenu u realnom vremenu.</li>
 * <li>{@code CustomerViewPanel} - Prikazuje listu svih (ili filtriranih) kupaca iz baze.</li>
 * <li>{@code JButton} (Delete) - Omogućuje uklanjanje selektiranog korisnika.</li>
 * </ul>
 */
public class ConjoinedPanel extends ActiveBasePanel implements ActionListener {

    private CustomerViewPanel customerViewPanel;
    private RegisterCustomerBottomPanel registerCustomerBottomPanel;
    private ActionCommandListener listener;
    private ViewPanelListener<Customer> viewPanelListener;

    public ConjoinedPanel() {}

    /**
     * {@inheritDoc}
     * <p>
     * Inicijalizira specijalizirane komponente za rad s kupcima.
     * </p>
     */
    @Override
    protected void initComps() {
        customerViewPanel = new CustomerViewPanel();
        registerCustomerBottomPanel = new RegisterCustomerBottomPanel();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Slaganje komponenti u vertikalni stupac pomoću {@link BorderLayout} i {@link JPanel}-a.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        add(customerViewPanel, BorderLayout.CENTER);
        add(registerCustomerBottomPanel, BorderLayout.SOUTH);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Postavlja akcijsku naredbu za gumb za brisanje.
     * </p>
     */
    @Override
    protected void activateComps() {
        registerCustomerBottomPanel.setActionListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                listener.eventOccurred(actionCommand);
            }
        });

        customerViewPanel.getList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                viewPanelListener.eventOccurred(customerViewPanel.getSelectedValue());
            }
        });
    }

    /**
     * Postavlja listener za prosljeđivanje komandi (npr. DELETE) prema glavnom prozoru.
     * @param e Implementacija {@link ActionCommandListener} sučelja.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(listener != null) {
            listener.eventOccurred(ActionCommandsEnum.valueOf(e.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(this, this.getClass().getSimpleName() + " listener error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Postavlja listener za prosljeđivanje komandi (npr. DELETE) prema glavnom prozoru.
     * @param listener Implementacija {@link ActionCommandListener} sučelja.
     */
    void setConjoinedPanelListener(ActionCommandListener listener) {
        this.listener = listener;
    }

    /**
     * Omogućuje vanjskim klasama da slušaju promjenu selekcije u unutrašnjoj listi.
     * @param viewPanelListener Implementacija {@link ViewPanelListener} za objekte tipa {@link Customer}.
     */
    void setUserViewPanelListener(ViewPanelListener<Customer> viewPanelListener) {
        this.viewPanelListener = viewPanelListener;
    }
}
