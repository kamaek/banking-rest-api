package com.banking.domain.money;

import java.util.Objects;

/**
 * A value object representing currency (e.g. USD, EUR).
 */
public final class Currency {

    /**
     * ISO 4217 currency code.
     *
     * @see <a href="https://www.ibm.com/support/knowledgecenter/en/SSZLC2_7.0.0/com.ibm.commerce.payments.developer.doc/refs/rpylerl2mst97.htm">Currency codes</a>
     */
    private final String code;

    Currency(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalStateException("Currency code cannot be blank.");
        }
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return code.equals(currency.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
