package com.banking;

import com.banking.domain.user.GetAllUsers;
import com.banking.domain.user.GetUserById;
import com.banking.domain.user.IndividualUser;
import com.banking.domain.user.UserService;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import com.google.gson.Gson;
import spark.Spark;

public class WebServer {

    private static final Gson GSON = new Gson();

    public static void main(String[] args) {
        final Repository<IndividualUser> userRepository = new InMemoryRepository<>();
        final UserService userService = new UserService(userRepository);

        Spark.path("/users", () -> {
            Spark.post("", (req, res) -> "Post a user\n");
            Spark.get("", new GetAllUsers(userService, GSON));
            Spark.get("/:id", new GetUserById(userService, GSON));
        });
    }
}
