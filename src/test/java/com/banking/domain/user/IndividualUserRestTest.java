package com.banking.domain.user;

import com.banking.rest.ContentType;
import com.banking.rest.ResponseCode;
import com.banking.rest.RestTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class IndividualUserRestTest extends RestTest {

    @Test
    void getAllReturnsEmptyArray() {
        when().get("users")
                .then()
                .statusCode(ResponseCode.OK)
                .contentType(ContentType.JSON)
                .body("", hasSize(0));
    }

    @Test
    void getByIdReturnsNotFound() {
        final String path = "users/absent-id";
        String expectedMessage = String.format("Resource /%s was not found. Please try again.", path);
        when().get(path)
                .then()
                .statusCode(ResponseCode.NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("errorMessage", equalTo(expectedMessage));
    }
}