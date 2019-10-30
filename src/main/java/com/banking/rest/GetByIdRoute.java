package com.banking.rest;

import com.banking.persistence.Entity;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Optional;

public abstract class GetByIdRoute<T extends Entity> implements Route {

    private final Gson gson;

    protected GetByIdRoute(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final String id = request.params(":id");
        final Optional<T> entity = entityWithId(id);
        response.type(ContentType.JSON);
        if (entity.isPresent()) {
            response.status(ResponseCode.OK);
            return gson.toJson(entity.get());
        } else {
            response.status(ResponseCode.NOT_FOUND);
            final String errMsg = String.format("Resource %s was not found. Please try again.", request.pathInfo());
            return gson.toJson(new ErrorResponse(errMsg));
        }
    }

    protected abstract Optional<T> entityWithId(String id);
}
