package com.banking;

import com.banking.domain.user.*;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
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
        Spark.awaitInitialization();
    }

    private void initUserRoutes(Gson gson, UserService userService) {
        Spark.path("/users", () -> {
            Spark.post("", new PostUser(userService, gson));
            Spark.get("", new GetAllUsers(userService, gson));
            Spark.get("/:id", new GetUserById(userService, gson));
        });
    }
}
