
package pt.uac.cafeteria.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * Represents the student account
 *
 */
public class Account {

    /** Constant with maximum value to generate a number */
    private static final int MAX_VALUE = 9999;

    /** Constant with minimum value to generate a number*/
    private static final int MIN_VALUE = 1000;

    /** Enumerated type with the status of the account */
    public enum Status {
        ACTIVE { @Override public String toString() { return "Activa"; } },
        BLOCKED { @Override public String toString() { return "Bloqueada"; } },
        CLOSED { @Override public String toString() { return "Fechada"; } }
    }

    /** Account number */
    private int number;

    /** Account pin code */
    private int pinCode;

    /** Account balance */
    private double balance;

    /** Account status */
    private Status status;

    /** Account transactions list */
    private List<Transaction> transactions;

    /** Recorded number of failed attempts at logging in */
    private int failedAttempts;

    /**
     * Default Constructor
     *
     * @param studendId   Student id
     */
    public Account(int studentId) {
        this.number = studentId;
        this.pinCode = randomNumber();
        this.balance = 5.0;
        this.status = Status.ACTIVE;
        this.transactions = new ArrayList();
    }


    /** Returns the account number */
    public int getNumber() {
        return number;
    }

    /**
     * Changes the pin code
     *
     * @param pinCode   the pin code that will be defined
     */
    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    /** Returns the account balance */
    public double getBalance() {
        return balance;
    }

    /**
     * Changes the balance
     *
     * @param balance   the balance that will be defined
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /** Returns the account status */
    public Status getStatus() {
        return status;
    }

    /** Checks if account is active */
    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    /** Checks if account is blocked */
    public boolean isBlocked() {
        return status == Status.BLOCKED;
    }

    /** Checks if account is closed */
    public boolean isClosed() {
        return status == Status.CLOSED;
    }

    /**
     * Authenticate account
     *
     * Gets blocked after three failed attempts
     *
     * @param pinCode  Account pin code
     * @return
     */
    public boolean authenticate(int pinCode) {
        if (this.pinCode == pinCode) {
            failedAttempts = 0;
            return true;
        }
        if (++failedAttempts == 3) {
            block();
        }
        return false;
    }

    /**
     * Changes the state.
     *
     * @param status    the status that will be defined
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Returns account transactions list */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** Returns a string that describe the account */
    @Override
    public String toString(){
        return "\nNumber: " + this.number +
                "\nPin Code: " + this.pinCode +
                "\nBalance: " + this.balance +
                "\nStatus: " + this.status +
                "\nTransactions: " + this.transactions;
    }

    /**
     * Method responsible for making a deposit and adding it into the transactions list
     *
     * @param amount    the amount that is deposit into the account
     * @param administrator the administrator that does the deposit
     */
    public void deposit (double amount, String administrator) {
        if (amount < 0) {
            amount = -amount;
        }
        this.balance = balance + amount;
        this.transactions.add(new Credit(administrator, amount));
    }

    /**
     * Method responsible for making a payment and adding it into the transactions list
     *
     * @param amount    the amount of the payment
     * @param date  the date of the meal
     * @param meal  the type of meal: dinner or lunch
     * @throws Exception    exception that leads with not enough credit
     */
    public void payment (double amount, Calendar date, Meal.Time meal) {
        if (amount <= this.balance) {
            this.balance = balance - amount;
            this.transactions.add(new Debit(date, meal));
        }
        else {
            throw new IllegalArgumentException("Nao possui credito suficiente");
        }
    }

    /**
     * Method responsible for recovering the active state of the account
     *
     * @param pinCode   the pin code of a student
     * @throws Exception    exceptions that leads with account not being blocked or having an invalid pin code
     */
    public void recoverAccountState(int pinCode) {
        if (this.pinCode == pinCode) {
            if (this.getStatus() == Status.BLOCKED) {
                setStatus(Status.ACTIVE);
            }
            else {
                throw new IllegalArgumentException("Conta nao esta bloqueada");
            }
        }
        else {
            throw new IllegalArgumentException("Codigo pin invalido");
        }
    }


    /**
     *
     * Method responsible for recovering the pin code of an account
     *
     * @param id    the id of a student, to check if he's the real pin code owner
     * @return  the pin code of the account
     * @throws Exception    exception that leads with invalid id
     */
    public int recoverAccountPinCode(Student student, int id) {
        if (student.getId() == id) {
            return this.pinCode;
        }
        throw new IllegalArgumentException("Identificacao do estudante invalida");
    }

    /** Sets the BLOCKED state of the account */
    public void block() {
        this.setStatus(Status.BLOCKED);
    }
    
    /** Sets the ACTIVE state of the account */
    public void unblock() {
        this.setStatus(Status.ACTIVE);
    }
    
    /** Sets the CLOSED state of the account */
    public void close() {
        this.setStatus(Status.CLOSED);
    }
    
    /** Alters the email of the Student */
    public void updateEmail(Student student, String email) {
        student.setEmail(email);
    }

    /** Generates a number between 1000 and 9999 */
    private static int randomNumber() {
        return MIN_VALUE + (int)(Math.random() * (MAX_VALUE - MIN_VALUE));
    }
}
