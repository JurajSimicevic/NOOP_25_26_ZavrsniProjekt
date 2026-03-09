package BackEnd.Management;

import BackEnd.Customer;
import BackEnd.DataAccessObject.CustomerDAO;
import BackEnd.DataAccessObject.IntCustomerDAO;
import BackEnd.DatabaseConnectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacija managera za korisnike. Koristi Singleton uzorak.
 * Podaci se sinkroniziraju između liste u RAM-u i baze podataka putem DAO sloja.
 */
public class CustomerManager implements CustomerManagerInt{

    /** Lista {@link Customer} koja čuva podatke u radnoj memoriji aplikacije. */
    private List<Customer> customers;

    IntCustomerDAO cDao;

    /** Trenutni tekstualni filteri za pretragu liste "customers" unutar GUI-ja. */
    private String customerFilter = "";

    private CustomerManager() {
        if(DatabaseConnectionManager.getInstance().isConnectionAlive()){
            cDao = CustomerDAO.getInstance();
            customers = cDao.getAll();

        }
    }

    /** Bill Pugh Singleton holder. */
    private static class Holder {
        private static final CustomerManager INSTANCE = new CustomerManager();
    }

    /** @return Jedinstvena instanca managera. */
    public static CustomerManagerInt getInstance() {
        return Holder.INSTANCE;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link CustomerDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean addCustomer(Customer c) {
        // 1. Ako je dodavanje u bazu uspješno:
        if (cDao.insert(c)) {
            // 2. Dodaj ga i u radnu memoriju
            customers.add(c);
        }
        return false;
    }

    /** {@inheritDoc}
     * <p>Poziva {@link CustomerDAO} za obradu ove operacije</p>
     */
    @Override
    public boolean deleteCustomer(Customer c) {
        // 1. Ako je brisanje kupca iz baze uspješno:
        if (cDao.delete(c.getId())) {
            // 2. I ako je brisanje kupca iz radne memorije uspješno:
            return customers.removeIf(customer -> customer.getId() == c.getId());
        }
        return false;
    }

    /** {@inheritDoc}
     * Vraća spremljenu listu članova knjižnice
     */
    @Override
    public List<Customer> getCustomers() {
        return customers;
    }

    /** {@inheritDoc} */
    @Override
    public void setFilter(String filter) {
        this.customerFilter = filter;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilter() {
        return customerFilter;
    }

    @Override
    public List<Customer> getFilteredList(String text) {
        if (text == null || text.isEmpty()) {
            return getCustomers();
        }

        String filter = text.toLowerCase();
        List<Customer> filtrirane = new ArrayList<>();

        for (Customer c : customers) {
            if (c.getIme().toLowerCase().contains(filter) ||
                    c.getPrezime().toLowerCase().contains(filter)) {
                filtrirane.add(c);
            }
        }
        return filtrirane;
    }
}
