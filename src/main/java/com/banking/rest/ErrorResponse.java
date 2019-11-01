package com.banking.rest;

import java.util.Collection;

public class ErrorResponse {

    private final String detail;
    private final Collection<String> validationMessages;

    private ErrorResponse(Builder builder) {
        this.detail = builder.detail;
        this.validationMessages = builder.validationMessages;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "detail='" + detail + '\'' +
                ", validationMessages=" + validationMessages +
                '}';
    }

    public static class Builder {

        private String detail;
        private Collection<String> validationMessages;

        /**
         * Prevents direct instantiation of this class.
         */
        private Builder() {
        }

        public Builder addDetail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder addValidationMessages(ValidationException exception) {
            this.validationMessages = exception.reasons();
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
