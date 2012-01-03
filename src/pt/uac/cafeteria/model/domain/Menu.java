
package pt.uac.cafeteria.model.domain;

import java.util.Calendar;

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

    /** Name of meat dish. */
    private String meat;

    /** Name of fish dish. */
    private String fish;

    /** Name of vegetarian dish. */
    private String veggie;

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
        this.meat = meat;
        this.fish = fish;
        this.veggie = vegetarian;
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

    /** Gets the meal time of day for the menu. */
    public Meal.Time getTime() {
        return id.getTime();
    }

    /** Gets the meat dish name in the menu. */
    public String getMeat() {
        return meat;
    }

    /** Gets the fish dish name in the menu. */
    public String getFish() {
        return fish;
    }

    /** Gets the vegetarian dish name in the menu. */
    public String getVegetarian() {
        return veggie;
    }

    /** Gets the soup name in the menu. */
    public String getSoup() {
        return soup;
    }

    /** Gets the dessert name in the menu. */
    public String getDessert() {
        return dessert;
    }

    /** Returns a meal with meat as main course, or null if there isn't one. */
    public Meal getMeatMeal() {
        if (meat == null || meat.isEmpty()) {
            return null;
        }
        return new Meal(id.getDay(), id.getTime(), Meal.Type.MEAT, soup, meat, dessert);
    }

    /** Returns a meal with fish as main course, or null if there isn't one. */
    public Meal getFishMeal() {
        if (fish == null || fish.isEmpty()) {
            return null;
        }
        return new Meal(id.getDay(), id.getTime(), Meal.Type.FISH, soup, fish, dessert);
    }

    /** Returns a meal with vegetarian as main course, or null if there isn't one. */
    public Meal getVegetarianMeal() {
        if (veggie == null || veggie.isEmpty()) {
            return null;
        }
        return new Meal(id.getDay(), id.getTime(), Meal.Type.VEGETARIAN, soup, veggie, dessert);
    }

    /** Gets the name of the main course dish, based on type of main course. */
    public String getMainCourse(Meal.Type type) {
        switch (type) {
            case MEAT : return this.meat;
            case FISH : return this.fish;
            case VEGETARIAN : return this.veggie;
            default :
                throw new IllegalArgumentException("Tipo de prato desconhecido.");
        }
    }
}
