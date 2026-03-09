package BackEnd;

import BackEnd.Management.LibraryManager;

import javax.swing.*;

/**
 * Singleton klasa zadužena za upravljanje aktivnom sesijom korisnika.
 * <p>
 * Djeluje kao centralni autoritet za autentifikaciju i autorizaciju. Čuva referencu na
 * trenutno prijavljenog {@link Librarian}-a te omogućuje provjeru administratorskih
 * ovlasti kroz cijelu aplikaciju.
 * </p>
 * <b>Mehanizam implementacije:</b>
 * <ul>
 * <li><b>Bill Pugh Singleton:</b> Koristi statičku unutarnju klasu (<i>Holder</i>) za
 * <i>thread-safe</i> i <i>lazy</i> inicijalizaciju instance bez potrebe za sinkronizacijom.</li>
 * <li><b>Session State:</b> Drži stanje prijave tijekom cijelog životnog ciklusa aplikacije.</li>
 * </ul>
 * <b>Ključne odgovornosti:</b>
 * <ul>
 * <li><b>Autentifikacija:</b> Provjera vjerodajnica putem {@link LibraryManager}-a uz
 * prethodnu provjeru veze s bazom preko {@link DatabaseConnectionManager}.</li>
 * <li><b>Autorizacija:</b> Metoda {@link #isCurrentUserAdmin()} služi za dinamičko
 * zaključavanje/otključavanje naprednih funkcija u GUI-ju.</li>
 * <li><b>Logout:</b> Sigurno uništavanje sesije postavljanjem korisnika na {@code null}.</li>
 * </ul>
 */
public class SessionManager implements  SessionManagerInterface{

    /** Trenutno prijavljeni {@code Librarian}. Ako je {@code null}, nitko nije prijavljen. */
    private Librarian loggedInUser;

    /** Privatni konstruktor za Singleton uzorak. */
    private SessionManager() {}

    /** Bill Pugh Singleton holder koji osigurava thread-safe instancu. */
    private static class Holder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    /** @return Jedinstvena instanca {@code SessionManager}-a. */
    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link DatabaseConnectionManager} {@code connect()} i provjerava je li operacija uspješna.</p>
     * Ako se uspio povezati prolazi kroz listu svih knjižničara učitanih u {@link LibraryManager}-u
     * i provjerava postoji li knjižničar sa unesenim {@code username} i {@code password}.
     * */
    @Override
    public boolean authenticate(String username, String password) {
        // 1. OBAVEZNO PRVO SPAJANJE NA BAZU (Prije nego itko dotakne LibraryManager!)
        if (!DatabaseConnectionManager.getInstance().isConnectionAlive()) {
            if (!DatabaseConnectionManager.getInstance().connect()) {
                JOptionPane.showMessageDialog(null, "Greška pri spajanju na server...", "Greška", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // 2. TEK SADA dohvaćamo managere (koji će sada vidjeti živu konekciju i učitati podatke)
        if(search4User(username, password)){
            return true;
        }

        // 3. Ako korisnik nije nađen
        JOptionPane.showMessageDialog(null, "Neispravno korisničko ime ili lozinka!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    /**
     * {@inheritDoc}
     * @return {@link #loggedInUser}
     */
    @Override
    public Librarian getLoggedInUser() { return loggedInUser; }

    /** {@inheritDoc} */
    @Override
    public void setLoggedInUser(Librarian user) {
        this.loggedInUser = user;
    }

    /**{@inheritDoc}*/
    @Override
    public boolean isCurrentUserAdmin() {
        return loggedInUser != null && loggedInUser.isAdmin();
    }

    /**{@inheritDoc}*/
    @Override
    public void logout() { this.loggedInUser = null; }

    private boolean search4User(String username, String password) {
        for (Librarian l : LibraryManager.getInstance().getLibrarians()) {
            if (l.getUsername().equals(username) && l.getPassword().equals(password)) {
                this.loggedInUser = l;
                return true;
            }
        }
        return false;
    }
}