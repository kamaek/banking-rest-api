package com.banking.domain.user;

import com.banking.persistence.Entity;

/**
 * An individual user of a bank, which can have multiple accounts.
 *
 * <p>This user is an individual, not a legal entity.
 */
public class IndividualUser extends Entity {

    private final String firstName;
    private final String secondName;

    public IndividualUser(String firstName, String secondName) {
        super();
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return String.format("Individual: %s %s", firstName, secondName);
    }
}
