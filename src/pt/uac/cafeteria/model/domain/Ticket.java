
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
}
