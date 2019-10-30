package com.banking.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T extends Entity> implements Repository<T> {

    private final Map<String, T> entities;

    public InMemoryRepository() {
        this.entities = new ConcurrentHashMap<>();
    }

    @Override
    public void add(T entity) {
        entities.put(entity.id(), entity);
    }

    @Override
    public Collection<T> allEntities() {
        return entities.values();
    }

    @Override
    public Optional<T> entity(String id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public void delete(T entity) {
        entities.remove(entity.id());
    }
}
