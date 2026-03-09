package BackEnd.DataAccessObject;

import BackEnd.DatabaseConnectionManager;
import BackEnd.Librarian;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za pristup podacima (DAO - Data Access Object) za entitet <b>Librarian</b>.
 * <p>
 * Centralno mjesto za upravljanje podacima o zaposlenicima (knjižničarima) unutar MySQL baze podataka.
 * Implementira <b>Singleton uzorak</b> (Bill Pugh varijanta) za optimiziran i
 * nitno-siguran pristup tablici {@code Librarians}.
 * </p>
 * <b>Ključne odgovornosti i operacije (CRUD):</b>
 * <ul>
 * <li><b>Registriranje:</b> Registriranje novih knjižničara.</li>
 * <li><b>Čitanje:</b> Dohvat svih knjižničara i mapiranje iz {@link ResultSet} u {@link Librarian} objekte.</li>
 * <li><b>Ažuriranje:</b> Ažuriranje podataka o korisniku</li>
 * <li><b>Brisanje:</b> Trajno uklanjanje knjižničara iz baze prema ID ključu.</li>
 * </ul>
 * <b>Tehnička implementacija:</b>
 * <ul>
 * <li><b>JDBC standard:</b> Korištenje {@link PreparedStatement} za zaštitu od SQL Injection napada.</li>
 * <li><b>Resource Management:</b> Automatsko zatvaranje resursa putem <i>try-with-resources</i> mehanizma.</li>
 * <li><b>Povezanost:</b> Ovisi o {@link DatabaseConnectionManager} za stabilnu vezu sa serverom.</li>
 * </ul>
 */
public class LibrarianDAO implements IntLibrarianDAO{

    /**
     * Privatni konstruktor - sprječava stvaranje novih instanci izvan klase.
     */
    private LibrarianDAO() {}

    /**
     * Statička unutarnja klasa koja drži instancu.
     * <p>
     * Inicijalizira se tek kod prvog poziva {@link #getInstance()} metode (Lazy Initialization).
     * </p>
     */
    private static class Holder {
        private static final LibrarianDAO INSTANCE = new LibrarianDAO();
    }

    /**
     * @return Jedinstvena instanca LibrarianDAO klase.
     */
    public static LibrarianDAO getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Mapira retke iz tablice {@code Librarians} u objekte klase {@link Librarian},
     * uključujući provjeru administratorskih prava i lozinki.
     * </p>
     */
    @Override
    public List<Librarian> getAll() {
        List<Librarian> librarians = new ArrayList<>();
        String sql = "SELECT * FROM Librarians";

        Connection conn = DatabaseConnectionManager.getInstance().getConnection();

        if(conn != null) {
            // Try-with-resources automatski zatvara resurse čak i ako se dogodi Exception
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    // Izvlačimo sve podatke (User dio + Librarian dio)
                    int id = rs.getInt("id");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    int dob = rs.getInt("dob");
                    String grad = rs.getString("grad");
                    String adresa = rs.getString("adresa_stanovanja");
                    String user = rs.getString("username");
                    String pass = rs.getString("password");
                    boolean admin = rs.getBoolean("is_admin");

                    // Koristimo tvoj konstruktor (pripazi na redoslijed parametara u tvojoj klasi!)
                    Librarian l = new Librarian(user, pass, ime, prezime, dob, grad, adresa, admin);
                    l.setId(id); // Postavljamo ID iz baze

                    librarians.add(l);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return librarians;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Izvršava SQL {@code INSERT} naredbu. Lozinke se spremaju u tekstualnom obliku,
     * a novi knjižničari po zadanim postavkama mogu imati ili nemati admin status
     * ovisno o proslijeđenom objektu.
     * </p>
     */
    @Override
    public boolean insert(Librarian l) {
        String sql = "INSERT INTO Librarians (ime, prezime, dob, grad, adresa_stanovanja, username, password, is_admin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnectionManager.getInstance().getConnection();
        if(conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // Postavljanje parametara za SQL upit (brojanje kreće od 1)
                pstmt.setString(1, l.getIme());
                pstmt.setString(2, l.getPrezime());
                pstmt.setInt(3, l.getDob());
                pstmt.setString(4, l.getGrad());
                pstmt.setString(5, l.getAdresaStanovanja());
                pstmt.setString(6, l.getUsername());
                pstmt.setString(7, l.getPassword());
                pstmt.setBoolean(8, l.isAdmin());

                pstmt.executeUpdate();

                // Dohvaćanje AUTO_INCREMENT ključa koji je baza generirala
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        l.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
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
     * Koristi {@code PreparedStatement} za sigurno brisanje zapisa prema primarnom
     * ključu {@code id} iz tablice {@code Librarians}.
     * </p>
     */
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Librarians WHERE id = ?";
        Connection conn = DatabaseConnectionManager.getInstance().getConnection();
        if(conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
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
     * Ova metoda je kritična za sigurnost jer omogućuje promjenu lozinke
     * ({@code password}) i razine pristupa ({@code is_admin}).
     * </p>
     */
    @Override
    public boolean update(Librarian l) {
        String sql = "UPDATE Librarians SET password = ?, is_admin = ? WHERE id = ?";
        Connection conn = DatabaseConnectionManager.getInstance().getConnection();
        if(conn != null) {
            try (
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, l.getPassword());
                pstmt.setBoolean(2, l.isAdmin());
                pstmt.setInt(3, l.getId());
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