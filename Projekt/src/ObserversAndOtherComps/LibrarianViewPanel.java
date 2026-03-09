package ObserversAndOtherComps;

import BackEnd.FilePathEnum;
import BackEnd.Librarian;

/**
 * Specijalizirani panel za prikaz knjižničara (zaposlenika).
 * <p>
 * Namijenjen isključivo administratorskom sučelju za pregled osoblja.
 * Sinkroniziran s {@link FilePathEnum#LIBRARIANS}.
 * </p>
 */
public class LibrarianViewPanel extends ViewPanel<Librarian> {

    public LibrarianViewPanel() {
        super(FilePathEnum.LIBRARIANS);
    }

    public LibrarianViewPanel( int height, int width) {
        super(FilePathEnum.LIBRARIANS, height, width);
    }
}
