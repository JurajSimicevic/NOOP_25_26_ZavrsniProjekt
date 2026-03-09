package BackEnd;


/**
 * <b>Record</b> klasa koja služi kao centralni spremnik za konfiguraciju pristupa bazi podataka.
 * <p>
 * Pohranjuje parametre poput adrese poslužitelja, porta, naziva baze te vjerodajnica (korisničko ime i lozinka).
 * Osnovna svrha je eliminacija <i>hard-codinga</i> parametara unutar izvršne logike sustava.
 * </p>
 * <b>Prednosti korištenja Recorda:</b>
 * <ul>
 * <li><b>Immutability:</b> Podaci su nepromjenjivi nakon kreiranja, što osigurava stabilnost konfiguracije.</li>
 * <li><b>Compactness:</b> Automatski generira konstruktor, <i>gettere</i>, {@code equals()}, {@code hashCode()} i {@code toString()} metode.</li>
 * <li><b>Decoupling:</b> Omogućuje lako prosljeđivanje postavki unutar {@link DatabaseConnectionManager}-a.</li>
 * </ul>
 * <b>Parametri konekcije:</b>
 * <ul>
 * <li>{@code host}: Mrežna adresa poslužitelja baze podataka.</li>
 * <li>{@code port}: Port na kojem baza sluša (obično 3306 za MySQL).</li>
 * <li>{@code dbName}: Naziv specifične sheme baze podataka.</li>
 * <li>{@code username/password}: Vjerodajnice za autentifikaciju.</li>
 * </ul>
 */
public record ConnectionInfo() {
    /** Adresa MySQL poslužitelja (Aiven Cloud host). */
    static String host = "mysql-3c6edc95-vjezba8mvc.k.aivencloud.com";

    /** Port na kojem MySQL servis sluša (standardni je obično 3306, ovdje je 24812). */
    static String port = "24812";

    /** Naziv baze podataka kojoj se pristupa. */
    static String databaseName = "defaultdb";

    /** Korisničko ime za autentifikaciju na bazu. */
    static String userName = "avnadmin";

    /** Lozinka za pristup bazi podataka. */
    static String password = "AVNS_jZh9yHGDlCmtA0H2yeC";
}
