package com.banking.domain.payment;

/**
 * Signals that a payment cannot be performed because instructed money has invalid currency.
 */
class InvalidPaymentCurrency extends PaymentException {

    InvalidPaymentCurrency(String message) {
        super(message);
    }
}
