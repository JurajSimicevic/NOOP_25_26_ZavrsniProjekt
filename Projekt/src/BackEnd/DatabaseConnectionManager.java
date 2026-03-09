package BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Singleton klasa zadužena za centralizirano upravljanje vezom s MySQL bazom podataka.
 * <p>
 * Implementira {@link DBConnectionManagerInterface} i osigurava robustan životni ciklus
 * mrežne veze, uključujući sigurno otvaranje, zatvaranje te kontinuiranu provjeru statusa.
 * </p>
 * <b>Ključne značajke sustava veza:</b>
 * <ul>
 * <li><b>SSL Enkripcija:</b> Koristi parametre iz {@link ConnectionInfo} za uspostavu sigurne komunikacije s <i>Cloud</i> poslužiteljem.</li>
 * <li><b>Singleton Pattern:</b> Osigurava postojanje samo jedne aktivne instance managera kroz cijeli životni vijek aplikacije.</li>
 * <li><b>Resource Safety:</b> Sprječava curenje resursa (<i>Connection Leaks</i>) preciznim metodama za terminaciju sesije.</li>
 * </ul>
 * <b>Funkcionalnosti sučelja:</b>
 * <ul>
 * <li>{@code connect()}: Inicijalizira JDBC driver i uspostavlja aktivni kanal prema bazi.</li>
 * <li>{@code disconnect()}: Sigurno zatvara postojeću konekciju i oslobađa resurse sustava.</li>
 * <li>{@code isConnectionAlive()}: Provjerava integritet veze prije izvršavanja upita.</li>
 * </ul>
 */
public class DatabaseConnectionManager implements DBConnectionManagerInterface {

    /** Objekt JDBC konekcije {@link Connection} koji se dijeli kroz cijelu aplikaciju. */
    private Connection connection;

    // Parametri iz centralne ConnectionInfo klase za spajanje na bazu
    private String host = ConnectionInfo.host;
    private String port = ConnectionInfo.port;
    private String databaseName = ConnectionInfo.databaseName;
    private String userName = ConnectionInfo.userName;
    private String password = ConnectionInfo.password;

    /** Interna zastavica koja prati logičko stanje povezanosti. */
    private boolean isConnected;

    /**
     * Bill Pugh Singleton implementacija.
     * <p>
     * Privatna statička klasa osigurava da se instanca managera stvori
     * tek kod prvog poziva, što optimizira resurse.
     * </p>
     */
    private static class Holder {
        private static final DatabaseConnectionManager INSTANCE = new DatabaseConnectionManager();
    }

    /**
     * @return Globalno dostupna instanca managera konekcije.
     */
    public static DBConnectionManagerInterface getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * U ovom slučaju uspostavlja vezu s MySQL poslužiteljem. Uključuje učitavanje JDBC drivera i konfiguraciju SSL-a (obavezno za Aiven Cloud).
     * </p>
     *
     * @return true ako je veza uspješno otvorena ili je već bila aktivna.
     */
    @Override
    public boolean connect() {
        try {
            // Ručno učitavanje MySQL drivera (potrebno u starijim verzijama Jave/JDBC-a)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Provjeravamo ako je veza već otvorena
            if (connection == null || connection.isClosed() || !isConnected) {
                // JDBC URL s dodanim sslmode=require parametrom za sigurnu vezu
                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslmode=require", userName, password);
                System.out.println("Uspješno spajanje na bazu.");
                isConnected = true;
                return isConnected;
            }
        } catch (SQLException e) {
            // Obavještavanje korisnika putem GUI-ja u slučaju mrežnih problema
            JOptionPane.showMessageDialog(null, "Pogreška pri spajanju na poslužitelj: " + e.getMessage(),
                    "Server Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            // Kritična greška: driver nedostaje u classpath-u
            throw new RuntimeException(e);
        }
        return isConnected = false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean disconnect() {
        if(isConnected) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    isConnected = false;
                    System.out.println("Veza s bazom prekinuta.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("returnNotConnectedMsg pokrenut");
            JOptionPane.showMessageDialog(null, "Već ste odspojeni!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnectionAlive() {
        try {
            // connection.isValid(2) šalje testni upit i čeka odgovor maksimalno 2 sekunde
            if(connection != null && !connection.isClosed() && connection.isValid(2) && isConnected){
                System.out.println("spojeno");
                return true;
            }
            System.out.println("odspojeno");
            return false;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>Sadrži logiku automatskog ponovnog spajanja (auto-reconnect) ako se veza prekine.</p>
     */
    @Override
    public Connection getConnection() {
        try {
            // 1. Provjera: Jesmo li uopće inicirali spajanje?
            if (!isConnected) {
                return null;
            }

            // 2. Provjera: Je li objekt konekcije mrtav iako mislimo da smo spojeni?
            if (connection == null || connection.isClosed()) {
                System.out.println("Konekcija je bila zatvorena, pokušavam se ponovno spojiti...");
                if (!connect()) { // Pokušaj se ponovno spojiti
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return connection;
    }

    /** Prikazuje generičku poruku o nedostupnosti baze. */
    private void returnNotConnectedMsg() {
        JOptionPane.showMessageDialog(null, "Niste povezani na bazu podataka!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
}