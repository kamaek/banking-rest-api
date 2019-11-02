package com.banking.domain.money;

public final class Currencies {

    /**
     * Prevents instantiation of this utility class.
     */
    private Currencies() {
    }

    public static Currency americanDollar() {
        return new Currency("USD");
    }

    public static Currency euro() {
        return new Currency("EUR");
    }
}
