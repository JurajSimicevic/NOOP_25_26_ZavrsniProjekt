package BackEnd.Management;

import BackEnd.Customer;

import java.util.List;

/**
 * Krajnje sučelje za upravljanje članovima knjižnice.
 * Proširuje {@link Filterable} za podršku pretraživanja kupaca.
 */
public interface CustomerManagerInt extends Filterable<Customer> {

    /**
     * Dodaje novi {@code Customer} objekt u sustav.
     * @param c Objekt korisnika.
     * @return {@code true} ako korisnik već ne postoji i uspješno je dodan.
     */
    boolean addCustomer(Customer c);

    /**
     * Briše odabrani {@code Customer} objekt iz sustava.
     * @param c Objekt korisnika kojeg treba ukloniti.
     * @return {@code true} ako je korisnik pronađen i obrisan.
     */
    boolean deleteCustomer(Customer c);

    /**  @return Aktualnu {@code List<Customer>} listu svih članova knjižnice u sustavu.*/
    List<Customer> getCustomers();
}
