
package pt.uac.cafeteria.model.persistence;

interface DataMapper<T, K> {
    public K insert(T o);
    public boolean delete(T o);
    public T find(K key);
}
