package com.banking.persistence;

import java.util.UUID;

/**
 * An entity, which is stored in a repository.
 */
public abstract class Entity {

    private final String id;

    public Entity() {
        this.id = UUID.randomUUID().toString();
    }

    public String id() {
        return id;
    }
}
