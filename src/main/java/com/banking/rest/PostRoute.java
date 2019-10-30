package com.banking.rest;

import com.banking.persistence.Entity;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public abstract class PostRoute<T extends Entity> implements Route {

    private final Gson gson;

    protected PostRoute(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, String> parameters = request.params();
        //TODO:30.10.2019:dmytro.hrankin: handle ValidationException
        final T createdEntity = create(parameters);
        response.type(ContentType.JSON);
        response.status(ResponseCode.CREATED);
        return gson.toJson(createdEntity);
    }

    protected abstract T create(Map<String, String> parameters) throws ValidationException;
}
