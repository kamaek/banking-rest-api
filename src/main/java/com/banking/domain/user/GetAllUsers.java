package com.banking.domain.user;

import com.banking.rest.route.GetAllRoute;
import com.google.gson.Gson;
import spark.Request;

import java.util.Collection;

public class GetAllUsers extends GetAllRoute<IndividualUser> {

    private final UserService userService;

    public GetAllUsers(UserService userService, Gson gson) {
        super(gson);
        this.userService = userService;
    }

    @Override
    protected Collection<IndividualUser> responseBody(Request request) {
        return userService.allIndividuals();
    }
}
