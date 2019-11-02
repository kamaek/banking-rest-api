package com.banking.rest.route;

/**
 * Signals that a POST request failed.
 */
public final class ResourceCreationFailed extends Exception {

    public ResourceCreationFailed(Throwable cause) {
        super(cause);
    }
}
