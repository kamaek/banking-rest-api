package com.banking.domain.account;

/**
 * Signals that an account not exists in the system.
 */
public class AccountNotExists extends Exception {

    public AccountNotExists(String accountId) {
        super(String.format("Account %s not exists in the system.", accountId));
    }
}
