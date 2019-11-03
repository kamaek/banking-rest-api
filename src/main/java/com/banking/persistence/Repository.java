package com.banking.persistence;

import java.util.Collection;
import java.util.Optional;

/**
 * A repository storing {@linkplain Entity entities} of a specific type.
 */
public interface Repository<T extends Entity> {

    /**
     * Creates or updates the entity.
     *
     * @param entity the entity to add, should have already initialized ID
     */
    void add(T entity);

    Collection<T> allEntities();

    Optional<T> entity(String id);

    void delete(T entity);
}
