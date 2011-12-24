
package pt.uac.cafeteria.model;

import java.util.Calendar;
import pt.uac.cafeteria.model.persistence.AbstractProperties;

/**
 * Application specific implementation of AbstractProperties.
 *
 * Implemented as a Singleton (only one instance allowed), used as a
 * global configuration object with a properties list, for the application.
 *
 * Methods are specifically defined for setting and getting required
 * application configuration fields, with their correct types. Meanwhile,
 * it is allowed to use it as a sort of Registry for other configuration
 * variables.
 */
public class Config extends AbstractProperties {

    /** Relative path and filename to the properties file. */
    protected static final String PATH = "resources/config.txt";

    /** Reference to the only instance of Config. */
    private static Config instance;

    /** Private constructor does not allow creation of new instances (Singleton). */
    private Config() { }

    /**
     * Gets the instance of Config.
     *
     * Implements the Singleton design pattern.
     *
     * @return Config object, with file path already set, based on default.
     */
    public static Config getInstance() {
        if (instance != null) {
            return instance;
        }
        Config config = new Config();
        config.setFilePath(PATH);
        return config;
    }

    @Override
    protected void setDefaults() {
        setYearToCurrent();
        setAccountSeed(1000);
        setMealPrice(2.40);
        setScholorshipDiscount(0.80);
        setSameDayTicket(0.45);
        setDBName("uacbd");
        setDBCredentials("root", "root");
    }

    /** Sets `year` property to current year. */
    public void setYearToCurrent() {
        setYear(Calendar.getInstance().get(Calendar.YEAR));
    }

    /**
     * Sets required `year` property.
     *
     * @param year int value for the year to set.
     */
    public void setYear(int year) {
        setInt("year", year);
    }

    /** Gets required `year` value. */
    public int getYear() {
        return getInt("year");
    }

    /**
     * Sets required `account_seed` property.
     *
     * This value is meant to be incremented and concatenated to the `year`
     * property to produced an unique Student ID number (same as Account ID).
     *
     * @param i int value for the `account_seed` property to set.
     */
    public void setAccountSeed(int i) {
        setInt("account_seed", i);
    }

    /** Gets required `account_seed` property.
     *
     * @return last increment for the Account or Student ID seed.
     */
    public int getAccountSeed() {
        return getInt("account_seed");
    }

    /**
     * Sets required `meal_price` property.
     *
     * This is the currency amount for the meal, without discounts or penalties.
     *
     * @param d price for each meal.
     */
    public void setMealPrice(double d) {
        setDouble("meal_price", d);
    }

    /**
     * Gets required `meal_price` property.
     *
     * @return price for each meal.
     */
    public double getMealPrice() {
        return getDouble("meal_price");
    }

    /**
     * Sets required `scholarship_discount` property.
     *
     * Scholarship students have a discount on each meal. This property
     * sets the currency amount for that discount.
     *
     * @param d amount to be subtracted from total meal price.
     */
    public void setScholorshipDiscount(double d) {
        setDouble("scholorship_discount", d);
    }

    /**
     * Gets required `scholarship_discount` property.
     *
     * @return amount to be subtracted from total meal price.
     */
    public double getScholorshipDiscount() {
        return getDouble("scholorship_discount");
    }

    /**
     * Sets required `same_day_ticket` property.
     *
     * Students that buy a ticket on the same day of the meal, get a
     * higher price. This property sets the amount to be added to the
     * total of the meal price.
     *
     * @param d amount to be added to the total meal price.
     */
    public void setSameDayTicket(double d) {
        setDouble("same_day_ticket", d);
    }

    /**
     * Gets required `same_day_ticket` property.
     *
     * @return amount to be added to the total meal price.
     */
    public double getSameDayTicket() {
        return getDouble("same_day_ticket");
    }

    /**
     * Sets required `db.name` property.
     *
     * @param dbname name of the database to use.
     */
    public void setDBName(String dbname) {
        setString("db.name", dbname);
    }

    /**
     * Gets required `db.name` property.
     *
     * @return name of the database to use.
     */
    public String getDBName() {
        return getString("db.name");
    }

    /**
     * Sets required `db.user` and `db.pass` properties.
     *
     * Credentials used to authenticate to the database server.
     *
     * @param user the username.
     * @param pass the password.
     */
    public void setDBCredentials(String user, String pass) {
        setString("db.user", user);
        setString("db.pass", pass);
    }

    /** Gets database username for client access. */
    public String getDBUsername() {
        return getString("db.user");
    }

    /** Gets database password for client access. */
    public String getDBPassword() {
        return getString("db.pass");
    }
}
