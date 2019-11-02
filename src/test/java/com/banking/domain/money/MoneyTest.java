package com.banking.domain.money;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

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
        final Money fiveDollars = new Money(five, Currencies.americanDollar());
        assertNotEquals(fiveEuro, fiveDollars);
    }

    @Test
    @DisplayName("scale amount according to currency")
    void scaleAmountAccordingToCurrency() {
        final BigDecimal five = new BigDecimal(5);
        final Money fiveEuro = new Money(five, Currencies.euro());
        final Money fiveYen = new Money(five, Currency.getInstance(Locale.JAPAN));
        assertEquals("5.00 EUR", fiveEuro.toString());
        assertEquals("5 JPY", fiveYen.toString());
    }

    @Test
    @DisplayName("not lose minor currency unit")
    void notLoseMinorCurrencyUnit() {
        final BigDecimal five = new BigDecimal("5.01");
        final ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> new Money(five, Currency.getInstance(Locale.JAPAN)));
        assertEquals("Rounding necessary", exception.getMessage());
    }
}