
package pt.uac.cafeteria.model.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {

    private final double moneyPrecision = 0.001;
    private final int testAccountNumber = 20112543;

    private final Administrator admin = new Administrator("Jon Doe", "jondoe", "password");
    private final Student mockStudent = new Student(testAccountNumber);
    private final Account testAccount = new Account(mockStudent);

    @Test
    public void canCreateAccount() {
        assertEquals(testAccountNumber, testAccount.getNumber());
        assertTrue(testAccount.isActive());
        assertEquals(5.0, testAccount.getBalance(), moneyPrecision);
        assertEquals(mockStudent, testAccount.getStudent());
    }

    @Test
    public void canMakeDeposit() {
        testAccount.deposit(10.0, admin.getUsername());
        assertEquals(15.0, testAccount.getBalance(), moneyPrecision);
    }

    @Test
    public void cantMakeDepositWithNegativeAmount() {
        testAccount.deposit(-5, admin.getUsername());
        assertEquals(10.0, testAccount.getBalance(), moneyPrecision);
    }

    @Test
    public void canRecoverPinCode() {
        testAccount.setPinCode(5544);
        assertTrue(testAccount.authenticate(5544));
    }

    @Test
    public void blockAccountAfterThreeConsecutiveFailedAuthenticationAttempts() {
        testAccount.setPinCode(1234);
        assertEquals(Account.Status.ACTIVE, testAccount.getStatus());

        // Attempt 1
        testAccount.authenticate(1928);
        assertTrue(testAccount.isActive());

        // Attempt 2
        testAccount.authenticate(7498);
        assertTrue(testAccount.isActive());

        // Attempt 3
        testAccount.authenticate(9745);
        assertEquals(Account.Status.BLOCKED, testAccount.getStatus());
    }

    @Test
    public void canBlockAndUnblockAccount() {
        testAccount.block();
        assertEquals(Account.Status.BLOCKED, testAccount.getStatus());

        testAccount.unblock();
        assertEquals(Account.Status.ACTIVE, testAccount.getStatus());
    }

    @Test
    public void canCloseAccount() {
        testAccount.close();
        assertEquals(Account.Status.CLOSED, testAccount.getStatus());
    }

    @Test
    public void canUpdateEmail() throws Exception {
        String testEmail = "mail@example.com";
        testAccount.updateEmail(testEmail);
        assertEquals(testEmail, testAccount.getStudent().getEmail());
    }
}
