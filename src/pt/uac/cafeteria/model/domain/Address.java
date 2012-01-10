
package pt.uac.cafeteria.model.domain;

/**
 * Class with simple address fields.
 */
public class Address implements DomainObject<Integer> {

    /** Unique identifier for the address. */
    private Integer id;

    /** Street address. */
    private String streetAddress;

    /**
     * Door number, and optionally the apartment number too.
     * <p>
     * Example: 12 - 1ºDto
     */
    private String number;

    /** Postal code. */
    private String postalCode;

    /** City. */
    private String city;

    /**
     * Creates a new Address instance.
     *
     * @param streetAddress the street address.
     * @param number the door number, and optionally apartment number too.
     * @param postalCode the postal code.
     * @param city the city.
     */
    public Address(String streetAddress, String number, String postalCode, String city) {
        this.streetAddress = streetAddress;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /** Returns the street address. */
    public String getStreetAddress() {
        return streetAddress;
    }

    /** Sets the street address to a new value. */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /** Gets the door number. */
    public String getNumber() {
        return number;
    }

    /** Sets a new door number. */
    public void setNumber(String number) {
        this.number = number;
    }

    /** Returns the postal code. */
    public String getPostalCode() {
        return postalCode;
    }

    /** Sets postal code to a new value. */
    public void setPostalCode(String postalCode) {
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

    @Override
    public String toString() {
        return "Address {"
            + "\n    id = " + getId()
            + "\n    streetAddress = " + getStreetAddress()
            + "\n    number = " + getNumber()
            + "\n    postalCode = " + getPostalCode()
            + "\n    city = " + getCity()
            + "\n  }";
    }
}
