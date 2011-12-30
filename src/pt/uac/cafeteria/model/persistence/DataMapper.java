
package pt.uac.cafeteria.model.persistence;

/**
 * A layer of mappers that moves data between objects and their data
 * persistence, while keeping them independent of each other and the
 * mapper itself.
 * <p>
 * Objects and relational databases (or non binary files) have different
 * mechanisms for structuring data. Many parts of an object, such as
 * collections and inheritance, aren't present in relational databases,
 * for instance.
 * <p>
 * The <i>Data Mapper</i> is a layer of software that separates in-memory
 * objects from their data storage. Its responsibility is to transfer
 * data between the two and also to isolate them from each other. With
 * <i>Data Mapper</i> the in-memory objects needn't know even that there's
 * a database present, or any XML document schema structure knowledge. Since
 * it's a form of <i>Mapper</i> (an object that sets up a communication
 * between two independent objects), <i>Data Mapper</i> itself is even
 * unknown to the domain layer.
 * <p>
 * Data persistence is based on basic CRUD operations (Create, Read, Update,
 * Delete). This interfaces provides a contract for CRUD that is independent
 * of data storage. Changing an object's storage from a file to a database,
 * shouldn't require changes to the domain layer, only to the mapper itself.
 *
 * @param <T> the domain object type.
 * @param <I> the domain object id type.
 */
public interface DataMapper<T, I> {

    /**
     * Saves a new domain object to its data storage.
     *
     * @param o the domain object.
     * @return the domain object id, if successful.
     */
    public I insert(T o);

    /**
     * Finds a domain object based on its id.
     *
     * @param id the domain object id.
     * @return The domain object if found, or null otherwise.
     */
    public T find(I id);

    /**
     * Updates a changed in-memory domain object in its storage.
     *
     * @param o the existing domain object (with id not null).
     * @return true if update, false otherwise.
     */
    public boolean update(T o);

    /**
     * Deletes an in-memory domain object from its storage.
     *
     * @param o the existing domain object (with id not null).
     * @return true if deleted, false otherwise.
     */
    public boolean delete(T o);
}
