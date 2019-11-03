package com.banking.domain.account;

import com.banking.domain.money.Currencies;
import com.banking.domain.money.Money;
import com.banking.domain.user.UserNotExists;
import com.banking.rest.ValidationMessage;
import com.banking.rest.route.PostBody;
import com.banking.rest.route.PostRoute;
import com.banking.rest.route.UnprocessableEntityException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class PostAccount extends PostRoute<Account, PostAccount.Body> {

    private final AccountService accountService;

    public PostAccount(AccountService accountService, Gson gson) {
        super(gson);
        this.accountService = accountService;
    }

    @Override
    protected Account create(Body body) throws UnprocessableEntityException {
        try {
            return accountService.openAccount(body.ownerId(), body.initialBalance(), body.accountType());
        } catch (UserNotExists e) {
            throw new UnprocessableEntityException(e);
        }
    }

    @Override
    protected Body extractPayload(Request request, Gson gson) {
        final JsonObject json = bodyAsJsonObject(request);
        final String ownerId = extractParameter(request, "userId").orElse("");
        final String currency = extractString(json, "currency").orElse("");
        final String initialAmount = extractString(json, "initialAmount").orElse("");
        final String accountType = extractString(json, "accountType").orElse("");
        return new Body(ownerId, currency, initialAmount, accountType);
    }

    static class Body implements PostBody {

        private final String ownerId;

        /**
         * ISO 4217 currency code.
         */
        private final String currency;

        /**
         * Initial amount of money to open the account with, e.g. {@code "100.00"}.
         */
        private final String initialAmount;

        /**
         * The type of account to create.
         *
         * @see AccountType
         */
        private final String accountType;

        Body(String ownerId, String currency, String initialAmount, String accountType) {
            this.ownerId = ownerId;
            this.currency = currency;
            this.initialAmount = initialAmount;
            this.accountType = accountType;
        }

        @Override
        public List<ValidationMessage> validate() {
            final List<ValidationMessage> messages = new ArrayList<>();
            final boolean ownerIdBlank = ownerId.trim().isEmpty();
            if (ownerIdBlank) {
                messages.add(new ValidationMessage("Field 'ownerId' cannot be blank."));
            }
            AccountType.validateAccountType(accountType).ifPresent(messages::add);
            Currencies.validateCurrencyCode(currency).ifPresent(messages::add);
            Money.validateAmount(initialAmount).ifPresent(messages::add);
            return messages;
        }

        private String ownerId() {
            return ownerId;
        }

        private Money initialBalance() {
            return new Money(initialAmount, currency);
        }

        public AccountType accountType() {
            return AccountType.fromRepresentation(accountType)
                    .orElseThrow(this::newInvalidAccountException);
        }

        private IllegalStateException newInvalidAccountException() {
            final String message = "AccountType representation is not valid, validate() method should be called before.";
            return new IllegalStateException(message);
        }
    }
}
