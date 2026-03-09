# 🖥️ Library Management System 9000

> *Desktop aplikacija za knjižničare u knjižnici izgrađena u Javi. Projekt je nadogradnja prethodnog projekta iz kolegija Osnove Objektnog Programiranja. Demonstrira primjenu objektno-orijentiranih principa (SOLID), MVC arhitekture i svih obrazaca dizajna (+ još nekih dodatnih) koje smo radili na predavanjima.*)

---

## 🛠️ Tehnologije

---
* **Jezik:** Java (SDK 20)
* **GUI:** **Java Swing**
* **Alati za izradu (Build tool):** Maven (za kontrolu zavisnosti).

## 🏗️ Arhitektura (MVC)

Aplikacija je strukturirana prema **Model-View-Controller (MVC)** arhitektonskom obrascu kako bi se osiguralo jasno razdvajanje korisničkog sučelja od poslovne logike.

---
* **Model:** Sadrži podatke i poslovnu logiku. Obavještava View o promjenama (koristeći Observer obrazac).
* **View:** Implementiran pomoću **Java Swing** biblioteke. Prikazuje podatke iz Modela korisniku i prosljeđuje korisničke akcije Controlleru.
* **Controller:** Prima unos od korisnika (preko View-a), obrađuje ga i ažurira Model.

## 🗄️ Detaljan opis ER modela baze podataka (ERD)

Baza podataka sastoji se od tri ključna entiteta koja prate poslovanje knjižnice. Struktura je dizajnirana kako bi podržala perzistenciju objekata uz osiguranje integriteta podataka.

---

### 1. Entiteti i atributi (Struktura tablica)


Definirane su sljedeće tablice unutar sustava:

#### 📘 Tablica `Books` (Entitet: **Knjiga**)
| Atribut | Tip podatka               | Opis |
| :--- |:--------------------------| :--- |
| `isbn` | **VARCHAR (PRIMARY_KEY)** | Jedinstveni međunarodni identifikator knjige. |
| `naziv_djela` | VARCHAR                   | Naslov knjige. |
| `autor` | VARCHAR                   | Ime i prezime autora. |
| `vrsta_djela` | VARCHAR                   | Žanr ili kategorija knjige. |
| `cijena` | DOUBLE                    | Prodajna ili nabavna vrijednost. |
| `posudjena` | BOOLEAN                   | Zastavica (0/1) za trenutni status dostupnosti. |
| `dana_posudbe` | INT                       | Broj dana na koji je knjiga iznajmljena. |
| `customer_id` | **INT (FOREIGN_KEY)**     | Poveznica s kupcem koji drži knjigu (može biti `NULL`). |

#### 👥 Tablica `Customers` (Entitet: **Korisnik**)
| Atribut | Tip podatka                           | Opis |
| :--- |:--------------------------------------| :--- |
| `id` | **INT (PRIMARY_KEY, AUTO_INCREMENT)** | Interni automatski generirani broj korisnika. |
| `ime` | VARCHAR                               | Ime korisnika. |
| `prezime` | VARCHAR                               | Prezime korisnika. |
| `dob` | INT                                   | Starost korisnika. |
| `grad` | VARCHAR                               | Grad stanovanja. |
| `adresa_stanovanja`| VARCHAR                               | Adresa za dostavu i kontakt. |

#### 🔑 Tablica `Librarians` (Entitet: **Knjižničar**)
| Atribut | Tip podatka | Opis |
| :--- | :--- | :--- |
| `id` | **INT (PRIMARY_KEY, AUTO_INCREMENT)** | Jedinstveni ID zaposlenika. |
| `username` | **VARCHAR (UNIQ)** | Jedinstveno korisničko ime za prijavu. |
| `password` | VARCHAR | Lozinka za pristup sustavu (Plain-text/Encrypted). |
| `is_admin` | BOOLEAN | Razina ovlasti (0 - zaposlenik, 1 - administrator). |
| *Osobni podaci* | - | Sadrži i atribute: `ime`, `prezime`, `dob`, `grad`, `adresa`. |

---

### 2. Relacije među entitetima

Glavna relacija u sustavu uspostavljena je radi praćenja procesa posudbe:

> **Relacija: Jedan-na-više (1:N)**
> * **Logika:** Jedan korisnik (`Customer`) može posuditi više knjiga istovremeno.
> * **Ograničenje:** Jedna konkretna knjiga (`Book`) u danom trenutku može biti kod najviše jednog korisnika.
> * **Implementacija:** Relacija se ostvaruje preko stranog ključa **`customer_id`** u tablici `Books`.

