package com.banking.domain.user;

import com.banking.persistence.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * A domain service performing business operations with users of a bank.
 */
public class UserService {

    private final Repository<IndividualUser> userRepository;

    public UserService(Repository<IndividualUser> userRepository) {
        this.userRepository = userRepository;
    }

    public IndividualUser signUpIndividual(String firstName, String lastName) {
        IndividualUser individualUser = new IndividualUser(firstName, lastName);
        userRepository.add(individualUser);
        return individualUser;
    }

    public Collection<IndividualUser> allIndividuals() {
        return userRepository.allEntities();
    }

    public Optional<IndividualUser> individual(String id) {
        return userRepository.entity(id);
    }
}
