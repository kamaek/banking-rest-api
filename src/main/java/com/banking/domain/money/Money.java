package com.banking.domain.money;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A value object representing amount of money and its currency.
 */
public final class Money {

    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        checkNotNegative(amount);
        this.amount = amount;
        this.currency = Objects.requireNonNull(currency);
    }

    private static void checkNotNegative(BigDecimal amount) {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Money cannot have negative amount.");
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount, currency);
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
