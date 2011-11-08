
package pt.uac.cafeteria.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class MealTest {

    @Test
    public void canCreateMeal() {
        Calendar mealDay = new GregorianCalendar(2011, 11, 11);

        Meal meat = Meal.createMeatCourse(mealDay, Meal.Time.LUNCH, "Beans", "Pork", "Cake");
        Meal fish = Meal.createFishCourse(mealDay, Meal.Time.LUNCH, "Beans", "Tuna", "Cake");
        Meal veggie = Meal.createVeggieCourse(mealDay, Meal.Time.LUNCH, "Beans", "Tofu", "Cake");

        assertEquals(Meal.Type.MEAT, meat.getType());
        assertEquals(Meal.Type.FISH, fish.getType());
        assertEquals(Meal.Type.VEGETARIAN, veggie.getType());
    }
}
