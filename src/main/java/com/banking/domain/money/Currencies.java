package com.banking.domain.money;

import com.banking.rest.ValidationMessage;

import java.util.Currency;
import java.util.Optional;

public final class Currencies {

    /**
     * Prevents instantiation of this utility class.
     */
    private Currencies() {
    }

    /**
     * Determines whether the specified code is a ISO 4217 currency code.
     */
    public static boolean isValidCurrencyCode(String code) {
        return Currency.getAvailableCurrencies()
                .parallelStream()
                .anyMatch(currency -> currency.getCurrencyCode().equals(code));
    }

    public static Optional<ValidationMessage> validateCurrencyCode(String code) {
        if (isValidCurrencyCode(code)) {
            return Optional.empty();
        }
        final String text = String.format("Code %s is not a valid ISO 4217 currency code.", code);
        return Optional.of(new ValidationMessage(text));
    }

    public static Currency americanDollar() {
        return Currency.getInstance("USD");
    }

    public static Currency euro() {
        return Currency.getInstance("EUR");
    }
}
