package com.banking.domain.money;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTypeAdapter extends TypeAdapter<Money> {

    private static final String AMOUNT_FIELD_NAME = "amount";
    private static final String CURRENCY_FIELD_NAME = "currency";

    @Override
    public void write(JsonWriter out, Money value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name(AMOUNT_FIELD_NAME);
        out.value(value.amount().toString());
        out.name(CURRENCY_FIELD_NAME);
        out.value(value.currency().getCurrencyCode());
        out.endObject();
    }

    @Override
    public Money read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.beginObject();
        final Map<String, String> fields = readFieldValues(in);
        in.endObject();
        final String amount = fields.get(AMOUNT_FIELD_NAME);
        final String currency = fields.get(CURRENCY_FIELD_NAME);
        return new Money(amount, currency);
    }

    private Map<String, String> readFieldValues(JsonReader in) throws IOException {
        final Map<String, String> fields = new HashMap<>();
        while (in.hasNext()) {
            fields.put(in.nextName(), in.nextString());
        }
        return fields;
    }
}
