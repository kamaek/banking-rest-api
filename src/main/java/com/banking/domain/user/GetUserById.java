package com.banking.domain.user;

import com.banking.rest.GetByIdRoute;
import com.google.gson.Gson;

import java.util.Optional;

public class GetUserById extends GetByIdRoute<IndividualUser> {

    private final UserService userService;

    public GetUserById(UserService userService, Gson gson) {
        super(gson);
        this.userService = userService;
    }

    @Override
    protected Optional<IndividualUser> entityWithId(String id) {
        return userService.individual(id);
    }
}
