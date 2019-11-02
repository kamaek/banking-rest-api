package com.banking.domain.account;

import com.banking.domain.money.Money;
import com.banking.domain.user.IndividualUser;
import com.banking.domain.user.UserNotExists;
import com.banking.persistence.Repository;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * A domain service performing business operations with accounts of users.
 */
public class AccountService {

    private final Repository<Account> accountRepository;
    private final Repository<IndividualUser> userRepository;

    public AccountService(Repository<Account> accountRepository, Repository<IndividualUser> userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    /**
     * Opens a debit account.
     *
     * <p>Account can have non-empty balance initially, if a user "provided" these money to a bank.
     *
     * @param accountIssuerId the user issued opening of the account (further owner of the account)
     * @param initialBalance  the initial balance of account
     * @return the opened account
     */
    public Account openDebitAccount(String accountIssuerId, Money initialBalance) throws UserNotExists {
        checkUserExists(accountIssuerId);
        final Account openedAccount = Account.debitAccount(accountIssuerId, initialBalance);
        accountRepository.add(openedAccount);
        return openedAccount;
    }

    /**
     * Obtains all accounts owned by the specified user.
     */
    public Collection<Account> accountsOwnedBy(String userId) throws UserNotExists {
        final IndividualUser user = checkUserExists(userId);
        return accountRepository.allEntities()
                .stream()
                .filter(account -> account.ownedBy(user))
                .collect(toList());
    }

    private IndividualUser checkUserExists(String userId) throws UserNotExists {
        final Optional<IndividualUser> user = userRepository.entity(userId);
        return user.orElseThrow(() -> new UserNotExists(userId));
    }
}
