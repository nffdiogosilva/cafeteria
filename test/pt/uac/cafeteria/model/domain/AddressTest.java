
package pt.uac.cafeteria.model.domain;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value=Parameterized.class)
public class AddressTest {

    private String street;
    private String postalCode;
    private String city;

    @Parameters
    public static Collection<String[]> getWrongParameters() {
        return Arrays.asList(new String[][] {
            {"Street", "9000", "City"},
            {"Street", "invalid", "City"},
            {"Street", "304-9943", "City"},
            {"Street", "9000 600", "City"},
        });
    }

    public AddressTest(String street, String postalCode, String city) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidData() {
        Address.build(street, postalCode, city);
    }
}
