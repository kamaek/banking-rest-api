package com.banking.rest;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;

/**
 * Expected response specifications to assert responses from the server.
 */
public final class ExpectedResponses {

    /**
     * Prevents instantiation of this utility class.
     */
    private ExpectedResponses() {
    }

    public static ResponseSpecification expectedSuccessResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.OK)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification expectedCreatedResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.CREATED)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification expectedNotFoundResponse(String resourcePath) {
        String expectedMessage = String.format("Resource /%s was not found. Please try again.", resourcePath);
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.NOT_FOUND)
                .expectContentType(ContentType.JSON)
                .expectBody("detail", equalTo(expectedMessage))
                .build();
    }

    public static ResponseSpecification expectedValidationError(String validationText) {
        return new ResponseSpecBuilder()
                .expectStatusCode(ResponseCode.UNPROCESSABLE_ENTITY)
                .expectContentType(ContentType.JSON)
                .expectBody("validationMessages", hasSize(1))
                .expectBody("validationMessages[0]", is(validationText))
                .build();
    }
}
