package com.banking.domain.user;

import com.banking.rest.ContentType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Specifications of requests to the user resource.
 */
public final class UserRequests {

    public static final String BASE_PATH = "users";

    /**
     * Prevents instantiation of this utility class.
     */
    private UserRequests() {
    }

    public static RequestSpecification postUserSpecification(String firstName, String lastName) {
        final PostUser.Body payload = new PostUser.Body(firstName, lastName);
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setBody(payload)
                .build();
    }

    public static Response postUser(String firstName, String lastName) {
        return given().spec(postUserSpecification(firstName, lastName))
                .when().post(BASE_PATH);
    }

    public static IndividualUser postUserAndExtractBody(String firstName, String lastName) {
        return postUser(firstName, lastName)
                .then().extract().body().as(IndividualUser.class);
    }

    public static String userPath(String userId) {
        return String.format("%s/%s", BASE_PATH, userId);
    }
}
