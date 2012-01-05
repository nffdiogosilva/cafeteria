
package pt.uac.cafeteria.model.domain;

/**
 * Administrator account.
 * <p>
 * Used for privileged access in the application.
 */
public class Administrator implements DomainObject<Integer> {

    /** The admin's unique id. */
    private Integer id;

    /** The person's full name */
    private String name;

    /** The username */
    private String username;

    /** The password */
    private String password;

    /**
     * Public constructor.
     *
     * @param name  Full name of the person with the admin role
     * @param username  Username between 6 and 10 chars
     * @param password  Password between 8 and 15 chars
     */
    public Administrator(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /** Returns the person's name */
    public String getName() {
        return name;
    }

    /**
     * Updates the name.
     *
     * @param name new full name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the username */
    public String getUsername() {
        return username;
    }

    /** Returns the password */
    public String getPassword() {
        return password;
    }

    /** Sets a new password */
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Tests if a given password matches this one.
     *
     * @param password  password to test
     * @return  true if passwords are equal; false otherwise
     */
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
