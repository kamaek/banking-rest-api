package com.banking.domain.payment;

import com.banking.domain.account.AccountNotExists;
import com.banking.domain.money.Money;
import com.banking.rest.ValidationMessage;
import com.banking.rest.route.PostBody;
import com.banking.rest.route.PostRoute;
import com.banking.rest.route.UnprocessableEntityException;
import com.google.gson.Gson;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class PostDomesticPayment extends PostRoute<DomesticPayment, PostDomesticPayment.Body> {

    private final PaymentService paymentService;

    public PostDomesticPayment(PaymentService paymentService, Gson gson) {
        super(gson);
        this.paymentService = paymentService;
    }

    @Override
    protected DomesticPayment create(Body body) throws UnprocessableEntityException {
        try {
            final String originId = body.senderAccountId;
            final String destinationId = body.recipientAccountId;
            return paymentService.transferMoneyDomestically(originId, destinationId, body.instructedAmount);
        } catch (PaymentException | AccountNotExists e) {
            throw new UnprocessableEntityException(e);
        }
    }

    @Override
    protected Body extractPayload(Request request, Gson gson) {
        final String rawBody = request.body();
        return gson.fromJson(rawBody, Body.class);
    }

    static class Body implements PostBody {

        private final String senderAccountId;
        private final String recipientAccountId;
        private final Money instructedAmount;

        private Body(String senderAccountId, String recipientAccountId, Money instructedAmount) {
            this.senderAccountId = senderAccountId;
            this.recipientAccountId = recipientAccountId;
            this.instructedAmount = instructedAmount;
        }

        @Override
        public List<ValidationMessage> validate() {
            final List<ValidationMessage> messages = new ArrayList<>();
            validateNotNullOrBlank(senderAccountId, "Please specify 'senderAccountId'.").ifPresent(messages::add);
            validateNotNullOrBlank(recipientAccountId, "Please specify 'recipientAccountId'.").ifPresent(messages::add);
            validateNotNul(instructedAmount, "Please specify 'instructedAmount'.").ifPresent(messages::add);
            return messages;
        }
    }
}
