package com.banking.domain.user;

import com.banking.rest.ContentType;
import com.banking.rest.RestTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.banking.domain.user.UserRequests.postUserSpecification;
import static com.banking.rest.ExpectedResponses.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        final String path = userPath("absent-id");
        when().get(path)
                .then()
                .spec(expectedNotFoundResponse(path));
    }

    @Test
    @DisplayName("create a user with non-blank first name and last name")
    void postUserWithNonBlankFirstAndLastName() {
        final String expectedFirstName = "John";
        final String expectedLastName = "Doe";
        postUser(expectedFirstName, expectedLastName)
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

    @Test
    @DisplayName("return existing user by ID")
    void getCreatedUser() {
        final IndividualUser createdUser = postUser("John", "Doe")
                .then().extract().body().as(IndividualUser.class);
        final String path = userPath(createdUser.id());
        final IndividualUser foundUser = when().get(path)
                .then()
                .spec(expectedSuccessResponse())
                .extract().body().as(IndividualUser.class);
        assertEquals(createdUser.id(), foundUser.id());
        assertEquals(createdUser.firstName(), foundUser.firstName());
        assertEquals(createdUser.lastName(), foundUser.lastName());
    }

    private static Response postUser(String firstName, String lastName) {
        return given().spec(postUserSpecification(firstName, lastName))
                .when().post(BASE_PATH);
    }

    private static String userPath(String userId) {
        return String.format("%s/%s", BASE_PATH, userId);
    }
}