
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
    private Map<String, Administrator> administrators;

    /**
     * Constructor
     * 
     * A default administrator account is created at instantiation
     */
    public Application() {
        Administrator default_admin = new Administrator(DEFAULT_ADMIN_NAME, DEFAULT_ADMIN_PASSWORD);

        administrators = new HashMap<String, Administrator>();
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
}
