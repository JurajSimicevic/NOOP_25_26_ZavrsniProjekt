package LoginComps;

import GUI_Comps.EffectUtils;

import BackEnd.*;
import GUI_Comps.BaseFrame;
import MainFrameComps.*;
import RegisterLibrarianComps.*;

import javax.swing.*;
import java.awt.*;

/**
 * Prozor za autentifikaciju knjižničara i primarna ulazna točka u aplikaciju.
 * <p>
 * Ova klasa služi za prikupljanje vjerodajnica korisnika te njihovu validaciju
 * putem {@link SessionManager}-a. Tek nakon uspješne prijave, sustav dozvoljava
 * instanciranje glavnog okvira aplikacije ({@link MainFrame}).
 * </p>
 * <b>Arhitektonske značajke:</b>
 * <ul>
 * <li><b>Upravljanje sesijom:</b> Izravno komunicira sa {@code SessionManager}-om
 * kako bi postavila aktivnog korisnika sustava.</li>
 * <li><b>Security:</b> Koristi {@link JPasswordField} za siguran unos lozinke,
 * sprječavajući njezino prikazivanje u običnom tekstu unutar memorije ili na ekranu.</li>
 * </ul>
 * <b>Tijek prijave:</b>
 * <ol>
 * <li>Korisnik unosi podatke -> Klik na "Prijava".</li>
 * <li>Dohvaćanje teksta -> Poziv {@code checkLogInCredentials()}.</li>
 * <li>Provjera u bazi -> Ako je OK, {@code LogInWindow} se uništava ({@code dispose()}) i otvara se {@code MainFrame}.</li>
 * </ol>
 */
public class LogInWindow extends BaseFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton prijavaBtn;
    private JButton registracijaBtn;

    /**
     * Inicijalizira glavni okvir za prijavu.
     * <ul>
     * <li>Postavlja naslov prozora na "LMS 9000 - Prijava u sustav".</li>
     * <li>Centrira prozor na ekranu korisnika ({@code setLocationRelativeTo(null)}).</li>
     * <li>Definira {@code EXIT_ON_CLOSE} kako bi se terminirala JVM nakon zatvaranja prozora.</li>
     * </ul>
     */
    public LogInWindow() {
        super("LMS 9000 - Prijava u sustav");
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);

        initComps();
        layoutComps();
        activateComps();

        setVisible(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void initComps() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        prijavaBtn = new JButton("Prijava");
        EffectUtils.pulseButton(prijavaBtn);
        registracijaBtn = new JButton("Registracija");

        // Dodatni vizualni feedback: Enter tipka aktivira gumb za prijavu
        getRootPane().setDefaultButton(prijavaBtn);
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Gradi formu koristeći {@link GridBagConstraints} za poravnanje labela i polja za unos.</li>
     * <li>Grupira gumbe "Prijava" i "Registracija" u zaseban južni panel radi vizualne preglednosti.</li>
     * </ul>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());

        // Glavni panel s poljima za unos (unutar okvira s naslovom)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Autorizacija knjižničara"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Red 1: Korisničko ime
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Korisničko ime:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Red 2: Lozinka
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Lozinka:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Panel s gumbima
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(prijavaBtn);
        buttonPanel.add(registracijaBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * {@inheritDoc}:
     * <ul>
     * <li>Registrira ActionListener za gumb registracije koji otvara {@link RegisterLibrarianWindow}.</li>
     * <li>Povezuje gumb za prijavu s logikom validacije vjerodajnica.</li>
     * </ul>
     */
    @Override
    protected void activateComps() {
        // Otvaranje forme za novog knjižničara (ako još nema račun)
        registracijaBtn.addActionListener(e -> {
            new RegisterLibrarianWindow(this);
        });

        // Logika prijave
        prijavaBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (checkLogInCredentials(user, pass)) {
                new MainFrame();
                disposeFadeOut();
            } else {
                // KRIVA LOZINKA - ZATRESI PROZOR!
                EffectUtils.shakeWindow(this);
                // Zacrveni polje za lozinku na sekundu
                passwordField.setBackground(new Color(255, 200, 200));
            }
        });
    }

    /**
     * Metoda koja vrši interakciju sa {@link SessionManager}-om radi autentifikacije.
     * <ul>
     * <li>Poziva {@code logIn} metodu managera s proslijeđenim parametrima.</li>
     * <li>U slučaju neuspjeha, prikazuje {@link JOptionPane} s porukom o pogrešnim podacima.</li>
     * <li>U slučaju uspjeha, inicijalizira {@link MainFrame} i zatvara trenutni prozor.</li>
     * </ul>
     * @param username Korisničko ime preuzeto iz tekstualnog polja.
     * @param password Lozinka preuzeta iz polja za lozinku.
     */
    private boolean checkLogInCredentials(String username, String password){
        return SessionManager.getInstance().authenticate(username, password);
    }

}