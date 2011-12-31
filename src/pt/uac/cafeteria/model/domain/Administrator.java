
package pt.uac.cafeteria.model.domain;

/**
 * Administrator account.
 *
 * Used for privileged access in the application.
 */
public class Administrator implements DomainObject<Integer> {

    /** Minimum accepted length for the username */
    private final int USER_MIN_LEN = 6;

    /** Maximum accepted length for the username */
    private final int USER_MAX_LEN = 10;

    /** Minimum accepted length for the password */
    private final int PASS_MIN_LEN = 8;

    /** Maximum accepted length for the password */
    private final int PASS_MAX_LEN = 15;

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
        if (username.length() < USER_MIN_LEN || username.length() > USER_MAX_LEN) {
            throw new IllegalArgumentException(
                String.format("O username deve ter um comprimento entre %d e %d.", USER_MIN_LEN, USER_MAX_LEN)
            );
        }
        if (password.length() < PASS_MIN_LEN || password.length() > PASS_MAX_LEN) {
            throw new IllegalArgumentException(
                String.format("A password deve ter um comprimento entre %d e %d.", PASS_MIN_LEN, PASS_MAX_LEN)
            );
        }
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
     * Updates the name
     * @param name  new full name
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
    public boolean isPasswordValid(String password) {
        return this.password.equals(password);
    }
}
