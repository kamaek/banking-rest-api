package com.banking.domain.account;

import com.banking.domain.user.UserRequests;
import com.banking.rest.ContentType;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Specifications of requests to the user resource.
 */
public class AccountRequests {

    /**
     * Prevents instantiation of this utility class.
     */
    private AccountRequests() {
    }

    public static String basePath(String userId) {
        return String.format("%s/%s/accounts", UserRequests.BASE_PATH, userId);
    }

    public static Response postAccount(String ownerId, String currency, String initialAmount, String accountType) {
        final JsonObject json = new JsonObject();
        json.addProperty("currency", currency);
        json.addProperty("initialAmount", initialAmount);
        json.addProperty("accountType", accountType);
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(basePath(ownerId));
    }
}
