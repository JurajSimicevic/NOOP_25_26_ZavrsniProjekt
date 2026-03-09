package GUI_Comps;

import org.pushingpixels.radiance.animation.api.Timeline;
import org.pushingpixels.radiance.animation.api.ease.Spline;

import javax.swing.*;
import java.awt.*;

public class EffectUtils {

    public static void shakeWindow(JFrame frame) {
        int originalX = frame.getX();

        // Kreiramo timeline koji miče prozor lijevo-desno
        Timeline shakeTimeline = Timeline.builder(frame)
                // Ide s originalne pozicije na -10 piksela, pa na +10, pa se vraća
                .addPropertyToInterpolate("x", originalX, originalX - 10)
                .addPropertyToInterpolate("x", originalX - 10, originalX + 10)
                .addPropertyToInterpolate("x", originalX + 10, originalX)
                .setDuration(100) // Brzina jednog "trestaja" (0.1 sekunda)
                .build();

        // Ponovi treskanje 3 puta za onaj pravi efekt
        shakeTimeline.playLoop(3, Timeline.RepeatBehavior.REVERSE);
    }

    public static void slideInPanel(JPanel panel) {
        int targetWidth = panel.getPreferredSize().width;
        int targetHeight = panel.getPreferredSize().height;

        // 2. Forsiraj početnu širinu na 0 odmah
        panel.setPreferredSize(new Dimension(0, targetHeight));
        panel.setMinimumSize(new Dimension(0, targetHeight));
        panel.setMaximumSize(new Dimension(0, targetHeight));

        SwingUtilities.invokeLater(() -> {
            // 2. Kreiramo Timeline
            Timeline slideIn = Timeline.builder(panel)
                    .addPropertyToInterpolate("preferredSize",
                            new Dimension(0, targetHeight), // Kreće od nule
                            new Dimension(targetWidth, targetHeight)) // Ide do pune širine
                    .setDuration(600)
                    .addCallback(new org.pushingpixels.radiance.animation.api.callback.TimelineCallbackAdapter() {
                        @Override
                        public void onTimelinePulse(float durationFraction, float timelinePosition) {
                            // JAAAKO BITNO: Revalidate govori roditelju da napravi mjesta za panel koji raste
                            if (panel.getParent() != null) {
                                panel.getParent().revalidate();
                                panel.getParent().repaint();
                            }
                        }
                    })
                    .build();

            slideIn.play();
        });
    }

    public static Timeline pulseButton(JButton button) {
        // 1. Inicijaliziramo timeline
        Timeline pulse = Timeline.builder(button)
                // Animiramo pozadinsku boju iz trenutne u npr. svijetlo plavu ili narančastu
                .addPropertyToInterpolate("background", button.getBackground(), new Color(0, 150, 255))
                .setDuration(1000)
                .build();

        // 2. Pokrećemo beskonačno ponavljanje (ping-pong efekt)
        pulse.playLoop(Timeline.RepeatBehavior.REVERSE);

        // 3. VRAĆAMO objekt (ovo rješava tvoju grešku "Required: Timeline, Provided: void")
        return pulse;
    }

    public static void animateFloatingLabel(JLabel label, int targetY, int targetFontSize) {
        Timeline floatingTimeline = Timeline.builder(label)
                .addPropertyToInterpolate("y", label.getY(), targetY)
                .addPropertyToInterpolate("font", label.getFont(), label.getFont().deriveFont((float)targetFontSize))
                .setDuration(250)
                .addCallback(new org.pushingpixels.radiance.animation.api.callback.TimelineCallbackAdapter() {
                    @Override
                    public void onTimelinePulse(float durationFraction, float timelinePosition) {
                        // Ovo sprječava Layout Managera da "ukrade" kontrolu tijekom animacije
                        label.getParent().repaint();
                    }
                })
                .build();

        floatingTimeline.play();
    }
}
