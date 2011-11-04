
package pt.uac.cafeteria.model;

import org.junit.Test;
import static org.junit.Assert.*;


public class AdministratorTest {

    @Test
    public void canCreateAdministrator() {
        Administrator admin = new Administrator("Jon Doe", "jondoe", "password");

        assertEquals("Jon Doe", admin.getName());
        assertEquals("jondoe", admin.getUsername());
        assertTrue(admin.isPasswordValid("password"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseLessThanSixCharsUsername() {
        new Administrator("Jon Doe", "short", "password");
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseMoreThanTenCharsUsername() {
        new Administrator("Jon Doe", "long_jondoe", "password");
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseLessThanEightCharsPassword() {
        new Administrator("Jon Doe", "jondoe", "secrest");
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseMoreThanFifteenCharsPassword() {
        new Administrator("Jon Doe", "jondoe", "verylongpassword");
    }
}