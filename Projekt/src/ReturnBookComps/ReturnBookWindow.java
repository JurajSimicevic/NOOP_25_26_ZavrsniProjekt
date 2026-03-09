package ReturnBookComps;

import BackEnd.*;
import GUI_Comps.ShortcuttableFrame;
import Commands.*;
import MainFrameComps.*;
import ObserversAndOtherComps.*;

import javax.swing.*;
import java.awt.*;


/**
 * Glavni prozor sučelja za povrat knjiga u knjižnicu.
 * <p>
 * Ovaj prozor omogućuje knjižničaru da odabere knjigu s popisa i evidentira
 * njezin povrat uz opcionalne naknade za kašnjenje ili oštećenje.
 * </p>
 * <b>Arhitektura prozora:</b>
 * <ul>
 * <li><b>North:</b> {@link KnjigeViewPanel} - Prikazuje popis knjiga koje su trenutno kod korisnika.</li>
 * <li><b>Center:</b> {@link ReturnBookFormPanel} - Sadrži kontrole za potvrdu povrata i obračun penala.</li>
 * </ul>
 * <p>
 * Prozor implementira {@link ShortcuttableFrame} čime podržava brze tipkovničke prečace (npr. Alt+X za izlaz).
 * </p>
 */
public class ReturnBookWindow extends ShortcuttableFrame {

    private ViewPanel<Knjiga> knjigeViewPanel;
    private ReturnBookFormPanel returnBookFormPanel;

    /**
     * Konstruktor klase {@code ReturnBookWindow}.
     * <p>
     * Postavlja naslov, dimenzije i osigurava da se prozor otvori na sredini ekrana.
     * Automatski pokreće životni ciklus komponenti kroz baznu klasu.
     * </p>
     */
    public ReturnBookWindow() {
        super("Vrati knjigu");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    /**
     * {@inheritDoc}
     * <p>Stvara panel za prikaz knjiga i formu s opcijama za povrat.</p>
     */
    @Override
    protected void initComps() {
        knjigeViewPanel = new KnjigeViewPanel();
        returnBookFormPanel = new ReturnBookFormPanel();
    }

    /**
     * {@inheritDoc}
     * <p>Slaže panele vertikalno koristeći {@link BorderLayout}.</p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        add(returnBookFormPanel, BorderLayout.CENTER);
        add(knjigeViewPanel, BorderLayout.NORTH);

    }

    /**
     * {@inheritDoc}
     * <p>
     * Postavlja {@link ReturnBookFormListener} koji reagira na akcije korisnika.
     * Ako se potvrdi povrat, poziva se {@link ReturnBookCommand} sa svim potrebnim parametrima.
     * </p>
     */
    @Override
    protected void activateComps() {
        returnBookFormPanel.setReturnBookFormListener(new ReturnBookFormListener() {
            @Override
            public void returnBookFormEventOccured(String actionCommand, boolean hasLateFee, boolean hasDamageFee) {
                if(actionCommand.equals(ActionCommandsEnum.BACK.toString())) {
                    new MainFrame();
                    disposeFadeOut();
                }
                if(actionCommand.equals(ActionCommandsEnum.RETURN_BOOK.toString())) {
                    boolean kasnjenje = returnBookFormPanel.getKasnjenjeCheckBox().isSelected();
                    boolean steta = returnBookFormPanel.getStetaCheckBox().isSelected();
                    CommandManager.getInstance().executeCommand(new ReturnBookCommand(knjigeViewPanel.getSelectedValue(), kasnjenje, steta));
                }
            }
        });
    }
}
