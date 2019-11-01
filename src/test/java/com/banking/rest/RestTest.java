package com.banking.rest;

import com.banking.WebServer;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

import static org.hamcrest.Matchers.equalTo;

public abstract class RestTest {

    @BeforeEach
    void setUp() {
        RestAssured.port = WebServer.DEFAULT_PORT;
        new WebServer().start(WebServer.DEFAULT_PORT);
    }

    @AfterEach
    void tearDown() {
        Spark.stop();
        Spark.awaitStop();
    }

    protected static ResponseSpecification expectedSuccessResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.OK)
                .expectContentType(ContentType.JSON)
                .build();
    }

    protected static ResponseSpecification expectedCreatedResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.CREATED)
                .expectContentType(ContentType.JSON)
                .build();
    }

    protected static ResponseSpecification expectedNotFoundResponse(String resourcePath) {
        String expectedMessage = String.format("Resource /%s was not found. Please try again.", resourcePath);
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.NOT_FOUND)
                .expectContentType(ContentType.JSON)
                .expectBody("errorMessage", equalTo(expectedMessage))
                .build();
    }
}
