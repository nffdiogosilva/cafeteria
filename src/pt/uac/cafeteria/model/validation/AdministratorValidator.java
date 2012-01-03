
package pt.uac.cafeteria.model.validation;

import pt.uac.cafeteria.model.domain.Administrator;

/**
 * Validates Administrator domain objects.
 * <p>
 * It's possible to define different boundaries for valid username and
 * password lengths at runtime.
 */
public class AdministratorValidator extends Validator<Administrator> {

    /** The minimum number of chars for the username. */
    private int user_min_len = 6;

    /** The maximum number of chars for the username. */
    private int user_max_len = 10;

    /** The minimum number of chars for the password. */
    private int pass_min_len = 8;

    /** The maximum number of chars for the password. */
    private int pass_max_len = 15;

    /**
     * Sets different boundaries for the number of chars in the username.
     *
     * @param min a new minimum number of chars.
     * @param max a new maximum number of chars.
     */
    public void setUsernameBounds(int min, int max) {
        user_min_len = min;
        user_max_len = max;
    }

    /**
     * Sets different boundaries for the number of chars in the password.
     *
     * @param min a new minimum number of chars.
     * @param max a new maximum number of chars.
     */
    public void setPasswordBounds(int min, int max) {
        pass_min_len = min;
        pass_max_len = max;
    }

    /** Gets the currently defined boundaries for the username length. */
    public int[] getUsernameBounds() {
        return new int[]{user_min_len, user_max_len};
    }

    /** Gets the currently defined boundaries for the password length. */
    public int[] getPasswordBounds() {
        return new int[]{pass_min_len, pass_max_len};
    }

    @Override
    protected void doAssertions(Administrator admin) {
        assertRequired("nome", admin.getName());

        if (assertRequired("username", admin.getUsername())) {
            assertUsername(admin.getUsername());
        }
        if (assertRequired("password", admin.getPassword())) {
            assertPassword(admin.getPassword());
        }
    }

    /**
     * Asserts if a username length is within predefined boundaries.
     *
     * @param user the username being tested.
     */
    protected void assertUsername(String user) {
        boolean bounds = inRange(user_min_len, user_max_len, user.length());
        check(bounds, String.format(
            "O username deve ter um comprimento entre %d e %d.",
            user_min_len, user_max_len
        ));
    }

    /**
     * Asserts if a password length is within predefined boundaries.
     *
     * @param pass the password being tested.
     */
    protected void assertPassword(String pass) {
        boolean bounds = inRange(pass_min_len, pass_max_len, pass.length());
        check(bounds, String.format(
            "A password deve ter um comprimento entre %d e %d.",
            pass_min_len, pass_max_len
        ));
    }
}
