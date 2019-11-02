package com.banking.rest.route;

/**
 * Signals that request failed because the request is semantically wrong.
 */
public final class UnprocessableEntityException extends Exception {

    public UnprocessableEntityException(Throwable cause) {
        super(cause);
    }
}
