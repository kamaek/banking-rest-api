package com.banking.domain.money;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money should")
class MoneyTest {

    @Test
    @DisplayName("not be negative")
    void notBeNegative() {
        final BigDecimal amount = new BigDecimal(-1);
        final IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new Money(amount, Currencies.euro()));
        assertEquals(exception.getMessage(), "Money cannot have negative amount.");
    }

    @Test
    @DisplayName("be equal if amount and currency are same")
    void beEqualIfAmountAndCurrencySame() {
        final BigDecimal five = new BigDecimal("5.00");
        final Money fiveEuro = new Money(five, Currencies.euro());
        final Money anotherFiveEuro = new Money(five, Currencies.euro());
        assertEquals(fiveEuro, anotherFiveEuro);
    }

    @Test
    @DisplayName("not be equal if amount is same, but currency is different")
    void notBeEqualIfAmountSameButCurrencyDifferent() {
        final BigDecimal five = new BigDecimal("5.00");
        final Money fiveEuro = new Money(five, Currencies.euro());
        final Money fiveDollar = new Money(five, Currencies.americanDollar());
        assertNotEquals(fiveEuro, fiveDollar);
    }
}