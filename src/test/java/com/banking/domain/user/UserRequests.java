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
        final PostUserPayload payload = new PostUserPayload(firstName, lastName);
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setBody(payload)
                .build();
    }

    private static final class PostUserPayload {

        private final String firstName;
        private final String lastName;

        private PostUserPayload(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "PostUserPayload{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
}
