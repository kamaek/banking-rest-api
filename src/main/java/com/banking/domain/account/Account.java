package com.banking.domain.account;

import com.banking.domain.money.Money;
import com.banking.persistence.Entity;

/**
 * An account of a user.
 */
public class Account extends Entity {

    /**
     * The ID of the user, who owns this account.
     */
    private final String ownerId;

    /**
     * The account type, which determines balance limitations.
     */
    private final AccountType accountType;

    /**
     * The balance of the account.
     */
    private Money balance;

    public Account(String ownerId, AccountType accountType, Money balance) {
        this.ownerId = ownerId;
        this.accountType = accountType;
        this.balance = balance;
    }
}
