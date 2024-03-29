
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Helder Correia
 * 
 * This file is part of Cafeteria.
 * 
 * Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cafeteria.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.uac.cafeteria.model;

import java.util.Calendar;
import pt.uac.cafeteria.model.persistence.abstracts.AbstractProperties;

/**
 * Application specific implementation of AbstractProperties.
 * <p>
 * Properties are specifically defined for required application
 * configuration fields, at the same time as it is allowed to use
 * it as a sort of Registry for other configuration variables.
 */
public class Config extends AbstractProperties {

    /** Default path to the properties file. */
    protected static final String DEFAULT_PATH = "data/config.txt";

    /** Year value set as a prefix for the id of new student accounts. */
    public static final String YEAR = "year";

    /**
     * This value is meant to be incremented and concatenated to the YEAR
     * property to produce a unique Student ID number (same as Account ID).
     */
    public static final String ACCOUNT_SEED = "account_seed";

    /** Currency amount for the meal, without discounts or penalties. */
    public static final String MEAL_PRICE = "meal.price";

    /**
     * Students that buy a ticket on the same day of the meal, get a
     * higher price. This property sets the amount to be added to the
     * total of the meal price.
     */
    public static final String SAME_DAY_TAX = "meal.same_day_tax";

    /**
     * Scholarship students have a discount on each meal. This property
     * sets the currency amount to be subtracted from the total meal price.
     */
    public static final String SCHOLARSHIP_DISCOUNT = "meal.scholarship_discount";

    /** Database name to use. */
    public static final String DB_NAME = "db.name";

    /** Database username credential for authentication. */
    public static final String DB_USER = "db.user";

    /** Database password credential for authentication. */
    public static final String DB_PASS = "db.pass";

    /** System email address, used also as username for authentication. */
    public static final String MAIL_USER = "mail.user";

    /** System email password for SMTP authentication. */
    public static final String MAIL_PASS = "mail.pass";

    /** Reference to this object's instance. */
    protected static Config instance;

    /**
     * Gets reference to a statically initialized instance. Allows lazy loading.
     *
     * @return reference to a Config object instance.
     */
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config(DEFAULT_PATH);
        }
        return instance;
    }

    /**
     * Creates a new object instance. File path needed.
     * <p>
     * Only used when isolation needed, or to set a different path from the
     * default one. Use Config.getInstance() instead to reuse a single
     * instance.
     *
     * @param filePath relative or absolute path to the file.
     */
    public Config(String filePath) {
        super(filePath);
    }

    @Override
    protected void setDefaults() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        setInt(YEAR, currentYear);
        set(ACCOUNT_SEED, "1000");
        set(MEAL_PRICE, "2.40");
        set(SAME_DAY_TAX, "0.45");
        set(SCHOLARSHIP_DISCOUNT, "0.80");

        // database settings
        set(DB_NAME, "uacbd");
        set(DB_USER, "root");
        set(DB_PASS, "root");

        // email account
        set(MAIL_USER, "univent.uac@gmail.com");
        set(MAIL_PASS, "univent1112");

        // email authentication settings
        set("mail.smtp.host", "smtp.gmail.com");
        set("mail.smtp.socketFactory.port", "465");
        set("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        set("mail.smtp.auth", "true");
        set("mail.smtp.port", "465");
    }
}
