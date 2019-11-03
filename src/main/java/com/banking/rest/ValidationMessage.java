package com.banking.rest;

public class ValidationMessage {

    private final String text;

    public ValidationMessage(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
