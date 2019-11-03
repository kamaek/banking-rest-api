package com.banking.domain.account;

import com.banking.domain.user.UserRequests;
import com.banking.rest.ContentType;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

import static com.banking.rest.ExpectedResponses.expectedSuccessResponse;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

/**
 * Specifications of requests to the user resource.
 */
public class AccountRequests {

    /**
     * Prevents instantiation of this utility class.
     */
    private AccountRequests() {
    }

    static String accountsPath(String userId) {
        return String.format("%s/%s/accounts", UserRequests.BASE_PATH, userId);
    }

    static String accountPath(String userId, String accountId) {
        return String.format("%s/%s", accountsPath(userId), accountId);
    }

    static Response postAccount(String ownerId, String currency, String initialAmount, String accountType) {
        final JsonObject json = new JsonObject();
        json.addProperty("currency", currency);
        json.addProperty("initialAmount", initialAmount);
        json.addProperty("accountType", accountType);
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(accountsPath(ownerId));
    }

    public static Account postAccountAndExtractResponse(String ownerId,
                                                        String currency,
                                                        String initialAmount,
                                                        String accountType) {
        return postAccount(ownerId, currency, initialAmount, accountType)
                .then()
                .extract().body().as(Account.class);
    }

    public static Account getAccountAndExtractBody(String userId, String accountId) {
        return when().get(accountPath(userId, accountId))
                .then()
                .spec(expectedSuccessResponse())
                .extract().body().as(Account.class);
    }
}
