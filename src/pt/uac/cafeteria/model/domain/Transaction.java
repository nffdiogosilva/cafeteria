
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

import java.util.Date;

/**
 * Account transaction superclass.
 * <p>
 * At least two types of transaction should be implemented:
 * a payment to add to the account credit, and a ticket when a meal is bought.
 *
 */
public abstract class Transaction implements java.io.Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** The date the transaction was made. */
    private final Date date;

    /** The amount being transacted. */
    private final double amount;

    /**
     * Base default constructor that sets the date for the current moment,
     * and amount being transacted.
     *
     * @param amount amount being transacted.
     */
    protected Transaction(double amount) {
        this.date = new Date();
        this.amount = Math.abs(amount);
    }

    /** Returns the date of the transaction. */
    public Date getDate() {
        return date;
    }

    /** Returns the amount transacted. */
    public double getAmount() {
        return amount;
    }
}
