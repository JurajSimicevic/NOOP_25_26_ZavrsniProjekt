package BackEnd;

import BackEnd.Management.LibraryManager;

import java.io.Serial;
import java.io.Serializable;

/**
 * Klasa modela koja predstavlja knjigu kao osnovni entitet sustava.
 * <p>
 * Objedinjuje bibliografske podatke (autor, naziv, ISBN, cijenu, te vrstu djela) s poslovnom logikom
 * praćenja statusa zaduženja. Implementira {@link Serializable} radi
 * mogućnosti perzistencije i prijenosa stanja objekta.
 * </p>
 * <b>Atributi i identifikacija:</b>
 * <ul>
 * <li><b>ISBN:</b> Jedinstveni identifikator knjige na razini kataloga.</li>
 * <li><b>Poslovni podaci:</b> Autor, naziv, vrsta djela i cijena primjerka.</li>
 * <li><b>ID posuditelja:</b> Referenca na {@link Customer} objekt koji trenutno posjeduje knjigu.</li>
 * </ul>
 * <b>Logika zaduživanja:</b>
 * <ul>
 * <li><b>Status posudbe:</b> {@code boolean} indikator koji određuje je li knjiga dostupna na polici.</li>
 * <li><b>Relink mehanizam:</b> Služi za povezivanje s korisnikom nakon učitavanja iz baze podataka.</li>
 * </ul>
 */
public class Knjiga implements Serializable {

    /** Privremeni ID kupca iz baze, služi za kasnije povezivanje s objektom Customer.
     * <p>(potrebno za {@code relinkBooks2Customers()} u {@link LibraryManager})</p>
     * */
    private int tmpCustomerId;

    /** Ime i prezime autora knjige. */
    private String autor;

    /** Puni naslov književnog djela. */
    private String nazivDjela;

    /** International Standard Book Number - jedinstveni identifikator izdanja. */
    private String isbn;

    /** Kategorija ili žanr knjige (npr. Roman, Znanstvena fantastika). */
    private String vrstaDjela;

    /** Zastavica koja označava je li knjiga trenutno kod nekog korisnika. */
    private boolean posudjena;

    /** Broj dana na koji je knjiga izdana korisniku. */
    private int danaPosudbe;

    /** Tržišna cijena knjige u eurima. */
    private double cijena;

    /** Referenca na objekt {@link Customer} koji trenutno drži knjigu. */
    private Customer posudioCustomer;

    /** Identifikator verzije serijalizacije za kompatibilnost objekata. */
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * Konstruktor za inicijalizaciju novog objekta Knjiga.
     * @param autor         Ime autora knjige.
     * @param nazivDjela    Naslov knjige.
     * @param isbn          Jedinstveni ISBN broj.
     * @param vrstaDjela    Žanr ili vrsta knjige.
     * @param cijena        Prodajna/nabavna cijena knjige.
     */
    public Knjiga(String autor, String nazivDjela, String isbn, String vrstaDjela, double cijena) {
        this.autor = autor;
        this.nazivDjela = nazivDjela;
        this.isbn = isbn;
        this.vrstaDjela = vrstaDjela;
        this.posudjena = false;
        this.danaPosudbe = 0;
        this.cijena = cijena;
        this.tmpCustomerId = -1;
    }

    // --- Getteri i Setteri ---

    /**
     * Postavlja {@link #tmpCustomerId}
     * @param id ID kupca iz tablice "Books".
     */
    public void setTmpCustomerId(int id) { this.tmpCustomerId = id; }

    /**
     * Vraća {@link #tmpCustomerId}
     * @return  {@link #tmpCustomerId} */
    public int getTmpCustomerId() { return tmpCustomerId; }

    /**
     * Vraća {@link #autor}
     * @return {@link #autor}*/
    public String getAutor() {
        return autor;
    }

    /**
     * Vraća {@link #nazivDjela}
     * @return {@link #nazivDjela}*/
    public String getNazivDjela() {
        return nazivDjela;
    }

    /**
     * Vraća {@link #isbn}
     * @return {@link #isbn}*/
    public String getIsbn() {
        return isbn;
    }

    /**
     * Vraća {@link #cijena}
     * @return {@link #cijena}*/
    public double getCijena() {
        return cijena;
    }

    /**
     * Vraća {@link #vrstaDjela}
     * @return {@link #vrstaDjela}*/
    public String getVrstaDjela() {
        return vrstaDjela;
    }

    /**
     * Provjerava je li knjiga posuđena.
     * @return {@code true} ako je posuđena, inače {@code false}.
     */
    public boolean isPosudjena() {
        return posudjena;
    }

    /**
     * Vraća {@link #posudioCustomer}
     * @return {@link #posudioCustomer}*/
    public Customer getPosudioCustomer() {
        return posudioCustomer;
    }

    /** Povezuje konkretan {@link Customer} objekt s ovom knjigom.
     * @param customer kupac kojeg namještamo da je "posudio" knjigu.
     * */
    public void setPosudioCustomer(Customer customer) {
        this.posudioCustomer = customer;
    }

    /**
     * Vraća {@link #danaPosudbe}
     * @return {@link #danaPosudbe}*/
    public int getDanaPosudbe() {
        return danaPosudbe;
    }

    /**
     * Postavlja status knjige na posuđenu i definira broj dana posudbe.
     * @param dana Broj dana na koje se knjiga posuđuje.
     */
    public void posudi(int dana) {
        this.posudjena = true;
        this.danaPosudbe = dana;
    }

    /**
     * Postavlja {@link #posudjena} knjige na dostupnu i {@link #posudioCustomer} na {@code null}.
     */
    public void vrati() {
        this.posudjena = false;
        this.danaPosudbe = 0;
        posudioCustomer = null;
    }

    /**
     * Pomoćna metoda za metodu {@link #toString()} koja služi kako bi u tekstualnom obliku bilo napisano
     * koliko dana je knjiga posuđena.
     * @return {@link #danaPosudbe} ako je posuđena, a ako je dostupna vraća vrijednost {@code String} "Dostupno".
     */
    private String dostupnost() {
        if (this.posudjena) {
            return "Posuđena " + this.danaPosudbe + " dana";
        } else {
            return "Dostupno";
        }
    }

    /**
     * Pomoćna metoda za dohvat informacija o korisniku za potrebe ispisa.
     * @return Tekstualni info o korisniku ili prazan {@code String} ako je knjiga dostupna.
     */
    private String customerInfo(){
        if (this.posudioCustomer == null) {
            return "";
        } else {
            return this.posudioCustomer.toString();
        }
    }

    /**
     * {@inheritDoc}
     * @return "Naziv - Autor (Status) Informacije_o_kupcu."
     */
    @Override
    public String toString() {
        String status = dostupnost();
        String customerInfo = customerInfo();
        return nazivDjela + " - " + autor + " (" + status + ") " + customerInfo;
    }
}

