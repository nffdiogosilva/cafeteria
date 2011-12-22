
package pt.uac.cafeteria.model.persistence;

public interface DataMapper<T, I> {
    public I insert(T o);
    public T find(I id);
    public int update(T o);
    public boolean delete(T o);
}
