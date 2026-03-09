package BackEnd;

import java.sql.Connection;

/**
 * Sučelje koje definira standardni ugovor za upravljanje vezom s bazom podataka.
 * <p>
 * Osigurava visoku razinu apstrakcije nad procesima uspostave i terminacije mrežnih sesija,
 * odvajajući logiku spajanja od ostatka poslovne logike aplikacije.
 * </p>
 * <b>Ključne arhitektonske prednosti:</b>
 * <ul>
 * <li><b>Zamjenjivost (Pluggability):</b> Omogućuje laku promjenu tehnologije baze podataka
 * (npr. prelazak s MySQL na SQLite ili PostgreSQL) uz minimalne izmjene u kodu.</li>
 * <li><b>Testabilnost:</b> Olakšava kreiranje <i>Mock</i> objekata za jedinično testiranje bez stvarne baze.</li>
 * <li><b>Standardizacija:</b> Definira fiksni skup metoda koje svaka implementacija mora podržavati.</li>
 * </ul>
 * <b>Metode ugovora:</b>
 * <ul>
 * <li>{@link #connect()}</li>
 * <li>{@link #disconnect()}</li>
 * <li>{@link #isConnectionAlive()}</li>
 * <li>{@link #getConnection()}</li>
 * </ul>
 */
public interface DBConnectionManagerInterface {

    /**
     * Uspostavlja vezu s udaljenim ili lokalnim poslužiteljem baze podataka.
     * @return true ako je veza uspješno otvorena, inače false.
     */
    boolean connect();

    /**
     * Sigurno prekida trenutnu sesiju i oslobađa resurse konekcije.
     */
    boolean disconnect();

    /**
     * Provjerava je li veza i dalje aktivna i funkcionalna.
     * @return {@code true} ako je komunikacija s bazom moguća.
     */
    boolean isConnectionAlive();

    /**
     * Vraća sirovi JDBC objekt konekcije potreban za izvršavanje SQL upita.
     * @return Instanca {@link Connection}.
     */
    Connection getConnection();
}
