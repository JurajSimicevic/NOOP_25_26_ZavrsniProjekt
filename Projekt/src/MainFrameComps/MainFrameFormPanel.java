package MainFrameComps;

import static Commands.ActionCommandsEnum.*;
import BackEnd.*;
import GUI_Comps.ActiveBasePanel;
import Commands.ActionCommandsEnum;
import GUI_Comps.EffectUtils;
import ObserversAndOtherComps.*;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;





/**
 * Bočni panel (East) glavnog prozora {@link MainFrame} namijenjen prikupljanju podataka za proces posudbe i kupnje.
 * <p>
 * Ova klasa nasljeđuje {@link ActiveBasePanel}, čime standardizira proces inicijalizacije,
 * rasporeda i aktivacije komponenti. Objedinjuje pretragu knjiga, odabir trajanja posudbe
 * te integrira {@link CustomerViewPanel} za odabir korisnika.
 * </p>
 * <b>Dizajn i arhitektura:</b>
 * <ul>
 * <li><b>Nasljeđivanje:</b> Koristi {@link ActiveBasePanel} za automatizaciju životnog ciklusa GUI-ja.</li>
 * <li><b>Kompozicija:</b> Uključuje {@link CustomerViewPanel} kao specijalizirani modul za prikaz kupaca.</li>
 * <li><b>Callback mehanizam:</b> Putem {@link ActionCommandListener}-a šalje signale o akcijama prema {@link MainFrame}-u.</li>
 * </ul>
 */
public class MainFrameFormPanel extends ActiveBasePanel implements ActionListener {

    private SearchField bookSearchField;
    private JRadioButton tridesetDana;
    private JRadioButton sezdesetDana;
    private SearchField customerSearchField;
    private CustomerViewPanel customerViewPanel;
    private JButton borrowButton;
    private JButton buyButton;
    private ActionCommandListener formPanelListener;


    /**
     * Konstruktor za {@code FormPanel}.
     * <p>
     * Inicijalizira vizualne postavke panela, primjenjuje složeni obrub (CompoundBorder) s naslovom.
     * </p>
     */
    public MainFrameFormPanel() {
        Border inner = BorderFactory.createEtchedBorder();
        Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border border = BorderFactory.createCompoundBorder(outer, inner);
        setBorder(border);
    }



    /**
     * {@inheritDoc}
     * <p>
     * Postavlja vizualni identitet panela (širinu i obrube) te inicijalizira
     * sve unutrašnje Swing komponente i grupe gumba.
     * </p>
     */
    @Override
    protected void initComps() {
        bookSearchField = new SearchField(FilePathEnum.KNJIGE, 30);
        ButtonGroup buttonGroup = new ButtonGroup();
        tridesetDana = new JRadioButton("Posudi na 30 Dana");
        sezdesetDana = new JRadioButton("Posudi na 60 Dana");
        buttonGroup.add(tridesetDana);
        buttonGroup.add(sezdesetDana);
        tridesetDana.setSelected(true);
        customerViewPanel = new CustomerViewPanel(100, 100);
        customerSearchField = new SearchField(FilePathEnum.CUSTOMERS, 15);
        borrowButton = new JButton("Posudi knjigu");
        buyButton = new JButton("Kupi knjigu");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Koristi {@link GridBagLayout} za precizno slaganje elemenata u stupac.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Red 0 & 1: Tražilica knjiga
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 5, 150);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(new JLabel("Traži:"), gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(bookSearchField, gbc);

        // Ispuna radi estetike
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(0, Short.MAX_VALUE)), gbc);

        // Separator za vizualno odvajanje sekcija
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        add(new JSeparator(), gbc);

        // Red 4 & 5: Opcije trajanja posudbe
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 150);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(tridesetDana, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 5, 150);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(sezdesetDana, gbc);

        // Red 6, 7 & 8: Sekcija kupca (Labela + Search + View)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(new JLabel("Ime korisnika:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 10, 5, 150);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(customerSearchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 10, 5, 150);
        gbc.weightx = 1.0; gbc.weighty = 1.0; // Uzima preostali prostor
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(customerViewPanel, gbc);

        // Red 9 & 10: Glavni akcijski gumbi
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        add(borrowButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        add(buyButton, gbc);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Povezuje vizualne gumbe s konstantama iz {@link ActionCommandsEnum}
     * i registrira ih na lokalni {@link ActionListener}.
     * </p>
     */
    @Override
    protected void activateComps() {

        borrowButton.setActionCommand(BORROW_BOOK.toString());
        borrowButton.addActionListener(this);

        buyButton.setActionCommand(BUY_BOOK.toString());
        buyButton.addActionListener(this);

    }

    /**
     * Vraća broj dana odabranih za posudbu knjige.
     * @return {@code 30} ili {@code 60} ovisno o selekciji radio gumba.
     */
    int getBrojDana(){
        if (tridesetDana.isSelected()) {
            return 30;
        } else {
            return 60;
        }
    }

    /**
     * Dohvaća trenutno selektiranog korisnika iz tablice/liste.
     * @return Objekt {@link Customer} koji je odabran u {@link CustomerViewPanel}-u.
     */
    Customer getCustomer() {
        return customerViewPanel.getSelectedValue();
    }

    /**
     * Postavlja slušača događaja koji omogućuje komunikaciju s kontrolerom (MainFrame).
     * @param formPanelListener Implementacija {@link ActionCommandListener} sučelja.
     */
    void setFormPanelListener(ActionCommandListener formPanelListener) {
        this.formPanelListener = formPanelListener;
    }

    /**
     * Obrađuje klikove na gumbe unutar panela i delegira informaciju vanjskom listeneru.
     * @param e Objekt akcijskog događaja.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(formPanelListener != null) {
            formPanelListener.eventOccurred(ActionCommandsEnum.valueOf(e.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(MainFrameFormPanel.this, "FormPanel listener is not defined!", "FormPanel Listener Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

}
