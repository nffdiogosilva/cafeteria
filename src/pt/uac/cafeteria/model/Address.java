
package pt.uac.cafeteria.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with simple address fields.
 */
public class Address {

    /** Street address. */
    private String streetAddress;

    /** Postal code. */
    private String postalCode;

    /** City. */
    private String city;

    /**
     * Factory method.
     *
     * @param streetAddress  street address
     * @param postalCode     postal code
     * @param city           city
     * @return               Address object
     */
    public static Address build(String streetAddress, String postalCode, String city) {
        Address address = new Address();

        address.setStreetAddress(streetAddress);
        address.setPostalCode(postalCode);
        address.setCity(city);

        return address;
    }

    /** Returns the street address. */
    public String getStreetAddress() {
        return streetAddress;
    }

    /** Sets the street address to a new value. */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /** Returns the postal code. */
    public String getPostalCode() {
        return postalCode;
    }

    /** Sets postal code to a new value. */
    public void setPostalCode(String postalCode) {
        if (!this.postalCodeIsValid(postalCode)) {
            throw new IllegalArgumentException("Codigo postal invalido");
        }
        this.postalCode = postalCode;
    }

    /** Returns the city. */
    public String getCity() {
        return city;
    }

    /** Sets city to a new value. */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Checks if postal code is in a valid format.
     *
     * Accepted: Portuguese format (e.g.: 9600-508).
     *
     * @param postalCode  postal code string to validate.
     * @return            true if in right format; false otherwise.
     */
    public boolean postalCodeIsValid(String postalCode){
        final String POSTAL_CODE_PATTERN = "^[0-9]{4}-[0-9]{3}$";
        Pattern pattern = Pattern.compile(POSTAL_CODE_PATTERN);
        Matcher matcher = pattern.matcher(postalCode);
        return matcher.matches();
    }
}
