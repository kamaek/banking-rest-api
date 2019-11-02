package com.banking;

import com.banking.domain.account.Account;
import com.banking.domain.account.AccountService;
import com.banking.domain.account.PostAccount;
import com.banking.domain.user.*;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import com.banking.rest.ContentType;
import com.banking.rest.ErrorResponse;
import com.banking.rest.ResponseCode;
import com.banking.rest.ValidationException;
import com.banking.rest.route.ResourceCreationFailed;
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
        final Repository<Account> accountRepository = new InMemoryRepository<>();
        final UserService userService = new UserService(userRepository);
        final AccountService accountService = new AccountService(accountRepository, userRepository);

        Spark.port(port);
        initUserRoutes(gson, userService);
        initAccountRoutes(gson, accountService);
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

    private void initAccountRoutes(Gson gson, AccountService accountService) {
        Spark.path("/users/:userId/accounts", () -> {
            Spark.post("", new PostAccount(accountService, gson));
            //TODO:02.11.2019:dmytro.hrankin: add missing endpoints
//            Spark.get("", new GetAllUsers(userService, gson));
//            Spark.get("/:id", new GetUserById(userService, gson));
        });
    }

    private void addExceptionsHandlers(Gson gson) {
        Spark.exception(ValidationException.class, (e, request, response) -> {
            final ErrorResponse responseBody = ErrorResponse.newBuilder().addValidationMessages(e).build();
            response.type(ContentType.JSON);
            response.status(ResponseCode.UNPROCESSABLE_ENTITY);
            response.body(gson.toJson(responseBody));
        });
        Spark.exception(ResourceCreationFailed.class, (e, request, response) -> {
            final String detail = e.getCause().getMessage();
            final ErrorResponse responseBody = ErrorResponse.newBuilder().addDetail(detail).build();
            response.type(ContentType.JSON);
            response.status(ResponseCode.UNPROCESSABLE_ENTITY);
            response.body(gson.toJson(responseBody));
        });
    }
}
