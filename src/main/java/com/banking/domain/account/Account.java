package com.banking.domain.account;

import com.banking.domain.money.Money;
import com.banking.domain.user.IndividualUser;
import com.banking.persistence.Entity;

import java.util.Objects;

/**
 * An account of a user with the balance of {@link Money}.
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

    private Account(String ownerId, AccountType accountType, Money balance) {
        this.ownerId = Objects.requireNonNull(ownerId);
        this.accountType = Objects.requireNonNull(accountType);
        this.balance = Objects.requireNonNull(balance);
    }

    public static Account debitAccount(String ownerId, Money balance) {
        return new Account(ownerId, AccountType.DEBIT, balance);
    }

    /**
     * Determines whether the specified user is the owner of the account.
     */
    public boolean ownedBy(IndividualUser user) {
        return ownerId.equals(user.id());
    }

    /**
     * Determines whether this and the specified account operate in the same currency.
     */
    public boolean operateInSameCurrency(Account anotherAccount) {
        return balance().hasSameCurrency(anotherAccount.balance());
    }

    public boolean isDebitAccount() {
        return accountType == AccountType.DEBIT;
    }

    // Visible for testing.
    public Money balance() {
        return balance;
    }
}
