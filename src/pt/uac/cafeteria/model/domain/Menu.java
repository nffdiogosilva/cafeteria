
package pt.uac.cafeteria.model.domain;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.Map;

/**
 * Meal Menu for a a lunch or dinner, in a given day.
 */
public class Menu implements DomainObject<Menu.Id> {

    /**
     * Unique identity for menus.
     * <p>
     * Composed of a date and meal time of day (lunch or dinner).
     */
    public static class Id {

        /** Day of the menu. */
        private final Calendar day;

        /** Time of the meal. */
        private final Meal.Time time;

        /** Creates a new Menu.Id instance. */
        public Id(Calendar day, Meal.Time time) {
            this.day = day;
            this.time = time;
        }

        /** Gets the date of the menu. */
        public Calendar getDay() {
            return (Calendar) this.day.clone();
        }

        /** Gets the time of day. */
        public Meal.Time getTime() {
            return this.time;
        }
    }

    /** Unique indentifier for this menu. */
    private Id id;

    private Map<Meal.Type, String> dishes = new EnumMap<Meal.Type, String>(Meal.Type.class);

    /** Soup name. */
    private String soup;

    /** The dessert name. */
    private String dessert;

    /**
     * Creates a new Menu instance, when Menu.Id is known.
     *
     * @param id the menu unique identifier.
     * @param meat the name of the meat dish.
     * @param fish the name of the fish dish.
     * @param vegetarian the name of the vegetarian dish.
     * @param soup the soup name.
     * @param dessert the dessert name.
     */
    public Menu(Id id, String meat, String fish, String vegetarian,
            String soup, String dessert) {

        this.id = id;
        dishes.put(Meal.Type.MEAT, meat);
        dishes.put(Meal.Type.FISH, fish);
        dishes.put(Meal.Type.VEGETARIAN, vegetarian);
        this.soup = soup;
        this.dessert = dessert;
    }

    /**
     * Creates a new Menu instance.
     *
     * @param day the day of the meal.
     * @param time the time of day for the meal.
     * @param meat the name of the meat dish.
     * @param fish the name of the fish dish.
     * @param vegetarian the name of the vegetarian dish.
     * @param soup the soup name.
     * @param dessert the dessert name.
     */
    public Menu(Calendar day, Meal.Time time, String meat, String fish,
            String vegetarian, String soup, String dessert) {

        this(new Id(day, time), meat, fish, vegetarian, soup, dessert);
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public void setId(Menu.Id id) {
        this.id = id;
    }

    /** Gets the name of the main course dish, based on type of meal. */
    public String getMainCourse(Meal.Type mealType) {
        return dishes.get(mealType);
    }

    /** Gets the soup name in the menu. */
    public String getSoup() {
        return soup;
    }

    /** Gets the dessert name in the menu. */
    public String getDessert() {
        return dessert;
    }

    /** Gets a <code>Meal</code> choice from an available main course type. */
    public Meal getMeal(Meal.Type mealType) {
        String mainCourse = getMainCourse(mealType);
        if (mainCourse == null) {
            return null;
        }
        return new Meal(id.getDay(), id.getTime(), mealType, soup, mainCourse, dessert);
    }
}
