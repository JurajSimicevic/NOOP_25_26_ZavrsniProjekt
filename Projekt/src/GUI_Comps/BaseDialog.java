package GUI_Comps;

import org.pushingpixels.radiance.animation.api.Timeline;
import org.pushingpixels.radiance.animation.api.callback.TimelineCallback;

import javax.swing.*;
import java.awt.*;

/**
 * Temeljna apstraktna klasa za sve dijaloge (podprozore) u aplikaciji.
 * <p>
 * Služi kao ekvivalent {@link BaseFrame} klasi, ali za komponente koje nasljeđuju {@link JDialog}.
 * Standardizira proces inicijalizacije sučelja kroz tri strogo definirane faze.
 * </p>
 * <b>Arhitektonske značajke:</b>
 * <ul>
 * <li><b>Polimorfni konstruktori:</b> Podržava inicijalizaciju dijaloga neovisno o tome
 * je li roditelj {@link Frame} ili drugi {@link JDialog}.</li>
 * <li><b>Životni ciklus:</b> Metoda {@code setup()} automatizira pozivanje faza
 * inicijalizacije, smanjujući mogućnost pogreške u podklasama.</li>
 * </ul>
 */
public abstract class BaseDialog extends JDialog {

    /**
     * Konstruktor za dijaloge kojima je roditelj {@link JFrame} (glavni prozor)..
     * @param owner Referenca na roditeljski {@link Frame}.
     * @param title Naslov dijaloga.
     * @param modal Određuje blokira li dijalog interakciju s roditeljem.
     */
    public BaseDialog(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setup();
    }

    /**
     * Konstruktor za dijaloge kojima je roditelj {@link JDialog} (drugi dijalog).
     * @param owner Referenca na roditeljski {@link JDialog}.
     * @param title Naslov dijaloga.
     * @param modal Određuje modalnost dijaloga.
     */
    public BaseDialog(JDialog owner, String title, boolean modal) {
        super(owner, title, modal);
        setup();
    }

    /**
     * Centralizirana metoda koja definira redoslijed izgradnje GUI-ja.
     */
    private void setup(){
        initComps();
        layoutComps();
        activateComps();
        openWindowFadeIn();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                animateClose();
            }
        });
        setIconImage(new ImageIcon("data/frame_icon.png").getImage());
    }

    /** Inicijalizira komponente prozora*/
    protected abstract void initComps();

    /** Raspoređuje komponente prozora*/
    protected abstract void layoutComps();

    /** Aktivira komponente prozora*/
    protected abstract void activateComps();

    /**
     * Radiance Animation Engine traži točno ovu metodu za animaciju 'x'
     */
    public void setX(int x) {
        this.setLocation(x, this.getY());
    }

    /**
     * Radiance Animation Engine traži točno ovu metodu za animaciju 'y'
     */
    public void setY(int y) {
        this.setLocation(this.getX(), y);
    }

    /**
     * Radiance Animation Engine traži točno ovu metodu za animaciju 'opacity'
     */
    public void setOpacity(float opacity) {
        // Moramo koristiti super.setOpacity jer JFrame to već ima,
        // ali Radiance ponekad treba eksplicitnu metodu u tvojoj klasi
        super.setOpacity(opacity);
    }

    private void openWindowFadeIn() {
        // --- ANIMACIJA PALJENJA ---
        // Postavi početnu prozirnost na 0 (nevidljivo)
        this.setOpacity(0.0f);

        SwingUtilities.invokeLater(() -> {
            // Kreiramo Timeline koji "gađa" opacity property tvog frame-a
            int finalY = this.getY();
            this.setLocation(this.getX(), finalY + 50); // Spusti ga malo prije starta

            // 3. POKRENI TIMELINE
            Timeline fadeIn = Timeline.builder(this)
                    .addPropertyToInterpolate("opacity", 0.0f, 1.0f)
                    .addPropertyToInterpolate("y", this.getY(), finalY) // Putuje prema gore
                    .setDuration(300)
                    .build();

            fadeIn.play();
        });
    }

    protected void disposeFadeOut(){
        int finalY = getY();
        int startY = finalY + 50;

        Timeline fadeOut = Timeline.builder(this)
                .addPropertyToInterpolate("opacity", 1.0f, 0.0f)
                .addPropertyToInterpolate("y", finalY, startY)
                .setDuration(200)
                // KLJUČNI DIO: Callback koji čeka kraj animacije
                .addCallback(new TimelineCallback() {
                    @Override
                    public void onTimelineStateChanged(Timeline.TimelineState oldState,
                                                       Timeline.TimelineState newState,
                                                       float durationFraction,
                                                       float timelinePosition) {
                        // Kad animacija dođe do kraja (postane IDLE), gasi sve
                        if (newState == Timeline.TimelineState.IDLE) {
                            dispose();
                        }
                    }
                    @Override public void onTimelinePulse(float durationFraction, float timelinePosition) {}
                })
                .build();

        fadeOut.play();
    }

    protected void animateClose() {
        // Ciljane dimenzije (nešto manje od trenutnih)
        Dimension targetSize = new Dimension(this.getWidth() - 60, this.getHeight() - 60);

        Timeline fadeOut = Timeline.builder(this)
                // 1. Postaje proziran
                .addPropertyToInterpolate("opacity", 1.0f, 0.0f)
                // 2. Smanjuje se (Shrink)
                .addPropertyToInterpolate("size", getSize(), targetSize)
                .setDuration(100)
                .addCallback(new TimelineCallback() {
                    @Override
                    public void onTimelineStateChanged(Timeline.TimelineState oldState,
                                                       Timeline.TimelineState newState,
                                                       float durationFraction,
                                                       float timelinePosition) {
                        // Tek kad animacija završi (IDLE), stvarno uništi prozor
                        if (newState == Timeline.TimelineState.IDLE) {
                            dispose();
                        }
                    }
                    @Override public void onTimelinePulse(float durationFraction, float timelinePosition) {}
                })
                .build();

        fadeOut.play();
    }
}
