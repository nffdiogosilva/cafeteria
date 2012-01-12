
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
    
    public String print() {
        return "" + date;
    }
}
