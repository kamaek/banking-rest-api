package com.banking.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ValidationException extends Exception {

    private final Collection<String> reasons;

    public ValidationException(String reason) {
        this(Collections.singleton(reason));
    }

    public ValidationException(Collection<String> reasons) {
        this.reasons = new ArrayList<>(reasons);
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "reasons=" + reasons +
                '}';
    }
}
