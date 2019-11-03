package com.banking.domain.payment;

import com.banking.domain.account.Account;
import com.banking.domain.money.Money;
import com.banking.domain.user.IndividualUser;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PaymentService should handle concurrency and")
class PaymentServiceConcurrencyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceConcurrencyTest.class);

    private final IndividualUser batman = new IndividualUser("Mr.", "Batman");

    private final Repository<IndividualUser> userRepository = new InMemoryRepository<>();
    private final Repository<Account> accountRepository = new InMemoryRepository<>();
    private final Repository<DomesticPayment> paymentRepository = new InMemoryRepository<>();
    private final PaymentService paymentService = new PaymentService(accountRepository, paymentRepository);

    @BeforeEach
    void setUp() {
        userRepository.add(batman);
    }

    @Test
    @DisplayName("prevent race condition for domestic money transfers")
    void preventRaceConditionForDomesticMoneyTransfer() throws ExecutionException, InterruptedException {
        final Money tenThousand = new Money("10000.00", "EUR");
        final Money tenEuro = new Money("1.00", "EUR");
        final Account firstAccount = createDebitAccount(batman, tenThousand);
        final Account secondAccount = createDebitAccount(batman, tenThousand);

        runConcurrently(1000,
                () -> paymentService.transferMoneyDomestically(firstAccount.id(), secondAccount.id(), tenEuro));
        assertAccountHasBalance(firstAccount.id(), new Money("9000.00", "EUR"));
        assertAccountHasBalance(secondAccount.id(), new Money("11000.00", "EUR"));
    }

    private Account createDebitAccount(IndividualUser owner, Money initialBalance) {
        final Account account = Account.debitAccount(owner.id(), initialBalance);
        accountRepository.add(account);
        return account;
    }

    private void assertAccountHasBalance(String accountId, Money expectedBalance) {
        final Optional<Account> account = accountRepository.entity(accountId);
        assertTrue(account.isPresent());
        assertEquals(expectedBalance, account.get().balance());
    }

    /**
     * Executes specified {@link Callable}, one execution per thread.
     *
     * <p>{@link Callable} is executed almost simultaneously within all threads,
     * this is achieved by employing {@link CountDownLatch}.
     *
     * <p>Ensures that there was at least one concurrent modifications and awaits all threads to finish work.
     */
    private static <T> void runConcurrently(int numberOfThreads, Callable<T> callable) throws ExecutionException, InterruptedException {
        final ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean running = new AtomicBoolean();
        final AtomicInteger overlaps = new AtomicInteger();
        Collection<Future<Void>> futures = new ArrayList<>(numberOfThreads);
        for (int t = 0; t < numberOfThreads; ++t) {
            futures.add(
                    service.submit(
                            () -> {
                                latch.await();
                                if (running.get()) {
                                    overlaps.incrementAndGet();
                                }
                                running.set(true);
                                callable.call();
                                running.set(false);
                                return null;
                            }
                    )
            );
        }
        latch.countDown();
        awaitAllFutures(futures);
        LOGGER.info("Concurrent access was performed {} times.", overlaps.get());
        assertTrue(overlaps.get() > 0, "There were no concurrent modifications.");
    }

    private static void awaitAllFutures(Collection<Future<Void>> futures) throws ExecutionException, InterruptedException {
        for (Future future : futures) {
            future.get();
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }
}