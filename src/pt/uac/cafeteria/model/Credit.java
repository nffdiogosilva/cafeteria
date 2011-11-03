
package pt.uac.cafeteria.model;

import java.util.Calendar;

/**
 *
 * Represents the credit transaction
 *
 */
public class Credit extends Transaction {

    /** Administrator responsible of the credit transaction */
    private String administrator;


    /**
     * Default Constructor
     *
     * @param administrator the administrator that does the credit transaction
     * @param amount    the amount that will be credited
     */
    public Credit(String administrator, double amount) {
        super.setDate(Calendar.getInstance());
        super.setAmount(amount);
        this.administrator = administrator;
    }

    /** Returns the administrator responsible of the credit transaction */
    public String getAdministrator() {
        return administrator;
    }
}
