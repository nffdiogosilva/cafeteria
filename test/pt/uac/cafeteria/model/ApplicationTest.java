
package pt.uac.cafeteria.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationTest {
    private Application app;

    private final int testAccountNumber = 20112543;

    class MockAccount extends Account {
        MockAccount(int pin) {
            super(new MockStudent(testAccountNumber));
            this.setPinCode(pin);
        }
    }
    class MockStudent extends Student {
        MockStudent(int number) {
            super(number);
        }
    }


    @Before
    public void setUp() throws Exception {
        app = new Application();
    }

    private void createStudentAccount() {
        app.addAccount(new MockAccount(1234));
    }


    @Test
    public void canAccessWithDefaultAdminCredentials() {
        Administrator admin = app.getAdministrator("administrador", "12345678");
        assertNotNull(admin);
    }

    @Test
    public void cantAccessWithInvalidAdminCredentials() {
        Administrator impostor = app.getAdministrator("administrador", "password");
        assertNull(impostor);
    }

    @Test
    public void canAddAdministrator() {
        Administrator expected = new Administrator("Jon Doe", "newadmin", "hisSecret");
        app.addAdministrator(expected);
        Administrator actual = app.getAdministrator("newadmin", "hisSecret");
        assertEquals(expected, actual);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantAddAdministratorWithExistingUsername() {
        app.addAdministrator(new Administrator());
    }

    @Test
    public void canAddStudentAccount() {
        createStudentAccount();
        Account account = app.getAccount(testAccountNumber);
        assertNotNull(account);
    }

    @Test
    public void studentCanAccess() {
        createStudentAccount();
        Account account = app.getAccount(testAccountNumber, 1234);
        assertNotNull(account);
    }

    @Test
    public void studentCantAccessWithInvalidCredentials() {
        createStudentAccount();
        Account account = app.getAccount(testAccountNumber, 4321);
        assertNull(account);
    }

    @Test
    public void blockAccountAfterThreeFailedAttempts() {
        createStudentAccount();

        app.getAccount(testAccountNumber);
        Account account = app.getAccount(testAccountNumber);
        assertTrue(account.isActive());
        assertFalse(account.isBlocked());

        // Attempt 1
        app.getAccount(testAccountNumber, 1928);
        account = app.getAccount(testAccountNumber);
        assertTrue(account.isActive());

        // Attempt 2
        app.getAccount(testAccountNumber, 7498);
        account = app.getAccount(testAccountNumber);
        assertTrue(account.isActive());

        // Attempt 3
        app.getAccount(testAccountNumber, 9745);
        account = app.getAccount(testAccountNumber);
        assertFalse(account.isActive());
        assertTrue(account.isBlocked());
    }

    @Test
    public void canDeleteStudentAccount() {
        createStudentAccount();

        Account account = app.getAccount(testAccountNumber);
        Student student = account.getStudent();
        app.deleteAccount(account.getNumber());

        assertNull(app.getAccount(testAccountNumber));
        assertEquals(student, app.getOldStudent(testAccountNumber));
    }
}
