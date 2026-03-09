package ReturnBookComps;

import GUI_Comps.ActiveBasePanel;
import Commands.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panel s formom za unos podataka pri vraćanju knjige.
 * Sadrži opcije za označavanje kašnjenja i oštećenja knjige te gumbe za potvrdu akcije.
 */
public class ReturnBookFormPanel extends ActiveBasePanel implements ActionListener {

    private JCheckBox kasnjenjeCheckBox;
    private JCheckBox stetaCheckBox;
    private JButton vratiButton;
    private JButton natragButton;
    private ReturnBookFormListener returnBookFormListener;

    /**
     * Konstruktor za klasu `ReturnBookComps.ReturnBookFormPanel`.
     * Inicijalizira i postavlja komponente.
     */
    public ReturnBookFormPanel() {}

    /**
     * Inicijalizira GUI komponente na panelu.
     */
    @Override
    protected void initComps() {
        kasnjenjeCheckBox = new JCheckBox("Kašnjenje");
        stetaCheckBox = new JCheckBox("Šteta");
        vratiButton = new JButton("Vrati");
        natragButton = new JButton("Natrag");
    }

    /**
     * Postavlja raspored panela.
     */
    @Override
    protected void layoutComps() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(kasnjenjeCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(stetaCheckBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 200, 5, 5);
        add(vratiButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(natragButton, gbc);
    }

    /**
     * Aktivira slušače događaja za prozor za vraćanje knjiga.
     */
    @Override
    protected void activateComps() {
        vratiButton.setActionCommand(ActionCommandsEnum.RETURN_BOOK.toString());
        natragButton.setActionCommand(ActionCommandsEnum.BACK.toString());

        vratiButton.addActionListener(this);
        natragButton.addActionListener(this);

    }

    public JCheckBox getKasnjenjeCheckBox() {
        return kasnjenjeCheckBox;
    }

    public JCheckBox getStetaCheckBox() {
        return stetaCheckBox;
    }

    /**
     * Postavlja slušač događaja za gumbe na ovom panelu.
     *
     * @param lst Slušač događaja.
     */
    public void setReturnBookFormListener(ReturnBookFormListener lst) {
        this.returnBookFormListener = lst;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        returnBookFormListener.returnBookFormEventOccured(ae.getActionCommand(), kasnjenjeCheckBox.isSelected(), stetaCheckBox.isSelected());
    }
}
