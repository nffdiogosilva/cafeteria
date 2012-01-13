
package pt.uac.cafeteria.model.domain;

import java.util.EnumMap;
import java.util.Map;

/**
 * Meal menu for a given day.
 * <p>
 * The menu has a collection of <code>Meal</code> objects, grouped by
 * the time of day the meals are served. At each meal time, only one meal
 * per type of meal can be defined.
 */
public class Menu implements DomainObject<Day> {

    /** Unique indentifier for this menu, which is the day. */
    private Day id;

    /** Map with meals for each meal type and meal time. */
    private Map<Meal.Time, Map<Meal.Type, Meal>> menu;

    /**
     * Creates a new <code>Menu</code> instance.
     *
     * @param id the day of the menu.
     */
    public Menu(Day id) {
        this.id = id;
        menu = new EnumMap<Meal.Time, Map<Meal.Type, Meal>>(Meal.Time.class);
    }

    /**
     * Adds an array of meals to the menu.
     *
     * @param meals the array of meals to add.
     */
    public void addMeals(Meal... meals) {
        for (Meal meal : meals) {
            addMeal(meal);
        }
    }

    /**
     * Adds a meal to the menu.
     *
     * @param meal the meal to add.
     */
    public void addMeal(Meal meal) {
        if (meal != null && meal.getDay().equals(id)) {

            Map<Meal.Type, Meal> submenu = menu.get(meal.getTime());
            if (submenu == null) {
                submenu = new EnumMap<Meal.Type, Meal>(Meal.Type.class);
            }
            submenu.put(meal.getType(), meal);
            menu.put(meal.getTime(), submenu);
        }
    }

    @Override
    public Day getId() {
        return id;
    }

    @Override
    public void setId(Day id) {
        this.id = id;
    }

    /** Gets all meals in the menu. */
    public Map<Meal.Time, Map<Meal.Type, Meal>> getMeals() {
        return menu;
    }

    /**
     * Gets the meals available for a given time.
     *
     * @param mealTime the time of the day.
     * @return the sub-menu of the given time, or null if there isn't one.
     */
    public Map<Meal.Type, Meal> getMeals(Meal.Time mealTime) {
        return menu.get(mealTime);
    }

    /**
     * Gets a meal choice from the day menu.
     *
     * @param mealTime time of the day.
     * @param mealType main course dish type.
     * @return the meal for the given time and type, or null if there isn't one.
     */
    public Meal getMeal(Meal.Time mealTime, Meal.Type mealType) {
        Map<Meal.Type, Meal> submenu = menu.get(mealTime);

        if (submenu == null) {
            return null;
        }

        return submenu.get(mealType);
    }

    /**
     * Removes all meals for a given meal time.
     *
     * @param mealTime the time of day for the meals to be removed.
     */
    public void removeMeals(Meal.Time mealTime) {
        menu.remove(mealTime);
    }

    /** Checks if the menu has no meals. */
    public boolean isEmpty() {
        return menu.isEmpty();
    }

    /** Checks if there are no meals for a give time. */
    public boolean isEmpty(Meal.Time mealTime) {
        return getMeals(mealTime) == null || getMeals(mealTime).isEmpty();
    }
}
