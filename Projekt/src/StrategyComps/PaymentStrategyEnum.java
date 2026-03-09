package StrategyComps;

/**
 * Enumeracija podržanih metoda plaćanja.
 * Služi kao type-safe način za odabir konkretne strategije u kontrolerima ili formama.
 */
public enum PaymentStrategyEnum {

    Gotovina, Kartica, Kriptovalute,
}
