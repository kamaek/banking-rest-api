package com.banking.rest.route;

import com.banking.persistence.Entity;
import com.banking.rest.ContentType;
import com.banking.rest.ResponseCode;
import com.banking.rest.ValidationException;
import com.banking.rest.ValidationMessage;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public abstract class PostRoute<E extends Entity, B extends PostBody> implements Route {

    private final Gson gson;
    private final Class<B> payloadClass;

    protected PostRoute(Gson gson, Class<B> payloadClass) {
        this.gson = gson;
        this.payloadClass = payloadClass;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final String rawBody = request.body();
        final B body = gson.fromJson(rawBody, payloadClass);
        final List<ValidationMessage> validationMessages = body.validate();
        if (!validationMessages.isEmpty()) {
            //TODO:30.10.2019:dmytro.hrankin: handle ValidationException somewhere
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
     */
    protected abstract E create(B body);
}
