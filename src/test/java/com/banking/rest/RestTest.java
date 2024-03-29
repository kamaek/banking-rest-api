package com.banking.rest;

import com.banking.WebServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

public abstract class RestTest {

    @BeforeEach
    protected void setUp() {
        RestAssured.port = WebServer.DEFAULT_PORT;
        new WebServer().start(WebServer.DEFAULT_PORT);
    }

    @AfterEach
    protected void tearDown() {
        Spark.stop();
        Spark.awaitStop();
    }
}
