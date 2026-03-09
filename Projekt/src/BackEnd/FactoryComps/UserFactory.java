package BackEnd.FactoryComps;

import BackEnd.User;

/**
 * Apstraktna tvornica (Abstract Factory) za kreiranje korisnika sustava.
 * <p>
 * Ovo sučelje definira zajednički ugovor za sve konkretne tvornice korisnika
 * (npr. {@code CustomerFactory} i {@code LibrarianFactory}). Omogućuje ostatku
 * aplikacije da kreira objekte koji nasljeđuju klasu {@link User} bez poznavanja
 * njihove točne klase ili detalja instanciranja.
 * </p>
 * * <b>Ključne prednosti:</b>
 * <ul>
 * <li><b>Polimorfizam:</b> Metoda vraća bazni tip {@link User}, što omogućuje
 * univerzalno rukovanje različitim tipovima korisnika.</li>
 * <li><b>Decoupling:</b> Razdvaja kôd korisničkog sučelja od konkretnih
 * konstruktora klasa.</li>
 * <li><b>Proširivost:</b> Lako dodavanje novih tipova korisnika bez promjene
 * postojećeg koda koji koristi ovo sučelje.</li>
 * </ul>
 */
public interface UserFactory {

    /**
     * Kreira i vraća novu instancu korisnika na temelju zajedničkih osobnih podataka.
     * @param ime      Ime korisnika.
     * @param prezime  Prezime korisnika.
     * @param dob      Dob korisnika (godine).
     * @param grad     Grad stanovanja.
     * @param adresa   Ulica i kućni broj.
     * @return         Objekt koji nasljeđuje klasu {@link User} (konkretno {@code Customer} ili {@code Librarian}).
     */
    User createUser(String ime, String prezime, int dob, String grad, String adresa);
}