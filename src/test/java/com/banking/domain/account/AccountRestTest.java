package com.banking.domain.account;

import com.banking.domain.user.IndividualUser;
import com.banking.domain.user.UserRequests;
import com.banking.rest.RestTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.banking.domain.account.AccountRequests.*;
import static com.banking.rest.ExpectedResponses.*;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Account resource should")
class AccountRestTest extends RestTest {

    private IndividualUser batman;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        batman = UserRequests.postUserAndExtractBody("Mr.", "Batman");
    }

    @Test
    @DisplayName("return an empty array if there are no accounts")
    void getAllReturnsEmptyArray() {
        when().get(accountsPath(batman.id()))
                .then()
                .spec(expectedSuccessResponse())
                .body("", hasSize(0));
    }

    @Test
    @DisplayName("return 404 if the account with the specified ID not exists")
    void getByIdReturnsNotFound() {
        final String path = accountPath(batman.id(), UUID.randomUUID().toString());
        when().get(path)
                .then()
                .spec(expectedNotFoundResponse(path));
    }

    @Test
    @DisplayName("return 422 if account owner not exists")
    void return422IfAccountOwnerNotExists() {
        final String nonExistingOwnerId = UUID.randomUUID().toString();
        final String expectedErrorText = String.format("User %s not exists in the system.", nonExistingOwnerId);
        postAccount(nonExistingOwnerId, "USD", "0.00", AccountType.DEBIT.toString())
                .then()
                .spec(expectedUnprocessableEntity(expectedErrorText));
    }

    @Test
    @DisplayName("open a debit account with the initial balance")
    void openDebitAccountWithInitialBalance() {
        final String expectedOwnerId = batman.id();
        final String expectedCurrency = "USD";
        final String expectedAmount = "50.00";
        final String expectedAccountType = AccountType.DEBIT.toString();
        postAccount(expectedOwnerId, expectedCurrency, expectedAmount, expectedAccountType)
                .then()
                .spec(expectedCreatedResponse())
                .body(
                        "id", not(emptyString()),
                        "balance.currency", is(expectedCurrency),
                        "balance.amount", is(expectedAmount),
                        "accountType", is(expectedAccountType)
                );
    }

    @Test
    @DisplayName("return existing account by ID")
    void returnExistingAccountById() {
        final Account expectedAccount = postAccountAndExtractResponse(batman.id(), "EUR", "100.00", AccountType.DEBIT.toString());
        final Account foundAccount = when().get(accountPath(batman.id(), expectedAccount.id()))
                .then()
                .spec(expectedSuccessResponse())
                .extract().body().as(Account.class);
        assertEquals(expectedAccount.id(), foundAccount.id());
        assertTrue(foundAccount.ownedBy(batman));
        assertEquals(expectedAccount.balance(), foundAccount.balance());
    }
}