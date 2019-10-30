package com.banking;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class WebServer {

    public static void main(String[] args) {
        Spark.path("/users", () -> {
            Spark.post("", (req, res) -> "Post a user\n");
            Spark.get("", (req, res) -> "Get all users\n");
            Spark.get("/:id", (req, res) -> "Get a user\n");
        });
    }

    private static class GetAllUsers implements Route {

        @Override
        public Object handle(Request request, Response response) throws Exception {
            response.type("application/json");
            response.body();
            return null;
        }
    }
}
