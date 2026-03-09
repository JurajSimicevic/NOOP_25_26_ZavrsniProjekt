import BackEnd.DatabaseConnectionManager;
import LoginComps.LogInWindow;
import org.pushingpixels.radiance.theming.api.RadianceThemingCortex;
import org.pushingpixels.radiance.theming.api.*;
import org.pushingpixels.radiance.theming.api.skin.*;
import javax.swing.*;


import javax.swing.*;
import java.awt.*;

/**
 * Glavna ulazna točka (Main entry point) cijele "Library Management System 9000" aplikacije.
 * Ova klasa je odgovorna za inicijalno podizanje sustava, što uključuje:
 * <ul>
 * <li>Postavljanje modernog grafičkog sučelja putem <b>Substance</b> biblioteke.</li>
 * <li>Osiguravanje nitne sigurnosti (Thread safety) pokretanjem sučelja unutar EDT-a.</li>
 * <li>Pokretanje početnog {@link LogInWindow} prozora za autorizaciju korisnika.</li>
 * </ul>
 * <b>Napomena:</b> Sadrži i zakomentirane blokove koda koji služe razvojnom inženjeru
 * za brzo testiranje specifičnih modula bez prolaska kroz proces prijave.
 */
public class App {

    /**
     * Main metoda koja pokreće cijeli sustav.
     * @param args Parametri komandne linije (ne koriste se u ovom sustavu).
     */
    public static void main(String[] args) {
        System.setProperty("sun.java2d.noddraw", "true");
        // 1. Omogući dekoraciju prozora za oštre rubove i animirane sistemske gumbe
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(() -> {
            try {
                // 2. Postavi skin (npr. Graphite)
                RadianceThemingCortex.GlobalScope.setSkin(new GraphiteGlassSkin());
                // Glatko "posvjetljivanje" gumba kad pređeš mišem preko njega
                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.ROLLOVER);

                // Glatki prijelaz boje kad klikneš na komponentu
                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.SELECTION);

                // Animacija fokusa (kad klikneš u polje za Text, rub polja polako zasvijetli)
                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.FOCUS);

                // Ghosting efekt - kad je komponenta onemogućena (disabled), polako izblijedi umjesto da samo "pukne" u sivo
                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.GHOSTING_BUTTON_PRESS);

                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.GHOSTING_ICON_ROLLOVER);

                // 3. DODATNE ANIMACIJE:
                // Ovo omogućuje animacije kod promjene stanja komponenti (npr. glatki fade-in/out)
                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.GHOSTING_BUTTON_PRESS);
                RadianceThemingCortex.GlobalScope.allowAnimations(RadianceThemingSlices.AnimationFacet.SELECTION);

                //Spajamo se na bazu
                DatabaseConnectionManager.getInstance().connect();

                new LogInWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
