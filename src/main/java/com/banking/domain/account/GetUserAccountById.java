package com.banking.domain.account;

import com.banking.domain.user.UserNotExists;
import com.banking.rest.route.GetByIdRoute;
import com.banking.rest.route.UnprocessableEntityException;
import com.google.gson.Gson;
import spark.Request;

import java.util.Optional;

public class GetUserAccountById extends GetByIdRoute<Account> {

    private final AccountService accountService;

    public GetUserAccountById(AccountService accountService, Gson gson) {
        super(gson);
        this.accountService = accountService;
    }

    @Override
    protected Optional<Account> entityWithId(String id, Request request) throws UnprocessableEntityException {
        final String userId = request.params("userId");
        try {
            return accountService.accountOwnedBy(userId, id);
        } catch (UserNotExists e) {
            throw new UnprocessableEntityException(e);
        }
    }
}
