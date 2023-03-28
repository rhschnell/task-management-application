package server.features;

import java.util.List;

public interface RepositoryService<T, ID> {

    /**
     * Inserts given entity into repository
     * @param t entity to be inserted
     */
    void insert(T t);

    /**
     * Deletes entity with given ID from repository
     * @param id id of entity to be deleted
     */
    void delete(ID id);

    /**
     * Finds and returns entity with given ID from repository
     * @param id id of entity to be found and returned
     * @return entity corresponding to given id
     */
    T getByID(ID id);

    /**
     * Returns all entities from repository
     * @return list of all entities from repository
     */
    List<T> getAll();
}
