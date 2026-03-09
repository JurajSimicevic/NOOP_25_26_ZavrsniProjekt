package ObserversAndOtherComps;

import BackEnd.Customer;
import BackEnd.FilePathEnum;
import BackEnd.Knjiga;
import GUI_Comps.EffectUtils;

import java.awt.*;

/**
 * Specijalizirani panel za prikaz liste knjiga unutar {@link ViewPanel} okvira.
 * <p>
 * Osim standardnog prikaza svih knjiga, nudi specifičnu funkcionalnost
 * prikaza knjiga koje su trenutno posuđene kod određenog korisnika.
 * </p>
 */
public class KnjigeViewPanel extends ViewPanel<Knjiga> {

    /** Defaultni konstruktor koji postavlja izvor podataka na {@link FilePathEnum#KNJIGE}. */
    public KnjigeViewPanel() {
        super(FilePathEnum.KNJIGE);
    }

    /**
     * Konstruktor s prilagođenim dimenzijama.
     * @param filePathEnum Izvor podataka.
     * @param height Visina panela.
     * @param width Širina panela.
     */
    public KnjigeViewPanel(FilePathEnum filePathEnum, int height, int width) {
        super(FilePathEnum.KNJIGE, height, width);
    }

    /** Čisti trenutni prikaz u listi. */
    public void emptyModel(){
        listModel.clear();
    }

    /**
     * Puni model liste isključivo knjigama koje je posudio određeni korisnik.
     * <p>
     * Ova metoda se koristi izvan uobičajenog Observer ciklusa, obično u
     * prozorima za povrat knjiga ili pregledu profila kupca.
     * </p>
     * @param user Korisnik čije posuđene knjige želimo prikazati.
     */
    public void populateBorrowedBooksList(Customer user) {
        emptyModel();
        if(!user.getPosudeneKnjige().isEmpty()){
            for(Knjiga k : user.getPosudeneKnjige()){
                listModel.addElement(k);
            }
        }

    }
}
