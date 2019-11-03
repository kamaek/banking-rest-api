package com.banking.domain.payment;

/**
 * Signals that a payment failed.
 */
abstract class PaymentException extends Exception {

    PaymentException(String message) {
        super(message);
    }
}
