package com.banking.domain.money;

import com.banking.rest.ValidationMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

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
    @DisplayName("recognize money with the same currency")
    void recognizeMoneyWithSameCurrency() {
        final Money fiveDollars = new Money("5", "USD");
        final Money fiftyDollars = new Money("50", "USD");
        assertTrue(fiveDollars.hasSameCurrency(fiftyDollars));
    }

    @Test
    @DisplayName("recognize money with a different currency")
    void recognizeMoneyWithDifferentCurrency() {
        final Money fiveEuro = new Money("5", "EUR");
        final Money fiftyDollars = new Money("50", "USD");
        assertFalse(fiveEuro.hasSameCurrency(fiftyDollars));
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

    @Nested
    class EqualityTests {

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
    }

    @Nested
    class ValidationTests {

        @Test
        @DisplayName("not consider negative amount as a valid money amount")
        void notConsiderNegativeAmountAsValid() {
            final Optional<ValidationMessage> message = Money.validateAmount("-5.00");
            assertTrue(message.isPresent());
            assertEquals("Money amount cannot be negative, '-5.00' is a negative number.", message.get().text());
        }

        @Test
        @DisplayName("not consider non-numeric string as a valid money amount")
        void notConsiderNonNumericStringAsValid() {
            final String expectedText = "Money amount should be represented as a number, e.g. '100.00'. 'five' is not a number.";
            final Optional<ValidationMessage> message = Money.validateAmount("five");
            assertTrue(message.isPresent());
            assertEquals(expectedText, message.get().text());
        }

        @Test
        @DisplayName("consider a positive numeric string as a valid money amount")
        void considerPositiveNumericStringAsValid() {
            final Optional<ValidationMessage> message = Money.validateAmount("5.00");
            assertFalse(message.isPresent());
        }
    }
}