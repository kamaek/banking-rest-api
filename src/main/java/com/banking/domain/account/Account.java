package com.banking.domain.account;

import com.banking.domain.money.Money;
import com.banking.domain.money.NegativeMoneyAmount;
import com.banking.domain.payment.NotEnoughFunds;
import com.banking.domain.user.IndividualUser;
import com.banking.persistence.Entity;

import java.util.Currency;
import java.util.Objects;

import static java.lang.String.format;

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
     * Withdraws the specified money from this account.
     *
     * <p>This method is thread-safe assuming that there is only one {@link Account} object with the same ID.
     * Otherwise, it would be better to use locking based on the value of account's ID.
     *
     * @throws NotEnoughFunds if the account has not enough funds
     */
    public void withdraw(Money money) throws NotEnoughFunds {
        checkCanOperateWith(money);
        synchronized (this) {
            try {
                balance = balance.subtract(money);
            } catch (NegativeMoneyAmount e) {
                throw new NotEnoughFunds(id());
            }
        }
    }

    /**
     * Deposits the specified money to this account.
     *
     * <p>This method is thread-safe assuming that there is only one {@link Account} object with the same ID.
     * Otherwise, it would be better to use locking based on the value of account's ID.
     */
    public void deposit(Money money) {
        checkCanOperateWith(money);
        synchronized (this) {
            balance = balance.add(money);
        }
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

    /**
     * Determines whether this account has enough funds to transfer the specified amount of money.
     */
    public boolean hasEnoughFundsToTransfer(Money money) {
        return money.isLessThan(balance);
    }

    public boolean hasBalanceIn(Currency currency) {
        return balance().isInCurrency(currency);
    }

    public boolean isDebitAccount() {
        return accountType == AccountType.DEBIT;
    }

    // Visible for testing.
    public Money balance() {
        return balance;
    }

    private void checkCanOperateWith(Money money) {
        if (!balance().hasSameCurrency(money)) {
            final String errMsg = format("Cannot operate with %s on %s account.", money.currency(), balance().currency());
            throw new IllegalStateException(errMsg);
        }
    }
}
