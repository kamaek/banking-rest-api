package com.banking.rest.route;

import com.banking.persistence.Entity;
import com.banking.rest.ContentType;
import com.banking.rest.ResponseCode;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;

public abstract class GetAllRoute<T extends Entity> implements Route {

    private final Gson gson;

    protected GetAllRoute(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type(ContentType.JSON);
        response.status(ResponseCode.OK);
        return gson.toJson(responseBody());
    }

    protected abstract Collection<T> responseBody();
}
