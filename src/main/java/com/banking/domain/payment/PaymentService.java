package com.banking.domain.payment;

import com.banking.domain.account.Account;
import com.banking.domain.money.Money;

/**
 * A domain service responsible transferring funds between accounts.
 */
//TODO:03.11.2019:dmytro.hrankin: ensure this class is thread-safe add a note about this
public class PaymentService {

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
            throws AccountsOperateInDifferentCurrencies, NotEnoughFunds {
        //TODO:03.11.2019:dmytro.hrankin: check accounts exist
        //TODO:03.11.2019:dmytro.hrankin: implement
    }
}
