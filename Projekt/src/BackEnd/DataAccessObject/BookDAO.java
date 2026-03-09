package BackEnd.DataAccessObject;

import BackEnd.DatabaseConnectionManager;
import BackEnd.Knjiga;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za pristup podacima (DAO - Data Access Object) za entitet <b>Knjiga</b>.
 * <p>
 * Centralno mjesto za izvršavanje SQL upita nad tablicom {@code Books} u MySQL bazi.
 * Implementira <b>Singleton uzorak</b> (Bill Pugh varijanta) kako bi se osigurala
 * nitna sigurnost i jedinstvena točka pristupa podacima.
 * </p>
 * <b>Glavne odgovornosti (CRUD):</b>
 * <ul>
 * <li><b>Čitanje:</b> Dohvat svih knjiga i mapiranje iz {@link ResultSet} u {@link Knjiga} objekte.</li>
 * <li><b>Pisanje:</b> Perzistencija novih naslova putem {@code PreparedStatement} zaštite.</li>
 * <li><b>Ažuriranje:</b> Sinkronizacija statusa posudbe i identifikatora korisnika (Customer ID).</li>
 * <li><b>Brisanje:</b> Trajno uklanjanje knjiga iz kataloga prema ISBN ključu.</li>
 * </ul>
 * <b>Tehničke napomene:</b>
 * <ul>
 * <li>Koristi {@link DatabaseConnectionManager} za dohvat aktivnih konekcija.</li>
 * <li>Implementira <i>try-with-resources</i> za automatsko upravljanje resursima.</li>
 * <li>Štiti sustav od SQL Injection napada korištenjem parametriziranih upita.</li>
 * </ul>
 */

public class BookDAO implements IntBookDAO {

    /**
     * Privatan konstruktor sprječava stvaranje novih instanci izvan ove klase.
     * Ovo je ključni dio Singleton uzorka.
     */
    private BookDAO() {}

    /**
     * Statička unutarnja klasa (Bill Pugh Singleton).
     * <p>
     * Instanca se stvara tek kada se prvi put pozove metoda getInstance().
     * Ovo je nitno-siguran (thread-safe) i učinkovit način implementacije Singletona.
     * </p>
     */
    private static class Holder {
        private static final BookDAO INSTANCE = new BookDAO();
    }

    /**
     * Jedinstvena instanca BookDAO klase.
     * @return Jedinstvena instanca BookDAO klase.
     */
    public static BookDAO getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementacija koristi SQL {@code SELECT * FROM Books} upit i mapira
     * svaki redak rezultata u novu instancu klase {@link Knjiga}.
     * </p>
     */
    @Override
    public List<Knjiga> getAll() {
        List<Knjiga> knjige = new ArrayList<>();
        String sql = "SELECT * FROM Books";

        // Dohvaćanje konekcije preko managera
        Connection conn = DatabaseConnectionManager.getInstance().getConnection();

        if(conn != null) {
            // try-with-resources automatski zatvara Statement i ResultSet nakon završetka
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    // 1. Mapiranje stupaca iz baze u lokalne varijable
                    String isbn = rs.getString("isbn");
                    String nazivDjela = rs.getString("naziv_djela");
                    String autor = rs.getString("autor");
                    String vrstaDjela = rs.getString("vrsta_djela");
                    double cijena = rs.getDouble("cijena");

                    // 2. Kreiranje Java objekta na temelju podataka iz baze
                    Knjiga k = new Knjiga(autor, nazivDjela, isbn, vrstaDjela, cijena);

                    // 3. Postavljanje statusa posudbe ako knjiga nije dostupna
                    boolean posudjena = rs.getBoolean("posudjena");
                    int danaPosudbe = rs.getInt("dana_posudbe");

                    if (posudjena) {
                        k.posudi(danaPosudbe); // Koristimo metodu posudi()
                    }

                    // 4. Ako je knjiga posuđena, povezujemo je s kupcem
                    int customerId = rs.getInt("customer_id");
                    if (!rs.wasNull()) {
                        k.setTmpCustomerId(customerId); // Privremeno spremanje ID-a za kasnije spajanje
                    } else {
                        k.setTmpCustomerId(-1); // Označavamo da knjiga nije kod nikoga
                    }

                    knjige.add(k);
                }
            } catch (SQLException e) {
                // Ispis greške u konzolu
                e.printStackTrace();
            }
        }
        return knjige;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Koristi {@code PreparedStatement} za izvršavanje {@code INSERT} naredbe
     * kako bi se spriječili SQL Injection napadi. Nova knjiga se inicijalno
     * postavlja kao dostupna (posudjena = false).
     * </p>
     */
    @Override
    public boolean insert(Knjiga k) {
        // PreparedStatement sprječava SQL Injection napade
        String sql = "INSERT INTO Books (isbn, naziv_djela, autor, vrsta_djela, cijena, posudjena, dana_posudbe) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnectionManager.getInstance().getConnection();
        if(conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Postavljanje parametara na mjesta upitnika (?)
                pstmt.setString(1, k.getIsbn());
                pstmt.setString(2, k.getNazivDjela());
                pstmt.setString(3, k.getAutor());
                pstmt.setString(4, k.getVrstaDjela());
                pstmt.setDouble(5, k.getCijena());
                pstmt.setBoolean(6, false); // Inicijalno, nova knjiga je uvijek dostupna
                pstmt.setInt(7, 0);         // Inicijalno, 0 dana posudbe

                // customer_id ostaje NULL u bazi po defaultu za novu knjigu

                pstmt.executeUpdate();  // Izvršavanje INSERT naredbe
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), this.getClass().getSimpleName() + " - SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Izvršava {@code DELETE} upit nad tablicom {@code Books} koristeći ISBN kao
     * primarni ključ za identifikaciju retka.
     * </p>
     */
    @Override
    public boolean delete(String isbn) {
        String sql = "DELETE FROM Books WHERE isbn = ?";
        Connection conn = DatabaseConnectionManager.getInstance().getConnection();

        if(conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, isbn);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), this.getClass().getSimpleName() + " - SQL Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sinkronizira polja {@code posudjena}, {@code dana_posudbe} i {@code customer_id}
     * u bazi. Ako je {@code customerId} parametar {@code null}, polje u bazi se
     * postavlja na {@code NULL} putem {@code Types.INTEGER}.
     * </p>
     */
    @Override
    public boolean updateBorrowStatus(Knjiga k, Integer customerId) {
        String sql = "UPDATE Books SET posudjena = ?, dana_posudbe = ?, customer_id = ? WHERE isbn = ?";

        Connection conn = DatabaseConnectionManager.getInstance().getConnection();
        if(conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setBoolean(1, k.isPosudjena());
                pstmt.setInt(2, k.getDanaPosudbe());

                // Provjera je li proslijeđen ID kupca ili null
                if (customerId == null) {
                    pstmt.setNull(3, Types.INTEGER); // Ako vraća knjigu, customer_id postaje NULL
                } else {
                    pstmt.setInt(3, customerId);     // Ako posuđuje, upisujemo ID kupca
                }

                pstmt.setString(4, k.getIsbn());

                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), this.getClass().getSimpleName() + " - SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
}
