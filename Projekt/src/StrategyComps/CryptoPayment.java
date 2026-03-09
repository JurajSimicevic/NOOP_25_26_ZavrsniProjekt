package StrategyComps;

import javax.swing.*;

/**
 * Implementacija strategije plaćanja kriptovalutama.
 * Omogućuje modernu metodu plaćanja (npr. Bitcoin) sa simuliranom autorizacijom transakcije.
 */
public class CryptoPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Plaćeno " + amount + " € putem kriptovaluta (autorizacija...).");
        JOptionPane.showMessageDialog(null, "Plaćanje u iznosu od $" + amount + " izvršeno BitCoinom.", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
    }
}
