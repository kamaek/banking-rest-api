package com.banking.domain.account;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class AccountTypeAdapter extends TypeAdapter<AccountType> {

    @Override
    public void write(JsonWriter out, AccountType value) throws IOException {
        out.value(value == null ? null : value.toString());
    }

    @Override
    public AccountType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return AccountType.fromRepresentation(in.nextString()).orElse(null);
    }
}
