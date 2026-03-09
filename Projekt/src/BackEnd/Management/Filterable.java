package BackEnd.Management;

import java.util.List;

/**
 * Generičko sučelje koje definira metode za filtriranje podataka.
 * Omogućuje uniformno upravljanje pretragama u različitim managerima.
 * @param <T> Tip objekta koji se filtrira (npr. Knjiga, Customer).
 */
public interface Filterable<T> {

    /** Postavlja trenutni tekstualni filter. */
    void setFilter(String filter);

    /** Vraća trenutno aktivni tekstualni filter. */
    String getFilter();

    /**
     * Vraća listu objekata filtriranu prema zadanom tekstu.
     * @param text Tekst koji se traži unutar atributa objekta.
     * @return Lista filtriranih objekata tipa T.
     */
    List<T> getFilteredList(String text);
}