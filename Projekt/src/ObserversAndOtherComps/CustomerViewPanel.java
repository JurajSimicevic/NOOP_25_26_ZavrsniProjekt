package ObserversAndOtherComps;

import BackEnd.Customer;
import BackEnd.FilePathEnum;

/**
 * Specijalizirani panel za prikaz liste kupaca.
 * <p>
 * Automatski se sinkronizira s kategorijom {@link FilePathEnum#CUSTOMERS}.
 * Koristi se u glavnom prozorima i dijalozima za odabir korisnika pri posudbi.
 * </p>
 */
public class CustomerViewPanel extends ViewPanel<Customer> {

    public CustomerViewPanel() {
        super(FilePathEnum.CUSTOMERS);
    }

    public CustomerViewPanel( int height, int width) {
        super(FilePathEnum.CUSTOMERS, height, width);
    }

}
