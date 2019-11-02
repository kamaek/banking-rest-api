package com.banking.domain.user;

import com.banking.rest.route.GetByIdRoute;
import com.google.gson.Gson;
import spark.Request;

import java.util.Optional;

public class GetUserById extends GetByIdRoute<IndividualUser> {

    private final UserService userService;

    public GetUserById(UserService userService, Gson gson) {
        super(gson);
        this.userService = userService;
    }

    @Override
    protected Optional<IndividualUser> entityWithId(String id, Request request) {
        return userService.individual(id);
    }
}
