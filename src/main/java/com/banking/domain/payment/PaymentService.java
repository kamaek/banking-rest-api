package com.banking.domain.payment;

import com.banking.domain.account.Account;
import com.banking.domain.money.Money;
import com.banking.persistence.Repository;

/**
 * A domain service responsible transferring funds between accounts.
 *
 * <p>Money transfers are performed in a thread-safe way.
 */
public class PaymentService {

    private final Repository<Account> accountRepository;

    public PaymentService(Repository<Account> accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Transfers the instructed amount of money from the sender's account to recipient's account.
     *
     * <p>This is a domestic transfer, so accounts should {@linkplain Account#operateInSameCurrency(Account)
     * operate in the same currency} to fulfill this operation.
     *
     * @param sender           the account to withdraw money from
     * @param recipient        the account to deposit money to
     * @param instructedAmount the amount of money instructed to be transferred
     */
    public void transferMoneyDomestically(Account sender, Account recipient, Money instructedAmount)
            throws AccountsOperateInDifferentCurrencies, NotEnoughFunds, CannotTransferMoneyWithinSameAccount {
        //TODO:03.11.2019:dmytro.hrankin: check accounts exist
        checkMoneyTransferToDifferentAccount(sender, recipient);
        if (!sender.operateInSameCurrency(recipient)) {
            throw new AccountsOperateInDifferentCurrencies(sender.id(), recipient.id());
        }
        sender.withdraw(instructedAmount);
        recipient.deposit(instructedAmount);
        updateAccounts(sender, recipient);
    }

    private void updateAccounts(Account firstAccount, Account secondAccount) {
        accountRepository.add(firstAccount);
        accountRepository.add(secondAccount);
    }

    private void checkMoneyTransferToDifferentAccount(Account sender, Account recipient)
            throws CannotTransferMoneyWithinSameAccount {
        if (sender.id().equals(recipient.id())) {
            throw new CannotTransferMoneyWithinSameAccount();
        }
    }
}
