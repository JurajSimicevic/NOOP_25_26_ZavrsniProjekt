package ObserversAndOtherComps;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Prilagođeno polje za pretragu (JTextField) koje omogućuje filtriranje podataka u stvarnom vremenu.
 * <p>
 * Implementira {@link DocumentListener} kako bi reagiralo na svaku promjenu teksta
 * (unos, brisanje ili izmjena). Polje je usko povezano s {@link LibraryManager}-om
 * kojemu šalje kriterije pretrage.
 * </p>
 * <b>Mehanizam rada:</b>
 * <ul>
 * <li>Svaki put kada korisnik tipka, poziva se metoda {@code filter()}.</li>
 * <li>Manager ažurira svoje filtere, što pokreće {@code notifyObservers()} proces.</li>
 * <li>Povezani {@link ViewPanel}-i se automatski osvježavaju s filtriranim podacima.</li>
 * </ul>
 */
public class SearchField extends JTextField{

    private FilePathEnum filePathEnum;

    /**
     * Konstruktor za standardno polje pretrage.
     * @param filePathEnum Definira kategoriju podataka koju ovo polje filtrira (Knjige, Kupci ili Knjižničari).
     */
    public SearchField(FilePathEnum filePathEnum) {
        super();
        setup(filePathEnum);
    }

    /**
     * Konstruktor koji omogućuje postavljanje širine polja.
     * @param filePathEnum Kategorija podataka za filtriranje.
     * @param columns Broj vidljivih stupaca (širina polja).
     */
    public SearchField(FilePathEnum filePathEnum, int columns) {
        super(columns);
        setup(filePathEnum);
    }

    /**
     * Konfigurira polje i pokreće osluškivanje promjena u dokumentu.
     */
    private void setup(FilePathEnum filePathEnum){
        this.filePathEnum = filePathEnum;
        activate();
    }

    /**
     * Registrira {@link DocumentListener} na unutrašnji model teksta.
     */
    private void activate() {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter(getText());
            }
        });
    }

    /**
     * Prosljeđuje uneseni tekst u {@link LibraryManager} ovisno o tipu pretrage.
     * @param text Trenutni sadržaj polja za pretragu.
     */
    private void filter(String text) {
        LibraryManagerInterface manager = LibraryManager.getInstance();

        if (filePathEnum == FilePathEnum.KNJIGE) {
            manager.setBookFilter(text);
        } else if (filePathEnum == FilePathEnum.CUSTOMERS) {
            manager.setCustomerFilter(text);
        } else if (filePathEnum == FilePathEnum.LIBRARIANS) {
            manager.setLibrarianFilter(text);
        }
    }

}
