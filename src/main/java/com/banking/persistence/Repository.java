package com.banking.persistence;

import java.util.Collection;
import java.util.Optional;

/**
 * A repository storing {@linkplain Entity entities}.
 */
public interface Repository<T extends Entity> {

    void add(T entity);

    Collection<T> allEntities();

    Optional<T> entity(String id);

    void delete(T entity);
}
