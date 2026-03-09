package StrategyComps;

import javax.swing.*;

/**
 * Implementacija strategije plaćanja karticom.
 * Simulira proces autorizacije i potvrđuje uspješnu transakciju putem kartičnog sustava.
 */
public class CardPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Plaćeno " + amount + " € putem kartice (autorizacija...).");
        JOptionPane.showMessageDialog(null, "Plaćanje u iznosu od $" + amount + " izvršeno karticom.", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
    }
}