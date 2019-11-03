package com.banking.domain.payment;

import com.banking.domain.money.Money;
import com.banking.persistence.Entity;

/**
 * A domestic payment, i.e. origin and destination accounts should operate in the same currency.
 */
public class DomesticPayment extends Entity {

    private final String senderAccountId;
    private final String recipientAccountId;
    private final Money instructedAmount;

    DomesticPayment(String senderAccountId, String recipientAccountId, Money instructedAmount) {
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.instructedAmount = instructedAmount;
    }

    String senderAccountId() {
        return senderAccountId;
    }

    String recipientAccountId() {
        return recipientAccountId;
    }

    Money instructedAmount() {
        return instructedAmount;
    }
}
