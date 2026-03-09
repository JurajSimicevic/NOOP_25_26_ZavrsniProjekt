package BackEnd.DataAccessObject;

import BackEnd.Customer;
import BackEnd.DatabaseConnectionManager;
import BackEnd.FactoryComps.CustomerFactory;
import BackEnd.FactoryComps.UserFactory;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za pristup podacima (DAO - Data Access Object) za entitet <b>Customer</b>.
 * <p>
 * Implementira JDBC operacije za interakciju s tablicom {@code Customers} u MySQL bazi podataka.
 * Osigurava da se svi podaci o korisnicima biblioteke trajno čuvaju i ispravno mapiraju u Java objekte.
 * </p>
 * <b>Glavne funkcionalnosti:</b>
 * <ul>
 * <li><b>CRD operacije:</b> Potpuna podrška za kreiranje, čitanje  i brisanje korisnika.</li>
 * <li><b>Auto-increment rukovanje:</b> Podržava automatsko generiranje primarnih ID ključeva pri unosu novih zapisa.</li>
 * <li><b>Mapiranje:</b> Transformacija rezultata SQL upita ({@link ResultSet}) u {@link Customer} objekte.</li>
 * </ul>
 * <b>Obrasci dizajna i sigurnost:</b>
 * <ul>
 * <li><b>Singleton:</b> Koristi Bill Pugh varijantu za nitno-siguran (thread-safe) pristup instanci.</li>
 * <li><b>SQL Zaštita:</b> Primjena {@link PreparedStatement} za prevenciju SQL Injection napada.</li>
 * <li><b>Resursi:</b> Implementacija <i>try-with-resources</i> bloka za sprječavanje curenja memorije i konekcija.</li>
 * </ul>
 */
public class CustomerDAO implements IntCustomerDAO {

    private UserFactory customerFactory = CustomerFactory.getInstance();
    /**
     * Privatni konstruktor onemogućava instanciranje klase izvan nje same.
     */
    private CustomerDAO() {}

    /**
     * Držač instance (Bill Pugh Singleton).
     * Osigurava thread-safe inicijalizaciju bez potrebe za sinkronizacijom.
     */
    private static class Holder {
        private static final CustomerDAO INSTANCE = new CustomerDAO();
    }

    /**
     * @return Jedinstvena instanca CustomerDAO klase.
     */
    public static CustomerDAO getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementacija koristi {@link ResultSet} za mapiranje redaka iz tablice
     * {@code Customers} u objekte koristeći {@code CustomerFactory}.
     * </p>
     */
    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        Connection conn = DatabaseConnectionManager.getInstance().getConnection();
        if (conn != null) {
            // Koristimo try-with-resources za automatsko zatvaranje Statementa i ResultSet-a
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Izvlačimo podatke iz baze (uključujući one iz User klase)
                int id = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                int dob = rs.getInt("dob");
                String grad = rs.getString("grad");
                String adresa = rs.getString("adresa_stanovanja");

                // Kreiramo objekt (pazi: id postavljamo ručno jer baza diktira ID)
                Customer c = (Customer) customerFactory.createUser(ime, prezime, dob, grad, adresa);
                c.setId(id); // Dodaj setId metodu u User ili Customer klasu ako je nemaš

                customers.add(c);
            }
        } catch (SQLException e) {
            // Logiranje greške u slučaju neuspjelog upita
            e.printStackTrace();
        }
            return customers;
        }
        return customers;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Metoda koristi {@code Statement.RETURN_GENERATED_KEYS} kako bi nakon uspješnog
     * unosa dohvatila automatski generirani ID iz baze i dodijelila ga proslijeđenom
     * objektu {@link Customer}.
     * </p>
     */
    @Override
    public boolean insert(Customer c) {
        String sql = "INSERT INTO Customers (ime, prezime, dob, grad, adresa_stanovanja) VALUES (?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnectionManager.getInstance().getConnection();

        if (conn != null) {
            // Drugi parametar omogućava dohvaćanje AUTO_INCREMENT vrijednosti iz baze
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, c.getIme());
                pstmt.setString(2, c.getPrezime());
                pstmt.setInt(3, c.getDob());
                pstmt.setString(4, c.getGrad());
                pstmt.setString(5, c.getAdresaStanovanja());

                pstmt.executeUpdate();

                // Dohvaćamo ID koji je baza automatski dodijelila
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Ažuriramo Java objekt s ID-om iz baze kako bi bio konzistentan
                        c.setId(generatedKeys.getInt(1));
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
     * Izvršava parametrizirani SQL {@code DELETE} upit nad tablicom {@code Customers}
     * prema primarnom ključu {@code id}.
     * </p>
     */
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Customers WHERE id = ?";
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
}