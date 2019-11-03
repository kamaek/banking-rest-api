package com.banking.domain.payment;

import com.banking.domain.account.Account;
import com.banking.domain.account.AccountNotExists;
import com.banking.domain.money.Money;
import com.banking.domain.user.IndividualUser;
import com.banking.persistence.InMemoryRepository;
import com.banking.persistence.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaymentService should")
class PaymentServiceTest {

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
    @DisplayName("not allow accounts with different currencies for a domestic transfer")
    void notAllowAccountsWithDifferentCurrenciesForDomesticTransfer() {
        final Account euroAccount = createDebitAccount(batman, new Money("5.00", "EUR"));
        final Account dollarAccount = createDebitAccount(batman, new Money("5.00", "USD"));
        final Money oneEuro = new Money("1.00", "EUR");
        final AccountsOperateInDifferentCurrencies exception = assertThrows(
                AccountsOperateInDifferentCurrencies.class,
                () -> paymentService.transferMoneyDomestically(euroAccount.id(), dollarAccount.id(), oneEuro));
        final String expectedExceptionMessage = format("Account %s and %s operate in different currencies.",
                euroAccount.id(), dollarAccount.id());
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("not allow domestic money transfer if sender doesn't have enough funds")
    void notAllowDomesticMoneyTransferIfNotEnoughFunds() {
        final Account zeroBalanceAccount = createDebitAccount(batman, new Money("0.00", "EUR"));
        final Account oneHundredBalanceAccount = createDebitAccount(batman, new Money("100.00", "EUR"));
        final Money oneEuro = new Money("1.00", "EUR");
        final NotEnoughFunds exception = assertThrows(
                NotEnoughFunds.class,
                () -> paymentService.transferMoneyDomestically(zeroBalanceAccount.id(), oneHundredBalanceAccount.id(), oneEuro));
        final String expectedExceptionMessage = format("Account %s doesn't have enough funds to perform a payment.",
                zeroBalanceAccount.id());
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("transfer money domestically")
    void transferMoneyDomestically() throws PaymentException, AccountNotExists {
        final Account zeroBalanceAccount = createDebitAccount(batman, new Money("0.00", "EUR"));
        final Account oneHundredBalanceAccount = createDebitAccount(batman, new Money("100.00", "EUR"));
        final Money tenEuro = new Money("10.00", "EUR");
        final DomesticPayment payment = paymentService.transferMoneyDomestically(
                oneHundredBalanceAccount.id(), zeroBalanceAccount.id(), tenEuro);
        assertAccountHasBalance(zeroBalanceAccount.id(), new Money("10.00", "EUR"));
        assertAccountHasBalance(oneHundredBalanceAccount.id(), new Money("90.00", "EUR"));
        assertNotNull(payment.id());
        assertEquals(zeroBalanceAccount.id(), payment.recipientAccountId());
        assertEquals(oneHundredBalanceAccount.id(), payment.senderAccountId());
        assertEquals(tenEuro, payment.instructedAmount());
    }

    @Test
    @DisplayName("not allow domestic money transfer to the same account")
    void notAllowDomesticMoneyTransferAmongSameAccount() {
        final Account account = createDebitAccount(batman, new Money("100.00", "EUR"));
        final Money tenEuro = new Money("10.00", "EUR");
        assertThrows(
                CannotTransferMoneyWithinSameAccount.class,
                () -> paymentService.transferMoneyDomestically(account.id(), account.id(), tenEuro));
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
}