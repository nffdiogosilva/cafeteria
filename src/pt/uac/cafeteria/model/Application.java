
package pt.uac.cafeteria.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import pt.uac.cafeteria.model.domain.Administrator;
import pt.uac.cafeteria.model.domain.Student;
import pt.uac.cafeteria.model.domain.Account;
import java.util.HashMap;
import java.util.Map;

/**
 * Application core
 *
 * Central hub from where application scope objects come
 */
public class Application {

    /** Default administrator username */
    private final String DEFAULT_ADMIN_USERNAME = "superadmin";

    /** Default administrator password */
    private final String DEFAULT_ADMIN_PASSWORD = "12345678";

    /** Map with administrator accounts */
    private final Map<String, Administrator> administrators = new HashMap<String, Administrator>();

    /** Map with student accounts */
    private final Map<Integer, Student> students = new HashMap<Integer, Student>();

    /** Map with old students that no longer have an account */
    private final Map<Integer, Student> oldStudents = new HashMap<Integer, Student>();

    /** Reusable database connection. */
    private static Connection db;

    /**
     * Constructor
     *
     * A default administrator account is created at instantiation
     */
    public Application() {
        Administrator default_admin = new Administrator(
            "Administrador", DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD
        );
        administrators.put(default_admin.getUsername(), default_admin);
    }

    /**
     * Inserts a new administrator account in the application
     *
     * @param admin  Administrator object
     */
    public void addAdministrator(Administrator admin) {
        if (administrators.get(admin.getUsername()) != null) {
            throw new IllegalArgumentException(
                String.format("Conta de administrador com o username '%s' já ocupada.", admin.getUsername())
            );
        }
        administrators.put(admin.getUsername(), admin);
    }

    /**
     * Authenticates an Administrator
     *
     * @param username  Administrator username
     * @param password  Administrator password
     * @return  Administrator object, or null if invalid
     */
    public Administrator getAdministrator(String username, String password) {
        Administrator admin = administrators.get(username);

        if (admin != null && admin.isPasswordValid(password)) {
            return admin;
        }

        return null;
    }

    /**
     * Adds a Student to the application
     *
     * @param student  Student
     */
    public void addStudent(Student student) {
        students.put(new Integer(student.getId()), student);
    }

    /**
     * Gets a student.
     *
     * @param accountNumber  Student's account process number
     * @return  Student student object
     */
    Student getStudent(int accountNumber) {
        return students.get(new Integer(accountNumber));
    }

    /**
     * Authenticates a Student using his account.
     *
     * Three failed attempts blocks the account.
     *
     * @param accountNumber  Account process number
     * @param pinCode  Account pin code
     * @return  Student account object, or null if does not authenticate
     */
    public Student getStudent(int accountNumber, int pinCode) {
        Student student = getStudent(accountNumber);
        Account account = student.getAccount();

        if (account != null && account.authenticate(pinCode)) {
            return student;
        }

        return null;
    }

    /**
     * Deletes a student 
     *
     * Student gets moved to an historic of students (old students)
     *
     * @param accountNumber  Account or student process number
     */
    public void deleteStudent(int accountNumber) {
        Integer studentNumber = new Integer(accountNumber);
        Student student = students.get(studentNumber);

        if (student != null) {
            students.remove(studentNumber);
            oldStudents.put(studentNumber, student);
        }
    }

    /**
     * Gets an old student from the historic
     *
     * @param studentNumber  Student process number
     * @return   Student object, or null if non-existent
     */
    public Student getOldStudent(int studentNumber) {
        return oldStudents.get(new Integer(studentNumber));
    }

    /**
     * Gets reference to reusable database connection, using credentials
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
     * Creates a new database connection using credentials stored in Config.
     *
     * @return database connection.
     * @throws ApplicationException if unable to create connection.
     */
    private static Connection initDBConnection() {
        Config config = Config.getInstance();
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
}
