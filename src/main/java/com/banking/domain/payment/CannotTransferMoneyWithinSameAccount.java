package com.banking.domain.payment;

/**
 * Signals that an attempt to transfer money to the same account was performed.
 */
public class CannotTransferMoneyWithinSameAccount extends PaymentException {

    CannotTransferMoneyWithinSameAccount() {
        super("You cannot transfer money to the same account.");
    }
}
