
package pt.uac.cafeteria.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a address of a student.
 */

public class Address {

    /** The street of a student */

    private String streetAddress;

    /** The Postal Code of a student */
    private String postalCode;

    /** The City of a student */

    private String city;

    /**
     * Default constructor.
     *
     * @param streetAddress street of a student
     * @param postalCode    postal code of a student
     * @param city  city of a student
     * @throws Exception    exception that leads with invalid postal code argument
     */

    public Address(String streetAddress, String postalCode, String city) throws Exception {
        this.streetAddress = streetAddress;
        if (this.postalCodeIsValid(postalCode)) {
            this.postalCode = postalCode;
        }
        else {
            throw new Exception ("INVALID POSTAL CODE FORMAT");
        }
        
        this.city = city;
    }

    /** Returns the street of a student */

    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Changes the street address.
     * 
     * @param streetAddress street address that will be defined
     */

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /** Returns the postal code of a student */

    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Changes the postal code.
     *
     * @param postalCode postal code that will be defined
     * @throws Exception exception that leads with invalid postal code format
     */

    public void setPostalCode(String postalCode) throws Exception {
        if (this.postalCodeIsValid(postalCode)) {
            this.postalCode = postalCode;
        }
        else {
               throw new Exception ("INVALID POSTAL CODE FORMAT");
        }
    }

    /** Returns the city of a student */

    public String getCity() {
        return city;
    }

    /**
     * Changes the city.
     *
     * @param city the city that will be defined
     */

    public void setCity(String city) {
        this.city = city;
    }

    /** Returns a string that describe the address of a student */

    @Override
    public String toString() {
        return "\nStreet Address: " + this.streetAddress +
                "\nPostal Code: " + this.postalCode +
                "\nCity: " + this.city;
    }

    /**
     * Method that checks if the postal code has the right format
     *
     * @param postalCode the email that will be checked
     * @return Returns a boolean. True if the postal code has the right format. False if the postal code has the wrong format
     */

    public boolean postalCodeIsValid(String postalCode){
        final String POSTAL_CODE_PATTERN = "^[0-9]{4}-[0-9]{3}$";
        Pattern pattern = Pattern.compile(POSTAL_CODE_PATTERN);
        Matcher matcher = pattern.matcher(postalCode);
        return matcher.matches();
    }
}
