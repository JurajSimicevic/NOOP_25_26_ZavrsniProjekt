package BuyBookComps;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import GUI_Comps.BaseDialog;
import GUI_Comps.ShortcuttableDialog;
import Commands.*;
import ObserversAndOtherComps.KnjigeViewPanel;
import StrategyComps.*;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Interaktivni dijaloški okvir za procesiranje transakcija kupnje knjiga.
 * <p>
 * Nasljeđuje {@link BaseDialog} i djeluje kao <i>Context</i> u <b>Strategy uzorku</b> za plaćanje.
 * Povezuje vizualne komponente s poslovnom logikom kroz sustav naredbi, osiguravajući
 * transakcijski integritet pri svakoj kupnji.
 * </p>
 * <b>Ključni mehanizmi rada:</b>
 * <ul>
 * <li><b>Dinamički odabir strategije:</b> Omogućuje izmjenu logike plaćanja (Gotovina, Kartica, Kripto)
 * u <i>runtimeu</i> bez izmjene koda dijaloga.</li>
 * <li><b>Command integracija:</b> Svaka uspješna kupnja se enkapsulira u {@link BuyBookCommand},
 * što omogućuje potencijalni <i>Undo</i> proces.</li>
 * <li><b>Data Binding:</b> Automatski sinkronizira prikaz cijene s odabranim naslovom iz {@link KnjigeViewPanel}.</li>
 * </ul>
 * <b>Tijek procesa:</b>
 * <ol>
 * <li>Inicijalizacija liste knjiga i dostupnih načina plaćanja.</li>
 * <li>Validacija odabira knjige i unosa podataka o kupcu.</li>
 * <li>Izvršavanje naplate putem odabrane {@link PaymentStrategy}.</li>
 * <li>Zapisivanje promjena u bazu putem {@link LibraryManager}-a.</li>
 * </ol>
 */
public class BuyBookWindow extends ShortcuttableDialog {

    /** Panel koji prikazuje listu dostupnih knjiga pomoću JList komponente. */
    private KnjigeViewPanel knjigeViewPanel;

    /** Padajući izbornik za odabir strategije plaćanja (definirano u PaymentStrategyEnum). */
    private JComboBox<PaymentStrategyEnum> paymentOptions;

    /** Gumbi za potvrdu kupnje ili izlaz iz dijaloga. */
    private JButton buyBtn, cancelBtn;

    /** Labela koja dinamički prikazuje cijenu trenutno odabrane knjige. */
    private JLabel priceLabel;

    /** Referenca na sučelje strategije koja će se instancirati u trenutku kupnje. */
    private PaymentStrategy paymentStrategy;

    /**
     * Konstruktor dijaloškog okvira.
     * @param parent Referenca na glavni JFrame radi centriranja i modalnosti.
     */
    public BuyBookWindow(JFrame parent) {
        super(parent, "Kupnja knjige", true); // true postavlja dijalog kao modalan
        setSize(500, 450);
        setLocationRelativeTo(parent);

        initComps();
        layoutComps();
        activateComps();

        setVisible(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void initComps() {
        knjigeViewPanel = new KnjigeViewPanel();
        paymentOptions = new JComboBox<>(PaymentStrategyEnum.values());
        buyBtn = new JButton("Kupi");
        cancelBtn = new JButton("Odustani");
        priceLabel = new JLabel("Odaberite knjigu...");
    }

    /** {@inheritDoc}
     * <p>({@code BorderLayout} i {@code GridLayout}).</p> */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout(10, 10));

        // Sredina - Lista knjiga
        add(knjigeViewPanel, BorderLayout.CENTER);

        // Donji dio - Opcije i gumbi
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p1.add(priceLabel);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p2.add(new JLabel("Način plaćanja: "));
        p2.add(paymentOptions);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p3.add(buyBtn);
        p3.add(cancelBtn);

        bottomPanel.add(p1);
        bottomPanel.add(p2);
        bottomPanel.add(p3);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /** {@inheritDoc} */
    @Override
    protected void activateComps() {
        // ListSelectionListener: detektira klik na knjigu i ažurira cijenu na ekranu
        knjigeViewPanel.getList().addListSelectionListener(e -> updatePriceLabel());

        // Zatvara prozor bez akcije
        cancelBtn.addActionListener(e -> animateClose());

        // Pokreće proceduru prodaje
        buyBtn.addActionListener(e -> saleProcedure());
    }

    /** Pomoćna metoda koja dohvaća cijenu odabrane knjige i ispisuje je u labelu. */
    private void updatePriceLabel() {
        Knjiga selected = knjigeViewPanel.getSelectedValue();
        if (selected != null) {
            priceLabel.setText("Cijena: " + selected.getCijena() + " €");
        }
    }

    /**
     * Provodi postupak prodaje.
     * Na temelju odabira u {@code JComboBox}-u instancira odgovarajuću konkretnu strategiju
     * ({@code CashPayment}, {@code CardPayment} ili {@code CryptoPayment}) te pokreće {@code BuyBookCommand}.
     */
    private void saleProcedure(){
        // Dohvat odabrane knjige iz panela
        Knjiga selected = knjigeViewPanel.getSelectedValue();
        if (selected == null) return; // Osiguranje ako ništa nije odabrano

        // Određivanje strategije (STRATEGY PATTERN)
        String method = Objects.requireNonNull(paymentOptions.getSelectedItem()).toString();

        if (method.equals(PaymentStrategyEnum.Gotovina.toString())) {
            paymentStrategy = new CashPayment();
        }
        if(method.equals(PaymentStrategyEnum.Kartica.toString())) {
            paymentStrategy = new CardPayment();
        }
        if(method.equals(PaymentStrategyEnum.Kriptovalute.toString())) {
            paymentStrategy = new CryptoPayment();
        }

        // Izvršavanje poslovne logike kroz Command uzorak (COMMAND PATTERN)
        // Ako command manager uspješno obriše knjigu iz baze/liste:
        if(CommandManager.getInstance().executeCommand(new BuyBookCommand(selected))){
            // Izvrši simulaciju plaćanja odabranom metodom
            paymentStrategy.processPayment(selected.getCijena());
        }
    }
}