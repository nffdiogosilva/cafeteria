
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Helder Correia, Nuno Silva
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
 * A type of transaction used when a student buys a meal ticket.
 */
public class Ticket extends Transaction {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** The meal being bought. */
    private final Meal meal;

    /**
     * Creates a new Ticket instance.
     *
     * @param meal the meal being bought.
     * @param price the total price of the meal.
     */
    public Ticket(Meal meal, double price) {
        super(price);
        this.meal = meal;
    }

    /** Gets the meal bought. */
    public Meal getMeal() {
        return meal;
    }

    @Override
    public String toString() {
        return "\n    Ticket {"
            + "\n      date = " + getDate()
            + "\n      amount = " + getAmount()
            + "\n      meal = " + getMeal()
            + "\n    }";
    }
}
