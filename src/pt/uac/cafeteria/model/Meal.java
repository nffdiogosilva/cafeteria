
package pt.uac.cafeteria.model;

import java.util.Date;

/**
 * Represents a meal
 */
public class Meal {

    /** Enumerates meal day time */
    enum Type {
        LUNCH  { @Override public String toString() { return "Dinner"; } },
        DINNER { @Override public String toString() { return "Lunch"; } };
    }

    /** Enumerates dish types */
    enum DishType {
        MEAT  { @Override public String toString() { return "Meat"; } },
        FISH { @Override public String toString() { return "Fish"; } },
        VEGETARIAN { @Override public String toString() { return "Vegetarian"; } };
    }

    /** Meal day time of service */
    private Type type;

    /** Service date of the meal */
    private Date date;

    /** Type of meal */
    private DishType dishType;

    /** Soup description */
    private String soup;

    /** Main course description */
    private String mainCourse;

    /** Dessert description */
    private String dessert;

    /**
     * Default Contructor
     *
     * @param type          meal day time of service
     * @param date          service date of the meal
     * @param dishType      Type of meal
     * @param Soup          soup description
     * @param mainCourse    main course description
     * @param dessert       dessert description
     */
    Meal(Type type, Date date, DishType dishType, String soup, String mainCourse, String dessert) {
        this.type = type;
        this.date = date;
        this.dishType = dishType;
        this.soup = soup;
        this.mainCourse = mainCourse;
        this.dessert = dessert;
    }

   /** Returns the day time of service of a meal */
    Type getType() {
        return type;
    }

    /**
     * Changes the day time of service of a meal
     *
     * @param type      the type that will be defined
     */
    void setType(Type type) {
        this.type = type;
    }

    /** Returns the date of a meal */
    public Date getDate() {
        return date;
    }

    /**
     * Changes the date of a meal
     *
     * @param date      the date that will be defined
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /** Returns the dish type of a meal */
    DishType getDishType() {
        return dishType;
    }

    /**
     * Changes the dish type of a meal
     *
     * @param dishType      the dish type that will be defined
     */
    void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    /** Returns the soup description of a meal */
    public String getSoup() {
        return soup;
    }

    /**
     * Changes the soup description of a meal
     *
     * @param Soup      the soup description that will be defined
     */
    public void setSoup(String soup) {
        this.soup = soup;
    }

    /** Returns the main course description of a meal */
    public String getMainCourse() {
        return mainCourse;
    }

    /**
     * Changes the main course description of a meal
     *
     * @param mainCourse    the main course description that will be defined
     */
    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    /** Returns the dessert description of a meal */
    public String getDessert() {
        return dessert;
    }

    /**
     * Changes the dessert description of a meal
     *
     * @param dessert       the dessert description that will be defined
     */
    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    /** Returns a string that describes a meal */
    @Override
    public String toString() {
        return "\n:Type " + this.type +
                "\nDate: " + this.date +
                "\nDish type: " + this.dishType +
                "\nSoup: " + this.soup +
                "\nMain course: " + this.mainCourse +
                "\nDessert: " + this.dessert;
    }
}