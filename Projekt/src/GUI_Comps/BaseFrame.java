package GUI_Comps;

import org.pushingpixels.radiance.animation.api.*;
import org.pushingpixels.radiance.animation.api.callback.TimelineCallback;

import javax.swing.*;
import java.awt.*;


/**
 * Osnovna apstraktna klasa za sve grafičke prozore (JFrame) u sustavu.
 * <p>
 * Ova klasa definira standardizirani životni vijek inicijalizacije korisničkog sučelja.
 * Svaki prozor koji naslijedi {@code BaseFrame} mora implementirati tri ključne faze
 * izgradnje GUI-ja, čime se osigurava uniformnost koda u cijelom projektu.
 * </p>
 */
public abstract class BaseFrame extends JFrame {

    /**
     * Konstruktor koji postavlja naslov prozora.
     * @param title Tekst koji će biti prikazan u naslovnoj traci prozora.
     */
    protected BaseFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        initComps();
        layoutComps();
        activateComps();
        openWindowFadeIn();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(BaseFrame.this, "Jeste li sigurni da želite ugasiti aplikaciju?", "Exit?",JOptionPane.YES_NO_OPTION);
                if(i == JOptionPane.YES_OPTION){
                    disposeFadeOut();
                }
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

    protected void openWindowFadeIn(){
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
                .setDuration(100)
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