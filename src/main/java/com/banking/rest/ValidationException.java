package com.banking.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ValidationException extends Exception {

    private final Collection<String> reasons;

    public ValidationException(Collection<String> reasons) {
        this.reasons = new ArrayList<>(reasons);
    }

    public static ValidationException fromMessages(Collection<ValidationMessage> validationMessages) {
        final List<String> reasons = validationMessages.stream()
                .map(ValidationMessage::text)
                .collect(toList());
        return new ValidationException(reasons);
    }

    public Collection<String> reasons() {
        return new ArrayList<>(reasons);
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "reasons=" + reasons +
                '}';
    }
}
