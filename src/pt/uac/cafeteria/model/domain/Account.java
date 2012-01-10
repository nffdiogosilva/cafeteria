
package pt.uac.cafeteria.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A student account in the cafeteria. It manages access to the system,
 * a balance for buying meals and has the list of transactions made.
 */
public class Account implements DomainObject<Integer>, java.io.Serializable {

    /**
     * Serialization version.
     *
     * @see http://www.mkyong.com/java-best-practices/understand-the-serialversionuid/
     */
    private static final long serialVersionUID = 1L;

    /** Minimum pin code value. */
    private final int MIN_VALUE = 1000;

    /** Maximum pin code value. */
    private final int MAX_VALUE = 9999;

    /** Starting balance for newly created accounts. */
    private final double STARTING_BALANCE = 5.0;

    /** Number of failed consecutive login attempts before blocking the account. */
    private final int MAX_FAILED_ATTEMPTS = 3;

    /** Enumerated type with the status of the account */
    public enum Status {
        /** Active account. Normal status. */
        ACTIVE { @Override public String toString() { return "Activa"; } },

        /** Blocked account. Student can't login. */
        BLOCKED { @Override public String toString() { return "Bloqueada"; } },

        /** Closed account. Old student, no longer studying. */
        CLOSED { @Override public String toString() { return "Fechada"; } }
    }

    /** Account id number. Same as the student id. */
    private int id;

    /** The access pin code. */
    private int pinCode;

    /** The account balance available. */
    private double balance;

    /** The account status. */
    private Status status;

    /** List of transactions associated with the account. */
    private List<Transaction> transactions;

    /** Recorded number of consecutive failed attempts at logging in. */
    private int failedLoginAttempts;

    /**
     * Creates a new Account, with auto-generated pin code.
     *
     * @param studendId the student id number, as an <code>int</code> in the java language.
     */
    public Account(int studentId) {
        this(new Integer(studentId));
    }

    /**
     * Creates a new Account, with auto-generated pin code.
     *
     * @param studentId the student id number.
     */
    public Account(Integer studentId) {
        this.id = studentId;
        this.pinCode = randomNumber(MIN_VALUE, MAX_VALUE);
        this.balance = STARTING_BALANCE;
        this.status = Status.ACTIVE;
        this.transactions = new ArrayList<Transaction>();
    }

    /** Generates a number between given boundaries. */
    private static int randomNumber(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }

    @Override
    public Integer getId() {
        return new Integer(id);
    }

    @Override
    public void setId(Integer id) {
        id = id.intValue();
    }

    /** Gets the current pin code. */
    public int getPinCode() {
        return pinCode;
    }

    /** Sets a new access pin code. */
    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    /** Gets the account balance */
    public double getBalance() {
        return balance;
    }

    /** Sets the account balance to a new amount. */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /** Gets the current account status. */
    public Status getStatus() {
        return status;
    }

    /** Sets account to a new status. */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Checks if the account is active. */
    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    /** Checks if the account is blocked. */
    public boolean isBlocked() {
        return status == Status.BLOCKED;
    }

    /** Checks if the account is closed. */
    public boolean isClosed() {
        return status == Status.CLOSED;
    }

    /** Blocks the account. Student won't be able to login. */
    public void block() {
        setStatus(Status.BLOCKED);
    }

    /** Sets the account back to active after being blocked. */
    public void unblock() {
        setStatus(Status.ACTIVE);
    }

    /** Closes the account. */
    public void close() {
        setStatus(Status.CLOSED);
    }

    /**
     * Tests if a given pin code is valid for this account.
     * <p>
     * After a predefined number of failed consecutive authentications,
     * the account gets blocked.
     * <p>
     * Only active accounts are allowed authentication.
     *
     * @param pinCode the pin code to validate.
     * @return true if pin code matches; false otherwise.
     */
    public boolean authenticate(int pinCode) {
        if (!isActive()) {
            return false;
        }
        if (this.pinCode == pinCode) {
            failedLoginAttempts = 0;
            return true;
        }
        if (++failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
            block();
            failedLoginAttempts = 0;
        }
        return false;
    }

    /**
     * Makes a deposit to the account, increasing balance.
     *
     * @param amount the amount being added to the balance.
     * @param administrator the administrator username doing the operation.
     */
    public void deposit(double amount, String administrator) {
        amount = Math.abs(amount);
        balance += amount;
        transactions.add(new Credit(administrator, amount));
    }

    /**
     * Buys a ticket for a meal.
     *
     * @param meal meal to buy a ticket to.
     * @param price price of the meal.
     * @throws IllegalArgumentException if not enough balance.
     */
    public void buyTicket(Meal meal, double price) {
        price = Math.abs(price);
        if (price > balance) {
            throw new IllegalArgumentException("Não possui saldo suficiente.");
        }
        balance -= price;
        transactions.add(new Ticket(meal, price));
    }

    /** Gets a list of the account's transactions. */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public String toString() {
        return "Account {"
            + "\n  id = " + getId()
            + "\n  pinCode = " + getPinCode()
            + "\n  balance = " + getBalance()
            + "\n  status = " + getStatus()
            + "\n  transactions = " + getTransactions()
            + "\n}"
        ;
    }
}
