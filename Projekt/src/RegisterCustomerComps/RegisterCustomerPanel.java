package RegisterCustomerComps;

import GUI_Comps.ActiveBasePanel;
import Commands.ActionCommandsEnum;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel s formom za unos podataka o novom kupcu (ime, prezime, adresa, itd.).
 * <p>
 * Služi kao ulazna točka za prikupljanje informacija o članovima knjižnice.
 * Podaci prikupljeni u ovom panelu koriste se za stvaranje novih objekata
 * putem {@link Commands.RegisterCustomerCommand}.
 * </p>
 * <b>Funkcionalnosti:</b>
 * <ul>
 * <li>Unos osobnih podataka putem {@link JTextField} komponenti.</li>
 * <li>Validacija numeričkog unosa za godine korisnika.</li>
 * <li>Sustav za navigaciju natrag na glavni prozor.</li>
 * </ul>
 */
public class RegisterCustomerPanel extends ActiveBasePanel implements ActionListener {

    private JTextField imeField, prezimeField, dobField, adresaField, cityField;
    private JButton registrirajBtn, returnBtn;
    private ActionCommandListener listener;

    /**
     * Konstruktor klase {@code RegisterCustomerPanel}.
     * <p>
     * Postavlja dimenzije i vizualne obrube (TitledBorder).
     * Nasljeđuje automatizaciju iz {@link ActiveBasePanel}.
     * </p>
     */
    public RegisterCustomerPanel() {
        setBorder(new TitledBorder("Registracija novog člana"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Stvara tekstualna polja i gumbe s predefiniranim širinama stupaca.
     * </p>
     */
    @Override
    protected void initComps() {
        imeField = new JTextField(15);
        prezimeField = new JTextField(15);
        dobField = new JTextField(5);
        cityField = new JTextField(15);
        adresaField = new JTextField(15);

        registrirajBtn = new JButton("Registriraj novog člana");
        returnBtn = new JButton("Natrag");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Koristi {@link GridBagLayout} za precizno poravnanje labela i polja za unos.
     * </p>
     */
    @Override
    protected void layoutComps() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Unos podataka", TitledBorder.LEFT, TitledBorder.TOP));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Ime:"), gbc);
        gbc.gridx = 1; add(imeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Prezime:"), gbc);
        gbc.gridx = 1; add(prezimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Dob (godine):"), gbc);
        gbc.gridx = 1; add(dobField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Grad:"), gbc);
        gbc.gridx = 1; add(cityField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Adresa:"), gbc);
        gbc.gridx = 1; add(adresaField, gbc);

        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.EAST;
        add(registrirajBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 8; gbc.anchor = GridBagConstraints.EAST;
        add(returnBtn, gbc);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Povezuje gumbe s akcijskim naredbama {@code REGISTER} i {@code BACK}.
     * </p>
     */
    @Override
    protected void activateComps() {
        registrirajBtn.setActionCommand(ActionCommandsEnum.REGISTER.toString());
        registrirajBtn.addActionListener(this);

        returnBtn.setActionCommand(ActionCommandsEnum.BACK.toString());
        returnBtn.addActionListener(this);
    }

    /**
     * Obrađuje klikove na gumbe i delegira ih putem listenera.
     * @param e Akcijski događaj.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(listener != null){
            listener.eventOccurred(ActionCommandsEnum.valueOf(e.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(this, "Listener not registered!", "Listener error", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Postavlja slušača događaja za prosljeđivanje komandi (npr. {@link RegisterCustomerWindow}-u).
     * @param listener Implementacija {@link ActionCommandListener} sučelja.
     */
    public void setActionCommandListener(ActionCommandListener listener) {
        this.listener = listener;
    }

    /**@return Vraća tekst iz {@link #imeField}*/
    public String getIme() {
        return imeField.getText();
    }

    /**@return Vraća tekst iz {@link #prezimeField}*/
    public String getPrezime() {
        return prezimeField.getText();
    }

    /**
     * Dohvaća dob korisnika iz tekstualnog polja.
     * <p>
     * Sadrži ugrađenu validaciju. U slučaju neispravnog unosa (npr. slova),
     * metoda vraća {@code 0}.
     * </p>
     * @return Broj godina kao {@code int}.
     */
    public int getDob() {
        try {
            return Integer.parseInt(dobField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**@return Vraća tekst iz {@link #cityField}*/
    public String getGrad() {
        return cityField.getText();
    }

    /**@return Vraća tekst iz {@link #adresaField}*/
    public String getAdresa() {
        return adresaField.getText();
    }
}
