package RegisterLibrarianComps;

import GUI_Comps.ShortcuttableDialog;
import Commands.*;
import LoginComps.LogInWindow;
import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;

/**
 * Specijalizirani dijalog za administraciju i registraciju knjižničara.
 * <p>
 * Za razliku od registracije kupaca, ovaj prozor se otvara kao {@link JDialog},
 * što sprječava interakciju s glavnim prozorom dok se ne završi rad s osobljem.
 * </p>
 * <b>Arhitektura:</b>
 * <ul>
 * <li><b>Lijeva strana:</b> {@link RegisterLibLeftPanel} - forma za unos podataka (uključujući lozinku).</li>
 * <li><b>Centar:</b> {@link ConjoinedRegisterLibrarianPanel} - lista postojećih knjižničara s pretragom.</li>
 * </ul>
 */
public class RegisterLibrarianWindow extends ShortcuttableDialog {

    private ConjoinedRegisterLibrarianPanel conjoinedPanel;
    private RegisterLibLeftPanel registerLibLeftPanel;

    /**
     * Konstruktor koji postavlja {@code JDialog} kao vlasnika i inicijalizira prozor.
     * @param owner Nadređeni prozor.
     */
    public RegisterLibrarianWindow(JDialog owner) {
        super(owner, "Registracija knjižničara", true);
        setupWindow();
    }

    /**
     * Konstruktor koji postavlja {@code JFrame} kao vlasnika dijaloga i inicijalizira prozor.
     * @param owner Nadređeni prozor.
     */
    public RegisterLibrarianWindow(JFrame owner) {
        super(owner, "Registracija knjižničara", true);
        setupWindow();
    }

    /** Postavlja osnovne parametre prozora (veličina, pozicija, vidljivost). */
    private void setupWindow() {
        setSize(800, 500);
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void initComps() {
        registerLibLeftPanel = new RegisterLibLeftPanel();
        conjoinedPanel = new ConjoinedRegisterLibrarianPanel();
    }

    /** {@inheritDoc} */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());

        conjoinedPanel.setBorder(BorderFactory.createTitledBorder("Baza registriranih knjižničara"));

        // Dodavanje na glavni prozor
        add(registerLibLeftPanel, BorderLayout.WEST);
        add(conjoinedPanel, BorderLayout.CENTER);
    }

    /** {@inheritDoc} */
    @Override
    protected void activateComps() {
        registerLibLeftPanel.setLibLeftPanelListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum ae) {
                if(ae == ActionCommandsEnum.BACK) {
                    animateClose();
                }
                if (ae == ActionCommandsEnum.REGISTER) {
                    izvrsiUnos();
                }
            }
        });
    }

    /**
     * Prikuplja sve podatke iz forme i šalje ih {@link CommandManager}-u.
     * <p>
     * Koristi {@link RegisterLibrarianCommand} koja osigurava da se novi
     * korisnički račun ispravno doda u sustav autorizacije.
     * </p>
     */
    private void izvrsiUnos(){
        String userName = registerLibLeftPanel.getUsername();
        String password = registerLibLeftPanel.getPassword();
        String ime = registerLibLeftPanel.getIme();
        String prezime = registerLibLeftPanel.getPrezime();
        int dob = registerLibLeftPanel.getDob();
        String grad = registerLibLeftPanel.getGrad();
        String adresa = registerLibLeftPanel.getAdresa();

        System.out.println("CommandManager.getInstance().executeCommand(new RegisterLibrarianCommand(userName, password, ime, prezime, dob, grad, adresa));");
        CommandManager.getInstance().executeCommand(new RegisterLibrarianCommand(userName, password, ime, prezime, dob, grad, adresa));
    }

}
