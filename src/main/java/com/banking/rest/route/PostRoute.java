package com.banking.rest.route;

import com.banking.persistence.Entity;
import com.banking.rest.ContentType;
import com.banking.rest.ResponseCode;
import com.banking.rest.ValidationException;
import com.banking.rest.ValidationMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.Optional;

public abstract class PostRoute<E extends Entity, B extends PostBody> implements Route {

    private final Gson gson;

    protected PostRoute(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final B body = extractPayload(request, gson);
        final List<ValidationMessage> validationMessages = body.validate();
        if (!validationMessages.isEmpty()) {
            throw ValidationException.fromMessages(validationMessages);
        }
        final E createdEntity = create(body);
        response.type(ContentType.JSON);
        response.status(ResponseCode.CREATED);
        return gson.toJson(createdEntity);
    }

    /**
     * Creates an entity from the valid request body.
     *
     * @param body the valid request body
     * @return created resource
     * @throws UnprocessableEntityException if business rules don't allow to create an entity
     */
    protected abstract E create(B body) throws UnprocessableEntityException;

    /**
     * Extracts payload from the specified request.
     */
    protected abstract B extractPayload(Request request, Gson gson);

    protected JsonObject bodyAsJsonObject(Request request) {
        return gson.fromJson(request.body(), JsonObject.class);
    }

    protected Optional<String> extractString(JsonObject json, String fieldName) {
        final Optional<JsonElement> field = Optional.ofNullable(json.get(fieldName));
        return field.map(JsonElement::getAsString);
    }

    protected Optional<String> extractParameter(Request request, String parameterName) {
        return Optional.ofNullable(request.params(parameterName));
    }
}