**Napomena o integritetu:**
Kada se knjiga vrati u knjižnicu, logika unutar `BookDAO.updateBorrowStatus` metode postavlja `customer_id` na `NULL`, čime se knjiga oslobađa za buduće posudbe.

---

### 3. SQL Skripta za kreiranje baze (DDL)
<details>
  <summary>🔎 Klikni ovdje za prikaz SQL DDL skripte</summary>

```sql
CREATE DATABASE IF NOT EXISTS KnjiznicaDB;
USE KnjiznicaDB;

CREATE TABLE Customers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           ime VARCHAR(50),
                           prezime VARCHAR(50),
                           dob INT,
                           grad VARCHAR(50),
                           adresa_stanovanja VARCHAR(100)
);

CREATE TABLE Librarians (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            ime VARCHAR(50),
                            prezime VARCHAR(50),
                            dob INT,
                            grad VARCHAR(50),
                            adresa_stanovanja VARCHAR(100),
                            username VARCHAR(50) UNIQUE NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE Books (
                       isbn VARCHAR(20) PRIMARY KEY,
                       naziv_djela VARCHAR(100),
                       autor VARCHAR(100),
                       vrsta_djela VARCHAR(50),
                       cijena DOUBLE,
                       posudjena BOOLEAN DEFAULT FALSE,
                       dana_posudbe INT DEFAULT 0,
                       customer_id INT,
                       FOREIGN KEY (customer_id) REFERENCES Customers(id) ON DELETE SET NULL
);
``` 
</details>

## 🧩 Obrasci dizajna (Design Patterns)

Kako bi kod bio održiv, fleksibilan i proširiv, implementirani su sljedeći obrasci dizajna:

---

| Obrazac (Pattern)           | Paket/Klasa                                            | Opis i uloga u projektu                                                                                                                                                                                                                                                                                                                                                                                                                                |
|:----------------------------|:-------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Strategy**                | `StrategyComps` + klasa `BuyBookWindow`                | Omogućuje dinamičku promjenu načina plaćanja pri kupovini knjige (CashPayment, CardPayment...).                                                                                                                                                                                                                                                                                                                                                        |
| **Observer**                | `ObserversAndOtherComps` i Observable `LibraryManager` | Korišten za komunikaciju između Modela (`LibraryManager`) i View-a (`ViewPanel`). Kada se podaci u Modelu promijene, View se automatski osvježava bez čvrste vezanosti (loose coupling).                                                                                                                                                                                                                                                               |
| **Fasada**                  | `LibraryManager`                                       | Iako je prethodno opisan kao `Observable`, ujedno je i "Fasada" za sve operacije. Zbrinjava se za kompleksne operacije što omogućava jednostavne pozive za akcijom (npr. brisanje knjige: Poziva `BookDAO.delete()`, briše knjigu iz memorije, te obaviještava `Observere`).                                                                                                                                                                           |
| **Command**                 | `Commands`                                             | Enkapsulira korisničke zahtjeve (npr. klikove na gumbe) u objekte. Ovo olakšava implementaciju funkcionalnosti poput *Undo/Redo*.                                                                                                                                                                                                                                                                                                                      |
| **Decorator**               | `Decorators`                                           | Omogućuje dinamičko stvaranje poruka Obavijesti, Upozorenja ili Greški.                                                                                                                                                                                                                                                                                                                                                                                |
| **Factory i SimpleFactory** | `FactoryComps`                                         | "Factory" za stvaranje objekata `User` podklasa: `Customer` i `Librarian`, a "Simple Factory" za objekte klase `Knjiga`                                                                                                                                                                                                                                                                                                                                |
| **Singleton**               | `Manager` i `DAO` klase                                | U projektu je korišten Bill Pugh Singleton obrazac za sve DAO i "Manager" klase. Singleton zato što nam ne treba nego jedna instanca ovih klasa (jer nam treba i samo jedan izvor podataka + štedi memoriju).                                                                                                                                                                                                                                          |
| **Data Access Object**      | `DataAccessObject` paket                               | DAO obrazac korišten je za postizanje separation of concerns (razdvajanje odgovornosti). Poslovna logika komunicira s podacima isključivo preko DAO sučelja, čime je postignuta visoka modularnost i olakšano održavanje sustava.                                                                                                                                                                                                                      |
## 📂 Struktura projekta

