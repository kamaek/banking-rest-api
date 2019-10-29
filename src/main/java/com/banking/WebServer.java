package com.banking;

import spark.Spark;

public class WebServer {

    public static void main(String[] args) {
        Spark.get("/hello", (req, res) -> "Hello World\n");
    }
}
