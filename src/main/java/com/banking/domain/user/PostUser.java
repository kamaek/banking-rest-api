package com.banking.domain.user;

import com.banking.rest.PostRoute;
import com.banking.rest.ValidationException;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Optional;

public class PostUser extends PostRoute<IndividualUser> {

    private final UserService userService;

    public PostUser(UserService userService, Gson gson) {
        super(gson);
        this.userService = userService;
    }

    @Override
    protected IndividualUser create(Map<String, String> parameters) throws ValidationException {
        final String firstName = Optional.ofNullable(parameters.get("firstName")).orElse("");
        final String lastName = Optional.ofNullable(parameters.get("lastName")).orElse("");
        final boolean firstNameBlank = firstName.trim().isEmpty();
        final boolean lastNameBlank = lastName.trim().isEmpty();
        if (firstNameBlank || lastNameBlank) {
            throw new ValidationException("First name or last name is blank.");
        }
        return userService.signUpIndividual(firstName, lastName);
    }
}