Ukratko, mapa projekta izgleda ovako:

---

```text
src/
 ├── AddBookComps/              # GUI za "AddBookWindow" prozor
 ├── BackEnd/                   # Logične klase za manipulaciju podatcima
 ├── BuyBookComps/              # GUI za "BuyBookWindow" prozor
 ├── Commands/                  # Command klase
 ├── Decorators/                # Dekoratori 
 ├── GUI_Comps                  # Bazične klase koje nasljeđuju sve ostale GUI klase
 ├── LibrarianManagementComps   # GUI za "LibrarianManagementComps" prozor
 ├── LoginComps                 # GUI za "LoginComps" prozor
 ├── MainFrameComps             # GUI za "MainFrame" prozor
 ├── ObserversAndOtherComps     # Sučelja i GUI vezani uz Observer + SearchField
 ├── RegisterCustomerComps      # GUI za "RegisterCustomerComps" prozor
 ├── RegisterLibrarianComps     # GUI za "RegisterLibrarianComps" prozor
 ├── ReturnBookComps            # GUI za "ReturnBookComps" prozor
 ├── StrategyComps              # Klase i sučelje za implementaciju Strategy dizajna
 └── UniverzalnoSucelje/        
 ```

## ⚙️ Opis rada projekta


### 🔄 Primjer toka izvršavanja: Brisanje knjige (Command & DAO Pattern)

Kako bismo osigurali čistu arhitekturu i razdvojili korisničko sučelje od poslovne logike, korisničke akcije su enkapsulirane u Command objekte.

Evo detaljnog pregleda (korak-po-korak) što se događa ispod haube kada korisnik klikne gumb **"Izbriši"** u AddBookWindow prozoru:

<details>
  <summary>🔎 Klikni ovdje za prikaz primjera koda </summary>

1. **Korisnička akcija (View):** Korisnik pritisne `JButton` za brisanje. Listener registrira događaj i prosljeđuje ga CommandManager-u.
```java
@Override
protected void activateComps(){
    addBookRightPanel.setAddBookWindowListener(new ActionCommandListener() {
        @Override
        public void eventOccurred(String actionCommand) {
            // Akcija za dodavanje nove knjige
            if (actionCommand.equals(ActionCommandsEnum.ADD.toString())) {
                izvrsiUnos();

            }
            // Akcija za brisanje trenutno odabrane knjige iz liste
            if (actionCommand.equals(ActionCommandsEnum.DELETE.toString())) {
                Knjiga k = addBookRightPanel.getSelectedBook(); //<------------------------Dohvaćamo objekt knjige iz ViewPanel-a
                CommandManager.getInstance().executeCommand(new DeleteBookCommand(k)); //<-Kreirana komanda i  poslana CommandManageru🟢!
            }
            // Zatvaranje ovog prozora i povratak na MainFrame
            if (actionCommand.equals(ActionCommandsEnum.BACK.toString())) {
                new MainFrame();
                dispose();
            }
        }
    });
}
```
*Primjer koda iz AddBookWindow klase u AddBookComps paketu | linija 95:*


2. **Kreiranje komande:** Listener prepoznaje akciju i instancira novu komandu `DeleteBookCommand`, prosljeđujući joj odabranu instancu klase `Knjiga` kao parametar.

```java
@Override
public boolean executeCommand(Command cmd) {
    if(DatabaseConnectionManager.getInstance().isConnectionAlive())
        if (cmd.canExecute()) {
            if (cmd.execute()) {
                undoStack.push(cmd);
                // Ključno: Svaka nova akcija briše redo povijest jer se stvara nova grana događaja
                redoStack.clear();
                System.out.println("Komanda izvršena i dodana u Undo stog.");
                return true;
            }
        }
    return false;
}
```
*Metoda executeCommand(Command cmd) koja je pozvana u prošlom kodu*

3. **Upravljanje komandama (CommandManager):** Kreirana komanda se predaje `CommandManager`-u
(implementiranom kao *Singleton*). On preuzima komandu i poziva njezinu metodu `canExecute()` koja provjerava jesu li 
ispunjeni uvjeti za izvršavanje operacije, zatim je izvršava pomoću `execute()`.

