package com.banking.domain.user;

/**
 * Signals that a user not exists in the system.
 */
public class UserNotExists extends Exception {

    private final String userId;

    public UserNotExists(String userId) {
        super(String.format("User %s not exists in the system.", userId));
        this.userId = userId;
    }

    public String userId() {
        return userId;
    }
}
