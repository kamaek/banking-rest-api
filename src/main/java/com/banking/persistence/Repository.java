package com.banking.persistence;

import java.util.Collection;

/**
 * A repository storing {@linkplain Entity entities}.
 */
public interface Repository<T extends Entity> {

    void add(T entity);

    Collection<T> allEntities();

    T entity(String id);

    void delete(T entity);
}
