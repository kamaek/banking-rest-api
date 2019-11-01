package com.banking;

import com.banking.domain.user.*;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import com.banking.rest.ContentType;
import com.banking.rest.ErrorResponse;
import com.banking.rest.ResponseCode;
import com.banking.rest.ValidationException;
import com.google.gson.Gson;
import spark.Spark;

public class WebServer {

    public static final int DEFAULT_PORT = 4567;

    public static void main(String[] args) {
        new WebServer().start(DEFAULT_PORT);
    }

    public void start(int port) {
        final Gson gson = new Gson();
        final Repository<IndividualUser> userRepository = new InMemoryRepository<>();
        final UserService userService = new UserService(userRepository);

        Spark.port(port);
        initUserRoutes(gson, userService);
        addExceptionsHandlers(gson);
        Spark.awaitInitialization();
    }

    private void initUserRoutes(Gson gson, UserService userService) {
        Spark.path("/users", () -> {
            Spark.post("", new PostUser(userService, gson));
            Spark.get("", new GetAllUsers(userService, gson));
            Spark.get("/:id", new GetUserById(userService, gson));
        });
    }

    private void addExceptionsHandlers(Gson gson) {
        Spark.exception(ValidationException.class, (e, request, response) -> {
            final ErrorResponse responseBody = ErrorResponse.newBuilder().addValidationMessages(e).build();
            response.type(ContentType.JSON);
            response.status(ResponseCode.UNPROCESSABLE_ENTITY);
            response.body(gson.toJson(responseBody));
        });
    }
}