```java
@Override
protected void createMessage() {

    ReturnMessage msg = new BasicReturnMessage("Greška pri brisanju:");

    if (bookToDelete == null) {
        msg = new BookNotSelectedDecorator(msg);
    } else if (bookToDelete.isPosudjena()) {
        msg = new BookAlreadyBorrowedDecorator(msg);
    }

    JOptionPane.showMessageDialog(null, msg.getMessage(), "Neuspjela validacija", JOptionPane.ERROR_MESSAGE);
}

@Override
public boolean canExecute() {
    if (bookToDelete == null || bookToDelete.isPosudjena()) {
        createMessage();
        return false;
    }
    return true;
}

@Override
public boolean execute() {
    if(libraryManager.removeBook(bookToDelete)){
        JOptionPane.showMessageDialog(null, "Knjiga uspješno izbrisana!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    return false;
}

```
*Metoda createMessage() je metoda dobivena nasljeđivanjem BaseCommand klase. Budući da su Command klase zadužene
za dosta provjera, u ovoj metodi se grade poruke o greškama pomoću dekoratora 
("lijepljenje" poruka svih grešaka u jednu poruku.)*

4. **Poslovna logika (Model):** Ako su uvjeti zadovoljeni, komanda poziva `LibraryManager.removeBook(Knjiga k)` 
koja prosljeđuje knjigu koju treba obrisati `BookDAO` klasi. Ako je `BookDAO.delete()` vratio `true`
briše knjige iz radne memorije (Radna memorija jest `private List<Knjiga> knjige` u samoj `LibraryManager` klasi)

```java
@Override
public boolean removeBook(Knjiga k) {
    if (bDao.delete(k.getIsbn())) {    //<------ Ako je brisanje knjige iz baze uspješno:

        knjige.remove(k);              //<------ Izbriši knjigu iz radne memorije.
        
        notifyObservers();             //<------ Obavještavamo sve panele (Observere) da se osvježe
        return true;
    }
    return false;
}
```
*Razlog za tolikim korištenjem `boolean` povratnog tipa metode je radi CommandManagera:
Ako je sve `true`sprema Command objekt u undo stog (Pogledajte kod gore)*

5. **Baza podataka (DAO):** `LibraryManager` delegira konačni zadatak klasi `BookDAO`, 
koja izvršava stvarni SQL upit i trajno uklanja knjigu iz MySQL baze podataka (vraća `true` ako je konekcija s bazom "živa"
i ako je operacija uspješna.)

