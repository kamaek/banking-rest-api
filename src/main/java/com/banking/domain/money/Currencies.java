package com.banking.domain.money;

import java.util.Currency;

public final class Currencies {

    /**
     * Prevents instantiation of this utility class.
     */
    private Currencies() {
    }

    public static Currency americanDollar() {
        return Currency.getInstance("USD");
    }

    public static Currency euro() {
        return Currency.getInstance("EUR");
    }
}
