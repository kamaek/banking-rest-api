package com.banking;

import com.banking.domain.account.*;
import com.banking.domain.money.Money;
import com.banking.domain.money.MoneyTypeAdapter;
import com.banking.domain.payment.DomesticPayment;
import com.banking.domain.payment.PaymentService;
import com.banking.domain.payment.PostDomesticPayment;
import com.banking.domain.user.*;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import com.banking.rest.ContentType;
import com.banking.rest.ErrorResponse;
import com.banking.rest.ResponseCode;
import com.banking.rest.ValidationException;
import com.banking.rest.route.UnprocessableEntityException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Spark;

public class WebServer {

    public static final int DEFAULT_PORT = 4567;

    private static final String GET_BY_ID_PREFIX = "/:id";

    public static void main(String[] args) {
        new WebServer().start(DEFAULT_PORT);
    }

    public void start(int port) {
        final Gson gson = newConfiguredGson();
        final Repository<IndividualUser> userRepository = new InMemoryRepository<>();
        final Repository<Account> accountRepository = new InMemoryRepository<>();
        final Repository<DomesticPayment> paymentRepository = new InMemoryRepository<>();
        final UserService userService = new UserService(userRepository);
        final AccountService accountService = new AccountService(accountRepository, userRepository);
        final PaymentService paymentService = new PaymentService(accountRepository, paymentRepository);

        Spark.port(port);
        initUserRoutes(gson, userService);
        initAccountRoutes(gson, accountService);
        initPaymentRoutes(gson, paymentService);
        addExceptionsHandlers(gson);
        Spark.awaitInitialization();
    }

    private Gson newConfiguredGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AccountType.class, new AccountTypeAdapter())
                .registerTypeAdapter(Money.class, new MoneyTypeAdapter())
                .create();
    }

    private void initUserRoutes(Gson gson, UserService userService) {
        Spark.path("/users", () -> {
            Spark.post("", new PostUser(userService, gson));
            Spark.get("", new GetAllUsers(userService, gson));
            Spark.get(GET_BY_ID_PREFIX, new GetUserById(userService, gson));
        });
    }

    private void initAccountRoutes(Gson gson, AccountService accountService) {
        Spark.path("/users/:userId/accounts", () -> {
            Spark.post("", new PostAccount(accountService, gson));
            Spark.get("", new GetUserAccounts(accountService, gson));
            Spark.get(GET_BY_ID_PREFIX, new GetUserAccountById(accountService, gson));
        });
    }

    private void initPaymentRoutes(Gson gson, PaymentService paymentService) {
        Spark.path("/domestic-payments", () -> {
            Spark.post("", new PostDomesticPayment(paymentService, gson));
        });
    }

    private void addExceptionsHandlers(Gson gson) {
        Spark.exception(ValidationException.class, (e, request, response) -> {
            final ErrorResponse responseBody = ErrorResponse.newBuilder().addValidationMessages(e).build();
            response.type(ContentType.JSON);
            response.status(ResponseCode.UNPROCESSABLE_ENTITY);
            response.body(gson.toJson(responseBody));
        });
        Spark.exception(UnprocessableEntityException.class, (e, request, response) -> {
            final String detail = e.getCause().getMessage();
            final ErrorResponse responseBody = ErrorResponse.newBuilder().addDetail(detail).build();
            response.type(ContentType.JSON);
            response.status(ResponseCode.UNPROCESSABLE_ENTITY);
            response.body(gson.toJson(responseBody));
        });
    }
}
