package com.banking.domain.money;

import com.banking.rest.ValidationMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Currencies should")
class CurrenciesTest {

    @Test
    @DisplayName("recognize a valid currency code")
    void recognizeValidCurrencyCode() {
        final Optional<ValidationMessage> validationMessage = Currencies.validateCurrencyCode("USD");
        assertFalse(validationMessage.isPresent());
    }

    @Test
    @DisplayName("recognize not a valid currency code")
    void recognizeNotValidCurrencyCode() {
        final Optional<ValidationMessage> validationMessage = Currencies.validateCurrencyCode("X-USD-X");
        assertTrue(validationMessage.isPresent());
        assertEquals("Code X-USD-X is not a valid ISO 4217 currency code.", validationMessage.get().text());
    }
}