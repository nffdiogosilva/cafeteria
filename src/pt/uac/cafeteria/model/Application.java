
package pt.uac.cafeteria.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Application core
 * 
 * Central hub from where application scope objects come
 */
public class Application {

    /** Default administrator username */
    private final String DEFAULT_ADMIN_NAME = "administrador";

    /** Default administrator password */
    private final String DEFAULT_ADMIN_PASSWORD = "12345678";

    /** Map with administrator accounts */
    private Map<String, Administrator> administrators = new HashMap<String, Administrator>();

    /** Map with student accounts */
    private Map<String, Account> accounts = new HashMap<String, Account>();
    
    /**
     * Constructor
     * 
     * A default administrator account is created at instantiation
     */
    public Application() {
        Administrator default_admin = new Administrator(DEFAULT_ADMIN_NAME, DEFAULT_ADMIN_PASSWORD);
        administrators.put(DEFAULT_ADMIN_NAME, default_admin);
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
     * Adds an account to the application
     * 
     * @param account  Student account
     */
    public void addAccount(Account account) {
        accounts.put(String.valueOf(account.getNumber()), account);
    }

    /**
     * Gets a student account.
     * 
     * @param accountNumber  Account process number
     * @return  Student account object
     */
    public Account getAccount(int accountNumber) {
        return accounts.get(String.valueOf(accountNumber));
    }

    /**
     * Authenticates a Student using his account.
     * 
     * Three failed attempts blocks the account.
     * 
     * @param accountNumber  Account process number
     * @param pinCode  Account pin code
     * @return  Student account object
     */
    public Account getAccount(int accountNumber, int pinCode) {
        Account account = getAccount(accountNumber);
        
        if (account != null && account.authenticate(pinCode)) {
            return account;
        }
        
        return null;
    }
}
