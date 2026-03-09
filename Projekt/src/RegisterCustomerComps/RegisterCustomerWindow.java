package RegisterCustomerComps;

import BackEnd.*;
import GUI_Comps.ShortcuttableFrame;
import Commands.*;
import MainFrameComps.*;
import ObserversAndOtherComps.*;

import UniverzalnoSucelje.ActionCommandListener;

import javax.swing.*;
import java.awt.*;

/**
 * Glavni upravljački prozor za administraciju baze kupaca.
 * Ovaj prozor predstavlja kompleksno sučelje koje integrira:
 * <ul>
 * <li>{@link RegisterCustomerPanel} - Formu za unos novih kupaca.</li>
 * <li>{@link ConjoinedPanel} - Centralni prikaz liste kupaca s opcijama pretrage i brisanja.</li>
 * <li>{@link KnjigeViewPanel} - Dinamički prikaz knjiga koje je selektirani kupac posudio.</li>
 * </ul>
 * <b>Arhitektura:</b>
 * <p>
 * Nasljeđuje {@link ShortcuttableFrame}, čime osigurava podršku za prečace na razini sustava.
 * Koristi <i>Command Pattern</i> za izvršavanje operacija registracije i brisanja,
 * što omogućuje povrat akcija (Undo/Redo).
 * </p>
 */
public class RegisterCustomerWindow extends ShortcuttableFrame {

    private KnjigeViewPanel knjigeViewPanel;
    private RegisterCustomerPanel registerCustomerPanel;
    private ConjoinedPanel conjoinedPanel;
    private Customer selectedCustomer;

    /**
     * Konstruktor prozora za registraciju kupaca.
     * <p>
     * Postavlja naslov, fiksne dimenzije (1018x600) i centriranje na ekranu.
     * Postavlja operaciju zatvaranja na {@code DISPOSE_ON_CLOSE} kako bi se oslobodili
     * resursi bez gašenja cijele aplikacije.
     * </p>
     */
    public RegisterCustomerWindow() {
        super("Library Management System 9000 - Registracija");

        setSize(1018, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inicijalizira unutrašnje panele. Posebno čisti model {@code knjigeViewPanel}-a
     * kako bi bio spreman za prikaz tek nakon odabira specifičnog korisnika.
     * </p>
     */
    @Override
    protected void initComps() {
        knjigeViewPanel = new KnjigeViewPanel();
        knjigeViewPanel.emptyModel();

        conjoinedPanel = new ConjoinedPanel();

        registerCustomerPanel = new RegisterCustomerPanel();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Organizira vizualne komponente koristeći {@link BorderLayout}.
     * Forma za registraciju zauzima zapadni dio, dok centralni i istočni dio
     * prikazuju liste unutar skrolajućih okna.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        JScrollPane centerScrollPane = new JScrollPane(conjoinedPanel);
        JScrollPane easternScrollPane = new JScrollPane(knjigeViewPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerScrollPane, easternScrollPane);
        splitPane.setDividerLocation(400);

        // Dodavanje naslova listama
        ((JScrollPane)splitPane.getLeftComponent()).setBorder(BorderFactory.createTitledBorder("Popis Korisnika"));
        ((JScrollPane)splitPane.getRightComponent()).setBorder(BorderFactory.createTitledBorder("Posuđene Knjige Odabranog Korisnika"));

        add(splitPane, BorderLayout.CENTER);
        add(registerCustomerPanel, BorderLayout.WEST);

    }

    /**
     * {@inheritDoc}
     * <p>
     * Postavlja logiku interakcije između komponenti:
     * </p>
     * <ul>
     * <li>Sluša gumbe za registraciju i povratak u glavni prozor.</li>
     * <li>Povezuje selekciju kupca u {@code conjoinedPanel}-u s prikazom knjiga u {@code knjigeViewPanel}-u.</li>
     * <li>Delegira naredbu brisanja kupca {@link CommandManager}-u.</li>
     * </ul>
     */
    @Override
    protected void activateComps() {
        // Gumb za registraciju
        registerCustomerPanel.setActionCommandListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                if(actionCommand == ActionCommandsEnum.REGISTER) {
                    register();
                }
                if(actionCommand == ActionCommandsEnum.BACK) {
                    new MainFrame();
                    disposeFadeOut();
                }
            }
        });

        conjoinedPanel.setUserViewPanelListener(new ViewPanelListener<Customer>() {
            @Override
            public void eventOccurred(Customer user) {
                if(user != null){
                    selectedCustomer = user;
                    knjigeViewPanel.populateBorrowedBooksList(user);
                }
            }
        });

        conjoinedPanel.setConjoinedPanelListener(new ActionCommandListener() {
            @Override
            public void eventOccurred(ActionCommandsEnum actionCommand) {
                if(actionCommand == ActionCommandsEnum.DELETE) {
                    CommandManager.getInstance().executeCommand(new DeleteCustomerCommand(selectedCustomer));
                }
            }
        });

    }

    /**
     * Prikuplja podatke iz forme i izvršava naredbu registracije novog kupca.
     * <p>
     * Koristi {@link RegisterCustomerCommand} kako bi osigurao da se operacija
     * može poništiti u slučaju pogreške.
     * </p>
     */
    private void register(){
        String ime = registerCustomerPanel.getIme();
        String prezime = registerCustomerPanel.getPrezime();
        int dob = registerCustomerPanel.getDob();
        String grad = registerCustomerPanel.getGrad();
        String adresa = registerCustomerPanel.getAdresa();
        CommandManager.getInstance().executeCommand(new RegisterCustomerCommand(ime, prezime, dob, grad, adresa));
    }
}