```java
public void insert(Knjiga k) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
</details>

---

### 🛡️ Integritet podataka i slojevitost
Projekt strogo poštuje pravilo razdvajanja odgovornosti **(Separation of Concerns (SoC))**:
* **Command klase** ne znaju za SQL. One služe isključivo za kontrolu toka i validaciju poslovnih pravila.
* **LibraryManager** je "mozak" aplikacije koji drži sinkronizirano stanje u memoriji s onim u bazi.
* **DAO (Data Access Object)** je jedina točka kontakta s bazom podataka, čime je postignuta visoka modularnost sustava.

---

## 📦 Popis vanjskih biblioteka i modula

U razvoju aplikacije korišten je **Apache Maven** za upravljanje ovisnostima. Korištene biblioteke omogućuju rad s bazom podataka te moderno i responzivno grafičko sučelje.

---

### 1. MySQL Connector/J
* **Namjena:** Službeni JDBC upravljački program (driver) koji omogućuje komunikaciju između Jave i MySQL baze podataka. Neophodan je za izvršavanje SQL upita unutar DAO klasa.
* **Osnovni podaci:**
    * **Grupa i artefakt:** `com.mysql:mysql-connector-j`
    * **Verzija:** `9.4.0`
    * **Lokacija preuzimanja:** [Maven Repository](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j)
    * **Službena dokumentacija:** [MySQL Connector/J Manual](https://dev.mysql.com/doc/connector-j/en/)

---

### 2. Radiance Framework
Radiance je skup biblioteka koji zamjenjuje standardni Swing "Look and Feel" modernim vizualnim stilovima i animacijama.

| Komponenta             | Namjena                                                                                                                                                                                                                           | Dokumentacija i lokacija                                                       |
|:-----------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| **Radiance-theming**   | Glavni okvir za vizualni identitet aplikacije. Omogućava korištenje modernih "skinova" koji zamjenjuju zastarjeli izgled standardnog Swinga.                                                                                      | [GitHub Radiance](https://github.com/kirill-grouchnikov/radiance)              |
| **radiance-animation** | Biblioteka (ranije poznata kao Trident) koja upravlja svim vizualnim tranzicijama u aplikaciji. Koristi se za fadeIn i fadeOut efekte pri paljenju u gašenju prozora koji su implementirani u BaseFrame i BaseDialog.             | [GitHub Radiance](https://github.com/kirill-grouchnikov/radiance)              |
| **ephemeral-chroma**   | Dio Radiance ekosustava koji se bavi naprednim radom s bojama. Omogućava dinamičku promjenu paleta i usklađivanje boja komponenti unutar odabrane teme kako bi GUI bio koherentan. (Dodatak bez kojeg Radiance ne funkcionira...) | [GitHub Radiance](https://github.com/kirill-grouchnikov/radiance)              |
| **MySQL-connector-j**  | Službeni JDBC (Java Database Connectivity) upravljački program koji omogućava aplikaciji komunikaciju s MySQL poslužiteljem. Neophodan je za izvršavanje SQL upita i rad s podacima u bazi.                                                                                                                                                                             | [MySQL Connector/J Developer Guide](https://dev.mysql.com/doc/connector-j/en/) |

* **Verzija:** `v1.0.2`
* **Izvor:** Biblioteke se povlače preko **JitPack** repozitorija ([https://jitpack.io](https://jitpack.io)).

---

### 🛠️ Tehničke postavke projekta

Projekt je konfiguriran za rad na modernoj Java platformi, što je vidljivo iz postavki u `pom.xml` datoteci:

* **Java SDK:** Verzija 23 (koristi se za kompajliranje i izvođenje).
* **Encoding:** `UTF-8` (osigurava ispravan prikaz hrvatskih dijakritičkih znakova).
* **Maven:** U projektu se nalaze 4 vanjske biblioteke 

```xml
  <dependencies>

  <!-- Source: https://repo1.maven.org/maven2/org/pushing-pixels/radiance-animation/8.5.0/radiance-animation-8.5.0.jar --> <!-- [Direkt Download] -->
  
  <!-- Source: https://mvnrepository.com/artifact/org.pushing-pixels/radiance-animation -->
  <dependency>
    <groupId>org.pushing-pixels</groupId>
    <artifactId>radiance-animation</artifactId>
    <version>8.5.0</version>
  </dependency>

  <!-- Source: https://repo1.maven.org/maven2/org/pushing-pixels/radiance-theming/8.0.0/radiance-theming-8.0.0.jar -->      <!-- [Direkt Download] -->
  
  <!-- Source: https://mvnrepository.com/artifact/org.pushing-pixels/radiance-theming -->
  <dependency>
    <groupId>org.pushing-pixels</groupId>
    <artifactId>radiance-theming</artifactId>
    <version>8.0.0</version>
  </dependency>

  <!-- Source: https://repo1.maven.org/maven2/org/pushing-pixels/ephemeral-chroma/1.0.0/ephemeral-chroma-1.0.0.jar -->      <!-- [Direkt Download] -->

  <!-- Source: https://mvnrepository.com/artifact/org.pushing-pixels/ephemeral-chroma -->
  <dependency>
    <groupId>org.pushing-pixels</groupId>
    <artifactId>ephemeral-chroma</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
  </dependency>

  <!-- Source: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.6.0/mysql-connector-j-9.6.0.jar -->             <!-- [Direkt Download] -->

  <!-- Source: https://mvnrepository.com/artifact/com.mysql/mysql-connector-j -->
  <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.6.0</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```
---

## Dijagrami klasa
## 💻—🔗—⚙️—💾 


U ovom poglavlju detaljno je prikazana arhitektura sustava kroz specijalizirane dijagrame klasa. Sustav je dekomponiran na logičke cjeline kako bi se jasno vidjela primjena projektnih obrazaca i hijerarhija nasljeđivanja.

---

### 1. Temeljna GUI hijerarhija i životni ciklus prozora

<span style="font-size:20px;">Temeljne i nasljeđene GUI komponente</span>

<figure>
  <img src="./graphics/GUI_Inheritance_UML.png" alt="UML dijagram klasa" >
  <figcaption>Slika 1: Hijerarhiski UML dijagram GUI komponenti </figcaption>
</figure>

Ovaj dijagram prikazuje "kralježnicu" aplikacije. Definirane su apstraktne baze koje standardiziraju inicijalizaciju sučelja, dok `Shortcuttable`
klase proširuju funkcionalnost podrškom za tipkovničke kratice (Undo: `CTRL + Z` | Redo: `CTRL + Y`).

* **Apstraktne klase na dijagramu:** `BaseFrame`, `BaseDialog`, `ShortcuttableFrame`, `ShortcuttableDialog`, `ActiveBasePanel`, `BasePanel`.
* **Opis:** Prikazuje nasljeđivanje gdje bazne klase definiraju metode `initComps()`, `layoutComps()` i `activateComps()`. `Shortcuttable` 
klase služe kao međusloj koji omogućuje uniformno rukovanje `KeyStroke` događajima u cijeloj aplikaciji.

---

### 2. Implementacija Command (Naredba) obrasca
Dijagram prikazuje potpuni "decoupling" (odvajanje) korisničkog sučelja od poslovne logike. Svaka akcija (unos knjige, registracija, povrat) tretirana je kao neovisni objekt koji se može izvršiti.

<span style="font-size:20px;">Temeljne i nasljeđene GUI komponente</span>

<figure>
  <img src="./graphics/Commands_UML.png" alt="UML dijagram klasa" >
  <figcaption>Slika 1: Hijerarhiski UML dijagram Command klasa </figcaption>
</figure>

* **Klase na dijagramu:** `CommandInterface` (interface), `CommandManager`, `AddBookCommand`, `RegisterLibrarianCommand`, `ReturnBookCommand`.
* **Opis:** `CommandManager` djeluje kao *Invoker* koji upravlja životnim ciklusom naredbi. Sve konkretne naredbe implementiraju `execute()`, `undo()` i `redo()` metode, osiguravajući visoku modularnost i mogućnost proširenja sustava bez zadiranja u GUI kôd.

---

### 3. Upravljanje sesijom i Model podataka
Ovaj dio dokumentira strukturu podataka i način na koji aplikacija upravlja stanjem prijavljenog korisnika tijekom rada.

* **Klase na dijagramu:** `Librarian`, `Knjiga`, `SessionManager`, `SessionManagerInterface` (interface).
* **Opis:** Istaknuta je upotreba **Singleton** obrasca na klasi `SessionManager` koja čuva referencu na `loggedInUser` (objekt klase `Librarian`). Prikazane su asocijacije koje definiraju koji entiteti su dostupni sustavu nakon uspješne autorizacije.

---

### 4. Validacija i Message Decorator obrazac
Prikaz mehanizma za dinamičku izgradnju poruka o pogreškama prilikom validacije formi, čime se izbjegava pisanje desetaka sličnih metoda za ispis upozorenja.

* **Klase na dijagramu:** `ReturnMessage`, `MessageDecorator`, `UsernameMissingDecorator`, `AddressMissingDecorator`, `PasswordMissingDecorator`.
* **Opis:** Korištenjem **Decorator** obrasca, osnovni objekt `ReturnMessage` se dinamički "omata" dodatnim informacijama o pogreškama. Ovo omogućuje slaganje kompleksnih validacijskih poruka (npr. "Nedostaje korisničko ime" + "Lozinka je prekratka") bez modificiranja originalne klase.

---

### 5. Observer obrazac i reaktivni prikaz (View)
Dijagram koji objašnjava kako sustav osigurava da su podaci u tablicama uvijek sinkronizirani s promjenama u modelu podataka (bazi).

* **Klase na dijagramu:** `Observer` (interface), `ViewPanel`, `ViewPanelListener`, `BasePanel`.
* **Opis:** `ViewPanel` implementira `Observer` sučelje. Kada `CommandManager` izvrši promjenu (npr. brisanje knjige), `ViewPanel` prima obavijest i automatski osvježava tablični prikaz koristeći `ViewPanelListener`, čime se izbjegava ručno osvježavanje prozora (tzv. "push" mehanizam).

---

### 6. Primjer kompozicije: Registracija knjižničara
Ovaj dijagram služi kao studija slučaja kako se kompleksni prozori u aplikaciji sastavljaju od više manjih, specijaliziranih komponenti.

* **Klase na dijagramu:** `RegisterLibrarianWindow`, `RegisterLibLeftPanel`, `ConjoinedRegisterLibrarianPanel`, `ViewPanel`.
* **Opis:** Prikazuje kako `RegisterLibrarianWindow` (kao glavni kontejner) agregira `RegisterLibLeftPanel` (forma za unos) i `ConjoinedRegisterLibrarianPanel` (prikaz i pretraga), demonstrirajući visoku razinu ponovne upotrebljivosti komponenti i lakše održavanje koda.