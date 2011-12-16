
package pt.uac.cafeteria.model.domain;

import java.util.Calendar;

/**
 *
 * Represents a transaction
 *
 */
public abstract class Transaction {

    /** The date of the occurrence of the transaction */
    private Calendar date;

    /** The amount made in the transaction */
    private double amount;

    /** Returns the date of the transaction */
    public Calendar getDate() {
        return date;
    }

    /**
     * Changes the date
     *
     * @param date  the date that will be defined
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /** Returns the amount of the transaction */
    public double getAmount() {
        return amount;
    }

    /**
     * Changes the amount
     *
     * @param amount    the amount that will be defined
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
