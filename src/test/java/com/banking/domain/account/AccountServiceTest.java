package com.banking.domain.account;

import com.banking.domain.money.Currencies;
import com.banking.domain.money.Money;
import com.banking.domain.user.IndividualUser;
import com.banking.domain.user.UserNotExists;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AccountService should")
class AccountServiceTest {

    private final IndividualUser batman = new IndividualUser("Mr.", "Batman");
    private final IndividualUser joker = new IndividualUser("Mr.", "Joker");

    private final Repository<IndividualUser> userRepository = new InMemoryRepository<>();
    private final Repository<Account> accountRepository = new InMemoryRepository<>();
    private final AccountService accountService = new AccountService(accountRepository, userRepository);

    @Test
    @DisplayName("not open an account if issuer doesn't exist")
    void notOpenAccountIfUserNotExists() {
        final String nonExistingIssuer = UUID.randomUUID().toString();
        final UserNotExists exception = assertThrows(
                UserNotExists.class,
                () -> accountService.openDebitAccount(nonExistingIssuer, fiveEuro()));
        assertEquals(nonExistingIssuer, exception.userId());
    }

    @Test
    @DisplayName("open a debit account with the initial balance")
    void openDebitAccountWithInitialBalance() throws UserNotExists {
        createUser(batman);
        final Money initialBalance = fiveEuro();
        final Account openedAccount = accountService.openDebitAccount(batman.id(), initialBalance);
        assertTrue(openedAccount.ownedBy(batman));
        assertTrue(openedAccount.isDebitAccount());
        assertEquals(initialBalance, openedAccount.balance());
    }

    @Test
    @DisplayName("not list accounts if user not exists")
    void notListAccountsIfUserNotExists() {
        final String nonExistingUser = UUID.randomUUID().toString();
        final UserNotExists exception = assertThrows(
                UserNotExists.class,
                () -> accountService.accountsOwnedBy(nonExistingUser));
        assertEquals(nonExistingUser, exception.userId());
    }

    @Test
    @DisplayName("list zero accounts if a user has no accounts")
    void listZeroAccountsIfUserHasNoAccounts() throws UserNotExists {
        createUser(batman);
        final Collection<Account> batmanAccounts = accountService.accountsOwnedBy(batman.id());
        assertTrue(batmanAccounts.isEmpty());
    }

    @Test
    @DisplayName("list accounts opened by a user")
    void listAccountsOpenedByUser() throws UserNotExists {
        createUser(batman);
        createUser(joker);
        final Money initialBalance = fiveEuro();
        final Account openedBatmanAccount = accountService.openDebitAccount(batman.id(), initialBalance);
        final Collection<Account> batmanAccounts = accountService.accountsOwnedBy(batman.id());
        final Collection<Account> jokerAccounts = accountService.accountsOwnedBy(joker.id());
        assertTrue(jokerAccounts.isEmpty());
        assertEquals(1, batmanAccounts.size());
        assertEquals(openedBatmanAccount.id(), batmanAccounts.iterator().next().id());
    }

    private void createUser(IndividualUser user) {
        userRepository.add(user);
    }

    private static Money fiveEuro() {
        return new Money(new BigDecimal(5), Currencies.euro());
    }
}