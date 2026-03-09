package StrategyComps;

import javax.swing.*;

/**
 * Implementacija strategije za plaćanje gotovinom.
 * Fokusira se na fizičku transakciju i izdavanje računa.
 */
public class CashPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Plaćeno " + amount + " € gotovinom na blagajni.");
        JOptionPane.showMessageDialog(null, "Molimo Vas da date račun kupcu!", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
    }
}
