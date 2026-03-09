package AddBookComps;

import GUI_Comps.BasePanel;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

/**
 * Panel koji sadrži grafičko sučelje za unos podataka o novoj knjizi.
 * <p>
 * Glavna svrha panela je unos, te prosljeđivanje informacija .
 * </p>
 * <b>Sastavne komponente panela:</b>
 * <ul>
 * <li><b>Tekstualna polja:</b> Autor, naziv djela, ISBN i cijena.</li>
 * <li><b>Padajući izbornik:</b> {@link JComboBox} za odabir žanra ili vrste djela.</li>
 * <li><b>Layout:</b> Koristi {@link GridBagLayout} za precizno pozicioniranje.</li>
 * </ul>
 */
public class AddBookFormPanel extends BasePanel {

    /** Tekstualno polje za unos imena i prezimena autora. */
    private JTextField autorField;

    /** Tekstualno polje za unos naslova knjige. */
    private JTextField nazivField;

    /** Tekstualno polje za unos ISBN broja knjige. */
    private JTextField isbnField;

    /** Tekstualno polje za unos cijene knjige. */
    private JTextField cijenaField;

    /** Padajući izbornik za odabir žanra ili vrste djela */
    private JComboBox<String> vrstaComboBox;

    /** Model za {@link #vrstaComboBox} kojem su elementi vrijednosti iz {@link VrsteDjelaEnum} klase */
    private DefaultComboBoxModel<String> vrstaModel;

    /**
     * Konstruktor klase AddBookFormPanel.
     * <p>Postavlja dekorativni okvir s naslovom te inicijalizira i raspoređuje komponente.</p>
     */
    public AddBookFormPanel() {
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Podaci o knjizi",
                TitledBorder.LEFT, TitledBorder.TOP));
    }

    /**
     * {@inheritDoc}
     * <p>Postavlja početne veličine tekstualnih polja i definira opcije u padajućem izborniku.</p>
     */
    @Override
    protected void initComps() {
        autorField = new JTextField(20);
        nazivField = new JTextField(20);
        isbnField = new JTextField(20);
        cijenaField = new JTextField(10);

        vrstaComboBox = new JComboBox<>();
        vrstaModel = new DefaultComboBoxModel<>();
        setupVrstaModel();
        vrstaComboBox.setModel(vrstaModel);

        // Postavljamo na -1 kako bi početni prikaz bio prazan (bez odabrane stavke)
        vrstaComboBox.setSelectedIndex(-1);
    }

    /**
     * {@inheritDoc}
     * Definira raspored komponenti unutar panela koristeći {@link GridBagLayout}.
     * Labele i pripadajuća polja se slažu vertikalno jedno ispod drugog.
     */
    @Override
    protected void layoutComps() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labela i polje za Autora
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Autor:"), gbc);
        gbc.gridy = 1;
        add(autorField, gbc);

        // Labela i polje za Naziv
        gbc.gridy = 2;
        add(new JLabel("Naziv djela:"), gbc);
        gbc.gridy = 3;
        add(nazivField, gbc);

        // Labela i polje za ISBN
        gbc.gridy = 4;
        add(new JLabel("ISBN:"), gbc);
        gbc.gridy = 5;
        add(isbnField, gbc);

        // Labela i polje za Cijenu
        gbc.gridy = 6;
        add(new JLabel("Cijena:"), gbc);
        gbc.gridy = 7;
        gbc.fill = 0;
        add(cijenaField, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labela i polje za Vrstu
        gbc.gridy = 8;
        add(new JLabel("Vrsta djela:"), gbc);
        gbc.gridy = 9;
        add(vrstaComboBox, gbc);
    }

    /**
     * Dohvaća uneseno ime autora.
     * @return {@link String} s imenom autora, bez vodećih i pratećih razmaka.
     */
    String getAutor() { return autorField.getText().trim(); }

    /**
     * Dohvaća uneseni naziv djela.
     * @return {@link String} s nazivom djela, bez vodećih i pratećih razmaka.
     */
    String getNaziv() { return nazivField.getText().trim(); }

    /**
     * Dohvaća uneseni ISBN broj.
     * @return {@link String} s ISBN brojem.
     */
    String getIsbn() { return isbnField.getText().trim(); }

    /**
     * Dohvaća unesenu cijenu i pretvara je u numerički tip {@code double}.
     * Ukoliko unos nije ispravan broj, vraća 0.
     * @return Cijena kao {@code  double} vrijednost.
     */
    double getCijena() {
        try {
            return Double.parseDouble(cijenaField.getText());
        } catch (NullPointerException e) {
            return -1;
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    /**
     * Dohvaća odabranu vrstu djela iz padajućeg izbornika.
     * @return {@link String} s nazivom vrste ili prazan string ako ništa nije odabrano.
     */
    String getVrsta() {
        if (vrstaModel.getSelectedItem() == null){
            return "";
        }
        return vrstaModel.getSelectedItem().toString();
    }

    /** Pomoćna metoda koja puni {@link #vrstaModel} svim vrijednostima iz enumeracijske klase {@link VrsteDjelaEnum} */
    private void setupVrstaModel() {
        for(VrsteDjelaEnum v : VrsteDjelaEnum.values()) {
            vrstaModel.addElement(v.toString());
        }
    }
}