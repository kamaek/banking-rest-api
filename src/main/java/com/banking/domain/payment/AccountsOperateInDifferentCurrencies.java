package com.banking.domain.payment;

/**
 * Signals that two accounts operate in different currencies, while this is not allowed for money transfer.
 */
public class AccountsOperateInDifferentCurrencies extends PaymentException {

    public AccountsOperateInDifferentCurrencies(String firstAccountId, String secondAccountId) {
        super(String.format("Account %s and %s operate in different currencies.", firstAccountId, secondAccountId));
    }
}
