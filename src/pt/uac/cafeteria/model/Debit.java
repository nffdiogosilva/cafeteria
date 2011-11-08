
package pt.uac.cafeteria.model;

import java.util.Calendar;

/**
 *
 * Represents a debit transaction
 *
 */

public class Debit extends Transaction {

    /** Enumerated type with the type of a meal */
    public enum MealTime {
        LUNCH  { @Override public String toString() { return "Almoco"; } },
        DINNER { @Override public String toString() { return "Jantar"; } };
    }
    /** The date of the meal */
    private Calendar mealDate;

    /** The type of the meal */
    private MealTime mealTime;

    /**
     * Default Constructor
     *
     * @param mealDate  the date that is defined as the date of the meal
     * @param mealType  the type of the meal that is defined
     */
    public Debit(Calendar mealDate, MealTime mealTime) {
        super.setDate(Calendar.getInstance());
        this.mealDate = mealDate;
        this.mealTime = mealTime;
    }

    /** Returns the date of the meal */
    public Calendar getMealDate() {
        return mealDate;
    }

    /** Returns the type of the meal */
    public MealTime getMealTime() {
        return mealTime;
    }
}
