
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Helder Correia
 * 
 * This file is part of Cafeteria.
 * 
 * Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cafeteria.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.uac.cafeteria.model.persistence.abstracts;

/**
 * Contract for objects that need to manage loaded instances.
 *
 * @param <T> the object type.
 * @param <I> the object id type.
 */
public interface Loader<T, I> {

    /**
     * Adds a new object to the loaded map, or replaces an existing one.
     *
     * @param id object id.
     * @param o object to be added.
     * @return The previous object associated with id, or null otherwise.
     */
    public T register(I id, T o);

    /**
     * Checks if an object is in the loaded map.
     *
     * @param id object id.
     * @return true if the id is found in the loaded map; false otherwise.
     */
    public boolean isLoaded(I id);

    /**
     * Gets an object from the loaded map.
     *
     * @param id object id.
     * @return The object if the id is found in the loaded map; null otherwise.
     */
    public T retrieve(I id);

    /**
     * Removes an object from the loaded map if it is present.
     *
     * @param id object id.
     * @return The object associated to the id, or null if no object found.
     */
    public T remove(I id);
}
