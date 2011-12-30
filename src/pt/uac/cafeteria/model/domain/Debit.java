
package pt.uac.cafeteria.model.domain;

import java.util.Calendar;

/**
 * Represents a debit transaction
 */
public class Debit extends Transaction {

    /** The date of the meal */
    private Calendar mealDate;

    /** The type of the meal */
    private Meal.Time mealTime;

    /**
     * Default Constructor
     *
     * @param mealDate  the date that is defined as the date of the meal
     * @param mealType  the type of the meal that is defined
     */
    public Debit(Calendar mealDate, Meal.Time mealTime) {
        super.setDate(Calendar.getInstance());
        this.mealDate = mealDate;
        this.mealTime = mealTime;
    }

    /** Returns the date of the meal */
    public Calendar getMealDate() {
        return mealDate;
    }

    /** Returns the type of the meal */
    public Meal.Time getMealTime() {
        return mealTime;
    }
}
