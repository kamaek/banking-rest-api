package com.banking.domain.account;

import com.banking.domain.user.UserNotExists;
import com.banking.rest.route.GetAllRoute;
import com.banking.rest.route.UnprocessableEntityException;
import com.google.gson.Gson;
import spark.Request;

import java.util.Collection;

public class GetUserAccounts extends GetAllRoute<Account> {

    private final AccountService accountService;

    public GetUserAccounts(AccountService accountService, Gson gson) {
        super(gson);
        this.accountService = accountService;
    }

    @Override
    protected Collection<Account> responseBody(Request request) throws UnprocessableEntityException {
        final String userId = request.params("userId");
        try {
            return accountService.accountsOwnedBy(userId);
        } catch (UserNotExists e) {
            throw new UnprocessableEntityException(e);
        }
    }
}
