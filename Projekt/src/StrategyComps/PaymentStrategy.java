package StrategyComps;

/**
 * Osnovno sučelje koje definira zajednički ugovor za sve strategije plaćanja.
 * <p>
 * Implementacijom <b>Strategy</b> dizajnerskog uzorka, omogućujemo sustavu
 * da dinamički odabere metodu obrade plaćanja bez mijenjanja koda klijenta.
 * </p>
 */
public interface PaymentStrategy {
    void processPayment(double amount);
}
