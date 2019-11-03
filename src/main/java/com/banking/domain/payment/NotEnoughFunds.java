package com.banking.domain.payment;

/**
 * Signals that an account doesn't have enough money to perform a payment.
 */
public class NotEnoughFunds extends PaymentException {

    NotEnoughFunds(String accountId) {
        super(String.format("Account %s doesn't have enough funds to perform a payment.", accountId));
    }
}
