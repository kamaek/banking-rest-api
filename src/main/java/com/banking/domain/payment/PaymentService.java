package com.banking.domain.payment;

import com.banking.domain.account.Account;
import com.banking.domain.account.AccountNotExists;
import com.banking.domain.money.Money;
import com.banking.persistence.Repository;

/**
 * A domain service responsible transferring funds between accounts.
 *
 * <p>Money transfers are performed in a thread-safe way.
 */
public class PaymentService {

    private final Repository<Account> accountRepository;
    private final Repository<DomesticPayment> paymentRepository;

    public PaymentService(Repository<Account> accountRepository, Repository<DomesticPayment> paymentRepository) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Transfers the instructed amount of money from the sender's account to recipient's account.
     *
     * <p>This is a domestic transfer, so accounts should {@linkplain Account#operateInSameCurrency(Account)
     * operate in the same currency} to fulfill this operation.
     *
     * @param senderId         the ID of the account to withdraw money from
     * @param recipientId      the ID of the account to deposit money to
     * @param instructedAmount the amount of money instructed to be transferred
     */
    public DomesticPayment transferMoneyDomestically(String senderId, String recipientId, Money instructedAmount)
            throws PaymentException, AccountNotExists {
        final Account sender = findAccountOrThrow(senderId);
        final Account recipient = findAccountOrThrow(recipientId);
        checkMoneyTransferToDifferentAccount(sender, recipient);
        checkHasSameCurrency(sender, recipient, instructedAmount);
        sender.withdraw(instructedAmount);
        recipient.deposit(instructedAmount);
        updateAccounts(sender, recipient);
        return createDomesticPayment(senderId, recipientId, instructedAmount);
    }

    private DomesticPayment createDomesticPayment(String senderId, String recipientId, Money instructedAmount) {
        final DomesticPayment payment = new DomesticPayment(senderId, recipientId, instructedAmount);
        paymentRepository.add(payment);
        return payment;
    }

    private Account findAccountOrThrow(String accountId) throws AccountNotExists {
        return accountRepository.entity(accountId)
                .orElseThrow(() -> new AccountNotExists(accountId));
    }

    private void updateAccounts(Account firstAccount, Account secondAccount) {
        accountRepository.add(firstAccount);
        accountRepository.add(secondAccount);
    }

    private void checkHasSameCurrency(Account sender, Account recipient, Money instructedAmount)
            throws AccountsOperateInDifferentCurrencies, InvalidPaymentCurrency {
        if (!sender.operateInSameCurrency(recipient)) {
            throw new AccountsOperateInDifferentCurrencies(sender.id(), recipient.id());
        }
        if (!sender.hasBalanceIn(instructedAmount.currency())) {
            final String errMsg = String.format("Accounts operate in %s, but instructed amount is in %s.",
                    sender.balance().currency(), instructedAmount.currency());
            throw new InvalidPaymentCurrency(errMsg);
        }
    }

    private void checkMoneyTransferToDifferentAccount(Account sender, Account recipient)
            throws CannotTransferMoneyWithinSameAccount {
        if (sender.id().equals(recipient.id())) {
            throw new CannotTransferMoneyWithinSameAccount();
        }
    }
}
