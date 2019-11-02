package com.banking.domain.money;

import com.banking.rest.ValidationMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A value object representing amount of money and its currency.
 */
public final class Money {

    private final BigDecimal amount;
    private final Currency currency;

    public Money(String amount, String currencyCode) {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public Money(BigDecimal amount, Currency currency) {
        checkNotNegative(amount);
        this.amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.UNNECESSARY);
        this.currency = Objects.requireNonNull(currency);
    }

    /**
     * Validates that the specified string is a valid money representation.
     *
     * <p>Money cannot have negative amount and should be represented as a number, e.g. {@code "100.00"}.
     */
    public static Optional<ValidationMessage> validateAmount(String rawAmount) {
        try {
            final BigDecimal amount = new BigDecimal(rawAmount);
            if (isNegative(amount)) {
                final String text = format("Money amount cannot be negative, '%s' is a negative number.", rawAmount);
                return Optional.of(new ValidationMessage(text));
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            final String text = format("Money amount should be represented as a number, e.g. '100.00'. '%s' is not a number.",
                    rawAmount);
            return Optional.of(new ValidationMessage(text));
        }
    }

    public BigDecimal amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    private static boolean isNegative(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private static void checkNotNegative(BigDecimal amount) {
        if (isNegative(amount)) {
            throw new IllegalStateException("Money cannot have negative amount.");
        }
    }

    @Override
    public String toString() {
        return format("%s %s", amount, currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) &&
                currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
