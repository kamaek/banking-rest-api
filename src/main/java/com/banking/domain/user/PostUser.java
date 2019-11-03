package com.banking.domain.user;

import com.banking.rest.ValidationMessage;
import com.banking.rest.route.PostBody;
import com.banking.rest.route.PostRoute;
import com.google.gson.Gson;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class PostUser extends PostRoute<IndividualUser, PostUser.Body> {

    private final UserService userService;

    public PostUser(UserService userService, Gson gson) {
        super(gson);
        this.userService = userService;
    }

    @Override
    protected IndividualUser create(Body body) {
        return userService.signUpIndividual(body.firstName, body.lastName);
    }

    @Override
    protected Body extractPayload(Request request, Gson gson) {
        final String rawBody = request.body();
        return gson.fromJson(rawBody, Body.class);
    }

    static class Body implements PostBody {

        private final String firstName;
        private final String lastName;

        Body(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public List<ValidationMessage> validate() {
            final List<ValidationMessage> messages = new ArrayList<>();
            validateNotNullOrBlank(firstName, "First name of a user cannot be blank.").ifPresent(messages::add);
            validateNotNullOrBlank(lastName, "Last name of a user cannot be blank.").ifPresent(messages::add);
            return messages;
        }
    }
}
