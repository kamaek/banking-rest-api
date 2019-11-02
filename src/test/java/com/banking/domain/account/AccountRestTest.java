package com.banking.domain.account;

import com.banking.domain.user.IndividualUser;
import com.banking.domain.user.UserRequests;
import com.banking.rest.RestTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.banking.domain.account.AccountRequests.postAccount;
import static com.banking.rest.ExpectedResponses.expectedUnprocessableEntity;

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
    @DisplayName("return 422 if account owner not exists")
    void return422IfAccountOwnerNotExists() {
        final String nonExistingOwnerId = UUID.randomUUID().toString();
        final String expectedErrorText = String.format("User %s not exists in the system.", nonExistingOwnerId);
        postAccount(nonExistingOwnerId, "USD", "0.00", AccountType.DEBIT.toString())
                .then()
                .spec(expectedUnprocessableEntity(expectedErrorText));
    }
}