
package pt.uac.cafeteria.model;

import java.sql.*;
import java.util.Calendar;
import pt.uac.cafeteria.model.domain.*;
import pt.uac.cafeteria.model.persistence.*;

/**
 * Application gateway.
 * <p>
 * Provides application initialization and gateway methods as a convenience
 * for units of work.
 */
public class Application {

    /** Default administrator username */
    private static final String DEFAULT_ADMIN_USERNAME = "superadmin";

    /** Default administrator password */
    private static final String DEFAULT_ADMIN_PASSWORD = "12345678";

    /** Global configuration object instance. */
    private static Config config = Config.getInstance();

    /** Reusable database connection. */
    private static Connection db;

    /** Application initialization. */
    public static void init() {
        initDBConnection();
        checkDefaultAdminAccount();
        MapperRegistry.account().loadAll();
    }

    /** Application finalization. Must be run on exit. */
    public static void close() {
        MapperRegistry.account().save();
    }

    /**
     * Creates a new database connection using credentials stored in Config.
     *
     * @return a database connection.
     * @throws ApplicationException if unable to create connection, or
     * db driver can't be loaded.
     */
    private static Connection initDBConnection() {
        String dbname = config.get(Config.DB_NAME);
        String dbuser = config.get(Config.DB_USER);
        String dbpass = config.get(Config.DB_PASS);
        String url = "jdbc:mysql://localhost/" + dbname;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, dbuser, dbpass);
        } catch (ClassNotFoundException e) {
            throw new ApplicationException("Falha ao carregar driver mysql.", e);
        } catch (SQLException e) {
            throw new ApplicationException("Problema em ligar à base de dados.", e);
        }
    }

    /**
     * Gets a reference to reusable database connection, using credentials
     * stored on Config object.
     *
     * @return database connection.
     */
    public static Connection getDBConnection() {
        if (db == null) {
            db = initDBConnection();
        }
        return db;
    }

    /**
     * Checks if default administrator account exists,
     * and creates it if not found.
     */
    private static void checkDefaultAdminAccount() {
        AdministratorMapper adminMapper = MapperRegistry.administrator();
        if (adminMapper.findByUsername(DEFAULT_ADMIN_USERNAME) == null) {
            Administrator defaultAdmin = new Administrator(
                "Administrador", DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD
            );
            adminMapper.insert(defaultAdmin);
        }
    }

    /**
     * Authenticates an Administrator, against a password.
     *
     * @param username the Administrator username.
     * @param password the Administrator password.
     * @return Administrator object, or null if invalid.
     */
    public static Administrator authenticateAdmin(String username, String password) {
        Administrator admin = MapperRegistry.administrator().findByUsername(username);

        if (admin != null && admin.authenticate(password)) {
            return admin;
        }

        return null;
    }

    /**
     * Creates a new Student and Account.
     * <p>
     * The account seed in config is auto-incremented.
     *
     * @param student the Student object.
     * @return The new student's id number.
     */
    public static Integer createStudent(Student student) {
        Integer id = student.getId();
        if (id == null) {
            id = getNextStudentId();
            student.setId(id);
        }
        if (MapperRegistry.student().insert(student) != null) {
            incrementStudentId();
            //sendNoticeOfNewAccount(student);
            return id;
        }
        return null;
    }

    /** Gets next student id number to be used. */
    public static Integer getNextStudentId() {
        String id = config.get(Config.YEAR) + config.get(Config.ACCOUNT_SEED);
        return Integer.valueOf(id);
    }

    /** Increments current account seed for student id number generation. */
    public static void incrementStudentId() {
        int currentSeed = config.getInt(Config.ACCOUNT_SEED);
        config.setInt(Config.ACCOUNT_SEED, ++currentSeed);
    }

    /**
     * Sends notice to student of newly created account.
     *
     * @param student the Student object.
     */
    public static void sendNoticeOfNewAccount(Student student) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Closes a student account.
     * <p>
     * The student is moved to a different list of old students, and the
     * account is closed.
     *
     * @param student the student to close the account.
     */
    public static void closeStudentAccount(Student student) {
        // move student from active list to historic list
        MapperRegistry.oldStudent().insert(student);
        MapperRegistry.student().delete(student);

        // close account
        student.getAccount().close();
        MapperRegistry.account().update(student.getAccount());
    }

    /**
     * Authenticates a Student against a pin code.
     *
     * @param accountNumber the Account process number or student id.
     * @param pinCode the Account pin code.
     * @return Student account object, or null if does not authenticate.
     * @throws ApplicationException if the account gets blocked after a certain
     * amount of successive failed login attempts.
     */
    public static Student authenticateStudent(int accountNumber, int pinCode) {
        Account account = MapperRegistry.account().find(accountNumber);

        if (account == null && account.authenticate(pinCode)) {
            return MapperRegistry.student().find(accountNumber);
        }

        if (account.isBlocked()) {
            throw new ApplicationException("Conta bloqueada!");
        }

        return null;
    }

    /**
     * Calculates the price of a meal for a student.
     *
     * @param meal the meal being bought.
     * @param student the student buying.
     * @return the cost of the meal for the student.
     */
    public static double mealPrice(Meal meal, Student student) {
        double price = config.getDouble(Config.MEAL_PRICE);

        if (isSameDay(meal.getDay(), Calendar.getInstance())) {
            price += config.getDouble(Config.SAME_DAY_TAX);
        }

        if (student.hasScholarship()) {
            price -= config.getDouble(Config.SCHOLARSHIP_DISCOUNT);
        }

        return price;
    }

    /**
     * Checks if two calendars represent the same day ignoring time.
     *
     * @param day1 the first day.
     * @param day2 the second day.
     * @return <code>true</code> if they represent the same day.
     */
    public static boolean isSameDay(Calendar day1, Calendar day2) {
        return (
            day1.get(Calendar.ERA) == day2.get(Calendar.ERA) &&
            day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR) &&
            day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR)
        );
    }
}
