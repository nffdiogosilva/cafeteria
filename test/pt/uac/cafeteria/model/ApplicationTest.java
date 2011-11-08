
package pt.uac.cafeteria.model;

import java.util.GregorianCalendar;
import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationTest {

    private final Application app = new Application();
    private final Calendar testDay = new GregorianCalendar(2011, 11, 11);

    private final int testAccountNumber = 20112543;

    private MealMenu createMealMenu(Calendar day, Meal.Time time) {
        return MealMenu.Builder(day, time)
                .setMeatCourse("Pork")
                .setFishCourse("Tuna")
                .setVeggieCourse("Soy")
                .setSoupAndDesert("Vegetables", "Cake")
                .build();
    }

    private void addStudentAccount() {
        Account mockAccount = new Account(new Student(testAccountNumber));
        mockAccount.setPinCode(1234);
        app.addAccount(mockAccount);
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
        addStudentAccount();
        Account account = app.getAccount(testAccountNumber);
        assertNotNull(account);
    }

    @Test
    public void studentCanAccess() {
        addStudentAccount();
        Account account = app.getAccount(testAccountNumber, 1234);
        assertNotNull(account);
    }

    @Test
    public void studentCantAccessWithInvalidCredentials() {
        addStudentAccount();
        Account account = app.getAccount(testAccountNumber, 4321);
        assertNull(account);
    }

    @Test
    public void blockAccountAfterThreeFailedAttempts() {
        addStudentAccount();

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
        addStudentAccount();

        Account account = app.getAccount(testAccountNumber);
        Student student = account.getStudent();
        app.deleteAccount(account.getNumber());

        assertNull(app.getAccount(testAccountNumber));
        assertEquals(student, app.getOldStudent(testAccountNumber));
    }

   @Test
   public void canAddMealMenu() {
       MealMenu menu = createMealMenu(testDay, Meal.Time.LUNCH);
       app.addMealMenu(menu);
       
       assertEquals(menu, app.getMealMenu(testDay, Meal.Time.LUNCH));
   }
}
