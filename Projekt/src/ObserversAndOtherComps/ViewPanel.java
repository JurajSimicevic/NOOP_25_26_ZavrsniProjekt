package ObserversAndOtherComps;

import BackEnd.*;
import BackEnd.Management.LibraryManager;
import BackEnd.Management.LibraryManagerInterface;
import GUI_Comps.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Apstraktna generička klasa koja služi kao temelj za sve vizualne liste u sustavu.
 * <p>
 * Implementira {@link Observer} sučelje, što joj omogućuje da automatski reagira na promjene
 * u bazi podataka (npr. dodavanje nove knjige ili kupca) bez ručnog osvježavanja.
 * </p>
 * <b>Ključne značajke:</b>
 * <ul>
 * <li><b>Generički dizajn:</b> Radi s bilo kojim tipom objekta (Knjiga, Customer, Librarian).</li>
 * <li><b>Automatsko ažuriranje:</b> Kroz metodu {@link #update()} sinkronizira prikaz s podacima iz {@link LibraryManager}.</li>
 * <li><b>Scroll podrška:</b> Automatski integrira {@link JScrollPane} za upravljanje velikim setovima podataka.</li>
 * </ul>
 *
 * @param <E> Tip objekta koji se prikazuje u listi (npr. Knjiga ili Customer).
 */
public abstract class ViewPanel<E> extends BasePanel implements Observer {

    protected JScrollPane textAreaScrollPane;
    protected DefaultListModel<E> listModel;
    protected JList<E> list;
    protected List<E> objects;
    protected ViewPanelListener<E> listener;
    protected FilePathEnum filePathEnum;

    /**
     * Konstruktor s osnovnim dimenzijama (200x150).
     * @param filePathEnum Tip podataka koji ovaj panel prikazuje (definira izvor podataka).
     */
    protected ViewPanel(FilePathEnum filePathEnum){
        setupPanel(filePathEnum, 200, 150);
    }

    /**
     * Konstruktor koji omogućuje prilagodbu veličine panela.
     * @param filePathEnum Tip podataka.
     * @param height Preferirana visina panela.
     * @param width Preferirana širina panela.
     */
    protected ViewPanel(FilePathEnum filePathEnum, int height, int width) {
        setupPanel(filePathEnum, height, width);
    }

    /**
     * Pomoćna metoda za konfiguraciju panela i registraciju kod subjekta.
     * <p>
     * Postavlja dimenzije i automatski dodaje ovaj panel u listu promatrača
     * unutar {@link LibraryManager}-a.
     * </p>
     */
    private void setupPanel(FilePathEnum filePathEnum, int height, int width) {
        this.filePathEnum = filePathEnum;
        setPreferredSize(new Dimension(width, height));

        LibraryManager.getInstance().addObserver(this);
        updateModel(fetchLatestData());// Prvo punjenje podataka
    }

    /**
     * {@inheritDoc}
     * <p>
     * Kreira {@link JScrollPane} koji udomljuje {@link JList} komponentu.
     * </p>
     */
    @Override
    protected void initComps() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        textAreaScrollPane = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Postavlja listu preko cijele površine panela koristeći {@link BorderLayout}.
     * </p>
     */
    @Override
    protected void layoutComps() {
        setLayout(new BorderLayout());
        add(textAreaScrollPane, BorderLayout.CENTER);
    }

    /**
     * Postavlja specijalizirani listener za interakciju s elementima liste.
     * @param listener Implementacija {@link ViewPanelListener} sučelja.
     */
    public void setViewPanelListener(ViewPanelListener<E> listener) {
        this.listener = listener;
    }

    /**
     * Dohvaća trenutno selektirani objekt iz liste.
     * @return Odabrani objekt tipa {@code E} ili {@code null} ako ništa nije odabrano.
     */
    public E getSelectedValue(){
        E selected = list.getSelectedValue();
        if(selected != null) {
            return selected;
        }
        return null;
    }

    /**
     * Interna metoda za osvježavanje vizualnog modela liste.
     * @param list Nova lista podataka pristigla iz baze/managera.
     */
    private void updateModel(List<E> list){
        listModel.clear();
        objects = list;
        for (E c : objects) {
            listModel.addElement(c);
        }
    }

    /** @return Referenca na unutrašnju {@link JList} komponentu. */
    public JList<E> getList(){
        return list;
    }

    /** @return Referenca na model podataka liste. */
    public DefaultListModel<E> getListModel(){
        return listModel;
    }

    /**
     * {@inheritDoc}.
     * <p>Poziva ju {@link LibraryManager} kada dođe do promjene podataka.</p>
     * <p>
     * Implementacija Observer sučelja. Forsira ponovno dohvaćanje podataka
     * i ažuriranje UI-ja na Event Dispatch Threadu.
     * </p>
     */
    @Override
    public void update() {
        updateModel(fetchLatestData());
    }

    /**
     * Dohvaća najnovije podatke iz {@link LibraryManager}-a ovisno o tipu panela.
     * <p>
     * Koristi {@code filePathEnum} kako bi odredio treba li povući filtrirane knjige,
     * kupce ili zaposlenike.
     * </p>
     * @return Lista objekata tipa {@code E} spremna za prikaz.
     */
    @SuppressWarnings("unchecked")
    private List<E> fetchLatestData() {
        LibraryManagerInterface manager = LibraryManager.getInstance();

        if (filePathEnum == FilePathEnum.KNJIGE) {
            return (List<E>) manager.getFilteredBooks(manager.getBookFilter());
        }

        if (filePathEnum == FilePathEnum.CUSTOMERS) {
            return (List<E>) manager.getFilteredCustomers(manager.getCustomerFilter());
        }

        if (filePathEnum == FilePathEnum.LIBRARIANS) {
            return (List<E>) manager.getFilteredLibrarians(manager.getLibrarianFilter());
        }

        return new ArrayList<>();
    }
}
