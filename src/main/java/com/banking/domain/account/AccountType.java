package com.banking.domain.account;

import com.banking.rest.ValidationMessage;

import java.util.Optional;
import java.util.stream.Stream;

public enum AccountType {

    /**
     * Debit account, which cannot have negative balance.
     */
    DEBIT("DebitAccount");

    private final String representation;

    AccountType(String representation) {
        this.representation = representation;
    }

    public static Optional<AccountType> fromRepresentation(String representation) {
        return Stream.of(AccountType.values())
                .filter(accountType -> accountType.representation.equals(representation))
                .findAny();
    }

    public static Optional<ValidationMessage> validateAccountType(String representation) {
        final Optional<AccountType> accountType = fromRepresentation(representation);
        if (accountType.isPresent()) {
            return Optional.empty();
        }
        final String text = String.format("Invalid account type – %s.", representation);
        return Optional.of(new ValidationMessage(text));
    }

    @Override
    public String toString() {
        return representation;
    }
}
