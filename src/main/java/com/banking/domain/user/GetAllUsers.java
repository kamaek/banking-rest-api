package com.banking.domain.user;

import com.banking.rest.GetAllRoute;
import com.google.gson.Gson;

import java.util.Collection;

public class GetAllUsers extends GetAllRoute<IndividualUser> {

    private final UserService userService;

    public GetAllUsers(UserService userService, Gson gson) {
        super(gson);
        this.userService = userService;
    }

    @Override
    protected Collection<IndividualUser> responseBody() {
        return userService.allIndividuals();
    }
}
