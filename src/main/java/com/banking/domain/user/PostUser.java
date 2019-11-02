package com.banking.domain.user;

import com.banking.rest.ValidationMessage;
import com.banking.rest.route.PostBody;
import com.banking.rest.route.PostRoute;
import com.banking.rest.route.ResourceCreationFailed;
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
    protected IndividualUser create(Body body) throws ResourceCreationFailed {
        return userService.signUpIndividual(body.firstName(), body.lastName());
    }

    @Override
    protected Body extractPayload(Request request, Gson gson) {
        final String rawBody = request.body();
        return gson.fromJson(rawBody, Body.class);
    }

    static class Body implements PostBody {

        private final String firstName;
        private final String lastName;

        @SuppressWarnings("unused" /* Used via reflection during deserialization. */)
        private Body() {
            this("", "");
        }

        Body(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public List<ValidationMessage> validate() {
            List<ValidationMessage> messages = new ArrayList<>();
            final boolean firstNameBlank = firstName().trim().isEmpty();
            final boolean lastNameBlank = lastName().trim().isEmpty();
            if (firstNameBlank) {
                messages.add(new ValidationMessage("First name of a user cannot be blank"));
            }
            if (lastNameBlank) {
                messages.add(new ValidationMessage("Last name of a user cannot be blank"));
            }
            return messages;
        }

        private String firstName() {
            return firstName;
        }

        private String lastName() {
            return lastName;
        }
    }
}
