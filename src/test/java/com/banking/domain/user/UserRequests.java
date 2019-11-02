package com.banking.domain.user;

import com.banking.rest.ContentType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

/**
 * Specifications of requests to the user resource.
 */
public final class UserRequests {

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
}
