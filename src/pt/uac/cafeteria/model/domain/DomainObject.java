
package pt.uac.cafeteria.model.domain;

/**
 * Layer Supertype for the Domain Model, which enforces Identify Field.
 * <p>
 * <i>Layer Supertype</i> is a type that acts as the supertype for all
 * types in its layer.
 * <p>
 * The <i>Domain Model</i> is an object model of the domain that
 * incorporates both behavior and data.
 * <p>
 * <i>Identify Field</i> saves a unique id field in an object to maintain
 * identity between an in-memory object and its data persistence.
 *
 * @param <I> unique id domain object type.
 */
public interface DomainObject<I> {

    /** Gets the object's id. */
    public I getId();

    /** Sets a new id for the object. */
    public void setId(I id);
}
