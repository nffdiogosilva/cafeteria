
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
