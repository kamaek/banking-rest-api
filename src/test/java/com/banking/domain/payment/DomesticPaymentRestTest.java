package com.banking.domain.payment;

import com.banking.domain.account.Account;
import com.banking.domain.account.AccountType;
import com.banking.domain.money.Money;
import com.banking.domain.user.IndividualUser;
import com.banking.rest.ContentType;
import com.banking.rest.RestTest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.banking.domain.account.AccountRequests.getAccountAndExtractBody;
import static com.banking.domain.account.AccountRequests.postAccountAndExtractResponse;
import static com.banking.domain.user.UserRequests.postUserAndExtractBody;
import static com.banking.rest.ExpectedResponses.expectedCreatedResponse;
import static com.banking.rest.ExpectedResponses.expectedUnprocessableEntity;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DomesticPayment resource should")
class DomesticPaymentRestTest extends RestTest {

    private static final String BASE_PATH = "domestic-payments";

    private IndividualUser batman;
    private Account shoppingAccount;
    private Account savingsAccount;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        batman = postUserAndExtractBody("Mr.", "Batman");
        final String accountType = AccountType.DEBIT.toString();
        shoppingAccount = postAccountAndExtractResponse(batman.id(), "USD", "100.00", accountType);
        savingsAccount = postAccountAndExtractResponse(batman.id(), "USD", "1000.00", accountType);
    }

    @Test
    @DisplayName("perform payment and adjust account balances")
    void performPaymentAndAdjustAccountBalances() {
        final String expectedAmount = "10.00";
        final String expectedCurrency = "USD";
        postPayment(savingsAccount.id(), shoppingAccount.id(), expectedAmount, expectedCurrency)
                .then()
                .spec(expectedCreatedResponse())
                .body(
                        "id", not(emptyString()),
                        "instructedAmount.amount", is(expectedAmount),
                        "instructedAmount.currency", is(expectedCurrency),
                        "senderAccountId", is(savingsAccount.id()),
                        "recipientAccountId", is(shoppingAccount.id())
                );
        final Account updatedSavingsAccount = getAccountAndExtractBody(batman.id(), savingsAccount.id());
        final Account updatedShoppingAccount = getAccountAndExtractBody(batman.id(), shoppingAccount.id());
        assertEquals("110.00 USD", updatedShoppingAccount.balance().toString());
        assertEquals("990.00 USD", updatedSavingsAccount.balance().toString());
    }

    @Test
    @DisplayName("return 422 if transferred money in a different currency")
    void notPerformPaymentIfAccountNotExists() {
        postPayment(savingsAccount.id(), shoppingAccount.id(), "10.00", "EUR")
                .then()
                .spec(expectedUnprocessableEntity("Accounts operate in USD, but instructed amount is in EUR."));
    }

    @Test
    @DisplayName("return 422 if sender doesn't have enough money for a transfer")
    void notPerformPaymentIfAccountOutOfMoney() {
        final Money exceedsSavings = savingsAccount.balance().add(new Money("1.00", "USD"));
        final String amountToTransfer = exceedsSavings.amount().toString();
        final String expectedMessage = format("Account %s doesn't have enough funds to perform a payment.", savingsAccount.id());
        postPayment(savingsAccount.id(), shoppingAccount.id(), amountToTransfer, "USD")
                .then()
                .spec(expectedUnprocessableEntity(expectedMessage));
    }

    private static Response postPayment(String originAccountId, String destinationAccountId, String amount, String currency) {
        final JsonObject json = new JsonObject();
        json.addProperty("senderAccountId", originAccountId);
        json.addProperty("recipientAccountId", destinationAccountId);
        json.add("instructedAmount", new Gson().toJsonTree(new Money(amount, currency)));
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(BASE_PATH);
    }
}