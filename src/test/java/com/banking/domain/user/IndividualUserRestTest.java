package com.banking.domain.user;

import com.banking.rest.RestTest;
import org.junit.jupiter.api.Test;

import static com.banking.domain.user.UserRequests.postUserSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

class IndividualUserRestTest extends RestTest {

    private static final String BASE_PATH = "/users";

    @Test
    void getAllReturnsEmptyArray() {
        when().get(BASE_PATH)
                .then()
                .spec(expectedSuccessResponse())
                .body("", hasSize(0));
    }

    @Test
    void getByIdReturnsNotFound() {
        final String path = BASE_PATH + "/absent-id";
        when().get(path)
                .then()
                .spec(expectedNotFoundResponse(path));
    }

    @Test
    void postUserWithNonEmptyFirstAndLastName() {
        final String expectedFirstName = "John";
        final String expectedLastName = "Doe";
        given().spec(postUserSpecification(expectedFirstName, expectedLastName))
                .when().post(BASE_PATH)
                .then()
                .spec(expectedCreatedResponse())
                .body(
                        "id", not(emptyString()),
                        "firstName", is(expectedFirstName),
                        "lastName", is(expectedLastName)
                );

    }
}