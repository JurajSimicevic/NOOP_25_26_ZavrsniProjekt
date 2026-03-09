package RegisterLibrarianComps;

import GUI_Comps.ActiveBasePanel;
import Commands.ActionCommandsEnum;
import GUI_Comps.EffectUtils;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel s formom za unos detaljnih podataka o novom zaposleniku (knjižničaru).
 * <p>
 * Za razliku od forme za kupce, ovaj panel sadrži sigurnosna polja za
 * <b>autentifikaciju</b> (korisničko ime i lozinku). Koristi {@link GridBagLayout}
 * kako bi osigurao da su labele i tekstualna polja savršeno poravnati bez obzira
 * na širinu panela.
 * </p>
 * <b>Specifičnosti:</b>
 * <ul>
 * <li>{@code JPasswordField} - koristi se za maskiranje unosa lozinke.</li>
 * <li>{@code ActiveBasePanel} - nasljeđuje automatizaciju životnog ciklusa komponenti.</li>
 * <li>{@code GridBagConstraints} - precizno upravlja razmještajem elemenata u mreži.</li>
 * </ul>
 */
public class RegisterLibLeftPanel extends ActiveBasePanel implements ActionListener {

    private JTextField usernameField, imeField, prezimeField, dobField, adresaField, cityField;
    private JPasswordField passwordField;
    private JButton registrirajBtn;
    private JButton returnBtn;
    private ActionCommandListener libLeftPanelListener;

    /**
     * Konstruktor panela. Postavlja vizualni okvir s naslovom.
     */
    public RegisterLibLeftPanel() {
        setBorder(new TitledBorder("Podaci o zaposleniku"));
    }

    /**
     * {@inheritDoc}
     * <p>Inicijalizira sva polja za unos i gumbe. Lozinka je zaštićena
     * putem {@link JPasswordField}.</p>
     */
    @Override
    protected void initComps() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        imeField = new JTextField(15);
        prezimeField = new JTextField(15);
        dobField = new JTextField(5);
        cityField = new JTextField(15);
        adresaField = new JTextField(15);

        registrirajBtn = new JButton("Registriraj knjižničara");
        returnBtn = new JButton("Natrag");
    }

    /**
     * {@inheritDoc}
     * <p>Slaže komponente u vertikalnu formu s dvije kolone (Label | Polje).</p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Unos podataka", TitledBorder.LEFT, TitledBorder.TOP));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Lozinka:"), gbc);
        gbc.gridx = 1; add(passwordField, gbc);

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
     * <p>Povezuje gumbe s akcijskim komandama za registraciju i povratak.</p>
     */
    @Override
    protected void activateComps() {
        registrirajBtn.setActionCommand(ActionCommandsEnum.REGISTER.toString());
        registrirajBtn.addActionListener(this);

        returnBtn.setActionCommand(ActionCommandsEnum.BACK.toString());
        returnBtn.addActionListener(this);
    }

    /**
     * Prosljeđuje klikove na gumbe vanjskom listeneru (obično prozoru).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(libLeftPanelListener != null){
            libLeftPanelListener.eventOccurred(ActionCommandsEnum.valueOf(e.getActionCommand()));
        } else {
            JOptionPane.showMessageDialog(this, "RegisterLibrarianLeftPanel listener not registered!", "Listener error", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Postavlja slušača događaja za ovaj panel. */
    public void setLibLeftPanelListener(ActionCommandListener libLeftPanelListener) {
        this.libLeftPanelListener = libLeftPanelListener;
    }

    /**@return vraća tekst iz {@link #usernameField} polja*/
    public String getUsername() {
        return usernameField.getText();
    }

    /**@return vraća tekst iz {@link #passwordField} polja*/
    public String getPassword() {
        return passwordField.getText();
    }

    /**@return vraća tekst iz {@link #imeField} polja*/
    public String getIme() {
        return imeField.getText();
    }

    /**@return vraća tekst iz {@link #prezimeField} polja*/
    public String getPrezime() {
        return prezimeField.getText();
    }

    /**
     * Sigurno dohvaća dob. Ako je unos neispravan, vraća 0.
     */
    public int getDob() {
        try {
            return Integer.parseInt(dobField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**@return vraća tekst iz {@link #cityField} polja*/
    public String getGrad() {
        return cityField.getText();
    }

    /**@return vraća tekst iz {@link #adresaField} polja*/
    public String getAdresa() {
        return adresaField.getText();
    }
}
