package com.banking.domain.user;

import com.banking.rest.ContentType;
import com.banking.rest.RestTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.banking.domain.user.UserRequests.postUserSpecification;
import static com.banking.rest.ExpectedResponses.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@DisplayName("IndividualUser resource should")
class IndividualUserRestTest extends RestTest {

    private static final String BASE_PATH = "users";

    @Test
    @DisplayName("return an empty array if there are not users")
    void getAllReturnsEmptyArray() {
        when().get(BASE_PATH)
                .then()
                .spec(expectedSuccessResponse())
                .body("", hasSize(0));
    }

    @Test
    @DisplayName("return 404 if the user with the specified ID was not found")
    void getByIdReturnsNotFound() {
        final String path = BASE_PATH + "/absent-id";
        when().get(path)
                .then()
                .spec(expectedNotFoundResponse(path));
    }

    @Test
    @DisplayName("create a user with non-blank first name and last name")
    void postUserWithNonBlankFirstAndLastName() {
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

    @Test
    @DisplayName("return 422 if first name is not specified during user creation")
    void notPostUserWithAbsentFirstName() {
        given().contentType(ContentType.JSON)
                .body("{\"lastName\": \"Doe\"}")
                .when().post(BASE_PATH)
                .then()
                .spec(expectedValidationError("First name of a user cannot be blank"));
    }
}