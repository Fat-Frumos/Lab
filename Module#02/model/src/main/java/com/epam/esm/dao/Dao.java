package com.epam.esm.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * The {@code Dao} interface provides methods for accessing
 * and manipulating data from a data source.
 * <p>
 * It is a generic interface that defines basic CRUD operations
 * for working with entities that implement the Serializable interface.
 *
 * @param <T> the type of entities that this interface works with.
 */
public interface Dao<T extends Serializable> {
    /**
     * Retrieves all entities of type {@code T}
     * from the data source.
     *
     * @return a list of all entities of type {@code T}.
     */
    List<T> getAll();

    /**
     * Retrieves an entity of type {@code T}
     * with the specified ID from the data source.
     *
     * @param id the ID of the entity to retrieve.
     * @return the entity with the specified ID
     * or {@code null} if no such entity exists.
     */
    Optional<T> getById(Long id);

    /**
     * Retrieves an entity of type {@code T} with the specified name from the data source.
     *
     * @param name the name of the entity to retrieve.
     * @return the entity with the specified name or {@code null} if no such entity exists.
     */
    Optional<T> getByName(String name);

    /**
     * Saves the specified entity of type {@code T} to the data source.
     *
     * @param entity the entity to save.
     * @return the ID of the saved entity.
     */
    Long save(T entity);

    /**
     * Deletes the entity of type {@code T} with the specified ID from the data source.
     *
     * @param id the ID of the entity to delete.
     */
    void delete(Long id);
}
