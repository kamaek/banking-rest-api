package com.banking.rest.route;

import com.banking.rest.ValidationMessage;

import java.util.List;
import java.util.Optional;

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

    default Optional<ValidationMessage> validateNotNullOrBlank(String value, String validationMessage) {
        return value == null || value.trim().isEmpty()
                ? Optional.of(new ValidationMessage(validationMessage))
                : Optional.empty();
    }

    default <T> Optional<ValidationMessage> validateNotNul(T value, String validationMessage) {
        return value == null
                ? Optional.of(new ValidationMessage(validationMessage))
                : Optional.empty();
    }
}
