
package pt.uac.cafeteria.model;

import java.util.List;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class MealMenuTest {

    private final Calendar day = new GregorianCalendar(2011, 11, 11);
    private final Meal.Time time = Meal.Time.DINNER;
    
    private final String soup = "Vegetables";
    private final String desert = "Cake";

    private MealMenu createMenu() {
        return MealMenu.Builder(day, time)
                .setMeatCourse("Pork")
                .setFishCourse("Tuna")
                .setVeggieCourse("Soy")
                .setSoupAndDesert(soup, desert)
                .build();
    }

    @Test
    public void canCreateMenu() {
        MealMenu menu = createMenu();

        Meal meat_expected = Meal.createMeatMeal(day, time, soup, "Pork", desert);
        Meal meat_actual = menu.getMeatMeal();
        assertEquals("menu must generate correct meat meal", meat_expected, meat_actual);

        Meal fish_expected = Meal.createFishMeal(day, time, soup, "Tuna", desert);
        Meal fish_actual = menu.getFishMeal();
        assertEquals("menu must generate correct fish meal", fish_expected, fish_actual);

        Meal veggie_expected = Meal.createVeggieMeal(day, time, soup, "Soy", desert);
        Meal veggie_actual = menu.getVeggieMeal();
        assertEquals("menu must generate correct veggie meal", veggie_expected, veggie_actual);
    }

    @Test(expected=IllegalArgumentException.class)
    public void mustHaveMainCourse() {
        MealMenu.Builder(day, time).
                .setSoupAndDesert(soup, desert)
                .build();
    }

    @Test(expected=IllegalArgumentException.class)
    public void mustHaveSoupAndDesert() {
        MealMenu.Builder(day, time)
                .setMeatCourse("Pork")
                .build();
    }

    @Test
    public void canGetChoices() {
        MealMenu menu = createMenu();

        List<String[]> expected = Arrays.asList(new String[][] {
            {Meal.Type.MEAT.toString(), "Pork"},
            {Meal.Type.FISH.toString(), "Tuna"},
            {Meal.Type.VEGETARIAN.toString(), "Soy"}
        });

        assertArrayEquals(expected, menu.getMainCourses());
    }
}
