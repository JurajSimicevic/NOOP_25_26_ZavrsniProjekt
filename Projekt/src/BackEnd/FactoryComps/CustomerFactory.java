package BackEnd.FactoryComps;

import BackEnd.Customer;
import BackEnd.User;

/**
 * Konkretna tvornica za kreiranje kupaca (članova knjižnice).
 * <p>
 * Implementirana kao <b>Singleton</b> kako bi se osigurala jedna točka pristupa
 * i spriječilo nepotrebno trošenje memorije instanciranjem više tvornica.
 * </p>
 * <b>Uloga u sustavu:</b>
 * <p>Ova klasa konkretizira {@link UserFactory} sučelje za potrebe registracije kupaca.
 * Budući da proces kreiranja kupca ne zahtijeva vanjska stanja (poput lozinke),
 * tvornica je izrazito brza i stateless u samom procesu kreiranja objekta.</p>
 */
public class CustomerFactory implements UserFactory {

    private static CustomerFactory instance;

    /**
     * Privatni konstruktor sprječava instanciranje izvan klase,
     * osiguravajući integritet Singleton uzorka.
     */
    private CustomerFactory() {}

    /**
     * Vraća jedinstvenu i nitno sigurnu (synchronized) instancu CustomerFactory klase.
     * @return instanca {@code CustomerFactory}.
     */
    public static synchronized CustomerFactory getInstance() {
        if (instance == null) {
            instance = new CustomerFactory();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * <p>Izravno instancira objekt klase {@link Customer} s proslijeđenim parametrima.
     * Vraća ga kao apstraktni tip {@link User} radi lakše integracije s ostatkom sustava.</p>
     */
    @Override
    public User createUser(String ime, String prezime, int dob, String grad, String adresa) {
        return new Customer(ime, prezime, dob, grad, adresa);
    }
}