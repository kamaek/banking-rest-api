package com.banking.rest;

import java.util.List;

/**
 * The body of a POST request.
 */
public interface PostBody {

    /**
     * Validates the body and returns validation messages.
     *
     * @return validation messages or an empty list if the body is valid
     */
    List<ValidationMessage> validate();
}